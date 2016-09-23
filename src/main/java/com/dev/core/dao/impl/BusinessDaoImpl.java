package com.dev.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.BusinessDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;

@Repository

public class BusinessDaoImpl implements BusinessDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Logger logger = Logger.getLogger(BusinessDaoImpl.class);

	/*
	 * ======营业收入趋势分析报表(品牌汇总)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepTrendBrand(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");

		String strSql = "";
		String totalSQL = "";

		// 获取日期中月份

		String strYear = "";
		String strMonth = "";
		String strDay = "";

		strYear = businessDateStart.split("-")[0];
		strMonth = businessDateStart.split("-")[1];
		strDay = businessDateStart.split("-")[2];

		int CurMonth = Integer.parseInt(strMonth);
		String strSqlTemp1 = "";
		String strSqlTemp2 = "";
		String strSqlTemp3 = "";

		for (int i = 1; i <= CurMonth; i++) {
			if (i == CurMonth) {

				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode))";
				strSqlTemp3 += " / SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + CurMonth + "月'";

			} else {
				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) ";
				strSqlTemp3 += " / SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + i + "月'";
			}
		}

		strSqlTemp1 = strSqlTemp1.substring(1, strSqlTemp1.length());
		strSqlTemp2 = strSqlTemp2.substring(1, strSqlTemp2.length());
		strSqlTemp3 = strSqlTemp3.substring(1, strSqlTemp3.length());

		strSql += "SELECT ";
		strSql += strSqlTemp1;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();

		// 第二张图形化 单量数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp2;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-1张图标SQL:" + strSql);

		PageModel dataTXHTwoPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHTwoPageModel);
		List<Object> dataTXHTwo1 = page.getResultList();

		// 第2-2张图形化 客单价数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp3;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-2张图标SQL:" + strSql);

		PageModel dataTXHThPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHThPageModel);
		List<Object> dataTXHTwo2 = page.getResultList();

		// 查询品牌信息
		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(areaCode,'') AS areaCode  FROM vrep_dict_brand ";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		PageModel brandPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();
		List<Object> listData = new ArrayList<Object>();
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCodeTemp = mapBrand.get("areaCode");
			String brandCodeTemp = mapBrand.get("brandCode");
			String brandCodeNameTemp = mapBrand.get("brandCodeName");

			// 明细数据
			strSql = "";
			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCodeTemp + "','|=|','" + brandCodeNameTemp + "'" + ",'|=|','"
					+ areaCodeTemp + "'),'') AS '品牌' ";

			strSql += " 			,IFNULL(SUM(fn_addup_day_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '本日<br>实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_week_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本周实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_month_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本月实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_year_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本年实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '本日单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本周单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本月单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本年单量' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '本日<br>平均客单价' ";

			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本周平均客单价' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本月平均客单价' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本年平均客单价' ";
			strSql += "  FROM vrep_dict_brand_area_store AS A ";

			strSql += "   WHERE A.companyCode = '" + companyCode + "'";

			// 门店
			if (storeCode != "") {
				storeCode = storeCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + storeCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + storeCode + "')";
			}

			if (brandCodeTemp != "") {
				strSql += " AND A.brandCode = '" + brandCodeTemp + "'";
			}

			strSql += " ORDER BY A.brandCodeName,A.areaName,A.storeCodeName";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'合计' AS '品牌' ";

		strSql += ",IFNULL(CAST(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '本日<br>实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本周实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本月实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本年实际收入' ";
		strSql += ",IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '本日单量' ";
		strSql += ",IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本周单量' ";
		strSql += ",IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本月单量' ";
		strSql += ",IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本年单量' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '本日<br>平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本周平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本月平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(totalSQL);

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("dataTXHOne"), dataTXHOne);
		queryDataMap.put(new String("dataTXHTwo1"), dataTXHTwo1);
		queryDataMap.put(new String("dataTXHTwo2"), dataTXHTwo2);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======营业收入趋势分析报表(区域汇总)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepTrendBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");

		String strSql = "";
		String totalSQL = "";
		String AreaCodeALL = "";

		// 区域
		strSql = "";
		totalSQL = "";
		strSql += "SELECT areaCode,areaName FROM vrep_dict_brand_area_store";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";
		strSql += " GROUP BY areaCode,areaName ORDER BY areaName ";

		logger.info("vrep_dict_brand_area_store:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			AreaCodeALL += "'" + areaCode + "',";
			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_areacode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");

			AreaCodeALL += allChildrenCode + ",";

			String isStore = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT storeCode FROM vrep_dict_brand_area_store ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND areaCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isStoreData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isStoreData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isStoreData.get(k);
				String storeCodeTemp = mapTemoS.get("storeCode");
				if (storeCodeTemp != null && !storeCodeTemp.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// 明细数据
			strSql = "";
			totalSQL = "";
			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '区域' ";

			strSql += " 			,IFNULL(SUM(fn_addup_day_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '本日<br>实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_week_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本周实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_month_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本月实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_year_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本年实际收入' ";
			strSql += " 			,IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '本日单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本周单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本月单量' ";
			strSql += " 			,IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),0) AS '截止本日<br>本年单量' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '本日<br>平均客单价' ";

			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本周平均客单价' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本月平均客单价' ";
			strSql += " 			,IFNULL(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)) ";
			strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '截止本日<br>本年平均客单价' ";
			strSql += "  FROM vrep_dict_brand_area_store AS A ";

			strSql += "   WHERE A.companyCode = '" + companyCode + "'";

			// 门店
			if (storeCode != "") {
				storeCode = storeCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + storeCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + storeCode + "')";
			}

			if (BrandCode != "") {
				strSql += " AND A.brandCode = '" + BrandCode + "'";
			}

			strSql += "   AND A.areaCode IN (" + allChildrenCode + ") ";

			strSql += " ORDER BY A.brandCodeName,A.areaName,A.storeCodeName";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		AreaCodeALL = AreaCodeALL.substring(0, AreaCodeALL.length() - 1);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'合计' AS '所属品牌' ";
		strSql += ",'' AS '区域' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '本日<br>实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本周实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本月实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本年实际收入' ";
		strSql += ",IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '本日单量' ";
		strSql += ",IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本周单量' ";
		strSql += ",IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本月单量' ";
		strSql += ",IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本年单量' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '本日<br>平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本周平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本月平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			strSql += "  AND A.brandCode = '" + BrandCode + "' ";
		}

		strSql += "   AND A.areaCode IN (" + AreaCodeALL + ") ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(totalSQL);

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// 获取日期中月份
		String strYear = "";
		String strMonth = "";
		String strDay = "";

		strYear = businessDateStart.split("-")[0];
		strMonth = businessDateStart.split("-")[1];
		strDay = businessDateStart.split("-")[2];

		int CurMonth = Integer.parseInt(strMonth);
		String strSqlTemp1 = "";
		String strSqlTemp2 = "";
		String strSqlTemp3 = "";

		for (int i = 1; i <= CurMonth; i++) {
			if (i == CurMonth) {

				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode))";
				strSqlTemp3 += " / SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + CurMonth + "月'";

			} else {
				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) ";
				strSqlTemp3 += " / SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + i + "月'";
			}
		}

		strSqlTemp1 = strSqlTemp1.substring(1, strSqlTemp1.length());
		strSqlTemp2 = strSqlTemp2.substring(1, strSqlTemp2.length());
		strSqlTemp3 = strSqlTemp3.substring(1, strSqlTemp3.length());

		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp1;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			strSql += "  AND A.brandCode ='" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode IN (" + AreaCodeALL + ") ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();

		// 第二张图形化 单量数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp2;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			strSql += "  AND A.brandCode ='" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode IN (" + AreaCodeALL + ") ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-1张图标SQL:" + strSql);

		PageModel dataTXHTwoPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHTwoPageModel);
		List<Object> dataTXHTwo1 = page.getResultList();

		// 第2-2张图形化 客单价数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp3;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			strSql += "  AND A.brandCode ='" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode IN (" + AreaCodeALL + ") ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-2张图标SQL:" + strSql);

		PageModel dataTXHThPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHThPageModel);
		List<Object> dataTXHTwo2 = page.getResultList();

		queryDataMap.put(new String("dataTXHOne"), dataTXHOne);
		queryDataMap.put(new String("dataTXHTwo1"), dataTXHTwo1);
		queryDataMap.put(new String("dataTXHTwo2"), dataTXHTwo2);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======营业收入趋势分析报表(门店汇总)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepTrend(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String AreaName = map.get("AreaName");

		String strSql = "";
		String totalSQL = "";

		// 获取日期中月份

		String strYear = "";
		String strMonth = "";
		String strDay = "";

		strYear = businessDateStart.split("-")[0];
		strMonth = businessDateStart.split("-")[1];
		strDay = businessDateStart.split("-")[2];

		int CurMonth = Integer.parseInt(strMonth);
		String strSqlTemp1 = "";
		String strSqlTemp2 = "";
		String strSqlTemp3 = "";

		for (int i = 1; i <= CurMonth; i++) {
			if (i == CurMonth) {

				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode))";
				strSqlTemp3 += " / SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + CurMonth + "月'";

			} else {
				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) ";
				strSqlTemp3 += " / SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + i + "月'";
			}
		}

		strSqlTemp1 = strSqlTemp1.substring(1, strSqlTemp1.length());
		strSqlTemp2 = strSqlTemp2.substring(1, strSqlTemp2.length());
		strSqlTemp3 = strSqlTemp3.substring(1, strSqlTemp3.length());

		strSql += "SELECT ";
		strSql += strSqlTemp1;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();

		// 第二张图形化 单量数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp2;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-1张图标SQL:" + strSql);

		PageModel dataTXHTwoPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHTwoPageModel);
		List<Object> dataTXHTwo1 = page.getResultList();

		// 第2-2张图形化 客单价数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp3;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-2张图标SQL:" + strSql);

		PageModel dataTXHThPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHThPageModel);
		List<Object> dataTXHTwo2 = page.getResultList();

		// 明细数据
		strSql = "";
		totalSQL = "";

		strSql += " SELECT ";
		strSql += "'" + BrandName + "' AS '所属品牌'";
		strSql += ",'" + AreaName + "' AS '所属区域'";
		strSql += " ,A.storeCodeName AS '门店' ";
		strSql += " 			,A.provinceName AS '省份' ";
		strSql += " 			,A.cityName AS '城市' ";
		strSql += " 			,A.regionName AS '地区' ";
		strSql += " 			,A.storeTypeName AS '门店类型' ";

		strSql += " 			,fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '本日<br>实际收入' ";
		strSql += " 			,fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本周实际收入' ";
		strSql += " 			,fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本月实际收入' ";
		strSql += " 			,fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本年实际收入' ";
		strSql += " 			,fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '本日单量' ";
		strSql += " 			,fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本周单量' ";
		strSql += " 			,fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本月单量' ";
		strSql += " 			,fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本年单量' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '本日<br>平均客单价' ";

		strSql += " 			,IFNULL(ROUND(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本周平均客单价' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本月平均客单价' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";

		strSql += " ORDER BY A.brandCodeName,A.areaName,A.storeCodeName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'合计' AS '所属品牌' ";
		strSql += ",'' AS '所属区域' ";
		strSql += ",'' AS '门店' ";
		strSql += ",'' AS '省份' ";
		strSql += ",'' AS '城市' ";
		strSql += ",'' AS '地区'";
		strSql += ",'' AS '门店类型' ";

		strSql += ",IFNULL(CAST(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '本日<br>实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本周实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本月实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本年实际收入' ";
		strSql += ",IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '本日单量' ";
		strSql += ",IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本周单量' ";
		strSql += ",IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本月单量' ";
		strSql += ",IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本年单量' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '本日<br>平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本周平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本月平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(totalSQL);

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("dataTXHOne"), dataTXHOne);
		queryDataMap.put(new String("dataTXHTwo1"), dataTXHTwo1);
		queryDataMap.put(new String("dataTXHTwo2"), dataTXHTwo2);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======营业收入趋势分析报表导出Excel==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepTrendExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String storeCode = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandName = map.get("BrandName");

		String strSql = "";
		String totalSQL = "";

		// 获取日期中月份

		String strYear = "";
		String strMonth = "";
		String strDay = "";

		strYear = businessDateStart.split("-")[0];
		strMonth = businessDateStart.split("-")[1];
		strDay = businessDateStart.split("-")[2];

		int CurMonth = Integer.parseInt(strMonth);
		String strSqlTemp1 = "";
		String strSqlTemp2 = "";
		String strSqlTemp3 = "";

		for (int i = 1; i <= CurMonth; i++) {
			if (i == CurMonth) {

				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + CurMonth + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode))";
				strSqlTemp3 += " / SUM(fn_addup_month_orders ('" + businessDateStart
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + CurMonth + "月'";

			} else {
				strSqlTemp1 += ",IFNULL(CAST(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp2 += ",IFNULL(CAST(SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0) AS '" + i + "月'";

				strSqlTemp3 += ",IFNULL(ROUND(SUM(fn_calculate_month_sales ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)) ";
				strSqlTemp3 += " / SUM(fn_calculate_month_orders ('" + (strYear + "-" + i + "-01")
						+ "',A.companyCode,A.brandCode,A.storeCode)),2),0) AS '" + i + "月'";
			}
		}

		strSqlTemp1 = strSqlTemp1.substring(1, strSqlTemp1.length());
		strSqlTemp2 = strSqlTemp2.substring(1, strSqlTemp2.length());
		strSqlTemp3 = strSqlTemp3.substring(1, strSqlTemp3.length());

		strSql += "SELECT ";
		strSql += strSqlTemp1;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第一张图标SQL:" + strSql);

		PageModel dataTXHOnePageModel = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHOnePageModel);
		List<Object> dataTXHOne = page.getResultList();

		// 第二张图形化 单量数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp2;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-1张图标SQL:" + strSql);

		PageModel dataTXHTwoPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHTwoPageModel);
		List<Object> dataTXHTwo1 = page.getResultList();

		// 第2-2张图形化 客单价数据源
		strSql = "";
		strSql += "SELECT ";
		strSql += strSqlTemp3;
		strSql += " FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";
		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info("第2-2张图标SQL:" + strSql);

		PageModel dataTXHThPageModel = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, dataTXHThPageModel);
		List<Object> dataTXHTwo2 = page.getResultList();

		// 明细数据
		strSql = "";
		totalSQL = "";
		strSql += " SELECT A.storeCodeName AS '门店' ";
		strSql += " 			,A.provinceName AS '省份' ";
		strSql += " 			,A.cityName AS '城市' ";
		strSql += " 			,A.regionName AS '地区' ";
		strSql += " 			,A.storeTypeName AS '门店类型' ";

		strSql += " 			,fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '本日<br>实际收入' ";
		strSql += " 			,fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本周实际收入' ";
		strSql += " 			,fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本月实际收入' ";
		strSql += " 			,fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本年实际收入' ";
		strSql += " 			,fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '本日单量' ";
		strSql += " 			,fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本周单量' ";
		strSql += " 			,fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本月单量' ";
		strSql += " 			,fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS '截止本日<br>本年单量' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '本日<br>平均客单价' ";

		strSql += " 			,IFNULL(ROUND(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本周平均客单价' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本月平均客单价' ";
		strSql += " 			,IFNULL(ROUND(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) ";
		strSql += " 					/ fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode),2),0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		strSql += " ORDER BY A.brandCodeName,A.areaName,A.storeCodeName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'合计' AS '门店' ";
		strSql += ",'' AS '省份' ";
		strSql += ",'' AS '城市' ";
		strSql += ",'' AS '地区'";
		strSql += ",'' AS '门店类型' ";

		strSql += ",IFNULL(CAST(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '本日<br>实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本周实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本月实际收入' ";
		strSql += ",IFNULL(CAST(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) AS CHAR),0)  AS '截止本日<br>本年实际收入' ";
		strSql += ",IFNULL(SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '本日单量' ";
		strSql += ",IFNULL(SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本周单量' ";
		strSql += ",IFNULL(SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本月单量' ";
		strSql += ",IFNULL(SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ,0)  AS '截止本日<br>本年单量' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_day_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_day_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '本日<br>平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_week_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_week_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本周平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_month_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_month_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本月平均客单价' ";
		strSql += ",IFNULL(CAST(ROUND(SUM(fn_addup_year_sales ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)) ";
		strSql += " 					/ SUM(fn_addup_year_orders ('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)),2) AS CHAR) ,0) AS '截止本日<br>本年平均客单价' ";
		strSql += "  FROM vrep_dict_brand_area_store AS A ";

		strSql += "   WHERE A.companyCode = '" + companyCode + "'";

		// 门店
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(totalSQL);

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("dataTXHOne"), dataTXHOne);
		queryDataMap.put(new String("dataTXHTwo1"), dataTXHTwo1);
		queryDataMap.put(new String("dataTXHTwo2"), dataTXHTwo2);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======营业收入环比分析报表========================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessHB(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");

		String strSql = "";
		String totalSQL = "";

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_brand");
		mapPam.put(new String("sqlWhere"), "where companyCode='" + companyCode + "'");

		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(areaCode,'') AS areaCode  FROM vrep_dict_brand ";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}

		logger.info("vrep_dict_brand:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String brandCode = mapBrand.get("brandCode");
			String brandCodeName = mapBrand.get("brandCodeName");

			// 明细数据
			strSql = "";
			totalSQL = "";
			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCode + "','|=|','" + brandCodeName + "'" + ",'|=|','" + areaCode
					+ "'),'') AS '品牌' ";
			strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
			strSql += ",IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";
			strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量' ";
			strSql += ",IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
			strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
			strSql += ",IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
			strSql += " - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2))";
			strSql += " / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ,CHAR (100)) ,'%'),'--') AS '日客单价环比'";
			strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
			strSql += ",IFNULL(SUM(lastWeekSales),0) AS '上周实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";
			strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
			strSql += ",IFNULL(SUM(lastWeekOrders),0) AS '上周单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比'";
			strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
			strSql += ",IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
			strSql += "  - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
			strSql += " / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100)),'%'),'--') AS '周客单价环比'";
			strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
			strSql += ",IFNULL(SUM(lastMonthSales),0) AS '上月实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";
			strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
			strSql += ",IFNULL(SUM(lastMonthOrders),0) AS '上月单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比'";
			strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
			strSql += ",IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
			strSql += " - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
			strSql += " / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
			strSql += ",CHAR (100)),'%'),'--') AS '月客单价环比'";
			strSql += " FROM (";
			strSql += " SELECT A.brandCodeName";
			strSql += ",fn_calculate_day_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
			strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
			strSql += ",fn_calculate_week_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
			strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
			strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

			strSql += ",fn_calculate_day_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
			strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
			strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
			strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
			strSql += " FROM vrep_dict_brand_area_store AS A";
			strSql += " WHERE A.companyCode = '" + companyCode + "'";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}

			strSql += "  AND A.brandCode = '" + brandCode + "'";
			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += " Select '合计' AS '品牌' ";
		strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += ",IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";
		strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += ",IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += ",IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
		strSql += " - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2))";
		strSql += " / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ,CHAR (100)) ,'%'),'--') AS '日客单价环比'";
		strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
		strSql += ",IFNULL(SUM(lastWeekSales),0) AS '上周实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";
		strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
		strSql += ",IFNULL(SUM(lastWeekOrders),0) AS '上周单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比'";
		strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
		strSql += ",IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
		strSql += "  - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += " / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100)),'%'),'--') AS '周客单价环比'";
		strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
		strSql += ",IFNULL(SUM(lastMonthSales),0) AS '上月实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";
		strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
		strSql += ",IFNULL(SUM(lastMonthOrders),0) AS '上月单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比'";
		strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
		strSql += ",IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
		strSql += " - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += " / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += ",CHAR (100)),'%'),'--') AS '月客单价环比'";
		strSql += " FROM (";
		strSql += " SELECT A.brandCodeName";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}
		strSql += " ) AS M ";

		totalSQL = "";

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
	 * ======营业收入环比分析点击品牌报表====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessHBBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");

		logger.info("AreaCode:" + AreaCode);

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";
		strSql += "SELECT areaCode,areaName FROM vrep_dict_brand_area_store";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";
		strSql += " GROUP BY areaCode,areaName ORDER BY areaName ";

		logger.info("vrep_dict_brand_area_store:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_areacode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");

			String isStore = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT storeCode FROM vrep_dict_brand_area_store ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND areaCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isStoreData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isStoreData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isStoreData.get(k);
				String storeCode = mapTemoS.get("storeCode");
				if (storeCode != null && !storeCode.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '区域' ";
			strSql += "      ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
			strSql += "      ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
			strSql += "      ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
			strSql += "      ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
			strSql += "			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
			strSql += "			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
			strSql += "      ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
			strSql += "                                - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
			strSql += "                                / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
			strSql += "										 ,CHAR (100)) ";
			strSql += "						 ,'%'),'--') AS '日客单价环比' ";
			strSql += "			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
			strSql += "			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
			strSql += "			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
			strSql += "			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
			strSql += "			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
			strSql += "			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
			strSql += "                                - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
			strSql += "                                / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
			strSql += "										 ,CHAR (100)) ";
			strSql += "						 ,'%'),'--') AS '周客单价环比' ";
			strSql += "			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
			strSql += "			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
			strSql += "			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
			strSql += "			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
			strSql += "      ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
			strSql += "			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
			strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
			strSql += "                                - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
			strSql += "                                / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
			strSql += "										 ,CHAR (100)) ";
			strSql += "						 ,'%'),'--') AS '月客单价环比' ";
			strSql += " FROM  ";
			strSql += " ( ";
			strSql += " SELECT A.brandCodeName ";
			strSql += "       ,A.areaName ";
			strSql += ",fn_calculate_day_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
			strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
			strSql += ",fn_calculate_week_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
			strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
			strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

			strSql += ",fn_calculate_day_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
			strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
			strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
			strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
			strSql += " FROM vrep_dict_brand_area_store AS A";
			strSql += " WHERE A.companyCode = '" + companyCode + "'";
			strSql += "   AND A.brandCode = '" + BrandCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}

			strSql += "   AND A.areaCode IN (" + allChildrenCode + ") ";
			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT '合计' AS '品牌' ";
		strSql += " ,'' AS '区域' ";
		strSql += "      ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += "      ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += "      ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
		strSql += "      ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += "			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += "			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += "      ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
		strSql += "                                - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
		strSql += "                                / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
		strSql += "										 ,CHAR (100)) ";
		strSql += "						 ,'%'),'--') AS '日客单价环比' ";
		strSql += "			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
		strSql += "			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
		strSql += "			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
		strSql += "			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
		strSql += "			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
		strSql += "			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
		strSql += "                                - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += "                                / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
		strSql += "										 ,CHAR (100)) ";
		strSql += "						 ,'%'),'--') AS '周客单价环比' ";
		strSql += "			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
		strSql += "			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
		strSql += "			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
		strSql += "			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
		strSql += "      ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
		strSql += "			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
		strSql += "			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
		strSql += "                                - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += "                                / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += "										 ,CHAR (100)) ";
		strSql += "						 ,'%'),'--') AS '月客单价环比' ";
		strSql += " FROM  ";
		strSql += " ( ";
		strSql += " SELECT A.brandCodeName ";
		strSql += "       ,A.areaName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "   AND A.brandCode = '" + BrandCode + "' ";
		strSql += " ) AS M ";

		totalSQL = "";

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
	 * ======营业收入环比分析点击区域门店报表==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";

		strSql += " SELECT brandCodeName AS '所属品牌' ";
		strSql += "       ,areaName AS '所属区域' ";
		strSql += "       ,provinceName AS '省份' ";
		strSql += "       ,cityName AS '城市' ";
		strSql += "       ,regionName AS '区县' ";
		strSql += "       ,storeCodeName AS '门店' ";
		strSql += "       ,storeTypeName AS '门店类型' ";

		strSql += "       ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += "       ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += "       ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
		strSql += "       ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += "       ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '日客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
		strSql += " 			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '周客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
		strSql += " 			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
		strSql += " 			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
		strSql += "       ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '月客单价环比' ";
		strSql += " FROM  ";
		strSql += " ( ";
		strSql += " SELECT A.brandCodeName ";
		strSql += "       ,A.areaName ";
		strSql += "       ,A.provinceName ";
		strSql += "       ,A.cityName ";
		strSql += "       ,A.regionName ";
		strSql += "       ,A.storeCodeName ";
		strSql += "       ,A.storeTypeName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		strSql += "   AND A.brandCode = '" + BrandCode + "' ";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";
		strSql += " ) AS M ";
		strSql += " GROUP BY brandCodeName,areaName,provinceName,cityName,regionName,storeCodeName ";

		totalSQL = "";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPageTemp = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPageTemp);

		strSql = "";
		strSql += " SELECT '合计' AS '所属品牌' ";
		strSql += "       ,'' AS '所属区域' ";
		strSql += "       ,'' AS '省份' ";
		strSql += "       ,'' AS '城市' ";
		strSql += "       ,'' AS '区县' ";
		strSql += "       ,'' AS '门店' ";
		strSql += "       ,'' AS '门店类型' ";
		strSql += "       ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += "       ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += "       ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
		strSql += "       ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += "       ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '日客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
		strSql += " 			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '周客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
		strSql += " 			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
		strSql += " 			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
		strSql += "       ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '月客单价环比' ";
		strSql += " FROM  ";
		strSql += " ( ";
		strSql += " SELECT A.brandCodeName ";
		strSql += "       ,A.areaName ";
		strSql += "       ,A.provinceName ";
		strSql += "       ,A.cityName ";
		strSql += "       ,A.regionName ";
		strSql += "       ,A.storeCodeName ";
		strSql += "       ,A.storeTypeName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		strSql += "   AND A.brandCode = '" + BrandCode + "' ";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "   AND A.areaCode = '" + AreaCode + "' ";
		strSql += " ) AS M ";

		totalSQL = "";

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
	 * ======营业收入环比分析点击区域门店报表导出Excel===========================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";

		strSql += " SELECT brandCodeName AS '所属品牌' ";
		strSql += "       ,areaName AS '所属区域' ";
		strSql += "       ,provinceName AS '省份' ";
		strSql += "       ,cityName AS '城市' ";
		strSql += "       ,regionName AS '区县' ";
		strSql += "       ,storeCodeName AS '门店' ";
		strSql += "       ,storeTypeName AS '门店类型' ";

		strSql += "       ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += "       ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += "       ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
		strSql += "       ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += "       ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '日客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
		strSql += " 			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '周客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
		strSql += " 			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
		strSql += " 			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
		strSql += "       ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '月客单价环比' ";
		strSql += " FROM  ";
		strSql += " ( ";
		strSql += " SELECT A.brandCodeName ";
		strSql += "       ,A.areaName ";
		strSql += "       ,A.provinceName ";
		strSql += "       ,A.cityName ";
		strSql += "       ,A.regionName ";
		strSql += "       ,A.storeCodeName ";
		strSql += "       ,A.storeTypeName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		// strSql += " AND A.brandCode = '" + BrandCode + "' ";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		// strSql += " AND A.areaCode = '" + AreaCode + "' ";
		strSql += " ) AS M ";
		strSql += " GROUP BY brandCodeName,areaName,provinceName,cityName,regionName,storeCodeName ";

		totalSQL = "";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPageTemp = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPageTemp);

		strSql = "";
		strSql += " SELECT '合计' AS '所属品牌' ";
		strSql += "       ,'' AS '所属区域' ";
		strSql += "       ,'' AS '省份' ";
		strSql += "       ,'' AS '城市' ";
		strSql += "       ,'' AS '区县' ";
		strSql += "       ,'' AS '门店' ";
		strSql += "       ,'' AS '门店类型' ";
		strSql += "       ,IFNULL(SUM(todaySales),0) AS '本日实际收入' ";
		strSql += "       ,IFNULL(SUM(lastWeekDaySales),0) AS '上周本日实际收入' ";
		strSql += "       ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比' ";
		strSql += "       ,IFNULL(SUM(todayOrders),0) AS '本日单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekDayOrders),0) AS '上周本日单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(lastWeekDayOrders)) / SUM(lastWeekDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价' ";
		strSql += "       ,IFNULL(ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),0) AS '上周本日客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekDaySales)/SUM(lastWeekDayOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '日客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入' ";
		strSql += " 			,IFNULL(SUM(lastWeekSales),0) AS '上周实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisWeekOrders),0) AS '本周单量' ";
		strSql += " 			,IFNULL(SUM(lastWeekOrders),0) AS '上周单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(lastWeekOrders)) / SUM(lastWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量环比' ";
		strSql += " 			,IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),0) AS '上周客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastWeekSales)/SUM(lastWeekOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '周客单价环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthSales),0) AS '本月实际收入' ";
		strSql += " 			,IFNULL(SUM(lastMonthSales),0) AS '上月实际收入' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比' ";
		strSql += " 			,IFNULL(SUM(thisMonthOrders),0) AS '本月单量' ";
		strSql += " 			,IFNULL(SUM(lastMonthOrders),0) AS '上月单量' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(lastMonthOrders)) / SUM(lastMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量环比' ";
		strSql += "       ,IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价' ";
		strSql += " 			,IFNULL(ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),0) AS '上月客单价' ";
		strSql += " 			,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2) ";
		strSql += "                                 - ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2)) ";
		strSql += "                                 / ROUND(SUM(lastMonthSales)/SUM(lastMonthOrders),2),4)* 100,2) ";
		strSql += " 										 ,CHAR (100)) ";
		strSql += " 						 ,'%'),'--') AS '月客单价环比' ";
		strSql += " FROM  ";
		strSql += " ( ";
		strSql += " SELECT A.brandCodeName ";
		strSql += "       ,A.areaName ";
		strSql += "       ,A.provinceName ";
		strSql += "       ,A.cityName ";
		strSql += "       ,A.regionName ";
		strSql += "       ,A.storeCodeName ";
		strSql += "       ,A.storeTypeName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDaySales'";
		strSql += ",fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthSales'";

		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 WEEK),A.companyCode,A.brandCode,A.storeCode) AS 'lastWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders(DATE_ADD('" + businessDateStart
				+ "', INTERVAL - 1 MONTH),A.companyCode,A.brandCode,A.storeCode) AS 'lastMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		// strSql += " AND A.brandCode = '" + BrandCode + "' ";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		// strSql += " AND A.areaCode = '" + AreaCode + "' ";
		strSql += " ) AS M ";

		totalSQL = "";

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
	 * ======营业收入同比分析报表========================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTB(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandName = map.get("BrandName");
		String BrandCode = map.get("BrandCode");

		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		businessDateEnd = TBTimeStart;

		String strSql = "";
		String totalSQL = "";
		// 饼图内圈数据
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
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
		strSql += " ,SUM(sDiscount) AS 'sDiscount'";
		strSql += " ,SUM(sNetAmountPrice) AS 'sNetAmountPrice'";
		strSql += " ,ROUND(SUM(sNetAmountPrice)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		strSql += " GROUP BY C.orderTypeDesc";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_brand");
		mapPam.put(new String("sqlWhere"), "where companyCode='" + companyCode + "'");

		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(areaCode,'') AS areaCode  FROM vrep_dict_brand ";
		strSql += " WHERE companyCode = '" + companyCode + "'";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}

		logger.info("vrep_dict_brand:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String brandCode = mapBrand.get("brandCode");
			String brandCodeName = mapBrand.get("brandCodeName");

			// 明细数据
			strSql = "";
			totalSQL = "";
			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCode + "','|=|','" + brandCodeName + "'" + ",'|=|','" + areaCode
					+ "'),'') AS '品牌' ";

			strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
			strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
			strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
			strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
			strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
			strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
			strSql += " - ROUND(SUM(compDaySales)/SUM(compDayOrders),2))";
			strSql += " / ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100))";
			strSql += "	,'%'),'--') AS '日客单价同比'";
			strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
			strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
			strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
			strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
			strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
			strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
			strSql += "	- ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2))";
			strSql += "	/ ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100))";
			strSql += " ,'%'),'--') AS '周客单价同比'";
			strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
			strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
			strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
			strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
			strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
			strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
			strSql += "  - ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
			strSql += "  / ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
			strSql += ",CHAR (100))";
			strSql += " ,'%'),'--') AS '月客单价同比'";
			strSql += " FROM (";
			strSql += " SELECT A.brandCodeName";
			strSql += ",A.areaName";
			strSql += ",fn_calculate_day_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
			strSql += ",fn_calculate_day_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
			strSql += " ,fn_calculate_week_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
			strSql += ",fn_calculate_week_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
			strSql += ",fn_calculate_day_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
			strSql += ",fn_calculate_day_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
			strSql += " FROM vrep_dict_brand_area_store AS A";
			strSql += " WHERE A.companyCode = '" + companyCode + "'";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}
			strSql += "  AND A.brandCode = '" + brandCode + "'";
			strSql += " ) AS M";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += " Select '合计' AS '品牌' ";
		strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
		strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
		strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
		strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
		strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
		strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
		strSql += " - ROUND(SUM(compDaySales)/SUM(compDayOrders),2))";
		strSql += " / ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100))";
		strSql += "	,'%'),'--') AS '日客单价同比'";
		strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
		strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
		strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
		strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
		strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
		strSql += "	- ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2))";
		strSql += "	/ ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100))";
		strSql += " ,'%'),'--') AS '周客单价同比'";
		strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
		strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
		strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
		strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
		strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
		strSql += "  - ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
		strSql += "  / ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
		strSql += ",CHAR (100))";
		strSql += " ,'%'),'--') AS '月客单价同比'";
		strSql += " FROM (";
		strSql += " SELECT A.brandCodeName";
		strSql += ",A.areaName";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
		strSql += " ,fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}
		strSql += " ) AS M";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("BTNData"), BTNData);
		queryDataMap.put(new String("ZZTData"), ZZTData);

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);
		return queryDataMap;
	}

	/*
	 * ======营业收入同比分析点击品牌报表=====================================================
	 * ===
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");

		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		businessDateEnd = TBTimeStart;

		logger.info("AreaCode:" + AreaCode);
		logger.info("StoreCode:" + StoreCode);

		String strSql = "";
		String totalSQL = "";
		// 明细数据

		strSql = "";
		totalSQL = "";
		strSql += "SELECT areaCode,areaName FROM vrep_dict_brand_area_store";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";
		strSql += " GROUP BY areaCode,areaName ORDER BY areaName ";

		logger.info("vrep_dict_brand_area_store:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_areacode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");
			String isStore = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT storeCode FROM vrep_dict_brand_area_store ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND areaCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isStoreData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isStoreData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isStoreData.get(k);
				String storeCode = mapTemoS.get("storeCode");
				if (storeCode != null && !storeCode.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '区域' ";

			strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
			strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
			strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
			strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
			strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
			strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
			strSql += " - ROUND(SUM(compDaySales)/SUM(compDayOrders),2))";
			strSql += " / ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100)),'%'),'--') AS '日客单价同比'";
			strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
			strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
			strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
			strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
			strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
			strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
			strSql += " - ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2)) ";
			strSql += " / ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100)),'%'),'--') AS '周客单价同比'";
			strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
			strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
			strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
			strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
			strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
			strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
			strSql += "- ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
			strSql += "/ ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
			strSql += " ,CHAR (100)),'%'),'--') AS '月客单价同比'";
			strSql += " FROM (";
			strSql += " SELECT A.brandCodeName";
			strSql += " ,A.areaName";
			strSql += ",fn_calculate_day_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
			strSql += ",fn_calculate_day_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
			strSql += " ,fn_calculate_week_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
			strSql += ",fn_calculate_week_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
			strSql += ",fn_calculate_month_sales('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
			strSql += ",fn_calculate_day_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
			strSql += ",fn_calculate_day_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
			strSql += ",fn_calculate_week_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateStart
					+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
			strSql += ",fn_calculate_month_orders('" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
			strSql += " FROM vrep_dict_brand_area_store AS A";
			strSql += " WHERE A.companyCode = '" + companyCode + "'";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}

			strSql += " AND A.brandCode = '" + BrandCode + "'";
			strSql += " AND A.areaCode IN (" + allChildrenCode + ")";

			strSql += " ) AS M";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT '合计' AS '品牌' ";
		strSql += " ,'' AS '区域' ";

		strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
		strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
		strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
		strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
		strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
		strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
		strSql += " - ROUND(SUM(compDaySales)/SUM(compDayOrders),2))";
		strSql += " / ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100)),'%'),'--') AS '日客单价同比'";
		strSql += ",IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
		strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
		strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
		strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
		strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
		strSql += " - ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2)) ";
		strSql += " / ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100)),'%'),'--') AS '周客单价同比'";
		strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
		strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
		strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
		strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
		strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
		strSql += "- ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
		strSql += "/ ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
		strSql += " ,CHAR (100)),'%'),'--') AS '月客单价同比'";
		strSql += " FROM (";
		strSql += " SELECT A.brandCodeName";
		strSql += " ,A.areaName";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
		strSql += " ,fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		strSql += " AND A.brandCode = '" + BrandCode + "'";

		strSql += " ) AS M";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// queryDataMap.put(new String("BTNData"), BTNData);
		// queryDataMap.put(new String("ZZTData"), ZZTData);

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======营业收入同比分析点击区域门店报表==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		businessDateEnd = TBTimeStart;

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";

		strSql += " SELECT brandCodeName AS '所属品牌' ";
		strSql += "       ,areaName AS '所属区域' ";
		strSql += "       ,provinceName AS '省份' ";
		strSql += "       ,cityName AS '城市' ";
		strSql += "       ,regionName AS '区县' ";
		strSql += "       ,storeCodeName AS '门店' ";
		strSql += "       ,storeTypeName AS '门店类型' ";
		strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
		strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
		strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
		strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
		strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
		strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
		strSql += "  - ROUND(SUM(compDaySales)/SUM(compDayOrders),2)) ";
		strSql += "/ ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)) ,'%'),'--') AS '日客单价同比'";
		strSql += " ,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
		strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
		strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
		strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
		strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
		strSql += "- ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2))";
		strSql += "/ ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)),'%'),'--') AS '周客单价同比'";
		strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
		strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
		strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
		strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
		strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
		strSql += "- ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
		strSql += "/ ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)),'%'),'--') AS '月客单价同比'";
		strSql += " FROM  (";
		strSql += " SELECT A.brandCodeName";
		strSql += ",A.areaName";
		strSql += ",A.provinceName";
		strSql += ",A.cityName";
		strSql += ",A.regionName";
		strSql += ",A.storeCodeName";
		strSql += ",A.storeTypeName ";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
		strSql += " ,fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "  AND A.brandCode = '" + BrandCode + "'";
		strSql += "  AND A.areaCode = '" + AreaCode + "'";

		strSql += " ) AS M";
		strSql += " GROUP BY brandCodeName,areaName,provinceName,cityName,regionName,storeCodeName";

		totalSQL = "";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPageTemp = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPageTemp);

		strSql = "";
		strSql += " SELECT '合计' AS '所属品牌' ";
		strSql += "       ,'' AS '所属区域' ";
		strSql += "       ,'' AS '省份' ";
		strSql += "       ,'' AS '城市' ";
		strSql += "       ,'' AS '区县' ";
		strSql += "       ,'' AS '门店' ";
		strSql += "       ,'' AS '门店类型' ";
		strSql += ",IFNULL(SUM(todaySales),0) AS '本日实际收入'";
		strSql += ",IFNULL(SUM(compDaySales),0) AS '同比日实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todaySales) - SUM(compDaySales)) / SUM(compDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入同比'";
		strSql += ",IFNULL(SUM(todayOrders),0) AS '本日单量'";
		strSql += ",IFNULL(SUM(compDayOrders),0) AS '同比日单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(todayOrders) - SUM(compDayOrders)) / SUM(compDayOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '日单量同比'";
		strSql += ",IFNULL(ROUND(SUM(todaySales)/SUM(todayOrders),2),0) AS '本日客单价'";
		strSql += ",IFNULL(ROUND(SUM(compDaySales)/SUM(compDayOrders),2),0) AS '同比日客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(todaySales)/SUM(todayOrders),2)";
		strSql += "  - ROUND(SUM(compDaySales)/SUM(compDayOrders),2)) ";
		strSql += "/ ROUND(SUM(compDaySales)/SUM(compDayOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)) ,'%'),'--') AS '日客单价同比'";
		strSql += " ,IFNULL(SUM(thisWeekSales),0) AS '本周实际收入'";
		strSql += ",IFNULL(SUM(compWeekSales),0) AS '同比周实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekSales) - SUM(compWeekSales)) / SUM(compWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入同比'";
		strSql += ",IFNULL(SUM(thisWeekOrders),0) AS '本周单量'";
		strSql += ",IFNULL(SUM(compWeekOrders),0) AS '同比周单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisWeekOrders) - SUM(compWeekOrders)) / SUM(compWeekOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '周单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2),0) AS '本周客单价'";
		strSql += ",IFNULL(ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),0) AS '上周客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisWeekSales)/SUM(thisWeekOrders),2)";
		strSql += "- ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2))";
		strSql += "/ ROUND(SUM(compWeekSales)/SUM(compWeekOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)),'%'),'--') AS '周客单价同比'";
		strSql += ",IFNULL(SUM(thisMonthSales),0) AS '本月实际收入'";
		strSql += ",IFNULL(SUM(compMonthSales),0) AS '同比月实际收入'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthSales) - SUM(compMonthSales)) / SUM(compMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入同比'";
		strSql += ",IFNULL(SUM(thisMonthOrders),0) AS '本月单量'";
		strSql += ",IFNULL(SUM(compMonthOrders),0) AS '同比月单量'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(thisMonthOrders) - SUM(compMonthOrders)) / SUM(compMonthOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '月单量同比'";
		strSql += ",IFNULL(ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2),0) AS '本月客单价'";
		strSql += ",IFNULL(ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),0) AS '同比月客单价'";
		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(thisMonthSales)/SUM(thisMonthOrders),2)";
		strSql += "- ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2))";
		strSql += "/ ROUND(SUM(compMonthSales)/SUM(compMonthOrders),2),4)* 100,2)";
		strSql += ",CHAR (100)),'%'),'--') AS '月客单价同比'";
		strSql += " FROM  (";
		strSql += " SELECT A.brandCodeName";
		strSql += ",A.areaName";
		strSql += ",A.provinceName";
		strSql += ",A.cityName";
		strSql += ",A.regionName";
		strSql += ",A.storeCodeName";
		strSql += ",fn_calculate_day_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todaySales'";
		strSql += ",fn_calculate_day_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDaySales'";
		strSql += " ,fn_calculate_week_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisWeekSales'";
		strSql += ",fn_calculate_week_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'thisMonthSales'";
		strSql += ",fn_calculate_month_sales('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthSales'";
		strSql += ",fn_calculate_day_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'todayOrders'";
		strSql += ",fn_calculate_day_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compDayOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisWeekOrders'";
		strSql += ",fn_calculate_week_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compWeekOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateStart
				+ "',A.companyCode,A.brandCode,A.storeCode)  AS 'thisMonthOrders'";
		strSql += ",fn_calculate_month_orders('" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'compMonthOrders'";
		strSql += " FROM vrep_dict_brand_area_store AS A";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "  AND A.brandCode = '" + BrandCode + "'";
		strSql += "  AND A.areaCode = '" + AreaCode + "'";

		strSql += " ) AS M";
		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析报表===============================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBDate(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandName = map.get("BrandName");
		String BrandCode = map.get("BrandCode");

		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		String strSql = "";
		String totalSQL = "";
		// 饼图内圈数据
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
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
		strSql += " ,SUM(sDiscount) AS 'sDiscount'";
		strSql += " ,SUM(sNetAmountPrice) AS 'sNetAmountPrice'";
		strSql += " ,ROUND(SUM(sNetAmountPrice)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		strSql += " GROUP BY C.orderTypeDesc";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_brand");
		mapPam.put(new String("sqlWhere"), "where companyCode='" + companyCode + "'");

		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(areaCode,'') AS areaCode  FROM vrep_dict_brand ";
		strSql += " WHERE companyCode = '" + companyCode + "'";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}

		logger.info("vrep_dict_brand:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String brandCode = mapBrand.get("brandCode");
			String brandCodeName = mapBrand.get("brandCodeName");

			// 明细数据
			strSql = "";
			totalSQL = "";
			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCode + "','|=|','" + brandCodeName + "'" + ",'|=|','" + areaCode
					+ "'),'') AS '品牌' ";

			strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
			strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
			strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
			strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
			strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
			strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
			strSql += " FROM ";
			strSql += " 	( ";
			strSql += " 		SELECT A.brandCodeName ";
			strSql += "           ,A.areaName ";
			strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
					+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
			strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
			strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
			strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
			strSql += " 		 FROM vrep_dict_brand_area_store AS A ";
			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}
			strSql += "  AND A.brandCode = '" + brandCode + "'";
			strSql += " 	) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += " Select '合计' AS '品牌' ";

		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 		SELECT A.brandCodeName ";
		strSql += "           ,A.areaName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += " 	) AS M ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("BTNData"), BTNData);
		queryDataMap.put(new String("ZZTData"), ZZTData);

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);
		return queryDataMap;
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击品牌报表===========================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");

		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		logger.info("AreaCode:" + AreaCode);
		logger.info("StoreCode:" + StoreCode);

		String strSql = "";
		String totalSQL = "";
		// 明细数据

		strSql = "";
		totalSQL = "";
		strSql += "SELECT areaCode,areaName FROM vrep_dict_brand_area_store";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";
		strSql += " GROUP BY areaCode,areaName ORDER BY areaName ";

		logger.info("vrep_dict_brand_area_store:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_areacode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");
			String isStore = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT storeCode FROM vrep_dict_brand_area_store ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND areaCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isStoreData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isStoreData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isStoreData.get(k);
				String storeCode = mapTemoS.get("storeCode");
				if (storeCode != null && !storeCode.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '区域' ";

			strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
			strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
			strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
			strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
			strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
			strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
			strSql += " FROM ";
			strSql += " 	( ";
			strSql += " 		SELECT A.brandCodeName ";
			strSql += "           ,A.areaName ";
			strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
					+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
			strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
			strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
			strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
					+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
			strSql += " 		 FROM vrep_dict_brand_area_store AS A ";
			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}
			strSql += "  AND A.brandCode = '" + BrandCode + "'";
			strSql += " AND A.areaCode IN (" + allChildrenCode + ")";

			strSql += " 	) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT '合计' AS '品牌' ";
		strSql += " ,'' AS '区域' ";

		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 		SELECT A.brandCodeName ";
		strSql += "           ,A.areaName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "  AND A.brandCode = '" + BrandCode + "'";
		strSql += " 	) AS M ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// queryDataMap.put(new String("BTNData"), BTNData);
		// queryDataMap.put(new String("ZZTData"), ZZTData);

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表=========================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";

		strSql += " SELECT ";
		strSql += " 	brandCodeName AS '所属品牌' ";
		strSql += " 	,areaName AS '所属区域' ";
		strSql += " 	,provinceName AS '省份' ";
		strSql += " 	,cityName AS '城市' ";
		strSql += " 	,regionName AS '区县' ";
		strSql += " 	,storeCodeName AS '门店' ";
		strSql += " 	,storeTypeName AS '门店类型' ";

		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 	  SELECT A.brandCodeName ";
		strSql += " 					,A.areaName ";
		strSql += " 					,A.provinceName ";
		strSql += " 					,A.cityName ";
		strSql += " 					,A.regionName ";
		strSql += " 					,A.storeCodeName ";
		strSql += " 					,A.storeTypeName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "  AND A.brandCode = '" + BrandCode + "'";
		strSql += "  AND A.areaCode = '" + AreaCode + "'";

		strSql += " 	) AS M ";
		strSql += " GROUP BY ";
		strSql += " 	brandCodeName, ";
		strSql += " 	areaName, ";
		strSql += " 	provinceName, ";
		strSql += " 	cityName, ";
		strSql += " 	regionName, ";
		strSql += " 	storeCodeName ";

		totalSQL = "";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPageTemp = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPageTemp);

		strSql = "";
		strSql += " SELECT '合计' AS '所属品牌' ";
		strSql += "       ,'' AS '所属区域' ";
		strSql += "       ,'' AS '省份' ";
		strSql += "       ,'' AS '城市' ";
		strSql += "       ,'' AS '区县' ";
		strSql += "       ,'' AS '门店' ";
		strSql += "       ,'' AS '门店类型' ";
		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 	  SELECT A.brandCodeName ";
		strSql += " 					,A.areaName ";
		strSql += " 					,A.provinceName ";
		strSql += " 					,A.cityName ";
		strSql += " 					,A.regionName ";
		strSql += " 					,A.storeCodeName ";
		strSql += " 					,A.storeTypeName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		strSql += "  AND A.brandCode = '" + BrandCode + "'";
		strSql += "  AND A.areaCode = '" + AreaCode + "'";

		strSql += " 	) AS M ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表导出Excel==================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";

		strSql += " SELECT ";
		strSql += " 	brandCodeName AS '所属品牌' ";
		strSql += " 	,areaName AS '所属区域' ";
		strSql += " 	,provinceName AS '省份' ";
		strSql += " 	,cityName AS '城市' ";
		strSql += " 	,regionName AS '区县' ";
		strSql += " 	,storeCodeName AS '门店' ";
		strSql += " 	,storeTypeName AS '门店类型' ";

		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 	  SELECT A.brandCodeName ";
		strSql += " 					,A.areaName ";
		strSql += " 					,A.provinceName ";
		strSql += " 					,A.cityName ";
		strSql += " 					,A.regionName ";
		strSql += " 					,A.storeCodeName ";
		strSql += " 					,A.storeTypeName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		// strSql += " AND A.brandCode = '" + BrandCode + "'";
		// strSql += " AND A.areaCode = '" + AreaCode + "'";

		strSql += " 	) AS M ";
		strSql += " GROUP BY ";
		strSql += " 	brandCodeName, ";
		strSql += " 	areaName, ";
		strSql += " 	provinceName, ";
		strSql += " 	cityName, ";
		strSql += " 	regionName, ";
		strSql += " 	storeCodeName ";

		totalSQL = "";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPageTemp = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPageTemp);

		strSql = "";
		strSql += " SELECT '合计' AS '所属品牌' ";
		strSql += "       ,'' AS '所属区域' ";
		strSql += "       ,'' AS '省份' ";
		strSql += "       ,'' AS '城市' ";
		strSql += "       ,'' AS '区县' ";
		strSql += "       ,'' AS '门店' ";
		strSql += "       ,'' AS '门店类型' ";
		strSql += " 	,IFNULL(SUM(aPeriodSales), 0) AS '实际收入<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodSales), 0) AS '实际收入<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(aPeriodSales) - SUM(bPeriodSales)) / SUM(bPeriodSales),4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";
		strSql += " 	,IFNULL(SUM(aPeriodOrders), 0) AS '单量<br>分析时段' ";
		strSql += " 	,IFNULL(SUM(bPeriodOrders), 0) AS '单量<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(aPeriodOrders) - SUM(bPeriodOrders)) / SUM(bPeriodOrders),4) * 100,2),CHAR (100)),'%'),'--') AS '单量<br>同比增长率' ";
		strSql += " 	,IFNULL(ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2),0) AS '平均客单价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),0) AS '平均客单价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(aPeriodSales) / SUM(aPeriodOrders),2) - ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2))  ";
		strSql += "                        / ROUND(SUM(bPeriodSales) / SUM(bPeriodOrders),2),4) * 100,2),CHAR (100)),'%'),'--') AS '平均客单价<br>同比增长率' ";
		strSql += " FROM ";
		strSql += " 	( ";
		strSql += " 	  SELECT A.brandCodeName ";
		strSql += " 					,A.areaName ";
		strSql += " 					,A.provinceName ";
		strSql += " 					,A.cityName ";
		strSql += " 					,A.regionName ";
		strSql += " 					,A.storeCodeName ";
		strSql += " 					,A.storeTypeName ";
		strSql += " 					,fn_calculate_date_period_sales ('" + businessDateStart + "','"
				+ businessDateEnd + "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodSales' ";
		strSql += "           ,fn_calculate_date_period_orders ('" + businessDateStart + "','" + businessDateEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'aPeriodOrders' ";
		strSql += " 					,fn_calculate_date_period_sales ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodSales' ";
		strSql += " 					,fn_calculate_date_period_orders ('" + TBTimeStart + "','" + TBTimeEnd
				+ "',A.companyCode,A.brandCode,A.storeCode) AS 'bPeriodOrders' ";
		strSql += " 		 FROM vrep_dict_brand_area_store AS A ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}
		// strSql += " AND A.brandCode = '" + BrandCode + "'";
		// strSql += " AND A.areaCode = '" + AreaCode + "'";

		strSql += " 	) AS M ";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		// queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======产品分类同比分析(品牌汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatTBBrand(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String CategoryCode = map.get("CategoryCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");
		String strSql = "";
		String totalSQL = "";

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_master_productcategory");
		mapPam.put(new String("sqlWhere"), "where level_ = 1 AND companyCode='" + companyCode + "'");

		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(productCategoryCode,'') AS areaCode  FROM vrep_dict_master_productcategory ";
		strSql += " WHERE level_ = 1 AND companyCode = '" + companyCode + "'";
		if (BrandCode != "" && !BrandCode.equals("ALL")) {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}

		strSql += " ORDER BY brandCodeName";

		logger.info("vrep_dict_master_productcategory:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String brandCode = mapBrand.get("brandCode");
			String brandCodeName = mapBrand.get("brandCodeName");

			// 查询销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS atodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS atodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + businessDateStart + "' AND businessDate<= '" + businessDateEnd
					+ "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("aSQL=" + strSql);

			PageModel pma = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pma);
			List<Object> dataProductA = page.getResultList();

			Map<String, String> mapA = new HashMap<String, String>();
			mapA = (Map<String, String>) dataProductA.get(0);
			String atodayNum = ((Object) mapA.get("atodayNum")).toString();
			String atodaySales = ((Object) mapA.get("atodaySales")).toString();

			// 查询同比产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS btodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS btodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + TBTimeStart + "' AND businessDate<= '" + TBTimeEnd + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("bSQL=" + strSql);
			PageModel pmb = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pmb);
			List<Object> dataProductB = page.getResultList();

			Map<String, String> mapB = new HashMap<String, String>();
			mapB = (Map<String, String>) dataProductB.get(0);
			String btodayNum = ((Object) mapB.get("btodayNum")).toString();
			String btodaySales = ((Object) mapB.get("btodaySales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCode + "','|=|','" + brandCodeName + "'" + ",'|=|','" + areaCode
					+ "'),'') AS '品牌' ";
			strSql += "	 ,IFNULL(SUM(atodaySales), 0) AS 'atodaySales' ";
			strSql += "	 ,IFNULL(SUM(btodaySales), 0) AS 'btodaySales' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(atodaySales) - SUM(btodaySales)) / SUM(btodaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodaySalesRate' ";
			strSql += "	 ,IFNULL(SUM(atodayNum), 0) AS 'atodayNum' ";
			strSql += "	 ,IFNULL(SUM(btodayNum), 0) AS 'btodayNum' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(atodayNum) - SUM(btodayNum)) / SUM(btodayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodayNumRate' ";

			strSql += " 	,IFNULL(ROUND(SUM(atodaySales) / SUM(atodayNum),2),0) AS '销售均价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(btodaySales) / SUM(btodayNum),2),0) AS '销售均价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(atodaySales) / SUM(atodayNum),2) - ROUND(SUM(btodaySales) / SUM(btodayNum),2))  ";
			strSql += "                        / ROUND(SUM(btodaySales) / SUM(btodayNum),2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

			strSql += " FROM ";
			strSql += "	( ";
			strSql += "		SELECT '" + brandCodeName + "' brandCodeName ";
			strSql += "," + atodayNum + " AS 'atodayNum' ";
			strSql += "," + atodaySales + " AS 'atodaySales' ";
			strSql += "," + btodayNum + " AS 'btodayNum' ";
			strSql += "," + btodaySales + " AS 'btodaySales' ";
			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int atodayNum = 0;
		int btodayNum = 0;

		double atodaySales = 0;
		double btodaySales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			atodayNum += Integer.parseInt(((Object) mapValue.get("atodayNum")).toString());
			atodaySales += Double.parseDouble(((Object) mapValue.get("atodaySales")).toString());

			btodayNum += Integer.parseInt(((Object) mapValue.get("btodayNum")).toString());
			btodaySales += Double.parseDouble(((Object) mapValue.get("btodaySales")).toString());

		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += " ,ROUND(" + atodaySales + ",2) AS '实际收入<br>分析时段' ";
		strSql += " ,ROUND(" + btodaySales + ",2) AS '实际收入<br>同比时段' ";

		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((" + atodaySales + " - " + btodaySales + ") / " + btodaySales
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";

		strSql += " ," + atodayNum + " AS '销量<br>分析时段' ";
		strSql += " ," + btodayNum + " AS '销量<br>同比时段' ";

		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + atodayNum + " - " + btodayNum + ") / " + btodayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '销量<br>同比增长率' ";

		strSql += " 	,IFNULL(ROUND(" + atodaySales + " / " + atodayNum + ",2),0) AS '销售均价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(" + btodaySales + " / " + btodayNum + ",2),0) AS '销售均价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(" + atodaySales + " / " + atodayNum + ",2) - ROUND("
				+ btodaySales + " / " + btodayNum + ",2))  ";
		strSql += "                        / ROUND(" + btodaySales + " / " + btodayNum
				+ ",2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

		logger.info(strSql);

		totalSQL = "";

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
	 * ======产品分类同比分析(分类汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatTBBrandChildren(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");

		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";
		strSql += "SELECT productCategoryCode AS areaCode,productCategoryCodeName AS areaName FROM vrep_dict_master_productcategory ";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";
		/*
		 * if (CategoryCode != "" && !CategoryCode.equals("ALL")) { CategoryCode
		 * = CategoryCode.replace("|", "','"); strSql +=
		 * " AND productCategoryCode in ('" + CategoryCode + "')"; }
		 */
		logger.info("vrep_dict_master_productcategory:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_categorycode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");

			String isLast = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT fn_CheckCategoryIsLast(companyCode,brandCode,productCategoryCode) AS IsLast FROM vrep_dict_master_productcategory  ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND productCategoryCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isLastData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isLastData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isLastData.get(k);
				isLast = mapTemoS.get("IsLast");
			}

			// 查询销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS atodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS atodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + businessDateStart + "' AND businessDate<= '" + businessDateEnd
					+ "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("aSQL=" + strSql);

			PageModel pma = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pma);
			List<Object> dataProductA = page.getResultList();

			Map<String, String> mapA = new HashMap<String, String>();
			mapA = (Map<String, String>) dataProductA.get(0);
			String atodayNum = ((Object) mapA.get("atodayNum")).toString();
			String atodaySales = ((Object) mapA.get("atodaySales")).toString();

			// 查询同比产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS btodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS btodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + TBTimeStart + "' AND businessDate<= '" + TBTimeEnd + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("bSQL=" + strSql);
			PageModel pmb = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pmb);
			List<Object> dataProductB = page.getResultList();

			Map<String, String> mapB = new HashMap<String, String>();
			mapB = (Map<String, String>) dataProductB.get(0);
			String btodayNum = ((Object) mapB.get("btodayNum")).toString();
			String btodaySales = ((Object) mapB.get("btodaySales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isLast + "'),'') AS '分类' ";

			strSql += "	 ,IFNULL(SUM(atodaySales), 0) AS 'atodaySales' ";
			strSql += "	 ,IFNULL(SUM(btodaySales), 0) AS 'btodaySales' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(atodaySales) - SUM(btodaySales)) / SUM(btodaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodaySalesRate' ";
			strSql += "	 ,IFNULL(SUM(atodayNum), 0) AS 'atodayNum' ";
			strSql += "	 ,IFNULL(SUM(btodayNum), 0) AS 'btodayNum' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(atodayNum) - SUM(btodayNum)) / SUM(btodayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodayNumRate' ";

			strSql += " 	,IFNULL(ROUND(SUM(atodaySales) / SUM(atodayNum),2),0) AS '销售均价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(btodaySales) / SUM(btodayNum),2),0) AS '销售均价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(atodaySales) / SUM(atodayNum),2) - ROUND(SUM(btodaySales) / SUM(btodayNum),2))  ";
			strSql += "                        / ROUND(SUM(btodaySales) / SUM(btodayNum),2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

			strSql += " FROM ";
			strSql += "	( ";
			strSql += "		SELECT '" + BrandName + "' brandCodeName ";
			strSql += "," + atodayNum + " AS 'atodayNum' ";
			strSql += "," + atodaySales + " AS 'atodaySales' ";
			strSql += "," + btodayNum + " AS 'btodayNum' ";
			strSql += "," + btodaySales + " AS 'btodaySales' ";

			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int atodayNum = 0;
		int btodayNum = 0;

		double atodaySales = 0;
		double btodaySales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			atodayNum += Integer.parseInt(((Object) mapValue.get("atodayNum")).toString());
			atodaySales += Double.parseDouble(((Object) mapValue.get("atodaySales")).toString());

			btodayNum += Integer.parseInt(((Object) mapValue.get("btodayNum")).toString());
			btodaySales += Double.parseDouble(((Object) mapValue.get("btodaySales")).toString());

		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += "  ,''  AS '分类' ";
		strSql += " ,ROUND(" + atodaySales + ",2) AS '实际收入<br>分析时段' ";
		strSql += " ,ROUND(" + btodaySales + ",2) AS '实际收入<br>同比时段' ";

		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((" + atodaySales + " - " + btodaySales + ") / " + btodaySales
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";

		strSql += " ," + atodayNum + " AS '销量<br>分析时段' ";
		strSql += " ," + btodayNum + " AS '销量<br>同比时段' ";

		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + atodayNum + " - " + btodayNum + ") / " + btodayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '销量<br>同比增长率' ";

		strSql += " 	,IFNULL(ROUND(" + atodaySales + " / " + atodayNum + ",2),0) AS '销售均价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(" + btodaySales + " / " + btodayNum + ",2),0) AS '销售均价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(" + atodaySales + " / " + atodayNum + ",2) - ROUND("
				+ btodaySales + " / " + btodayNum + ",2))  ";
		strSql += "                        / ROUND(" + btodaySales + " / " + btodayNum
				+ ",2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

		totalSQL = "";

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
	 * ======产品分类同比分析(产品汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");
		String strSql = "";
		String totalSQL = "";

		// 传入查询的末级分类下有哪些产品
		strSql = "";
		totalSQL = "";
		strSql += "SELECT A.productCode,A.productName,B.productCategoryCodeName ";
		strSql += "  FROM vrep_dict_product_all AS A ";
		strSql += "LEFT OUTER JOIN vrep_dict_master_productcategory AS B ON A.masterCategoryCode = B.productCategoryCode ";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		strSql += "   AND A.brandCode = '" + BrandCode + "'";
		strSql += "   AND A.masterCategoryCode = '" + AreaCode + "'";



		logger.info("master_n_product:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		// 循环查询到该末级分类下的产品
		for (int i = 0; i < dictData.size(); i++) {
			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String productCode = mapBrand.get("productCode");
			String productName = mapBrand.get("productName");
			String productCategoryCodeName = mapBrand.get("productCategoryCodeName");

			// 查询产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS atodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS atodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + businessDateStart + "' AND businessDate<= '" + businessDateEnd
					+ "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("aSQL=" + strSql);

			PageModel pma = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pma);
			List<Object> dataProductA = page.getResultList();

			Map<String, String> mapA = new HashMap<String, String>();
			mapA = (Map<String, String>) dataProductA.get(0);
			String atodayNum = ((Object) mapA.get("atodayNum")).toString();
			String atodaySales = ((Object) mapA.get("atodaySales")).toString();

			// 查询同比产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS btodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS btodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + TBTimeStart + "' AND businessDate<= '" + TBTimeEnd + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("bSQL=" + strSql);
			PageModel pmb = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pmb);
			List<Object> dataProductB = page.getResultList();

			Map<String, String> mapB = new HashMap<String, String>();
			mapB = (Map<String, String>) dataProductB.get(0);
			String btodayNum = ((Object) mapB.get("btodayNum")).toString();
			String btodaySales = ((Object) mapB.get("btodaySales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += "  ,'" + productCategoryCodeName + "'  AS '所属分类' ";
			strSql += "  ,'" + productName + "'  AS '产品名称' ";

			strSql += "	 ,IFNULL(SUM(atodaySales), 0) AS 'atodaySales' ";
			strSql += "	 ,IFNULL(SUM(btodaySales), 0) AS 'btodaySales' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(atodaySales) - SUM(btodaySales)) / SUM(btodaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodaySalesRate' ";
			strSql += "	 ,IFNULL(SUM(atodayNum), 0) AS 'atodayNum' ";
			strSql += "	 ,IFNULL(SUM(btodayNum), 0) AS 'btodayNum' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(atodayNum) - SUM(btodayNum)) / SUM(btodayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodayNumRate' ";

			strSql += " 	,IFNULL(ROUND(SUM(atodaySales) / SUM(atodayNum),2),0) AS '销售均价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(btodaySales) / SUM(btodayNum),2),0) AS '销售均价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(atodaySales) / SUM(atodayNum),2) - ROUND(SUM(btodaySales) / SUM(btodayNum),2))  ";
			strSql += "                        / ROUND(SUM(btodaySales) / SUM(btodayNum),2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

			strSql += " FROM ";
			strSql += "	( SELECT ";
			strSql += atodayNum + " AS 'atodayNum' ";
			strSql += "	, " + atodaySales + " AS 'atodaySales' ";
			strSql += "	, " + btodayNum + " AS 'btodayNum' ";
			strSql += "	, " + btodaySales + " AS 'btodaySales' ";
			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int atodayNum = 0;
		int btodayNum = 0;

		double atodaySales = 0;
		double btodaySales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			atodayNum += Integer.parseInt(((Object) mapValue.get("atodayNum")).toString());
			atodaySales += Double.parseDouble(((Object) mapValue.get("atodaySales")).toString());

			btodayNum += Integer.parseInt(((Object) mapValue.get("btodayNum")).toString());
			btodaySales += Double.parseDouble(((Object) mapValue.get("btodaySales")).toString());

		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += "  ,''  AS '所属分类' ";
		strSql += "  ,''  AS '产品名称' ";
		strSql += " ,ROUND(" + atodaySales + ",2) AS '实际收入<br>分析时段' ";
		strSql += " ,ROUND(" + btodaySales + ",2) AS '实际收入<br>同比时段' ";

		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((" + atodaySales + " - " + btodaySales + ") / " + btodaySales
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";

		strSql += " ," + atodayNum + " AS '销量<br>分析时段' ";
		strSql += " ," + btodayNum + " AS '销量<br>同比时段' ";

		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + atodayNum + " - " + btodayNum + ") / " + btodayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '销量<br>同比增长率' ";

		strSql += " 	,IFNULL(ROUND(" + atodaySales + " / " + atodayNum + ",2),0) AS '销售均价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(" + btodaySales + " / " + btodayNum + ",2),0) AS '销售均价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(" + atodaySales + " / " + atodayNum + ",2) - ROUND("
				+ btodaySales + " / " + btodayNum + ",2))  ";
		strSql += "                        / ROUND(" + btodaySales + " / " + btodayNum
				+ ",2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

		totalSQL = "";

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
	 * ======产品分类同比分析导出Excel====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");
		String TBTimeStart = map.get("TBTimeStart");
		String TBTimeEnd = map.get("TBTimeEnd");
		String strSql = "";
		String totalSQL = "";

		// 传入查询的末级分类下有哪些产品
		strSql = "";
		totalSQL = "";
		strSql += "SELECT A.productCode,A.productName,B.productCategoryCodeName,C.brandCodeName,A.brandCode ";
		strSql += "  FROM vrep_dict_product_all AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_master_productcategory AS B ON A.masterCategoryCode = B.productCategoryCode ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand AS C ON A.brandCode = C.brandCode ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";

		if (BrandCode != "" && !BrandCode.equals("ALL")) {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND  A.brandCode in ('" + BrandCode + "')";
		}


		if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
			CategoryCode = CategoryCode.replace("|", "','");
			strSql += " AND A.masterCategoryCode in ('" + CategoryCode + "')";
		}

		strSql += "   ORDER BY C.brandCodeName,B.productCategoryCodeName,A.productName";

		logger.info("master_n_product:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		// 循环查询到该末级分类下的产品
		for (int i = 0; i < dictData.size(); i++) {
			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String productCode = mapBrand.get("productCode");
			String productName = mapBrand.get("productName");
			String productCategoryCodeName = mapBrand.get("productCategoryCodeName");
			String brandCodeName = mapBrand.get("brandCodeName");
			String brandCode = mapBrand.get("brandCode");

			// 查询产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS atodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS atodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + businessDateStart + "' AND businessDate<= '" + businessDateEnd
					+ "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + brandCode + "'";
			strSql += "       AND productCode = '" + productCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("aSQL=" + strSql);

			PageModel pma = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pma);
			List<Object> dataProductA = page.getResultList();

			Map<String, String> mapA = new HashMap<String, String>();
			mapA = (Map<String, String>) dataProductA.get(0);
			String atodayNum = ((Object) mapA.get("atodayNum")).toString();
			String atodaySales = ((Object) mapA.get("atodaySales")).toString();

			// 查询同比产品销量和实际收入
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS btodayNum ";
			strSql += "			,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS btodaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE (businessDate >= '" + TBTimeStart + "' AND businessDate<= '" + TBTimeEnd + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + brandCode + "'";
			strSql += "       AND productCode = '" + productCode + "' ";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info("bSQL=" + strSql);
			PageModel pmb = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pmb);
			List<Object> dataProductB = page.getResultList();

			Map<String, String> mapB = new HashMap<String, String>();
			mapB = (Map<String, String>) dataProductB.get(0);
			String btodayNum = ((Object) mapB.get("btodayNum")).toString();
			String btodaySales = ((Object) mapB.get("btodaySales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + brandCodeName + "' AS '所属品牌' ";
			strSql += "  ,'" + productCategoryCodeName + "'  AS '所属分类' ";
			strSql += "  ,'" + productName + "'  AS '产品名称' ";

			strSql += "	 ,IFNULL(SUM(atodaySales), 0) AS 'atodaySales' ";
			strSql += "	 ,IFNULL(SUM(btodaySales), 0) AS 'btodaySales' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT(ROUND(ROUND((SUM(atodaySales) - SUM(btodaySales)) / SUM(btodaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodaySalesRate' ";
			strSql += "	 ,IFNULL(SUM(atodayNum), 0) AS 'atodayNum' ";
			strSql += "	 ,IFNULL(SUM(btodayNum), 0) AS 'btodayNum' ";
			strSql += "  ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(atodayNum) - SUM(btodayNum)) / SUM(btodayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'btodayNumRate' ";

			strSql += " 	,IFNULL(ROUND(SUM(atodaySales) / SUM(atodayNum),2),0) AS '销售均价<br>分析时段' ";
			strSql += " 	,IFNULL(ROUND(SUM(btodaySales) / SUM(btodayNum),2),0) AS '销售均价<br>同比时段' ";
			strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(SUM(atodaySales) / SUM(atodayNum),2) - ROUND(SUM(btodaySales) / SUM(btodayNum),2))  ";
			strSql += "                        / ROUND(SUM(btodaySales) / SUM(btodayNum),2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

			strSql += " FROM ";
			strSql += "	( select ";
			strSql += atodayNum + " AS 'atodayNum' ";
			strSql += "	, " + atodaySales + " AS 'atodaySales' ";
			strSql += "	, " + btodayNum + " AS 'btodayNum' ";
			strSql += "	, " + btodaySales + " AS 'btodaySales' ";
			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 10000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int atodayNum = 0;
		int btodayNum = 0;

		double atodaySales = 0;
		double btodaySales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);

			atodayNum += Integer.parseInt(((Object) mapValue.get("atodayNum")).toString());
			atodaySales += Double.parseDouble(((Object) mapValue.get("atodaySales")).toString());

			btodayNum += Integer.parseInt(((Object) mapValue.get("btodayNum")).toString());
			btodaySales += Double.parseDouble(((Object) mapValue.get("btodaySales")).toString());

		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += "  ,''  AS '所属分类' ";
		strSql += "  ,''  AS '产品名称' ";
		strSql += " ,ROUND(" + atodaySales + ",4) AS '实际收入<br>分析时段' ";
		strSql += " ,ROUND(" + btodaySales + ",4) AS '实际收入<br>同比时段' ";

		strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND((" + atodaySales + " - " + btodaySales + ") / " + btodaySales
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '实际收入<br>同比增长率' ";

		strSql += " ," + atodayNum + " AS '销量<br>分析时段' ";
		strSql += " ," + btodayNum + " AS '销量<br>同比时段' ";

		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + atodayNum + " - " + btodayNum + ") / " + btodayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '销量<br>同比增长率' ";

		strSql += " 	,IFNULL(ROUND(" + atodaySales + " / " + atodayNum + ",2),0) AS '销售均价<br>分析时段' ";
		strSql += " 	,IFNULL(ROUND(" + btodaySales + " / " + btodayNum + ",2),0) AS '销售均价<br>同比时段' ";
		strSql += " 	,IFNULL(CONCAT(CONVERT(ROUND(ROUND((ROUND(" + atodaySales + " / " + atodayNum + ",2) - ROUND("
				+ btodaySales + " / " + btodayNum + ",2))  ";
		strSql += "                        / ROUND(" + btodaySales + " / " + btodayNum
				+ ",2),4) * 100,2),CHAR (100)),'%'),'--') AS '销售均价<br>同比增长率' ";

		totalSQL = "";

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
	 * ======产品分类环比分析(品牌汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatHBBrand(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String CategoryCode = map.get("CategoryCode");

		String strSql = "";
		String totalSQL = "";

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_master_productcategory");
		mapPam.put(new String("sqlWhere"), "where level_ = 1 AND companyCode='" + companyCode + "'");

		strSql = "";
		totalSQL = "";
		strSql += "SELECT brandCode,brandCodeName,IFNULL(productCategoryCode,'') AS areaCode  FROM vrep_dict_master_productcategory ";
		strSql += " WHERE level_ = 1 AND companyCode = '" + companyCode + "'";
		if (BrandCode != "" && !BrandCode.equals("ALL")) {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND brandCode in ('" + BrandCode + "')";
		}

		strSql += " ORDER BY brandCodeName";

		logger.info("vrep_dict_master_productcategory:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> brandData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < brandData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) brandData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String brandCode = mapBrand.get("brandCode");
			String brandCodeName = mapBrand.get("brandCodeName");

			// ======本日销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS todayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS todaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate = '" + businessDateStart + "' ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel pm = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataToday = page.getResultList();

			Map<String, String> mapToday = new HashMap<String, String>();
			mapToday = (Map<String, String>) dataToday.get(0);
			String todayNum = ((Object) mapToday.get("todayNum")).toString();
			String todaySales = ((Object) mapToday.get("todaySales")).toString();

			// ======上周本日销量及销售额=================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekDayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekDaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate =  DATE_ADD('" + businessDateStart + "', INTERVAL - 1 WEEK) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeekDay = page.getResultList();

			Map<String, String> mapLastWeekDay = new HashMap<String, String>();
			mapLastWeekDay = (Map<String, String>) dataLastWeekDay.get(0);
			String lastWeekDayNum = ((Object) mapLastWeekDay.get("lastWeekDayNum")).toString();
			String lastWeekDaySales = ((Object) mapLastWeekDay.get("lastWeekDaySales")).toString();

			// ======本周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS thisWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK('" + businessDateStart + "',1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisWeek = page.getResultList();

			Map<String, String> mapThisWeek = new HashMap<String, String>();
			mapThisWeek = (Map<String, String>) dataThisWeek.get(0);
			String thisWeekNum = ((Object) mapThisWeek.get("thisWeekNum")).toString();
			String thisWeekSales = ((Object) mapThisWeek.get("thisWeekSales")).toString();

			// ======上周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK)) ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeek = page.getResultList();

			Map<String, String> mapLastWeek = new HashMap<String, String>();
			mapLastWeek = (Map<String, String>) dataLastWeek.get(0);
			String lastWeekNum = ((Object) mapLastWeek.get("lastWeekNum")).toString();
			String lastWeekSales = ((Object) mapLastWeek.get("lastWeekSales")).toString();

			// ======本月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS thisMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND MONTH(businessDate) = MONTH('" + businessDateStart + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisMonth = page.getResultList();

			Map<String, String> mapThisMonth = new HashMap<String, String>();
			mapThisMonth = (Map<String, String>) dataThisMonth.get(0);
			String thisMonthNum = ((Object) mapThisMonth.get("thisMonthNum")).toString();
			String thisMonthSales = ((Object) mapThisMonth.get("thisMonthSales")).toString();

			// ======上月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS lastMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND MONTH(businessDate) = MONTH(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastMonth = page.getResultList();

			Map<String, String> mapLastMonth = new HashMap<String, String>();
			mapLastMonth = (Map<String, String>) dataLastMonth.get(0);
			String lastMonthNum = ((Object) mapLastMonth.get("lastMonthNum")).toString();
			String lastMonthSales = ((Object) mapLastMonth.get("lastMonthSales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCode + "','|=|','" + brandCodeName + "'" + ",'|=|','" + areaCode
					+ "'),'') AS '品牌' ";

			strSql += "	 ,IFNULL(SUM(todayNum), 0) AS 'todayNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDayNum), 0) AS 'lastWeekDayNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todayNum) - SUM(lastWeekDayNum)) / SUM(lastWeekDayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'dayNumRate' ";
			strSql += "	 ,IFNULL(SUM(todaySales), 0) AS 'todaySales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDaySales), 0) AS 'lastWeekDaySales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'daySalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisWeekNum), 0) AS 'thisWeekNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekNum), 0) AS 'lastWeekNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekNum) - SUM(lastWeekNum)) / SUM(lastWeekNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekNumRate' ";
			strSql += "  ,IFNULL(SUM(thisWeekSales), 0) AS 'thisWeekSales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekSales), 0) AS 'lastWeekSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekSalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisMonthNum), 0) AS 'thisMonthNum' ";
			strSql += "	 ,IFNULL(SUM(lastMonthNum), 0) AS 'lastMonthNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthNum) - SUM(lastMonthNum)) / SUM(lastMonthNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthNumRate' ";
			strSql += "  ,IFNULL(SUM(thisMonthSales), 0) AS 'thisMonthSales' ";
			strSql += "	 ,IFNULL(SUM(lastMonthSales), 0) AS 'lastMonthSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthSalesRate' ";
			strSql += " FROM ";
			strSql += "	( ";
			strSql += "		SELECT '" + brandCodeName + "' brandCodeName ";

			strSql += "," + todayNum + " AS 'todayNum' ";
			strSql += "," + lastWeekDayNum + " AS 'lastWeekDayNum' ";
			strSql += "," + thisWeekNum + " AS 'thisWeekNum' ";
			strSql += "," + lastWeekNum + " AS 'lastWeekNum' ";
			strSql += "," + thisMonthNum + " AS 'thisMonthNum' ";
			strSql += "," + lastMonthNum + " AS 'lastMonthNum' ";
			strSql += "," + todaySales + " AS 'todaySales' ";
			strSql += "," + lastWeekDaySales + " AS 'lastWeekDaySales' ";
			strSql += "," + thisWeekSales + " AS 'thisWeekSales' ";
			strSql += "," + lastWeekSales + " AS 'lastWeekSales' ";
			strSql += "," + thisMonthSales + " AS 'thisMonthSales' ";
			strSql += "," + lastMonthSales + " AS 'lastMonthSales' ";

			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int todayNum = 0;
		int lastWeekDayNum = 0;
		double todaySales = 0;
		double lastWeekDaySales = 0;
		int thisWeekNum = 0;
		int lastWeekNum = 0;
		double thisWeekSales = 0;
		double lastWeekSales = 0;
		int thisMonthNum = 0;
		int lastMonthNum = 0;
		double thisMonthSales = 0;
		double lastMonthSales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			todayNum += Integer.parseInt(((Object) mapValue.get("todayNum")).toString());
			lastWeekDayNum += Integer.parseInt(((Object) mapValue.get("lastWeekDayNum")).toString());
			todaySales += Double.parseDouble(((Object) mapValue.get("todaySales")).toString());
			lastWeekDaySales += Double.parseDouble(((Object) mapValue.get("lastWeekDaySales")).toString());
			thisWeekNum += Integer.parseInt(((Object) mapValue.get("thisWeekNum")).toString());
			lastWeekNum += Integer.parseInt(((Object) mapValue.get("lastWeekNum")).toString());
			thisWeekSales += Double.parseDouble(((Object) mapValue.get("thisWeekSales")).toString());
			lastWeekSales += Double.parseDouble(((Object) mapValue.get("lastWeekSales")).toString());
			thisMonthNum += Integer.parseInt(((Object) mapValue.get("thisMonthNum")).toString());
			lastMonthNum += Integer.parseInt(((Object) mapValue.get("lastMonthNum")).toString());
			thisMonthSales += Double.parseDouble(((Object) mapValue.get("thisMonthSales")).toString());
			lastMonthSales += Double.parseDouble(((Object) mapValue.get("lastMonthSales")).toString());
		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += " ," + todayNum + " AS '本日销量' ";
		strSql += " ," + lastWeekDayNum + " AS '上周本日销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todayNum + " - " + lastWeekDayNum + ") / " + lastWeekDayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '日销量环比'";
		strSql += " ,ROUND(" + todaySales + ",2) AS '本日实际收入' ";
		strSql += " ,ROUND(" + lastWeekDaySales + ",2) AS '上周本日实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todaySales + " - " + lastWeekDaySales + ") / "
				+ lastWeekDaySales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";

		strSql += " ," + thisWeekNum + " AS '本周销量' ";
		strSql += " ," + lastWeekNum + " AS '上周销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekNum + " - " + lastWeekNum + ") / " + lastWeekNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '周销量环比'";
		strSql += " ,ROUND(" + thisWeekSales + ",2) AS '本周实际收入' ";
		strSql += " ,ROUND(" + lastWeekSales + ",2) AS '上周实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekSales + " - " + lastWeekSales + ") / "
				+ lastWeekSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";

		strSql += " ," + thisMonthNum + " AS '本月销量' ";
		strSql += " ," + lastMonthNum + " AS '上月销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthNum + " - " + lastMonthNum + ") / " + lastMonthNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '月销量环比'";
		strSql += " ,ROUND(" + thisMonthSales + ",2) AS '本月实际收入' ";
		strSql += " ,ROUND(" + lastMonthSales + ",2) AS '上月实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthSales + " - " + lastMonthSales + ") / "
				+ lastMonthSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";

		logger.info(strSql);

		totalSQL = "";

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
	 * ======产品分类环比分析(分类汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatHBBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");

		String strSql = "";
		String totalSQL = "";

		// 明细数据

		strSql = "";
		totalSQL = "";
		strSql += "SELECT productCategoryCode AS areaCode,productCategoryCodeName AS areaName FROM vrep_dict_master_productcategory ";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND brandCode = '" + BrandCode + "'";
		strSql += " AND parentCode = '" + AreaCode + "'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		for (int i = 0; i < dictData.size(); i++) {

			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String areaCode = mapBrand.get("areaCode");
			String areaName = mapBrand.get("areaName");

			// 获取该区域下所有子区域代码
			strSql = "";
			totalSQL = "";
			strSql += "SELECT fn_get_all_categorycode('" + areaCode + "') AS 'allChildrenCode'";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageModel PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> allChildrenCodeData = page.getResultList();
			Map<String, String> allChildrenCodeMap = new HashMap<String, String>();
			allChildrenCodeMap = (Map<String, String>) allChildrenCodeData.get(0);
			String allChildrenCode = allChildrenCodeMap.get("allChildrenCode");

			String isLast = "N";

			strSql = "";
			totalSQL = "";
			strSql += " SELECT fn_CheckCategoryIsLast(companyCode,brandCode,productCategoryCode) AS IsLast FROM vrep_dict_master_productcategory  ";
			strSql += "WHERE companyCode = '" + companyCode + "' ";
			strSql += " AND brandCode = '" + BrandCode + "'";
			strSql += " AND productCategoryCode = '" + areaCode + "' ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			PageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, PageTemp);
			List<Object> isLastData = page.getResultList();

			logger.info(strSql);

			for (int k = 0; k < isLastData.size(); k++) {
				Map<String, String> mapTemoS = (Map<String, String>) isLastData.get(k);
				isLast = mapTemoS.get("IsLast");
			}

			// ======本日销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS todayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS todaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate = '" + businessDateStart + "' ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";

			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel pm = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataToday = page.getResultList();

			Map<String, String> mapToday = new HashMap<String, String>();
			mapToday = (Map<String, String>) dataToday.get(0);
			String todayNum = ((Object) mapToday.get("todayNum")).toString();
			String todaySales = ((Object) mapToday.get("todaySales")).toString();

			// ======上周本日销量及销售额=================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekDayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekDaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate =  DATE_ADD('" + businessDateStart + "', INTERVAL - 1 WEEK) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeekDay = page.getResultList();

			Map<String, String> mapLastWeekDay = new HashMap<String, String>();
			mapLastWeekDay = (Map<String, String>) dataLastWeekDay.get(0);
			String lastWeekDayNum = ((Object) mapLastWeekDay.get("lastWeekDayNum")).toString();
			String lastWeekDaySales = ((Object) mapLastWeekDay.get("lastWeekDaySales")).toString();

			// ======本周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS thisWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK('" + businessDateStart + "',1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisWeek = page.getResultList();

			Map<String, String> mapThisWeek = new HashMap<String, String>();
			mapThisWeek = (Map<String, String>) dataThisWeek.get(0);
			String thisWeekNum = ((Object) mapThisWeek.get("thisWeekNum")).toString();
			String thisWeekSales = ((Object) mapThisWeek.get("thisWeekSales")).toString();

			// ======上周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK)) ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeek = page.getResultList();

			Map<String, String> mapLastWeek = new HashMap<String, String>();
			mapLastWeek = (Map<String, String>) dataLastWeek.get(0);
			String lastWeekNum = ((Object) mapLastWeek.get("lastWeekNum")).toString();
			String lastWeekSales = ((Object) mapLastWeek.get("lastWeekSales")).toString();

			// ======本月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS thisMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND MONTH(businessDate) = MONTH('" + businessDateStart + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisMonth = page.getResultList();

			Map<String, String> mapThisMonth = new HashMap<String, String>();
			mapThisMonth = (Map<String, String>) dataThisMonth.get(0);
			String thisMonthNum = ((Object) mapThisMonth.get("thisMonthNum")).toString();
			String thisMonthSales = ((Object) mapThisMonth.get("thisMonthSales")).toString();

			// ======上月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS lastMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND MONTH(businessDate) = MONTH(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "   AND masterCategoryCode IN (" + allChildrenCode + ") ";

			if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
				CategoryCode = CategoryCode.replace("|", "','");
				strSql += " AND masterCategoryCode in ('" + CategoryCode + "')";
			}
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastMonth = page.getResultList();

			Map<String, String> mapLastMonth = new HashMap<String, String>();
			mapLastMonth = (Map<String, String>) dataLastMonth.get(0);
			String lastMonthNum = ((Object) mapLastMonth.get("lastMonthNum")).toString();
			String lastMonthSales = ((Object) mapLastMonth.get("lastMonthSales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isLast + "'),'') AS '分类' ";
			strSql += "	 ,IFNULL(SUM(todayNum), 0) AS 'todayNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDayNum), 0) AS 'lastWeekDayNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todayNum) - SUM(lastWeekDayNum)) / SUM(lastWeekDayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'dayNumRate' ";
			strSql += "	 ,IFNULL(SUM(todaySales), 0) AS 'todaySales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDaySales), 0) AS 'lastWeekDaySales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'daySalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisWeekNum), 0) AS 'thisWeekNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekNum), 0) AS 'lastWeekNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekNum) - SUM(lastWeekNum)) / SUM(lastWeekNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekNumRate' ";
			strSql += "  ,IFNULL(SUM(thisWeekSales), 0) AS 'thisWeekSales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekSales), 0) AS 'lastWeekSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekSalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisMonthNum), 0) AS 'thisMonthNum' ";
			strSql += "	 ,IFNULL(SUM(lastMonthNum), 0) AS 'lastMonthNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthNum) - SUM(lastMonthNum)) / SUM(lastMonthNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthNumRate' ";
			strSql += "  ,IFNULL(SUM(thisMonthSales), 0) AS 'thisMonthSales' ";
			strSql += "	 ,IFNULL(SUM(lastMonthSales), 0) AS 'lastMonthSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthSalesRate' ";
			strSql += " FROM ";
			strSql += "	( ";
			strSql += "		SELECT ";
			strSql += " " + todayNum + " AS 'todayNum' ";
			strSql += "	," + lastWeekDayNum + " AS 'lastWeekDayNum' ";
			strSql += "," + thisWeekNum + " AS 'thisWeekNum' ";
			strSql += "," + lastWeekNum + " AS 'lastWeekNum' ";
			strSql += "," + thisMonthNum + " AS 'thisMonthNum' ";
			strSql += "," + lastMonthNum + " AS 'lastMonthNum' ";
			strSql += "," + todaySales + " AS 'todaySales' ";
			strSql += "," + lastWeekDaySales + " AS 'lastWeekDaySales' ";
			strSql += "," + thisWeekSales + " AS 'thisWeekSales' ";
			strSql += "," + lastWeekSales + " AS 'lastWeekSales' ";
			strSql += "," + thisMonthSales + " AS 'thisMonthSales' ";
			strSql += "," + lastMonthSales + " AS 'lastMonthSales' ";

			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int todayNum = 0;
		int lastWeekDayNum = 0;
		double todaySales = 0;
		double lastWeekDaySales = 0;
		int thisWeekNum = 0;
		int lastWeekNum = 0;
		double thisWeekSales = 0;
		double lastWeekSales = 0;
		int thisMonthNum = 0;
		int lastMonthNum = 0;
		double thisMonthSales = 0;
		double lastMonthSales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			todayNum += Integer.parseInt(((Object) mapValue.get("todayNum")).toString());
			lastWeekDayNum += Integer.parseInt(((Object) mapValue.get("lastWeekDayNum")).toString());
			todaySales += Double.parseDouble(((Object) mapValue.get("todaySales")).toString());
			lastWeekDaySales += Double.parseDouble(((Object) mapValue.get("lastWeekDaySales")).toString());
			thisWeekNum += Integer.parseInt(((Object) mapValue.get("thisWeekNum")).toString());
			lastWeekNum += Integer.parseInt(((Object) mapValue.get("lastWeekNum")).toString());
			thisWeekSales += Double.parseDouble(((Object) mapValue.get("thisWeekSales")).toString());
			lastWeekSales += Double.parseDouble(((Object) mapValue.get("lastWeekSales")).toString());
			thisMonthNum += Integer.parseInt(((Object) mapValue.get("thisMonthNum")).toString());
			lastMonthNum += Integer.parseInt(((Object) mapValue.get("lastMonthNum")).toString());
			thisMonthSales += Double.parseDouble(((Object) mapValue.get("thisMonthSales")).toString());
			lastMonthSales += Double.parseDouble(((Object) mapValue.get("lastMonthSales")).toString());
		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += " ,'' AS '所属分类' ";
		strSql += " ," + todayNum + " AS '本日销量' ";
		strSql += " ," + lastWeekDayNum + " AS '上周本日销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todayNum + " - " + lastWeekDayNum + ") / " + lastWeekDayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '日销量环比'";
		strSql += " ,ROUND(" + todaySales + ",2) AS '本日实际收入' ";
		strSql += " ,ROUND(" + lastWeekDaySales + ",2) AS '上周本日实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todaySales + " - " + lastWeekDaySales + ") / "
				+ lastWeekDaySales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";

		strSql += " ," + thisWeekNum + " AS '本周销量' ";
		strSql += " ," + lastWeekNum + " AS '上周销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekNum + " - " + lastWeekNum + ") / " + lastWeekNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '周销量环比'";
		strSql += " ,ROUND(" + thisWeekSales + ",2) AS '本周实际收入' ";
		strSql += " ,ROUND(" + lastWeekSales + ",2) AS '上周实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekSales + " - " + lastWeekSales + ") / "
				+ lastWeekSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";

		strSql += " ," + thisMonthNum + " AS '本月销量' ";
		strSql += " ," + lastMonthNum + " AS '上月销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthNum + " - " + lastMonthNum + ") / " + lastMonthNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '月销量环比'";
		strSql += " ,ROUND(" + thisMonthSales + ",2) AS '本月实际收入' ";
		strSql += " ,ROUND(" + lastMonthSales + ",2) AS '上月实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthSales + " - " + lastMonthSales + ") / "
				+ lastMonthSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";

		logger.info("cateSum:" + strSql);

		totalSQL = "";

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
	 * ======产品分类环比分析(产品汇总)====================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");

		String strSql = "";
		String totalSQL = "";

		// 传入查询的末级分类下有哪些产品
		strSql = "";
		totalSQL = "";
		strSql += "SELECT A.productCode,A.productName,B.productCategoryCodeName ";
		strSql += "  FROM vrep_dict_product_all AS A ";
		strSql += "LEFT OUTER JOIN vrep_dict_master_productcategory AS B ON A.masterCategoryCode = B.productCategoryCode ";
		strSql += " WHERE A.companyCode = '" + companyCode + "'";
		strSql += "   AND A.brandCode = '" + BrandCode + "'";
		strSql += "   AND A.masterCategoryCode = '" + AreaCode + "'";

		
		
		logger.info("master_n_product:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		// 循环查询到该末级分类下的产品
		for (int i = 0; i < dictData.size(); i++) {
			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String productCode = mapBrand.get("productCode");
			String productName = mapBrand.get("productName");
			String productCategoryCodeName = mapBrand.get("productCategoryCodeName");

			// ======本日销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS todayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS todaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate = '" + businessDateStart + "' ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "			 AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel pm = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataToday = page.getResultList();

			Map<String, String> mapToday = new HashMap<String, String>();
			mapToday = (Map<String, String>) dataToday.get(0);
			String todayNum = ((Object) mapToday.get("todayNum")).toString();
			String todaySales = ((Object) mapToday.get("todaySales")).toString();

			// ======上周本日销量及销售额=================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekDayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekDaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate =  DATE_ADD('" + businessDateStart + "', INTERVAL - 1 WEEK) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "			 AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeekDay = page.getResultList();

			Map<String, String> mapLastWeekDay = new HashMap<String, String>();
			mapLastWeekDay = (Map<String, String>) dataLastWeekDay.get(0);
			String lastWeekDayNum = ((Object) mapLastWeekDay.get("lastWeekDayNum")).toString();
			String lastWeekDaySales = ((Object) mapLastWeekDay.get("lastWeekDaySales")).toString();

			// ======本周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS thisWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK('" + businessDateStart + "',1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisWeek = page.getResultList();

			Map<String, String> mapThisWeek = new HashMap<String, String>();
			mapThisWeek = (Map<String, String>) dataThisWeek.get(0);
			String thisWeekNum = ((Object) mapThisWeek.get("thisWeekNum")).toString();
			String thisWeekSales = ((Object) mapThisWeek.get("thisWeekSales")).toString();

			// ======上周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK)) ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeek = page.getResultList();

			Map<String, String> mapLastWeek = new HashMap<String, String>();
			mapLastWeek = (Map<String, String>) dataLastWeek.get(0);
			String lastWeekNum = ((Object) mapLastWeek.get("lastWeekNum")).toString();
			String lastWeekSales = ((Object) mapLastWeek.get("lastWeekSales")).toString();

			// ======本月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS thisMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND MONTH(businessDate) = MONTH('" + businessDateStart + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisMonth = page.getResultList();

			Map<String, String> mapThisMonth = new HashMap<String, String>();
			mapThisMonth = (Map<String, String>) dataThisMonth.get(0);
			String thisMonthNum = ((Object) mapThisMonth.get("thisMonthNum")).toString();
			String thisMonthSales = ((Object) mapThisMonth.get("thisMonthSales")).toString();

			// ======上月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS lastMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND MONTH(businessDate) = MONTH(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + BrandCode + "' ";
			strSql += "			 AND masterCategoryCode = '" + AreaCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastMonth = page.getResultList();

			Map<String, String> mapLastMonth = new HashMap<String, String>();
			mapLastMonth = (Map<String, String>) dataLastMonth.get(0);
			String lastMonthNum = ((Object) mapLastMonth.get("lastMonthNum")).toString();
			String lastMonthSales = ((Object) mapLastMonth.get("lastMonthSales")).toString();

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + BrandName + "' AS '所属品牌' ";
			strSql += "  ,'" + productCategoryCodeName + "'  AS '所属分类' ";
			strSql += "  ,'" + productName + "'  AS '产品名称' ";
			strSql += "	 ,IFNULL(SUM(todayNum), 0) AS 'todayNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDayNum), 0) AS 'lastWeekDayNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todayNum) - SUM(lastWeekDayNum)) / SUM(lastWeekDayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'dayNumRate' ";
			strSql += "	 ,IFNULL(SUM(todaySales), 0) AS 'todaySales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDaySales), 0) AS 'lastWeekDaySales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'daySalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisWeekNum), 0) AS 'thisWeekNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekNum), 0) AS 'lastWeekNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekNum) - SUM(lastWeekNum)) / SUM(lastWeekNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekNumRate' ";
			strSql += "  ,IFNULL(SUM(thisWeekSales), 0) AS 'thisWeekSales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekSales), 0) AS 'lastWeekSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekSalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisMonthNum), 0) AS 'thisMonthNum' ";
			strSql += "	 ,IFNULL(SUM(lastMonthNum), 0) AS 'lastMonthNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthNum) - SUM(lastMonthNum)) / SUM(lastMonthNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthNumRate' ";
			strSql += "  ,IFNULL(SUM(thisMonthSales), 0) AS 'thisMonthSales' ";
			strSql += "	 ,IFNULL(SUM(lastMonthSales), 0) AS 'lastMonthSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthSalesRate' ";
			strSql += " FROM ";
			strSql += "	( ";
			strSql += "		SELECT '" + productName + "' AS productName ";
			strSql += " ," + todayNum + " AS 'todayNum' ";
			strSql += "	," + lastWeekDayNum + " AS 'lastWeekDayNum' ";
			strSql += "," + thisWeekNum + " AS 'thisWeekNum' ";
			strSql += "," + lastWeekNum + " AS 'lastWeekNum' ";

			strSql += "," + thisMonthNum + " AS 'thisMonthNum' ";
			strSql += "," + lastMonthNum + " AS 'lastMonthNum' ";

			strSql += "," + todaySales + " AS 'todaySales' ";
			strSql += "," + lastWeekDaySales + " AS 'lastWeekDaySales' ";
			strSql += "," + thisWeekSales + " AS 'thisWeekSales' ";
			strSql += "," + lastWeekSales + " AS 'lastWeekSales' ";

			strSql += "," + thisMonthSales + " AS 'thisMonthSales' ";
			strSql += "," + lastMonthSales + " AS 'lastMonthSales' ";

			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int todayNum = 0;
		int lastWeekDayNum = 0;
		double todaySales = 0;
		double lastWeekDaySales = 0;
		int thisWeekNum = 0;
		int lastWeekNum = 0;
		double thisWeekSales = 0;
		double lastWeekSales = 0;
		int thisMonthNum = 0;
		int lastMonthNum = 0;
		double thisMonthSales = 0;
		double lastMonthSales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			logger.info(mapValue);

			todayNum += Integer.parseInt(((Object) mapValue.get("todayNum")).toString());
			lastWeekDayNum += Integer.parseInt(((Object) mapValue.get("lastWeekDayNum")).toString());
			todaySales += Double.parseDouble(((Object) mapValue.get("todaySales")).toString());
			lastWeekDaySales += Double.parseDouble(((Object) mapValue.get("lastWeekDaySales")).toString());
			thisWeekNum += Integer.parseInt(((Object) mapValue.get("thisWeekNum")).toString());
			lastWeekNum += Integer.parseInt(((Object) mapValue.get("lastWeekNum")).toString());
			thisWeekSales += Double.parseDouble(((Object) mapValue.get("thisWeekSales")).toString());
			lastWeekSales += Double.parseDouble(((Object) mapValue.get("lastWeekSales")).toString());
			thisMonthNum += Integer.parseInt(((Object) mapValue.get("thisMonthNum")).toString());
			lastMonthNum += Integer.parseInt(((Object) mapValue.get("lastMonthNum")).toString());
			thisMonthSales += Double.parseDouble(((Object) mapValue.get("thisMonthSales")).toString());
			lastMonthSales += Double.parseDouble(((Object) mapValue.get("lastMonthSales")).toString());
		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += " ,'' AS '所属分类' ";
		strSql += " ,'' AS '产品名称' ";
		strSql += " ," + todayNum + " AS '本日销量' ";
		strSql += " ," + lastWeekDayNum + " AS '上周本日销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todayNum + " - " + lastWeekDayNum + ") / " + lastWeekDayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '日销量环比'";
		strSql += " ,ROUND(" + todaySales + ",2) AS '本日实际收入' ";
		strSql += " ,ROUND(" + lastWeekDaySales + ",2) AS '上周本日实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todaySales + " - " + lastWeekDaySales + ") / "
				+ lastWeekDaySales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";

		strSql += " ," + thisWeekNum + " AS '本周销量' ";
		strSql += " ," + lastWeekNum + " AS '上周销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekNum + " - " + lastWeekNum + ") / " + lastWeekNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '周销量环比'";
		strSql += " ,ROUND(" + thisWeekSales + ",2) AS '本周实际收入' ";
		strSql += " ,ROUND(" + lastWeekSales + ",2) AS '上周实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekSales + " - " + lastWeekSales + ") / "
				+ lastWeekSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";

		strSql += " ," + thisMonthNum + " AS '本月销量' ";
		strSql += " ," + lastMonthNum + " AS '上月销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthNum + " - " + lastMonthNum + ") / " + lastMonthNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '月销量环比'";
		strSql += " ,ROUND(" + thisMonthSales + ",2) AS '本月实际收入' ";
		strSql += " ,ROUND(" + lastMonthSales + ",2) AS '上月实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthSales + " - " + lastMonthSales + ") / "
				+ lastMonthSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";

		logger.info("productSum:" + strSql);

		totalSQL = "";

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
	 * ======产品分类环比分析导出Excel===================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelName = map.get("ChannelName");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String CategoryCode = map.get("CategoryCode");

		String strSql = "";
		String totalSQL = "";

		// 传入查询的末级分类下有哪些产品
		strSql = "";
		totalSQL = "";
		strSql += "SELECT A.productCode,A.productName,B.productCategoryCodeName,C.brandCodeName,A.brandCode ";
		strSql += "  FROM vrep_dict_product_all AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_master_productcategory AS B ON A.masterCategoryCode = B.productCategoryCode ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand AS C ON A.brandCode = C.brandCode ";

		strSql += " WHERE A.companyCode = '" + companyCode + "'";

		if (BrandCode != "" && !BrandCode.equals("ALL")) {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND  A.brandCode in ('" + BrandCode + "')";
		}

		if (CategoryCode != "" && !CategoryCode.equals("ALL")) {
			CategoryCode = CategoryCode.replace("|", "','");
			strSql += " AND A.masterCategoryCode in ('" + CategoryCode + "')";
		}

		strSql += "   ORDER BY C.brandCodeName,B.productCategoryCodeName,A.productName";

		logger.info("master_n_product:" + strSql);

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel brandPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
		List<Object> dictData = page.getResultList();

		List<Object> listData = new ArrayList<Object>();

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();

		// 循环查询到该末级分类下的产品
		for (int i = 0; i < dictData.size(); i++) {
			Map<String, String> mapBrand = new HashMap<String, String>();
			mapBrand = (Map<String, String>) dictData.get(i);
			String productCode = mapBrand.get("productCode");
			String productName = mapBrand.get("productName");
			String productCategoryCodeName = mapBrand.get("productCategoryCodeName");
			String brandCodeName = mapBrand.get("brandCodeName");
			String brandCode = mapBrand.get("brandCode");

			// ======本日销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS todayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS todaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate = '" + businessDateStart + "' ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "			 AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel pm = new PageModel();
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataToday = page.getResultList();

			Map<String, String> mapToday = new HashMap<String, String>();
			mapToday = (Map<String, String>) dataToday.get(0);
			String todayNum = ((Object) mapToday.get("todayNum")).toString();
			String todaySales = ((Object) mapToday.get("todaySales")).toString();

			// ======上周本日销量及销售额=================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekDayNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekDaySales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE businessDate =  DATE_ADD('" + businessDateStart + "', INTERVAL - 1 WEEK) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "			 AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeekDay = page.getResultList();

			Map<String, String> mapLastWeekDay = new HashMap<String, String>();
			mapLastWeekDay = (Map<String, String>) dataLastWeekDay.get(0);
			String lastWeekDayNum = ((Object) mapLastWeekDay.get("lastWeekDayNum")).toString();
			String lastWeekDaySales = ((Object) mapLastWeekDay.get("lastWeekDaySales")).toString();

			// ======本周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS thisWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK('" + businessDateStart + "',1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisWeek = page.getResultList();

			Map<String, String> mapThisWeek = new HashMap<String, String>();
			mapThisWeek = (Map<String, String>) dataThisWeek.get(0);
			String thisWeekNum = ((Object) mapThisWeek.get("thisWeekNum")).toString();
			String thisWeekSales = ((Object) mapThisWeek.get("thisWeekSales")).toString();

			// ======上周销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += "	SELECT IFNULL(SUM(menuItemtNum),0) AS lastWeekNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastWeekSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK)) ";
			strSql += "			 AND WEEK(businessDate,1) = WEEK(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 WEEK),1) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastWeek = page.getResultList();

			Map<String, String> mapLastWeek = new HashMap<String, String>();
			mapLastWeek = (Map<String, String>) dataLastWeek.get(0);
			String lastWeekNum = ((Object) mapLastWeek.get("lastWeekNum")).toString();
			String lastWeekSales = ((Object) mapLastWeek.get("lastWeekSales")).toString();

			// ======本月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS thisMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS thisMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR('" + businessDateStart + "') ";
			strSql += "			 AND MONTH(businessDate) = MONTH('" + businessDateStart + "') ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataThisMonth = page.getResultList();

			Map<String, String> mapThisMonth = new HashMap<String, String>();
			mapThisMonth = (Map<String, String>) dataThisMonth.get(0);
			String thisMonthNum = ((Object) mapThisMonth.get("thisMonthNum")).toString();
			String thisMonthSales = ((Object) mapThisMonth.get("thisMonthSales")).toString();

			// ======上月销量及销售额==================================================
			strSql = "";
			totalSQL = "";
			strSql += " SELECT IFNULL(SUM(menuItemtNum),0) AS lastMonthNum ";
			strSql += "		  ,IFNULL(SUM(amountPrice - itemDiscount - sDiscount),0) AS lastMonthSales ";
			strSql += "			FROM rep_sales_item_product_sales ";
			strSql += "		 WHERE YEAR(businessDate) = YEAR(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND MONTH(businessDate) = MONTH(DATE_ADD('" + businessDateStart
					+ "', INTERVAL - 1 MONTH)) ";
			strSql += "			 AND companyCode = '" + companyCode + "' ";
			strSql += "			 AND brandCode = '" + brandCode + "' ";
			strSql += "       AND productCode = '" + productCode + "' ";
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}
			totalSQL = "select count(1) from (" + strSql + ") as Tab";
			page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, pm);
			List<Object> dataLastMonth = page.getResultList();

			Map<String, String> mapLastMonth = new HashMap<String, String>();
			mapLastMonth = (Map<String, String>) dataLastMonth.get(0);
			String lastMonthNum = ((Object) mapLastMonth.get("lastMonthNum")).toString();
			String lastMonthSales = ((Object) mapLastMonth.get("lastMonthSales")).toString();
			
			

			// 明细数据
			strSql = "";
			totalSQL = "";

			strSql += "SELECT '" + brandCodeName + "' AS '所属品牌' ";
			strSql += "  ,'" + productCategoryCodeName + "'  AS '所属分类' ";
			strSql += "  ,'" + productName + "'  AS '产品名称' ";
			strSql += "	 ,IFNULL(SUM(todayNum), 0) AS 'todayNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDayNum), 0) AS 'lastWeekDayNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todayNum) - SUM(lastWeekDayNum)) / SUM(lastWeekDayNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'dayNumRate' ";
			strSql += "	 ,IFNULL(SUM(todaySales), 0) AS 'todaySales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekDaySales), 0) AS 'lastWeekDaySales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(todaySales) - SUM(lastWeekDaySales)) / SUM(lastWeekDaySales),4) * 100,2),CHAR (100)),'%'),'--') AS 'daySalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisWeekNum), 0) AS 'thisWeekNum' ";
			strSql += "	 ,IFNULL(SUM(lastWeekNum), 0) AS 'lastWeekNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekNum) - SUM(lastWeekNum)) / SUM(lastWeekNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekNumRate' ";
			strSql += "  ,IFNULL(SUM(thisWeekSales), 0) AS 'thisWeekSales' ";
			strSql += "	 ,IFNULL(SUM(lastWeekSales), 0) AS 'lastWeekSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisWeekSales) - SUM(lastWeekSales)) / SUM(lastWeekSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'weekSalesRate' ";
			strSql += "	 ,IFNULL(SUM(thisMonthNum), 0) AS 'thisMonthNum' ";
			strSql += "	 ,IFNULL(SUM(lastMonthNum), 0) AS 'lastMonthNum' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthNum) - SUM(lastMonthNum)) / SUM(lastMonthNum),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthNumRate' ";
			strSql += "  ,IFNULL(SUM(thisMonthSales), 0) AS 'thisMonthSales' ";
			strSql += "	 ,IFNULL(SUM(lastMonthSales), 0) AS 'lastMonthSales' ";
			strSql += "	 ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((SUM(thisMonthSales) - SUM(lastMonthSales)) / SUM(lastMonthSales),4) * 100,2),CHAR (100)),'%'),'--') AS 'monthSalesRate' ";
			strSql += " FROM ";
			strSql += "	( ";

			strSql += "		SELECT '" + productName + "' AS productName ";
			strSql += " ," + todayNum + " AS 'todayNum' ";
			strSql += "	," + lastWeekDayNum + " AS 'lastWeekDayNum' ";
			strSql += "," + thisWeekNum + " AS 'thisWeekNum' ";
			strSql += "," + lastWeekNum + " AS 'lastWeekNum' ";

			strSql += "," + thisMonthNum + " AS 'thisMonthNum' ";
			strSql += "," + lastMonthNum + " AS 'lastMonthNum' ";

			strSql += "," + todaySales + " AS 'todaySales' ";
			strSql += "," + lastWeekDaySales + " AS 'lastWeekDaySales' ";
			strSql += "," + thisWeekSales + " AS 'thisWeekSales' ";
			strSql += "," + lastWeekSales + " AS 'lastWeekSales' ";

			strSql += "," + thisMonthSales + " AS 'thisMonthSales' ";
			strSql += "," + lastMonthSales + " AS 'lastMonthSales' ";


			strSql += " ) AS M ";

			totalSQL = "";
			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int todayNum = 0;
		int lastWeekDayNum = 0;
		double todaySales = 0;
		double lastWeekDaySales = 0;
		int thisWeekNum = 0;
		int lastWeekNum = 0;
		double thisWeekSales = 0;
		double lastWeekSales = 0;
		int thisMonthNum = 0;
		int lastMonthNum = 0;
		double thisMonthSales = 0;
		double lastMonthSales = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			

			todayNum += Integer.parseInt(((Object) mapValue.get("todayNum")).toString());
			lastWeekDayNum += Integer.parseInt(((Object) mapValue.get("lastWeekDayNum")).toString());
			todaySales += Double.parseDouble(((Object) mapValue.get("todaySales")).toString());
			lastWeekDaySales += Double.parseDouble(((Object) mapValue.get("lastWeekDaySales")).toString());
			thisWeekNum += Integer.parseInt(((Object) mapValue.get("thisWeekNum")).toString());
			lastWeekNum += Integer.parseInt(((Object) mapValue.get("lastWeekNum")).toString());
			thisWeekSales += Double.parseDouble(((Object) mapValue.get("thisWeekSales")).toString());
			lastWeekSales += Double.parseDouble(((Object) mapValue.get("lastWeekSales")).toString());
			thisMonthNum += Integer.parseInt(((Object) mapValue.get("thisMonthNum")).toString());
			lastMonthNum += Integer.parseInt(((Object) mapValue.get("lastMonthNum")).toString());
			thisMonthSales += Double.parseDouble(((Object) mapValue.get("thisMonthSales")).toString());
			lastMonthSales += Double.parseDouble(((Object) mapValue.get("lastMonthSales")).toString());
		}

		strSql = "";
		strSql += "SELECT";
		strSql += " '合计' AS '所属品牌' ";
		strSql += " ,'' AS '所属分类' ";
		strSql += " ,'' AS '产品名称' ";
		strSql += " ," + todayNum + " AS '本日销量' ";
		strSql += " ," + lastWeekDayNum + " AS '上周本日销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todayNum + " - " + lastWeekDayNum + ") / " + lastWeekDayNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '日销量环比'";
		strSql += " ,ROUND(" + todaySales + ",2) AS '本日实际收入' ";
		strSql += " ,ROUND(" + lastWeekDaySales + ",2) AS '上周本日实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + todaySales + " - " + lastWeekDaySales + ") / "
				+ lastWeekDaySales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '日实际收入环比'";

		strSql += " ," + thisWeekNum + " AS '本周销量' ";
		strSql += " ," + lastWeekNum + " AS '上周销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekNum + " - " + lastWeekNum + ") / " + lastWeekNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '周销量环比'";
		strSql += " ,ROUND(" + thisWeekSales + ",2) AS '本周实际收入' ";
		strSql += " ,ROUND(" + lastWeekSales + ",2) AS '上周实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisWeekSales + " - " + lastWeekSales + ") / "
				+ lastWeekSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '周实际收入环比'";

		strSql += " ," + thisMonthNum + " AS '本月销量' ";
		strSql += " ," + lastMonthNum + " AS '上月销量' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthNum + " - " + lastMonthNum + ") / " + lastMonthNum
				+ ",4) * 100,2),CHAR (100)),'%'),'--') AS '月销量环比'";
		strSql += " ,ROUND(" + thisMonthSales + ",2) AS '本月实际收入' ";
		strSql += " ,ROUND(" + lastMonthSales + ",2) AS '上月实际收入' ";
		strSql += " ,IFNULL(CONCAT(CONVERT (ROUND(ROUND((" + thisMonthSales + " - " + lastMonthSales + ") / "
				+ lastMonthSales + ",4) * 100,2),CHAR (100)),'%'),'--') AS '月实际收入环比'";

		logger.info("productSum:" + strSql);

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;

	}

}
