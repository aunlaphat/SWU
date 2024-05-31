package com.arg.swu.services;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CourseActivityData;
import com.arg.swu.dto.CourseActivityMethodData;
import com.arg.swu.dto.CourseAttachData;
import com.arg.swu.dto.CourseCompanyData;
import com.arg.swu.dto.CourseInstructorData;
import com.arg.swu.dto.CourseLogData;
import com.arg.swu.dto.CourseMainData;
import com.arg.swu.dto.CourseMatchingData;
import com.arg.swu.dto.CourseOccupationData;
import com.arg.swu.dto.CourseRequestAttachData;
import com.arg.swu.dto.CourseScloData;
import com.arg.swu.dto.CourseSkillData;
import com.arg.swu.dto.CourseTargetData;
import com.arg.swu.dto.CoursepublicAttachData;
import com.arg.swu.dto.CoursepublicInstructorData;
import com.arg.swu.dto.CoursepublicLogData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.DocxShortCourseCycleApprovalProposalFormData;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.dto.MasSharePercentData;
import com.arg.swu.dto.ResponseMoodleData;
import com.arg.swu.dto.SwuCurriculumData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CourseActivity;
import com.arg.swu.models.CourseActivityMethod;
import com.arg.swu.models.CourseAttach;
import com.arg.swu.models.CourseCompany;
import com.arg.swu.models.CourseInstructor;
import com.arg.swu.models.CourseLog;
import com.arg.swu.models.CourseMain;
import com.arg.swu.models.CourseMatching;
import com.arg.swu.models.CourseOccupation;
import com.arg.swu.models.CourseRequestAttach;
import com.arg.swu.models.CourseSclo;
import com.arg.swu.models.CourseSkill;
import com.arg.swu.models.CourseTarget;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.MasPersonal;
import com.arg.swu.models.MasSharePercent;
import com.arg.swu.models.SysMoodle;
import com.arg.swu.repositories.CourseActivityMethodRepository;
import com.arg.swu.repositories.CourseActivityRepository;
import com.arg.swu.repositories.CourseAttachRepository;
import com.arg.swu.repositories.CourseCompanyRepository;
import com.arg.swu.repositories.CourseInstructorRepository;
import com.arg.swu.repositories.CourseLogRepository;
import com.arg.swu.repositories.CourseMainRepository;
import com.arg.swu.repositories.CourseMatchingRepository;
import com.arg.swu.repositories.CourseOccupationRepository;
import com.arg.swu.repositories.CourseRequestAttachRepository;
import com.arg.swu.repositories.CourseScloRepository;
import com.arg.swu.repositories.CourseSkillRepository;
import com.arg.swu.repositories.CourseTargetRepository;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.MasSharePercentRepository;
import com.arg.swu.repositories.SysMoodleRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class CourseManagementService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(CourseManagementService.class);

	private final CommonService commonService;
	private final JdbcTemplate jdbcTemplate;
	private final FileStorageService fileStorageService;

	private final CourseActivityRepository courseActivityRepository;
	private final CourseActivityMethodRepository courseActivityMethodRepository;
	private final CourseAttachRepository courseAttachRepository;
	private final CourseCompanyRepository courseCompanyRepository;
	private final CourseInstructorRepository courseInstructorRepository;
	private final CourseLogRepository courseLogRepository;
	private final CourseMainRepository courseMainRepository;
	private final CourseMatchingRepository courseMatchingRepository;
	private final CourseOccupationRepository courseOccupationRepository;
	private final CoursepublicMainRepository coursepublicMainRepository;
	private final CourseRequestAttachRepository courseRequestAttachRepository;
	private final CourseScloRepository courseScloRepository;
	private final CourseSkillRepository courseSkillRepository;
	private final CourseTargetRepository courseTargetRepository;
	private final MasPersonalRepository masPersonalRepository;
	private final MasSharePercentRepository masSharePercentRepository;
	private final EntityMapperService mapperService;

	private final SysMoodleRepository sysMoodleRepository;
	private final UtilityService utilityService;


	@Value("${app.config.moodle.url}")
	private String moodleUrl;

	public Map<String, Object> findCourseActivity(CourseActivityData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and ca.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ca.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by ca.course_activity_id asc ) AS row_num, ca.*, ");
		sb.append(" (select string_agg(mcm.course_method_name_th, ', ') from course_activity_method cam left join mas_course_method mcm on cam.course_method_id = mcm.course_method_id where cam.course_activity_id = ca.course_activity_id  ) coruse_activity_method_th, ");
		sb.append(" (select string_agg(mcm.course_method_name_en, ', ') from course_activity_method cam left join mas_course_method mcm on cam.course_method_id = mcm.course_method_id where cam.course_activity_id = ca.course_activity_id  ) coruse_activity_method_en ");
		sb.append(" from course_activity ca ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by ca.course_activity_id asc ");
		sb.append(LIMIT);

		List<CourseActivityData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseActivityData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_activity ca where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseActivityMethod(CourseActivityMethodData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cam.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cam.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by cam.course_activity_method_id asc ) AS row_num, cam.* ");
		sb.append(" from course_activity_method cam ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by cam.course_activity_method_id asc ");
		sb.append(LIMIT);

		List<CourseActivityData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseActivityData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_activity_method cam where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseMain(CourseMainData criteria, AutUser userAction) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cm.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (StringUtils.isNoneBlank(criteria.getCourseNameTh())) {
			conditions.append(" and (cm.course_code ilike ? or cm.course_name_th ilike ? or cm.course_name_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getCourseNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCourseNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCourseNameTh(), true, true));
		}

		if (null != criteria.getDepIdLevel1()) {
			conditions.append(" and cm.dep_id_level1 = ? ");
			params.add(criteria.getDepIdLevel1());
		}

		if (null != criteria.getDepIdLevel2()) {
			conditions.append(" and cm.dep_id_level2 = ? ");
			params.add(criteria.getDepIdLevel2());
		}

		if (null != criteria.getCourseFormat()) {
			conditions.append(" and cm.course_format = ? ");
			params.add(criteria.getCourseFormat());
		}

		if (null != criteria.getCourseTypeId()) {
			conditions.append(" and cm.course_type_id = ? ");
			params.add(criteria.getCourseTypeId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cm.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		if (null != criteria.getCourseMainStatus()) {
			conditions.append(" and cm.course_main_status = ? ");
			params.add(criteria.getCourseMainStatus());
		}

		if (StringUtils.isNoneBlank(criteria.getDepNameThLevel1())) {
			conditions.append(" and (md1.dep_name_th ilike ? or md1.dep_name_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameThLevel1(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameThLevel1(), true, true));
		}

		if (null != criteria.getCourseMainStatus()) {
			conditions.append(" and cm.course_main_status = ? ");
			params.add(criteria.getCourseMainStatus());
		}

		if (StringUtils.isNoneBlank(criteria.getDepNameThLevel1())) {
			conditions.append(" and (md1.dep_name_th ilike ? or md1.dep_name_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameThLevel1(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameThLevel1(), true, true));
		}

		if (null != userAction) {
			if (null != userAction.getAccessLevel()) {
				/**
				 * 30025001 Super Admin
				 * 30025002 Faculty
				 * 30025003 Department
				 * 30025004 Personal
				 */
				if (userAction.getAccessLevel().equals(30025002l)) {
					conditions.append(" and ( cm.dep_id_level1 = ? or cm.create_by_id = ? ) ");
					params.add(userAction.getDepIdLevel1());
					params.add(userAction.getUserId());
				} else if (userAction.getAccessLevel().equals(30025004l) || userAction.getAccessLevel().equals(30025003l)) {
					conditions.append(" and ( exists ( ");
					conditions.append(" 	select 1 ");
					conditions.append(" 	from course_instructor ci  ");
					conditions.append(" 	where ci.course_id = cm.course_id ");
					conditions.append(" 	and ci.instructor_id = ?  ");
					conditions.append(" 	and ci.instructor_main = true ");
					conditions.append("   ) or cm.create_by_id = ? ");
					conditions.append(" ) ");
					conditions.append(" and cm.dep_id_level2 = ? ");
					params.add(userAction.getRefUserId());
					params.add(userAction.getUserId());
					params.add(userAction.getDepIdLevel2());
				}

			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by cm.course_id desc ) AS row_num, cm.*, ");

		if (StringUtils.isNotBlank(criteria.getMode())) {
			if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
				sb.append("( select count (coursepublic_id) from coursepublic_main cpm ");
				sb.append("where cpm.active_flag = true and cpm.course_id = cm.course_id ");
				sb.append(") count_coursepublic_id,");
				sb.append("( select max(create_date) from course_log cl ");
				sb.append("where cl.active_flag = true and cl.course_main_status = 30010003 and cl.course_id = cm.course_id ");
				sb.append(") max_create_date,");
			}
		}

		sb.append(" cf.name_lo course_format_th, cf.name_en course_format_en, ");
		sb.append(" cms.name_lo course_main_status_th, cms.name_en course_main_status_en, ");
		sb.append(" cns.name_lo course_new_status_th, cns.name_en course_new_status_en, ");
		sb.append(" du.name_lo duration_time_unit_th, du.name_en duration_time_unit_en, ");
		sb.append(" gf.name_lo grade_format_th, gf.name_en grade_format_en, ");
		sb.append(" ig.name_lo industry_group_th, ig.name_en industry_group_en, ");
		sb.append(" mct.course_type_code, mct.course_type_name_th, mct.course_type_name_en, ");
		sb.append(" mct.course_mapping_status, ");
		sb.append(" md1.dep_code dep_code_level1, md1.dep_name_th dep_name_th_level1, md1.dep_name_en dep_name_en_level1, md1.dep_name_short_th dep_name_short_th_level1, md1.dep_name_short_en dep_name_short_en_level1, md1.dep_name_abbr_th dep_name_abbr_th_level1, md1.dep_name_abbr_en dep_name_abbr_en_level1, ");
		sb.append(" md2.dep_code dep_code_level2, md2.dep_name_th dep_name_th_level2, md2.dep_name_en dep_name_en_level2, md2.dep_name_short_th dep_name_short_th_level2, md2.dep_name_short_en dep_name_short_en_level2, md2.dep_name_abbr_th dep_name_abbr_th_level2, md2.dep_name_abbr_en dep_name_abbr_en_level2, ");
		sb.append(" mps.name_lo mapping_status_th, mps.name_en mapping_status_en, ");
		sb.append(" concat(au.firstname_th, ' ', au.lastname_th) save_by_full_name_th, ");
		sb.append(" concat(au.firstname_en, ' ', au.lastname_en) save_by_full_name_en, ");
		sb.append(" array( ");
		sb.append(" 	select ctl.name_lo ");
		sb.append(" 	from course_target ct  ");
		sb.append(" 	left join lookup_catalog ctl on ct.target_group_id = ctl.catalog_id  ");
		sb.append(" 	where ct.course_id = cm.course_id ");
		sb.append(" ) target_th, ");
		sb.append(" array( ");
		sb.append(" 	select ctl.name_en ");
		sb.append(" 	from course_target ct  ");
		sb.append(" 	left join lookup_catalog ctl on ct.target_group_id = ctl.catalog_id  ");
		sb.append(" 	where ct.course_id = cm.course_id ");
		sb.append(" ) target_en, ");
		sb.append(" array( ");
		sb.append(" 	select mo.occupation_name_th  ");
		sb.append(" 	from course_occupation co  ");
		sb.append(" 	left join mas_occupation mo ON co.occupation_id = mo.occupation_id  ");
		sb.append(" 	where co.course_id  = cm.course_id ");
		sb.append(" ) occupation_th, ");
		sb.append(" array( ");
		sb.append(" 	select mo.occupation_name_en ");
		sb.append(" 	from course_occupation co  ");
		sb.append(" 	left join mas_occupation mo ON co.occupation_id = mo.occupation_id  ");
		sb.append(" 	where co.course_id  = cm.course_id ");
		sb.append(" ) occupation_en ");
		sb.append(" from course_main cm ");
		sb.append(" left join lookup_catalog cf on cm.course_format = cf.catalog_id ");
		sb.append(" left join lookup_catalog cms on cm.course_main_status = cms.catalog_id ");
		sb.append(" left join lookup_catalog cns on cm.course_new_status = cns.catalog_id ");
		sb.append(" left join lookup_catalog du on cm.duration_time_unit = du.catalog_id ");
		sb.append(" left join lookup_catalog gf on cm.grade_format = gf.catalog_id ");
		sb.append(" left join lookup_catalog ig on cm.industry_group_id = ig.catalog_id ");
		sb.append(" left join mas_course_type mct on cm.course_type_id = mct.course_type_id ");
		sb.append(" left join lookup_catalog mps on mct.course_mapping_status = mps.key::boolean and mps.parent_id = 30013000 ");
		sb.append(" left join mas_department md1 on cm.dep_id_level1 = md1.dep_id ");
		sb.append(" left join mas_department md2 on cm.dep_id_level2 = md2.dep_id ");
		sb.append(" left join aut_user au on cm.create_by_id = au.user_id ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by cm.course_id desc ");
		sb.append(LIMIT);

		// BeanPropertyRowMapper.newInstance(CourseMainData.class)
		List<CourseMainData> entries = jdbcTemplate.query(sb.toString(),
				(rs, row) -> {
					CourseMainData cm = new CourseMainData();

					cm.setRowNum(rs.getLong("row_num"));
					cm.setCourseId(rs.getLong("course_id"));
					cm.setCourseRefId(rs.getLong("course_ref_id"));
					cm.setCourseVersion(rs.getLong("course_version"));
					cm.setCourseNewStatus(rs.getLong("course_new_status"));
					cm.setCourseTypeId(rs.getLong("course_type_id"));
					cm.setDepIdLevel1(rs.getLong("dep_id_level1"));
					cm.setDepIdLevel2(rs.getLong("dep_id_level2"));
					cm.setCourseCode(rs.getString("course_code"));
					cm.setCourseNameTh(rs.getString("course_name_th"));
					cm.setCourseNameEn(rs.getString("course_name_en"));
					cm.setCourseDescTh(rs.getString("course_desc_th"));
					cm.setCourseDescEn(rs.getString("course_desc_en"));
					cm.setCourseHashtag(rs.getArray("course_hashtag") == null ? new String[] {} : (String[]) rs.getArray("course_hashtag").getArray());
					cm.setCourseMainStatus(rs.getLong("course_main_status"));
					cm.setForceStatus(rs.getBoolean("force_status"));
					cm.setCourseFormat(rs.getLong("course_format"));
					cm.setCourseFormatDescTh(rs.getString("course_format_desc_th"));
					cm.setCourseFormatDescEn(rs.getString("course_format_desc_en"));
					cm.setCourseTheoryH(rs.getBigDecimal("course_theory_h"));
					cm.setCourseActionH(rs.getBigDecimal("course_action_h"));
					cm.setCourseTotalH(rs.getBigDecimal("course_total_h"));
					cm.setCourseDurationTime(rs.getBigDecimal("course_duration_time"));
					cm.setDurationTimeUnit(rs.getLong("duration_time_unit"));
					cm.setGradeFormat(rs.getLong("grade_format"));
					cm.setCreditAmount(rs.getBigDecimal("credit_amount"));
					cm.setIndustryGroupId(rs.getLong("industry_group_id"));
					cm.setTargetGroupOtherStatus(rs.getBoolean("target_group_other_status"));
					cm.setTargetGroupOtherName(rs.getString("target_group_other_name"));
					cm.setCourseSpecificRequirementTh(rs.getString("course_specific_requirement_th"));
					cm.setCourseSpecificRequirementEn(rs.getString("course_specific_requirement_en"));
					cm.setRequestDate(rs.getDate("request_date"));
					cm.setActiveFlag(rs.getBoolean("active_flag"));
					cm.setCourseFormatTh(rs.getString("course_format_th"));
					cm.setCourseFormatEn(rs.getString("course_format_en"));
					cm.setCourseMainStatusTh(rs.getString("course_main_status_th"));
					cm.setCourseMainStatusEn(rs.getString("course_main_status_en"));
					cm.setCourseNewStatusTh(rs.getString("course_new_status_th"));
					cm.setCourseNewStatusEn(rs.getString("course_new_status_en"));
					cm.setDurationTimeUnitTh(rs.getString("duration_time_unit_th"));
					cm.setDurationTimeUnitEn(rs.getString("duration_time_unit_en"));
					cm.setGradeFormatTh(rs.getString("grade_format_th"));
					cm.setGradeFormatEn(rs.getString("grade_format_en"));
					cm.setIndustryGroupTh(rs.getString("industry_group_th"));
					cm.setIndustryGroupEn(rs.getString("industry_group_en"));
					cm.setCourseTypeCode(rs.getString("course_type_code"));
					cm.setCourseTypeNameTh(rs.getString("course_type_name_th"));
					cm.setCourseTypeNameEn(rs.getString("course_type_name_en"));
					cm.setDepCodeLevel1(rs.getString("dep_code_level1"));
					cm.setDepNameThLevel1(rs.getString("dep_name_th_level1"));
					cm.setDepNameEnLevel1(rs.getString("dep_name_en_level1"));
					cm.setDepNameShortThLevel1(rs.getString("dep_name_short_th_level1"));
					cm.setDepNameShortEnLevel1(rs.getString("dep_name_short_en_level1"));
					cm.setDepNameAbbrThLevel1(rs.getString("dep_name_abbr_th_level1"));
					cm.setDepNameAbbrEnLevel1(rs.getString("dep_name_abbr_en_level1"));
					cm.setDepCodeLevel2(rs.getString("dep_code_level2"));
					cm.setDepNameThLevel2(rs.getString("dep_name_th_level2"));
					cm.setDepNameEnLevel2(rs.getString("dep_name_en_level2"));
					cm.setDepNameShortThLevel2(rs.getString("dep_name_short_th_level2"));
					cm.setDepNameShortEnLevel2(rs.getString("dep_name_short_en_level2"));
					cm.setDepNameAbbrThLevel2(rs.getString("dep_name_abbr_th_level2"));
					cm.setDepNameAbbrEnLevel2(rs.getString("dep_name_abbr_en_level2"));
					cm.setMappingStatusTh(rs.getString("mapping_status_th"));
					cm.setMappingStatusEn(rs.getString("mapping_status_en"));
					cm.setSaveByFullNameTh(rs.getString("save_by_full_name_th"));
					cm.setSaveByFullNameEn(rs.getString("save_by_full_name_en"));
					cm.setTargetTh(rs.getArray("target_th") == null ? new String[] {} : (String[]) rs.getArray("target_th").getArray());
					cm.setTargetEn(rs.getArray("target_en") == null ? new String[] {} : (String[]) rs.getArray("target_en").getArray());
					cm.setOccupationTh(rs.getArray("occupation_th") == null ? new String[] {} : (String[]) rs.getArray("occupation_th").getArray());
					cm.setOccupationEn(rs.getArray("occupation_en") == null ? new String[] {} : (String[]) rs.getArray("occupation_en").getArray());
					cm.setCreateById(rs.getLong("create_by_id"));

					cm.setCourseMappingStatus(rs.getBoolean("course_mapping_status"));

					if (MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode())) {
						cm.setCountCoursepublicId(rs.getLong("count_coursepublic_id"));
						cm.setMaxCreateDate(rs.getDate("max_create_date"));
					}

					return cm;
				},
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append(" select count(*) ");
		count.append(" from course_main cm ");
		count.append(" left join lookup_catalog cf on cm.course_format = cf.catalog_id ");
		count.append(" left join lookup_catalog cms on cm.course_main_status = cms.catalog_id ");
		count.append(" left join lookup_catalog cns on cm.course_new_status = cns.catalog_id ");
		count.append(" left join lookup_catalog du on cm.duration_time_unit = du.catalog_id ");
		count.append(" left join lookup_catalog gf on cm.grade_format = gf.catalog_id ");
		count.append(" left join lookup_catalog ig on cm.industry_group_id = ig.catalog_id ");
		count.append(" left join mas_course_type mct on cm.course_type_id = mct.course_type_id ");
		count.append(" left join lookup_catalog mps on mct.course_mapping_status = mps.key::boolean and mps.parent_id = 30013000 ");
		count.append(" left join mas_department md1 on cm.dep_id_level1 = md1.dep_id ");
		count.append(" left join mas_department md2 on cm.dep_id_level1 = md2.dep_id ");
		count.append(" left join aut_user au on cm.create_by_id = au.user_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseCompany(CourseCompanyData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cc.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cc.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by cc.course_company_id asc ) AS row_num, cc.* ");
		sb.append(" from course_company cc ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by cc.course_company_id asc ");
		sb.append(LIMIT);

		List<CourseCompanyData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseCompanyData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_company cc where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseInstructor(CourseInstructorData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and ci.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ci.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by ci.instructor_main desc, ci.instructor_type asc ) AS row_num, ci.*, ");
		sb.append(" concat(mp.prefix_th, ' ', mp.firstname_th, ' ',coalesce(mp.middlename_th , ''), mp.lastname_th) fullname_th, ");
		sb.append(" concat(mp.prefix_en, ' ', mp.firstname_en, ' ',coalesce(mp.middlename_en , ''), mp.lastname_en) fullname_en, ");
		sb.append(" mp.email ");
		sb.append(" from course_instructor ci ");
		sb.append(" left join mas_personal mp on ci.instructor_id = mp.personal_id ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by ci.instructor_main desc, ci.instructor_type asc ");
		sb.append(LIMIT);

		List<CourseInstructorData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseInstructorData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		if (null != entries && !entries.isEmpty()) {
			if (StringUtils.isNotBlank(criteria.getMode())) {
				if (MODE_SEARCH.equals(criteria.getMode())) {
					entries = entries.stream().map(o -> {
						UploadFileData c = UploadFileData.builder().filename(o.getFilename()).prefix(o.getPrefix()).module(o.getModule()).build();
						boolean fileIsExists = fileStorageService.checkLinkFileIsExistsByModel(c);
						if (fileIsExists) {
							String base64 = fileStorageService.getBase64ByModel(c);
							o.setBase64(base64);
							return o;
						}
						return o;
					}).toList();
				}
			}
		}

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_instructor ci ");
		count.append(" left join mas_personal mp on ci.instructor_id = mp.personal_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> findCourseAttach(CourseAttachData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and ca.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ca.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by ca.course_attach_id asc ) AS row_num, ca.* ");
		sb.append(" from course_attach ca ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by ca.course_attach_id asc ");
		sb.append(LIMIT);

		List<CourseAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_attach ca where 1=1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseSkill(CourseSkillData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cs.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cs.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cs.course_skill_id asc ) AS row_num, cs.*, ");
		sb.append(" mgs.general_skill_name_th, mgs.general_skill_name_en, ");
		sb.append(" lc.name_lo level_name_th, lc.name_en  level_name_en ");
		sb.append(" from course_skill cs ");
		sb.append(" left join mas_general_skill mgs on cs.general_skill_id = mgs.general_skill_id ");
		sb.append(" left join lookup_catalog lc on cs.skill_level = lc.catalog_id ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cs.course_skill_id asc ");
		sb.append(LIMIT);

		List<CourseSkillData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseSkillData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_skill cs where 1=1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		StringBuilder sum = new StringBuilder();
		sum.append(" select sum(cs.skill_weight) ");
        sum.append(" from course_skill cs left join mas_general_skill mgs on cs.general_skill_id = mgs.general_skill_id ");
		sum.append(" left join lookup_catalog lc on cs.skill_level = lc.catalog_id ");
		sum.append(WHERE);
		sum.append(conditions.toString());
		Long sumWeight = jdbcTemplate.queryForObject(sum.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		result.put("sumWeight", sumWeight);

		return result;

	}

	public Map<String, Object> findCourseOccupation(CourseOccupationData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and co.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and co.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by co.course_occupation_id asc ) AS row_num, co.* ");
		sb.append(" from course_occupation co ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by co.course_occupation_id asc ");
		sb.append(LIMIT);

		List<CourseOccupationData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseOccupationData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_occupation co where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseSclo(CourseScloData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cs.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cs.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cs.course_sclo_id asc ) AS row_num, cs.* ");
		sb.append(" from course_sclo cs ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cs.course_sclo_id asc ");
		sb.append(LIMIT);

		List<CourseScloData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseScloData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_sclo cs where 1=1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseTarget(CourseTargetData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and ct.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ct.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER (order by ct.course_target_id asc ) AS row_num, ct.* ");
		sb.append(" from course_target ct ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by ct.course_target_id asc ");
		sb.append(LIMIT);

		List<CourseTargetData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseTargetData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_target ct where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseRequestAttach(CourseRequestAttachData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cra.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cra.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cra.course_request_attach_id asc ) AS row_num, cra.* ");
		sb.append(" from course_request_attach cra ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cra.course_request_attach_id asc ");
		sb.append(LIMIT);

		List<CourseRequestAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseRequestAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from course_request_attach cra where 1=1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCourseMatching(CourseMatchingData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCourseId()) {
			conditions.append(" and cm.course_id = ? ");
			params.add(criteria.getCourseId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cm.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cm.course_matching_id asc ) AS row_num, cm.*, ");
		sb.append(" ss.subject_code_th, ss.subject_code_en, ss.subject_name_th, ss.subject_name_en, ");
		sb.append(" sc.curriculum_name_th, sc.curriculum_name_en, ss.subject_credit, ");
		sb.append(" sc.owner_dep_name_th ,sc.owner_dep_name_en, ss.subject_set ");
		sb.append(" from course_matching cm ");
		sb.append(" left join swu_curriculum sc on cm.curriculum_swu_id = sc.curriculum_swu_id ");
		sb.append(" left join swu_subject ss on cm.curriculum_swu_id = ss.curriculum_swu_id and cm.subject_swu_id = ss.subject_swu_id ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cm.course_matching_id asc ");
		sb.append(LIMIT);

		List<CourseMatchingData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CourseMatchingData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append(" select count(*) from course_matching cm ");
		count.append(" left join swu_curriculum sc on cm.curriculum_swu_id = sc.curriculum_swu_id ");
		count.append(" left join swu_subject ss on cm.curriculum_swu_id = ss.curriculum_swu_id and cm.subject_swu_id = ss.subject_swu_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findSwuCurriculum(SwuCurriculumData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (StringUtils.isNoneBlank(criteria.getCurriculumSwuId())) {
			conditions.append(" and sc.curriculum_swu_id ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumSwuId(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getCurriculumNameTh())) {
			conditions.append(" and (sc.curriculum_name_th ilike ? or sc.curriculum_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumNameTh(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getSubjectCodeTh())) {
			conditions.append(" and (ss.subject_code_th ilike ? or ss.subject_code_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectCodeTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectCodeTh(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getSubjectNameTh())) {
			conditions.append(" and (ss.subject_name_th ilike ? or ss.subject_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectNameTh(), true, true));
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and sc.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		if (null != criteria.getSubjectSwuId()) {
			conditions.append(" and ss.subject_swu_id = ? ");
			params.add(criteria.getSubjectSwuId());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by sc.curriculum_swu_id asc ) AS row_num, ");
		sb.append(" concat('[', ss.subject_swu_id, ']-', ss.curriculum_swu_id) code, ");
		sb.append(" sc.curriculum_swu_id, ");
		sb.append(" sc.curriculum_mhesi_id, ");
		sb.append(" sc.curriculum_name_th, ");
		sb.append(" sc.curriculum_name_en, ");
		sb.append(" sc.credit_study, ");
		sb.append(" sc.degree_code, ");
		sb.append(" sc.degree_short_th, ");
		sb.append(" sc.degree_name_th, ");
		sb.append(" sc.degree_short_en, ");
		sb.append(" sc.degree_name_en, ");
		sb.append(" sc.major_code, ");
		sb.append(" sc.major_name_th, ");
		sb.append(" sc.major_name_en, ");
		sb.append(" sc.edu_level_code, ");
		sb.append(" sc.edu_level_name_th, ");
		sb.append(" sc.edu_level_name_en, ");
		sb.append(" sc.study_year, ");
		sb.append(" sc.dep_code, ");
		sb.append(" ss.subject_swu_id, ");
		sb.append(" ss.curriculum_swu_id, ");
		sb.append(" ss.curriculum_mhesi_id, ");
		sb.append(" ss.group_id, ");
		sb.append(" ss.group_th, ");
		sb.append(" ss.group_en, ");
		sb.append(" ss.group_detail, ");
		sb.append(" ss.minor_type_id, ");
		sb.append(" ss.minor_type_th, ");
		sb.append(" ss.subject_set, ");
		sb.append(" ss.subject_code_th, ");
		sb.append(" ss.subject_code_en, ");
		sb.append(" ss.subject_name_th, ");
		sb.append(" ss.subject_name_en, ");
		sb.append(" ss.subject_credit, ");
		sb.append(" ss.subject_type_th, ");
		sb.append(" sc.owner_dep_name_th, ");
		sb.append(" sc.owner_dep_name_en ");
		sb.append(" from swu_curriculum sc ");
		sb.append(" left join swu_subject ss on sc.curriculum_swu_id = ss.curriculum_swu_id ");
		sb.append(WHERE);
		sb.append(" and ss.curriculum_swu_id is not null ");
		sb.append(conditions.toString());
		sb.append(" order by sc.curriculum_swu_id asc ");
		sb.append(LIMIT);

		List<SwuCurriculumData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(SwuCurriculumData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from swu_curriculum sc ");
		count.append(" left join swu_subject ss on sc.curriculum_swu_id = ss.curriculum_swu_id ");
		count.append(WHERE);
		count.append(" and ss.curriculum_swu_id is not null ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findSwuCurriculumByPersonal(SwuCurriculumData criteria, Long personalId) {
		
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null == personalId) {
			result.put(ENTRIES, new ArrayList<>());
			result.put(TOTAL_RECORDS, 0l);
			return result;
		}
		
		params.add(personalId);

		if (StringUtils.isNoneBlank(criteria.getCurriculumSwuId())) {
			conditions.append(" and sc.curriculum_swu_id ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumSwuId(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getCurriculumNameTh())) {
			conditions.append(" and (sc.curriculum_name_th ilike ? or sc.curriculum_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCurriculumNameTh(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getSubjectCodeTh())) {
			
			conditions.append(" and (ss.subject_code_th || '-' || ss.subject_set  ilike ? or ss.subject_code_en || '-' || ss.subject_set ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectCodeTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectCodeTh(), true, true));
		}

		if (StringUtils.isNoneBlank(criteria.getSubjectNameTh())) {
			conditions.append(" and (ss.subject_name_th ilike ? or ss.subject_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSubjectNameTh(), true, true));
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and sc.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		if (null != criteria.getSubjectSwuId()) {
			conditions.append(" and ss.subject_swu_id = ? ");
			params.add(criteria.getSubjectSwuId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getOwnerDepNameTh())) {
			conditions.append(" and (sc.owner_dep_name_th ilike ? or sc.owner_dep_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getOwnerDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getOwnerDepNameTh(), true, true));
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by subject_code_en, ss.curriculum_swu_id ) AS row_num, ");
		sb.append(" concat('[', ss.subject_swu_id, ']-', ss.curriculum_swu_id) code, ");
		sb.append(" sc.curriculum_swu_id, ");
		sb.append(" sc.curriculum_mhesi_id, ");
		sb.append(" sc.curriculum_name_th, ");
		sb.append(" sc.curriculum_name_en, ");
		sb.append(" sc.credit_study, ");
		sb.append(" sc.degree_code, ");
		sb.append(" sc.degree_short_th, ");
		sb.append(" sc.degree_name_th, ");
		sb.append(" sc.degree_short_en, ");
		sb.append(" sc.degree_name_en, ");
		sb.append(" sc.major_code, ");
		sb.append(" sc.major_name_th, ");
		sb.append(" sc.major_name_en, ");
		sb.append(" sc.edu_level_code, ");
		sb.append(" sc.edu_level_name_th, ");
		sb.append(" sc.edu_level_name_en, ");
		sb.append(" sc.study_year, ");
		sb.append(" sc.dep_code, ");
		sb.append(" ss.subject_swu_id, ");
		sb.append(" ss.curriculum_swu_id, ");
		sb.append(" ss.curriculum_mhesi_id, ");
		sb.append(" ss.group_id, ");
		sb.append(" ss.group_th, ");
		sb.append(" ss.group_en, ");
		sb.append(" ss.group_detail, ");
		sb.append(" ss.minor_type_id, ");
		sb.append(" ss.minor_type_th, ");
		sb.append(" ss.subject_set, ");
		sb.append(" ss.subject_code_th, ");
		sb.append(" ss.subject_code_en, ");
		sb.append(" ss.subject_name_th, ");
		sb.append(" ss.subject_name_en, ");
		sb.append(" ss.subject_credit, ");
		sb.append(" ss.subject_type_th, ");
		sb.append(" sc.owner_dep_name_th, ");
		sb.append(" sc.owner_dep_name_en ");
		sb.append(" from swu_subject ss ");
		sb.append(" join swu_curriculum sc on ss.curriculum_swu_id = sc.curriculum_swu_id and ss.curriculum_mhesi_id = sc.curriculum_mhesi_id ");
		sb.append(" join mas_personal mp on 1 = 1 ");
		sb.append(" join aut_user au on au.active_flag = true and au.ref_user_type = 30032001 and au.ref_user_id = mp.personal_id ");
		sb.append(" join lookup_catalog al on au.access_level = al.catalog_id ");
		sb.append(" left join mas_department dep_child on (case when sc.owner_dep_code is not null then sc.owner_dep_code else sc.dep_code end) = dep_child.dep_code and dep_child.dep_parent_id != 0 ");
		sb.append(" left join mas_department dep_parent on (case when sc.owner_faculty_code is not null then sc.owner_faculty_code else sc.dep_code end) = dep_parent.dep_code and dep_parent.dep_parent_id = 0 ");
		sb.append(" left join mas_department perdep_child on perdep_child.dep_id = au.dep_id_level2 and dep_child.dep_parent_id != 0 ");
		sb.append(" left join mas_department perdep_parent on perdep_parent.dep_id = au.dep_id_level1 and dep_parent.dep_parent_id = 0 ");
		sb.append(" where mp.personal_id = ? ");
		sb.append(" and ( case ");
		sb.append("   when au.access_level in ( 30025004,30025003) then sc.owner_dep_code ");
		sb.append("   when au.access_level in ( 30025002 ) then sc.owner_faculty_code ");
		sb.append("   else '1' end ");
		sb.append(" ) = ( case ");
		sb.append("   when au.access_level in ( 30025004, 30025003) then perdep_child.dep_code ");
		sb.append("   when au.access_level in ( 30025002 ) then perdep_parent.dep_code ");
		sb.append("   else '1' end ");
		sb.append(" ) ");
		sb.append(conditions);
		sb.append(" order by subject_code_en, ss.curriculum_swu_id ");
		sb.append(LIMIT);

		List<SwuCurriculumData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(SwuCurriculumData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append(" select count(*) ");
		count.append(" from swu_subject ss ");
		count.append(" join swu_curriculum sc on ss.curriculum_swu_id = sc.curriculum_swu_id and ss.curriculum_mhesi_id = sc.curriculum_mhesi_id ");
		count.append(" join mas_personal mp on 1 = 1 ");
		count.append(" join aut_user au on au.active_flag = true and au.ref_user_type = 30032001 and au.ref_user_id = mp.personal_id ");
		count.append(" join lookup_catalog al on au.access_level = al.catalog_id ");
		count.append(" left join mas_department dep_child on (case when sc.owner_dep_code is not null then sc.owner_dep_code else sc.dep_code end) = dep_child.dep_code and dep_child.dep_parent_id != 0 ");
		count.append(" left join mas_department dep_parent on (case when sc.owner_faculty_code is not null then sc.owner_faculty_code else sc.dep_code end) = dep_parent.dep_code and dep_parent.dep_parent_id = 0 ");
		count.append(" left join mas_department perdep_child on perdep_child.dep_id = au.dep_id_level2 and dep_child.dep_parent_id != 0 ");
		count.append(" left join mas_department perdep_parent on perdep_parent.dep_id = au.dep_id_level1 and dep_parent.dep_parent_id = 0 ");
		count.append(" where mp.personal_id = ? ");
		count.append(" and ( case ");
		count.append("   when au.access_level in ( 30025004,30025003) then sc.owner_dep_code ");
		count.append("   when au.access_level in ( 30025002 ) then sc.owner_faculty_code ");
		count.append("   else '1' end ");
		count.append(" ) = ( case ");
		count.append("   when au.access_level in ( 30025004,30025003) then perdep_child.dep_code ");
		count.append("   when au.access_level in ( 30025002 ) then perdep_parent.dep_code ");
		count.append("   else '1' end ");
		count.append(" ) ");
		count.append(conditions);
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCoursepublicMain(CoursepublicMainData criteria, AutUser userAction) {

		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getDepIdLevel1()) {
			conditions.append(" and cpm.dep_id_level1 = ? ");
			params.add(criteria.getDepIdLevel1());
		}

		if (null != criteria.getDepIdLevel2()) {
			conditions.append(" and cpm.dep_id_level2 = ? ");
			params.add(criteria.getDepIdLevel2());
		}

		if (StringUtils.isNoneBlank(criteria.getPublicNameTh())) {
			conditions.append(" and (cpm.public_name_th ilike ? or cpm.public_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPublicNameTh(), true, true));
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cpm.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		if (null != criteria.getPopularStatus()) {
			conditions.append(" and cm.popular_status = ? ");
			params.add(criteria.getPopularStatus());
		}

		if (null != criteria.getCoursepublicStatus()) {
			conditions.append(" and cpm.coursepublic_status = ? ");
			params.add(criteria.getCoursepublicStatus());
		}

		if (null != userAction) {
			if (null != userAction.getAccessLevel()) {
				/**
				 * 30025001 Super Admin
				 * 30025002 Faculty
				 * 30025003 Department
				 * 30025004 Personal
				 */
				if (userAction.getAccessLevel().equals(30025002l)) {
					conditions.append(" and ( cpm.dep_id_level1 = ? or cpm.create_by_id = ? ) ");
					params.add(userAction.getDepIdLevel1());
					params.add(userAction.getUserId());
				} else if (userAction.getAccessLevel().equals(30025004l) || userAction.getAccessLevel().equals(30025003l)) {
					conditions.append(" and ( exists ( ");
					conditions.append(" 	select 1 ");
					conditions.append(" 	from coursepublic_instructor ci  ");
					conditions.append(" 	where ci.coursepublic_id = cpm.coursepublic_id ");
					conditions.append(" 	and ci.instructor_id = ? ");
					conditions.append(" 	and ci.instructor_main = true ");
					conditions.append("   ) or cpm.create_by_id = ? ");
					conditions.append(" ) ");
					conditions.append(" and cpm.dep_id_level2 = ? ");
					params.add(userAction.getRefUserId());
					params.add(userAction.getUserId());
					params.add(userAction.getDepIdLevel2());
				}

			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cpm.coursepublic_id desc ) AS row_num, ");
		sb.append(" cm.course_code, ");
		sb.append(" cpm.*, ");
		sb.append(" lc.name_lo coursepublic_status_th, lc.name_en coursepublic_status_en, ");
		sb.append(" (select concat('/publicfile/', cpmd.prefix, '/', cpmd.module, '/', cpmd.filename) from coursepublic_media cpmd where cpmd.media_type = 30012001 and cpmd.coursepublic_id = cpm.coursepublic_id) thumbnail ");
		sb.append(" from coursepublic_main cpm  ");
		sb.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		sb.append(" left join lookup_catalog lc on cpm.coursepublic_status = lc.catalog_id ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cpm.coursepublic_id desc ");
		sb.append(LIMIT);

		List<CoursepublicMainData> entries = jdbcTemplate.query(sb.toString(),
				(rs, row) -> {
					CoursepublicMainData cpm = new CoursepublicMainData();

					cpm.setActiveFlag(rs.getBoolean("active_flag"));
					cpm.setBuasriStatus(rs.getBoolean("buasri_status"));

					Long[] certificateFormat = null;
					Array arr = rs.getArray("certificate_format");
					if (null == arr) {
						certificateFormat = new Long[] {};
					} else {
						Integer[] datas = (Integer[]) arr.getArray();
						certificateFormat = Arrays.stream(datas)
								.map(Long::valueOf)
								.toArray(Long[]::new);
					}
					cpm.setCertificateFormat(certificateFormat);

					cpm.setCostShareCenterPercent(rs.getBigDecimal("cost_share_center_percent"));
					cpm.setCostShareDepPercent(rs.getBigDecimal("cost_share_dep_percent"));
					cpm.setCostShareGlobalPercent(rs.getBigDecimal("cost_share_global_percent"));
					cpm.setCourseClassEnd(rs.getDate("course_class_end"));
					cpm.setCourseClassStart(rs.getDate("course_class_start"));
					cpm.setCourseFormat(rs.getLong("course_format"));
					cpm.setCourseFormatDescEn(rs.getString("course_format_desc_en"));
					cpm.setCourseFormatDescTh(rs.getString("course_format_desc_th"));
					cpm.setCourseGeneration(rs.getLong("course_generation"));
					cpm.setCourseId(rs.getLong("course_id"));
					cpm.setCoursePublicEnd(rs.getDate("course_public_end"));
					cpm.setCoursepublicId(rs.getLong("coursepublic_id"));
					cpm.setCoursePublicStart(rs.getDate("course_public_start"));
					cpm.setCoursepublicStatus(rs.getLong("coursepublic_status"));
					cpm.setCourseRegisEnd(rs.getDate("course_regis_end"));
					cpm.setCourseRegisStart(rs.getDate("course_regis_start"));
					cpm.setCourseTimeEn(rs.getString("course_time_en"));
					cpm.setCourseTimeTh(rs.getString("course_time_th"));
					cpm.setDepIdLevel1(rs.getLong("dep_id_level1"));
					cpm.setDepIdLevel2(rs.getLong("dep_id_level2"));
					cpm.setFeeAmount(rs.getBigDecimal("fee_amount"));
					cpm.setFeeStatus(rs.getLong("fee_status"));
					cpm.setMaxEnroll(rs.getLong("max_enroll"));
					cpm.setPopularStatus(rs.getBoolean("popular_status"));
					cpm.setPromotionAmount(rs.getBigDecimal("promotion_amount"));
					cpm.setPromotionEnd(rs.getDate("promotion_end"));
					cpm.setPromotionStart(rs.getDate("promotion_start"));
					cpm.setPublicFormat(rs.getLong("public_format"));
					cpm.setPublicNameEn(rs.getString("public_name_en"));
					cpm.setPublicNameTh(rs.getString("public_name_th"));
					cpm.setPublicStatus(rs.getBoolean("public_status"));
					cpm.setRowNum(rs.getLong("row_num"));

					/** custom */
					cpm.setCourseCode(rs.getString("course_code"));
					cpm.setCoursepublicStatusTh(rs.getString("coursepublic_status_th"));
					cpm.setCoursepublicStatusEn(rs.getString("coursepublic_status_en"));
					cpm.setThumbnail(rs.getString("thumbnail"));

					return cpm;
				},
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from coursepublic_main cpm ");
		count.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		count.append(" left join lookup_catalog lc on cpm.coursepublic_status = lc.catalog_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> findCoursepublicInstructor(CoursepublicInstructorData criteria) {

		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCoursepublicId()) {
			conditions.append(" and ci.coursepublic_id = ? ");
			params.add(criteria.getCoursepublicId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ci.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by ci.instructor_main desc, ci.instructor_type asc ) AS row_num, ci.*,");
		sb.append(" concat(mp.prefix_th, ' ', mp.firstname_th, ' ',coalesce(mp.middlename_th , ''), mp.lastname_th) fullname_th, ");
		sb.append(" concat(mp.prefix_en, ' ', mp.firstname_en, ' ',coalesce(mp.middlename_en , ''), mp.lastname_en) fullname_en, ");
		sb.append(" mp.email, ");
		sb.append(" mp.prefix_short_th, mp.prefix_short_en, ");
		sb.append(" cpm.course_format, cpm.course_format_desc_th, cpm.course_format_desc_en, cpm.course_time_th, cpm.course_time_en, cpm.certificate_format, ");
		sb.append(" cm.course_total_h, cm.grade_format, cm.credit_amount, cm.target_group_other_status, cm.target_group_other_name, cm.industry_group_id ");
		// sb.append(" ct.target_group_id ");
		// sb.append(" md.dep_name_th, md.dep_name_en ");
		// sb.append(" md1.dep_name_th, md1.dep_name_en ");
		sb.append(" from coursepublic_instructor ci ");
		sb.append(" left join mas_personal mp on ci.instructor_id = mp.personal_id ");
		sb.append(" left join coursepublic_main cpm on cpm.coursepublic_id = ci.coursepublic_id ");
		sb.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		// sb.append(" left join course_target ct on ct.course_id = cm.course_id ");
		// sb.append(" left join mas_department md on md.dep_id = mp.dep_id_level1 ");
		// sb.append(" left join mas_department md1 on md1.dep_id = mp.dep_id_level2 ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by ci.instructor_main desc, ci.instructor_type asc ");
		sb.append(LIMIT);

		List<CoursepublicInstructorData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CoursepublicInstructorData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from coursepublic_instructor ci ");
		count.append(" left join mas_personal mp on ci.instructor_id = mp.personal_id ");
		count.append(" left join coursepublic_main cpm on cpm.coursepublic_id = ci.coursepublic_id ");
		count.append(" left join course_main cm on cpm.course_id = cm.course_id ");
		// count.append(" left join course_target ct on ct.course_id = cm.course_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		if (null != entries && !entries.isEmpty()) {
			if (StringUtils.isNotBlank(criteria.getMode())) {
				if (MODE_SEARCH.equals(criteria.getMode())) {
					entries = entries.stream().map(o -> {
						UploadFileData c = UploadFileData.builder().filename(o.getFilename()).prefix(o.getPrefix()).module(o.getModule()).build();
						boolean fileIsExists = fileStorageService.checkLinkFileIsExistsByModel(c);
						if (fileIsExists) {
							String base64 = fileStorageService.getBase64ByModel(c);
							o.setBase64(base64);
							return o;
						}
						return o;
					}).toList();
				}
			}
		}

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> findCoursepublicAttach(CoursepublicAttachData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCoursepublicId()) {
			conditions.append(" and ca.coursepublic_id = ? ");
			params.add(criteria.getCoursepublicId());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ca.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by ca.coursepublic_attach_id asc ) AS row_num, ca.* ");
		sb.append(" from coursepublic_attach ca ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by ca.coursepublic_attach_id asc ");
		sb.append(LIMIT);

		List<CoursepublicAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CoursepublicAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from coursepublic_attach ca where 1=1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> findCoursepublicMedia(CoursepublicMediaData criteria) {
		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getCoursepublicId()) {
			conditions.append(" and cm.coursepublic_id = ? ");
			params.add(criteria.getCoursepublicId());
		}

		if (null != criteria.getMediaType()) {
			conditions.append(" and cm.media_type = ? ");
			params.add(criteria.getMediaType());
		}

		if (null != criteria.getActiveFlag()) {
			conditions.append(" and cm.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		if (null != criteria.getPopularStatus()) {
			conditions.append(" and cpm.popular_status = ? ");
			params.add(criteria.getPopularStatus());
		}

		if (null != criteria.getPublicStatus()) {
			conditions.append(" and cpm.public_status = ? ");
			params.add(criteria.getPublicStatus());
		}

		if (null != criteria.getCourseTypeId()) {
			conditions.append(" and cma.course_type_id = ? ");
			params.add(criteria.getCourseTypeId());
		}

		if (null != criteria.getIndustryGroupId()) {
			conditions.append(" and cma.industry_group_id = ? ");
			params.add(criteria.getIndustryGroupId());
		}

		if (null != criteria.getFeeStatus()) {
			conditions.append(" and cpm.fee_status = ? ");
			params.add(criteria.getFeeStatus());
		}

		if (null != criteria.getCourseKeyword()) {
			conditions.append(" and cpm.public_name_th like ? ");
			//Condition like for keyword
			//and (cpm.public_name_th like '%ก%' or cpm.public_name_th like '%ก%' or cma.course_name_th like '%ก%' or cma.course_name_en like '%ก%' or cma.course_desc_th  like '%ก%' or cma.course_desc_en  like '%ก%' or cma.course_format_desc_th  like '%ก%' or cma.course_format_desc_en  like '%ก%')

			params.add(CommonUtils.concatLikeParam(criteria.getCourseKeyword(), true, true));
		}

		if (StringUtils.isNotBlank(criteria.getMode())) {
			if ("course".equals(criteria.getMode())) {

				// p'o
				// find course target
				if (null != criteria.getCourseTargets() && !criteria.getCourseTargets().isEmpty()) {
					String inCodition = String.format(" and exists ( select 1 from course_target ct where cma.course_id = ct.course_id and ct.target_group_id in (%s) )", String.join(",", Collections.nCopies(criteria.getCourseTargets().size(), "?")));
					conditions.append(inCodition);
					params.addAll(criteria.getCourseTargets());
				}

				// find course format
				if (null != criteria.getCourseFormats() && !criteria.getCourseFormats().isEmpty()) {
					String inCodition = String.format(" and cpm.course_format in (%s) ", String.join(",", Collections.nCopies(criteria.getCourseFormats().size(), "?")));
					conditions.append(inCodition);
					params.addAll(criteria.getCourseFormats());
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by cm.coursepublic_media_id asc ) AS row_num, cm.*, ");
		sb.append(" cpm.*, ");
		sb.append(" cma.course_type_id, ");
		sb.append(" mct.course_type_code, mct.course_type_name_th, mct.course_type_name_en, ");
		sb.append(" cm.prefix, cm.module, cm.filename ");
		sb.append(" from coursepublic_media cm ");
		sb.append(" left join coursepublic_main cpm on cpm.coursepublic_id = cm.coursepublic_id ");
		sb.append(" left join course_main cma on cpm.course_id = cma.course_id ");
		sb.append(" left join mas_course_type mct on cma.course_type_id = mct.course_type_id ");

		if (StringUtils.isNotBlank(criteria.getMode())) {
			if ("course".equals(criteria.getMode())) {
				// p'o
				// join relate table
				// sb.append(" left join course_target ct on ct.course_id = cma.course_id ");
				// sb.append(" left join lookup_catalog ctg on ctg.catalog_id = ct.course_target_id  ");


			}
		}

		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(" order by cm.coursepublic_media_id asc ");
		sb.append(LIMIT);

		List<CoursepublicMediaData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(CoursepublicMediaData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

		StringBuilder count = new StringBuilder();
		count.append("select count(*) ");
		count.append(" from coursepublic_media cm  ");
		count.append(" left join coursepublic_main cpm on cpm.coursepublic_id = cm.coursepublic_id ");
		count.append(" left join course_main cma on cpm.course_id = cma.course_id ");
		count.append(" left join mas_course_type mct on cma.course_type_id = mct.course_type_id ");
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if ("course".equals(criteria.getMode())) {
				// p'o
				// join relate table
				// count.append(" left join course_target ct on ct.course_id = cma.course_id ");
				// count.append(" left join lookup_catalog ctg on ctg.catalog_id = ct.course_target_id  ");


			}
		}
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;

	}

	public Map<String, Object> getCountAllCourse() {

		Map<String, Object> result = new HashMap<>();

		StringBuilder count = new StringBuilder();
		count.append("select count(cm.course_id) from course_main cm ");
		count.append(WHERE);

		Long totalCourses = jdbcTemplate.queryForObject(count.toString() + " and cm.course_main_status = 30010005 ", Long.class);
		Long transferableCourses = jdbcTemplate.queryForObject(count.toString() + " and exists( select cmc.course_id from course_matching cmc where cm.course_id = cmc.course_id) ", Long.class);
		Long untransferableCourses = jdbcTemplate.queryForObject(count.toString() + " and not exists( select cmc.course_id from course_matching cmc where cm.course_id = cmc.course_id) ", Long.class);
		Long totalSkilledCourses = jdbcTemplate.queryForObject(count.toString() + " and exists ( select cs.course_id from course_skill cs where cm.course_id = cs.course_id ) ", Long.class);

		result.put("totalCourses", totalCourses);
		result.put("transferableCourses", transferableCourses);
		result.put("untransferableCourses", untransferableCourses);
		result.put("totalSkilledCourses", totalSkilledCourses);

		return result;
	}

	public DocxShortCourseCycleApprovalProposalFormData findShortCourseCycleApprovalProposalFormData(DocxShortCourseCycleApprovalProposalFormData criteria) {
		if (null == criteria.getCoursepublicId()) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select cpm.coursepublic_id, cpm.course_id, cm.course_name_th, cm.course_name_en, cpm.public_name_th, cpm.public_name_en, ");
		sql.append(" cm.course_desc_th, cm.course_desc_en, cpm.max_enroll, cpm.course_regis_start, cpm.course_regis_end, cpm.course_class_start, cpm.course_class_end, ");
		sql.append(" cpm.course_format, cf.name_lo course_format_th, cf.name_en course_format_en, cpm.course_time_th, cpm.course_time_en, ");
		sql.append(" cpm.course_format_desc_th, cpm.course_format_desc_en, cpm.fee_status, fst.name_lo  fee_status_th, fst.name_en fee_status_en, cpm.fee_amount, cpm.promotion_amount, ");
		sql.append(" ( select  ");
		sql.append(" 	(case  ");
		sql.append(" 		when ci.instructor_type != true then (select mp.email from mas_personal mp where mp.personal_id = ci.instructor_id) ");
		sql.append(" 		else ci.external_email  ");
		sql.append(" 	end )  ");
		sql.append("    from coursepublic_instructor ci where ci.coursepublic_id = cpm.coursepublic_id and ci.instructor_main = true ");
		sql.append("    order by ci.coursepublic_instructor_id desc ");
		sql.append("    limit 1  ");
		sql.append(" ) email, ");
		sql.append(" ( select  ");
		sql.append(" 	(case  ");
		sql.append(" 		when ci.instructor_type != true then (select concat(mp.prefix_th, mp.firstname_th, ' ', mp.middlename_th, ' ', mp.lastname_th) from mas_personal mp where mp.personal_id = ci.instructor_id) ");
		sql.append(" 		else ci.external_name_th ");
		sql.append(" 	end )  ");
		sql.append("    from coursepublic_instructor ci where ci.coursepublic_id = cpm.coursepublic_id and ci.instructor_main = true ");
		sql.append("    order by ci.coursepublic_instructor_id desc ");
		sql.append("    limit 1  ");
		sql.append(" ) instructor_name_th, ");
		sql.append(" ( select  ");
		sql.append(" 	(case  ");
		sql.append(" 		when ci.instructor_type != true then (select concat(mp.prefix_en, mp.firstname_en, ' ', mp.middlename_en, ' ', mp.lastname_en) from mas_personal mp where mp.personal_id = ci.instructor_id) ");
		sql.append(" 		else ci.external_name_en ");
		sql.append(" 	end )  ");
		sql.append("    from coursepublic_instructor ci where ci.coursepublic_id = cpm.coursepublic_id and ci.instructor_main = true ");
		sql.append("    order by ci.coursepublic_instructor_id desc ");
		sql.append("    limit 1  ");
		sql.append(" ) instructor_name_en ");
		sql.append(" from coursepublic_main cpm  ");
		sql.append(" left join course_main cm on cpm.course_id = cm.course_id  ");
		sql.append(" left join lookup_catalog cf on cpm.course_format = cf.catalog_id  ");
		sql.append(" left join lookup_catalog fst on cpm.fee_status = fst.catalog_id ");
		sql.append(" where cpm.coursepublic_id = ? ");
		return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<DocxShortCourseCycleApprovalProposalFormData>(DocxShortCourseCycleApprovalProposalFormData.class), criteria.getCoursepublicId());
	}

	@Transactional
	public CourseMain copyCourseMain(CourseMain main, AutUser userAction) {

		Date now = new Date();

		Long version = main.getCourseVersion() + 1l;
		Long courseRefId = main.getCourseId();

		/** รอส่ง */
		CourseMain newMain = main
				.toBuilder()
				.courseId(null)
				.courseMainStatus(30010002l)
				.courseRefId(courseRefId)
				.courseVersion(version)
				.activeFlag(true)
				.updateBy(null)
				.updateDate(null)
				.createBy(userAction)
				.createDate(now)
				.build();

		courseMainRepository.save(newMain);

		String courseCode = commonService.generateCourseCode(newMain);
		newMain.setCourseCode(courseCode);

		courseMainRepository.save(newMain);

		Long courseId = newMain.getCourseId();

		CourseLog courseLog = CourseLog
				.builder()
				.courseId(courseId)
				.courseMainStatus(30010002l)
				.activeFlag(true)
				.createBy(userAction)
				.createDate(now)
				.build();

		courseLogRepository.save(courseLog);

		List<CourseActivity> courseActivities = courseActivityRepository.findByCourseId(courseRefId);
		if (null != courseActivities && !courseActivities.isEmpty()) {
			List<CourseActivity> newCourseActivities = courseActivities.stream().map(o -> {
				CourseActivity ca = o.toBuilder()
						.courseActivityId(null)
						.courseActivityId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return ca;
			}).toList();

			courseActivityRepository.saveAll(newCourseActivities);
		}

		List<CourseActivityMethod> courseActivitieMethods = courseActivityMethodRepository.findByCourseId(courseRefId);
		if (null != courseActivitieMethods && !courseActivitieMethods.isEmpty()) {
			List<CourseActivityMethod> newCourseActivitieMethods = courseActivitieMethods.stream().map(o -> {
				CourseActivityMethod cam = o.toBuilder()
						.courseActivityMethodId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cam;
			}).toList();

			courseActivityMethodRepository.saveAll(newCourseActivitieMethods);
		}

		List<CourseAttach> courseAttachs = courseAttachRepository.findByCourseId(courseRefId);
		if (null != courseAttachs && !courseAttachs.isEmpty()) {
			List<CourseAttach> newCourseAttachs = courseAttachs.stream().map(o -> {
				CourseAttach ca = o.toBuilder()
						.courseAttachId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return ca;
			}).toList();

			courseAttachRepository.saveAll(newCourseAttachs);
		}

		List<CourseCompany> courseCompanys = courseCompanyRepository.findByCourseId(courseRefId);
		if (null != courseCompanys && !courseCompanys.isEmpty()) {
			List<CourseCompany> newCourseCompanys = courseCompanys.stream().map(o -> {
				CourseCompany cc = o.toBuilder()
						.courseCompanyId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cc;
			}).toList();

			courseCompanyRepository.saveAll(newCourseCompanys);
		}

		List<CourseInstructor> courseInstructors = courseInstructorRepository.findByCourseId(courseRefId);
		if (null != courseInstructors && !courseInstructors.isEmpty()) {
			List<CourseInstructor> newCourseInstructors = courseInstructors.stream().map(o -> {
				CourseInstructor ci = o.toBuilder()
						.courseInstructorId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return ci;
			}).toList();

			courseInstructorRepository.saveAll(newCourseInstructors);
		}

		List<CourseMatching> courseMatchings = courseMatchingRepository.findByCourseId(courseRefId);
		if (null != courseMatchings && !courseMatchings.isEmpty()) {
			List<CourseMatching> newCourseMatchings = courseMatchings.stream().map(o -> {
				CourseMatching cm = o.toBuilder()
						.courseMatchingId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cm;
			}).toList();

			courseMatchingRepository.saveAll(newCourseMatchings);
		}

		List<CourseOccupation> courseOccupations = courseOccupationRepository.findByCourseId(courseRefId);
		if (null != courseOccupations && !courseOccupations.isEmpty()) {
			List<CourseOccupation> newCourseOccupations = courseOccupations.stream().map(o -> {
				CourseOccupation co = o.toBuilder()
						.courseOccupationId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return co;
			}).toList();

			courseOccupationRepository.saveAll(newCourseOccupations);
		}

		List<CourseRequestAttach> courseRequestAttachs = courseRequestAttachRepository.findByCourseId(courseRefId);
		if (null != courseRequestAttachs && !courseRequestAttachs.isEmpty()) {
			List<CourseRequestAttach> newCourseRequestAttachs = courseRequestAttachs.stream().map(o -> {
				CourseRequestAttach cra = o.toBuilder()
						.courseRequestAttachId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cra;
			}).toList();

			courseRequestAttachRepository.saveAll(newCourseRequestAttachs);
		}

		List<CourseSclo> courseSclos = courseScloRepository.findByCourseId(courseRefId);
		if (null != courseSclos && !courseSclos.isEmpty()) {
			List<CourseSclo> newCourseSclos = courseSclos.stream().map(o -> {
				CourseSclo cs = o.toBuilder()
						.courseScloId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cs;
			}).toList();

			courseScloRepository.saveAll(newCourseSclos);
		}

		List<CourseSkill> courseSkills = courseSkillRepository.findByCourseId(courseRefId);
		if (null != courseSkills && !courseSkills.isEmpty()) {
			List<CourseSkill> newCourseSkills = courseSkills.stream().map(o -> {
				CourseSkill cs = o.toBuilder()
						.courseSkillId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return cs;
			}).toList();

			courseSkillRepository.saveAll(newCourseSkills);
		}

		List<CourseTarget> courseTargets = courseTargetRepository.findByCourseId(courseRefId);
		if (null != courseTargets && !courseTargets.isEmpty()) {
			List<CourseTarget> newCourseTargets = courseTargets.stream().map(o -> {
				CourseTarget ct = o.toBuilder()
						.courseTargetId(null)
						.courseId(courseId)
						.createBy(userAction)
						.createDate(now)
						.updateBy(null)
						.updateDate(null)
						.build();
				return ct;
			}).toList();

			courseTargetRepository.saveAll(newCourseTargets);
		}

		return newMain;
	}

	public Map<String, Object> getRegistrationList(Long coursePublicId) {

		Map<String, Object> result = new HashMap<>();
		List<Object> params = new ArrayList<>();
		StringBuilder sp = new StringBuilder();
		sp.append("select row_number() over () row_num, fp.member_id , fp.receipt_date ,mc.course_id , mc.coursepublic_id , mi.member_firstname_th , mi.member_lastname_th , ");
		sp.append("mi.member_firstname_en , mi.member_lastname_en , fp.payment_amount , fp.payment_status , fp.ref1 , fp.ref2 ");
		sp.append("from finance_payment fp ");
		sp.append("join member_course mc on mc.member_course_id = fp.member_course_id ");
		sp.append("join member_info mi on mi.member_id = mc.member_id ");
		sp.append("where mc.coursepublic_id = ? ");
		params.add(coursePublicId);

		List<FinancePaymentData> financePaymentData = jdbcTemplate.query(sp.toString(),BeanPropertyRowMapper.newInstance(FinancePaymentData.class), params.toArray());
		result.put(ENTRIES, financePaymentData);

		StringBuilder count = new StringBuilder();
		params = new ArrayList<>();
		count.append("select count(*) as count from finance_payment fp ");
		count.append("join member_course mc on mc.member_course_id = fp.member_course_id ");
		count.append("join member_info mi on mi.member_id = mc.member_id ");
		count.append("where mc.coursepublic_id = ? ");
		params.add(coursePublicId);

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> getMasSharePercentData(Long depId) {

		Map<String, Object> result = new HashMap<>();
		List<MasSharePercentData> masSharePercentData = new ArrayList<>();
		List<MasSharePercent> masSharePercent = masSharePercentRepository.findByDepId(depId);
		if(!masSharePercent.isEmpty()) {
			for(int i = 0;i < masSharePercent.size();i++){
				MasSharePercentData masSharePercentDataObj = mapperService.convertToEntity(masSharePercent.get(i), MasSharePercentData.class);
				masSharePercentData.add(masSharePercentDataObj);
			}
		}
		result.put(ENTRIES,  masSharePercentData);

		return result;
	}

	public Map<String, Object> getCoursePublicLogDataLimitOne(Long coursepublicId, Long coursepublicStatus) {
		StringBuilder sp = new StringBuilder();
		Map<String, Object> result = new HashMap<>();
		List<Object> params = new ArrayList<>();
		sp.append("select * from coursepublic_log cl where cl.coursepublic_status = ? and cl.coursepublic_id = ?");
		params.add(coursepublicStatus);
		params.add(coursepublicId);
		sp.append(" order by cl.create_date desc limit 1");
		List<CoursepublicLogData> entries = jdbcTemplate.query(sp.toString(), BeanPropertyRowMapper.newInstance(CoursepublicLogData.class),params.toArray());
		result.put(ENTRIES, entries);
		return result;
	}

	public void coreCourseCreateCourses(Long coursepublicId) {
		try {

			LOG.info("coreCourseCreateCourses -> {}", coursepublicId);

			if (moodleUrl != null && !moodleUrl.equals("skip") && null != coursepublicId) {

				CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);

				if (null == coursepublicMain) {
					LOG.info("===================[ coursepublic_main is null ]=====================");
					throw new Exception("coursepublic_main is null");
				}

				SysMoodle sysMoodle = sysMoodleRepository.findActive();

				StringBuilder url = new StringBuilder();
				url.append(sysMoodle.getDomain())
						.append("?wstoken=").append(sysMoodle.getToken())
						.append("&wsfunction=").append("core_course_create_courses")
						.append("&moodlewsrestformat=").append("json")
						.append("&courses[0][categoryid]=").append("1")
						.append("&courses[0][fullname]=").append(coursepublicMain.getPublicNameTh())
						.append("&courses[0][shortname]=").append(coursepublicMain.getPublicNameTh());

				RestTemplate restTemplate = new RestTemplate();

				LOG.info("core_course_create_courses -> {}", url);

				ResponseEntity<String> resultPesponse = restTemplate.getForEntity(url.toString(), String.class);
				String resMap = resultPesponse.getBody();
				LOG.info("core_course_create_courses response -> {}", resMap);
				if (HttpStatus.OK.equals(resultPesponse.getStatusCode())) {
					String responseBody = resultPesponse.getBody();
					Type typeList = new TypeToken<ArrayList<ResponseMoodleData>>() {
					}.getType();
					List<ResponseMoodleData> datas = new Gson().fromJson(responseBody, typeList);
					if (null != datas && !datas.isEmpty()) {
						CoursepublicMain update = coursepublicMain.toBuilder().moodleCourseId(datas.get(0).getId()).build();
						coursepublicMainRepository.save(update);
					}
				}

			} else {
				LOG.warn("Call MOODLE SKIP MODE....");
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void coreUserCreateUsers(Long personId) {
		try {
			if (moodleUrl != null && !moodleUrl.equals("skip")) {

				MasPersonal masPersonal = masPersonalRepository.findById(personId).orElse(null);

				if (null == masPersonal) return;

				String moodlePassword = utilityService.generateDefaultPassword(masPersonal.getBuasriId());

				SysMoodle sysMoodle = sysMoodleRepository.findActive();

				StringBuilder url = new StringBuilder();
				url.append(sysMoodle.getDomain())
						.append("?wstoken=").append(sysMoodle.getToken())
						.append("&wsfunction=").append("core_user_create_users")
						.append("&moodlewsrestformat=").append("json")
						.append("&users[0][username]=").append(masPersonal.getEmail())
						.append("&users[0][password]=").append(moodlePassword)
						.append("&users[0][firstname]=").append(masPersonal.getFirstnameTh())
						.append("&users[0][lastname]=").append(masPersonal.getLastnameTh())
						.append("&users[0][email]=").append(masPersonal.getEmail());

				RestTemplate restTemplate = new RestTemplate();

				LOG.info("core_user_create_users -> {}", url);

				ResponseEntity<String> resultPesponse = restTemplate.getForEntity(url.toString(), String.class);
				String resMap = resultPesponse.getBody();
				LOG.info("core_user_create_users response -> {}", resMap);
				if (HttpStatus.OK.equals(resultPesponse.getStatusCode())) {
					String responseBody = resultPesponse.getBody();
					Type typeList = new TypeToken<ArrayList<ResponseMoodleData>>() {
					}.getType();
					List<ResponseMoodleData> datas = new Gson().fromJson(responseBody, typeList);
					if (null != datas && !datas.isEmpty()) {
						MasPersonal update = masPersonal
							.toBuilder()
							.moodleUserId(datas.get(0).getId())
							.moodlePassword(utilityService.encryptAES256(moodlePassword))
							.build();
						masPersonalRepository.save(update);
					}
				}

			} else {
				LOG.warn("Call MOODLE SKIP MODE....");
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void enrolManualEnrolUsers(Long coursepublicId, Long moodleUserId) {
		try {
			if (moodleUrl != null && !moodleUrl.equals("skip")) {

				CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);

				if (null == coursepublicMain) {
					LOG.info("===================[ enrol_manual_enrol_users is null ]=====================");
					throw new Exception("enrol_manual_enrol_users is null");
				}

				SysMoodle sysMoodle = sysMoodleRepository.findActive();

				StringBuilder url = new StringBuilder();
				url.append(sysMoodle.getDomain())
						.append("?wstoken=").append(sysMoodle.getToken())
						.append("&wsfunction=").append("enrol_manual_enrol_users")
						.append("&moodlewsrestformat=").append("json")
						.append("&enrolments[0][roleid]=").append("3")
						.append("&enrolments[0][userid]=").append(String.valueOf(moodleUserId))
						.append("&enrolments[0][courseid]=").append(String.valueOf(coursepublicMain.getMoodleCourseId()));

				RestTemplate restTemplate = new RestTemplate();

				LOG.info("enrol_manual_enrol_users -> {}", url);

				ResponseEntity<Object> resultPesponse = restTemplate.getForEntity(url.toString(), Object.class);
				Object resMap = resultPesponse.getBody();
				LOG.info("enrol_manual_enrol_users response -> {}", resMap);

			} else {
				LOG.warn("Call MOODLE SKIP MODE....");
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public MasPersonalData getMoodleUserId(Long coursepublicId) {
		String sql = """
							select mp.*
				   from coursepublic_main cpm
				   join coursepublic_instructor cpi on cpm.coursepublic_id = cpi.coursepublic_id
				   join mas_personal mp on mp.personal_id = cpi.instructor_id
				where cpm.coursepublic_id = ?
				   and cpi.instructor_main = true
							""";
    	return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<MasPersonalData>(MasPersonalData.class), coursepublicId);
	}

	public List<CourseLogData> findCourseLogDataByCourseId(Long courseId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select cl.*, ");
		sql.append(" au.firstname_th || ' ' || au.lastname_th fullname_th, ");
		sql.append(" au.firstname_en || ' ' || au.lastname_en fullname_en, ");
		sql.append(" cms.name_lo status_name_th, cms.name_en status_name_en ");
		sql.append(" from course_log cl ");
		sql.append(" left join aut_user au on cl.create_by_id = au.user_id  ");
		sql.append(" left join lookup_catalog cms on cl.course_main_status = cms.catalog_id ");
		sql.append(" where cl.course_id = ? ");
		sql.append(" order by course_log_id desc ");

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CourseLogData.class), courseId);
	}

	public List<CoursepublicLogData> findCoursepublicLogDataByCoursepublicId(Long coursepublicId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select cl.*, ");
		sql.append(" au.firstname_th || ' ' || au.lastname_th fullname_th, ");
		sql.append(" au.firstname_en || ' ' || au.lastname_en fullname_en, ");
		sql.append(" cms.name_lo status_name_th, cms.name_en status_name_en ");
		sql.append(" from coursepublic_log cl ");
		sql.append(" left join aut_user au on cl.create_by_id = au.user_id ");
		sql.append(" left join lookup_catalog cms on cl.coursepublic_status = cms.catalog_id ");
		sql.append(" where cl.coursepublic_id = ? ");
		sql.append(" order by coursepublic_log_id desc ");
    	return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CoursepublicLogData.class), coursepublicId);
	}

}
