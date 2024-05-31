package com.arg.swu.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.arg.swu.dto.AllMenuData;
import com.arg.swu.dto.AutRoleData;
import com.arg.swu.dto.AutUserData;
import com.arg.swu.dto.MemberAddressData;
import com.arg.swu.dto.MemberInfoData;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class UserManagementService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(UserManagementService.class);

	private final JdbcTemplate jdbcTemplate;
	
	public Map<String, Object> findUserByCondition(AutUserData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getUsername())) {
			conditions.append(" and au.username ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getUsername(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getEmail())) {
			conditions.append(" and au.email ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getEmail(), true, true));
		}
		
		if (null != criteria.getIsLocal()) {
			if (Boolean.TRUE.equals(criteria.getIsLocal())) {				
				conditions.append(" and au.is_local = ? ");
				params.add(criteria.getIsLocal());
			} else {
				conditions.append(" and ( au.is_local = false or au.is_local is null ) ");
			}
		}
		
		if (StringUtils.isNoneBlank(criteria.getFirstnameTh())) {
			String[] fullname = criteria.getFirstnameTh().split("\\s+");
			String firstname = StringUtils.trim(fullname[0]);
			String lastname = fullname.length == 2 ? StringUtils.trim(fullname[1]) : null;
			
			if (StringUtils.isNotBlank(firstname) && StringUtils.isNotBlank(lastname)) {
				if (StringUtils.isNotBlank(firstname)) {
					conditions.append(" and (au.firstname_th ilike ? or au.firstname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
				}
				if (StringUtils.isNotBlank(lastname)) {				
					conditions.append(" and (au.lastname_th ilike ? or au.lastname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
				}
			} else if (StringUtils.isNotBlank(firstname)) {
				conditions.append(" and (au.firstname_th ilike ? or au.firstname_en ilike ? or au.lastname_th ilike ? or au.lastname_en ilike ?) ");
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
			}
		}
		
		if (StringUtils.isNoneBlank(criteria.getTelephone())) {
			conditions.append(" and au.telephone ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getTelephone(), true, true));
		}
		
		if (null != criteria.getRoleIds() && !criteria.getRoleIds().isEmpty()) {
			String inCodition = String.format(" and aur.role_id in (%s) ", String.join(",", Collections.nCopies(criteria.getRoleIds().size(), "?")));
			conditions.append(inCodition);
			params.addAll(criteria.getRoleIds());
		}
		
		if (null != criteria.getRoleId()) {
			conditions.append(" and aur.role_id = ? ");
			params.add(criteria.getRoleId());
		}
		
		if (null != criteria.getRefUserType()) {
			conditions.append(" and au.ref_user_type = ? ");
			params.add(criteria.getRefUserType());
		} 
		/**
		else {
			conditions.append(" and (au.ref_user_type = ? or au.ref_user_type is null) ");
			params.add(30032001l);
		}
		*/
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and au.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by au.create_date desc ) AS row_num, ");
		sb.append(" au.user_id, au.user_photo_path, au.username, au.ref_user_type, au.ref_user_id, au.firstname_th, au.lastname_th, au.firstname_en, au.lastname_en, au.dep_id_level1, au.dep_id_level2, au.email, au.telephone, au.user_signature_path, au.is_local, au.active_flag, au.active_period_status, au.active_period_start, au.active_period_end, au.access_level, au.last_login_datetime, au.last_logout_datetime, au.last_changepasswd_datetime ");
		sb.append(" from aut_user au ");
		sb.append(" left join aut_user_role aur on au.user_id = aur.user_id ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" group by  au.user_id, au.user_photo_path, au.username, au.ref_user_type, au.ref_user_id, au.firstname_th, au.lastname_th, au.firstname_en, au.lastname_en, au.dep_id_level1, au.dep_id_level2, au.email, au.telephone, au.user_signature_path, au.is_local, au.active_flag, au.active_period_status, au.active_period_start, au.active_period_end, au.access_level, au.last_login_datetime, au.last_logout_datetime, au.last_changepasswd_datetime ");
		sb.append(" order by au.create_date desc ");
		sb.append(LIMIT);

		List<AutUserData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(AutUserData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from ( ");
		count.append("select au.user_id from aut_user au ");
		count.append(" left join aut_user_role aur on au.user_id = aur.user_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		count.append(" group by au.user_id ");
		count.append(" ) a ");
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public List<AutRoleData> getRolesByUserId(Long userId) {
		return jdbcTemplate.query("select ar.* from aut_user_role aur left join aut_role ar on aur.role_id = ar.role_id where aur.user_id = ? and ar.active_flag = true", BeanPropertyRowMapper.newInstance(AutRoleData.class), userId);
	}
	
	public Map<String, Object> findRoleByCondition(AutRoleData criteria) {
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getName())) {
			conditions.append(" and ar.name ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getName(), true, true));
		}
		
		if (null != criteria.getActiveFlag()) {
			conditions.append(" and ar.active_flag = ? ");
			params.add(criteria.getActiveFlag());
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select row_number() OVER (order by ar.role_id asc ) AS row_num, ar.* ");
		sb.append(" from aut_role ar ");
		
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by ar.role_id asc ");
		sb.append(LIMIT);
		
		List<AutRoleData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(AutRoleData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from aut_role ar ");
		count.append(WHERE);
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public List<AllMenuData> getPermission(AutRoleData role) {
		
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (null != role.getRoleId()) {
			sb.append(" select ");
			sb.append(" p.permission_id, ");
			sb.append(" am.menu_id, ");
			sb.append(" am.menu_code, ");
			sb.append(" am.menu_type, ");
			sb.append(" am.parent_id, ");
			sb.append(" am.order_number, ");
			sb.append(" am.name, ");
			sb.append(" am.show_name_key, ");
			sb.append(" am.link, ");
			sb.append(" p.active_flag ");
			sb.append(" from aut_menu am ");
			sb.append(" left join ( ");
			sb.append("  select ap.permission_id, ap.menu_id, ap.active_flag ");
			sb.append("  from aut_permission ap  ");
			sb.append("  where 1 = 1 ");
			sb.append("  and ap.role_id = ? ");
			sb.append(" ) p ");
			sb.append(" on am.menu_id = p.menu_id ");
			sb.append(" where am.active_flag = true ");
			sb.append(" order by am.menu_id, am.order_number ");
			params.add(role.getRoleId());
		} else {
			sb.append(" select ");
			sb.append(" null permission_id, ");
			sb.append(" am.menu_id, ");
			sb.append(" am.menu_code, ");
			sb.append(" am.menu_type, ");
			sb.append(" am.parent_id, ");
			sb.append(" am.order_number, ");
			sb.append(" am.name, ");
			sb.append(" am.show_name_key, ");
			sb.append(" am.link, ");
			sb.append(" false active_flag ");
			sb.append(" from aut_menu am ");
			sb.append(" where am.active_flag = true ");
			sb.append(" order by am.menu_id, am.order_number ");
		}
		return jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(AllMenuData.class), params.toArray());
	}
	
	public Map<String, Object> findMemberInfoByCondition(MemberInfoData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if (StringUtils.isNoneBlank(criteria.getMemberCardno())) {
			conditions.append(" and mi.member_cardno like ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getMemberCardno(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getMemberEmail())) {
			conditions.append(" and mi.member_email ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getMemberEmail(), true, true));
		}
		
		if (StringUtils.isNoneBlank(criteria.getMemberFirstnameTh())) {
			
			String[] fullname = criteria.getMemberFirstnameTh().split("\\s+");
			String firstname = StringUtils.trim(fullname[0]);
			String lastname = fullname.length == 2 ? StringUtils.trim(fullname[1]) : null;
			
			if (StringUtils.isNotBlank(firstname) && StringUtils.isNotBlank(lastname)) {
				if (StringUtils.isNotBlank(firstname)) {
					conditions.append(" and (mi.member_firstname_th ilike ? or mi.member_firstname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
					params.add(CommonUtils.concatLikeParam(firstname, true, true));
				}
				if (StringUtils.isNotBlank(lastname)) {				
					conditions.append(" and (mi.member_lastname_th ilike ? or mi.member_lastname_en ilike ?) ");
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
					params.add(CommonUtils.concatLikeParam(lastname, true, true));
				}
			} else if (StringUtils.isNotBlank(firstname)) {
				conditions.append(" and (mi.member_firstname_th ilike ? or mi.member_firstname_en ilike ? or mi.member_lastname_th ilike ? or mi.member_lastname_en ilike ?) ");
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
				params.add(CommonUtils.concatLikeParam(firstname, true, true));
			}
			
		}
		
		if (null != criteria.getMemberPhoneno()) {
			conditions.append(" and mi.member_phoneno ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getMemberPhoneno(), true, true));
		}
				
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by mi.member_id asc ) AS row_num, ");  
		sb.append(" mc.name_lo channel_th, mc.name_en channel_en, ");  
		sb.append(" mi.*, au.last_login_datetime as last_login ");
		sb.append(" from member_info mi  ");
		sb.append(" left join lookup_catalog mc on mi.member_channel = mc.catalog_id ");
		sb.append(" left join aut_user au on au.ref_user_id = mi.member_id and au.ref_user_type = 30032002 ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by mi.member_id asc ");
		sb.append(LIMIT);
		
		List<MemberInfoData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MemberInfoData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from member_info mi left join lookup_catalog mc on mi.member_channel = mc.catalog_id ");
		count.append(" left join aut_user au on au.ref_user_id = mi.member_id and au.ref_user_type = 30032002 where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}
	
	public Map<String, Object> findMemberAddressByCondition(MemberAddressData criteria) {
		
		Map<String, Object> result = new HashMap<>();
		
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
				
		StringBuilder sb = new StringBuilder();
		sb.append(" select row_number() OVER (order by ma.member_address_id asc ) AS row_num, ");  
		sb.append(" ma.* ");
		sb.append(" from member_address ma  ");
		sb.append(WHERE);
		
		sb.append(conditions.toString());
		
		sb.append(" order by ma.member_address_id asc ");
		sb.append(LIMIT);
		
		List<MemberAddressData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(MemberAddressData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		
		StringBuilder count = new StringBuilder();
		count.append("select count(*) from member_address ma where 1 = 1 ");
		count.append(conditions.toString());
		
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		
		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);
		
		return result;
	}

}
