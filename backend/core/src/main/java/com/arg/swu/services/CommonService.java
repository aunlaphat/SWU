package com.arg.swu.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.models.CourseMain;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class CommonService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(CommonService.class);
	
	private final JdbcTemplate jdbcTemplate;
	
	private static final String SQL_OCCUPATION = "select concat('OC', lpad((m.code + 1)::text, 3, '0')) from ( select coalesce(max(to_number(substring(mo.occupation_code from '\\d{3}'), 'FMTH000')), 0) code from mas_occupation mo ) m";
	private static final String SQL_COURSE_TYPE = "select concat('CT', lpad((m.code + 1)::text, 3, '0')) from ( select coalesce(max(to_number(substring(mct.course_type_code from '\\d{3}'), 'FMTH000')), 0) code from mas_course_type mct ) m";
	private static final String SQL_GENERATE_BUASRI_ID = "select concat('cbs', right((to_char(now(), 'yyyy')::int + 543)::text, 2 ) ,lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(max(to_number(substring(right(mi.buasri_id, 5) from '\\d{5}'), 'FMTH00000')), 0) code from member_info mi ) m";
	private static final String SQL_GENERAL_SKILL = "select concat('S', lpad((m.code + 1)::text, 4, '0')) from ( select coalesce(max(to_number(substring(mgs.general_skill_code from '\\d{4}'), 'FMTH0000')), 0) code from mas_general_skill mgs ) m";
	
	public String generateRunningNumber(RunningNumber runningNumber) {
		String value = "";
		switch (runningNumber) {
		case OCCUPATION: {
			value = getNextLastNumber(RunningNumber.OCCUPATION.getValue(), 3, "", "0");
			break;
		}
		case COURSE_TYPE: {
			value = getNextLastNumber(RunningNumber.COURSE_TYPE.getValue(), 3, "", "0");
			break;
		}
		case BUASRI_ID: {
			value = jdbcTemplate.queryForObject(SQL_GENERATE_BUASRI_ID, String.class);
			break;
		}
		case GENERAL_SKILL: {
			value = getNextLastNumber(RunningNumber.GENERAL_SKILL.getValue(), 4, "", "0");
			break;
		}
		case RECEIPT_NO: {
			value = generateRecriptNo();
		}
		default:
			break;
		}
		return value;
	}
	
	private String getNextLastNumber(String key, int lpad, String delimiter, String padStr) {
		if (StringUtils.isBlank(key)) return null;
		padStr = StringUtils.isBlank(padStr) ? "0" : padStr;
		String nextNumber = jdbcTemplate.queryForObject("select generate_running(?) ", String.class, key);
		String running = StringUtils.leftPad(nextNumber, lpad, padStr);
		StringJoiner joiner = new StringJoiner(delimiter);
		joiner.add(key);
		joiner.add(running);
		return joiner.toString();
	}
	
	public String generateCourseCode(CourseMain courseMain) {
		
		StringBuilder q1 = new StringBuilder();
		q1.append(" select ");
		q1.append(" concat (");
		q1.append(" ( case when mct.course_mapping_status then 'D' else 'N' end ),");
		q1.append(" 'LL',");
		q1.append(" md.dep_name_abbr_en");
		q1.append(" ) code");
		q1.append(" from course_main cmt ");
		q1.append(" left join mas_course_type mct on cmt.course_type_id = mct.course_type_id");
		q1.append(" left join mas_department md on cmt.dep_id_level1 = md.dep_id");
		q1.append(" where 1 = 1");
		q1.append(" and cmt.course_id = ? ");
		String prefixCode = jdbcTemplate.queryForObject(q1.toString(), String.class, courseMain.getCourseId());
		
		StringBuilder code = new StringBuilder();
		code.append(getNextLastNumber(prefixCode, 3, "", "0"));
		if (null != courseMain.getCourseVersion()) {
			code.append("-");
			code.append(courseMain.getCourseVersion());
		}
		
		return code.toString();
	}
	
	public Long getCourseGeneration(Long courseId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select coalesce(max(cm.course_generation), 0) + 1 ");
		sql.append("from coursepublic_main cm ");
		sql.append("where cm.course_id = ? ");
		return jdbcTemplate.queryForObject(sql.toString(), Long.class, courseId);
	}
	
	private String generateRecriptNo() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select concat( ");
		sb.append("   concat(substring(receipt_prefix, 0, (strpos(receipt_prefix, 'YYth') + strpos(receipt_prefix, 'YYen') + strpos(receipt_prefix, 'YYYYth') + strpos(receipt_prefix, 'YYYYen'))), EXTRACT(YEAR FROM CURRENT_DATE) -  ");
		sb.append("     ( ");
		sb.append(" 	  	case when strpos(receipt_prefix, 'YYth') > 0 then 1957 ");
		sb.append(" 		when strpos(receipt_prefix, 'YYYYth')>0 then -543 ");
		sb.append(" 		when strpos(receipt_prefix, 'YYen')>0 then 2000 ");
		sb.append(" 		else 0 end ");
		sb.append(" 	) ");
		sb.append("   ),  ");
		sb.append("   lpad( ");
		sb.append("   generate_running( ");
		sb.append("     concat(substring(receipt_prefix, 0, (strpos(receipt_prefix, 'YYth')+strpos(receipt_prefix, 'YYen') + strpos(receipt_prefix, 'YYYYth')+strpos(receipt_prefix, 'YYYYen'))), EXTRACT(YEAR FROM CURRENT_DATE) -  ");
		sb.append(" 	  	( ");
		sb.append(" 		  	case when strpos(receipt_prefix, 'YYth') > 0 then 1957 ");
		sb.append(" 			when strpos(receipt_prefix, 'YYYYth') > 0 then - 543 ");
		sb.append(" 			when strpos(receipt_prefix, 'YYen') > 0 then 2000 ");
		sb.append(" 			else 0 end ");
		sb.append(" 		) ");
		sb.append(" 	) ");
		sb.append("   ), 5, '0') ");
		sb.append(" ) ");
		sb.append(" from finance_receipt_config frc where active_flag = true ");
		return jdbcTemplate.queryForObject(sb.toString(), String.class);
	}
	
	/*
    public Long generateReceiptNo(Long paymentId,String prefix){
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        Date presentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(presentDate);
        Integer yearInt = calendar.get(Calendar.YEAR);
        Integer monthInt = calendar.get(Calendar.MONTH);
        String yearText = yearInt.toString();
        String monthText = monthInt.toString();
        Date lacalDate = this.convertToDateViaSqlDate(this.convertToLocalDateViaInstant(presentDate));
        calendar.setTime(lacalDate);
        Integer localeYearInt = calendar.get(Calendar.YEAR);
        String localeYearText = localeYearInt.toString();
        Long receiptNo = 0l;
        switch(prefix){
            case "YYth":{
                sql = new StringBuilder();
                sql.append("select concat(?, lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(cast(right(fp.receipt_no,4)as int),0) as code from finance_payment fp  where payment_id=?) m");
                params.add(localeYearText.substring(2,4));
                params.add(paymentId);
                receiptNo = jdbcTemplate.queryForObject(sql.toString(),Long.class, params.toArray());
                break;
            }
            case "YYen":{
                sql = new StringBuilder();
                sql.append("select concat(?, lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(cast(right(fp.receipt_no,4)as int),0) as code from finance_payment fp  where payment_id=?) m");
                params.add(yearText.substring(2,4));
                params.add(paymentId);
                receiptNo = jdbcTemplate.queryForObject(sql.toString(),Long.class, params.toArray());
                break;
            }
            case "YYYYth":{
                sql = new StringBuilder();
                sql.append("select concat(?, lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(cast(right(fp.receipt_no,4)as int),0) as code from finance_payment fp  where payment_id=?) m");
                params.add(localeYearText);
                params.add(paymentId);
                receiptNo = jdbcTemplate.queryForObject(sql.toString(),Long.class, params.toArray());
                break;
            }
            case "YYYYen":{
                sql = new StringBuilder();
                sql.append("select concat(?, lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(cast(right(fp.receipt_no,4)as int),0) as code from finance_payment fp  where payment_id=?) m");
                params.add(yearText);
                params.add(paymentId);
                receiptNo = jdbcTemplate.queryForObject(sql.toString(),Long.class, params.toArray());
                break;
            }
            case "mm":{
                sql = new StringBuilder();
                sql.append("select concat(?, lpad((m.code + 1)::text, 5, '0')) from ( select coalesce(cast(right(fp.receipt_no,4)as int),0) as code from finance_payment fp  where payment_id=?) m");
                params.add(monthText);
                params.add(paymentId); 
                receiptNo = jdbcTemplate.queryForObject(sql.toString(),Long.class, params.toArray());
                break;
            }
            default:{
                break;
            }
        }
        return receiptNo;
    } 
    */
	
    public String generateMemberCourseCertificateRunningNo(String courseCode){
    	String key = "SWU-" + courseCode;
		return getNextLastNumber(key, 6, "-", "0");
		/*
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        
        sql.append("select concat(?,'-',lpad((m.code + 1)::text, 5, '0')) as certificate_no from ( select coalesce(cast(right(mc.certificate_no,4)as int),0) as code from member_course mc  where mc.certificate_no like ?) m");
        params.add(courseCode);
        params.add(CommonUtils.concatLikeParam(courseCode, true, true));
        String certificateRunningNo = new String();
        List<MemberCourseData> certificateRunningNoList = jdbcTemplate.query(sql.toString(),BeanPropertyRowMapper.newInstance(MemberCourseData.class),params.toArray());
        if(certificateRunningNoList.isEmpty()){
            certificateRunningNo=courseCode.concat("-000001");
        }else{
            certificateRunningNo=certificateRunningNoList.get(0).getCertificateNo();
        }
        return certificateRunningNo;
        */
    }
    /*
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    }
    
    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
    */
}
