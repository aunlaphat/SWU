package com.arg.swu.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.AllMenuData;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class LoginService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(LoginService.class);
	
	private final JdbcTemplate jdbcTemplate;
	
	public List<AllMenuData> getAllMenu(Long userId) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" am.menu_id, ");
		sql.append(" am.menu_code, ");
		sql.append(" am.menu_type, ");
		sql.append(" am.parent_id, ");
		sql.append(" am.order_number, ");
		sql.append(" am.name, ");
		sql.append(" am.show_name_key, ");
		sql.append(" am.link, ");
		sql.append(" r.active_flag ");
		sql.append(" from aut_menu am ");
		sql.append(" left join ( ");
		sql.append("  select  ");
		sql.append("   ap.menu_id, ");
		sql.append("   max(case when active_flag then 1 else 0 end)::boolean as active_flag ");
		sql.append("  from aut_permission ap  ");
		sql.append("  where 1 = 1 ");
		sql.append("  and ap.active_flag = true  ");
		sql.append("  and ap.role_id in ( ");
		sql.append("   select aur.role_id  ");
		sql.append("   from aut_user_role aur ");
		sql.append("   where aur.user_id = ? ");
		sql.append("  ) ");
		sql.append("  group by ap.menu_id ");
		sql.append(" ) r ");
		sql.append(" on am.menu_id  = r.menu_id ");
		sql.append(" where 1=1 ");
		sql.append(" and am.active_flag = true ");
		sql.append(" and r.active_flag = true ");
		sql.append(" order by am.menu_id asc ");
		
		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(AllMenuData.class), userId);
	}
	
}
