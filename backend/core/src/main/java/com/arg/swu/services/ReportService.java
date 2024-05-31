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
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.dto.ReportCanceledCourseData;
import com.arg.swu.dto.ReportCourseEnrollmentData;
import com.arg.swu.dto.ReportCoursePaymentData;
import com.arg.swu.dto.ReportDepartmentIncomeData;
import com.arg.swu.dto.ReportEnrollmentAndPaymentData;
import com.arg.swu.dto.ReportEnrollmentListData;
import com.arg.swu.dto.ReportOfferedCourseData;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Service
@RequiredArgsConstructor
public class ReportService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(ReportService.class);

	private final JdbcTemplate jdbcTemplate;
	
// report 1 
	public Map<String, Object> findOfferedCourseReport(ReportOfferedCourseData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getDepNameShortTh())) {
			conditions.append(" and (department.dep_name_short_th ilike ? or department.dep_name_short_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and ( concat(course_m.course_code,'-',coalesce(course_m.course_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(course_m.course_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}	
		
		if (null != criteria.getCourseStatus()) {
			conditions.append(" and course_m.course_main_status = ? ");
			params.add(criteria.getCourseStatus());
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by department.dep_code, course_m.course_code asc ) AS row_num, ");
		sb.append(" department.dep_id, department.dep_name_short_th, department.dep_name_short_en, course_m.course_code, course_m.course_name_th, course_m.course_name_en, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(course_m.course_name_th,'')) as full_course_th, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(course_m.course_name_en,'')) as full_course_en, ");
		sb.append(" count(coursepublic_m.coursepublic_id) as count_round, ");
		sb.append(" course_l.create_date as date_send, ");
		sb.append(" concat(users.firstname_th, ' ',users.lastname_th) as user_send_th, ");
		sb.append(" concat(users.firstname_en, ' ',users.lastname_en) as user_send_en, ");
		sb.append(" course_m.course_main_status as course_status, course_sts.name_lo  as status_th, course_sts.name_en  as status_en ");
		sb.append(" from course_main course_m ");

		sb.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id ");

		sb.append(" left join ( ");
		sb.append(" select la.course_id, la.create_by_id , date(la.create_date) as create_date ");
		sb.append(" from course_log la ");
		sb.append(" join course_log lb on la.course_id = lb.course_id ");
		sb.append(" where la.course_main_status = 30010003 and lb.course_main_status = 30010003 and la.active_flag = true and lb.active_flag = true ");
		sb.append(" group by la.course_id, la.create_by_id, la.create_date ");
		sb.append(" having la.create_date = max(lb.create_date) ");
		sb.append(" ) course_l on  course_m.course_id = course_l.course_id ");
		
		sb.append(" left join coursepublic_main coursepublic_m on  coursepublic_m.course_id = course_m.course_id  and coursepublic_m.active_flag = true ");
		sb.append(" left join aut_user users on course_l.create_by_id = users.user_id ");
		sb.append(" left join lookup_catalog as course_sts on course_m.course_main_status = course_sts.catalog_id  and course_sts.parent_id = 30010000 ");
		
		sb.append(WHERE);
		sb.append(" and course_m.active_flag = true ");
		
		sb.append(conditions.toString());
		
		sb.append(" group by department.dep_id, department.dep_name_short_th, department.dep_name_short_en, course_m.course_id, course_m.course_code, course_m.course_name_th, course_m.course_name_en, users.firstname_th, users.lastname_th, users.firstname_en, users.lastname_en, course_l.create_date, course_sts.name_lo, course_sts.name_en ");
		sb.append(" order by department.dep_code, course_m.course_code asc ");
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);

				List<ReportOfferedCourseData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportOfferedCourseData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append(" select count(*) from ( ");
				count.append(" select ");
				count.append(" department.dep_id, department.dep_name_short_th, department.dep_name_short_en, course_m.course_code, course_m.course_name_th, course_m.course_name_en, ");
				count.append(" concat(course_m.course_code,'-',coalesce(course_m.course_name_th,'')) as full_course_th, ");
				count.append(" concat(course_m.course_code,'-',coalesce(course_m.course_name_en,'')) as full_course_en, ");
				count.append(" count(coursepublic_m.coursepublic_id) as count_round, ");
				count.append(" course_l.create_date as date_send, ");
				count.append(" concat(users.firstname_th, ' ',users.lastname_th) as user_send_th, ");
				count.append(" concat(users.firstname_en, ' ',users.lastname_en) as user_send_en, ");
				count.append(" course_m.course_main_status as course_status, course_sts.name_lo  as status_th, course_sts.name_en  as status_en ");
				count.append(" from course_main course_m ");

				count.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id ");

				count.append(" left join ( ");
				count.append(" select la.course_id, la.create_by_id , date(la.create_date) as create_date ");
				count.append(" from course_log la ");
				count.append(" join course_log lb on la.course_id = lb.course_id ");
				count.append(" where la.course_main_status = 30010003 and lb.course_main_status = 30010003 and la.active_flag = true and lb.active_flag = true ");
				count.append(" group by la.course_id, la.create_by_id, la.create_date ");
				count.append(" having la.create_date = max(lb.create_date) ");
				count.append(" ) course_l on  course_m.course_id = course_l.course_id ");
				
				count.append(" left join coursepublic_main coursepublic_m on  coursepublic_m.course_id = course_m.course_id  and coursepublic_m.active_flag = true ");
				count.append(" left join aut_user users on course_l.create_by_id = users.user_id ");
				count.append(" left join lookup_catalog as course_sts on course_m.course_main_status = course_sts.catalog_id  and course_sts.parent_id = 30010000 ");
				
				count.append(WHERE);
				count.append(" and course_m.active_flag = true ");
				
				count.append(conditions.toString());
				
				count.append(" group by department.dep_id, department.dep_name_short_th, department.dep_name_short_en, course_m.course_id, course_m.course_code, course_m.course_name_th, course_m.course_name_en, users.firstname_th, users.lastname_th, users.firstname_en, users.lastname_en, course_l.create_date, course_sts.name_lo, course_sts.name_en ");
				count.append(" order by department.dep_code, course_m.course_code asc ");
				count.append(" ) tb ");
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
				
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}

// report 2
	public Map<String, Object> findCanceledCourseReport(ReportCanceledCourseData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getRequestCancelDateList() && !criteria.getRequestCancelDateList().isEmpty()) {
			if (null == criteria.getRequestCancelDateList().get(1)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				conditions.append(" and coursepublic_m.request_cancel_date = ?::timestamp ");
				params.add(sdf.format(criteria.getRequestCancelDateList().get(0)));
			} else {
				conditions.append(" and coursepublic_m.request_cancel_date between ?::timestamp and ?::timestamp ");
	            params.add(CommonUtils.convertDateSqlDate(criteria.getRequestCancelDateList().get(0), true, false));
	            params.add(CommonUtils.convertDateSqlDate(criteria.getRequestCancelDateList().get(1), false, true));
			}
        }
		
		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and (concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}		
		
		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by department.dep_code ,  course_m.course_code asc ) AS row_num,  ");
		sb.append(" coursepublic_m.coursepublic_id ,coursepublic_m.request_cancel_date, course_m.dep_id_level1, department.dep_name_short_th, department.dep_name_short_en, course_m.course_code, coursepublic_m.public_name_th, coursepublic_m.public_name_en, coursepublic_m.course_generation ,");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) as full_course_th,  ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) as full_course_en,  ");
		sb.append(" coalesce(register.member_count,0) as register_count,  coalesce(register.payment_amount,0) as remain_amount");
		
		sb.append(" from coursepublic_main coursepublic_m  ");
		sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id  ");
		sb.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id  ");
		sb.append(" left join ( select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount  ");
		sb.append(" from member_course member_c left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id  ");
		sb.append(" where member_c.active_flag = true and member_c.study_status in (30016004) and finance_p.payment_status = 30033004 "); //////
		sb.append(" group by member_c.coursepublic_id ) ");
		sb.append(" register on coursepublic_m.coursepublic_id = register.coursepublic_id    ");
		
		sb.append(WHERE);
		sb.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014005)  ");
		sb.append(conditions.toString());
		
		sb.append(" order by department.dep_code,  course_m.course_code asc ");
	
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
	
				List<ReportCanceledCourseData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportCanceledCourseData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append(" select count(*) from coursepublic_main coursepublic_m ");
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id  ");
				count.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id  ");
				count.append(" left join ( select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount  ");
				count.append(" from member_course member_c left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id  ");
				count.append(" where member_c.active_flag = true and member_c.study_status in (30016004) and finance_p.payment_status = 30033004 ");
				count.append(" group by member_c.coursepublic_id ) ");
				count.append(" register on coursepublic_m.coursepublic_id = register.coursepublic_id    ");
				
				count.append(WHERE);
				count.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014005)  ");
				
				count.append(conditions.toString());
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
				
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;	
	}
	
// report 3
	public Map<String, Object> findEnrollmentListReport(ReportEnrollmentListData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and (concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getFullNameTh())) {
			conditions.append(" and (concat(COALESCE(prefix_n.prefixname_name_th,''), COALESCE(member_i.member_firstname_th,''), ' ', COALESCE(member_i.member_lastname_th,'') ) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getReceiptNo())) {
			conditions.append(" and finance_p.receipt_no ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getReceiptNo(), true, true));
		}
		
		if (null != criteria.getReceiptDateList() && !criteria.getReceiptDateList().isEmpty()) {
			if (null == criteria.getReceiptDateList().get(1)) {				
				conditions.append(" and finance_p.receipt_date between ?::timestamp and ?::timestamp ");
	            params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), true, false));
	            params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), false, true));
			} else {
				conditions.append(" and finance_p.receipt_date between ?::timestamp and ?::timestamp ");
	            params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(0), true, false));
	            params.add(CommonUtils.convertDateSqlDate(criteria.getReceiptDateList().get(1), false, true));
			}
        }
		
		if (null != criteria.getPayType()) {
			conditions.append(" and finance_p.payment_type = ? ");
			params.add(criteria.getPayType());
		}

		if (null != criteria.getTransactionDatetimeList() && !criteria.getTransactionDatetimeList().isEmpty()) {
			if (null == criteria.getTransactionDatetimeList().get(1)) {
				conditions.append(" and finance_p.transaction_datetime between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getTransactionDatetimeList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getTransactionDatetimeList().get(0), false, true));
			} else {
				conditions.append(" and finance_p.transaction_datetime between ?::timestamp and ?::timestamp " );
				params.add(CommonUtils.convertDateSqlDate(criteria.getTransactionDatetimeList().get(0), true, false));
				params.add(CommonUtils.convertDateSqlDate(criteria.getTransactionDatetimeList().get(1), false, true));
			}
		}
		
		if (null != criteria.getStudyStatus()) {
			conditions.append(" and member_c.study_status = ? ");
			params.add(criteria.getStudyStatus());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by course_m.course_code asc ) AS row_num, ");
		sb.append(" member_c.member_course_id, course_m.course_code, coursepublic_m.public_name_th, coursepublic_m.public_name_en, ");        
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) as full_course_th, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) as full_course_en, ");
		sb.append(" member_i.member_id, prefix_n.prefixname_name_th, prefix_n.prefixname_name_en, member_i.member_firstname_th, member_i.member_firstname_en, member_i.member_lastname_th, member_i.member_lastname_en, ");
		sb.append(" concat(COALESCE(prefix_n.prefixname_name_th,''), COALESCE(member_i.member_firstname_th,''), ' ', COALESCE(member_i.member_lastname_th,'') )  as full_name_th, ");
		sb.append(" concat(COALESCE(prefix_n.prefixname_name_en,''), COALESCE(member_i.member_firstname_en,''), ' ', COALESCE(member_i.member_lastname_en,'') )  as full_name_en,");
		sb.append(" finance_p.receipt_no, finance_p.receipt_date, finance_p.payment_type as pay_type, pay_type.name_lo as pay_type_th, pay_type.name_en  as pay_type_en, finance_p.payment_amount, date(finance_p.transaction_datetime) as transaction_datetime, member_c.study_status, study_sts.name_lo as study_status_th, study_sts.name_en as study_status_en ");
		sb.append(" from member_course member_c  ");
		
		sb.append(" join coursepublic_main coursepublic_m on member_c.coursepublic_id = coursepublic_m.coursepublic_id ");
		sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
		sb.append(" join member_info member_i on member_c.member_id = member_i.member_id");
		sb.append(" left join sys_prefixname prefix_n on member_i.prefixname_id = prefix_n.prefixname_id ");
		sb.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id and finance_p.active_flag = true ");
		sb.append(" left join lookup_catalog as pay_type on finance_p.payment_type = pay_type.catalog_id  and pay_type.parent_id = 30023000 ");
		sb.append(" left join lookup_catalog as study_sts on member_c.study_status = study_sts.catalog_id  and study_sts.parent_id = 30016000 ");
		
		sb.append(WHERE);
		sb.append("and member_c.active_flag = true and coursepublic_m.active_flag = true ");
		
		sb.append(conditions.toString());
		
		sb.append(" order by course_m.course_code asc ");
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
		
				List<ReportEnrollmentListData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportEnrollmentListData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append("select count(*) from member_course member_c ");
				count.append(" join coursepublic_main coursepublic_m on member_c.coursepublic_id = coursepublic_m.coursepublic_id ");
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
				count.append(" join member_info member_i on member_c.member_id = member_i.member_id");
				count.append(" left join sys_prefixname prefix_n on member_i.prefixname_id = prefix_n.prefixname_id ");
				count.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id and finance_p.active_flag = true ");
				count.append(" left join lookup_catalog as pay_type on finance_p.payment_type = pay_type.catalog_id  and pay_type.parent_id = 30023000 ");
				count.append(" left join lookup_catalog as study_sts on member_c.study_status = study_sts.catalog_id  and study_sts.parent_id = 30016000 ");
				
				count.append(WHERE);
				count.append("and member_c.active_flag = true and coursepublic_m.active_flag = true ");
				
				count.append(conditions.toString());
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
		
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}

// report 4
	public Map<String, Object> findCourseEnrollmentReport(ReportCourseEnrollmentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and (concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}	
		
		if (null != criteria.getCourseStatus()) {
			conditions.append(" and  coursepublic_m.coursepublic_status = ? ");
			params.add(criteria.getCourseStatus());
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by coursepublic_m.create_date desc , course_m.course_code , coursepublic_m.course_generation desc ) AS row_num, ");
		sb.append(" course_m.course_code, coursepublic_m.public_name_th, coursepublic_m.public_name_en, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) as full_course_th, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) as full_course_en, ");
		sb.append(" coursepublic_m.max_enroll, register.member_count as register_amount, completed_regis.member_count as completed_amount, coursepublic_m.coursepublic_status as course_status, course_sts.name_lo  as status_th, course_sts.name_en  as status_en ");
        sb.append(" from coursepublic_main coursepublic_m ");
       
        sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
		
        sb.append(" left join ( ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count from member_course member_c ");
		sb.append(" where member_c.active_flag = true group by member_c.coursepublic_id ");
		sb.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
		
		sb.append(" left join ( ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count from member_course member_c  ");
		sb.append(" where member_c.active_flag = true and member_c.study_status in (30016001, 30016002) group by member_c.coursepublic_id ");
		sb.append(" ) completed_regis on coursepublic_m.coursepublic_id = completed_regis.coursepublic_id ");
		
		sb.append(" left join lookup_catalog course_sts on coursepublic_m.coursepublic_status = course_sts.catalog_id  and course_sts.parent_id = 30014000 ");
		
		sb.append(WHERE);
		sb.append("and ( register.member_count is not null or coursepublic_m.coursepublic_status in (30014003, 30014006) ) ");
		
		sb.append(conditions.toString());
		
		sb.append(" order by coursepublic_m.create_date desc , course_m.course_code , coursepublic_m.course_generation desc ");
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
		
				List<ReportCourseEnrollmentData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportCourseEnrollmentData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append(" select count(*) from coursepublic_main coursepublic_m ");
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
				
				count.append(" left join ( ");
		        count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count from member_course member_c ");
				count.append(" where member_c.active_flag = true group by member_c.coursepublic_id ");
				count.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
				
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count from member_course member_c  ");
				count.append(" where member_c.active_flag = true and member_c.study_status in (30016001, 30016002) group by member_c.coursepublic_id ");
				count.append(" ) completed_regis on coursepublic_m.coursepublic_id = completed_regis.coursepublic_id ");
				
				count.append(" left join lookup_catalog course_sts on coursepublic_m.coursepublic_status = course_sts.catalog_id  and course_sts.parent_id = 30014000 ");
				
				count.append(WHERE);
				count.append("and ( register.member_count is not null or coursepublic_m.coursepublic_status in (30014003, 30014006) ) ");
				
				count.append(conditions.toString());
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
		
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}
	
// report 5
	public Map<String, Object> findCoursePaymentReport(ReportCoursePaymentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and (concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by course_m.course_code asc ) AS row_num, ");
		sb.append(" course_m.course_code, coursepublic_m.public_name_th, coursepublic_m.public_name_en, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) as full_course_th, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) as full_course_en, ");
		sb.append(" coalesce(register.member_count,0) as register_count, ");
		sb.append(" coalesce(register.payment_amount,0) as register_amount, ");
		sb.append(" coalesce(pay.payment_amount,0) as pay_amount, ");
		sb.append(" coalesce(refund.payment_amount,0) as refund_amount,");
		sb.append(" coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) as remain_amount ");
		sb.append(" from coursepublic_main coursepublic_m ");
		
		sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
		
		sb.append(" left join ( ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append(" from member_course member_c ");
		sb.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append(" where member_c.active_flag = true ");
		sb.append(" group by member_c.coursepublic_id ");
		sb.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
		
		sb.append(" left join ( ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append(" from member_course member_c");
		sb.append("	join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append("	where member_c.active_flag = true and member_c.payment_status = 30017002 ");
		sb.append("	group by member_c.coursepublic_id ");
		sb.append("	) pay on coursepublic_m.coursepublic_id = pay.coursepublic_id ");
		
		sb.append(" left join ( ");
		sb.append("	select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append("	from member_course member_c ");
		sb.append("	join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append("	where member_c.active_flag = true and member_c.payment_status = 30016004 ");
		sb.append("	group by member_c.coursepublic_id ");
		sb.append("	) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
		
		sb.append(WHERE);
		sb.append("and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
		
		sb.append(conditions.toString());
		
		sb.append(" order by course_m.course_code asc ");
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
		
				List<ReportCoursePaymentData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportCoursePaymentData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append("select count(*) from coursepublic_main coursepublic_m ");
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
				
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c ");
				count.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append(" where member_c.active_flag = true ");
				count.append(" group by member_c.coursepublic_id ");
				count.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
				
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c");
				count.append("	join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append("	where member_c.active_flag = true and member_c.payment_status = 30017002 ");
				count.append("	group by member_c.coursepublic_id ");
				count.append("	) pay on coursepublic_m.coursepublic_id = pay.coursepublic_id ");
				
				count.append(" left join ( ");
				count.append("	select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append("	from member_course member_c ");
				count.append("	join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append("	where member_c.active_flag = true and member_c.payment_status = 30016004 ");
				count.append("	group by member_c.coursepublic_id ");
				count.append("	) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
				
				count.append(WHERE);
				count.append("and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
				
				count.append(conditions.toString());
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
		
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}
	
	
// report 6
	
	public Map<String, Object> findEnrollmentAndPaymentReport(ReportEnrollmentAndPaymentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getDepNameShortTh())) {
			conditions.append(" and (department.dep_name_short_th ilike ? or department.dep_name_short_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getFullCourseTh())) {
			conditions.append(" and (concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) ilike ? or concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullCourseTh(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by department.dep_code ,  course_m.course_code asc ) AS row_num, ");
		sb.append(" course_m.dep_id_level1, department.dep_name_short_th, department.dep_name_short_en, course_m.course_code, coursepublic_m.public_name_th, coursepublic_m.public_name_en, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_th,'')) as full_course_th, ");
		sb.append(" concat(course_m.course_code,'-',coalesce(coursepublic_m.public_name_en,'')) as full_course_en, ");
		sb.append(" coalesce(register.member_count,0) as register_count, ");
		sb.append(" coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) as remain_amount ");
		sb.append(" from coursepublic_main coursepublic_m ");
		
		sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
		
		sb.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id ");
		sb.append(" left join ( ");
		sb.append("	select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append("	from member_course member_c ");
		
		sb.append("	left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append("	where member_c.active_flag = true ");
		sb.append("	group by member_c.coursepublic_id ");
		sb.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");		
		
		sb.append(" left join ( ");
		sb.append("	select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append("	from member_course member_c ");
		sb.append("	join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append("	where member_c.active_flag = true and member_c.payment_status = 30016004 ");
		sb.append("	group by member_c.coursepublic_id ");
		sb.append(" ) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
		
		sb.append(WHERE);
		sb.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
		
		sb.append(conditions.toString());
		
		sb.append(" order by department.dep_code,  course_m.course_code asc ");
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
		
				List<ReportEnrollmentAndPaymentData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportEnrollmentAndPaymentData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append(" select count(*) from coursepublic_main coursepublic_m ");
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id ");
				
				count.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id ");
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c ");
				
				count.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append(" where member_c.active_flag = true ");
				count.append(" group by member_c.coursepublic_id ");
				count.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");		
				
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c ");
				count.append(" join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append(" where member_c.active_flag = true and member_c.payment_status = 30016004 ");
				count.append(" group by member_c.coursepublic_id ");
				count.append(" ) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
				
				count.append(WHERE);
				count.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
				
				count.append(conditions.toString());
			
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
		
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}

// report 7
	public Map<String, Object> findDepartmentIncomeReport(ReportDepartmentIncomeData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
//		if (null != criteria.getResultDateList() && !criteria.getResultDateList().isEmpty()) {
//			if (null == criteria.getResultDateList().get(1)) {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				conditions.append(" and result_date = ? ");
//				params.add(sdf.format(criteria.getResultDateList().get(0)));
//			} else {
//				conditions.append(" and result_date between ?::timestamp and ?::timestamp " );
//				params.add(CommonUtils.convertDateSqlDate(criteria.getResultDateList().get(0), true, false));
//				params.add(CommonUtils.convertDateSqlDate(criteria.getResultDateList().get(1), false, true));
//			}
//		}
		
		if (StringUtils.isNoneBlank(criteria.getDepNameShortTh())) {
			conditions.append(" and (department.dep_name_short_th ilike ? or department.dep_name_short_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameShortTh(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by department.dep_code asc ) AS row_num, ");
		sb.append(" date(now()) as result_date, department.dep_id, department.dep_name_short_th, department.dep_name_short_en, ");
		sb.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) as remain_amount, ");
		sb.append(" coursepublic_m.cost_share_dep_percent, ");
		sb.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_dep_percent,0)/100.00)  as cost_share_dep_amount, ");
		sb.append(" coursepublic_m.cost_share_global_percent, ");
		sb.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_global_percent,0)/100.00) as cost_share_global_amount, ");
		sb.append(" coursepublic_m.cost_share_center_percent, ");
		sb.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_center_percent,0)/100.00) as cost_share_center_amount ");
		sb.append(" from coursepublic_main coursepublic_m ");
				
		sb.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id  ");
		sb.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id  ");

		sb.append(" left join (  ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append(" from member_course member_c ");
		sb.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append(" where member_c.active_flag = true ");
		sb.append(" group by member_c.coursepublic_id ");
		sb.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
		
		sb.append(" left join ( ");
		sb.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
		sb.append(" from member_course member_c ");
		sb.append(" join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
		sb.append(" where member_c.active_flag = true and member_c.payment_status = 30016004 ");
		sb.append(" group by member_c.coursepublic_id ");
		sb.append(" ) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
		
		sb.append(WHERE);
		sb.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
		
		sb.append(conditions.toString());
		
		sb.append(" group by department.dep_id, department.dep_name_short_th, department.dep_name_short_en, coursepublic_m.cost_share_dep_percent, coursepublic_m.cost_share_global_percent, coursepublic_m.cost_share_center_percent ");
		sb.append(" order by department.dep_code ");
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_SEARCH.equals(criteria.getMode())) {			
				sb.append(LIMIT);
		
				List<ReportDepartmentIncomeData> entries = jdbcTemplate.query(sb.toString(),
						BeanPropertyRowMapper.newInstance(ReportDepartmentIncomeData.class),
						CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
				
				StringBuilder count = new StringBuilder();
				count.append(" select count(*) from ( ");
				count.append(" select row_number() OVER (order by department.dep_code asc ) AS row_num, ");
				count.append(" date(now()) as result_date, department.dep_id, department.dep_name_short_th, department.dep_name_short_en, ");
				count.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) as remain_amount, ");
				count.append(" coursepublic_m.cost_share_dep_percent, ");
				count.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_dep_percent,0)/100.00)  as cost_share_dep_amount, ");
				count.append(" coursepublic_m.cost_share_global_percent, ");
				count.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_global_percent,0)/100.00) as cost_share_global_amount, ");
				count.append(" coursepublic_m.cost_share_center_percent, ");
				count.append(" sum( ( coalesce(register.payment_amount,0)-coalesce(refund.payment_amount,0) ) ) * (coalesce(cost_share_center_percent,0)/100.00) as cost_share_center_amount ");
				count.append(" from coursepublic_main coursepublic_m ");
						
				count.append(" join course_main course_m on coursepublic_m.course_id = course_m.course_id  ");
				count.append(" left join mas_department department on course_m.dep_id_level1 = department.dep_id  ");
		
				count.append(" left join (  ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c ");
				count.append(" left join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append(" where member_c.active_flag = true ");
				count.append(" group by member_c.coursepublic_id ");
				count.append(" ) register on coursepublic_m.coursepublic_id = register.coursepublic_id ");
				
				count.append(" left join ( ");
				count.append(" select member_c.coursepublic_id, count(member_c.member_course_id) as member_count, sum(finance_p.payment_amount) as  payment_amount ");
				count.append(" from member_course member_c ");
				count.append(" join finance_payment finance_p on finance_p.member_course_id = member_c.member_course_id ");
				count.append(" where member_c.active_flag = true and member_c.payment_status = 30016004 ");
				count.append(" group by member_c.coursepublic_id ");
				count.append(" ) refund on coursepublic_m.coursepublic_id = refund.coursepublic_id ");
				
				count.append(WHERE);
				count.append(" and coursepublic_m.active_flag = true and coursepublic_m.coursepublic_status in (30014003, 30014004, 30014006) ");
				
				count.append(conditions.toString());
				
				count.append(" group by department.dep_id, department.dep_name_short_th, department.dep_name_short_en, coursepublic_m.cost_share_dep_percent, coursepublic_m.cost_share_global_percent, coursepublic_m.cost_share_center_percent ");
				count.append(" order by department.dep_code ");
				
				count.append(" ) tb ");
				
				Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
				
				result.put(ENTRIES, entries);
				result.put(TOTAL_RECORDS, totalRecords);
		
			} else if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				result.put(QUERY, sb.toString());
				result.put(PARAMS, params.toArray());
			}
		}
		
		return result;
	}
	
	public Map<String, Object> findGradeExportist(MemberCourseData memberCourseData) {
        LOG.info(">>>>>>>>>>>>findGradeExportist<<<<<<<<<<<<<<");
        
        Map<String, Object> result = new HashMap<>();
        if (MODE_EXPORT_EXCEL_BASE64.equals(memberCourseData.getMode())) {
			
			StringBuilder sb = new StringBuilder();
			StringBuilder conditions = new StringBuilder();
			List<Object> params = new ArrayList<>();
			sb.append("select row_number() over (order by mc.member_course_id) row_num,mc.*,mi.member_no, mi.member_id ,sp.prefixname_code,sp.prefixname_name_th,sp.prefixname_name_en,  \n" +
						"mi.member_firstname_th,mi.member_lastname_th ,mi.member_firstname_en ,mi.member_lastname_en,\n" +
						"lc2.name_lo as studyStatusNameLo,lc2.name_en as studyStatusNameEn\n" +
						"from member_course mc \n" +
						"join member_info mi on mc.member_id =mi.member_id \n" +
						"join sys_prefixname sp on sp.prefixname_id =mi.prefixname_id \n" +
						"join lookup_catalog lc2 on lc2.catalog_id = mc.study_status \n" +
						"where mc.study_status in (30016001, 30016002) and mc.active_flag = true");

			if (memberCourseData.getCoursepublicId() != null) {
				LOG.info("coursepublic_id::" + memberCourseData.getCoursepublicId());
				conditions.append(" and mc.coursepublic_id = ?");
				params.add(memberCourseData.getCoursepublicId());
			}
			sb.append(conditions.toString());
			sb.append(" order by mc.member_course_id");

			result.put(QUERY, sb.toString());
			result.put(PARAMS, params.toArray());
        }

        return result;
    }
           
    public Map<String, Object> findPaymentDataExportList(FinancePaymentData criteria) {
        LOG.info(">>>>>>>>>>>>findPaymentDataExportList<<<<<<<<<<<<<<");
        
        Map<String, Object> result = new HashMap<>();
        if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
			
			
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
                        sb.append(" select row_number() OVER (order by fp.payment_id asc ) AS row_num, ");
                        sb.append(" ctl.name_lo payment_type_th, ctl.name_en payment_type_en, cpm.course_id, cpm.public_name_th, cpm.public_name_en, cm.course_code, mi.member_id, mi.member_firstname_th, mi.member_firstname_en, mi.member_lastname_th, mi.member_lastname_en, ");        
                        sb.append(" fp.* ");
                        sb.append(" from finance_payment fp ");
                        sb.append(" left join lookup_catalog ctl on fp.payment_type = ctl.catalog_id ");
                        sb.append(" left join coursepublic_main cpm on fp.coursepublic_id = cpm.coursepublic_id ");
                        sb.append(" left join course_main cm on cpm.course_id = cm.course_id ");
                        sb.append(" left join member_info mi on fp.member_id = mi.member_id ");
                        sb.append(WHERE);

                        sb.append(conditions.toString());

                        sb.append(" order by payment_id asc ");

			result.put(QUERY, sb.toString());
			result.put(PARAMS, params.toArray());
        }

        return result;
    }            
	
}
