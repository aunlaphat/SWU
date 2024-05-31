package com.arg.swu.services;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.BuasriGenerateData;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class ScheduleService implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(ScheduleService.class);

	private final JdbcTemplate jdbcTemplate;
	
	public List<BuasriGenerateData> findBuasriListData(boolean generate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select mi.member_id, mi.buasri_id, mi.member_firstname_en, mi.member_lastname_en, mi.member_cardno, '201' gidnumber, '100M' M100 ");
		sql.append(" from member_info mi ");
		sql.append(WHERE);
		if (generate) {			
			sql.append(" and mi.buasri_id is not null ");
			sql.append(" and mi.buasri_id_expire_date > now() ");
			sql.append(" and (mi.buasri_active_status is null or mi.buasri_active_status = false) ");
		} else {
			sql.append(" and mi.buasri_id_expire_date < now() ");
			sql.append(" and mi.buasri_active_status = true ");
		}
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(BuasriGenerateData.class));
	}
	
	@Transactional
	public void updateBuasriActiveStatus(List<Long> ids, boolean buasriActiveStatus) {
		if (null != ids && !ids.isEmpty()) {
			List<Object> params = new ArrayList<>();
			StringBuilder sql = new StringBuilder();
			
			sql.append(" update member_info set buasri_active_status = ? ");
			params.add(buasriActiveStatus);
			
			String inCodition = String.format(" where member_id in (%s) ", String.join(",", Collections.nCopies(ids.size(), "?")));
			sql.append(inCodition);
			params.addAll(ids);
			
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}
	
	@Transactional
	public void dumpMasPersonalFromApi() {
		
		StringBuilder insertSql = new StringBuilder();
		insertSql.append(" insert into mas_personal ( ");
		insertSql.append(" 	personal_type, ");
		insertSql.append(" 	personal_line, ");
		insertSql.append(" 	buasri_id, ");
		insertSql.append(" 	status_id, ");
		insertSql.append(" 	status_name_th, ");
		insertSql.append(" 	status_name_en, ");
		insertSql.append(" 	email, ");
		insertSql.append(" 	prefix_short_th, ");
		insertSql.append(" 	prefix_th, ");
		insertSql.append(" 	prefix_short_en, ");
		insertSql.append(" 	prefix_en,  ");
		insertSql.append(" 	firstname_th, ");
		insertSql.append(" 	middlename_th, ");
		insertSql.append(" 	lastname_th, ");
		insertSql.append(" 	firstname_en, ");
		insertSql.append(" 	middlename_en, ");
		insertSql.append(" 	lastname_en, ");
		insertSql.append(" 	position_th, ");
		insertSql.append(" 	position_en, ");
		insertSql.append(" 	dep_code_level1, ");
		insertSql.append(" 	dep_code_level2, ");
		insertSql.append(" 	active_flag, ");
		insertSql.append(" 	create_date, ");
		insertSql.append(" 	create_by_id ");
		insertSql.append(" ) ");
		insertSql.append(" select  ");
		insertSql.append(" st.catalog_id personal_type, ");
		insertSql.append(" tp.staff_line personal_line, ");
		insertSql.append(" tp.buasri_id, ");
		insertSql.append(" tp.status_id, ");
		insertSql.append(" tp.status_th status_name_th, ");
		insertSql.append(" tp.status_en status_name_en, ");
		insertSql.append(" tp.gafe_mail email, ");
		insertSql.append(" tp.pname_int_sname_th prefix_short_th, ");
		insertSql.append(" tp.pname_int_lname_th prefix_th, ");
		insertSql.append(" tp.pname_int_sname_en prefix_short_en, ");
		insertSql.append(" tp.pname_int_lname_en prefix_en,  ");
		insertSql.append(" tp.person_fname_th firstname_th, ");
		insertSql.append(" tp.person_mname_th middlename_th, ");
		insertSql.append(" tp.person_lname_th lastname_th, ");
		insertSql.append(" tp.person_fname_en firstname_en, ");
		insertSql.append(" tp.person_mname_en middlename_en, ");
		insertSql.append(" tp.person_lname_en lastname_en, ");
		insertSql.append(" tp.position_th, ");
		insertSql.append(" tp.position_en, ");
		insertSql.append(" tp.faculty_id dep_code_level1, ");
		insertSql.append(" tp.dept_id dep_code_level2, ");
		insertSql.append(" true active_flag, ");
		insertSql.append(" now() create_date, ");
		insertSql.append(" 2 create_by_id ");
		insertSql.append(" from temp_personal tp ");
		insertSql.append(" left join lookup_catalog st on st.key = tp.staff_type and st.parent_id = 30020000 ");
		insertSql.append(" left join mas_personal mp  ");
		insertSql.append(" on mp.buasri_id = tp.buasri_id  ");
		insertSql.append(" where mp.buasri_id is null ");
		
		StringBuilder updateSql = new StringBuilder();
		updateSql.append(" update mas_personal ");
		updateSql.append(" set  ");
		updateSql.append(" personal_type = st.catalog_id, ");
		updateSql.append(" personal_line = tp.staff_line, ");
		updateSql.append(" buasri_id = tp.buasri_id, ");
		updateSql.append(" status_id = tp.status_id, ");
		updateSql.append(" status_name_th = tp.status_th, ");
		updateSql.append(" status_name_en = tp.status_en, ");
		updateSql.append(" email = tp.gafe_mail, ");
		updateSql.append(" prefix_short_th = tp.pname_int_sname_th, ");
		updateSql.append(" prefix_th = tp.pname_int_lname_th, ");
		updateSql.append(" prefix_short_en = tp.pname_int_sname_en, ");
		updateSql.append(" prefix_en = tp.pname_int_lname_en,  ");
		updateSql.append(" firstname_th = tp.person_fname_th, ");
		updateSql.append(" middlename_th = tp.person_mname_th, ");
		updateSql.append(" lastname_th = tp.person_lname_th, ");
		updateSql.append(" firstname_en = tp.person_fname_en, ");
		updateSql.append(" middlename_en = tp.person_mname_en, ");
		updateSql.append(" lastname_en = tp.person_lname_en, ");
		updateSql.append(" position_th = tp.position_th, ");
		updateSql.append(" position_en = tp.position_en, ");
		updateSql.append(" dep_code_level1 = tp.faculty_id, ");
		updateSql.append(" dep_code_level2 = tp.dept_id, ");
		updateSql.append(" active_flag = true, ");
		updateSql.append(" update_date = now(), ");
		updateSql.append(" update_by_id = 2 ");
		updateSql.append(" from temp_personal tp ");
		updateSql.append(" left join lookup_catalog st on st.key = tp.staff_type and st.parent_id = 30020000 ");
		updateSql.append(" where mas_personal.buasri_id = tp.buasri_id ");
		
		StringBuilder sqlUpdateDeptId = new StringBuilder();
		sqlUpdateDeptId.append(" update mas_personal ");
		sqlUpdateDeptId.append(" set ");
		sqlUpdateDeptId.append(" dep_id_level1 = md1.dep_id, ");
		sqlUpdateDeptId.append(" dep_id_level2 = md2.dep_id ");
		sqlUpdateDeptId.append(" from mas_personal mp ");
		sqlUpdateDeptId.append(" left join mas_department md1 on mp.dep_code_level1 = md1.dep_code ");
		sqlUpdateDeptId.append(" left join mas_department md2 on mp.dep_code_level2 = md2.dep_code ");
		sqlUpdateDeptId.append(" where mp.personal_id = mas_personal.personal_id ");
		
		StringBuilder sqlUpdateUser = new StringBuilder();
		sqlUpdateUser.append(" update ");
		sqlUpdateUser.append(" 	aut_user ");
		sqlUpdateUser.append(" set ");
		sqlUpdateUser.append(" 	firstname_en = mp.firstname_en, ");
		sqlUpdateUser.append(" 	lastname_en = mp.lastname_en, ");
		sqlUpdateUser.append(" 	firstname_th = mp.firstname_th, ");
		sqlUpdateUser.append(" 	lastname_th = mp.lastname_th, ");
		sqlUpdateUser.append(" 	dep_id_level1 = mp.dep_id_level1, ");
		sqlUpdateUser.append(" 	dep_id_level2 = mp.dep_id_level2, ");
		sqlUpdateUser.append(" 	email = mp.email ");
		sqlUpdateUser.append(" from ");
		sqlUpdateUser.append(" 	mas_personal mp ");
		sqlUpdateUser.append(" where aut_user.username = mp.buasri_id ");
		
		jdbcTemplate.execute(" update mas_personal set active_flag = false where 1 = 1 ");
		jdbcTemplate.execute(insertSql.toString());
		jdbcTemplate.execute(updateSql.toString());
		jdbcTemplate.execute(sqlUpdateDeptId.toString());
		jdbcTemplate.execute(sqlUpdateUser.toString());
		jdbcTemplate.execute("truncate table temp_personal");
	}
	
	@Transactional
	public void dumpMasDepartmentFromApi() {
		StringBuilder insertSql = new StringBuilder();
		insertSql.append(" insert into mas_department ( ");
		insertSql.append(" 	dep_code, ");
		insertSql.append(" 	dep_name_th, ");
		insertSql.append(" 	dep_name_en, ");
		insertSql.append(" 	dep_name_short_th, ");
		insertSql.append(" 	dep_name_short_en, ");
		insertSql.append(" 	dep_name_abbr_th, ");
		insertSql.append(" 	dep_name_abbr_en, ");
		insertSql.append(" 	active_flag, ");
		insertSql.append(" 	create_by_id, ");
		insertSql.append(" 	create_date ");
		insertSql.append(" ) ");
		insertSql.append(" select ");
		insertSql.append(" td.dept_id dep_code, ");
		insertSql.append(" td.dept_lname_th dep_name_th, ");
		insertSql.append(" td.dept_lname_en dep_name_en, ");
		insertSql.append(" td.dept_sname_th dep_name_short_th, ");
		insertSql.append(" td.dept_sname_en dep_name_short_en, ");
		insertSql.append(" td.dept_abbv_th dep_name_abbr_th, ");
		insertSql.append(" td.dept_abbv_en dep_name_abbr_en, ");
		insertSql.append(" true active_flag, ");
		insertSql.append(" 2 create_by_id, ");
		insertSql.append(" now() create_date ");
		insertSql.append(" from temp_department td ");
		insertSql.append(" left join mas_department md ");
		insertSql.append(" on md.dep_code = td.dept_id ");
		insertSql.append(" where md.dep_code is null ");
		
		StringBuilder updateSql = new StringBuilder();
		updateSql.append(" update mas_department ");
		updateSql.append(" set ");
		updateSql.append(" dep_code = td.dept_id, ");
		updateSql.append(" dep_name_th = td.dept_lname_th, ");
		updateSql.append(" dep_name_en = td.dept_lname_en, ");
		updateSql.append(" dep_name_short_th = td.dept_sname_th, ");
		updateSql.append(" dep_name_short_en = td.dept_sname_en, ");
		updateSql.append(" dep_name_abbr_th = td.dept_abbv_th, ");
		updateSql.append(" dep_name_abbr_en = td.dept_abbv_en, ");
		updateSql.append(" active_flag = true, ");
		updateSql.append(" update_by_id = 2, ");
		updateSql.append(" update_date = now() ");
		updateSql.append(" from temp_department td ");
		updateSql.append(" where mas_department.dep_code = td.dept_id ");
		
		jdbcTemplate.execute(" update mas_department set active_flag = false where 1 = 1 ");
		jdbcTemplate.execute(insertSql.toString());
		jdbcTemplate.execute(updateSql.toString());
		jdbcTemplate.execute("truncate table temp_department");
		
	}

    @Transactional
	public Boolean updateFianceAfterPayment(Long paymentId, Long paymentType) {
    	
    	return jdbcTemplate.queryForObject(" select update_finance_payment(?, now()::timestamp, ?) ", Boolean.class, paymentId, String.valueOf(paymentType));
    }
    
    @Transactional
    public void callProcImportFinancePayment(Long impId) throws Exception {
    	Map<String, Object> resultData = jdbcTemplate.call(connection -> {
    		Integer i = 1;
    		CallableStatement statment = connection.prepareCall("call proc_import_finance_payment(?)");
    		statment.setLong(i++, impId);
    		return statment;
    	}, List.of());
    	
    	LOG.info("callProcImportFinancePayment -> {}",resultData);
    }
    
	
}
