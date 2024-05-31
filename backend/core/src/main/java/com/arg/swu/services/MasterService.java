package com.arg.swu.services;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.MasBankAccountData;
import com.arg.swu.dto.MasBankBranchData;
import com.arg.swu.dto.MasBankChargeAttachData;
import com.arg.swu.dto.MasBankChargeData;
import com.arg.swu.dto.MasBankData;
import com.arg.swu.dto.MasCourseTypeData;
import com.arg.swu.dto.MasDepartmentData;
import com.arg.swu.dto.MasGeneralSkillData;
import com.arg.swu.dto.MasOccupationData;
import com.arg.swu.dto.MasOccupationGroupData;
import com.arg.swu.dto.MasOccupationSkillData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.dto.MasSharePercentAttachData;
import com.arg.swu.dto.MasSharePercentData;
import com.arg.swu.dto.MasSharePercentHistoryData;
import com.arg.swu.dto.MasWebsiteBannerData;
import com.arg.swu.dto.NotiInfoData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.NotiInfo;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.NotiInfoRepository;
import java.util.Date;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class MasterService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(MasterService.class);
	
	private final JdbcTemplate jdbcTemplate;
	private final NotiInfoRepository notiInfoRepository;
        private final EntityMapperService mapperService;
        private final AutUserRepo autUserRepo;
        private final FileStorageService fileStorageService;
	public Map<String, Object> findCourseTypeCondition(MasCourseTypeData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getCourseTypeCode())) {
			conditions.append(" and mct.course_type_code like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getCourseTypeCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getCourseTypeNameTh())) {
			conditions.append(" and mct.course_type_name_th ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getCourseTypeNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getCourseTypeNameEn())) {
			conditions.append(" and mct.course_type_name_en ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getCourseTypeNameEn(), true, true));
		}
		
		if (null != criteria.getCourseMappingStatus()) {
			conditions.append(" and mct.course_mapping_status = ? ");
			params.add(criteria.getCourseMappingStatus());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mct.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by mct.create_date desc  ) AS row_num, mct.* ");
		sb.append(" from mas_course_type mct ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by mct.create_date desc ");
		sb.append(LIMIT);
		
		List<MasCourseTypeData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasCourseTypeData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_course_type mct where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findGeneralSkillByCondition(MasGeneralSkillData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		String orderBy = " order by mgs.create_date  desc ";
		
		if (StringUtils.isNoneBlank(criteria.getGeneralSkillCode())) {
			conditions.append(" and mgs.general_skill_code like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getGeneralSkillCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getGeneralSkillNameTh())) {
			conditions.append(" and (mgs.general_skill_name_th like ? or mgs.general_skill_name_en like ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getGeneralSkillNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getGeneralSkillNameTh(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mgs.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		if (null != criteria.getSkillFormat()) {
			conditions.append(" and mgs.skill_format = ? ");
			params.add(criteria.getSkillFormat());
		}
		
		if (StringUtils.isNotBlank(criteria.getMode())) {
			if ("mainpage".equals(criteria.getMode())) {
				
				orderBy = " order by mgs.update_date desc ";
				
				conditions.append(" and general_skill_parent_id is null ");
				conditions.append(" and exists( select general_skill_id from course_skill cs where cs.general_skill_id = mgs.general_skill_id ) ");
				
			}
		}
		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER ( ");
		sb.append(orderBy);
		sb.append(" ) AS row_num,  mgs.*, ");
		sb.append("lookup.name_lo skill_format_th, lookup.name_en skill_format_en");
		sb.append(" from mas_general_skill mgs ");
		sb.append(" left join lookup_catalog lookup on mgs.skill_format = lookup.catalog_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(orderBy);
		sb.append(LIMIT);
		
		List<MasGeneralSkillData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasGeneralSkillData.class),
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
		count.append("select count(*) from mas_general_skill mgs where 1 = 1 ");
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findBankAccountByCondition(MasBankAccountData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getBankId()) {
			conditions.append(" and mba.bank_id = ? ");
			params.add(criteria.getBankId());
		}
		
		if (null != criteria.getBankBranchId()) {
			conditions.append(" and mba.bank_branch_id = ? ");
			params.add(criteria.getBankBranchId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getAccountNo())) {
			conditions.append(" and mba.account_no like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getAccountNo(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getAccountNameTh())) {
			conditions.append(" and mba.account_name_th like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getAccountNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getAccountNameEn())) {
			conditions.append(" and mba.account_name_en like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getAccountNameEn(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mba.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by bank_account_id asc ) AS row_num, ");
		sb.append(" mb.bank_name_th, mb.bank_name_en, mbb.bank_branch_name_th, mbb.bank_branch_name_en, ");
		sb.append(" mba.* ");
		sb.append(" from mas_bank_account mba ");
		sb.append(" left join mas_bank mb on mba.bank_id = mb.bank_id  ");
		sb.append(" left join mas_bank_branch mbb on mba.bank_branch_id = mbb.bank_branch_id ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by bank_account_id asc ");
		sb.append(LIMIT);
		
		List<MasBankAccountData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasBankAccountData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_bank_account mba where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findBankBranchByCondition(MasBankBranchData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getBankId()) {
			conditions.append(" and mbb.bank_id = ? ");
			params.add(criteria.getBankId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getBankBranchCode())) {
			conditions.append(" and mbb.bank_branch_code like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankBranchCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getBankBranchNameTh())) {
			conditions.append(" and mbb.bank_branch_name_th like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankBranchNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getBankBranchNameEn())) {
			conditions.append(" and mbb.bank_branch_name_en like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankBranchNameEn(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mbb.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select  row_number() OVER (order by bank_branch_id asc ) AS row_num, ");
		sb.append(" mb.bank_name_th, mb.bank_name_en, mbb.* ");
		sb.append(" from mas_bank_branch mbb ");
		sb.append(" left join mas_bank mb on mbb.bank_id = mb.bank_id ");
		sb.append(WHERE);

		sb.append(conditions.toString());

		sb.append(" order by bank_branch_id asc ");
		sb.append(LIMIT);

		List<MasBankBranchData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasBankBranchData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_bank_branch mbb where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findBankByCondition(MasBankData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getBankCode())) {
			conditions.append(" and mb.bank_code like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getBankNameTh())) {
			conditions.append(" and mb.bank_name_th ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankNameTh(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getBankNameEn())) {
			conditions.append(" and mb.bank_name_en ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBankNameEn(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mb.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by bank_id asc ) AS row_num, mb.* ");
		sb.append(" from mas_bank mb ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by bank_id asc ");
		sb.append(LIMIT);

		List<MasBankData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasBankData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_bank mb where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}

	public Map<String, Object> findOccupationGroupCondition(MasOccupationGroupData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getOccupationGroupCode())) {
			conditions.append(" and mcg.occupation_group_code ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationGroupCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getOccupationGroupNameTh())) {
			conditions.append(" and (mcg.occupation_group_name_th ilike ? or mcg.occupation_group_name_en ilike ?)");
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationGroupNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationGroupNameTh(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mcg.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by occupation_group_id asc ) AS row_num, mcg.* ");
		sb.append(" from mas_occupation_group mcg ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by occupation_group_id asc ");
		sb.append(LIMIT);
		
		List<MasOccupationGroupData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasOccupationGroupData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_occupation_group mcg where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}

	public Map<String, Object> findOccupationCondition(MasOccupationData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getOccupationCode())) {
			conditions.append(" and mo.occupation_code ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationCode(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getOccupationNameTh())) {
			conditions.append(" and (mo.occupation_name_th ilike ? or mo.occupation_name_en ilike ?)");
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getOccupationNameTh(), true, true));
		}
		
		if (null != criteria.getOccupationGroupId()) {
			conditions.append(" and mo.occupation_group_id = ? ");
			params.add(criteria.getOccupationGroupId());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mo.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mo.occupation_id asc ) AS row_num, ");
		sb.append(" mog.occupation_group_name_th, mog.occupation_group_name_en, mo.* ");
		sb.append(" from mas_occupation mo ");
		sb.append(" left join mas_occupation_group mog on mo.occupation_group_id = mog.occupation_group_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by occupation_id asc ");
		sb.append(LIMIT);
		
		List<MasOccupationData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasOccupationData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_occupation mo where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findOccupationSkillCondition(MasOccupationSkillData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getOccupationId()) {
			conditions.append(" and mos.occupation_id = ? ");
			params.add(criteria.getOccupationId());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mos.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mos.occ_skill_id asc ) AS row_num, ");
		sb.append(" lc.name_lo level_name_th, lc.name_en level_name_en, ");
		sb.append(" mgs.general_skill_name_th, mgs.general_skill_name_en, "); 
		sb.append(" mos.* ");
		sb.append(" from mas_occupation_skill mos ");
		sb.append(" left join mas_general_skill mgs on mos.general_skill_id = mgs.general_skill_id ");
		sb.append(" left join lookup_catalog lc on mos.occ_skill_level = lc.catalog_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by mos.occ_skill_id asc ");
		sb.append(LIMIT);
		
		List<MasOccupationSkillData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasOccupationSkillData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_occupation_skill mos where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
        
	public Map<String, Object> findSharePercentTableData(MasSharePercentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (criteria.getDepId() != null) {
			conditions.append(" and msp.dep_id = ? ");
			params.add(criteria.getDepId());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() over () row_num, ");
		sb.append(" md.dep_id, md.dep_name_th, md.dep_name_en,msp.share_percent_id,msp.create_by_id,msp.update_date, ");
		sb.append(" coalesce(msp.cost_share_dep_percent, 0) cost_share_dep_percent, coalesce(msp.cost_share_global_percent, 0) cost_share_global_percent, coalesce(msp.cost_share_center_percent, 0) cost_share_center_percent ");
		
		sb.append(" from mas_share_percent msp ");
		sb.append(" left join mas_department md on md.dep_id = msp.dep_id ");

		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(LIMIT);
		
		List<MasSharePercentData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasSharePercentData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from  mas_share_percent msp  where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
        
	public Map<String, Object> findSharePercentHistoryTableData(MasSharePercentHistoryData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (criteria.getDepId()!=null) {
			conditions.append(" and msph.dep_id = ? ");
			params.add(criteria.getDepId());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() over () row_num, ");
		sb.append(" concat(au.firstname_th,' ',coalesce(au.lastname_th,'')) as full_name_th, ");
		sb.append(" concat(au.firstname_en,' ',coalesce(au.lastname_en,'')) as full_name_en, ");
		sb.append(" msph.create_date, ");
		sb.append(" coalesce(msph.cost_share_dep_percent, 0) cost_share_dep_percent, coalesce(msph.cost_share_global_percent, 0) cost_share_global_percent, coalesce(msph.cost_share_center_percent, 0) cost_share_center_percent ");
		
		sb.append(" from mas_share_percent_history msph ");
		sb.append(" left join aut_user au on au.user_id = msph.create_by_id ");

		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(LIMIT);
		
		List<MasSharePercentHistoryData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasSharePercentHistoryData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from  mas_share_percent_history msph  where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
        
	public Map<String, Object> findSharePercentAttachTableData(MasSharePercentAttachData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (criteria.getDepId()!=null) {
			conditions.append(" and mspa.dep_id = ? ");
			params.add(criteria.getDepId());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("select row_number() over () row_num, ");
		sb.append(" mspa.file_name,mspa.file_link ");
		
		sb.append(" from mas_share_percent_attach mspa ");
		sb.append(" left join mas_share_percent msp on msp.share_percent_id = mspa.share_percent_id ");

		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(LIMIT);

		List<MasSharePercentAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasSharePercentAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from  mas_share_percent_attach mspa  where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}

	public Map<String, Object> findDepartmentCondition(MasDepartmentData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getDepCode())) {
			conditions.append(" and md.dep_code ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getDepCode(), true, true));
		}
		
		conditions.append(" and md.dep_parent_id = 0 ");
		
		if (StringUtils.isNoneBlank(criteria.getDepNameTh())) {
			conditions.append(" and (md.dep_name_th ilike ? or md.dep_name_en ilike ? ");
			conditions.append(" or md.dep_name_short_th ilike ? or md.dep_name_short_en ilike ? ");
			conditions.append(" or md.dep_name_abbr_th ilike ? or md.dep_name_abbr_en ilike ? )");
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getDepNameTh(), true, true));
		}
		
		if (null != criteria.getEducationStatus()) {
			conditions.append(" and md.education_status = ? ");
			params.add(criteria.getEducationStatus());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and md.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by md.dep_id asc ) AS row_num, md.* ");
		sb.append(" from mas_department md ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by md.dep_id asc ");
		sb.append(LIMIT);
		
		List<MasDepartmentData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasDepartmentData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_department md where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		Date syncDate = jdbcTemplate.queryForObject("""
				select ssd.create_date 
				from sys_sync_data ssd 
				where table_name = 'mas_department'
				order by create_date desc
				limit 1
				""", Date.class);
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		result.put("syncDate", syncDate);
		
		return result;
	}

	public Map<String, Object> findBankChargeCondition(MasBankChargeData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != criteria.getPaymentType()) {
			conditions.append(" and mbc.payment_type = ? ");
			params.add(criteria.getPaymentType());
		}
		
		if (null != criteria.getCardType()) {
			conditions.append(" and mbc.card_type = ? ");
			params.add(criteria.getCardType());
		}
		
		if (null != criteria.getStartYearList() && !criteria.getStartYearList().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			if (null == criteria.getStartYearList().get(1)) {
				conditions.append(" and mbc.start_year = ? ");
				Long start = NumberUtils.toLong(sdf.format(criteria.getStartYearList().get(0)));
				params.add(start);
			} else {
				conditions.append(" and mbc.start_year between ? and ? ");
				Long start = NumberUtils.toLong(sdf.format(criteria.getStartYearList().get(0)));
				Long end = NumberUtils.toLong(sdf.format(criteria.getStartYearList().get(1)));
				params.add(start);
				params.add(end);
				
			}
        }
		
		if (null != criteria.getCreateDateList() && !criteria.getCreateDateList().isEmpty()) {
			if (null == criteria.getCreateDateList().get(1)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				conditions.append(" and mbc.create_date = ? ");
				params.add(sdf.format(criteria.getCreateDateList().get(0)));
			} else {
				conditions.append(" and mbc.create_date between ?::timestamp and ?::timestamp ");
	            params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(0), true, false));
	            params.add(CommonUtils.convertDateSqlDate(criteria.getCreateDateList().get(1), false, true));
				
			}
        }
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mbc.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mbc.charge_id asc ) AS row_num, ");
		sb.append(" pt.name_lo payment_type_name_th, pt.name_en payment_type_name_en, ct.name_lo card_type_name_th, ct.name_en card_type_name_en, ");
		sb.append(" mbc.* ");
		sb.append(" from mas_bank_charge mbc ");
		sb.append(" left join lookup_catalog pt on mbc.payment_type = pt.catalog_id ");
		sb.append(" left join lookup_catalog ct on mbc.card_type = ct.catalog_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by charge_id asc ");
		sb.append(LIMIT);
		
		List<MasBankChargeData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasBankChargeData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_bank_charge mbc  where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findBankChargeAttachCondition(MasBankChargeAttachData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (null != criteria.getChargeId()) {
			conditions.append("and mbca.charge_id = ? ");
			params.add(criteria.getChargeId());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mbca.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mbca.charge_attach_id asc ) AS row_num, mbca.* ");
		sb.append(" from mas_bank_charge_attach mbca  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by mbca.charge_attach_id asc ");
		sb.append(LIMIT);
		
		List<MasBankChargeAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasBankChargeAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from mas_bank_charge_attach mbca where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}

	public Map<String, Object> findPersonalCondition(MasPersonalData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();

		if (StringUtils.isNoneBlank(criteria.getBuasriId())) {
			conditions.append(" and mp.buasri_id ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getBuasriId(), true, true));
		}
		
		/**
		if (StringUtils.isNoneBlank(criteria.getFullname())) {
			conditions.append(" and ( mp.firstname_th ilike ? or mp.middlename_th ilike ? or mp.lastname_th ilike ? ");
			conditions.append(" or mp.firstname_en ilike ? or mp.middlename_en ilike ? or mp.lastname_en ilike ? ) ");
			conditions.append(" and (concat(mp.firstname_th,' ',coalesce(mp.lastname_th,'')) ilike ? or concat(mp.firstname_en,' ',coalesce(mp.lastname_en,'')) ilike ? ) ");
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getFullname(), true, true));
		}
		*/
		
		if (StringUtils.isNoneBlank(criteria.getFullname())) {
			String[] fullname = criteria.getFullname().split("\\s+");
			String firstname = StringUtils.trim(fullname[0]);
			String lastname = fullname.length == 2 ? StringUtils.trim(fullname[1]) : null;
			
			if (StringUtils.isNotBlank(firstname) && StringUtils.isNotBlank(lastname)) {
				if (StringUtils.isNotBlank(firstname)) {
					conditions.append(" and (mp.firstname_th ilike ? or mp.firstname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
				}
				if (StringUtils.isNotBlank(lastname)) {				
					conditions.append(" and (mp.lastname_th ilike ? or mp.lastname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
				}
			} else if (StringUtils.isNotBlank(firstname)) {
				conditions.append(" and (mp.firstname_th ilike ? or mp.firstname_en ilike ? or mp.lastname_th ilike ? or mp.lastname_en ilike ?) ");
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
			}
		}
		
		if (StringUtils.isNoneBlank(criteria.getPositionTh())) {
			conditions.append(" and ( mp.position_th ilike ? or mp.position_en ilike ? ) ");
			params.add(CommonUtils.concatLikeParam(criteria.getPositionTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getPositionTh(), true, true));
		}
		
		if (null != criteria.getPersonalType()) {
			conditions.append(" and mp.personal_type = ? ");
			params.add(criteria.getPersonalType());
		}
		
		if (StringUtils.isNoneBlank(criteria.getEmail())) {
			conditions.append(" and mp.email ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getEmail(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getStatusNameTh())) {
			conditions.append(" and ( mp.status_name_th ilike ? or mp.status_name_en = ?  ) ");
			params.add(CommonUtils.concatLikeParam(criteria.getStatusNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getStatusNameTh(), true, true));
		}
		
		if (null != criteria.getDepIdLevel1()) {			
			conditions.append(" and (mp.dep_id_level1 = ? or mp.dep_id_level2 = ?) ");
			params.add(criteria.getDepIdLevel1());
			params.add(criteria.getDepIdLevel1());
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and mp.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mp.personal_id asc ) AS row_num, ");
		sb.append(" lc.name_lo personal_type_th, lc.name_en personal_type_en,  ");
		sb.append(" md1.dep_name_th dep_name_lv1_th, md1.dep_name_en dep_name_lv1_en, ");
		sb.append(" md2.dep_name_th dep_name_lv2_th, md2.dep_name_en dep_name_lv2_en, ");
		sb.append(" concat(mp.firstname_th,' ',coalesce(mp.lastname_th,'')) as full_name_th, ");
		sb.append(" concat(mp.firstname_en,' ',coalesce(mp.lastname_en,'')) as full_name_en, ");
		sb.append(" mp.* ");
		sb.append(" from mas_personal mp  ");
		sb.append(" left join mas_department md1 on mp.dep_id_level1 = md1.dep_id  ");
		sb.append(" left join mas_department md2 on mp.dep_id_level2  = md2.dep_id  ");
		sb.append(" left join lookup_catalog lc on mp.personal_type = lc.catalog_id  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());

		sb.append(" order by mp.personal_id asc ");
		sb.append(LIMIT);
		
		List<MasPersonalData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MasPersonalData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append(" select count(*) ");
		count.append(" from mas_personal mp  ");
		count.append(" left join mas_department md1 on mp.dep_id_level1 = md1.dep_id  ");
		count.append(" left join mas_department md2 on mp.dep_id_level2  = md2.dep_id  ");
		count.append(" left join lookup_catalog lc on mp.personal_type = lc.catalog_id  ");
		count.append(WHERE);
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		Date syncDate = jdbcTemplate.queryForObject("""
				select ssd.create_date 
				from sys_sync_data ssd 
				where table_name = 'mas_personal'
				order by create_date desc
				limit 1
				""", Date.class);
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		result.put("syncDate", syncDate);
		
		return result;
	}
	
	public Map<String, Object> findNotiInfo(NotiInfoData criteria) {
            Map<String, Object> result = new HashMap<>();
            StringBuilder conditions = new StringBuilder();
            List<Object> params = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("select row_number() over () row_num,ni.*,lc.name_en as topic_en,lc.name_lo as topic_th,lc2.name_en as recipient_en,lc2.name_lo as recipient_th "
                    + "from noti_info ni join lookup_catalog lc on lc.catalog_id = ni.noti_topic "
                    + "join lookup_catalog lc2 on lc2.catalog_id = ni.noti_recipient ");
            if(criteria.getNotiTopic()!=null){
                conditions.append(" and ni.noti_topic = ? ");
                params.add(criteria.getNotiTopic());
            }
            if(criteria.getNotiRecipient()!=null){
                conditions.append(" and ni.noti_recipient = ? ");
                params.add(criteria.getNotiRecipient());
            }
            if(criteria.getActiveFlag()!=null){
                conditions.append(" and ni.active_flag = ? ");
                params.add(criteria.getActiveFlag());
            }
            sb.append(WHERE);
            sb.append(conditions.toString());
            sb.append(LIMIT);
            
            List<NotiInfoData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(NotiInfoData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
            StringBuilder count = new StringBuilder();
            count.append("select count(*) from noti_info ni join lookup_catalog lc on lc.catalog_id = ni.noti_topic "
                    + "join lookup_catalog lc2 on lc2.catalog_id = ni.noti_recipient ");
            count.append(WHERE);
            count.append(conditions.toString());
            Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
            result.put(ENTRIES, entries);
            result.put(TOTAL_RECORDS, totalRecords);
            return result;
        }
        
        public Map<String, Object> updateNotiDetail(AutUser userAction, NotiInfoData criteria) {
            Map<String, Object> result = new HashMap<>();
            NotiInfo obj = mapperService.convertToEntity(criteria, NotiInfo.class);
            obj.setUpdateBy(userAction);
            obj.setUpdateDate(new Date());
            LOG.info("criteria.getCreateById()::"+criteria.getCreateById());
            AutUser createBy = autUserRepo.findByUserId(criteria.getCreateById());
            obj.setCreateBy(createBy);
            notiInfoRepository.save(obj);
            criteria = mapperService.convertToEntity(obj, NotiInfoData.class);
            result.put(ENTRIES, criteria);
            return result;
        }
        
        public Map<String, Object> findWebsiteBannerByCondition(MasWebsiteBannerData criteria) {
    		
    		Map<String, Object> result = new HashMap<>();
    		
    		StringBuilder conditions = new StringBuilder();
    		List<Object> params = new ArrayList<>();
    		String orderBy = " order by mb.banner_id asc ";
    		
    		if (StringUtils.isNoneBlank(criteria.getBannerName())) {
    			conditions.append(" and mb.banner_name ilike ? ");
    			params.add(CommonUtils.concatLikeParam(criteria.getBannerName(), true, true));
    		}
    		
    		if (StringUtils.isNoneBlank(criteria.getFullNameTh())) {
    			conditions.append(" and (concat(COALESCE(au.firstname_th ,''), ' ', COALESCE(au.lastname_th ,'')) ilike ? ");
    			conditions.append(" or concat(COALESCE(au.firstname_en ,''), ' ', COALESCE(au.lastname_en ,'')) ilike ? ) ");
    			params.add(CommonUtils.concatLikeParam(criteria.getFullNameTh(), true, true));
    			params.add(CommonUtils.concatLikeParam(criteria.getFullNameTh(), true, true));
    		}
    		
    		if (null != criteria.getActiveFlag()) {
    			conditions.append(" and mb.active_flag = ? ");
    			params.add(criteria.getActiveFlag());
    		}
    		
    		if (null != criteria.getLastUpDateList() && !criteria.getLastUpDateList().isEmpty() ) {
    			if (null == criteria.getLastUpDateList().get(1)) {
    				conditions.append(" and mb.update_date between ?::timestamp and ?::timestamp " );
    				params.add(CommonUtils.convertDateSqlDate(criteria.getLastUpDateList().get(0), true, false));
    				params.add(CommonUtils.convertDateSqlDate(criteria.getLastUpDateList().get(0), false, true));
    			} else {
    				conditions.append(" and mb.update_date between ?::timestamp and ?::timestamp ");
    	            params.add(CommonUtils.convertDateSqlDate(criteria.getLastUpDateList().get(0), true, false));
    	            params.add(CommonUtils.convertDateSqlDate(criteria.getLastUpDateList().get(1), false, true));
    			}
    			
            }
    		
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append(" select row_number() OVER ( ");
    		sb.append(orderBy);
    		sb.append(" ) AS row_num,  mb.*, ");
    		sb.append(" concat(COALESCE(au.firstname_th ,''), ' ', COALESCE(au.lastname_th ,'') )  as full_name_th,  ");
    		sb.append(" concat(COALESCE(au.firstname_en ,''), ' ', COALESCE(au.lastname_en ,'') )  as full_name_en  ");
    		sb.append(" from mas_banner mb ");
    		sb.append(" join aut_user au on au.user_id = mb.create_by_id  ");
    		sb.append(WHERE);
    		sb.append(" and (case when mb.display_start_date is null then true else (now() between display_start_date and display_end_date) end) = true ");
    		sb.append(conditions.toString());
    		
    		sb.append(orderBy);
    		sb.append(LIMIT);
    		
    		List<MasWebsiteBannerData> entries = jdbcTemplate.query(sb.toString(),
    				BeanPropertyRowMapper.newInstance(MasWebsiteBannerData.class),
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
    		count.append("select count(*) from mas_banner mb");
    		count.append(" join aut_user au on au.user_id = mb.create_by_id  ");
    		count.append(WHERE);
    		count.append(" and (case when mb.display_start_date is null then true else (now() between display_start_date and display_end_date) end) = true ");
    		count.append(conditions.toString());
    		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
    		
    		result.put(ENTRIES, entries);
    		result.put(TOTAL_RECORDS, totalRecords);
    		
    		
    		return result;
    	}
}
