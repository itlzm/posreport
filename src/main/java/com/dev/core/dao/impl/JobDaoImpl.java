package com.dev.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.JobDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;
import com.sun.javafx.binding.StringFormatter;

import jdk.nashorn.internal.objects.annotations.Where;
@Repository
public class JobDaoImpl implements JobDao {
	
	private static Logger logger = Logger.getLogger(StoreDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, List<Object>> queryJobDetail(Map<String, String> map) {
		
		Map<String, List<Object>> queryJobDetailMap = new HashMap<String, List<Object>>();
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		String jobStep = map.get("jobStep");
		
		String strSelect = "";
		strSelect += "repUUID AS '日志编码', DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日志时间',jobStep AS '日志步骤编号', stepDo AS '日志步骤名称', stepDesc AS  '日志步骤说明'";
		
		
		String sqlWhere = "";
		sqlWhere += " WHERE repUUID IS NOT NULL";
		if(jobStep != "" && jobStep != null){
			sqlWhere += " AND jobStep = '" + jobStep + "'";
		}
		
		sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
		
		
		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "rep_job_log");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		List<Object> dataListJob = getQueryData(mappam, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		/*
		 * 查询制作图形报表所需数据
		 * SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  GROUP BY jobStep
		 */
		
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  ";
		//下拉控件选择执行步骤，默认选择全部执行步骤，当选中指定执行步骤时，sql语句中加入where过滤条件
		if(jobStep != "" && jobStep != null){
			strSql += " WHERE jobStep = '" + jobStep + "'";
			strSql += " GROUP BY jobStep";
		}else{
			strSql += " GROUP BY jobStep";
		}
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTX = page.getResultList();
		
		String strSumSelect = "";
		//repUUID AS '日志编码', jobTime AS '日志时间',jobStep AS '日志步骤编号', stepDo AS '日志步骤名称', stepDesc AS  '日志步骤说明'
		//strSumSelect += "'合计' AS '日志编码','' AS '日志时间','' AS '日志步骤编号','' AS '日志步骤名称','' AS '日志步骤说明'";
		strSumSelect += "'合计' AS '日志编码','' AS '日志时间'";
		strSumSelect += ",IFNULL(SUM(`日志步骤编号`),0) AS '日志步骤编号'";
		strSumSelect += ",'' AS '日志步骤名称','' AS '日志步骤说明'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from rep_job_log ";
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
		mappamSum.put(new String("pageSize"), "10");

		List<Object> dataListSum = new ArrayList<Object>();

		if (PageIndex == 1) {
			dataListSum = getQueryData(mappamSum, pageModelSum);
		}
		
		queryJobDetailMap.put(new String("listData"), dataListJob);
		queryJobDetailMap.put(new String("pageData"), pageModelList);
		queryJobDetailMap.put(new String("listDataSum"), dataListSum);
		queryJobDetailMap.put(new String("dataTX"), dataTX);
		
		return queryJobDetailMap;
	}
	public Map<String, List<Object>> queryPaymentKey(Map<String, String> map) {
		
		Map<String, List<Object>> queryPaymentKeyMap = new HashMap<String, List<Object>>();
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String PaymentTypeName = map.get("PaymentTypeName");
		/*
		 * SELECT paymentKey AS '支付方式编码', paymentKeyName AS '支付方式名称', paymentTypeKey AS '支付方式分类编码'
			,paymentTypeName AS '支付方式分类名称'
			 FROM rep_dict_paymentkey
		 */
		String strSelect = "";
		strSelect += "paymentKey AS '支付方式编码', paymentKeyName AS '支付方式名称', ";
		strSelect += "paymentTypeKey AS '支付方式分类编码',paymentTypeName AS '支付方式分类名称'";
		
		String sqlWhere = "";
		sqlWhere += " WHERE paymentKey IS NOT NULL";
		if(PaymentTypeName != ""){
			sqlWhere += " AND paymentTypeName = '" + PaymentTypeName + "'";
		}
		
		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "rep_dict_paymentkey");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		List<Object> dataListPayment = getQueryData(mappam, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		
		//SELECT paymentTypeName,COUNT(1) AS Num FROM rep_dict_paymentkey GROUP BY paymentTypeName
		//查询制作图形报表所需数据
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT paymentTypeName,COUNT(1) AS Num FROM rep_dict_paymentkey ";
		//下拉控件选择支付方式名称，默认选择全部支付方式名称，当选中指定支付方式名称时，sql语句中加入where过滤条件
		if(PaymentTypeName != ""){
			strSql += " WHERE paymentTypeName = '" + PaymentTypeName + "'";
			strSql += " GROUP BY paymentTypeName";
		}else{
			strSql += " GROUP BY paymentTypeName";
		}
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();
		
		queryPaymentKeyMap.put(new String("dataTXHOne"), dataTXHOne);
		queryPaymentKeyMap.put(new String("listData"), dataListPayment);
		queryPaymentKeyMap.put(new String("pageData"), pageModelList);
		return queryPaymentKeyMap;
	}
	public Map<String, List<Object>> queryRepjobstatistics(Map<String, String> map) {
		
		Map<String, List<Object>> queryRepjobstatistics = new HashMap<String, List<Object>>();
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		/*
		 * SELECT DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日期',COUNT(1) AS '次数'
			FROM rep_job_log  GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') 
			ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')
		 */
		String strSelect = "";
		strSelect += "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日期',COUNT(1) AS '次数'";
		String sqlWhere = "";
		sqlWhere += "GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		sqlWhere += "ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		
		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "rep_job_log");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		List<Object> dataListJob = getQueryData(mappam, pageModel);
		System.out.println(mappam);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		String strSumSelect = "";
		strSumSelect += "'合计' AS '日期'";
		strSumSelect += ",IFNULL(SUM(`次数`),0) AS '次数'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from rep_job_log ";
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
		mappamSum.put(new String("pageSize"), "10");

		List<Object> dataListSum = new ArrayList<Object>();

		if (PageIndex == 1) {
			dataListSum = getQueryData(mappamSum, pageModelSum);
		}
		
		/*
		 * 查询图形报表数据
		 */
		//select DATE_FORMAT(jobTime,'%Y-%m-%d'),COUNT(1) AS jobTimeNum from rep_job_log 
		//GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')
		
		String strSql = "";
		String totalSQL = "";
		
		strSql += " select DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime,COUNT(1) AS jobTimeNum from rep_job_log ";
		strSql += " GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		totalSQL += "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();
		
		queryRepjobstatistics.put(new String("dataTXHOne"), dataTXHOne);
		queryRepjobstatistics.put(new String("listData"), dataListJob);
		queryRepjobstatistics.put(new String("pageData"), pageModelList);
		queryRepjobstatistics.put(new String("listDataSum"), dataListSum);
		return queryRepjobstatistics;
	}
	/*
	 * 下钻方法
	 */
	public Map<String, List<Object>> queryDownGo(Map<String, String> map) {
		
		Map<String, List<Object>> queryDownGo = new HashMap<String, List<Object>>();
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobTime = map.get("jobTime");

		/*
		 * SELECT  repUUID AS '日志编码', jobTime AS '日志时间',jobStep AS '日志步骤编号', stepDo AS '日志步骤名称', stepDesc AS  '日志步骤说明'
		 * FROM   rep_job_log 
		   WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '2016-08-18'
		 */
		String strSelect = "";
		strSelect += "repUUID AS '日志编码', DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日志时间',jobStep AS '日志步骤编号', stepDo AS '日志步骤名称', stepDesc AS  '日志步骤说明'";
		String sqlWhere = "WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "'";
		
		
		/*
		 * 查询明细数据列表 带分页
		 */
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "rep_job_log");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		List<Object> dataListJob = getQueryData(mappam, pageModel);
		
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		/*
		 * 下钻时，通过传递的日期时间，查询出展示图形报表需要的数据源
		 * SELECT jobStep,COUNT(1) AS jobStepNum,DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime FROM rep_job_log 
		 * WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '2016-09-05' GROUP BY jobStep
		 */
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT jobStep,COUNT(1) AS jobStepNum,DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime FROM rep_job_log ";
		strSql += "WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "' GROUP BY jobStep";
		
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXH = page.getResultList();
		
		queryDownGo.put(new String("listData"), dataListJob);
		queryDownGo.put(new String("pageData"), pageModelList);
		queryDownGo.put(new String("dataTXH"), dataTXH);
		return queryDownGo;
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
