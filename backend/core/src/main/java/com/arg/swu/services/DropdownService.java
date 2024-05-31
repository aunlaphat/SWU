package com.arg.swu.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.DropdownCriteriaData;
import com.arg.swu.dto.DropdownData;
import com.arg.swu.dto.DropdownTambonData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.models.AutUser;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class DropdownService implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(DropdownService.class);
	
	private final JdbcTemplate jdbcTemplate;
	
	public List<DropdownData> getLookup(DropdownCriteriaData criteria) {
		
		if (null == criteria.getId()) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("select lc.catalog_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', lc.key, ']-', lc.name_lo) name_th, concat('[', lc.key, ']-', lc.name_en) name_en ");
		} else {
			sql.append(" lc.name_lo name_th, lc.name_en name_en ");
		}

		sql.append(" from lookup_catalog lc  ");
		sql.append(" where 1 = 1 and lc.active_flag = true  ");

		if (null != criteria.getId()) {
			sql.append(" and lc.parent_id = ? ");
			params.add(criteria.getId());
		}

		if (null != criteria.getPkIds() && !criteria.getPkIds().isEmpty()) {
			String inCodition = String.format(" and lc.catalog_id in (%s) ", String.join(",", Collections.nCopies(criteria.getPkIds().size(), "?")));
			sql.append(inCodition);
			params.addAll(criteria.getPkIds());
		}
		
		sql.append(" order by lc.catalog_id asc ");
		return jdbcTemplate.query(sql.toString() ,BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getUser(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select au.user_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat(au.firstname_th, ' ', au.lastname_th) name_th, concat(au.firstname_en, ' ', au.lastname_en) name_en ");
		} else {
			sql.append(" concat(au.firstname_th, ' ', au.lastname_th) name_th, concat(au.firstname_en, ' ', au.lastname_en) name_en ");
		}

		sql.append(" from aut_user au ");
		sql.append(" where 1 = 1 and au.active_flag = true ");

		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (au.firstname_th ilike ? or au.lastname_th ilike ? or au.firstname_en ilike ? or au.lastname_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and au.user_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by au.user_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getBank(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mb.bank_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mb.bank_code, ']-', mb.bank_name_th) name_th, concat('[', mb.bank_code, ']-', mb.bank_name_en) name_en ");
		} else {
			sql.append(" mb.bank_name_th name_th, mb.bank_name_en name_en ");
		}

		sql.append(" from mas_bank mb ");
		sql.append(" where 1 = 1 and mb.active_flag = true ");

		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mb.bank_name_th ilike ? or mb.bank_name_en ilike ? or mb.bank_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mb.bank_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by mb.bank_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getBankAccount(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mba.bank_account_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mba.account_no, ']-', mba.account_name_th) name_th, concat('[', mba.account_no, ']-', mba.account_name_en) name_en ");
		} else {
			sql.append(" mba.account_name_th name_th, mba.account_name_en name_en ");
		}
		
		sql.append(" from mas_bank_account mba ");
		sql.append(" where 1 = 1 and mba.active_flag = true ");
		
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mba.account_name_th ilike ? or mba.account_name_en ilike ? or mba.account_no ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mba.bank_account_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by mba.bank_account_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getBankBranch(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mbb.bank_branch_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mbb.bank_branch_code , ']-', mbb.bank_branch_name_th) name_th, concat('[', mbb.bank_branch_code, ']-', mbb.bank_branch_name_en) name_en ");
		} else {
			sql.append(" mbb.bank_branch_name_th name_th, mbb.bank_branch_name_en name_en ");
		}

		sql.append(" from mas_bank_branch mbb ");
		sql.append(" where 1 = 1 and mbb.active_flag = true ");
		
		if (null != criteria.getId()) {
			sql.append(" and mbb.bank_id = ? ");
			params.add(criteria.getId());
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mbb.bank_branch_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mbb.bank_branch_name_th ilike ? or mbb.bank_branch_name_en  ilike ? or mbb.bank_branch_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by mbb.bank_branch_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getDepartment(DropdownCriteriaData criteria, AutUser userAction) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select md.dep_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', md.dep_code, ']-', md.dep_name_th) name_th, concat('[', md.dep_code, ']-', md.dep_name_en) name_en ");
		} else {
			sql.append(" md.dep_name_th name_th, md.dep_name_en name_en ");
		}
		
		sql.append(" from mas_department md ");
		sql.append(" where 1 = 1 and md.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (md.dep_name_th ilike ? or md.dep_name_en ilike ? or md.dep_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getId()) {
			sql.append(" and md.dep_parent_id = ? ");
			params.add(criteria.getId());
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and md.dep_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != criteria.getDepType()) {
			if (criteria.getDepType().equals(30009001l)) {
				sql.append(" and md.dep_parent_id = 0 ");
				
				if (null != userAction) {
					if (null != userAction.getAccessLevel()) {
						if (userAction.getAccessLevel().equals(30025004l)) {
							sql.append(" and md.dep_id = ? ");
							params.add(userAction.getDepIdLevel1());
						}
						
					}
				}
				
			} else {
				sql.append(" and md.dep_parent_id <> 0 ");
				
				if (null != userAction) {
					if (null != userAction.getAccessLevel()) {
						if (userAction.getAccessLevel().equals(30025004l) || userAction.getAccessLevel().equals(30025003l)) {
							sql.append(" and md.dep_id = ? ");
							params.add(userAction.getDepIdLevel2());
						}
						
					}
				}
				
				
			}
		}
		
		sql.append(" order by md.dep_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getGeneralSkill(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mgs.general_skill_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mgs.general_skill_code  , ']-', mgs.general_skill_name_th) name_th, concat('[', mgs.general_skill_code  , ']-', mgs.general_skill_name_en) name_en ");
		} else {
			sql.append(" mgs.general_skill_name_th name_th, general_skill_name_en name_en ");
		}

		sql.append(" from mas_general_skill mgs  ");
		sql.append(" where 1 = 1 and mgs.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mgs.general_skill_name_th ilike ? or mgs.general_skill_name_en  ilike ? or mgs.general_skill_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		if (null != criteria.getPkId()) {
			sql.append(" and mgs.general_skill_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by mgs.general_skill_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getMethod(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mcm.course_method_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mcm.course_method_id  , ']-', mcm.course_method_name_th) name_th, concat('[', mcm.course_method_id  , ']-', mcm.course_method_name_en) name_en ");
		} else {
			sql.append(" mcm.course_method_name_th name_th, mcm.course_method_name_en name_en ");
		}
		
		sql.append(" from mas_course_method mcm  ");
		sql.append(" where 1 = 1 and mcm.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mcm.course_method_name_th ilike ? or mcm.course_method_name_th  ilike ? ) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mcm.course_method_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != criteria.getPkIds() && !criteria.getPkIds().isEmpty() && StringUtils.isBlank(criteria.getSearchValue())) {
			String inCodition = String.format(" and mcm.course_method_id in (%s) ", String.join(",", Collections.nCopies(criteria.getPkIds().size(), "?")));
			sql.append(inCodition);
			params.addAll(criteria.getPkIds());
		}
		
		sql.append(" order by mcm.course_method_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}

	public List<DropdownData> getOccupation(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mo.occupation_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mo.occupation_code, ']-', mo.occupation_name_th) name_th, concat('[', mo.occupation_code, ']-', mo.occupation_name_en) name_en ");
		} else {
			sql.append(" mo.occupation_name_th name_th, mo.occupation_name_en name_en ");
		}

		sql.append(" from mas_occupation mo ");
		sql.append(" where 1 = 1 and mo.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mo.occupation_name_th ilike ? or mo.occupation_name_en ilike ? or mo.occupation_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		if (null != criteria.getPkId()) {
			sql.append(" and mo.occupation_id = ? ");
			params.add(criteria.getPkId());
		}

		if (null != criteria.getPkIds() && !criteria.getPkIds().isEmpty()) {
			String inCodition = String.format(" and mo.occupation_id in (%s) ", String.join(",", Collections.nCopies(criteria.getPkIds().size(), "?")));
			sql.append(inCodition);
			params.addAll(criteria.getPkIds());
		}
		
		sql.append(" order by mo.occupation_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getOccupationGroup(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mog.occupation_group_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mog.occupation_group_code  , ']-', mog.occupation_group_name_th) name_th, concat('[', mog.occupation_group_code , ']-', mog.occupation_group_name_en) name_en ");
		} else {
			sql.append(" mog.occupation_group_name_th name_th, mog.occupation_group_name_en name_en ");
		}

		sql.append(" from mas_occupation_group mog ");
		sql.append(" where 1 = 1 and mog.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mog.occupation_group_name_th ilike ? or mog.occupation_group_name_en  ilike ? or mog.occupation_group_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		if (null != criteria.getPkId()) {
			sql.append(" and mog.occupation_group_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by mog.occupation_group_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getPersonal(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mp.personal_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mp.buasri_id , ']-', mp.prefix_th, ' ', mp.firstname_th, ' ',coalesce(mp.middlename_th , ''), mp.lastname_th) name_th, concat('[', mp.buasri_id , ']-', mp.prefix_en, ' ', mp.firstname_en, ' ',coalesce(mp.middlename_en , ''), mp.lastname_en) name_en ");
		} else {
			sql.append(" concat(mp.firstname_th, ' ',coalesce(mp.middlename_th , ''), mp.lastname_th) name_th, concat(mp.firstname_en, ' ',coalesce(mp.middlename_en , ''), mp.lastname_en) name_en ");
		}
		
		sql.append(" from mas_personal mp ");
		sql.append(" where 1 = 1 and mp.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mp.buasri_id ilike ? or mp.prefix_th ilike ? or mp.firstname_th ilike ? or mp.middlename_th ilike ? or mp.lastname_th ilike ? or mp.prefix_en ilike ? or mp.firstname_en ilike ? or mp.middlename_en ilike ? or mp.lastname_en ilike ? ) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mp.personal_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getMode())) {
			if ("CREATE".equals(criteria.getMode())) {
				/** Drop down. ของผู้ใช้งานต้องไม่เอา user ที่ถูกเพิ่มแล้วมาแสดงให้เลือก */
				sql.append(" and not exists ( select 1 from aut_user au where au.ref_user_id = mp.personal_id ) ");
			}
		}
		
		sql.append(" order by mp.personal_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getCourseType(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mct.course_type_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', mct.course_type_code, ']-', mct.course_type_name_th) name_th, concat('[', mct.course_type_code, ']-', mct.course_type_name_en) name_en ");
		} else {
			sql.append(" mct.course_type_name_th name_th, mct.course_type_name_en name_en ");
		}
		
		sql.append(" from mas_course_type mct ");
		sql.append(" where 1 = 1 and mct.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mct.course_type_code ilike ? or mct.course_type_name_th ilike ? or mct.course_type_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and mct.course_type_id = ? ");
			params.add(criteria.getPkId());
		}
		
		sql.append(" order by mct.course_type_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
        
	public List<DropdownData> getRole(DropdownCriteriaData criteria) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append(" select ar.role_id value, ");
		sql.append(" ar.name name_th, ar.name name_en ");
		sql.append(" from aut_role ar ");
		sql.append(" where 1 = 1 and ar.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and ar.name ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and ar.role_id = ? ");
			params.add(criteria.getPkId());
		}

		if (null != criteria.getPkIds() && !criteria.getPkIds().isEmpty()) {
			String inCodition = String.format(" and ar.role_id in (%s) ", String.join(",", Collections.nCopies(criteria.getPkIds().size(), "?")));
			sql.append(inCodition);
			params.addAll(criteria.getPkIds());
		}
		
		sql.append(" order by ar.role_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getCourseMain(DropdownCriteriaData criteria, AutUser userAction) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select cm.course_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', cm.course_code, ']-', cm.course_name_th) name_th, concat('[', cm.course_code, ']-', cm.course_name_en) name_en ");
		} else {
			sql.append(" cm.course_name_th name_th, cm.course_name_en name_en ");
		}
		
		sql.append(" from course_main cm ");
		sql.append(" where 1 = 1 and cm.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (cm.course_code ilike ? or cm.course_name_th ilike ? or cm.course_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		
		// 30010005 หลักสูตรที่ผ่านการอนุมัติแล้ว
		if (null != criteria.getId()) {
			sql.append(" and cm.course_main_status = ? ");
			params.add(criteria.getId());
		}
		
		if (null != criteria.getPkId()) {
			sql.append(" and cm.course_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != userAction) {
			if (null != userAction.getAccessLevel()) {
				if (userAction.getAccessLevel().equals(30025002l)) {
					sql.append(" and ( cm.dep_id_level1 = ? or cm.create_by_id = ? ) ");
					params.add(userAction.getDepIdLevel1());
					params.add(userAction.getUserId());
				} else if (userAction.getAccessLevel().equals(30025004l) || userAction.getAccessLevel().equals(30025003l)) {
					sql.append(" and ( exists ( ");
					sql.append(" 	select 1 ");
					sql.append(" 	from course_instructor ci  ");
					sql.append(" 	where ci.course_id = cm.course_id ");
					sql.append(" 	and ci.instructor_id = ?  ");
					sql.append(" 	and ci.instructor_main = true ");
					sql.append("    ) or cm.create_by_id = ? ");
					sql.append(" ) ");
					sql.append(" and cm.dep_id_level2 = ? ");
					params.add(userAction.getRefUserId());
					params.add(userAction.getUserId());
					params.add(userAction.getDepIdLevel2());
				}
			}
		}
		
		sql.append(" order by cm.course_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
	public List<DropdownData> getCoursepublicMain(DropdownCriteriaData criteria, AutUser userAction) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select cpm.coursepublic_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', cm.course_code, ']-', cpm.public_name_th) name_th, concat('[', cm.course_code, ']-', cpm.public_name_en) name_en ");
		} else {
			sql.append(" cpm.public_name_th name_th, cpm.public_name_en name_en ");
		}
		
		sql.append(" from coursepublic_main cpm ");
		sql.append(" left join course_main cm on cpm.course_id = cm.course_id  ");
		sql.append(" where 1 = 1 and cpm.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (cm.course_code ilike ? or cpm.public_name_th ilike ? or cpm.public_name_en ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}
		/*
		if (null != criteria.getId()) {
			// 30010005 หลักสูตรที่ผ่านการอนุมัติแล้ว
			sql.append(" and cm.course_main_status = ? ");
			params.add(30010005l);
		}
		*/
		if (null != criteria.getPkId()) {
			sql.append(" and cpm.coursepublic_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != userAction) {
			if (null != userAction.getAccessLevel()) {
				/**
				 * 30025001	Super Admin
				 * 30025002	Faculty
				 * 30025003	Department
				 * 30025004	Personal
				 */
				if (userAction.getAccessLevel().equals(30025002l)) {
					sql.append(" and ( cpm.dep_id_level1 = ? or cpm.create_by_id = ? ) ");
					params.add(userAction.getDepIdLevel1());
					params.add(userAction.getUserId());
				} else if (userAction.getAccessLevel().equals(30025004l) || userAction.getAccessLevel().equals(30025003l)) {
					sql.append(" and ( exists ( ");
					sql.append(" 	select 1 ");
					sql.append(" 	from coursepublic_instructor ci  ");
					sql.append(" 	where ci.coursepublic_id = cpm.coursepublic_id ");
					sql.append(" 	and ci.instructor_id = ? ");
					sql.append(" 	and ci.instructor_main = true ");
					sql.append("   ) or cpm.create_by_id = ? ");
					sql.append(" ) ");
					sql.append(" and cpm.dep_id_level2 = ? ");
					params.add(userAction.getRefUserId());
					params.add(userAction.getUserId());
					params.add(userAction.getDepIdLevel2());
				}
				
			}
		}
		
		sql.append(" order by cpm.coursepublic_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}

	public DropdownData getLookupById(Long pkId, Boolean displayCode) {
		
		if (null == pkId) {
			return new DropdownData();
		}

		StringBuilder sql = new StringBuilder();
		sql.append("select lc.catalog_id value, ");
		if (Boolean.TRUE.equals(displayCode)) {
			sql.append(" concat('[', lc.key, ']-', lc.name_lo) name_th, concat('[', lc.key, ']-', lc.name_en) name_en ");
		} else {
			sql.append(" lc.name_lo name_th, lc.name_en name_en ");
		}

		sql.append(" from lookup_catalog lc  ");
		sql.append(" where 1 = 1 and lc.active_flag = true and lc.catalog_id = ? ");
		sql.append(" order by lc.catalog_id asc ");

		return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<DropdownData>(DropdownData.class), pkId);
	}

	public List<DropdownData> getCountry(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select sc.country_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', sc.country_code , ']-', sc.country_name_th) name_th, concat('[', sc.country_code , ']-', sc.country_name_en) name_en ");
		} else {
			sql.append(" sc.country_name_th name_th, sc.country_name_en name_en ");
		}

		sql.append(" from sys_country sc ");
		sql.append(" where 1 = 1 and sc.active_flag = true ");
		
		if (null != criteria.getPkId()) {
			sql.append(" and sc.country_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (sc.country_name_th ilike ? or sc.country_name_en ilike ? or sc.country_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by sc.country_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}

	public List<DropdownData> getProvince(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select sp.province_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', sp.province_code , ']-', sp.province_name_th) name_th, concat('[', sp.province_code , ']-', sp.province_name_en) name_en ");
		} else {
			sql.append(" sp.province_name_th name_th, sp.province_name_en name_en ");
		}

		sql.append(" from sys_province sp ");
		sql.append(" where 1 = 1 and sp.active_flag = true ");
		
		if (null != criteria.getPkId()) {
			sql.append(" and sp.province_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (sp.province_name_th ilike ? or sp.province_name_en  ilike ? or sp.province_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by sp.province_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}

	public List<DropdownData> getAmphur(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select sa.amphur_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', sa.amphur_code , ']-', sa.amphur_name_th) name_th, concat('[', sa.amphur_code , ']-', sa.amphur_name_en) name_en ");
		} else {
			sql.append(" sa.amphur_name_th name_th, sa.amphur_name_en name_en ");
		}

		sql.append(" from sys_amphur sa ");
		sql.append(" where 1 = 1 and sa.active_flag = true ");
		
		if (null != criteria.getPkId()) {
			sql.append(" and sa.amphur_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != criteria.getId()) {
			sql.append(" and sa.province_id = ? ");
			params.add(criteria.getId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (sa.amphur_name_th ilike ? or sa.amphur_name_en  ilike ? or sa.amphur_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by sa.amphur_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}

	public List<DropdownTambonData> getTambon(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select st.tambon_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', st.tambon_code , ']-', st.tambon_name_th) name_th, concat('[', st.tambon_code , ']-', st.tambon_name_en) name_en, st.zip_code ");
		} else {
			sql.append(" st.tambon_name_th name_th, st.tambon_name_en name_en, st.zip_code ");
		}

		sql.append(" from sys_tambon st ");
		sql.append(" where 1 = 1 and st.active_flag = true ");
		
		if (null != criteria.getPkId()) {
			sql.append(" and st.tambon_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (null != criteria.getId()) {
			sql.append(" and st.amphur_id = ? ");
			params.add(criteria.getId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (st.tambon_name_th ilike ? or st.tambon_name_en  ilike ? or st.tambon_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by st.tambon_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownTambonData.class), params.toArray());
	}
	
	public List<DropdownData> getPrefixname(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select sp.prefixname_id value, ");
		if (Boolean.TRUE.equals(criteria.getDisplayCode())) {
			sql.append(" concat('[', sp.prefixname_code , ']-', sp.prefixname_name_th) name_th, concat('[', sp.prefixname_code , ']-', sp.prefixname_name_en) name_en ");
		} else {
			sql.append(" sp.prefixname_name_th name_th, sp.prefixname_name_en name_en ");
		}

		sql.append(" from sys_prefixname sp ");
		sql.append(" where 1 = 1 and sp.active_flag = true ");
		
		if (null != criteria.getPkId()) {
			sql.append(" and sp.prefixname_id = ? ");
			params.add(criteria.getPkId());
		}
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (sp.prefixname_name_th ilike ? or sp.prefixname_name_en  ilike ? or sp.prefixname_code ilike ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by sp.prefixname_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
	
        public List<DropdownData> getTeacher(DropdownCriteriaData criteria) {

		StringBuilder sql = new StringBuilder();
		
		List<Object> params = new ArrayList<>();
		
		sql.append("select mp.personal_id value,concat(mp.prefix_short_th,' ',mp.firstname_th,' ',mp.lastname_th) name_th,concat(mp.prefix_short_en,' ',mp.firstname_en,' ',mp.lastname_en) name_en,mp.* ");
		sql.append(" from mas_personal mp ");
		sql.append(" where 1 = 1 and mp.active_flag = true ");
		
		if (StringUtils.isNoneBlank(criteria.getSearchValue())) {
			sql.append(" and (mp.prefix_short_th=? or mp.prefix_short_en=? or mp.firstname_th like ? or mp.lastname_th like ? or mp.firstname_en like ? or mp.lastname_en like ?) ");
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getSearchValue(), true, true));
		}

		sql.append(" order by mp.personal_id asc ");
		sql.append(DEFAULT_DROPDOWN_LIMIT);

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DropdownData.class), params.toArray());
	}
}
