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
	 * 返回下拉数据集合
	 * 
	 * @moduleName 模块表名
	 */
	

	@SuppressWarnings("unchecked")
	public List<Object> getSelectDropDownList(Map<String, String> map) {

		String selectCol = map.get("selectCol");
		String moduleName = map.get("moduleName");
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
		PageModel pageModel = new PageModel();
		Pagination page = new Pagination(sql, 1, 10000, jdbcTemplate, totalSQL, pageModel);
		return page.getResultList();
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

	/*
	 * 支付方式结构分析
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
	 * 支付方式
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
	 * ======门店支付方式分析报表========================================================
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
		strSelect += "storeCodeName AS '门店',businessDate AS '营业日期',storeTypeName AS '门店类型'";
		for (int i = 0; i < storePayMModelList.size(); i++) {
			StorePayMModel storePayMModel = (StorePayMModel) storePayMModelList.get(i);
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
					+ "' THEN subVamount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>收入金额'";
			
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
			+ "' THEN subNetAmount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>实际收入'";
	
			
			strSelect += ",MAX(CASE paymentKeyName WHEN '" + storePayMModel.getPaymentKeyName()
					+ "' THEN paymentKeyCount ELSE 0 END ) AS '" + storePayMModel.getPaymentKeyName() + "<br>收入次数'";
		}
		strSelect += ",SUM(subSamount) AS '收入金额合计'";
		strSelect += ",SUM(commissionAmount) AS '平台佣金合计' ";
		strSelect += ",SUM(businessFavourableAmount) AS '商家承担优惠金额合计'";
		strSelect += ",SUM(platformFavourableAmount) AS '平台承担优惠金额合计' ";
		strSelect += ",SUM(subNetAmount) AS '实际收入合计'";
		
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
		 * 查询明细数据列表 带分页
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
		strSumSelect += "'合计' AS '门店','' AS '营业日期','' AS '门店类型'";
		for (int i = 0; i < storePayMModelList.size(); i++) {
			StorePayMModel storePayMModel = (StorePayMModel) storePayMModelList.get(i);

			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>收入金额`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>收入金额' ";
			
			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>实际收入`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>实际收入' ";

			strSumSelect += ",IFNULL(sum(`" + storePayMModel.getPaymentKeyName() + "<br>收入次数`),0) AS '"
					+ storePayMModel.getPaymentKeyName() + "<br>收入次数' ";

		}
		strSumSelect += ",IFNULL(SUM(`收入金额合计`),0) AS 收入金额合计";
		strSumSelect += ",IFNULL(SUM(`平台佣金合计`),0) AS 平台佣金合计";
		strSumSelect += ",IFNULL(SUM(`商家承担优惠金额合计`),0) AS '商家承担优惠金额合计'";
		strSumSelect += ",IFNULL(SUM(`平台承担优惠金额合计`),0) AS 平台承担优惠金额合计";
		strSumSelect += ",IFNULL(SUM(`实际收入合计`),0) AS 实际收入合计";
		
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_payitem ";
		strFromTab += sqlWhere;
		strFromTab += ") AS tab";

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

		queryReppaymodeMap.put(new String("storePay"), storePayMModelList);
		queryReppaymodeMap.put(new String("storePayType"), storePayTypeList);
		queryReppaymodeMap.put(new String("listData"), dataListChannel);
		queryReppaymodeMap.put(new String("pageData"), pageModelList);
		queryReppaymodeMap.put(new String("listDataSum"), dataListSum);

		return queryReppaymodeMap;
	}

	/*
	 * ======门店支付方式明细报表========================================================
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
		strSelect += "storeCodeName AS '门店',businessDate AS '营业日期',IFNULL(storeTypeName,'') AS '门店类型',IFNULL(orderNo,'') AS '订单单号',date_format(createDate,'%Y-%c-%d %H:%i:%s') AS '结账时间',IFNULL(paymentKeyName,'') AS '支付方式'";
		strSelect += ",IFNULL(sAmount,0) AS '支付金额'";
		
		strSelect += ",IFNULL(commissionAmount, 0) AS '平台佣金'";
		strSelect += ",IFNULL(businessFavourableAmount, 0) AS '商家承担优惠金额'";
		strSelect += ",IFNULL(platformFavourableAmount, 0) AS '平台承担优惠金额'";
		strSelect += ",IFNULL(sAmountNet, 0) AS '实际收入'";
		
		strSelect += ",IFNULL(thirdOrderNo,'') AS '第三方支付单号'";
		strSelect += ",IFNULL(thirdMerchantNo,'') AS '第三方支付商户号'";
		strSelect += ",IFNULL(thirdPaymentUserAccount,'') AS '第三方支付顾客账号'";
		strSelect += ",IFNULL(employeeCode,'') AS '操作人员账号'";
		strSelect += ",IFNULL(employeeName,'') AS '操作人员名称'";
		
		
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
		 * 查询明细数据列表 带分页
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
		strSumSelect += "'合计' AS '门店','' AS '营业日期','' AS '门店类型','' AS '订单单号','' AS '结账时间','' AS '支付方式'";
		strSumSelect += ",IFNULL(SUM(`支付金额`),0) AS '支付金额'";
		strSumSelect += ",IFNULL(SUM(`平台佣金`),0) AS '平台佣金'";
		strSumSelect += ",IFNULL(SUM(`商家承担优惠金额`), 0) AS '商家承担优惠金额'";
		strSumSelect += ",IFNULL(SUM(`平台承担优惠金额`), 0) AS '平台承担优惠金额'";
		strSumSelect += ",IFNULL(SUM(`实际收入`), 0) AS '实际收入'";
		strSumSelect += ",'' AS '第三方支付单号'";
		strSumSelect += ",'' AS '第三方支付商户号'";
		strSumSelect += ",'' AS '第三方支付顾客账号'";
		strSumSelect += ",'' AS '操作人员账号'";
		strSumSelect += ",'' AS '操作人员名称'";
		
		String strFromTab = "(";
		strFromTab += "SELECT ";
		strFromTab += strSelect;
		strFromTab += " from vrep_sales_daily_payitem_detail ";
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

		List<Object> dataListSum = getQueryData(mappamSum, pageModelSum);

		queryReppayDetailMap.put(new String("listData"), dataListChannel);
		queryReppayDetailMap.put(new String("pageData"), pageModelList);
		queryReppayDetailMap.put(new String("listDataSum"), dataListSum);

		return queryReppayDetailMap;
	}

	/*
	 * ======门店渠道营收分析报表========================================================
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
		 * 圆形图形化数据
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
		 * 查询明细数据列表 带分页
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
		 * 柱状图图形化数据
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
		 * 查询明细数据列表 带分页
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
		 * 数据列表展示
		 */

		strSelect = "";
		strSelect += "A.storeCodeName AS '门店',A.channelName AS '渠道',A.businessDate AS '营业日期',storeTypeName AS '门店类型', IFNULL(SUM(A.subOrderCount),0) AS '订单单量'";
		strSelect += ",IFNULL(SUM(A.subOrderTotalAmount),0) AS '订单总额',IFNULL(SUM(A.subDiscountAmount),0) AS '订单优惠金额',IFNULL(SUM(A.subNetAmountPrice),0) AS '订单收入'";
		strSelect += ",IFNULL(A.commissionAmount, 0) AS '平台佣金'";
		strSelect += ",IFNULL(A.businessFavourableAmount, 0) AS '商家承担优惠金额'";
		strSelect += ",IFNULL(A.platformFavourableAmount, 0) AS '平台承担优惠金额'";
		strSelect += ",IFNULL(A.subNetAmount, 0) AS '实际收入'";

		
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmountPrice)/SUM(A.subOrderCount),2),0) AS '订单收入均价'";
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmount) / SUM(A.subOrderCount),2),0) AS '实际收入均价'";

		
		//strSelect += ",IFNULL(SUM(B.subOrderCount),0) AS '取消单量',IFNULL(SUM(B.subNetAmountPrice),0) AS '取消金额'";
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
		 * 查询明细数据列表 带分页
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
		 * 数据列表合计行
		 */

		strSelect = "";
		strSelect += "'合计' AS '门店','' AS '渠道','' AS '营业日期','' AS '门店类型',IFNULL(SUM(A.subOrderCount),0) AS '订单单量'";
		strSelect += ",IFNULL(SUM(A.subOrderTotalAmount),0) AS '订单总额',IFNULL(SUM(A.subDiscountAmount),0) AS '订单优惠金额',IFNULL(SUM(A.subNetAmountPrice),0) AS '订单收入'";
		
		strSelect += ",IFNULL(SUM(A.commissionAmount), 0) AS '平台佣金'";
		strSelect += ",IFNULL(SUM(A.businessFavourableAmount), 0) AS '商家承担优惠金额'";
		strSelect += ",IFNULL(SUM(A.platformFavourableAmount), 0) AS '平台承担优惠金额'";
		strSelect += ",IFNULL(SUM(A.subNetAmount), 0) AS '实际收入'";

			
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmountPrice)/SUM(A.subOrderCount),2),0) AS '订单收入均价'";
		strSelect += ",IFNULL(ROUND(SUM(A.subNetAmount) / SUM(A.subOrderCount),2),0) AS '实际收入均价'";

		
		
		//strSelect += ",IFNULL(SUM(B.subOrderCount),0) AS '取消单量',IFNULL(SUM(B.subNetAmountPrice),0) AS '取消金额'";
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
		 * 查询明细数据列表 带分页
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
	 * ======门店分时销售分析报表========================================================
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

		// 查询营业时段类型
		String strSelect = "";
		strSelect += " bizHourDesc";

		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		// 品牌
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
		strFormTab += "       ,CASE WHEN A.bizHour = '营业时段' THEN 1 ";
		strFormTab += "             WHEN A.bizHour = '其他时段' THEN 999  ";
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
		// 门店
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

		// 列表数据查询
		strSelect = "";
		strSelect += " storeCodeName AS '门店',businessDate AS '营业日期',storeTypeName AS '门店类型' ";
		if (bizHourDescList.size() == 0) {
			strSelect += ",MAX(CASE bizHour WHEN '营业时段' THEN subOrderCount ELSE 0 END ) AS '营业时段<br>订单量'";
			strSelect += ",MAX(CASE bizHour WHEN '营业时段' THEN subNetAmountPrice ELSE 0 END ) AS '营业时段<br>实际收入'";
			strSelect += ",MAX(CASE bizHour WHEN '营业时段' THEN TA ELSE 0 END ) AS '营业时段<br>客单价'";
		} else {

			for (int i = 0; i < bizHourDescList.size(); i++) {
				Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc")
						+ "' THEN subOrderCount ELSE 0 END ) AS '" + hourDescMap.get("bizHourDesc") + "<br>订单量'";
				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc")
						+ "' THEN subNetAmountPrice ELSE 0 END ) AS '" + hourDescMap.get("bizHourDesc") + "<br>实际收入'";
				strSelect += ",MAX(CASE bizHour WHEN '" + hourDescMap.get("bizHourDesc") + "' THEN TA ELSE 0 END ) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>客单价'";
			}

			strSelect += ",MAX(CASE bizHour WHEN '其他时段' THEN subOrderCount ELSE 0 END ) AS '其他时段<br>订单量'";
			strSelect += ",MAX(CASE bizHour WHEN '其他时段' THEN subNetAmountPrice ELSE 0 END ) AS '其他时段<br>实际收入'";
			strSelect += ",MAX(CASE bizHour WHEN '其他时段' THEN TA ELSE 0 END ) AS '其他时段<br>客单价'";
		}

		strSelect += ",SUM(subOrderCount) AS '合计<br>单量'";
		strSelect += ",SUM(subNetAmountPrice) AS '合计<br>实际收入'";

		sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		// 门店
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

		strSumSelect += " '合计' AS '门店','' AS '营业日期','' AS '门店类型'";
		if (bizHourDescList.size() == 0) {
			strSumSelect += ",IFNULL(sum(`营业时段<br>订单量`),0) AS '营业时段<br>订单量' ";
			strSumSelect += ",IFNULL(sum(`营业时段<br>实际收入`),0) AS '营业时段<br>实际收入'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`营业时段<br>实际收入`),0)/IFNULL(sum(`营业时段<br>订单量`),0),2),0) AS '营业时段<br>客单价'";
		} else {

			for (int i = 0; i < bizHourDescList.size(); i++) {
				Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

				/*
				 * strSumSelect += ",IFNULL(sum(`" +
				 * hourDescMap.get("bizHourDesc") + "<br>订单量`),0) AS '" +
				 * hourDescMap.get("bizHourDesc") + "<br>订单量' "; strSumSelect +=
				 * ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") +
				 * "<br>营业额`),0) AS '" + hourDescMap.get("bizHourDesc") +
				 * "<br>营业额'"; strSumSelect += ",IFNULL(sum(`" +
				 * hourDescMap.get("bizHourDesc") + "<br>客单价`),0) AS '" +
				 * hourDescMap.get("bizHourDesc") + "<br>客单价'";
				 */

				strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>订单量`),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>订单量' ";
				strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>营业额`),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>实际收入'";
				strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`" + hourDescMap.get("bizHourDesc")
						+ "<br>营业额`),0)/IFNULL(sum(`" + hourDescMap.get("bizHourDesc") + "<br>订单量`),0),2),0) AS '"
						+ hourDescMap.get("bizHourDesc") + "<br>客单价'";
			}

			strSumSelect += ",IFNULL(sum(`其他时段<br>订单量`),0) AS '其他时段<br>订单量'";
			strSumSelect += ",IFNULL(sum(`其他时段<br>实际收入`),0) AS '其他时段<br>实际收入'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`其他时段<br>实际收入`),0)/IFNULL(sum(`其他时段<br>订单量`),0),2),0) AS '其他时段<br>客单价'";

		}

		strSumSelect += ",IFNULL(sum(`合计<br>单量`),0) AS '合计<br>单量' ";
		strSumSelect += ",IFNULL(sum(`合计<br>实际收入`),0) AS '合计<br>实际收入' ";

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
	 * ======门店活动销售分析报表========================================================
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
		// 饼图内圈数据
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// 门店
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// 渠道
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}

		// 优惠活动
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}
		strSql += " GROUP BY C.orderTypeDesc";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageBT = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageBT);
		List<Object> BTNData = page.getResultList();

		// 饼图外圈及柱状图数据
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
		// 门店
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// 渠道
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// 优惠活动
		if (favourableName != "") {
			strSql += " AND A.favourableName = '" + favourableName + "'";
		}
		strSql += " GROUP BY D.channelName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("strSql:"+strSql);
		
		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		// 优惠活动横向对比图
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
		// 门店
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// 渠道
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// 优惠活动
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

		strSql += " SELECT B.storeCodeName AS '门店'";
		strSql += " ,D.channelName AS '销售渠道' ";
		strSql += " ,A.businessDate AS '营业日期'  ";
		strSql += " ,B.storeTypeName AS '门店类型'  ";
		strSql += " ,orderTypeDesc AS '售卖方式' ";
		strSql += " ,favourableName AS '优惠名称' ";
		strSql += " ,SUM(OrderCount) AS '使用次数' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '订单金额（分摊）' ";
		strSql += " ,SUM(sDiscount) AS '优惠金额' ";
		
		strSql += ",SUM(sNetAmountPrice) AS '订单收入（分摊）'";
		strSql += ",SUM(sCommissionAmount) AS '平台佣金（分摊）'";
		strSql += ",SUM(businessFavourableAmount) AS '商家承担优惠金额'";
		strSql += ",SUM(platformFavourableAmount) AS '平台承担优惠金额'";
		strSql += ",SUM(subNetAmount) AS '实际收入（分摊）'";
		strSql += ",ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2) AS '单次订单收入（分摊）'";
		strSql += ",ROUND(SUM(subNetAmount) / SUM(OrderCount),2) AS '单次实际收入（分摊）'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}
		// 门店
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// 渠道
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// 优惠活动
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
		strSql += " SELECT '合计' AS '门店' ";
		strSql += " ,'' AS '销售渠道' ";
		strSql += " ,'' AS '营业日期' ";
		strSql += " ,'' AS '门店类型' ";
		strSql += " ,'' AS '售卖方式' ";
		strSql += " ,'' AS '优惠名称' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS '使用次数' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '订单金额（分摊）' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '优惠金额' ";
		
		strSql += ",IFNULL(SUM(sNetAmountPrice),0) AS '订单收入（分摊）'";
		strSql += ",IFNULL(SUM(sCommissionAmount),0) AS '平台佣金（分摊）'";
		strSql += ",IFNULL(SUM(businessFavourableAmount),0) AS '商家承担优惠金额'";
		strSql += ",IFNULL(SUM(platformFavourableAmount),0) AS '平台承担优惠金额'";
		strSql += ",IFNULL(SUM(subNetAmount),0) AS '实际收入（分摊）'";
		strSql += ",IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '单次订单收入（分摊）'";
		strSql += ",IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '单次实际收入（分摊）'";
		
		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {

			strSql += " AND A.brandcode = '" + BrandCode + "'";
		}

		// 门店
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// 渠道
		if (ChannelCode != "") {
			strSql += " AND A.salesChannelKey = '" + ChannelCode + "'";
		}
		// 优惠活动
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
	 * ======门店产品销售分析报表========================================================
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
		strSql += " IFNULL(productName,'') AS '产品名称' ";
		strSql += " ,IFNULL(serviceTypeName, '') AS '类型'";
		strSql += " ,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += " ,IFNULL(SUM(orderCount),0)  AS '订单单量' ";
		strSql += " ,IFNULL(SUM(amountPrice),0) AS '销售金额'";
		strSql += " ,IFNULL(SUM(netAmountPrice),0) AS '实际金额' ";
		
		strSql += " ,IFNULL(SUM(sDiscount), 0) AS '实际订单优惠（分摊）'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS '实际收入'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(orderCount),2),'--') AS '平均每单<br>实际收入'";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND(SUM(subNetAmount) / "+netAmountPrice+",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入占比'";

		
		strSql += "  FROM vrep_sales_item_detail AS A ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		// 门店
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
		strSql += " SELECT '合计' AS '产品名称' ";
		strSql += " ,'' AS '类型'";
		strSql += " ,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += " ,IFNULL(SUM(orderCount),0) AS '订单单量' ";
		strSql += " ,IFNULL(SUM(amountPrice),0) AS '销售金额'";
		strSql += " ,IFNULL(SUM(netAmountPrice),0) AS '实际金额' ";
		
		strSql += " ,IFNULL(SUM(sDiscount), 0) AS '实际订单优惠（分摊）'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS '实际收入'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(orderCount),2),'--') AS '平均每单<br>实际收入'";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND(SUM(subNetAmount) / "+netAmountPrice+",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入占比'";

		strSql += "  FROM vrep_sales_item_detail AS A ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		// 门店
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
	 * ======门店半个小时销售分析报表======================================================
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

		// 查询营业时段类型
		String strSelect = "";
		strSelect += "  bizHourTime";

		String sqlWhere = "";

		sqlWhere += " WHERE companyCode = '" + companyCode + "' ";
		sqlWhere += "   AND (businessDate >='" + businessDateStart + "' AND businessDate <='" + businessDateEnd + "') ";
		// 门店
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
		// 门店
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

		// 列表数据查询
		strSelect = "";
		strSelect += " storeCodeName AS '门店',businessDate AS '营业日期',storeTypeName AS '门店类型' ";

		for (int i = 0; i < bizHourDescList.size(); i++) {
			Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime")
					+ "' THEN subOrderCount ELSE 0 END ) AS '" + hourDescMap.get("bizHourTime") + "<br>订单量'";
			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime")
					+ "' THEN subNetAmountPrice ELSE 0 END ) AS '" + hourDescMap.get("bizHourTime") + "<br>实际收入'";
			strSelect += ",MAX(CASE bizHourTime WHEN '" + hourDescMap.get("bizHourTime") + "' THEN TA ELSE 0 END ) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>客单价'";
		}

		strSelect += ",SUM(subOrderCount) AS '合计<br>订单量'";
		strSelect += ",SUM(subNetAmountPrice) AS '合计<br>实际收入'";

		sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		// 门店
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

		strSumSelect += " '合计' AS '门店','' AS '营业日期','' AS '门店类型'";

		for (int i = 0; i < bizHourDescList.size(); i++) {
			Map<String, String> hourDescMap = (Map<String, String>) bizHourDescList.get(i);

			strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>订单量`),0) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>订单量' ";
			strSumSelect += ",IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>实际收入`),0) AS '"
					+ hourDescMap.get("bizHourTime") + "<br>实际收入'";
			strSumSelect += ",IFNULL(ROUND(IFNULL(sum(`" + hourDescMap.get("bizHourTime") + "<br>实际收入`),0)/IFNULL(sum(`"
					+ hourDescMap.get("bizHourTime") + "<br>订单量`),0),2),0) AS '" + hourDescMap.get("bizHourTime")
					+ "<br>客单价'";
		}

		strSumSelect += ",IFNULL(sum(`合计<br>订单量`),0) AS '合计<br>订单量' ";
		strSumSelect += ",IFNULL(sum(`合计<br>实际收入`),0) AS '合计<br>实际收入' ";

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
	 * ======门店交易流水明细报表========================================================
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
		strSql += "      IFNULL(brandCodeName,'') AS '品牌' ";
		strSql += "			,IFNULL(storeCodeName,'') AS '门店' ";
		strSql += "      ,IFNULL(storeTypeName,'') AS '门店类型' ";
		strSql += "      ,IFNULL(channelName,'') AS '渠道' ";
		strSql += "			,IFNULL(businessDate,'') AS '营业日期' ";

		if (IsExportExcel.equals("Y")) {
			strSql += ",IFNULL(orderNo,'') AS '订单单号' ";
		} else {
			strSql += ",IFNULL(CONCAT(orderCode,'|=|', orderNo),'') AS '订单单号' ";
		}

		strSql += "			,IFNULL(description,'') AS '订单备注' ";
		strSql += "      ,IFNULL(thirdOrderNo,'') AS '第三方交易单号' ";
		strSql += "			,IFNULL(orderPeopleNum,'') AS '订单人数' ";
		strSql += "			,IFNULL(orderTotalAmount,'') AS '订单金额' ";
		strSql += "			,IFNULL(DiscountAmount,'') AS '优惠金额' ";
		
		strSql += "			,IFNULL(netAmountPrice, '') AS '订单收入'";
		strSql += "			,IFNULL(commissionAmount, '') AS '平台佣金'";
		strSql += "			,IFNULL(businessFavourableAmount, '') AS '商家承担优惠金额'";
		strSql += "			,IFNULL(platformFavourableAmount, '') AS '平台承担优惠金额'";
		strSql += "			,IFNULL(subNetAmount, '') AS '实际收入'";

		
		strSql += "			,date_format(orderDate,'%Y-%c-%d %H:%i:%s') AS '订单时间' ";
		strSql += "			,IFNULL(invoiceHeader,'') AS '发票抬头' ";
		strSql += "			,IFNULL(orderTypeDesc,'') AS '订单类型' ";
		strSql += "			,IFNULL(orderStatusDesc,'') AS '订单状态' ";
		//strSql += "			,IFNULL(employName,'') AS '操作人员' ";
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
		strSql += "      '合计' AS '品牌' ";
		strSql += "			,'' AS '门店' ";
		strSql += "      ,'' AS '门店类型' ";
		strSql += "      ,'' AS '渠道' ";
		strSql += "			,'' AS '营业日期' ";
		strSql += "			,'' AS '订单单号' ";
		strSql += "			,'' AS '订单备注' ";
		strSql += "      ,'' AS '第三方交易单号' ";
		strSql += "			,IFNULL(SUM( orderPeopleNum),0) AS '订单人数' ";
		strSql += "			,IFNULL(SUM(orderTotalAmount),0) AS '订单金额' ";
		strSql += "			,IFNULL(SUM(DiscountAmount),0) AS '优惠金额' ";
		
		strSql += "			,IFNULL(SUM(netAmountPrice), 0) AS '订单收入'";
		strSql += "			,IFNULL(SUM(commissionAmount), 0) AS '平台佣金'";
		strSql += "			,IFNULL(SUM(businessFavourableAmount),0) AS '商家承担优惠金额'";
		strSql += "			,IFNULL(SUM(platformFavourableAmount), 0) AS '平台承担优惠金额'";
		strSql += "			,IFNULL(SUM(subNetAmount), 0) AS '实际收入'";
		
		
		
		strSql += "			,'' AS '订单时间' ";
		strSql += "			,'' AS '发票抬头' ";
		strSql += "			,'' AS '订单类型' ";
		strSql += "			,'' AS '订单状态' ";
		//strSql += "			,'' AS '操作人员' ";
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
	 * ======门店交易流水单据详细明细======================================================
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

		// 订单表头
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

		// 订单明细
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

		// 循环明细订单明细查询子明细
		for (int i = 0; i < listDataItem.size(); i++) {
			Map<String, String> itemMap = (Map<String, String>) listDataItem.get(i);

			String proectGroup = itemMap.get("proectGroup");
			String orderItemCode = itemMap.get("orderItemCode");
			// proectGroup 为 1 时为存在子明细

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

		// 订单明细合计
		strSql = "";
		strSql += " SELECT ";
		strSql += " '合计' AS productName";
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

		// 订单折扣优惠
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

		// 订单折扣优惠合计
		strSql = "";
		strSql += " SELECT ";
		strSql += " '合计' AS favourableName";
		strSql += " ,IFNULL(SUM(displayAmount),0) AS displayAmount";
		strSql += " FROM vrep_sales_order_payment_favourable ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataFavourableSum = page.getResultList();

		// 订单支付明细
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

		// 订单支付明细合计
		strSql = "";
		strSql += " SELECT ";
		strSql += " '合计' AS paymentKeyName";
		strSql += " ,IFNULL(SUM(displayAmount),0) AS displayAmount";
		strSql += " FROM vrep_sales_order_payment_item ";
		strSql += "  WHERE orderCode = '" + OrderCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		pageModelList = new ArrayList<Object>();
		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listDataPaymentSum = page.getResultList();

		// 订单表头
		queryDataMap.put(new String("listDataHeader"), listDataHeader);
		// 订单明细
		queryDataMap.put(new String("listDataItem"), listDataItem);
		queryDataMap.put(new String("listDataItemSum"), listDataItemSum);

		// 订单折扣优惠
		queryDataMap.put(new String("listDataFavourable"), listDataFavourable);
		queryDataMap.put(new String("listDataFavourableSum"), listDataFavourableSum);

		// 订单支付明细
		queryDataMap.put(new String("listDataPayment"), listDataPayment);
		queryDataMap.put(new String("listDataPaymentSum"), listDataPaymentSum);

		return queryDataMap;
	}

	/*
	 * ======门店杯量统计报表(杯量汇总)====================================================
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
		strSql += " SELECT brandCodeName AS '品牌' ";
		strSql += ",IFNULL(CONCAT(brandCode,'|=|',productUomCodeName,'|=|',productUomCode),'') AS '单位' ";
		strSql += "      ,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += "      ,IFNULL(SUM(orderCount),0) AS '订单单量' ";
		strSql += "      ,IFNULL(SUM(amountPrice),0) AS '订单金额' ";
		strSql += "      ,IFNULL(SUM(displayDiscount),0) AS '实际优惠金额' ";
		strSql += "	 ,IFNULL(SUM(displayNetAmount),0) AS '实际收入' ";
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

		logger.info("杯量" + strSql);

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// 合计
		strSql = "";
		strSql += " SELECT '合计' AS '品牌' ";
		strSql += "		 ,'' AS '单位' ";
		strSql += "      ,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += "      ,IFNULL(SUM(orderCount),0) AS '订单单量' ";
		strSql += "      ,IFNULL(SUM(amountPrice),0) AS '订单金额' ";

		strSql += "      ,IFNULL(SUM(displayDiscount),0) AS '实际优惠金额' ";
		strSql += "	 ,IFNULL(SUM(displayNetAmount),0) AS '实际收入' ";
		
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

		logger.info("合计:" + strSql);
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
	 * ======门店杯量统计报表(杯量销售明细)==================================================
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

		// 单位销售报表明细
		strSql = "";
		strSql += " SELECT brandCodeName AS '品牌' ";
		strSql += " 			,storeCodeName AS '门店' ";
		strSql += " 			,channelName AS '渠道' ";
		strSql += "       ,businessDate  AS '日期' ";
		strSql += " 			,productName AS '品名' ";
		strSql += "       ,productUomCodeName AS '单位' ";
		strSql += " 			,menuItemtNum AS '销售数量' ";
		strSql += "       ,orderCount AS '订单单量' ";
		strSql += "       ,amountPrice AS '订单金额' ";
		strSql += "       ,displayDiscount AS '实际优惠金额' ";
		strSql += " 			,displayNetAmount AS '实际收入' ";
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

		logger.info("杯量明细" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// 合计
		strSql = "";

		strSql += " SELECT '合计' AS '品牌' ";
		strSql += " 			,'' AS '门店' ";
		strSql += " 			,'' AS '渠道' ";
		strSql += "       ,''  AS '日期' ";
		strSql += " 			,'' AS '品名' ";
		strSql += "       ,'' AS '单位' ";
		strSql += " 			,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += "       ,IFNULL(SUM(orderCount),0) AS '订单单量' ";
		strSql += "       ,IFNULL(SUM(amountPrice),0) AS '订单金额' ";
		strSql += "       ,IFNULL(SUM(displayDiscount),0) AS '实际优惠金额' ";
		strSql += " 			,IFNULL(SUM(displayNetAmount),0) AS '实际收入' ";
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

		logger.info("合计:" + strSql);
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
	 * ======门店杯量统计报表(杯量销售明细)导出Excel===========================================
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

		// 单位销售报表明细
		strSql = "";
		strSql += " SELECT brandCodeName AS '品牌' ";
		strSql += " 			,storeCodeName AS '门店' ";
		strSql += " 			,channelName AS '渠道' ";
		strSql += "       ,businessDate  AS '日期' ";
		strSql += " 			,productName AS '品名' ";
		strSql += "       ,productUomCodeName AS '单位' ";
		strSql += " 			,menuItemtNum AS '销售数量' ";
		strSql += "       ,orderCount AS '订单单量' ";
		strSql += "       ,amountPrice AS '订单金额' ";
		strSql += "       ,displayDiscount AS '实际优惠金额' ";
		strSql += " 			,displayNetAmount AS '实际收入' ";
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

		logger.info("杯量明细" + strSql);
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(listDataPage);

		// 合计
		strSql = "";

		strSql += " SELECT '合计' AS '品牌' ";
		strSql += " 			,'' AS '门店' ";
		strSql += " 			,'' AS '渠道' ";
		strSql += "       ,''  AS '日期' ";
		strSql += " 			,'' AS '品名' ";
		strSql += "       ,'' AS '单位' ";
		strSql += " 			,IFNULL(SUM(menuItemtNum),0) AS '销售数量' ";
		strSql += "       ,IFNULL(SUM(orderCount),0) AS '订单单量' ";
		strSql += "       ,IFNULL(SUM(amountPrice),0) AS '订单金额' ";
		strSql += "       ,IFNULL(SUM(displayDiscount),0) AS '实际优惠金额' ";
		strSql += " 			,IFNULL(SUM(displayNetAmount),0) AS '实际收入' ";
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

		logger.info("合计:" + strSql);
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
