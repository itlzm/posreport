package com.dev.core.dao.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.dao.ActivityDao;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;

@Repository
public class ActivityDaoImpl implements ActivityDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Logger logger = Logger.getLogger(ActivityDaoImpl.class);

	/*
	 * ======�����������������(Ʒ�ƻ���)==================================================
	 */
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepChannelActivityBrand(Map<String, String> map) {
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
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

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
		strSql += " ,SUM(sDiscount) AS 'sDiscount'";
		strSql += " ,SUM(subNetAmount) AS 'subNetAmount'";
		strSql += " ,ROUND(SUM(sNetAmountPrice)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}
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

		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		// ��ѯƷ����Ϣ
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

			// ��ϸ����
			strSql = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCodeTemp + "','|=|','" + brandCodeNameTemp + "'" + ",'|=|','"
					+ areaCodeTemp + "'),'') AS 'Ʒ��' ";
			// strSql += " ,D.channelName AS '��������' ";
			// strSql += " ,orderTypeDesc AS '������ʽ' ";
			// strSql += " ,favourableName AS '�Ż�����' ";
			strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
			strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
			strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
			
			strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
			strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
			strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
			strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
			strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

			
			strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
			strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
			strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
			strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (brandCodeTemp != "") {
				strSql += " AND A.brandCode = '" + brandCodeTemp + "'";
			}
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

			// strSql += " GROUP BY ";
			// strSql += " orderTypeDesc ";
			// strSql += " ,D.channelName";
			// strSql += " ,favourableName";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS 'Ʒ��' ";
		// strSql += " '' AS '��������' ";
		// strSql += " ,'' AS '������ʽ' ";
		// strSql += " ,'' AS '�Ż�����' ";

		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

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
	 * ======�����������������(�������)==================================================
	 */
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepChannelActivityBrandChildren(Map<String, String> map) {
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
		String AreaCode = map.get("AreaCode");
		String BrandName = map.get("BrandName");

		String strSql = "";
		String totalSQL = "";

		String AreaCodeALL = "";

		// ����
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
			// ��ȡ���������������������
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

			// ��ϸ����
			strSql = "";
			totalSQL = "";
			strSql += "SELECT '" + BrandName + "' AS '����Ʒ��' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '����' ";

			strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
			strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
			strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
			
			strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
			strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
			strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
			strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
			strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

			strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
			strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
			strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
			strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (BrandCode != "") {
				strSql += " AND A.brandCode = '" + BrandCode + "'";
			}

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

			strSql += "   AND B.areaCode IN (" + allChildrenCode + ") ";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}
		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int OrderCount = 0;
		double sOrderTotalAmount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;
		double sCommissionAmount = 0;
		double businessFavourableAmount = 0;
		double platformFavourableAmount = 0;
		double subNetAmount = 0;
		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);

			logger.info(mapValue);

			OrderCount += Integer.parseInt(((Object) mapValue.get("ʹ�ô���")).toString());
			sOrderTotalAmount += Double.parseDouble(((Object) mapValue.get("��������̯��")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݽ��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�������루��̯��")).toString());
			sCommissionAmount+=Double.parseDouble(((Object) mapValue.get("ƽ̨Ӷ�𣨷�̯��")).toString());
			businessFavourableAmount+=Double.parseDouble(((Object) mapValue.get("�̼ҳе��Żݽ��")).toString());
			platformFavourableAmount+=Double.parseDouble(((Object) mapValue.get("ƽ̨�е��Żݽ��")).toString());
			subNetAmount+=Double.parseDouble(((Object) mapValue.get("ʵ�����루��̯��")).toString());
			
		}
		
		
		
	
	
		

		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS '����Ʒ��' ";
		strSql += " ,'' AS '����' ";

		strSql += " ,'" + OrderCount + "' AS 'ʹ�ô���' ";
		strSql += " ,'" + sOrderTotalAmount + "' AS '��������̯��' ";
		strSql += " ,'" + sDiscount + "' AS '�Żݽ��' ";
		
		strSql += " ,'" + sNetAmountPrice + "' AS '�������루��̯��' ";
		strSql += " ,'" + sCommissionAmount + "' AS 'ƽ̨Ӷ�𣨷�̯��' ";
		strSql += " ,'" + businessFavourableAmount + "' AS '�̼ҳе��Żݽ��' ";
		strSql += " ,'" + platformFavourableAmount + "' AS 'ƽ̨�е��Żݽ��' ";
		strSql += " ,'" + subNetAmount + "' AS 'ʵ�����루��̯��' ";
		
	
		if (OrderCount == 0) {
			strSql += " ,'0' AS '���ζ������루��̯��' ";
			strSql += " ,'0' AS '����ʵ�����루��̯��' ";
		} else {
			strSql += " ,IFNULL(ROUND(" + (sNetAmountPrice / OrderCount) + ",2),0) AS '���ζ������루��̯��' ";
			strSql += " ,IFNULL(ROUND(" + (subNetAmount / OrderCount) + ",2),0) AS '����ʵ�����루��̯��' ";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		AreaCodeALL = AreaCodeALL.substring(0, AreaCodeALL.length() - 1);

		strSql = "";
		// ��ͼ��Ȧ����
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

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

		strSql += "   AND B.areaCode IN (" + AreaCodeALL + ") ";

		strSql += " GROUP BY C.orderTypeDesc";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageBT = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageBT);
		List<Object> BTNData = page.getResultList();

		// ��ͼ��Ȧ����״ͼ����
		strSql = "";
		strSql += "SELECT D.channelName AS 'channelName'";
		strSql += " ,SUM(OrderCount) AS 'OrderCount'";
		strSql += " ,SUM(sOrderTotalAmount) AS 'sOrderTotalAmount'";
		strSql += " ,SUM(sDiscount) AS 'sDiscount'";
		strSql += " ,SUM(subNetAmount) AS 'subNetAmount'";
		strSql += " ,ROUND(SUM(sNetAmountPrice)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}
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
		strSql += "   AND B.areaCode IN (" + AreaCodeALL + ") ";

		strSql += " GROUP BY D.channelName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		queryDataMap.put(new String("BTNData"), BTNData);
		queryDataMap.put(new String("ZZTData"), ZZTData);
		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�����������������(�ŵ����)==================================================
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, List<Object>> queryRepChannelActivity(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String AreaName = map.get("AreaName");

		String favourableName = map.get("favourableName");

		String strSql = "";
		String totalSQL = "";
		// ��ͼ��Ȧ����
		strSql += "SELECT C.orderTypeDesc AS orderTypeDesc";
		strSql += " ,SUM(OrderCount) AS OrderCount";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}

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
		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

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
		strSql += " ,SUM(sDiscount) AS 'sDiscount'";
		strSql += " ,SUM(subNetAmount) AS 'subNetAmount'";
		strSql += " ,ROUND(SUM(sNetAmountPrice)/SUM(OrderCount),2) AS 'kdPrice'";
		strSql += " FROM vrep_sales_daily_store_favourable AS A";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";
		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
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

		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

		strSql += " GROUP BY D.channelName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageZZ = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageZZ);
		List<Object> ZZTData = page.getResultList();

		strSql = "";
		totalSQL = "";
		strSql += "SELECT ";
		strSql += "'" + BrandName + "' AS '����Ʒ��'";
		strSql += ",'" + AreaName + "' AS '��������'";

		strSql += " ,D.channelName AS '��������' ";
		strSql += " ,orderTypeDesc AS '������ʽ' ";
		strSql += " ,favourableName AS '�Ż�����' ";
		strSql += " ,SUM(OrderCount) AS 'ʹ�ô���' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '��������̯��' ";
		strSql += " ,SUM(sDiscount) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
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

		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

		strSql += " GROUP BY ";
		strSql += " orderTypeDesc ";
		strSql += " ,D.channelName";
		strSql += " ,favourableName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS '����Ʒ��' ";
		strSql += " ,'' AS '��������' ";
		strSql += " ,'' AS '��������' ";
		strSql += " ,'' AS '������ʽ' ";
		strSql += " ,'' AS '�Ż�����' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		
		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
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

		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

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
	 * ======�������������������Excel=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepChannelActivityExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String ChannelCode = map.get("ChannelCode");
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String AreaName = map.get("AreaName");

		String favourableName = map.get("favourableName");

		String strSql = "";
		String totalSQL = "";

		strSql = "";
		totalSQL = "";
		strSql += "SELECT ";
		strSql += " D.channelName AS '��������' ";
		strSql += " ,orderTypeDesc AS '������ʽ' ";
		strSql += " ,favourableName AS '�Ż�����' ";
		strSql += " ,SUM(OrderCount) AS 'ʹ�ô���' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '��������̯��' ";
		strSql += " ,SUM(sDiscount) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		
		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
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

		strSql += " GROUP BY ";
		strSql += " orderTypeDesc ";
		strSql += " ,D.channelName";
		strSql += " ,favourableName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += " '�ϼ�' AS '��������' ";
		strSql += " ,'' AS '������ʽ' ";
		strSql += " ,'' AS '�Ż�����' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
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
	 * ======�����Աȷ�������(Ʒ�ƻ���)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityTransverseBrand(Map<String, String> map) {
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

		// �Żݻ����Ա�ͼ
		strSql = "";
		totalSQL = "";
		strSql += "SELECT";
		strSql += " A.favourableName AS 'favourableName'";
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
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageDBT);
		List<Object> DBTData = page.getResultList();

		// ��ѯƷ����Ϣ
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

			// ��ϸ����
			strSql = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCodeTemp + "','|=|','" + brandCodeNameTemp + "'" + ",'|=|','"
					+ areaCodeTemp + "'),'') AS 'Ʒ��' ";
			// strSql += " ,D.channelName AS '��������' ";
			// strSql += " ,orderTypeDesc AS '������ʽ' ";
			// strSql += " ,favourableName AS '�Ż�����' ";
			strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
			strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
			strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
			
			strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
			strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
			strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
			strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
			strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

			
			strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
			strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
			strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
			strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (brandCodeTemp != "") {
				strSql += " AND A.brandCode = '" + brandCodeTemp + "'";
			}
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

			// strSql += " GROUP BY ";
			// strSql += " orderTypeDesc ";
			// strSql += " ,D.channelName";
			// strSql += " ,favourableName";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS 'Ʒ��' ";
		// strSql += " '' AS '��������' ";
		// strSql += " ,'' AS '������ʽ' ";
		// strSql += " ,'' AS '�Ż�����' ";

		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += "  AND A.brandCode in ('" + BrandCode + "')";
		}

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

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�����Աȷ�������(�������)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityTransverseBrandChildren(Map<String, String> map) {
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
		String AreaCode = map.get("AreaCode");
		String BrandName = map.get("BrandName");

		String strSql = "";
		String totalSQL = "";

		String AreaCodeALL = "";

		// ����
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
			// ��ȡ���������������������
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

			// ��ϸ����
			strSql = "";
			totalSQL = "";
			strSql += "SELECT '" + BrandName + "' AS '����Ʒ��' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '����' ";

			strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
			strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
			strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
			
			strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
			strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
			strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
			strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
			strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
			strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

			strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
			strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
			strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
			strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

			strSql += "  WHERE A.companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (BrandCode != "") {
				strSql += " AND A.brandCode = '" + BrandCode + "'";
			}

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

			strSql += "   AND B.areaCode IN (" + allChildrenCode + ") ";

			totalSQL = "";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageTemp);
			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}
		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int OrderCount = 0;
		double sOrderTotalAmount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;
		double sCommissionAmount = 0;
		double businessFavourableAmount = 0;
		double platformFavourableAmount = 0;
		double subNetAmount = 0;
		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);

			logger.info(mapValue);

			OrderCount += Integer.parseInt(((Object) mapValue.get("ʹ�ô���")).toString());
			sOrderTotalAmount += Double.parseDouble(((Object) mapValue.get("��������̯��")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݽ��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�������루��̯��")).toString());
		
			sCommissionAmount+=Double.parseDouble(((Object) mapValue.get("ƽ̨Ӷ�𣨷�̯��")).toString());
			businessFavourableAmount+=Double.parseDouble(((Object) mapValue.get("�̼ҳе��Żݽ��")).toString());
			platformFavourableAmount+=Double.parseDouble(((Object) mapValue.get("ƽ̨�е��Żݽ��")).toString());
			subNetAmount +=Double.parseDouble(((Object) mapValue.get("ʵ�����루��̯��")).toString());
			
		}

		
		
		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS '����Ʒ��' ";
		strSql += " ,'' AS '����' ";

		strSql += " ,'" + OrderCount + "' AS 'ʹ�ô���' ";
		strSql += " ,'" + sOrderTotalAmount + "' AS '��������̯��' ";
		strSql += " ,'" + sDiscount + "' AS '�Żݽ��' ";
		
		strSql += " ,'" + sNetAmountPrice + "' AS '�������루��̯��' ";
		strSql += " ,'" + sCommissionAmount + "' AS 'ƽ̨Ӷ�𣨷�̯��' ";
		strSql += " ,'" + businessFavourableAmount + "' AS '�̼ҳе��Żݽ��' ";
		strSql += " ,'" + platformFavourableAmount + "' AS 'ƽ̨�е��Żݽ��' ";
		strSql += " ,'" + subNetAmount + "' AS 'ʵ�����루��̯��' ";

		if (OrderCount == 0) {
			strSql += " ,'0' AS '���ζ������루��̯��' ";
			strSql += " ,'0' AS '����ʵ�����루��̯��' ";
		} else {
			strSql += " ,IFNULL(ROUND(" + (sNetAmountPrice / OrderCount) + ",2),0) AS '���ζ������루��̯��' ";
			strSql += " ,IFNULL(ROUND(" + (subNetAmount / OrderCount) + ",2),0) AS '����ʵ�����루��̯��' ";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		AreaCodeALL = AreaCodeALL.substring(0, AreaCodeALL.length() - 1);

		strSql = "";
		// �Żݻ����Ա�ͼ
		strSql = "";
		totalSQL = "";
		strSql += "SELECT";
		strSql += " A.favourableName AS 'favourableName'";
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
		strSql += "   AND B.areaCode IN (" + AreaCodeALL + ") ";

		strSql += " GROUP BY A.favourableName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageDBT = new PageModel();
		page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageDBT);
		List<Object> DBTData = page.getResultList();

		queryDataMap.put(new String("DBTData"), DBTData);
		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�����Աȷ�������(�ŵ����)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityTransverse(Map<String, String> map) {
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
		String BrandName = map.get("BrandName");
		String AreaName = map.get("AreaName");
		String AreaCode = map.get("AreaCode");

		String strSql = "";
		String totalSQL = "";

		// �Żݻ����Ա�ͼ
		strSql = "";
		totalSQL = "";
		strSql += "SELECT";
		strSql += " A.favourableName AS 'favourableName'";
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

		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

		strSql += " GROUP BY A.favourableName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageDBT = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, listDataPageDBT);
		List<Object> DBTData = page.getResultList();

		strSql = "";
		totalSQL = "";

		strSql += "SELECT";
		strSql += "'" + BrandName + "' AS '����Ʒ��'";
		strSql += ",'" + AreaName + "' AS '��������'";
		strSql += " ,favourableName AS '�Ż�����' ";
		strSql += " ,SUM(OrderCount) AS 'ʹ�ô���' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '��������̯��' ";
		strSql += " ,SUM(sDiscount) AS '�Żݽ��' ";
	
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

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
		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

		strSql += " GROUP BY ";
		// strSql += " A.businessDate ";
		// strSql += " ,B.storeCodeName ";
		// strSql += " orderTypeDesc ";
		// strSql += " ,D.channelName";
		strSql += " favourableName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT";
		strSql += "'�ϼ�' AS '����Ʒ��'";
		strSql += ",'' AS '��������'";
		strSql += " ,'' AS '�Ż�����' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

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
		strSql += "   AND B.areaCode = '" + AreaCode + "' ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�����Աȷ���������Excel=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityTransverseExportExcel(Map<String, String> map) {
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
		String BrandName = map.get("BrandName");
		String AreaName = map.get("AreaName");
		String AreaCode = map.get("AreaCode");

		String strSql = "";
		String totalSQL = "";

		// �Żݻ����Ա�ͼ
		strSql = "";
		totalSQL = "";
		strSql += "SELECT ";
		strSql += " A.favourableName AS 'favourableName'";
		strSql += ",SUM(OrderCount) AS 'OrderCount'";
		strSql += ",SUM(sOrderTotalAmount) AS 'sOrderTotalAmount'";
		strSql += ",-1*SUM(subNetDiscount) AS 'subNetDiscount'";
		strSql += ",SUM(sNetAmountPrice) AS 'sNetAmountPrice'";
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
		Pagination page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageDBT);
		List<Object> DBTData = page.getResultList();

		strSql = "";
		totalSQL = "";

		strSql += "SELECT";
		strSql += " favourableName AS '�Ż�����' ";
		strSql += " ,SUM(OrderCount) AS 'ʹ�ô���' ";
		strSql += " ,SUM(sOrderTotalAmount) AS '��������̯��' ";
		strSql += " ,SUM(sDiscount) AS '�Żݽ��' ";
	
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

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

		strSql += " GROUP BY ";
		// strSql += " A.businessDate ";
		// strSql += " ,B.storeCodeName ";
		// strSql += " orderTypeDesc ";
		// strSql += " ,D.channelName";
		strSql += " favourableName";

		totalSQL = "";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";
		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT";
		strSql += " '�ϼ�' AS '�Ż�����' ";
		strSql += " ,IFNULL(SUM(OrderCount),0) AS 'ʹ�ô���' ";
		strSql += " ,IFNULL(SUM(sOrderTotalAmount),0) AS '��������̯��' ";
		strSql += " ,IFNULL(SUM(sDiscount),0) AS '�Żݽ��' ";
		
		strSql += " ,IFNULL(SUM(sNetAmountPrice), 0) AS '�������루��̯��'";
		strSql += " ,IFNULL(SUM(sCommissionAmount), 0) AS 'ƽ̨Ӷ�𣨷�̯��'";
		strSql += " ,IFNULL(SUM(businessFavourableAmount), 0) AS '�̼ҳе��Żݽ��'";
		strSql += " ,IFNULL(SUM(platformFavourableAmount), 0) AS 'ƽ̨�е��Żݽ��'";
		strSql += " ,IFNULL(SUM(subNetAmount), 0) AS 'ʵ�����루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(sNetAmountPrice) / SUM(OrderCount),2),0) AS '���ζ������루��̯��'";
		strSql += " ,IFNULL(ROUND(SUM(subNetAmount) / SUM(OrderCount),2),0) AS '����ʵ�����루��̯��'";

		strSql += "   FROM vrep_sales_daily_store_favourable AS A ";
		strSql += " LEFT OUTER JOIN vrep_dict_store AS B ON A.storeCode = B.storeCode ";
		strSql += " LEFT OUTER JOIN rep_dict_ordertype AS C ON A.OrderType = C.OrderType ";
		strSql += " LEFT OUTER JOIN vrep_dict_brand_channel AS D ON (A.companyCode = D.companyCode AND A.brandCode = D.brandCode AND A.salesChannelKey = D.channelKey) ";

		strSql += "  WHERE A.companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

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

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPageSum = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPageSum);
		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("DBTData"), DBTData);

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(Ʒ�ƻ���)=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepMealSingleBrand(Map<String, String> map) {
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

		// ��ѯƷ����Ϣ
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
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
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

			// ��ϸ����
			strSql = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCodeTemp + "','|=|','" + brandCodeNameTemp + "'" + ",'|=|','"
					+ areaCodeTemp + "'),'') AS 'Ʒ��' ";
			strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
			strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
			strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
			strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
			strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
			strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
			strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
			strSql += " WHERE ";
			strSql += "  A.storeCode = C.storeCode";

			strSql += "  AND A.companyCode = '" + companyCode + "'";
			strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
					+ "')";

			if (storeCode != "") {
				storeCode = storeCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + storeCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + storeCode + "')";
			}

			if (brandCodeTemp != "") {
				strSql += " AND A.brandCode = '" + brandCodeTemp + "'";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageTemp);

			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT '�ϼ�' AS 'Ʒ��'";
		strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
		strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
		strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
		strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
		strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
		strSql += " WHERE  A.storeCode = C.storeCode";

		strSql += "  AND A.companyCode = '" + companyCode + "'";
		strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		// �ŵ�
		if (storeCode != "") {
			storeCode = storeCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + storeCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + storeCode + "')";
		}

		if (BrandCode != "") {
			BrandCode = BrandCode.replace("|", "','");
			strSql += " AND A.brandCode in ('" + BrandCode + "')";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�������)=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepMealSingleBrandChildren(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");

		String strSql = "";
		String totalSQL = "";

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

			// ��ȡ���������������������
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
				String storeCodeTemp = mapTemoS.get("storeCode");
				if (storeCodeTemp != null && !storeCodeTemp.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// ��ϸ����
			strSql = "";
			totalSQL = "";
			strSql += "SELECT '" + BrandName + "' AS '����Ʒ��' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '����' ";

			strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
			strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
			strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
			strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
			strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
			strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
			strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
			
			strSql += " WHERE ";
			strSql += "  A.storeCode = C.storeCode";

			strSql += "  AND A.companyCode = '" + companyCode + "'";
			strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
					+ "')";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND A.storeCode in('" + StoreCode + "')";
			}

			if (BrandCode != "") {
				strSql += " AND A.brandCode = '" + BrandCode + "'";
			}

			strSql += "   AND C.areaCode IN (" + allChildrenCode + ") ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageTemp);

			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int orderCount = 0;
		double amountPrice = 0;
		double DiscountAmount = 0;
		double netAmountPrice = 0;
		double netAmountPriceorderCount = 0;
		int totSalesCount = 0;
		int i_SalesCount = 0;
		double i_NetAmountPrice = 0;
		double i_sDiscount = 0;
		int i_dSalesCount = 0;
		double i_dAmountPrice = 0;
		double i_dDiscount = 0;
		double i_dNetPrice = 0;
		int g_SalesCount = 0;
		double g_NetAmountPrice = 0;
		double g_sDiscount = 0;
		int g_dSalesCount = 0;
		double g_dAmountPrice = 0;
		double g_dDiscount = 0;
		double g_dNetPrice = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);

			logger.info(mapValue);

			orderCount += Integer.parseInt(((Object) mapValue.get("�ܶ�����")).toString());
			amountPrice += Double.parseDouble(((Object) mapValue.get("�����۶�")).toString());
			DiscountAmount += Double.parseDouble(((Object) mapValue.get("���ۿ۶�")).toString());
			netAmountPrice += Double.parseDouble(((Object) mapValue.get("�������")).toString());
			totSalesCount += Integer.parseInt(((Object) mapValue.get("��Ʒ�����ϼ�")).toString());
			i_SalesCount += Integer.parseInt(((Object) mapValue.get("���۵�Ʒ����")).toString());
			i_NetAmountPrice += Double.parseDouble(((Object) mapValue.get("���۵�Ʒ����")).toString());
			i_sDiscount += Double.parseDouble(((Object) mapValue.get("���۵�Ʒ��̯�����ۿ�")).toString());
			i_dSalesCount += Integer.parseInt(((Object) mapValue.get("�ۿ۵�Ʒ����")).toString());
			i_dAmountPrice += Double.parseDouble(((Object) mapValue.get("�ۿ۵�Ʒ���۽��")).toString());
			i_dDiscount += Double.parseDouble(((Object) mapValue.get("��Ʒ�ۿ۽��")).toString());
			i_dNetPrice += Double.parseDouble(((Object) mapValue.get("��Ʒ�ۿ�������")).toString());
			g_SalesCount += Integer.parseInt(((Object) mapValue.get("�����ײ������ϼ�")).toString());
			g_NetAmountPrice += Double.parseDouble(((Object) mapValue.get("�����ײ�����ϼ�")).toString());
			g_sDiscount += Double.parseDouble(((Object) mapValue.get("�����ײͷ�̯�����ۿ�")).toString());
			g_dSalesCount += Integer.parseInt(((Object) mapValue.get("�ۿ��ײ�����")).toString());
			g_dAmountPrice += Double.parseDouble(((Object) mapValue.get("�ۿ��ײ����۽��")).toString());
			g_dDiscount += Double.parseDouble(((Object) mapValue.get("�ۿ��ײʹ��۽��")).toString());
			g_dNetPrice += Double.parseDouble(((Object) mapValue.get("�ۿ��ײ�ʵ������")).toString());
		}

		strSql = "";
		strSql += "SELECT '�ϼ�' AS '����Ʒ��'";
		strSql += ", '' AS '����' ";
		strSql += ",'" + orderCount + "' AS '�ܶ�����'";
		strSql += ",CONVERT(ROUND('" + amountPrice + "',2),CHAR(100)) AS '�����۶�'";
		strSql += ",CONVERT(ROUND('" + DiscountAmount + "',2),CHAR(100)) AS '���ۿ۶�'";
		strSql += ",CONVERT(ROUND('" + netAmountPrice + "',2),CHAR(100)) AS '�������'";

		if (orderCount == 0) {
			strSql += ",'0.00' AS 'ƽ���͵���'";
		} else {
			strSql += ",IFNULL(CONVERT(ROUND(" + (amountPrice / orderCount) + ",2),CHAR(100)),0) AS 'ƽ���͵���'";
		}

		strSql += ",'" + totSalesCount + "' AS '��Ʒ�����ϼ�'";
		strSql += ",'" + i_SalesCount + "' AS '���۵�Ʒ����'";
		strSql += ",CONVERT(ROUND('" + i_NetAmountPrice + "',2),CHAR(100)) AS '���۵�Ʒ����'";
		strSql += ",CONVERT(ROUND('" + i_sDiscount + "',2),CHAR(100)) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",'" + i_dSalesCount + "' AS '�ۿ۵�Ʒ����'";
		strSql += ",CONVERT(ROUND('" + i_dAmountPrice + "',2),CHAR(100)) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",CONVERT(ROUND('" + i_dDiscount + "',2),CHAR(100)) AS '��Ʒ�ۿ۽��'";
		strSql += ",CONVERT(ROUND('" + i_dNetPrice + "',2),CHAR(100)) AS '��Ʒ�ۿ�������'";
		strSql += ",'" + g_SalesCount + "' AS '�����ײ������ϼ�'";
		strSql += ",CONVERT(ROUND('" + g_NetAmountPrice + "',2),CHAR(100)) AS '�����ײ�����ϼ�'";
		strSql += ",CONVERT(ROUND('" + g_sDiscount + "',2),CHAR(100)) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",'" + g_dSalesCount + "' AS '�ۿ��ײ�����'";
		strSql += ",CONVERT(ROUND('" + g_dAmountPrice + "',2),CHAR(100)) AS '�ۿ��ײ����۽��'";
		strSql += ",CONVERT(ROUND('" + g_dDiscount + "',2),CHAR(100)) AS '�ۿ��ײʹ��۽��'";
		strSql += ",CONVERT(ROUND('" + g_dNetPrice + "',2),CHAR(100)) AS '�ۿ��ײ�ʵ������'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�ŵ����)=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepMealSingle(Map<String, String> map) {
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
		String AreaName = map.get("AreaName");
		String strSql = "";
		String totalSQL = "";
		strSql += "SELECT";
		strSql += "'" + BrandName + "' AS '����Ʒ��'";
		strSql += ",'" + AreaName + "' AS '��������'";
		strSql += ",C.storeCodeName AS '�ŵ�'";
		strSql += ",C.storeTypeName AS '�ŵ�����'";
		strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
		strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
		strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
		strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
		strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
		strSql += " WHERE ";
		strSql += "  A.storeCode = C.storeCode";

		strSql += "  AND A.companyCode = '" + companyCode + "'";
		strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}

		strSql += "   AND C.areaCode = '" + AreaCode + "' ";

		strSql += " GROUP BY C.storeCodeName,C.storeTypeName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'�ϼ�' AS '����Ʒ��'";
		strSql += ",'' AS '��������'";
		strSql += ",'' AS '�ŵ�'";
		strSql += ",'' AS '�ŵ�����' ";
		strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
		strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
		strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
		strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
		strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
		strSql += " WHERE  A.storeCode = C.storeCode";

		strSql += "  AND A.companyCode = '" + companyCode + "'";
		strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}
		strSql += "   AND C.areaCode = '" + AreaCode + "' ";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷���������Excel=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepMealSingleExportExcel(Map<String, String> map) {
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
		strSql += "SELECT ";
		strSql += " C.storeCodeName AS '�ŵ�'";
		strSql += ",C.storeTypeName AS '�ŵ�����'";
		strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
		strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
		strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
		strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
		strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
		strSql += " WHERE ";
		strSql += "  A.storeCode = C.storeCode";

		strSql += "  AND A.companyCode = '" + companyCode + "'";
		strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}

		strSql += " GROUP BY C.storeCodeName,C.storeTypeName";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		strSql = "";
		strSql += "SELECT ";
		strSql += "'�ϼ�' AS '�ŵ�'";
		strSql += ",'' AS '�ŵ�����' ";
		strSql += ",IFNULL(SUM(A.orderCount),0) AS '�ܶ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.amountPrice),2),CHAR(100)),0) AS '�����۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetDiscount),2),CHAR(100)),0) AS '���ۿ۶�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�������'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount)/SUM(A.orderCount),2),CHAR(100)),0) AS 'ƽ���͵���'";
		strSql += ",IFNULL(SUM(A.totSalesCount),0) AS '��Ʒ�����ϼ�'";
		strSql += ",IFNULL(SUM(A.i_SalesCount),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_NetAmountPrice),2),CHAR(100)),0) AS '���۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_sDiscount),2),CHAR(100)),0) AS '���۵�Ʒ��̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.i_dSalesCount),0) AS '�ۿ۵�Ʒ����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dAmountPrice),2),CHAR(100)),0) AS '�ۿ۵�Ʒ���۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dDiscount),2),CHAR(100)),0) AS '��Ʒ�ۿ۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.i_dNetPrice),2),CHAR(100)),0) AS '��Ʒ�ۿ�������'";
		strSql += ",IFNULL(SUM(A.g_SalesCount),0) AS '�����ײ������ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_NetAmountPrice),2),CHAR(100)),0) AS '�����ײ�����ϼ�'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_sDiscount),2),CHAR(100)),0) AS '�����ײͷ�̯�����ۿ�'";
		strSql += ",IFNULL(SUM(A.g_dSalesCount),0) AS '�ۿ��ײ�����'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dAmountPrice),2),CHAR(100)),0) AS '�ۿ��ײ����۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dDiscount),2),CHAR(100)),0) AS '�ۿ��ײʹ��۽��'";
		strSql += ",IFNULL(CONVERT(ROUND(SUM(A.g_dNetPrice),2),CHAR(100)),0) AS '�ۿ��ײ�ʵ������'";
		strSql += " FROM rep_sales_daily_store_discount AS A ,vrep_dict_store AS C";
		strSql += " WHERE  A.storeCode = C.storeCode";

		strSql += "  AND A.companyCode = '" + companyCode + "'";
		strSql += " AND (A.businessDate >= '" + businessDateStart + "' AND A.businessDate <= '" + businessDateEnd
				+ "')";

		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND A.storeCode in('" + StoreCode + "')";
		}

		// ����
		if (BrandCode != "") {
			strSql += " AND A.brandCode = '" + BrandCode + "'";
		}

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�Ӫ�չ��׷�������(Ʒ�ƻ���)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityRevenueBrand(Map<String, String> map) {
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

		// ��ѯƷ����Ϣ
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
		Pagination page = new Pagination(strSql, 1, 1000, jdbcTemplate, totalSQL, brandPage);
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

			// ��ϸ����
			strSql = "";

			strSql += "SELECT ";
			strSql += "IFNULL(CONCAT('" + brandCodeTemp + "','|=|','" + brandCodeNameTemp + "'" + ",'|=|','"
					+ areaCodeTemp + "'),'') AS 'Ʒ��' ";

			strSql += ",IFNULL(SUM(A.OrderCount),0) AS '�Żݻ����'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.OrderCount)/SUM(B.subOrderCount),4) * 100,2),CHAR(100)),'%'),0) AS '�ܵ���ռ��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.sDiscount),2),CHAR(100)),0) AS '�Żݻ�ۿ۽��'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.sDiscount)/SUM(B.subDiscountAmount),4) * 100,2),CHAR(100)),'%'),0) AS '���ۿ�ռ��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�Żݻ����'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.subNetAmount)/SUM(B.subNetAmount),4) * 100,2),CHAR(100)),'%'),0) AS '������ռ��'";
			// strSql += ",IFNULL(CONVERT(ROUND(SUM(A.TC),2),CHAR(100)),0) AS
			// '�Żݻ�͵���'";
			// strSql += ",IFNULL(CONVERT(ROUND(SUM(B.subTC),2),CHAR(100)),0) AS
			// 'ƽ���͵���'";
			strSql += " FROM ";
			strSql += "(SELECT companyCode,storeCode";
			strSql += ",SUM(OrderCount) AS OrderCount";
			strSql += ",SUM(sOrderTotalAmount) AS sOrderTotalAmount";
			strSql += ",SUM(sDiscount) AS sDiscount";
			strSql += " ,SUM(subNetAmount) AS subNetAmount";
			strSql += ",ROUND(SUM(subNetAmount)/SUM(OrderCount),2) AS TC";
			strSql += " FROM vrep_sales_daily_store_favourable";
			strSql += " WHERE ";
			strSql += "(businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
			strSql += "  AND companyCode = '" + companyCode + "'";
			// �ŵ�
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (brandCodeTemp != "") {
				strSql += " AND brandCode = '" + brandCodeTemp + "'";
			}

			if (favourableName != "") {
				strSql += " AND favourableName = '" + favourableName + "'";
			}

			strSql += " GROUP BY companyCode,storeCode) AS A,";
			strSql += "(SELECT companyCode,storeCode";
			strSql += ",SUM(subOrderCount) AS subOrderCount";
			strSql += ",SUM(subOrderTotalAmount) AS subOrderTotalAmount";
			strSql += ",SUM(subNetAmount) AS subNetAmount";
			strSql += ",SUM(subDiscountAmount) AS subDiscountAmount";
			strSql += " ,ROUND(SUM(subNetAmount)/SUM(subOrderCount),2) AS subTC";
			strSql += " FROM rep_sales_daily_store ";
			strSql += "  WHERE companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (brandCodeTemp != "") {
				strSql += " AND brandCode = '" + brandCodeTemp + "'";
			}

			strSql += " GROUP BY companyCode,storeCode) AS B,vrep_dict_store AS C";
			strSql += " WHERE A.companyCode = B.companyCode";
			strSql += " AND A.storeCode = B.storeCode";
			strSql += " AND A.storeCode = C.storeCode";

			if (brandCodeTemp != "") {
				strSql += " AND C.brandCode = '" + brandCodeTemp + "'";
			}

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageTemp);

			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int OrderCount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			OrderCount += Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݻ�ۿ۽��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�Żݻ����")).toString());
		}

		strSql = "";

		if (OrderCount != 0) {
			for (int k = 0; k < listData.size(); k++) {
				Map<String, String> mapValue = new HashMap<String, String>();
				mapValue = (Map<String, String>) listData.get(k);
				
				int orderNum = Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());

				float size = (float) orderNum / OrderCount;
				DecimalFormat df = new DecimalFormat("0.00");// ��ʽ��С��������Ĳ�0
				String filesize = df.format(size*100)+"%";// ���ص���String���͵�
				mapValue.put("�ܵ���ռ��", filesize);
			}
		}

		strSql += "SELECT '�ϼ�' AS 'Ʒ��'";
		strSql += ",'" + OrderCount + "' AS '�Żݻʹ�ô���'";
		strSql += ",'' AS '�Żݻʹ�ô���ռ��'";
		strSql += ",CONVERT(ROUND('" + sDiscount + "',2),CHAR(100)) AS '�Żݽ��'";
		strSql += ",'' AS '�Żݽ��ռ��'";
		strSql += ",CONVERT(ROUND('" + sNetAmountPrice + "',2),CHAR(100)) AS '�Żݻ����'";
		strSql += ",'' AS '�Żݻ����ռ��'";

		// strSql += ",'' AS '�Żݻ�͵���'";
		// strSql += ",'' AS 'ƽ���͵���'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�Ӫ�չ��׷�������(�������)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityRevenueBrandChildren(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String favourableName = map.get("favourableName");

		String strSql = "";
		String totalSQL = "";

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

			// ��ȡ���������������������
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
				String storeCodeTemp = mapTemoS.get("storeCode");
				if (storeCodeTemp != null && !storeCodeTemp.equals(null)) {
					isStore = "Y";
					break;
				}
			}

			// ��ϸ����
			strSql = "";
			totalSQL = "";
			strSql += "SELECT '" + BrandName + "' AS '����Ʒ��' ";
			strSql += ",IFNULL(CONCAT('" + BrandCode + "','|=|','" + BrandName + "','|=|','" + areaCode + "','|=|','"
					+ areaName + "'" + ",'|=|','" + isStore + "'),'') AS '����' ";

			strSql += ",IFNULL(SUM(A.OrderCount),0) AS '�Żݻ����'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.OrderCount)/SUM(B.subOrderCount),4) * 100,2),CHAR(100)),'%'),0) AS '�ܵ���ռ��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.sDiscount),2),CHAR(100)),0) AS '�Żݻ�ۿ۽��'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.sDiscount)/SUM(B.subDiscountAmount),4) * 100,2),CHAR(100)),'%'),0) AS '���ۿ�ռ��'";
			strSql += ",IFNULL(CONVERT(ROUND(SUM(A.subNetAmount),2),CHAR(100)),0) AS '�Żݻ����'";
			strSql += ",IFNULL(CONCAT(CONVERT(ROUND(ROUND(SUM(A.subNetAmount)/SUM(B.subNetAmount),4) * 100,2),CHAR(100)),'%'),0) AS '������ռ��'";
			// strSql += ",IFNULL(CONVERT(ROUND(SUM(A.TC),2),CHAR(100)),0) AS
			// '�Żݻ�͵���'";
			// strSql += ",IFNULL(CONVERT(ROUND(SUM(B.subTC),2),CHAR(100)),0) AS
			// 'ƽ���͵���'";
			strSql += " FROM ";
			strSql += "(SELECT companyCode,storeCode";
			strSql += ",IFNULL(SUM(OrderCount),0) AS OrderCount";
			strSql += ",IFNULL(SUM(sOrderTotalAmount),0) AS sOrderTotalAmount";
			strSql += ",IFNULL(SUM(sDiscount),0) AS sDiscount";
			strSql += ",IFNULL(SUM(subNetAmount),0) AS subNetAmount";
			strSql += ",IFNULL(ROUND(SUM(subNetAmount)/SUM(OrderCount),2),0) AS TC";
			strSql += " FROM vrep_sales_daily_store_favourable";
			strSql += " WHERE ";
			strSql += "(businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
			strSql += "  AND companyCode = '" + companyCode + "'";
			// �ŵ�
			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (BrandCode != "") {
				strSql += " AND brandCode = '" + BrandCode + "'";
			}

			if (favourableName != "") {
				strSql += " AND favourableName = '" + favourableName + "'";
			}

			strSql += " GROUP BY companyCode,storeCode) AS A,";
			strSql += "(SELECT companyCode,storeCode";
			strSql += ",IFNULL(SUM(subOrderCount),0) AS subOrderCount";
			strSql += ",IFNULL(SUM(subOrderTotalAmount),0) AS subOrderTotalAmount";
			strSql += ",IFNULL(SUM(subNetAmount),0) AS subNetAmount";
			strSql += ",IFNULL(SUM(subDiscountAmount),0) AS subDiscountAmount";
			strSql += " ,IFNULL(ROUND(SUM(subNetAmount)/SUM(subOrderCount),2),0) AS subTC";
			strSql += " FROM rep_sales_daily_store ";
			strSql += "  WHERE companyCode = '" + companyCode + "'";
			strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd
					+ "')";

			if (StoreCode != "") {
				StoreCode = StoreCode.replace("|", "','");
				strSql += " AND storeCode in('" + StoreCode + "')";
			} else {
				strSql += " AND storeCode in('" + StoreCode + "')";
			}

			if (BrandCode != "") {
				strSql += " AND brandCode = '" + BrandCode + "'";
			}

			strSql += " GROUP BY companyCode,storeCode) AS B,vrep_dict_store AS C";
			strSql += " WHERE A.companyCode = B.companyCode";
			strSql += " AND A.storeCode = B.storeCode";
			strSql += " AND A.storeCode = C.storeCode";

			if (BrandCode != "") {
				strSql += " AND C.brandCode = '" + BrandCode + "'";
			}

			strSql += "   AND C.areaCode IN (" + allChildrenCode + ") ";

			totalSQL = "select count(1) from (" + strSql + ") as Tab";

			logger.info(strSql);
			logger.info(totalSQL);

			PageModel listDataPageTemp = new PageModel();
			page = new Pagination(strSql, 1, 100000, jdbcTemplate, totalSQL, listDataPageTemp);

			List<Object> listDataTemp = page.getResultList();
			listData.addAll(listDataTemp);
			listDataPage.setTotalRows(listDataPage.getTotalRows() + listDataPageTemp.getTotalRows());

		}

		listDataPage.setTotalPages(1);
		pageModelList.add(listDataPage);

		int OrderCount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			OrderCount += Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݻ�ۿ۽��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�Żݻ����")).toString());
		}

		
		if (OrderCount != 0) {
			for (int k = 0; k < listData.size(); k++) {
				Map<String, String> mapValue = new HashMap<String, String>();
				mapValue = (Map<String, String>) listData.get(k);
				
				int orderNum = Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());

				float size = (float) orderNum / OrderCount;
				DecimalFormat df = new DecimalFormat("0.00");// ��ʽ��С��������Ĳ�0
				String filesize = df.format(size*100)+"%";// ���ص���String���͵�
				mapValue.put("�ܵ���ռ��", filesize);
			}
		}
		
		
		strSql = "";

		strSql += "SELECT '�ϼ�' AS 'Ʒ��'";
		strSql += ",'' AS '����' ";

		strSql += ",'" + OrderCount + "' AS '�Żݻʹ�ô���'";
		strSql += ",'' AS '�Żݻʹ�ô���ռ��'";
		strSql += ",CONVERT(ROUND('" + sDiscount + "',2),CHAR(100)) AS '�Żݽ��'";
		strSql += ",'' AS '�Żݽ��ռ��'";
		strSql += ",CONVERT(ROUND('" + sNetAmountPrice + "',2),CHAR(100)) AS '�Żݻ����'";
		strSql += ",'' AS '�Żݻ����ռ��'";

		// strSql += ",'' AS '�Żݻ�͵���'";
		// strSql += ",'' AS 'ƽ���͵���'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�Ӫ�չ��׷�������(�ŵ����)==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityRevenue(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String ChannelCode = map.get("ChannelCode");
		String favourableName = map.get("favourableName");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String AreaName = map.get("AreaName");

		String strSql = "";
		String totalSQL = "";

		strSql += " SELECT  ";
		strSql += "'" + BrandName + "' AS '����Ʒ��'";
		strSql += ",'" + AreaName + "' AS '��������'";
		strSql += ",C.storeCodeName AS '�ŵ�'";
		strSql += ",C.storeTypeName AS '�ŵ�����'";
		strSql += ",A.favourableName AS '�����'";
		strSql += ",A.OrderCount AS '�Żݻ����'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.OrderCount/B.subOrderCount,4) * 100,2),CHAR(100)),'%') AS '�ܵ���ռ��'";
		strSql += ",CONVERT(ROUND(A.sDiscount,2),CHAR(100)) AS '�Żݻ�ۿ۽��'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.sDiscount/B.subDiscountAmount,4) * 100,2),CHAR(100)),'%') AS '���ۿ�ռ��'";
		strSql += ",CONVERT(ROUND(A.subNetAmount,2),CHAR(100)) AS '�Żݻ����'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.subNetAmount/B.subNetAmount,4) * 100,2),CHAR(100)),'%') AS '������ռ��'";
		// strSql += ",CONVERT(ROUND(A.TC,2),CHAR(100)) AS '�Żݻ�͵���'";
		// strSql += ",CONVERT(ROUND(B.subTC,2),CHAR(100)) AS 'ƽ���͵���'";
		strSql += " FROM ";
		strSql += "(SELECT companyCode,storeCode,favourableName";
		strSql += ",SUM(OrderCount) AS OrderCount";
		strSql += ",SUM(sOrderTotalAmount) AS sOrderTotalAmount";
		strSql += ",SUM(sDiscount) AS sDiscount";
		strSql += " ,SUM(subNetAmount) AS subNetAmount";
		strSql += ",ROUND(SUM(subNetAmount)/SUM(OrderCount),2) AS TC";
		strSql += " FROM vrep_sales_daily_store_favourable";
		strSql += " WHERE ";
		strSql += "(businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		strSql += "  AND companyCode = '" + companyCode + "'";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		if (favourableName != "") {
			strSql += " AND favourableName = '" + favourableName + "'";
		}

		strSql += " GROUP BY companyCode,storeCode,favourableName) AS A,";
		strSql += "(SELECT companyCode,storeCode";
		strSql += ",SUM(subOrderCount) AS subOrderCount";
		strSql += ",SUM(subOrderTotalAmount) AS subOrderTotalAmount";
		strSql += ",SUM(subNetAmount) AS subNetAmount";
		strSql += ",SUM(subDiscountAmount) AS subDiscountAmount";
		strSql += " ,ROUND(SUM(subNetAmount)/SUM(subOrderCount),2) AS subTC";
		strSql += " FROM rep_sales_daily_store ";
		strSql += "  WHERE companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		strSql += " GROUP BY companyCode,storeCode) AS B,vrep_dict_store AS C";
		strSql += " WHERE A.companyCode = B.companyCode";
		strSql += " AND A.storeCode = B.storeCode";
		strSql += " AND A.storeCode = C.storeCode";

		strSql += "   AND C.areaCode ='" + AreaCode + "'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, 10000, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);

		
		int OrderCount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			OrderCount += Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݻ�ۿ۽��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�Żݻ����")).toString());
		}

		
		if (OrderCount != 0) {
			for (int k = 0; k < listData.size(); k++) {
				Map<String, String> mapValue = new HashMap<String, String>();
				mapValue = (Map<String, String>) listData.get(k);
				
				int orderNum = Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());

				float size = (float) orderNum / OrderCount;
				DecimalFormat df = new DecimalFormat("0.00");// ��ʽ��С��������Ĳ�0
				String filesize = df.format(size*100)+"%";// ���ص���String���͵�
				mapValue.put("�ܵ���ռ��", filesize);
			}
		}
		
		
		
		
		
		
		
		
		
		strSql = "";

		strSql += "SELECT '�ϼ�' AS '����Ʒ��'";
		strSql += ", '' AS '��������'";
		strSql += ", '' AS '�ŵ�'";
		strSql += ", '' AS '�ŵ�����'";
		strSql += ",'' AS '�����'";

		strSql += ",'" + OrderCount + "' AS '�Żݻʹ�ô���'";
		strSql += ",'' AS '�Żݻʹ�ô���ռ��'";
		strSql += ",CONVERT(ROUND('" + sDiscount + "',2),CHAR(100)) AS '�Żݽ��'";
		strSql += ",'' AS '�Żݽ��ռ��'";
		strSql += ",CONVERT(ROUND('" + sNetAmountPrice + "',2),CHAR(100)) AS '�Żݻ����'";
		strSql += ",'' AS '�Żݻ����ռ��'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

	/*
	 * ======�Ӫ�չ��׷���������Excel=================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryRepActivityRevenueExportExcel(Map<String, String> map) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreCode = map.get("StoreCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String BrandCode = map.get("BrandCode");
		String ChannelCode = map.get("ChannelCode");
		String favourableName = map.get("favourableName");
		String BrandName = map.get("BrandName");
		String AreaCode = map.get("AreaCode");
		String AreaName = map.get("AreaName");

		String strSql = "";
		String totalSQL = "";

		strSql += " SELECT  ";
		strSql += "C.storeCodeName AS '�ŵ�'";
		strSql += ",C.storeTypeName AS '�ŵ�����'";
		strSql += ",A.favourableName AS '�����'";
		strSql += ",A.OrderCount AS '�Żݻ����'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.OrderCount/B.subOrderCount,4) * 100,2),CHAR(100)),'%') AS '�ܵ���ռ��'";
		strSql += ",A.sDiscount AS '�Żݻ�ۿ۽��'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.sDiscount/B.subDiscountAmount,4) * 100,2),CHAR(100)),'%') AS '���ۿ�ռ��'";
		strSql += ",A.subNetAmount AS '�Żݻ����'";
		strSql += ",CONCAT(CONVERT(ROUND(ROUND(A.subNetAmount/B.subNetAmount,4) * 100,2),CHAR(100)),'%') AS '������ռ��'";
		// strSql += ",A.TC AS '�Żݻ�͵���'";
		// strSql += ",B.subTC AS 'ƽ���͵���'";
		strSql += " FROM ";
		strSql += "(SELECT companyCode,storeCode,favourableName";
		strSql += ",SUM(OrderCount) AS OrderCount";
		strSql += ",SUM(sOrderTotalAmount) AS sOrderTotalAmount";
		strSql += ",SUM(sDiscount) AS sDiscount";
		strSql += " ,SUM(subNetAmount) AS subNetAmount";
		strSql += ",ROUND(SUM(subNetAmount)/SUM(OrderCount),2) AS TC";
		strSql += " FROM vrep_sales_daily_store_favourable";
		strSql += " WHERE ";
		strSql += "(businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";
		strSql += "  AND companyCode = '" + companyCode + "'";
		// �ŵ�
		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		if (favourableName != "") {
			strSql += " AND favourableName = '" + favourableName + "'";
		}

		strSql += " GROUP BY companyCode,storeCode,favourableName) AS A,";
		strSql += "(SELECT companyCode,storeCode";
		strSql += ",SUM(subOrderCount) AS subOrderCount";
		strSql += ",SUM(subOrderTotalAmount) AS subOrderTotalAmount";
		strSql += ",SUM(subNetAmount) AS subNetAmount";
		strSql += ",SUM(subDiscountAmount) AS subDiscountAmount";
		strSql += " ,ROUND(SUM(subNetAmount)/SUM(subOrderCount),2) AS subTC";
		strSql += " FROM rep_sales_daily_store ";
		strSql += "  WHERE companyCode = '" + companyCode + "'";
		strSql += " AND (businessDate >= '" + businessDateStart + "' AND businessDate <= '" + businessDateEnd + "')";

		if (StoreCode != "") {
			StoreCode = StoreCode.replace("|", "','");
			strSql += " AND storeCode in('" + StoreCode + "')";
		} else {
			strSql += " AND storeCode in('" + StoreCode + "')";
		}
		if (BrandCode != "") {
			strSql += " AND brandCode = '" + BrandCode + "'";
		}
		strSql += " GROUP BY companyCode,storeCode) AS B,vrep_dict_store AS C";
		strSql += " WHERE A.companyCode = B.companyCode";
		strSql += " AND A.storeCode = B.storeCode";
		strSql += " AND A.storeCode = C.storeCode";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		List<Object> pageModelList = new ArrayList<Object>();
		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, PageIndex, PageSize, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listData = page.getResultList();
		pageModelList.add(listDataPage);
		int OrderCount = 0;
		double sDiscount = 0;
		double sNetAmountPrice = 0;

		for (int k = 0; k < listData.size(); k++) {

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(k);
			OrderCount += Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());
			sDiscount += Double.parseDouble(((Object) mapValue.get("�Żݻ�ۿ۽��")).toString());
			sNetAmountPrice += Double.parseDouble(((Object) mapValue.get("�Żݻ����")).toString());
		}

		
		if (OrderCount != 0) {
			for (int k = 0; k < listData.size(); k++) {
				Map<String, String> mapValue = new HashMap<String, String>();
				mapValue = (Map<String, String>) listData.get(k);
				
				int orderNum = Integer.parseInt(((Object) mapValue.get("�Żݻ����")).toString());

				float size = (float) orderNum / OrderCount;
				DecimalFormat df = new DecimalFormat("0.00");// ��ʽ��С��������Ĳ�0
				String filesize = df.format(size*100)+"%";// ���ص���String���͵�
				mapValue.put("�ܵ���ռ��", filesize);
			}
		}
		
		strSql = "";

		strSql += "SELECT '�ϼ�' AS '�ŵ�'";
		strSql += ", '' AS '�ŵ�����'";
		strSql += ",'' AS '�����'";

		strSql += ",'" + OrderCount + "' AS '�Żݻʹ�ô���'";
		strSql += ",'' AS '�Żݻʹ�ô���ռ��'";
		strSql += ",CONVERT(ROUND('" + sDiscount + "',2),CHAR(100)) AS '�Żݽ��'";
		strSql += ",'' AS '�Żݽ��ռ��'";
		strSql += ",CONVERT(ROUND('" + sNetAmountPrice + "',2),CHAR(100)) AS '�Żݻ����'";
		strSql += ",'' AS '�Żݻ����ռ��'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		logger.info(strSql);
		logger.info(totalSQL);

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 10, jdbcTemplate, totalSQL, listDataPage);

		List<Object> listDataSum = page.getResultList();

		queryDataMap.put(new String("listData"), listData);
		queryDataMap.put(new String("pageData"), pageModelList);
		queryDataMap.put(new String("listDataSum"), listDataSum);

		return queryDataMap;
	}

}
