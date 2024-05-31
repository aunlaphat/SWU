package com.arg.swu.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.ResponseMoodleData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.FinanceImportDetail;
import com.arg.swu.models.FinanceImportLog;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.models.SysMoodle;
import com.arg.swu.models.SysPrefixname;
import com.arg.swu.models.SysWebhookKbank;
import com.arg.swu.models.TmpFinanceImportDetail;
import com.arg.swu.models.TmpFinanceImportLog;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.FinanceImportDetailRepository;
import com.arg.swu.repositories.FinanceImportLogRepository;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.MemberInfoRepository;
import com.arg.swu.repositories.SysMoodleRepository;
import com.arg.swu.repositories.SysPrefixnameRepository;
import com.arg.swu.repositories.SysWebhookKbankRepository;
import com.arg.swu.repositories.TmpFinanceImportDetailRepository;
import com.arg.swu.repositories.TmpFinanceImportLogRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class FinancePaymentProcess implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(FinancePaymentProcess.class);

	private final FileStorageService fileStorageService;
	private final JmsSender jmsSender;
	
	private final ScheduleService scheduleService;
	
	private final CoursepublicMainRepository coursepublicMainRepository;
	private final FinancePaymentRepository financePaymentRepository;
	
	private final FinanceImportLogRepository financeImportLogRepository;
	private final FinanceImportDetailRepository financeImportDetailRepository;
	
	private final MemberInfoRepository memberInfoRepository;

	private final TmpFinanceImportLogRepository tmpFinanceImportLogRepository;
	private final TmpFinanceImportDetailRepository tmpFinanceImportDetailRepository;
	
	private final SysPrefixnameRepository sysPrefixnameRepository;
	
	private final SysWebhookKbankRepository sysWebhookKbankRepository;
	
	private final SysMoodleRepository sysMoodleRepository;
	private final UtilityService utilityService;

	@Value("${app.config.moodle.url}")
	private String moodleUrl;
	/*
	private enum FinancePaymentType {
		COURSEPUBLIC_ID(0),
		DETAIL_DATE(1),
		MEMBER_ID(2),
		MEMBER_NAME(3),
		REF1(4),
		REF2(5),
		PAYMENT_AMOUNT(6);

		private int value;

		private FinancePaymentType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	};
	*/
	
	private enum ImportPaymentType {
		COURSEPUBLIC_ID(0),
		PAY_DATETIME(1),
		PAY_AMOUNT(2),
		MEMBER_ID_CARD(3),
		PREFIX(4),
		FIRSTNAME_TH(5),
		LASTNAME_TH(6),
		FIRSTNAME_EN(7),
		LASTNAME_EN(8),
		EMAIL(9),
		ADDRESS(10),
		ORG_LEGAL_CODE(11),
		ORG_NAME(12),
		ORG_ADDRESS(13);

		private int value;

		private ImportPaymentType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	};

	@JmsListener(destination = "importpayment?prioritizedMessages=true")
	public void importpayment(String id) throws Exception {
		Long tmpImpId = Long.valueOf(id);
		TmpFinanceImportLog header = tmpFinanceImportLogRepository.findById(tmpImpId).orElse(null);

		if (null == header) {
			TmpFinanceImportLog update = header.toBuilder().messageError("TmpFinanceImportLog is null").activeFlag(true).build();
			tmpFinanceImportLogRepository.save(update);
			throw new NullPointerException("TmpFinanceImportLog is null");
		}
		
		CoursepublicMain cm = coursepublicMainRepository.findById(header.getCoursepublicId()).orElse(null);

		if (null == cm) {
			TmpFinanceImportLog update = header.toBuilder().messageError("ไม่พบรอบคอร์ส").activeFlag(true).build();
			tmpFinanceImportLogRepository.save(update);
			throw new NullPointerException("ไม่พบรอบคอร์ส");
		}
		
		BufferedReader br = null;
		
		UploadFileData uplaodFileData = UploadFileData
				.builder()
				.filename(header.getImpFileName())
				.prefix(header.getPrefix())
				.module(header.getModule())
				.build();
		
		String fullPath = fileStorageService.concatnatePathByModel(uplaodFileData);

		LOG.info("fullPath -> {}", fullPath);

		if (!fileStorageService.checkFileIsExists(fullPath)) {
			TmpFinanceImportLog update = header.toBuilder().messageError("File is not exists").activeFlag(true).build();
			tmpFinanceImportLogRepository.save(update);
			throw new NullPointerException("File is not exists");
		}
		
		try {
			
			File file = new File(fullPath);
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = "";
			String[] tempArr;
			
			List<TmpFinanceImportDetail> list = new ArrayList<>();
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			int index  = 0;

			while ((line = br.readLine()) != null) {

				// skip column name
				if (index == 0) {
					index++;
					continue;
				}
				
				String regex = "\"(\\d+(,\\d{3})*(\\.\\d+)?)\"";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(line);

				StringBuffer result = new StringBuffer();
				while (matcher.find()) {
					String quotedNumber = matcher.group(1); // Get the number without quotes
					String unquotedNumber = quotedNumber.replace(",", ""); // Remove commas
					matcher.appendReplacement(result, unquotedNumber);
				}
				matcher.appendTail(result);
				
				String output = result.toString();

				tempArr = output.split(",");

				int total = tempArr.length;

				List<String> wrongList = new ArrayList<>();
				
				Long coursepublicId = null;
				try {
					coursepublicId = Long.valueOf(readData(tempArr, ImportPaymentType.COURSEPUBLIC_ID.getValue(), total));
					if (coursepublicId.compareTo(cm.getCoursepublicId()) != 0) {
						wrongList.add("different[coursepublicId]");
					}
				} catch (Exception e) {
					wrongList.add("format[coursepublicId]");
				}
				
				Date payDatetime = null;
				try {
					payDatetime = dateFormat.parse(readData(tempArr, ImportPaymentType.PAY_DATETIME.getValue(), total));
				} catch (Exception e) {
					wrongList.add("format[payDatetime]");
				}
				
				BigDecimal payAmount = null;
				try {
					payAmount =  NumberUtils.toScaledBigDecimal(readData(tempArr, ImportPaymentType.PAY_AMOUNT.getValue(), total));
				} catch (Exception e) {
					wrongList.add("format[payAmount]");
				}

				String memberIdCard = readData(tempArr, ImportPaymentType.MEMBER_ID_CARD.getValue(), total);
				try {
					if (StringUtils.isNoneBlank(memberIdCard)) {
						/*
						int memberCardNo = memberInfoRepository.countMemberCardNo(memberIdCard);
						if (memberCardNo > 0) {
							wrongList.add("dupplicate[memberIdCard]");
						}
						*/
					} else {
						wrongList.add("empty[memberIdCard]");
					}
				} catch (Exception e) {
					wrongList.add("empty[memberIdCard]");
				}
				
				String prefix = readData(tempArr, ImportPaymentType.PREFIX.getValue(), total);
				Long prefixnameId = null;
				try {
					if (StringUtils.isNoneBlank(prefix)) {
						
						SysPrefixname sysPrefixname = sysPrefixnameRepository.findByPrefixnameNameTh(prefix);
						if (null == sysPrefixname) {
							wrongList.add("notfound[prefixnameId]");
							wrongList.add("notfound[prefixnameId]");
						} else {
							prefixnameId = sysPrefixname.getPrefixnameId();
						}
					} else {
						wrongList.add("empty[prefix]");
					}
				} catch (Exception e) {
					wrongList.add("empty[prefix]");
				}

				String firstnameTh = readData(tempArr, ImportPaymentType.FIRSTNAME_TH.getValue(), total);
				if (StringUtils.isBlank(firstnameTh)) {
					wrongList.add("empty[firstnameTh]");
				}

				String lastnameTh = readData(tempArr, ImportPaymentType.LASTNAME_TH.getValue(), total);
				if (StringUtils.isBlank(lastnameTh)) {
					wrongList.add("empty[lastnameTh]");
				}

				String firstnameEn = readData(tempArr, ImportPaymentType.FIRSTNAME_EN.getValue(), total);
				if (StringUtils.isBlank(firstnameEn)) {
					wrongList.add("empty[firstnameEn]");
				}

				String lastnameEn = readData(tempArr, ImportPaymentType.LASTNAME_EN.getValue(), total);
				if (StringUtils.isBlank(lastnameEn)) {
					wrongList.add("empty[lastnameEn]");
				}

				String email = readData(tempArr, ImportPaymentType.EMAIL.getValue(), total);
				if (StringUtils.isBlank(email)) {
					wrongList.add("empty[email]");
				}

				String address = readData(tempArr, ImportPaymentType.ADDRESS.getValue(), total);
				if (StringUtils.isBlank(address)) {
					wrongList.add("empty[address]");
				}
				
				String orgLegalCode = readData(tempArr, ImportPaymentType.ORG_LEGAL_CODE.getValue(), total);
				String orgName = readData(tempArr, ImportPaymentType.ORG_NAME.getValue(), total);
				String orgAddress = readData(tempArr, ImportPaymentType.ORG_ADDRESS.getValue(), total);
				
				TmpFinanceImportDetail detail = TmpFinanceImportDetail
						.builder()
						.tmpImpId(tmpImpId)
						.coursepublicId(coursepublicId)
						.payDatetime(payDatetime)
						.payAmount(payAmount)
						.memberIdCard(memberIdCard)
						.prefix(prefix)
						.prefixnameId(prefixnameId)
						.firstnameTh(firstnameTh)
						.lastnameTh(lastnameTh)
						.firstnameEn(firstnameEn)
						.lastnameEn(lastnameEn)
						.email(email)
						.address(address)
						.orgLegalCode(orgLegalCode)
						.orgName(orgName)
						.orgAddress(orgAddress)
						.detailStatus(ImportStatus.PASS.getValue())
						.activeFlag(true)
						.createDate(new Date())
						.createBy(header.getCreateBy())
						.build();
				
				/*
				Date detailDate = null;
				try {
					detailDate = dateFormat.parse(readData(tempArr, FinancePaymentType.DETAIL_DATE.getValue(), total));
				} catch (Exception e) {
					wrongList.add("format[detailDate]");
				}

				Long memberId = null;
				try {
					memberId = Long.valueOf(readData(tempArr, FinancePaymentType.MEMBER_ID.getValue(), total));
				} catch (Exception e) {
					wrongList.add("format[memberId]");
				}

				BigDecimal paymentAmount = null;
				try {
					paymentAmount =  NumberUtils.toScaledBigDecimal(readData(tempArr, FinancePaymentType.PAYMENT_AMOUNT.getValue(), total));
				} catch (Exception e) {
					wrongList.add("format[paymentAmount]");
				}

				TmpFinanceImportDetail detail = TmpFinanceImportDetail
						.builder()
						.tmpImpId(tmpImpId)
						.coursepublicId(coursepublicId)
						.detailDate(detailDate)
						.memberId(memberId)
						.memberName(readData(tempArr, FinancePaymentType.MEMBER_NAME.getValue(), total))
						.ref1(readData(tempArr, FinancePaymentType.REF1.getValue(), total))
						.ref2(readData(tempArr, FinancePaymentType.REF2.getValue(), total))
						.paymentAmount(paymentAmount)
						.detailStatus(ImportStatus.PASS.getValue())
						.build();
				*/

				if (!wrongList.isEmpty()) {
					detail.setErrorMessage(String.join(", ", wrongList));
					detail.setDetailStatus(ImportStatus.FAIL.getValue());
					detail.setActiveFlag(false);
				}

				list.add(detail);

			}

			list = tmpFinanceImportDetailRepository.saveAll(list);

			BigDecimal impFileMoneys = list.stream()
				.map(TmpFinanceImportDetail::getPayAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			Long rows = (long) list.size();
			
			Long totalRegister = cm.getMaxEnroll() - (null == cm.getTotalRegister() ? 0l : cm.getTotalRegister());
			
			if (rows.compareTo(totalRegister) > 0) {

				String msgError = "จำนวนที่นำเข้ามากกว่าจำนวนที่เปิดรับ";
				
				header.setImpFileRow(rows);
				header.setPassRow(0l);
				header.setFailRow(rows);
				header.setImpFileMoney(impFileMoneys);
				header.setMessageError(msgError);
				header.setActiveFlag(true);
				
				list = list.stream().map(o -> {
					String msg = StringUtils.isBlank(o.getErrorMessage()) ? "" : o.getErrorMessage();
					msg = msg + msgError;
					TmpFinanceImportDetail detail = o.toBuilder()
							.errorMessage(msgError)
							.detailStatus(ImportStatus.FAIL.getValue())
							.activeFlag(true)
							.build();
					return detail;
				}).toList();
				
				
			} else {

				Long pass = (long) list.stream().filter(o -> Boolean.TRUE.equals(o.getActiveFlag())).count();
				Long fail = rows - pass;

				header.setImpFileRow(rows);
				header.setPassRow(pass);
				header.setFailRow(fail);
				header.setImpFileMoney(impFileMoneys);
				header.setActiveFlag(true);
				
			}

			tmpFinanceImportLogRepository.save(header);

		} catch (IOException ioe) {

			header.setActiveFlag(false);
			header.setMessageError(ImportStatus.FAIL.getValue());

			tmpFinanceImportLogRepository.save(header);

			ioe.printStackTrace();
			throw new IOException("Read file error");

		} catch (Exception e) {

			header.setActiveFlag(false);
			header.setMessageError(ImportStatus.FAIL.getValue());

			tmpFinanceImportLogRepository.save(header);
			LOG.error(e.getMessage(), e);

		} finally {
			if (null != br) {
				br.close();
			}
		}
	}
	
	@JmsListener(destination = "confirmpayment?prioritizedMessages=true")
	public void confirmpayment(String id) throws Exception {

		Long tmpImpId = Long.valueOf(id);
		TmpFinanceImportLog header = tmpFinanceImportLogRepository.findById(tmpImpId).orElse(null);
		LOG.info("HEADER -> {}", header);
		if (null == header) {
			TmpFinanceImportLog update = header.toBuilder().messageError("TmpFinanceImportLog is null").activeFlag(true).build();
			tmpFinanceImportLogRepository.save(update);
			LOG.info("TmpFinanceImportLog is null ");
			throw new NullPointerException("TmpFinanceImportLog is null");
		}
		
		// call store procedure
		List<TmpFinanceImportDetail> list = tmpFinanceImportDetailRepository.findByTmpImpId(tmpImpId);
		
		if (null == list || list.isEmpty()) {
			TmpFinanceImportLog update = header.toBuilder().messageError("TmpFinanceImportDetail is null").activeFlag(true).build();
			tmpFinanceImportLogRepository.save(update);
			LOG.info("TmpFinanceImportDetail is null ");
			throw new NullPointerException("TmpFinanceImportDetail is null");
		}
		
		Date now = new Date();
		AutUser userAction = header.getCreateBy();

		FinanceImportLog financeImportLog = FinanceImportLog
				.builder()
				.fileReferenceCode(header.getFileReferenceCode())
				.impFileName(header.getImpFileName())
				.impFileLink(header.getImpFileLink())
				.impFileSize(header.getImpFileSize())
				.impFileRow(header.getImpFileRow())
				.passRow(header.getPassRow())
				.failRow(header.getFailRow())
				.missRow(header.getMissRow())
				.messageError(header.getMessageError())
				.impFileMoney(header.getImpFileMoney())
				.coursepublicId(header.getCoursepublicId())
				.activeFlag(true)
				.createBy(userAction)
				.createDate(now)
				.build();
		
		financeImportLogRepository.save(financeImportLog);
		
		List<FinanceImportDetail> detailList = list.stream().map(temp -> {
			FinanceImportDetail detail = FinanceImportDetail
					.builder()
					.impId(financeImportLog.getImpId())
					.coursepublicId(temp.getCoursepublicId())
					.payDatetime(temp.getPayDatetime())
					.payAmount(temp.getPayAmount())
					.memberIdCard(temp.getMemberIdCard())
					.prefixnameId(temp.getPrefixnameId())
					.prefix(temp.getPrefix())
					.firstnameTh(temp.getFirstnameTh())
					.lastnameTh(temp.getLastnameTh())
					.firstnameEn(temp.getFirstnameEn())
					.lastnameEn(temp.getLastnameEn())
					.email(temp.getEmail())
					.address(temp.getAddress())
					.orgLegalCode(temp.getOrgLegalCode())
					.orgName(temp.getOrgName())
					.orgAddress(temp.getOrgAddress())
					.detailStatus(temp.getDetailStatus())
					.errorMessage(temp.getErrorMessage())
					.activeFlag(true)
					.createBy(userAction)
					.createDate(now)
					.build();
			return detail;
		}).toList();
		
		if (null != detailList && !detailList.isEmpty()) {
			financeImportDetailRepository.saveAll(detailList);
			
			try {
				// call proc
				scheduleService.callProcImportFinancePayment(financeImportLog.getImpId());
			} catch (Exception e) {

				financeImportLog.setStatus("fail");
				financeImportLog.setMessageError(e.getMessage());
				financeImportLog.setUpdateDate(new Date());
				financeImportLog.setUpdateBy(financeImportLog.getCreateBy());
				
				financeImportLogRepository.save(financeImportLog);

				LOG.error("==============[ confirmpayment call proc ]==============");
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	/*
	@Transactional
	private void saveActualTable(List<FinanceImportLogDetailData> cloneDetailList, CoursepublicMain coursepublicMain, AutUser userAction, Date now) {
		
		Long courseId = (null == coursepublicMain ? null : coursepublicMain.getCourseId());
		
		cloneDetailList = cloneDetailList.stream().map(data -> {
			MemberCourse memberCourse = MemberCourse.builder()
					.coursepublicId(data.getCoursepublicId())
					.courseId(courseId)
					.memberId(data.getMemberId())
					.registerDate(now)
					.paymentStatus(30017002l)
					.studyStatus(30016001l)
					.activeFlag(true)
					.createBy(userAction)
					.createDate(now)
					.build();
			memberCourseRepository.save(memberCourse);
			return data.toBuilder().memberCourseId(memberCourse.getMemberCourseId()).build();
		}).toList();

		MasBankAccount bankAccount = masBankAccountRepository.findActive();

		List<FinancePayment> financePaymentList = cloneDetailList.stream().map(data -> {
			FinancePayment financePayment = FinancePayment
					.builder()
					.bankAccountId(bankAccount.getBankAccountId())
					.paymentType(30023003l)
					.memberCourseId(data.getMemberCourseId())
					.coursepublicId(data.getCoursepublicId())
					.memberId(data.getMemberId())
					.paymentAmount(data.getPaymentAmount())
					.paymentStatus(30033002l)
					.receiptDate(data.getReceiptDate())
					.detailImpId(data.getDetailId())
					.orgCode(data.getOrgCode())
					.orgName(data.getOrgName())
					.orgAddress(data.getOrgAddress())
					.activeFlag(true)
					.createBy(userAction)
					.createDate(now)
					.build();
			return financePayment;
		}).toList();
		
		financePaymentRepository.saveAll(financePaymentList);
		
		for (int i = 0; i < financePaymentList.size(); i++) {		
			
			// step 4 generate receipt
	        // List<String> datas = Arrays.asList(String.valueOf(financePaymentList.get(i).getPaymentId()), "2" );
	        // jmsSender.sendMessage(Q_RECEIPT_PRINT, String.join(";", datas));
	                
			// step 5 ส่งอีเมลยืนยันแจ้งผู้เรียน
	        // List<String> dataMail = Arrays.asList(String.valueOf(financePaymentList.get(i).getPaymentId()), String.valueOf(EMAIL_TEMPLATE_30036010) );
	        // jmsSender.sendMessage(Q_SENDMAIL, String.join(";", dataMail));
	        
			// step 6 ส่งอีเมลยืนยันแจ้งอาจารย์ผู้สอน
			// step 7 ส่งอีเมลยืนยันแจ้งเจ้าหน้าที
			 
		}
		
		coursepublicMainRepository.updateTotalRegister(coursepublicMain.getCoursepublicId());
	}
	*/
	
	@JmsListener(destination = "reconcilepayment?prioritizedMessages=true")
	public void reconcilepayment(String chargeId) throws Exception {
		
		// validate checksum
		SysWebhookKbank webhook = sysWebhookKbankRepository.findActiveFlagByChargeId(chargeId);
		if (null == webhook) {
			LOG.error("================[ webhook not found ]================");
			throw new NullPointerException("Charge ID not found");
		}
		
		if (!Boolean.TRUE.equals(webhook.getChecksum())) {
			LOG.error("================[ Checksum is fail ]================");
			throw new Exception("Checksum is fail");
		}
		
		/**
		MasBankAccount bankAccount = masBankAccountRepository.findActive();
		
		// step 1
		// 30033002 ยืนยันการชำระเงินแล้ว
		FinancePayment updateFinancePayment = financePayment
				.toBuilder()
				.paymentStatus(30033002l)
				.receiptDate(new Date())
				.transactionId(chargeId)
				.transactionDatetime(new Date())
				.transactionPayAccount(bankAccount.getAccountNo())
				.build();
		financePaymentRepository.save(updateFinancePayment);
		
		memberCourseRepository.updateMemberCourse(30017002l, 30016001l, updateFinancePayment.getMemberCourseId());
		
		// step 2
		coursepublicMainRepository.updateTotalRegister(updateFinancePayment.getCoursepublicId());
		
		// step 3
		if (null != updateFinancePayment.getMemberCourseId() && null != updateFinancePayment.getMemberId() && null != updateFinancePayment.getCampaignId()) {			
			memberCouponRepository.updateMemberCourseCoupon(updateFinancePayment.getMemberCourseId(), updateFinancePayment.getMemberId(), updateFinancePayment.getCampaignId());
		}
		
        // step 8
        CoursepublicMain cm = coursepublicMainRepository.findById(financePayment.getCoursepublicId()).orElse(null);
        if (null != cm) {
        	if (Boolean.TRUE.equals(cm.getBuasriStatus())) {
        		MemberInfo memberInfo = memberInfoRepository.findById(financePayment.getMemberId()).orElse(null);
        		if (null != memberInfo) {
        			if (StringUtils.isBlank(memberInfo.getBuasriId())) {        				
        				String buasriId = "cbs" + memberInfo.getMemberNo();// commonService.generateRunningNumber(RunningNumber.BUASRI_ID);
        				MemberInfo updateMemberInfo = memberInfo
        						.toBuilder()
        						.buasriId(buasriId)
        						.buasriIdDate(new Date())
        						.buasriIdCreateDate(new Date())
        						.buasriIdExpireDate(cm.getCourseClassEnd())
        						.updateBy(new AutUser(2l))
        						.updateDate(new Date())
        						.build();
        				memberInfoRepository.save(updateMemberInfo);
        			} else {
        				scheduleService.updateBuasriExpireDateByCoursepublicIdAndMemberId(financePayment.getCoursepublicId(), financePayment.getMemberId());
        			}
        		}
        	}
        }
        */
		
		FinancePayment financePayment = financePaymentRepository.findByChargeId(chargeId);
		
		if (null == financePayment) {
			LOG.error("================[ finance_payment is null ]================");
			throw new Exception("finance_payment is null");
		}
		
		Boolean updted = scheduleService.updateFianceAfterPayment(financePayment.getPaymentId(), financePayment.getPaymentType());
		if (!Boolean.TRUE.equals(updted)) {
			LOG.error("================[ CALL update_finance_payment fail ]================");
			throw new Exception("CALL update_finance_payment fail");
		}
		
		CoursepublicMain cm = coursepublicMainRepository.findById(financePayment.getCoursepublicId()).orElse(null);
		
		if (null == cm) {
			LOG.error("================[ coursepublic_main is null ]================");
			throw new Exception("coursepublic_main is null");
		}
		
        if (null != financePayment.getMemberId()) {

			MemberInfo memberInfo = memberInfoRepository.findById(financePayment.getMemberId()).orElse(null);
			
			if (null != memberInfo) {
		        try {
		        	
		        	if(moodleUrl != null && !moodleUrl.equals("skip")) {
		        		
		        		if (null == memberInfo.getMoodleUserId()) {

							String moodlePassword = utilityService.generateDefaultPassword(memberInfo.getMemberCardno());
					
							SysMoodle sysMoodle = sysMoodleRepository.findActive();

							StringBuilder url = new StringBuilder();
							url.append(sysMoodle.getDomain())
									.append("?wstoken=").append(sysMoodle.getToken())
									.append("&wsfunction=").append("core_user_create_users")
									.append("&moodlewsrestformat=").append("json")
									.append("&users[0][username]=").append(memberInfo.getMemberEmail())
									.append("&users[0][password]=").append(moodlePassword)
									.append("&users[0][firstname]=").append(memberInfo.getMemberFirstnameTh())
									.append("&users[0][lastname]=").append(memberInfo.getMemberLastnameTh())
									.append("&users[0][email]=").append(memberInfo.getMemberEmail());
		        			
		        			RestTemplate restTemplate = new RestTemplate();

							LOG.info("core_user_create_users -> {}", url);

		        			ResponseEntity<String> resultPesponse = restTemplate.getForEntity(url.toString(), String.class);
		        			String resMap = resultPesponse.getBody();
							LOG.info("core_user_create_users response -> {}", resMap);
		        			if (HttpStatus.OK.equals(resultPesponse.getStatusCode())) {
		        				String responseBody = resultPesponse.getBody();
		        				Type typeList = new TypeToken<ArrayList<ResponseMoodleData>>() {}.getType();
		        				List<ResponseMoodleData> datas = new Gson().fromJson(responseBody, typeList);
		        				if (null != datas && !datas.isEmpty()) {
		        					MemberInfo update =  memberInfo.toBuilder()
										.moodleUserId(datas.get(0).getId())
										.moodlePassword(utilityService.encryptAES256(moodlePassword))
										.build();
		        					memberInfoRepository.save(update);
	        	        			enrolManualEnrolUsers(update.getMoodleUserId(), cm.getMoodleCourseId());
		        				}
		        			}
		        		} else {
		        			enrolManualEnrolUsers(memberInfo.getMoodleUserId(), cm.getMoodleCourseId() );
		        		}
	
					} else {
						LOG.warn("Call MOODLE SKIP MODE....");
					}
				} catch (Exception e) {
					LOG.error("====================[ payment moodle ]=====================");
					LOG.error(e.getMessage(), e);
				}
			}
	        
        }
        
		// step 4 generate receipt
        List<String> datas = Arrays.asList(String.valueOf(financePayment.getPaymentId()), "2" );
        jmsSender.sendMessage(Q_RECEIPT_PRINT, String.join(";", datas));
                
		// step 5 ส่งอีเมลยืนยันแจ้งผู้เรียน
//        List<String> dataMail = Arrays.asList(String.valueOf(updateFinancePayment.getPaymentId()), String.valueOf(EMAIL_TEMPLATE_30036010) );
//        jmsSender.sendMessage(Q_SENDMAIL, String.join(";", dataMail));
        List<String> list = Arrays.asList(String.valueOf(financePayment.getMemberCourseId()), String.valueOf(EMAIL_TEMPLATE_30036003));
        jmsSender.sendMessage(Q_SENDMAIL, String.join(";", list));
                
		// step 6 ส่งอีเมลยืนยันแจ้งอาจารย์ผู้สอน
        List<String> listIn = Arrays.asList(String.valueOf(financePayment.getMemberCourseId()), String.valueOf(EMAIL_TEMPLATE_30036004));
        jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));
        
		// step 7 ส่งอีเมลยืนยันแจ้งเจ้าหน้าที
	}
	
	private void enrolManualEnrolUsers(Long moodleUserId, Long moodleCourseId) {

        try {
        	if(moodleUrl != null && !moodleUrl.equals("skip")) {

					SysMoodle sysMoodle = sysMoodleRepository.findActive();

					StringBuilder url = new StringBuilder();
					url.append(sysMoodle.getDomain())
						.append("?wstoken=").append(sysMoodle.getToken())
						.append("&wsfunction=").append("enrol_manual_enrol_users")
						.append("&moodlewsrestformat=").append("json")
						.append("&enrolments[0][roleid]=").append("5")
						.append("&enrolments[0][userid]=").append(String.valueOf(moodleUserId))
						.append("&enrolments[0][courseid]=").append(String.valueOf(moodleCourseId));
        			
        			RestTemplate restTemplate = new RestTemplate();
        			
        			LOG.info("enrolManualEnrolUsers -> {} ", url);
        			ResponseEntity<String> resultPesponse = restTemplate.getForEntity(url.toString(), String.class);
        			String resMap = resultPesponse.getBody();
        			LOG.info("enrolManualEnrolUsers response -> {}", resMap);

			} else {
				LOG.warn("Call MOODLE SKIP MODE....");
			}
		} catch (Exception e) {
			LOG.error("====================[ enrolManualEnrolUsers ]=====================");
			LOG.error(e.getMessage(), e);
		}
        
	}

	private String readData(String[] datas, int index, int totalSize) {
		if (index < totalSize) {
			if (datas[index].startsWith("\"") && datas[index].endsWith("\"")) {
				return datas[index].substring(1, datas[index].length() - 1);
			}
			return datas[index];
		} else {
			return null;
		}
	}

}
