package com.dev.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.PersonDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;
@Repository
public class PersonDaoImpl implements PersonDao {
	
	//记录日志
	private static Logger logger = Logger.getLogger(StoreDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//作业日志分析
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map) {
		
		Map<String, List<Object>> queryRepjobDetailMap = new HashMap<String, List<Object>>();
		
		//取出map集合中的值
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		String jobStep = map.get("jobStep");
		//获取jobTime值，在下钻时使用。
		String jobTime = map.get("jobTime");
		
		String strSelect = "repUUID AS '日志编码',"
				+ "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日志时间',"
				+ "jobStep AS '日志步骤编号', stepDo AS '日志步骤名称',"
				+ "stepDesc AS  '日志步骤说明'";
		//过滤条件
		String sqlWhere = " WHERE repUUID IS NOT NULL";
		//若jobStep不为ALL，需要在SQL中加入过滤条件
		//下钻时没有传入jobstep，判断不为null
		if(jobStep != null && !jobStep.equals("ALL")){
			sqlWhere += " AND jobStep = '" + jobStep + "'";
		}
		//下钻时传入jobTime精确时间，SQL中加入过滤条件。
		if(jobTime != null && jobTime != ""){
			sqlWhere += " AND DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "'";
		}else{
			sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
		}
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
		 * 查询合计行明细数据 listDataSum的size为1
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
		 * 查询图形化报表展示所需明细数据
		 */
		String strSql = "SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  ";
		//改变查询条件，图形化报表显示指定执行步骤信息
		strSql += sqlWhere;
		strSql += " GROUP BY jobStep";
		String totalSQL = "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("第一张图标SQL:" + strSql);
		
		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXH = page.getResultList();
		
		queryRepjobDetailMap.put(new String("listData"), dataListJob);
		queryRepjobDetailMap.put(new String("pageData"), pageModelList);
		queryRepjobDetailMap.put(new String("listDataSum"), dataListSum);
		queryRepjobDetailMap.put(new String("dataTXH"), dataTXH);
		
		return queryRepjobDetailMap;
	}
	//作业日志统计分析
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map) {
		
		Map<String, List<Object>> queryRepjobStatisticsMap = new HashMap<String, List<Object>>();
		
		//取出map集合中的值
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		
		String strSelect = "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '日期',COUNT(1) AS '次数'";
		String sqlWhere = " WHERE 1 = 1";
		sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
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
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
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
		 * 查询合计行明细数据
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
		
		String strSql = " select DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime,COUNT(1) AS jobTimeNum from rep_job_log ";
		strSql += sqlWhere;
		String totalSQL = "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXH = page.getResultList();
		
		queryRepjobStatisticsMap.put(new String("listData"), dataListJob);
		queryRepjobStatisticsMap.put(new String("pageData"), pageModelList);
		queryRepjobStatisticsMap.put(new String("listDataSum"), dataListSum);
		queryRepjobStatisticsMap.put(new String("dataTXH"), dataTXH);
		
		return queryRepjobStatisticsMap;
	}
	
	/*
	 * 根据查询语句返回查询信息,通用方法
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
