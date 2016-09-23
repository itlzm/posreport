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
	
	//��¼��־
	private static Logger logger = Logger.getLogger(StoreDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//��ҵ��־����
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map) {
		
		Map<String, List<Object>> queryRepjobDetailMap = new HashMap<String, List<Object>>();
		
		//ȡ��map�����е�ֵ
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		String jobStep = map.get("jobStep");
		//��ȡjobTimeֵ��������ʱʹ�á�
		String jobTime = map.get("jobTime");
		
		String strSelect = "repUUID AS '��־����',"
				+ "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '��־ʱ��',"
				+ "jobStep AS '��־������', stepDo AS '��־��������',"
				+ "stepDesc AS  '��־����˵��'";
		//��������
		String sqlWhere = " WHERE repUUID IS NOT NULL";
		//��jobStep��ΪALL����Ҫ��SQL�м����������
		//����ʱû�д���jobstep���жϲ�Ϊnull
		if(jobStep != null && !jobStep.equals("ALL")){
			sqlWhere += " AND jobStep = '" + jobStep + "'";
		}
		//����ʱ����jobTime��ȷʱ�䣬SQL�м������������
		if(jobTime != null && jobTime != ""){
			sqlWhere += " AND DATE_FORMAT(jobTime,'%Y-%m-%d') = '" + jobTime + "'";
		}else{
			sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
		}
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
		 * ��ѯ�ϼ�����ϸ���� listDataSum��sizeΪ1
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
		 * ��ѯͼ�λ�����չʾ������ϸ����
		 */
		String strSql = "SELECT jobStep,COUNT(1) AS jobStepNum FROM rep_job_log  ";
		//�ı��ѯ������ͼ�λ�������ʾָ��ִ�в�����Ϣ
		strSql += sqlWhere;
		strSql += " GROUP BY jobStep";
		String totalSQL = "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("��һ��ͼ��SQL:" + strSql);
		
		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXH = page.getResultList();
		
		queryRepjobDetailMap.put(new String("listData"), dataListJob);
		queryRepjobDetailMap.put(new String("pageData"), pageModelList);
		queryRepjobDetailMap.put(new String("listDataSum"), dataListSum);
		queryRepjobDetailMap.put(new String("dataTXH"), dataTXH);
		
		return queryRepjobDetailMap;
	}
	//��ҵ��־ͳ�Ʒ���
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map) {
		
		Map<String, List<Object>> queryRepjobStatisticsMap = new HashMap<String, List<Object>>();
		
		//ȡ��map�����е�ֵ
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String jobDateStart = map.get("jobDateStart");
		String jobDateEnd = map.get("jobDateEnd");
		
		String strSelect = "DATE_FORMAT(jobTime,'%Y-%m-%d') AS '����',COUNT(1) AS '����'";
		String sqlWhere = " WHERE 1 = 1";
		sqlWhere += " AND (jobTime >= '" + jobDateStart + "' AND jobTime <= '" + jobDateEnd + "')";
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
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
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
		 * ��ѯ�ϼ�����ϸ����
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
		
		String strSql = " select DATE_FORMAT(jobTime,'%Y-%m-%d') AS jobTime,COUNT(1) AS jobTimeNum from rep_job_log ";
		strSql += sqlWhere;
		String totalSQL = "select count(1) from (" + strSql + ") as Tab";
		
		logger.info("��һ��ͼ��SQL:" + strSql);

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
	 * ���ݲ�ѯ��䷵�ز�ѯ��Ϣ,ͨ�÷���
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
