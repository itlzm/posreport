package com.dev.core.dao.impl;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.SystemMainDao;
import com.dev.core.model.UserModel;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;

@Repository
public class SystemMainDaoImpl implements SystemMainDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	// 记录日志信息
	private static Logger logger = Logger.getLogger(SystemMainDaoImpl.class);

	
	@SuppressWarnings("rawtypes")
	public List getMenuFunctionList(UserModel userModel) {

		// 菜单表
		String sql = "select distinct FuncID,PID,ClassLevel,IsLast,TitleText,DescText,MenuWebPageUrl,employeeCode from vrep_dict_employeerolefuncid where employeeCode = '"
				+ userModel.getEmployeeCode() + "'  order by FuncID";
		// 查询菜单个数
		String totalSQL = "select count(1) from vrep_dict_employeerolefuncid where employeeCode = '"
				+ userModel.getEmployeeCode() + "' ";
		PageModel pageModel = new PageModel();
		Pagination page = new Pagination(sql, 1, 1000, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
	}

	/*
	 * 返回菜单按钮信息
	 */
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public List getMenuFunctionButtonList(Map<String, String> map) {

		String companyCode = map.get("companyCode");
		String roleID = map.get("roleID");
		String funcID = map.get("funcID");

		List queryData = new ArrayList();

		String strSql = "";

		String totalSQL = "";

		strSql = "SELECT distinct A.EventName" + ",A.DisplayText" + ",A.ButtonImage" + ",A.ModalsPageName"
				+ " FROM rep_function_button  AS A"
				+ " INNER JOIN rep_role_function_button AS B ON A.FuncID = B.FuncID AND A.ButtonID = B.ButtonID"
				+ " WHERE B.companyCode = '" + companyCode + "'" + " AND (B.roleID in (" + roleID + ") or B.roleID ='00')"
				+ "AND B.FuncID = '" + funcID + "' ORDER  BY A.SortOrder";

		logger.info(strSql);
		
		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		PageModel pageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
	}

}
