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
		strSelect += "companyCode AS '�̼ұ���',fullName AS '�̼�����',phone AS '�绰',contractAddress AS '��ַ',contractPerson AS '��ϵ��'";
		
		
		String sqlWhere = "";
		sqlWhere += " WHERE companyCode IS NOT NULL";
		if(contractPerson != "" && contractPerson != null){
			sqlWhere += " AND contractPerson in('" + contractPerson + "')";
		}
		if(fullName != "" && fullName != null){
			sqlWhere += " AND fullName in('" + fullName + "')";
		}
		
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
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
	
		strSumSelect += "'�ϼ�' AS '�̼ұ���','' AS '�̼�����','' AS '�绰','' AS '��ַ','' AS '��ϵ��'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from cos_merchant ";
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
		mappamSum.put(new String("pageSize"), "1");

		List<Object> dataListSum = getQueryData(mappamSum, pageModelSum);
		
		queryMerchantDetailMap.put(new String("listData"), dataListMerchant);
		queryMerchantDetailMap.put(new String("pageData"), pageModelList);
		queryMerchantDetailMap.put(new String("listDataSum"), dataListSum);
		
		return queryMerchantDetailMap;
	}
	
	public Map<String, List<Object>> queryMerchantDetail2(Map<String, String> map) {
		
		Map<String, List<Object>> queryMerchantDetailMap = new HashMap<String, List<Object>>();
		
		//ȡ��Controller���ø÷���ʱ������map�����е�ֵ
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String contractPerson = map.get("ContractPerson");
		String fullName = map.get("FullName");
		
		//ƴдSQL���,strSelect:�г���Ҫ��ѯ���ֶ�,sqlWhere:���˵�����
		String strSelect = "companyCode AS '�̼ұ���',"
				+ "fullName AS '�̼�����',"
				+ "phone AS '�绰',"
				+ "contractAddress AS '��ַ',"
				+ "contractPerson AS '��ϵ��'";
		String sqlWhere = " WHERE companyCode IS NOT NULL";
		//���û��������̼����ƻ���ϵ�ˣ���fullName��contractPerson��ֵ��Ϊ""ʱ����Ҫ��SQL����м��Ϲ�������
		//ʹ��like��ģ����ѯ��%����ʾ����������ַ�
		//���Ϸ�null�жϣ��������Excelʱ�������û����������
		if(fullName != "" && fullName != null){
			sqlWhere += " AND fullName like'%" + fullName + "%' ";
		}
		if(contractPerson != "" && contractPerson != null){
			sqlWhere += " AND contractPerson like'%" + contractPerson + "%' ";
		}
		
		//ִ��SQL��䣬��ѯ��ϸ���� ����ҳ
		PageModel pageModel = new PageModel();
		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "cos_merchant");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));
		
		//��ά�������ϸ����
		List<Object> dataListMerchant = getQueryData(mappam, pageModel);
		//��ҳ��Ϣ��ϸ����
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);
		
		//��¼��־
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());
		
		//ƴдSQL���,��ѯ���������һ�кϼ���Ϣ,ʹ��sum����,ʹdataListSum��sizeΪ1,����pageSizeֵӰ��
		String strSumSelect = "'�ϼ�' AS '�̼ұ���','' AS '�̼�����','' AS '�绰','' AS '��ַ'";
		strSumSelect += ",IFNULL(SUM(`��ϵ��`),0) AS '��ϵ��'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from cos_merchant ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		//��¼��־
		logger.info("strFromTab:" + strFromTab);
		
		//ִ��SQL��䣬��ά���������һ�У���ϼƵ�������Ϣ
		PageModel pageModelSum = new PageModel();

		Map<String, String> mappamSum = new HashMap<String, String>();
		mappamSum.put(new String("selectCol"), strSumSelect);
		mappamSum.put(new String("tabName"), strFromTab);
		mappamSum.put(new String("sqlWhere"), "");
		mappamSum.put(new String("pageIndex"), "1");
		mappamSum.put(new String("pageSize"), "10");

		//��ά���������һ�У���ϼƵ�������Ϣ
		List<Object> dataListSum = new ArrayList<Object>();
		if (PageIndex == 1) {
			dataListSum = getQueryData(mappamSum, pageModelSum);
		}
		
		//listData:js��ȡ��listData�󣬱����õ���ά�������ϸ���ݡ�
		//pageData:js��ȡ��pageData�󣬰󶨷�ҳ��Ϣ����[266]����¼ ��ǰ��[1]ҳ/��[18]ҳ
		//listDataSum:js��ȡ��listDataSum���䳤��Ϊ1���������ɶ�ά���<thead></thead>�������������һ�кϼ���Ϣ
		queryMerchantDetailMap.put(new String("listData"), dataListMerchant);
		queryMerchantDetailMap.put(new String("pageData"), pageModelList);
		queryMerchantDetailMap.put(new String("listDataSum"), dataListSum);
		
		return queryMerchantDetailMap;
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
