package com.dev.core.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import com.dev.core.dao.UserDao;
import com.dev.core.model.RoleModel;
import com.dev.core.model.UserModel;


@Repository
public class UserDaoImpl implements UserDao {

	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@SuppressWarnings("unchecked")
	public UserModel getLoginCheck(Map<String, String> map) {

		String oaGH = map.get("username");
		String companyCode = map.get("companyCode");

		String sql = "select * from vrep_dict_employee where (userCode='" + oaGH + "' or employNo='" + oaGH
				+ "' or employeeCode='" + oaGH + "') and companyCode='" + companyCode + "' ";
		logger.info(sql);
		try {
			UserModel userModel = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(UserModel.class));

			userModel.setCompanyCode(companyCode);
			sql = "SELECT * FROM rep_role_employee WHERE employeeCode = '" + userModel.getEmployeeCode() + "'";
			List<RoleModel> roleList = jdbcTemplate.query(sql, new RoleMapper());
			logger.info(sql);
			String strRoleList = "";
			for (int i = 0; i < roleList.size(); i++) {
				RoleModel roleModel = roleList.get(i);
				if (i < roleList.size() - 1) {
					strRoleList += "'" + roleModel.getRoleID() + "',";
				} else {
					strRoleList += "'" + roleModel.getRoleID() + "'";
				}
			}
			userModel.setRoleList(strRoleList);
			return userModel;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return null;

		}
	}

	@SuppressWarnings("rawtypes")
	private static final class RoleMapper implements RowMapper {

		
		public Object mapRow(java.sql.ResultSet rs, int arg1) throws SQLException {
			RoleModel roleModel = new RoleModel();
			roleModel.setRoleID(rs.getString("roleID"));
			return roleModel;
		}

	}
}
