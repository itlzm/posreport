package com.dev.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.MerchantDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;
@Repository
public class MerchantDaoImpl implements MerchantDao {
	
	private static Logger logger = Logger.getLogger(StoreDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, List<Object>> queryMerchantDetail(Map<String, String> map) {
		Map<String, List<Object>> queryMerchantDetailMap = new HashMap<String, List<Object>>();
		
		String contractPerson = map.get("ContractPerson");
		String fullName = map.get("FullName");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		
		String strSelect = "";
		strSelect += "companyCode AS '商家编码',fullName AS '商家名称',phone AS '电话',contractAddress AS '地址',contractPerson AS '联系人'";
		
		
		String sqlWhere = "";
		sqlWhere += " WHERE companyCode IS NOT NULL";
		if(contractPerson != "" && contractPerson != null){
			sqlWhere += " AND contractPerson in('" + contractPerson + "')";
		}
		if(fullName != "" && fullName != null){
			sqlWhere += " AND fullName in('" + fullName + "')";
		}
		
		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "cos_merchant");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		List<Object> dataListMerchant = getQueryData(mappam, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		String strSumSelect = "";
	
		strSumSelect += "'合计' AS '商家编码','' AS '商家名称','' AS '电话','' AS '地址','' AS '联系人'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from cos_merchant ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		logger.info("strFromTab:" + strFromTab);

		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModelSum = new PageModel();

		Map<String, String> mappamSum = new HashMap<String, String>();
		mappamSum.put(new String("selectCol"), strSumSelect);
		mappamSum.put(new String("tabName"), strFromTab);
		mappamSum.put(new String("sqlWhere"), "");
		mappamSum.put(new String("pageIndex"), "1");
		mappamSum.put(new String("pageSize"), "1");

		List<Object> dataListSum = getQueryData(mappamSum, pageModelSum);
		
		queryMerchantDetailMap.put(new String("listData"), dataListMerchant);
		queryMerchantDetailMap.put(new String("pageData"), pageModelList);
		queryMerchantDetailMap.put(new String("listDataSum"), dataListSum);
		
		return queryMerchantDetailMap;
	}
	
	public Map<String, List<Object>> queryMerchantDetail2(Map<String, String> map) {
		
		Map<String, List<Object>> queryMerchantDetailMap = new HashMap<String, List<Object>>();
		
		//取出Controller调用该方法时，传入map参数中的值
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String contractPerson = map.get("ContractPerson");
		String fullName = map.get("FullName");
		
		//拼写SQL语句,strSelect:列出需要查询的字段,sqlWhere:过滤的条件
		String strSelect = "companyCode AS '商家编码',"
				+ "fullName AS '商家名称',"
				+ "phone AS '电话',"
				+ "contractAddress AS '地址',"
				+ "contractPerson AS '联系人'";
		String sqlWhere = " WHERE companyCode IS NOT NULL";
		//若用户输入了商家名称或联系人，即fullName和contractPerson的值不为""时，需要在SQL语句中加上过滤条件
		//使用like，模糊查询，%：表示任意个或多个字符
		//加上非null判断，解决导出Excel时，表格中没有数据问题
		if(fullName != "" && fullName != null){
			sqlWhere += " AND fullName like'%" + fullName + "%' ";
		}
		if(contractPerson != "" && contractPerson != null){
			sqlWhere += " AND contractPerson like'%" + contractPerson + "%' ";
		}
		
		//执行SQL语句，查询明细数据 带分页
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "cos_merchant");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		//二维表格中详细数据
		List<Object> dataListMerchant = getQueryData(mappam, pageModel);
		//分页信息明细数据
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		//记录日志
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		//拼写SQL语句,查询表格最下面一行合计信息,使用sum函数,使dataListSum的size为1,不受pageSize值影响
		String strSumSelect = "'合计' AS '商家编码','' AS '商家名称','' AS '电话','' AS '地址'";
		strSumSelect += ",IFNULL(SUM(`联系人`),0) AS '联系人'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from cos_merchant ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		//记录日志
		logger.info("strFromTab:" + strFromTab);
		
		//执行SQL语句，二维表格最下面一行，求合计的数据信息
		PageModel pageModelSum = new PageModel();

		Map<String, String> mappamSum = new HashMap<String, String>();
		mappamSum.put(new String("selectCol"), strSumSelect);
		mappamSum.put(new String("tabName"), strFromTab);
		mappamSum.put(new String("sqlWhere"), "");
		mappamSum.put(new String("pageIndex"), "1");
		mappamSum.put(new String("pageSize"), "10");

		//二维表格最下面一行，求合计的数据信息
		List<Object> dataListSum = new ArrayList<Object>();
		if (PageIndex == 1) {
			dataListSum = getQueryData(mappamSum, pageModelSum);
		}
		
		//listData:js获取到listData后，遍历得到二维表格中详细数据。
		//pageData:js获取到pageData后，绑定分页信息，共[266]条记录 当前第[1]页/共[18]页
		//listDataSum:js获取到listDataSum后，其长度为1，遍历生成二维表格<thead></thead>，表格中最下面一行合计信息
		queryMerchantDetailMap.put(new String("listData"), dataListMerchant);
		queryMerchantDetailMap.put(new String("pageData"), pageModelList);
		queryMerchantDetailMap.put(new String("listDataSum"), dataListSum);
		
		return queryMerchantDetailMap;
	}
	
	/*
	 * 根据查询语句返回查询信息
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getQueryData(Map<String, String> map, PageModel pageModel) {

		String selectCol = map.get("selectCol");
		String tabName = map.get("tabName");
		String sqlWhere = map.get("sqlWhere");
		int pageIndex = Integer.parseInt(map.get("pageIndex"));
		int pageSize = Integer.parseInt(map.get("pageSize"));

		// 下拉表
		String sql = "select " + selectCol + " from " + tabName;

		if (sqlWhere != "") {
			sql += " " + sqlWhere;
			// totalSQL += " " + sqlWhere;
		}

		// 记录个数
		String totalSQL = "select count(1) from (" + sql + ") as Tab";

		logger.info("sql:" + sql);
		logger.info("totalSQL:" + totalSQL);

		Pagination page = new Pagination(sql, pageIndex, pageSize, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
	}

}
