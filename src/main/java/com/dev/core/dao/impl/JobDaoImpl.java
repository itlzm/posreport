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
		strSelect += "repUUID AS '��־����', DATE_FORMAT(jobTime,'%Y-%m-%d') AS '��־ʱ��',jobStep AS '��־������', stepDo AS '��־��������', stepDesc AS  '��־����˵��'";
		
		
		String sqlWhere = "";
		sqlWhere += " WHERE repUUID IS NOT NULL";
		if(jobStep != "" && jobStep != null){
			sqlWhere += " AND jobStep = '" + jobStep + "'";
		}
		
		sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
		
		
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		 * ��ѯ����ͼ�α�����������
		 * SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  GROUP BY jobStep
		 */
		
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  ";
		//�����ؼ�ѡ��ִ�в��裬Ĭ��ѡ��ȫ��ִ�в��裬��ѡ��ָ��ִ�в���ʱ��sql����м���where��������
		if(jobStep != "" && jobStep != null){
			strSql += " WHERE jobStep = '" + jobStep + "'";
			strSql += " GROUP BY jobStep";
		}else{
			strSql += " GROUP BY jobStep";
		}
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("��һ��ͼ��SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTX = page.getResultList();
		
		String strSumSelect = "";
		//repUUID AS '��־����', jobTime AS '��־ʱ��',jobStep AS '��־������', stepDo AS '��־��������', stepDesc AS  '��־����˵��'
		//strSumSelect += "'�ϼ�' AS '��־����','' AS '��־ʱ��','' AS '��־������','' AS '��־��������','' AS '��־����˵��'";
		strSumSelect += "'�ϼ�' AS '��־����','' AS '��־ʱ��'";
		strSumSelect += ",IFNULL(SUM(`��־������`),0) AS '��־������'";
		strSumSelect += ",'' AS '��־��������','' AS '��־����˵��'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from rep_job_log ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		logger.info("strFromTab:" + strFromTab);

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		 * SELECT paymentKey AS '֧����ʽ����', paymentKeyName AS '֧����ʽ����', paymentTypeKey AS '֧����ʽ�������'
			,paymentTypeName AS '֧����ʽ��������'
			 FROM rep_dict_paymentkey
		 */
		String strSelect = "";
		strSelect += "paymentKey AS '֧����ʽ����', paymentKeyName AS '֧����ʽ����', ";
		strSelect += "paymentTypeKey AS '֧����ʽ�������',paymentTypeName AS '֧����ʽ��������'";
		
		String sqlWhere = "";
		sqlWhere += " WHERE paymentKey IS NOT NULL";
		if(PaymentTypeName != ""){
			sqlWhere += " AND paymentTypeName = '" + PaymentTypeName + "'";
		}
		
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		//��ѯ����ͼ�α�����������
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT paymentTypeName,COUNT(1) AS Num FROM rep_dict_paymentkey ";
		//�����ؼ�ѡ��֧����ʽ���ƣ�Ĭ��ѡ��ȫ��֧����ʽ���ƣ���ѡ��ָ��֧����ʽ����ʱ��sql����м���where��������
		if(PaymentTypeName != ""){
			strSql += " WHERE paymentTypeName = '" + PaymentTypeName + "'";
			strSql += " GROUP BY paymentTypeName";
		}else{
			strSql += " GROUP BY paymentTypeName";
		}
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("��һ��ͼ��SQL:" + strSql);

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
		 * SELECT DATE_FORMAT(jobTime,'%Y-%m-%d') AS '����',COUNT(1) AS '����'
			FROM rep_job_log  GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') 
			ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')
		 */
		String strSelect = "";
		strSelect += "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '����',COUNT(1) AS '����'";
		String sqlWhere = "";
		sqlWhere += "GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		sqlWhere += "ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		strSumSelect += "'�ϼ�' AS '����'";
		strSumSelect += ",IFNULL(SUM(`����`),0) AS '����'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from rep_job_log ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		logger.info("strFromTab:" + strFromTab);

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		 * ��ѯͼ�α�������
		 */
		//select DATE_FORMAT(jobTime,'%Y-%m-%d'),COUNT(1) AS jobTimeNum from rep_job_log 
		//GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')
		
		String strSql = "";
		String totalSQL = "";
		
		strSql += " select DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime,COUNT(1) AS jobTimeNum from rep_job_log ";
		strSql += " GROUP BY DATE_FORMAT(jobTime,'%Y-%m-%d') ORDER BY DATE_FORMAT(jobTime,'%Y-%m-%d')";
		totalSQL += "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("��һ��ͼ��SQL:" + strSql);

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
	 * ���귽��
	 */
	public Map<String, List<Object>> queryDownGo(Map<String, String> map) {
		
		Map<String, List<Object>> queryDownGo = new HashMap<String, List<Object>>();
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobTime = map.get("jobTime");

		/*
		 * SELECT  repUUID AS '��־����', jobTime AS '��־ʱ��',jobStep AS '��־������', stepDo AS '��־��������', stepDesc AS  '��־����˵��'
		 * FROM   rep_job_log 
		   WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '2016-08-18'
		 */
		String strSelect = "";
		strSelect += "repUUID AS '��־����', DATE_FORMAT(jobTime,'%Y-%m-%d') AS '��־ʱ��',jobStep AS '��־������', stepDo AS '��־��������', stepDesc AS  '��־����˵��'";
		String sqlWhere = "WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "'";
		
		
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
		 * ����ʱ��ͨ�����ݵ�����ʱ�䣬��ѯ��չʾͼ�α�����Ҫ������Դ
		 * SELECT jobStep,COUNT(1) AS jobStepNum,DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime FROM rep_job_log 
		 * WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '2016-09-05' GROUP BY jobStep
		 */
		String strSql = "";
		String totalSQL = "";
		
		strSql += "SELECT jobStep,COUNT(1) AS jobStepNum,DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime FROM rep_job_log ";
		strSql += "WHERE DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "' GROUP BY jobStep";
		
		totalSQL += "select count(1) from (" + strSql + ") as Tab";	
		
		logger.info("��һ��ͼ��SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXH = page.getResultList();
		
		queryDownGo.put(new String("listData"), dataListJob);
		queryDownGo.put(new String("pageData"), pageModelList);
		queryDownGo.put(new String("dataTXH"), dataTXH);
		return queryDownGo;
	}
	
	
	
	/*
	 * ���ݲ�ѯ��䷵�ز�ѯ��Ϣ
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getQueryData(Map<String, String> map, PageModel pageModel) {

		String selectCol = map.get("selectCol");
		String tabName = map.get("tabName");
		String sqlWhere = map.get("sqlWhere");
		int pageIndex = Integer.parseInt(map.get("pageIndex"));
		int pageSize = Integer.parseInt(map.get("pageSize"));

		// ������
		String sql = "select " + selectCol + " from " + tabName;

		if (sqlWhere != "") {
			sql += " " + sqlWhere;
			// totalSQL += " " + sqlWhere;
		}

		// ��¼����
		String totalSQL = "select count(1) from (" + sql + ") as Tab";

		logger.info("sql:" + sql);
		logger.info("totalSQL:" + totalSQL);

		Pagination page = new Pagination(sql, pageIndex, pageSize, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
	}

}
