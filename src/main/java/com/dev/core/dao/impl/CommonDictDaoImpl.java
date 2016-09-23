package com.dev.core.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.CommonDictDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;

/*
 * 公共字典Dao接口实现
 * */

@Repository
public class CommonDictDaoImpl implements CommonDictDao {

	private static Logger logger = Logger.getLogger(CommonDictDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@SuppressWarnings("rawtypes")
	public List getDropDownDictList(Map<String, String> map) {
		//查询列名
		String selectCol = map.get("selectCol");
		//表名
		String moduleName = map.get("moduleName");
		//查询条件
		String sqlWhere = map.get("sqlWhere");

		// 下拉表
		String sql = "select " + selectCol + " from " + moduleName;
		// 记录个数
		String totalSQL = "select count(1) from " + moduleName;

		if (sqlWhere != "") {
			sql += " " + sqlWhere;

			/*
			 * if (!sqlWhere.startsWith("GROUP")) { totalSQL += " " + sqlWhere;
			 * }
			 */
		}
		logger.info(sql);
		PageModel pageModel = new PageModel();
		Pagination page = new Pagination(sql, 1, 10000, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
	}

}
