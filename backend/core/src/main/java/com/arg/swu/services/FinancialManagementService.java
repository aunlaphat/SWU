package com.arg.swu.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.FinanceImportDetailData;
import com.arg.swu.dto.FinanceImportLogData;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.FinanceReceiptConfigData;
import com.arg.swu.dto.TmpFinanceImportDetailData;
import com.arg.swu.dto.TmpFinanceImportLogData;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class FinancialManagementService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(FinancialManagementService.class);

	private final JdbcTemplate jdbcTemplate;
	
	public Map<String, Object> findPaymentListByCondition(FinancePaymentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getReceiptDateList() && !criteria.getReceiptDateList().isEmpty()) {
			if (null == criteria.getReceiptDateList().get(1)) {
				conditions.append(" and fp.receipt_date between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), false, true));
			} else {
				conditions.append(" and fp.receipt_date between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(1), false, true));
			}
		}
		
		if (null != criteria.getPaymentType()) {
			conditions.append(" and fp.payment_type = ? ");
			params.add(criteria.getPaymentType());
		}
		
		if (StringUtils.isNoneBlank(criteria.getPublicNameTh())) {
			conditions.append(" and (cm.course_code ilike ? or cpm.public_name_th ilike ? or cpm.public_name_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getMemberFirstnameTh())) {
			conditions.append(" and (mi.member_firstname_th ilike ? or mi.member_lastname_th ilike ? or mi.member_firstname_en ilike ? or mi.member_lastname_en ilike ?)");
			params.add(CommonUtils.concatLikeParam(criteria.getMemberFirstnameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getMemberFirstnameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getMemberFirstnameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getMemberFirstnameTh(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by fp.payment_id desc ) AS row_num, ");
		sb.append(" ctl.name_lo payment_type_th, ctl.name_en payment_type_en, cpm.course_id, cpm.public_name_th, cpm.public_name_en, cm.course_code, mi.member_id, mi.member_firstname_th, mi.member_firstname_en, mi.member_lastname_th, mi.member_lastname_en, ");        
		sb.append(" fp.* ");
		sb.append(" from finance_payment fp ");
		sb.append(" left join lookup_catalog ctl on fp.payment_type = ctl.catalog_id ");
		sb.append(" left join coursepublic_main cpm on fp.coursepublic_id = cpm.coursepublic_id ");
		sb.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		sb.append(" left join member_info mi on fp.member_id = mi.member_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by payment_id desc ");
		sb.append(LIMIT);
		
		List<FinancePaymentData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(FinancePaymentData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from finance_payment fp");
		
		count.append(" left join lookup_catalog ctl on fp.payment_type = ctl.catalog_id ");
		count.append(" left join coursepublic_main cpm on fp.coursepublic_id = cpm.coursepublic_id ");
		count.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		count.append(" left join member_info mi on fp.member_id = mi.member_id ");
		count.append(WHERE);
		
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	
	public Map<String, Object> findPaymentImportListByCondition(FinanceImportLogData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getCreateDateList() && !criteria.getCreateDateList().isEmpty()) {
			if (null == criteria.getCreateDateList().get(1)) {
				conditions.append(" and fil.create_date between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(0), false, true));
			} else {
				conditions.append(" and fil.create_date between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(1), false, true));
			}
		}
		
		if (StringUtils.isNotBlank(criteria.getImpFileName())) {
			conditions.append(" and fil.imp_file_name ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getImpFileName(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getPublicNameTh())) {
			conditions.append(" and (cm.course_code ilike ? or cpm.public_name_th ilike ? or cpm.public_name_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getFileReferenceCode())) {
			conditions.append(" and fil.file_reference_code ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getFileReferenceCode(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by fil.imp_id desc ) AS row_num,");
		sb.append(" cpm.coursepublic_id, cpm.public_name_en, cpm.public_name_th, cm.course_code, ");        
		sb.append(" fil.* ");
		sb.append(" from finance_import_log fil ");
		sb.append(" left join coursepublic_main cpm on fil.coursepublic_id = cpm.coursepublic_id ");
		sb.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by imp_id desc ");
		sb.append(LIMIT);
		
		List<FinanceImportLogData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(FinanceImportLogData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from finance_import_log fil ");
		count.append(" left join coursepublic_main cpm on fil.coursepublic_id = cpm.coursepublic_id ");
		count.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findPaymentImportDetailListByCondition(FinanceImportDetailData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getImpId()) {
			conditions.append(" and fid.imp_id = ? ");
			params.add(criteria.getImpId());
		}
				
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by fid.detail_id asc ) AS row_num,");
		sb.append(" cpm.coursepublic_id, cpm.public_name_en, cpm.public_name_th, cm.course_code, fil.imp_file_name, fil.imp_file_money, fil.create_date,  ");        
		sb.append(" fid.* ");
		sb.append(" from finance_import_detail fid  ");
		sb.append(" left join coursepublic_main cpm on fid.coursepublic_id = cpm.coursepublic_id  ");
		sb.append(" left join course_main cm on cpm.course_id = cm.course_id  ");
		sb.append(" left join finance_import_log fil on fid.imp_id = fil.imp_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by detail_id asc ");
		sb.append(LIMIT);
		
		List<FinanceImportDetailData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(FinanceImportDetailData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from finance_import_detail fid  where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findFinanceReceiptConfigByCondition(FinanceReceiptConfigData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
				
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by frc.finance_receipt_config asc ) AS row_num, ");  
		sb.append(" frc.* ");
		sb.append(" from finance_receipt_config frc  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by frc.finance_receipt_config asc ");
		sb.append(LIMIT);
		
		List<FinanceReceiptConfigData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(FinanceReceiptConfigData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from finance_receipt_config frc  where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findTmpFinanceImportDetailByCondition(TmpFinanceImportDetailData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getTmpImpId()) {
			conditions.append(" and tfid.tmp_imp_id = ? ");
			params.add(criteria.getTmpImpId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and tfid.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by tfid.detail_id asc ) AS row_num, ");  
		sb.append(" tfid.* ");
		sb.append(" from tmp_finance_import_detail tfid  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by tfid.detail_id asc ");
		sb.append(LIMIT);
		
		List<TmpFinanceImportDetailData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(TmpFinanceImportDetailData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from tmp_finance_import_detail tfid where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findTmpFinanceImportLogByCondition(TmpFinanceImportLogData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
				
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by tfil.tmp_imp_id asc ) AS row_num, ");  
		sb.append(" tfil.* ");
		sb.append(" from tmp_finance_import_log tfil  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by tfil.tmp_imp_id asc ");
		sb.append(LIMIT);
		
		List<TmpFinanceImportLogData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(TmpFinanceImportLogData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from tmp_finance_import_log tfil  where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	

}
