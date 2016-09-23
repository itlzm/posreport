package com.dev.core.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.StoreDao;
import com.dev.core.model.ProductModel;

import com.dev.core.model.StorePayMModel;

import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;


@Repository
public class StoreDaoImpl implements StoreDao {

	private static Logger logger = Logger.getLogger(StoreDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * �����������ݼ���
	 * 
	 * @moduleName ģ�����
	 */
	

	@SuppressWarnings("unchecked")
	public List<Object> getSelectDropDownList(Map<String, String> map) {

		String selectCol = map.get("selectCol");
		String moduleName = map.get("moduleName");
		String sqlWhere = map.get("sqlWhere");

		// ������
		String sql = "select " + selectCol + " from " + moduleName;
		// ��¼����
		String totalSQL = "select count(1) from " + moduleName;

		if (sqlWhere != "") {
			sql += " " + sqlWhere;

			/*
			 * if (!sqlWhere.startsWith("GROUP")) { totalSQL += " " + sqlWhere;
			 * }
			 */
		}
		PageModel pageModel = new PageModel();
		Pagination page = new Pagination(sql, 1, 10000, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
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

	/*
	 * ֧����ʽ�ṹ����
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getStorePayMModel(Map<String, String> map) {

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");

		String sql = " SELECT paymentKeyName,SUM(paymentKeyCount) AS paymentKeyCount,SUM(subSamount) AS subSamount,SUM(subNetAmount) AS subNetAmount";
		sql += "  FROM vrep_sales_daily_payitem ";
		sql += " WHERE companyCode = '" + companyCode + "'";
		sql += "  AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			sql += " AND storeCode in('" + storeCode + "')";
		} else {
			sql += " AND storeCode in('" + storeCode + "')";
		}
		sql += " GROUP BY paymentKeyName ";

		logger.info(sql);

		return jdbcTemplate.query(sql, new StorePayMapper());
	}

	@SuppressWarnings("rawtypes")
	private static final class StorePayMapper implements RowMapper {

		
		public Object mapRow(java.sql.ResultSet rs, int arg1) throws SQLException {
			StorePayMModel storePayMModel = new StorePayMModel();
			storePayMModel.setPaymentKeyName(rs.getString("paymentKeyName"));
			storePayMModel.setPaymentKeyCount(rs.getString("paymentKeyCount"));
			storePayMModel.setSubSamount(rs.getString("subSamount"));
			storePayMModel.setSubNetAmount(rs.getString("subNetAmount"));
			return storePayMModel;
		}
	}

	/*
	 * ֧����ʽ
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getStorePayType(Map<String, String> map) {

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");

		String sql = " SELECT PayType,SUM(paymentKeyCount) AS paymentKeyCount,SUM(subSamount) AS subSamount,SUM(subNetAmount) AS subNetAmount";
		sql += "  FROM vrep_sales_daily_payitem ";
		sql += " WHERE companyCode = '" + companyCode + "'";
		sql += "  AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			sql += " AND storeCode in('" + storeCode + "')";
		} else {
			sql += " AND storeCode in('" + storeCode + "')";
		}
		sql += " GROUP BY PayType ";

		logger.info(sql);

		return jdbcTemplate.query(sql, new StorePayTypeMapper());
	}

	@SuppressWarnings("rawtypes")
	private static final class StorePayTypeMapper implements RowMapper {

		
		public Object mapRow(java.sql.ResultSet rs, int arg1) throws SQLException {
			StorePayMModel storePayMModel = new StorePayMModel();
			storePayMModel.setPayType(rs.getString("PayType"));
			storePayMModel.setPaymentKeyCount(rs.getString("paymentKeyCount"));
			storePayMModel.setSubSamount(rs.getString("subSamount"));
			return storePayMModel;
		}
	}

	/*
	 * ======�ŵ�֧����ʽ��������========================================================
	 */
	
	public Map<String, List<Object>> queryReppaymode(Map<String, String> map, List<Object> storePayMModelList,
			List<Object> storePayTypeList) {

		Map<String, List<Object>> queryReppaymodeMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));

		String strSelect = "";
		strSelect += "storeCodeName AS '�ŵ�',businessDate AS 'Ӫҵ����',storeTypeName AS '�ŵ�����'";
		for (int i = 0; i < storePayMModelList.size(); i++) {
			StorePayMModel storePayMModel = (StorePayMModel) storePayMModelList.get(i);
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
					+ "' THEN subVamount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>������'";
			
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
			+ "' THEN subNetAmount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>ʵ������'";
	
			
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
					+ "' THEN paymentKeyCount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>�������'";
		}
		strSelect += ",SUM(subSamount) AS '������ϼ�'";
		strSelect += ",SUM(commissionAmount) AS 'ƽ̨Ӷ��ϼ�' ";
		strSelect += ",SUM(businessFavourableAmount) AS '�̼ҳе��Żݽ��ϼ�'";
		strSelect += ",SUM(platformFavourableAmount) AS 'ƽ̨�е��Żݽ��ϼ�' ";
		strSelect += ",SUM(subNetAmount) AS 'ʵ������ϼ�'";
		
		// strSelect +=" FROM vrep_sales_daily_payitem";
		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			sqlWhere += " AND storeCode in('" + storeCode + "')";
		} else {
			sqlWhere += " AND storeCode in('" + storeCode + "')";
		}
		sqlWhere += " GROUP BY businessDate,storeCodeName";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel = new PageModel();

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "vrep_sales_daily_payitem");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> dataListChannel = getQueryData(mappam, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);

		String strSumSelect = "";
		strSumSelect += "'�ϼ�' AS '�ŵ�','' AS 'Ӫҵ����','' AS '�ŵ�����'";
		for (int i = 0; i < storePayMModelList.size(); i++) {
			StorePayMModel storePayMModel = (StorePayMModel) storePayMModelList.get(i);

			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>������`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>������' ";
			
			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>ʵ������`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>ʵ������' ";

			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>�������`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>�������' ";

		}
		strSumSelect += ",IFNULL(SUM(`������ϼ�`),0) AS ������ϼ�";
		strSumSelect += ",IFNULL(SUM(`ƽ̨Ӷ��ϼ�`),0) AS ƽ̨Ӷ��ϼ�";
		strSumSelect += ",IFNULL(SUM(`�̼ҳе��Żݽ��ϼ�`),0) AS '�̼ҳе��Żݽ��ϼ�'";
		strSumSelect += ",IFNULL(SUM(`ƽ̨�е��Żݽ��ϼ�`),0) AS ƽ̨�е��Żݽ��ϼ�";
		strSumSelect += ",IFNULL(SUM(`ʵ������ϼ�`),0) AS ʵ������ϼ�";
		
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_payitem ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

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

		queryReppaymodeMap.put(new String("storePay"), storePayMModelList);
		queryReppaymodeMap.put(new String("storePayType"), storePayTypeList);
		queryReppaymodeMap.put(new String("listData"), dataListChannel);
		queryReppaymodeMap.put(new String("pageData"), pageModelList);
		queryReppaymodeMap.put(new String("listDataSum"), dataListSum);

		return queryReppaymodeMap;
	}

	/*
	 * ======�ŵ�֧����ʽ��ϸ����========================================================
	 */
	
	public Map<String, List<Object>> queryReppayDetail(Map<String, String> map) {
		Map<String, List<Object>> queryReppayDetailMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String Paymentkey = map.get("Paymentkey");

		String strSelect = "";
		strSelect += "storeCodeName AS '�ŵ�',businessDate AS 'Ӫҵ����',IFNULL(storeTypeName,'') AS '�ŵ�����',IFNULL(orderNo,'') AS '��������',date_format(createDate,'%Y-%c-%d %H:%i:%s') AS '����ʱ��',IFNULL(paymentKeyName,'') AS '֧����ʽ'";
		strSelect += ",IFNULL(sAmount,0) AS '֧�����'";
		
		strSelect += ",IFNULL(commissionAmount, 0) AS 'ƽ̨Ӷ��'";
		strSelect += ",IFNULL(businessFavourableAmount, 0) AS '�̼ҳе��Żݽ��'";
		strSelect += ",IFNULL(platformFavourableAmount, 0) AS 'ƽ̨�е��Żݽ��'";
		strSelect += ",IFNULL(sAmountNet, 0) AS 'ʵ������'";
		
		strSelect += ",IFNULL(thirdOrderNo,'') AS '������֧������'";
		strSelect += ",IFNULL(thirdMerchantNo,'') AS '������֧���̻���'";
		strSelect += ",IFNULL(thirdPaymentUserAccount,'') AS '������֧���˿��˺�'";
		strSelect += ",IFNULL(employeeCode,'') AS '������Ա�˺�'";
		strSelect += ",IFNULL(employeeName,'') AS '������Ա����'";
		
		
		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			sqlWhere += " AND storeCode in('" + storeCode + "')";
		} else {
			sqlWhere += " AND storeCode in('" + storeCode + "')";
		}
		if (Paymentkey != "") {
			sqlWhere += " AND paymentKey = '" + Paymentkey + "'";
		}
		// sqlWhere += " GROUP BY
		// businessDate,storeCodeName,orderNo,paymentKeyName,createDate";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel = new PageModel();

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "vrep_sales_daily_payitem_detail");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> dataListChannel = getQueryData(mappam, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);

		String strSumSelect = "";
		strSumSelect += "'�ϼ�' AS '�ŵ�','' AS 'Ӫҵ����','' AS '�ŵ�����','' AS '��������','' AS '����ʱ��','' AS '֧����ʽ'";
		strSumSelect += ",IFNULL(SUM(`֧�����`),0) AS '֧�����'";
		strSumSelect += ",IFNULL(SUM(`ƽ̨Ӷ��`),0) AS 'ƽ̨Ӷ��'";
		strSumSelect += ",IFNULL(SUM(`�̼ҳе��Żݽ��`), 0) AS '�̼ҳе��Żݽ��'";
		strSumSelect += ",IFNULL(SUM(`ƽ̨�е��Żݽ��`), 0) AS 'ƽ̨�е��Żݽ��'";
		strSumSelect += ",IFNULL(SUM(`ʵ������`), 0) AS 'ʵ������'";
		strSumSelect += ",'' AS '������֧������'";
		strSumSelect += ",'' AS '������֧���̻���'";
		strSumSelect += ",'' AS '������֧���˿��˺�'";
		strSumSelect += ",'' AS '������Ա�˺�'";
		strSumSelect += ",'' AS '������Ա����'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_payitem_detail ";
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

		List<Object> dataListSum = getQueryData(mappamSum, pageModelSum);

		queryReppayDetailMap.put(new String("listData"), dataListChannel);
		queryReppayDetailMap.put(new String("pageData"), pageModelList);
		queryReppayDetailMap.put(new String("listDataSum"), dataListSum);

		return queryReppayDetailMap;
	}

	/*
	 * ======�ŵ�����Ӫ�շ�������========================================================
	 */
	
	@SuppressWarnings("unused")
	public Map<String, List<Object>> queryRepChannelSales(Map<String, String> map) {
		Map<String, List<Object>> queryRepChannelSalesMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		/*
		 * Բ��ͼ�λ�����
		 */
		String strSelect = "";
		strSelect += "A.channelName,IFNULL(SUM(A.subOrderCount),0) AS subOrderCount,IFNULL(SUM(A.subNetAmount),0) AS subNetAmount";

		String sqlWhere = "";
		sqlWhere += " LEFT OUTER JOIN rep_sales_daily_store_channel_cancel AS B";
		sqlWhere += " ON A.businessDate = B.businessDate";
		sqlWhere += " AND A.companyCode = B.companyCode";
		sqlWhere += " AND A.storeCode = B.storeCode";
		sqlWhere += " AND A.salesChannelKey = B.salesChannelKey";
		sqlWhere += " WHERE A.companyCode = '" + companyCode + "'";
		sqlWhere += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (ChannelCode != "") {
			sqlWhere += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}

		sqlWhere += " GROUP BY A.channelName";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel1 = new PageModel();

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(new String("selectCol"), strSelect);
		map1.put(new String("tabName"), "vrep_sales_daily_store_channel AS A");
		map1.put(new String("sqlWhere"), sqlWhere);
		map1.put(new String("pageIndex"), "1");
		map1.put(new String("pageSize"), "1000");
		List<Object> dataListChannel1 = getQueryData(map1, pageModel1);

		/*
		 * ��״ͼͼ�λ�����
		 */
		strSelect = "";
		strSelect += "A.channelName,IFNULL(SUM(A.subOrderCount),0) as subOrderCount,IFNULL(SUM(A.subOrderTotalAmount),0) as subOrderTotalAmount";
		strSelect += ",IFNULL(SUM(A.subDiscountAmount),0) as subDiscountAmount,IFNULL(SUM(A.subNetAmount),0) as subNetAmount ";
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmountPrice)/SUM(A.subOrderCount),2),0) as subJunPrice,IFNULL(SUM(B.subOrderCount),0) as cancelNum";
		strSelect += ",IFNULL(SUM(B.subNetAmountPrice),0) as cancelPrice";
		// vrep_sales_daily_store_channel AS A

		sqlWhere = "";
		sqlWhere += " LEFT OUTER JOIN rep_sales_daily_store_channel_cancel AS B";
		sqlWhere += " ON A.businessDate = B.businessDate";
		sqlWhere += " AND A.companyCode = B.companyCode";
		sqlWhere += " AND A.storeCode = B.storeCode";
		sqlWhere += " AND A.salesChannelKey = B.salesChannelKey";
		sqlWhere += " WHERE A.companyCode = '" + companyCode + "'";
		sqlWhere += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (ChannelCode != "") {
			sqlWhere += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}

		sqlWhere += " GROUP BY A.channelName";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel2 = new PageModel();

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(new String("selectCol"), strSelect);
		map2.put(new String("tabName"), "vrep_sales_daily_store_channel AS A");
		map2.put(new String("sqlWhere"), sqlWhere);
		map2.put(new String("pageIndex"), "1");
		map2.put(new String("pageSize"), "1000");
		List<Object> dataListChannel2 = getQueryData(map2, pageModel2);

		/*
		 * �����б�չʾ
		 */

		strSelect = "";
		strSelect += "A.storeCodeName AS '�ŵ�',A.channelName AS '����',A.businessDate AS 'Ӫҵ����',storeTypeName AS '�ŵ�����', IFNULL(SUM(A.subOrderCount),0) AS '��������'";
		strSelect += ",IFNULL(SUM(A.subOrderTotalAmount),0) AS '�����ܶ�',IFNULL(SUM(A.subDiscountAmount),0) AS '�����Żݽ��',IFNULL(SUM(A.subNetAmountPrice),0) AS '��������'";
		strSelect += ",IFNULL(A.commissionAmount, 0) AS 'ƽ̨Ӷ��'";
		strSelect += ",IFNULL(A.businessFavourableAmount, 0) AS '�̼ҳе��Żݽ��'";
		strSelect += ",IFNULL(A.platformFavourableAmount, 0) AS 'ƽ̨�е��Żݽ��'";
		strSelect += ",IFNULL(A.subNetAmount, 0) AS 'ʵ������'";

		
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmountPrice)/SUM(A.subOrderCount),2),0) AS '�����������'";
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmount) / SUM(A.subOrderCount),2),0) AS 'ʵ���������'";

		
		//strSelect += ",IFNULL(SUM(B.subOrderCount),0) AS 'ȡ������',IFNULL(SUM(B.subNetAmountPrice),0) AS 'ȡ�����'";
		// strSelect +=" FROM vrep_sales_daily_payitem";
		sqlWhere = "";
		sqlWhere += " LEFT OUTER JOIN rep_sales_daily_store_channel_cancel AS B";
		sqlWhere += " ON A.businessDate = B.businessDate";
		sqlWhere += " AND A.companyCode = B.companyCode";
		sqlWhere += " AND A.storeCode = B.storeCode";
		sqlWhere += " AND A.salesChannelKey = B.salesChannelKey";
		sqlWhere += " WHERE A.companyCode = '" + companyCode + "'";
		sqlWhere += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (ChannelCode != "") {
			sqlWhere += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		sqlWhere += " GROUP BY A.businessDate,A.storeCodeName,A.channelName";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel3 = new PageModel();

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put(new String("selectCol"), strSelect);
		map3.put(new String("tabName"), "vrep_sales_daily_store_channel AS A");
		map3.put(new String("sqlWhere"), sqlWhere);
		map3.put(new String("pageIndex"), String.valueOf(PageIndex));
		map3.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> dataListChannel3 = getQueryData(map3, pageModel3);
		logger.info("PageModel:" + pageModel3.getTotalPages() + "," + pageModel3.getTotalRows());
		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel3);

		/*
		 * �����б�ϼ���
		 */

		strSelect = "";
		strSelect += "'�ϼ�' AS '�ŵ�','' AS '����','' AS 'Ӫҵ����','' AS '�ŵ�����',IFNULL(SUM(A.subOrderCount),0) AS '��������'";
		strSelect += ",IFNULL(SUM(A.subOrderTotalAmount),0) AS '�����ܶ�',IFNULL(SUM(A.subDiscountAmount),0) AS '�����Żݽ��',IFNULL(SUM(A.subNetAmountPrice),0) AS '��������'";
		
		strSelect += ",IFNULL(SUM(A.commissionAmount), 0) AS 'ƽ̨Ӷ��'";
		strSelect += ",IFNULL(SUM(A.businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSelect += ",IFNULL(SUM(A.platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSelect += ",IFNULL(SUM(A.subNetAmount), 0) AS 'ʵ������'";

			
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmountPrice)/SUM(A.subOrderCount),2),0) AS '�����������'";
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmount) / SUM(A.subOrderCount),2),0) AS 'ʵ���������'";

		
		
		//strSelect += ",IFNULL(SUM(B.subOrderCount),0) AS 'ȡ������',IFNULL(SUM(B.subNetAmountPrice),0) AS 'ȡ�����'";
		// strSelect +=" FROM vrep_sales_daily_payitem";
		sqlWhere = "";
		sqlWhere += " LEFT OUTER JOIN rep_sales_daily_store_channel_cancel AS B";
		sqlWhere += " ON A.businessDate = B.businessDate";
		sqlWhere += " AND A.companyCode = B.companyCode";
		sqlWhere += " AND A.storeCode = B.storeCode";
		sqlWhere += " AND A.salesChannelKey = B.salesChannelKey";
		sqlWhere += " WHERE A.companyCode = '" + companyCode + "'";
		sqlWhere += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (ChannelCode != "") {
			sqlWhere += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel4 = new PageModel();

		Map<String, String> map4 = new HashMap<String, String>();
		map4.put(new String("selectCol"), strSelect);
		map4.put(new String("tabName"), "vrep_sales_daily_store_channel AS A");
		map4.put(new String("sqlWhere"), sqlWhere);
		map4.put(new String("pageIndex"), "1");
		map4.put(new String("pageSize"), "10");

		List<Object> dataListChannel4 = new ArrayList<Object>();

		if (PageIndex == 1) {
			dataListChannel4 = getQueryData(map4, pageModel4);
		}

		queryRepChannelSalesMap.put(new String("channel1"), dataListChannel1);
		queryRepChannelSalesMap.put(new String("channel2"), dataListChannel2);

		queryRepChannelSalesMap.put(new String("listData"), dataListChannel3);
		queryRepChannelSalesMap.put(new String("pageData"), pageModelList);

		queryRepChannelSalesMap.put(new String("channelSum"), dataListChannel4);

		return queryRepChannelSalesMap;

	}

	/*
	 * ======�ŵ��ʱ���۷�������========================================================
	 */
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepTimeSales(Map<String, String> map) {
		Map<String, List<Object>> queryRepTimeSalesMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BranCode = map.get("BranCode");

		// ��ѯӪҵʱ������
		String strSelect = "";
		strSelect += " bizHourDesc";

		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		// Ʒ��
		if (BranCode != "") {
			BranCode = BranCode.replace("|", "','");
			sqlWhere += " AND brandCode in('" + BranCode + "')";
		}

		sqlWhere += " GROUP BY bizHourDesc ORDER BY sortOrder";

		Map<String, String> bizHourDescMap = new HashMap<String, String>();
		bizHourDescMap.put(new String("selectCol"), strSelect);
		bizHourDescMap.put(new String("tabName"), "rep_dict_business_hour");
		bizHourDescMap.put(new String("sqlWhere"), sqlWhere);
		bizHourDescMap.put(new String("pageIndex"), "1");
		bizHourDescMap.put(new String("pageSize"), "100");
		PageModel bizHourDescPage = new PageModel();
		List<Object> bizHourDescList = getQueryData(bizHourDescMap, bizHourDescPage);

		strSelect = "";

		strSelect += " bizHour ";
		strSelect += "       ,IFNULL(SUM(subOrderCount),0) AS tot_SubOrderCount ";
		strSelect += "       ,IFNULL(SUM(subNetAmountPrice),0) AS tot_SubNetAmountPrice ";
		strSelect += "       ,IFNULL(ROUND(SUM(subNetAmountPrice)/SUM(subOrderCount),2),0) AS TA ";

		String strFormTab = "";

		strFormTab += " ( ";
		strFormTab += " SELECT A.businessDate ";
		strFormTab += "       ,A.companyCode ";
		strFormTab += "       ,A.brandcode ";
		strFormTab += "       ,A.storeCode ";
		strFormTab += "       ,A.bizHour ";
		strFormTab += "       ,CASE WHEN A.bizHour = 'Ӫҵʱ��' THEN 1 ";
		strFormTab += "             WHEN A.bizHour = '����ʱ��' THEN 999  ";
		strFormTab += "             ELSE B.sortOrder ";
		strFormTab += "             END AS sortOrder ";
		strFormTab += "       ,A.subOrderCount ";
		strFormTab += "       ,A.subNetAmountPrice ";
		strFormTab += "   FROM vrep_sales_daily_store_business_hour AS A  ";
		strFormTab += " LEFT OUTER JOIN rep_dict_business_hour AS B  ";
		strFormTab += " ON A.companyCode = B.companyCode  ";
		strFormTab += " AND A.brandcode = B.brandCode  ";
		strFormTab += " AND A.bizHour = B.bizHourDesc ";
		strFormTab += " ) AS M ";

		sqlWhere = "";
		sqlWhere += " WHERE M.companyCode = '" + companyCode + "' ";
		sqlWhere += "   AND (M.businessDate >='" + businessDateStart + "' AND M.businessDate <='" + businessDateEnd
				+ "') ";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND M.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND M.storeCode in('" + StoreCode + "')";
		}

		sqlWhere += " GROUP BY M.bizHour ";
		sqlWhere += " ORDER BY M.sortOrder ";

		Map<String, String> listDataTXMap = new HashMap<String, String>();
		listDataTXMap.put(new String("selectCol"), strSelect);
		listDataTXMap.put(new String("tabName"), strFormTab);
		listDataTXMap.put(new String("sqlWhere"), sqlWhere);
		listDataTXMap.put(new String("pageIndex"), "1");
		listDataTXMap.put(new String("pageSize"), "1000");

		PageModel txPage = new PageModel();

		List<Object> listTxData = getQueryData(listDataTXMap, txPage);

		logger.info("QuerySQLList=" + "select " + strSelect + " from " + strFormTab + sqlWhere);

		// �б����ݲ�ѯ
		strSelect = "";
		strSelect += " storeCodeName AS '�ŵ�',businessDate AS 'Ӫҵ����',storeTypeName AS '�ŵ�����' ";
		if (bizHourDescList.size() == 0) {
			strSelect += ",MAX(CASE bizHour WHEN 'Ӫҵʱ��' THEN subOrderCount ELSE 0 END ) AS 'Ӫҵʱ��<br>������'";
			strSelect += ",MAX(CASE bizHour WHEN 'Ӫҵʱ��' THEN subNetAmountPrice ELSE 0 END ) AS 'Ӫҵʱ��<br>ʵ������'";
			strSelect += ",MAX(CASE bizHour WHEN 'Ӫҵʱ��' THEN TA ELSE 0 END ) AS 'Ӫҵʱ��<br>�͵���'";
		} else {

			for (int i = 0; i < bizHourDescList.size(); i++) {
				Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc")
						+ "' THEN subOrderCount ELSE 0 END ) AS '" + hourDescMap.get("bizHourDesc") + "<br>������'";
				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc")
						+ "' THEN subNetAmountPrice ELSE 0 END ) AS '" + hourDescMap.get("bizHourDesc") + "<br>ʵ������'";
				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc") + "' THEN TA ELSE 0 END ) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>�͵���'";
			}

			strSelect += ",MAX(CASE bizHour WHEN '����ʱ��' THEN subOrderCount ELSE 0 END ) AS '����ʱ��<br>������'";
			strSelect += ",MAX(CASE bizHour WHEN '����ʱ��' THEN subNetAmountPrice ELSE 0 END ) AS '����ʱ��<br>ʵ������'";
			strSelect += ",MAX(CASE bizHour WHEN '����ʱ��' THEN TA ELSE 0 END ) AS '����ʱ��<br>�͵���'";
		}

		strSelect += ",SUM(subOrderCount) AS '�ϼ�<br>����'";
		strSelect += ",SUM(subNetAmountPrice) AS '�ϼ�<br>ʵ������'";

		sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		}

		sqlWhere += " GROUP BY businessDate,storeCodeName";
		sqlWhere += " ORDER BY businessDate,storeCodeName";

		logger.info("QuerySQL=" + "select " + strSelect + " from vrep_sales_daily_store_business_hour " + sqlWhere);

		Map<String, String> listDataMap = new HashMap<String, String>();
		listDataMap.put(new String("selectCol"), strSelect);
		listDataMap.put(new String("tabName"), "vrep_sales_daily_store_business_hour");
		listDataMap.put(new String("sqlWhere"), sqlWhere);
		listDataMap.put(new String("pageIndex"), String.valueOf(PageIndex));
		listDataMap.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		List<Object> listData = getQueryData(listDataMap, listDataPage);
		pageModelList.add(listDataPage);

		String strSumSelect = "";

		strSumSelect += " '�ϼ�' AS '�ŵ�','' AS 'Ӫҵ����','' AS '�ŵ�����'";
		if (bizHourDescList.size() == 0) {
			strSumSelect += ",IFNULL(sum(`Ӫҵʱ��<br>������`),0) AS 'Ӫҵʱ��<br>������' ";
			strSumSelect += ",IFNULL(sum(`Ӫҵʱ��<br>ʵ������`),0) AS 'Ӫҵʱ��<br>ʵ������'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`Ӫҵʱ��<br>ʵ������`),0)/IFNULL(sum(`Ӫҵʱ��<br>������`),0),2),0) AS 'Ӫҵʱ��<br>�͵���'";
		} else {

			for (int i = 0; i < bizHourDescList.size(); i++) {
				Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

				/*
				 * strSumSelect += ",IFNULL(sum(`" +
				 * hourDescMap.get("bizHourDesc") + "<br>������`),0) AS '" +
				 * hourDescMap.get("bizHourDesc") + "<br>������' "; strSumSelect +=
				 * ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") +
				 * "<br>Ӫҵ��`),0) AS '" + hourDescMap.get("bizHourDesc") +
				 * "<br>Ӫҵ��'"; strSumSelect += ",IFNULL(sum(`" +
				 * hourDescMap.get("bizHourDesc") + "<br>�͵���`),0) AS '" +
				 * hourDescMap.get("bizHourDesc") + "<br>�͵���'";
				 */

				strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>������`),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>������' ";
				strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>Ӫҵ��`),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>ʵ������'";
				strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`" + hourDescMap.get("bizHourDesc")
						+ "<br>Ӫҵ��`),0)/IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>������`),0),2),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>�͵���'";
			}

			strSumSelect += ",IFNULL(sum(`����ʱ��<br>������`),0) AS '����ʱ��<br>������'";
			strSumSelect += ",IFNULL(sum(`����ʱ��<br>ʵ������`),0) AS '����ʱ��<br>ʵ������'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`����ʱ��<br>ʵ������`),0)/IFNULL(sum(`����ʱ��<br>������`),0),2),0) AS '����ʱ��<br>�͵���'";

		}

		strSumSelect += ",IFNULL(sum(`�ϼ�<br>����`),0) AS '�ϼ�<br>����' ";
		strSumSelect += ",IFNULL(sum(`�ϼ�<br>ʵ������`),0) AS '�ϼ�<br>ʵ������' ";

		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_store_business_hour ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		logger.info("sumSQL=" + "select " + strSumSelect + " from " + strFromTab);

		Map<String, String> listDataSumMap = new HashMap<String, String>();
		listDataSumMap.put(new String("selectCol"), strSumSelect);
		listDataSumMap.put(new String("tabName"), strFromTab);
		listDataSumMap.put(new String("sqlWhere"), "");
		listDataSumMap.put(new String("pageIndex"), "1");
		listDataSumMap.put(new String("pageSize"), "10");

		PageModel listDataSumPage = new PageModel();

		List<Object> listDataSum = new ArrayList<Object>();
		if (PageIndex == 1) {
			listDataSum = getQueryData(listDataSumMap, listDataSumPage);
		}

		queryRepTimeSalesMap.put(new String("listTxData"), listTxData);
		queryRepTimeSalesMap.put(new String("listData"), listData);
		queryRepTimeSalesMap.put(new String("pageData"), pageModelList);
		queryRepTimeSalesMap.put(new String("listDataSum"), listDataSum);

		return queryRepTimeSalesMap;
	}

	/*
	 * ======�ŵ����۷�������========================================================
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> queryRepActiveSales(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		String favourableName = map.get("favourableName");

		String strSql = "";
		String totalSQL = "";
		// ��ͼ��Ȧ����
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}

		// �Żݻ
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}
		strSql += " GROUP BY C.orderTypeDesc";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageBT = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageBT);
		List<Object> BTNData = page.getResultList();

		// ��ͼ��Ȧ����״ͼ����
		strSql = "";
		strSql += "SELECT D.channelName AS 'channelName'";
		strSql += " ,SUM(OrderCount) AS 'OrderCount'";
		strSql += " ,SUM(sOrderTotalAmount) AS 'sOrderTotalAmount'";
		strSql += " ,SUM(subNetDiscount ) AS 'subNetDiscount'";
		strSql += " ,SUM(subNetAmount ) AS 'subNetAmount'";
		strSql += " ,ROUND(SUM(subNetAmount)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// �Żݻ
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}
		strSql += " GROUP BY D.channelName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("strSql:"+strSql);
		
		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		// �Żݻ����Ա�ͼ
		strSql = "";
		totalSQL = "";
		strSql += "SELECT A.favourableName AS 'favourableName'";
		strSql += ",SUM(OrderCount) AS 'OrderCount'";
		strSql += ",SUM(sOrderTotalAmount) AS 'sOrderTotalAmount'";
		strSql += ",-1*SUM(subNetDiscount) AS 'subNetDiscount'";
		strSql += ",SUM(subNetAmount) AS 'subNetAmount'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// �Żݻ
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}
		strSql += " GROUP BY A.favourableName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageDBT = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageDBT);
		List<Object> DBTData = page.getResultList();

		strSql = "";
		totalSQL = "";

		strSql += " SELECT B.storeCodeName AS '�ŵ�'";
		strSql += " ,D.channelName AS '��������' ";
		strSql += " ,A.businessDate AS 'Ӫҵ����'  ";
		strSql += " ,B.storeTypeName AS '�ŵ�����'  ";
		strSql += " ,orderTypeDesc AS '������ʽ' ";
		strSql += " ,favourableName AS '�Ż�����' ";
		strSql += " ,SUM(OrderCount) AS 'ʹ�ô���' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '��������̯��' ";
		strSql += " ,SUM(sDiscount) AS '�Żݽ��' ";
		
		strSql += ",SUM(sNetAmountPrice) AS '�������루��̯��'";
		strSql += ",SUM(sCommissionAmount) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += ",SUM(businessFavourableAmount) AS '�̼ҳе��Żݽ��'";
		strSql += ",SUM(platformFavourableAmount) AS 'ƽ̨�е��Żݽ��'";
		strSql += ",SUM(subNetAmount) AS 'ʵ�����루��̯��'";
		strSql += ",ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2) AS '���ζ������루��̯��'";
		strSql += ",ROUND(SUM(subNetAmount) / SUM(OrderCount),2) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// �Żݻ
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}

		strSql += " GROUP BY A.businessDate ";
		strSql += " ,B.storeCodeName ";
		strSql += " ,orderTypeDesc ";
		strSql += " ,orderTypeDesc ";
		strSql += " ,D.channelName";
		strSql += " ,favourableName";

		logger.info("strSql"+strSql);
		
		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += " SELECT '�ϼ�' AS '�ŵ�' ";
		strSql += " ,'' AS '��������' ";
		strSql += " ,'' AS 'Ӫҵ����' ";
		strSql += " ,'' AS '�ŵ�����' ";
		strSql += " ,'' AS '������ʽ' ";
		strSql += " ,'' AS '�Ż�����' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += ",IFNULL(SUM(sNetAmountPrice),0) AS '�������루��̯��'";
		strSql += ",IFNULL(SUM(sCommissionAmount),0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += ",IFNULL(SUM(businessFavourableAmount),0) AS '�̼ҳе��Żݽ��'";
		strSql += ",IFNULL(SUM(platformFavourableAmount),0) AS 'ƽ̨�е��Żݽ��'";
		strSql += ",IFNULL(SUM(subNetAmount),0) AS 'ʵ�����루��̯��'";
		strSql += ",IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += ",IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";
		
		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}

		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// �Żݻ
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}

		logger.info(strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("BTNData"), BTNData);
		queryDataMap.put(new String("ZZTData"), ZZTData);
		queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ��Ʒ���۷�������========================================================
	 */
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, List<Object>> queryRepProductSales(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandName = map.get("BrandName");

		String OrderByCol = map.get("OrderByCol");
		String OrderBySX = map.get("OrderBySX");
		String TopNum = map.get("TopNum");

		String strSql = "";
		String totalSQL = "";

		ProductModel productModel = null;
		strSql = "SELECT SUM(subNetAmount) AS 'subNetAmount' FROM vrep_sales_item_detail AS A  ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		logger.info("subNetAmountStrSql" + strSql);
		String netAmountPrice = "";
		try {
			productModel = jdbcTemplate.queryForObject(strSql, BeanPropertyRowMapper.newInstance(ProductModel.class));
		} catch (Exception ex) {
			productModel = null;
		}

		logger.info("productModel.getSubNetAmount():" + productModel.getSubNetAmount());

		if (productModel != null) {
			if (productModel.getSubNetAmount() != "" && productModel.getSubNetAmount() != null) {
				if (Float.parseFloat(productModel.getSubNetAmount()) == 0) {
					netAmountPrice = "1";
				} else {
					netAmountPrice = productModel.getSubNetAmount();
				}
			} else {
				netAmountPrice = productModel.getSubNetAmount();
			}
		} else {
			netAmountPrice = "1";
		}

		strSql = "";
		strSql += " SELECT * FROM (";
		strSql += " SELECT ";
		strSql += " IFNULL(productName,'') AS '��Ʒ����' ";
		strSql += " ,IFNULL(serviceTypeName, '') AS '����'";
		strSql += " ,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += " ,IFNULL(SUM(orderCount),0)  AS '��������' ";
		strSql += " ,IFNULL(SUM(amountPrice),0) AS '���۽��'";
		strSql += " ,IFNULL(SUM(netAmountPrice),0) AS 'ʵ�ʽ��' ";
		
		strSql += " ,IFNULL(SUM(sDiscount), 0) AS 'ʵ�ʶ����Żݣ���̯��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ������'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(orderCount),2),'--') AS 'ƽ��ÿ��<br>ʵ������'";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND(SUM(subNetAmount) / "+netAmountPrice+",4) * 100,2),CHAR (100)),'%'),'--') AS 'ʵ������ռ��'";

		
		strSql += "  FROM vrep_sales_item_detail AS A ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		// �ŵ�
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		strSql += " GROUP BY ProductName ";

		if (OrderByCol != "") {
			strSql += " ORDER BY SUM(" + OrderByCol + ") ";

			if (OrderBySX != "") {
				strSql += " " + OrderBySX;
			}
		}

		if (TopNum != "") {
			strSql += " limit 0," + TopNum;
		}

		strSql += " ) tabMain ";

		totalSQL = "";

		logger.info("SQL:" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += " SELECT '�ϼ�' AS '��Ʒ����' ";
		strSql += " ,'' AS '����'";
		strSql += " ,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += " ,IFNULL(SUM(orderCount),0) AS '��������' ";
		strSql += " ,IFNULL(SUM(amountPrice),0) AS '���۽��'";
		strSql += " ,IFNULL(SUM(netAmountPrice),0) AS 'ʵ�ʽ��' ";
		
		strSql += " ,IFNULL(SUM(sDiscount), 0) AS 'ʵ�ʶ����Żݣ���̯��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ������'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(orderCount),2),'--') AS 'ƽ��ÿ��<br>ʵ������'";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND(SUM(subNetAmount) / "+netAmountPrice+",4) * 100,2),CHAR (100)),'%'),'--') AS 'ʵ������ռ��'";

		strSql += "  FROM vrep_sales_item_detail AS A ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		// �ŵ�
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		logger.info(strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ���Сʱ���۷�������======================================================
	 */
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepHalfTimeSales(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BranCode = map.get("BranCode");

		// ��ѯӪҵʱ������
		String strSelect = "";
		strSelect += "  bizHourTime";

		String sqlWhere = "";

		sqlWhere += " WHERE companyCode = '" + companyCode + "' ";
		sqlWhere += "   AND (businessDate >='" + businessDateStart + "' AND businessDate <='" + businessDateEnd + "') ";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		}

		sqlWhere += " GROUP BY bizHourTime ORDER BY bizHourTime";

		Map<String, String> bizHourDescMap = new HashMap<String, String>();
		bizHourDescMap.put(new String("selectCol"), strSelect);
		bizHourDescMap.put(new String("tabName"), "vrep_sales_daily_store_business_hourtime");
		bizHourDescMap.put(new String("sqlWhere"), sqlWhere);
		bizHourDescMap.put(new String("pageIndex"), "1");
		bizHourDescMap.put(new String("pageSize"), "1000");
		PageModel bizHourDescPage = new PageModel();
		List<Object> bizHourDescList = getQueryData(bizHourDescMap, bizHourDescPage);

		strSelect = "";

		strSelect += " bizHourTime ";
		strSelect += "       ,SUM(subOrderCount) AS tot_SubOrderCount ";
		strSelect += "       ,SUM(subNetAmountPrice) AS tot_SubNetAmountPrice ";
		strSelect += "       ,ROUND(SUM(subNetAmountPrice)/SUM(subOrderCount),2) AS TA ";

		String strFormTab = "";

		strFormTab += " ( ";
		strFormTab += " SELECT A.businessDate ";
		strFormTab += "       ,A.companyCode ";
		strFormTab += "       ,A.brandcode ";
		strFormTab += "       ,A.storeCode ";
		strFormTab += "       ,A.bizHourTime ";
		strFormTab += "       ,A.subOrderCount ";
		strFormTab += "       ,A.subNetAmountPrice ";
		strFormTab += "   FROM vrep_sales_daily_store_business_hourtime AS A  ";
		strFormTab += " ) AS M ";

		sqlWhere = "";
		sqlWhere += " WHERE M.companyCode = '" + companyCode + "' ";
		sqlWhere += "   AND (M.businessDate >='" + businessDateStart + "' AND M.businessDate <='" + businessDateEnd
				+ "') ";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND M.storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND M.storeCode in('" + StoreCode + "')";
		}

		sqlWhere += " GROUP BY M.bizHourTime ";
		sqlWhere += " ORDER BY M.bizHourTime ";

		Map<String, String> listDataTXMap = new HashMap<String, String>();
		listDataTXMap.put(new String("selectCol"), strSelect);
		listDataTXMap.put(new String("tabName"), strFormTab);
		listDataTXMap.put(new String("sqlWhere"), sqlWhere);
		listDataTXMap.put(new String("pageIndex"), "1");
		listDataTXMap.put(new String("pageSize"), "1000");

		PageModel txPage = new PageModel();

		List<Object> listTxData = getQueryData(listDataTXMap, txPage);

		logger.info("QuerySQLList=" + "select " + strSelect + " from " + strFormTab + sqlWhere);

		// �б����ݲ�ѯ
		strSelect = "";
		strSelect += " storeCodeName AS '�ŵ�',businessDate AS 'Ӫҵ����',storeTypeName AS '�ŵ�����' ";

		for (int i = 0; i < bizHourDescList.size(); i++) {
			Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime")
					+ "' THEN subOrderCount ELSE 0 END ) AS '" + hourDescMap.get("bizHourTime") + "<br>������'";
			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime")
					+ "' THEN subNetAmountPrice ELSE 0 END ) AS '" + hourDescMap.get("bizHourTime") + "<br>ʵ������'";
			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime") + "' THEN TA ELSE 0 END ) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>�͵���'";
		}

		strSelect += ",SUM(subOrderCount) AS '�ϼ�<br>������'";
		strSelect += ",SUM(subNetAmountPrice) AS '�ϼ�<br>ʵ������'";

		sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		} else {
			sqlWhere += " AND storeCode in('" + StoreCode + "')";
		}

		sqlWhere += " GROUP BY businessDate,storeCodeName";
		sqlWhere += " ORDER BY businessDate,storeCodeName";

		logger.info("QuerySQL=" + "select " + strSelect + " from vrep_sales_daily_store_business_hourtime " + sqlWhere);

		Map<String, String> listDataMap = new HashMap<String, String>();
		listDataMap.put(new String("selectCol"), strSelect);
		listDataMap.put(new String("tabName"), "vrep_sales_daily_store_business_hourtime");
		listDataMap.put(new String("sqlWhere"), sqlWhere);
		listDataMap.put(new String("pageIndex"), String.valueOf(PageIndex));
		listDataMap.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		List<Object> listData = getQueryData(listDataMap, listDataPage);
		pageModelList.add(listDataPage);

		String strSumSelect = "";

		strSumSelect += " '�ϼ�' AS '�ŵ�','' AS 'Ӫҵ����','' AS '�ŵ�����'";

		for (int i = 0; i < bizHourDescList.size(); i++) {
			Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

			strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>������`),0) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>������' ";
			strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>ʵ������`),0) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>ʵ������'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>ʵ������`),0)/IFNULL(sum(`"
					+ hourDescMap.get("bizHourTime") + "<br>������`),0),2),0) AS '" + hourDescMap.get("bizHourTime")
					+ "<br>�͵���'";
		}

		strSumSelect += ",IFNULL(sum(`�ϼ�<br>������`),0) AS '�ϼ�<br>������' ";
		strSumSelect += ",IFNULL(sum(`�ϼ�<br>ʵ������`),0) AS '�ϼ�<br>ʵ������' ";

		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_store_business_hourtime ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

		logger.info("sumSQL=" + "select " + strSumSelect + " from " + strFromTab);

		Map<String, String> listDataSumMap = new HashMap<String, String>();
		listDataSumMap.put(new String("selectCol"), strSumSelect);
		listDataSumMap.put(new String("tabName"), strFromTab);
		listDataSumMap.put(new String("sqlWhere"), "");
		listDataSumMap.put(new String("pageIndex"), "1");
		listDataSumMap.put(new String("pageSize"), "10");

		PageModel listDataSumPage = new PageModel();

		List<Object> listDataSum = new ArrayList<Object>();
		if (PageIndex == 1) {
			listDataSum = getQueryData(listDataSumMap, listDataSumPage);
		}

		queryDataMap.put(new String("listTxData"), listTxData);
		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ꽻����ˮ��ϸ����========================================================
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> queryRepSalesDetail(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		String BrandCode = map.get("BrandCode");
		String IsExportExcel = map.get("IsExportExcel");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));

		String strSql = "";
		String totalSQL = "";

		strSql += "SELECT  ";
		strSql += "      IFNULL(brandCodeName,'') AS 'Ʒ��' ";
		strSql += "			,IFNULL(storeCodeName,'') AS '�ŵ�' ";
		strSql += "      ,IFNULL(storeTypeName,'') AS '�ŵ�����' ";
		strSql += "      ,IFNULL(channelName,'') AS '����' ";
		strSql += "			,IFNULL(businessDate,'') AS 'Ӫҵ����' ";

		if (IsExportExcel.equals("Y")) {
			strSql += ",IFNULL(orderNo,'') AS '��������' ";
		} else {
			strSql += ",IFNULL(CONCAT(orderCode,'|=|', orderNo),'') AS '��������' ";
		}

		strSql += "			,IFNULL(description,'') AS '������ע' ";
		strSql += "      ,IFNULL(thirdOrderNo,'') AS '���������׵���' ";
		strSql += "			,IFNULL(orderPeopleNum,'') AS '��������' ";
		strSql += "			,IFNULL(orderTotalAmount,'') AS '�������' ";
		strSql += "			,IFNULL(DiscountAmount,'') AS '�Żݽ��' ";
		
		strSql += "			,IFNULL(netAmountPrice, '') AS '��������'";
		strSql += "			,IFNULL(commissionAmount, '') AS 'ƽ̨Ӷ��'";
		strSql += "			,IFNULL(businessFavourableAmount, '') AS '�̼ҳе��Żݽ��'";
		strSql += "			,IFNULL(platformFavourableAmount, '') AS 'ƽ̨�е��Żݽ��'";
		strSql += "			,IFNULL(subNetAmount, '') AS 'ʵ������'";

		
		strSql += "			,date_format(orderDate,'%Y-%c-%d %H:%i:%s') AS '����ʱ��' ";
		strSql += "			,IFNULL(invoiceHeader,'') AS '��Ʊ̧ͷ' ";
		strSql += "			,IFNULL(orderTypeDesc,'') AS '��������' ";
		strSql += "			,IFNULL(orderStatusDesc,'') AS '����״̬' ";
		//strSql += "			,IFNULL(employName,'') AS '������Ա' ";
		strSql += " FROM vrep_sales_order_header ";

		strSql += "  WHERE companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT  ";
		strSql += "      '�ϼ�' AS 'Ʒ��' ";
		strSql += "			,'' AS '�ŵ�' ";
		strSql += "      ,'' AS '�ŵ�����' ";
		strSql += "      ,'' AS '����' ";
		strSql += "			,'' AS 'Ӫҵ����' ";
		strSql += "			,'' AS '��������' ";
		strSql += "			,'' AS '������ע' ";
		strSql += "      ,'' AS '���������׵���' ";
		strSql += "			,IFNULL(SUM( orderPeopleNum),0) AS '��������' ";
		strSql += "			,IFNULL(SUM(orderTotalAmount),0) AS '�������' ";
		strSql += "			,IFNULL(SUM(DiscountAmount),0) AS '�Żݽ��' ";
		
		strSql += "			,IFNULL(SUM(netAmountPrice), 0) AS '��������'";
		strSql += "			,IFNULL(SUM(commissionAmount), 0) AS 'ƽ̨Ӷ��'";
		strSql += "			,IFNULL(SUM(businessFavourableAmount),0) AS '�̼ҳе��Żݽ��'";
		strSql += "			,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += "			,IFNULL(SUM(subNetAmount), 0) AS 'ʵ������'";
		
		
		
		strSql += "			,'' AS '����ʱ��' ";
		strSql += "			,'' AS '��Ʊ̧ͷ' ";
		strSql += "			,'' AS '��������' ";
		strSql += "			,'' AS '����״̬' ";
		//strSql += "			,'' AS '������Ա' ";
		strSql += " FROM vrep_sales_order_header ";

		strSql += "  WHERE companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ꽻����ˮ������ϸ��ϸ======================================================
	 */
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepSalesDetailOrderDetail(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		String BrandCode = map.get("BrandCode");
		String OrderCode = map.get("OrderCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String strSql = "";
		String totalSQL = "";

		// ������ͷ
		strSql = "";
		strSql += "SELECT  ";
		strSql += "      IFNULL(brandCodeName,'') AS brandCodeName";
		strSql += "			,IFNULL(storeCodeName,'') AS storeCodeName ";
		strSql += "      ,IFNULL(storeTypeName,'') AS storeTypeName ";
		strSql += "      ,IFNULL(channelName,'') AS channelName ";
		strSql += "			,IFNULL(businessDate,'') AS businessDate";
		strSql += ", IFNULL(orderNo,'') AS orderNo ";
		strSql += "			,IFNULL(description,'') AS description ";
		strSql += "      ,IFNULL(thirdOrderNo,'') AS thirdOrderNo";
		strSql += "			,IFNULL(orderPeopleNum,'') AS orderPeopleNum  ";
		strSql += "			,IFNULL(orderTotalAmount,'') AS orderTotalAmount ";
		strSql += "			,IFNULL(DiscountAmount,'') AS DiscountAmount  ";
		strSql += "			,IFNULL(netAmountPrice,'') AS netAmountPrice ";
		
		strSql += "			,IFNULL(commissionAmount,'') AS 'commissionAmount'";
		strSql += "			,IFNULL(businessFavourableAmount,'') AS 'businessFavourableAmount'";
		strSql += "			,IFNULL(platformFavourableAmount,'') AS 'platformFavourableAmount' ";
		strSql += "			,IFNULL(subNetAmount,'') AS 'subNetAmount' ";

		
		strSql += "			,date_format(orderDate,'%Y-%c-%d %h:%i:%s') AS orderDate ";
		strSql += "			,IFNULL(invoiceHeader,'') AS  invoiceHeader ";
		strSql += "			,IFNULL(orderTypeDesc,'') AS orderTypeDesc  ";
		strSql += "			,IFNULL(orderStatusDesc,'') AS orderStatusDesc ";
		strSql += "			,IFNULL(employName,'') AS employName";
		strSql += " FROM vrep_sales_order_header ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataHeader = page.getResultList();
		pageModelList.add(listDataPage);

		// ������ϸ
		strSql = "";
		strSql += " SELECT ";
		strSql += " IFNULL(productName,'') AS productName";
		strSql += " ,IFNULL(numDesc,'') AS numDesc ";

		// strSql += " ,IFNULL(amountPrice,'') AS amountPrice";
		// strSql += ",IFNULL(displayDiscount,'') AS displayDiscount";
		strSql += " ,IFNULL(netAmountPrice,'') AS netAmountPrice";
		strSql += " ,IFNULL(proectGroup,'') AS proectGroup";
		strSql += " ,IFNULL(orderItemCode,'') AS orderItemCode";
		strSql += " FROM vrep_sales_order_item ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataItem = page.getResultList();

		// ѭ����ϸ������ϸ��ѯ����ϸ
		for (int i = 0; i < listDataItem.size(); i++) {
			Map<String, String> itemMap = (Map<String, String>) listDataItem.get(i);

			String proectGroup = itemMap.get("proectGroup");
			String orderItemCode = itemMap.get("orderItemCode");
			// proectGroup Ϊ 1 ʱΪ��������ϸ

			if (proectGroup.equals("1")) {

				strSql = "";
				strSql += " SELECT ";
				strSql += " IFNULL(productName,'') AS productName";
				strSql += " ,IFNULL(productNum,'') AS numDesc ";
				strSql += " ,IFNULL(netAmountPrice,'') AS netAmountPrice";
				strSql += " ,'' AS proectGroup";
				strSql += " FROM rep_pre_sales_order_subitem ";
				strSql += "  WHERE orderItemCode = '" + orderItemCode + "'";
				totalSQL = "select count(1) from (" + strSql + ") as Tab";

				logger.info("subitemSQL:" + strSql);

				pageModelList = new ArrayList<Object>();
				listDataPage = new PageModel();
				page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
				List<Object> orderSubItem = page.getResultList();

				for (int j = 0; j < orderSubItem.size(); j++) {

					Map<String, String> itemMapNew = new HashMap<String, String>();

					Map<String, String> subItemMap = (Map<String, String>) orderSubItem.get(j);

					itemMapNew.put(new String("productName"), "<span style='padding-left: 30px;'>"+subItemMap.get("productName")+"</span>");
					itemMapNew.put(new String("numDesc"), subItemMap.get("numDesc"));
					itemMapNew.put(new String("netAmountPrice"), subItemMap.get("netAmountPrice"));
					itemMapNew.put(new String("proectGroup"), subItemMap.get("proectGroup"));
					itemMapNew.put(new String("orderItemCode"), "");


					listDataItem.add((i+1), itemMapNew);
				}
			}

		}

		// ������ϸ�ϼ�
		strSql = "";
		strSql += " SELECT ";
		strSql += " '�ϼ�' AS productName";
		strSql += " ,IFNULL(SUM(numDesc),0) AS numDesc";
		strSql += " ,IFNULL(SUM(amountPrice),0) AS amountPrice";
		strSql += ",IFNULL(SUM(displayDiscount),0) AS displayDiscount";
		strSql += " ,IFNULL(SUM(netAmountPrice),0) AS netAmountPrice";
		strSql += " FROM vrep_sales_order_item ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataItemSum = page.getResultList();

		// �����ۿ��Ż�
		strSql = "";
		strSql += " SELECT ";
		strSql += " favourableName";
		strSql += " ,displayAmount";
		strSql += " FROM vrep_sales_order_payment_favourable ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataFavourable = page.getResultList();

		// �����ۿ��Żݺϼ�
		strSql = "";
		strSql += " SELECT ";
		strSql += " '�ϼ�' AS favourableName";
		strSql += " ,IFNULL(SUM(displayAmount),0) AS displayAmount";
		strSql += " FROM vrep_sales_order_payment_favourable ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataFavourableSum = page.getResultList();

		// ����֧����ϸ
		strSql = "";
		strSql += " SELECT ";
		strSql += " paymentKeyName";
		strSql += " ,displayAmount";
		strSql += " FROM vrep_sales_order_payment_item ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataPayment = page.getResultList();

		// ����֧����ϸ�ϼ�
		strSql = "";
		strSql += " SELECT ";
		strSql += " '�ϼ�' AS paymentKeyName";
		strSql += " ,IFNULL(SUM(displayAmount),0) AS displayAmount";
		strSql += " FROM vrep_sales_order_payment_item ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataPaymentSum = page.getResultList();

		// ������ͷ
		queryDataMap.put(new String("listDataHeader"), listDataHeader);
		// ������ϸ
		queryDataMap.put(new String("listDataItem"), listDataItem);
		queryDataMap.put(new String("listDataItemSum"), listDataItemSum);

		// �����ۿ��Ż�
		queryDataMap.put(new String("listDataFavourable"), listDataFavourable);
		queryDataMap.put(new String("listDataFavourableSum"), listDataFavourableSum);

		// ����֧����ϸ
		queryDataMap.put(new String("listDataPayment"), listDataPayment);
		queryDataMap.put(new String("listDataPaymentSum"), listDataPaymentSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(��������)====================================================
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> queryRepSalesCup(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String strSql = "";
		String totalSQL = "";

		strSql = "";
		strSql += " SELECT brandCodeName AS 'Ʒ��' ";
		strSql += ",IFNULL(CONCAT(brandCode,'|=|',productUomCodeName,'|=|',productUomCode),'') AS '��λ' ";
		strSql += "      ,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += "      ,IFNULL(SUM(orderCount),0) AS '��������' ";
		strSql += "      ,IFNULL(SUM(amountPrice),0) AS '�������' ";
		strSql += "      ,IFNULL(SUM(displayDiscount),0) AS 'ʵ���Żݽ��' ";
		strSql += "	 ,IFNULL(SUM(displayNetAmount),0) AS 'ʵ������' ";
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND storeCode in('" + storeCode + "')";
		}
		strSql += " GROUP BY brandCodeName,productUomCodeName,productUomCode ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("����" + strSql);

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// �ϼ�
		strSql = "";
		strSql += " SELECT '�ϼ�' AS 'Ʒ��' ";
		strSql += "		 ,'' AS '��λ' ";
		strSql += "      ,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += "      ,IFNULL(SUM(orderCount),0) AS '��������' ";
		strSql += "      ,IFNULL(SUM(amountPrice),0) AS '�������' ";

		strSql += "      ,IFNULL(SUM(displayDiscount),0) AS 'ʵ���Żݽ��' ";
		strSql += "	 ,IFNULL(SUM(displayNetAmount),0) AS 'ʵ������' ";
		
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND storeCode in('" + storeCode + "')";
		}

		logger.info("�ϼ�:" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)==================================================
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> queryRepSalesCupDetail(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String ProductUomCode = map.get("ProductUomCode");
		String strSql = "";
		String totalSQL = "";

		// ��λ���۱�����ϸ
		strSql = "";
		strSql += " SELECT brandCodeName AS 'Ʒ��' ";
		strSql += " 			,storeCodeName AS '�ŵ�' ";
		strSql += " 			,channelName AS '����' ";
		strSql += "       ,businessDate  AS '����' ";
		strSql += " 			,productName AS 'Ʒ��' ";
		strSql += "       ,productUomCodeName AS '��λ' ";
		strSql += " 			,menuItemtNum AS '��������' ";
		strSql += "       ,orderCount AS '��������' ";
		strSql += "       ,amountPrice AS '�������' ";
		strSql += "       ,displayDiscount AS 'ʵ���Żݽ��' ";
		strSql += " 			,displayNetAmount AS 'ʵ������' ";
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {

			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND StoreCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}

		strSql += "   AND productUomCode = '" + ProductUomCode + "' ";

		logger.info("������ϸ" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// �ϼ�
		strSql = "";

		strSql += " SELECT '�ϼ�' AS 'Ʒ��' ";
		strSql += " 			,'' AS '�ŵ�' ";
		strSql += " 			,'' AS '����' ";
		strSql += "       ,''  AS '����' ";
		strSql += " 			,'' AS 'Ʒ��' ";
		strSql += "       ,'' AS '��λ' ";
		strSql += " 			,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += "       ,IFNULL(SUM(orderCount),0) AS '��������' ";
		strSql += "       ,IFNULL(SUM(amountPrice),0) AS '�������' ";
		strSql += "       ,IFNULL(SUM(displayDiscount),0) AS 'ʵ���Żݽ��' ";
		strSql += " 			,IFNULL(SUM(displayNetAmount),0) AS 'ʵ������' ";
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {

			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}

		strSql += "   AND productUomCode = '" + ProductUomCode + "' ";

		logger.info("�ϼ�:" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)����Excel===========================================
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> queryRepSalesCupDetailExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String strSql = "";
		String totalSQL = "";

		// ��λ���۱�����ϸ
		strSql = "";
		strSql += " SELECT brandCodeName AS 'Ʒ��' ";
		strSql += " 			,storeCodeName AS '�ŵ�' ";
		strSql += " 			,channelName AS '����' ";
		strSql += "       ,businessDate  AS '����' ";
		strSql += " 			,productName AS 'Ʒ��' ";
		strSql += "       ,productUomCodeName AS '��λ' ";
		strSql += " 			,menuItemtNum AS '��������' ";
		strSql += "       ,orderCount AS '��������' ";
		strSql += "       ,amountPrice AS '�������' ";
		strSql += "       ,displayDiscount AS 'ʵ���Żݽ��' ";
		strSql += " 			,displayNetAmount AS 'ʵ������' ";
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in('" + BrandCode + "')";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND StoreCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}

		logger.info("������ϸ" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// �ϼ�
		strSql = "";

		strSql += " SELECT '�ϼ�' AS 'Ʒ��' ";
		strSql += " 			,'' AS '�ŵ�' ";
		strSql += " 			,'' AS '����' ";
		strSql += "       ,''  AS '����' ";
		strSql += " 			,'' AS 'Ʒ��' ";
		strSql += "       ,'' AS '��λ' ";
		strSql += " 			,IFNULL(SUM(menuItemtNum),0) AS '��������' ";
		strSql += "       ,IFNULL(SUM(orderCount),0) AS '��������' ";
		strSql += "       ,IFNULL(SUM(amountPrice),0) AS '�������' ";
		strSql += "       ,IFNULL(SUM(displayDiscount),0) AS 'ʵ���Żݽ��' ";
		strSql += " 			,IFNULL(SUM(displayNetAmount),0) AS 'ʵ������' ";
		strSql += " FROM vrep_sales_item_product_sales ";
		strSql += " WHERE ";
		strSql += " companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in('" + BrandCode + "')";
		}
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}

		logger.info("�ϼ�:" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

}
