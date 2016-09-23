package com.dev.core.dao;

import java.util.List;
import java.util.Map;


import com.dev.core.util.PageModel;
@SuppressWarnings("rawtypes")
public interface StoreDao {

	/*
	 * 返回下拉数据集合
	 * 
	 * @moduleName 模块表名
	 */
	
	public List getSelectDropDownList(Map<String, String> map);

	/*
	 * 根据查询语句返回查询信息
	 */
	public List getQueryData(Map<String, String> map, PageModel pageModel);

	/*
	 * 支付方式结构分析
	 */
	public List getStorePayMModel(Map<String, String> map);

	/*
	 * 支付方式
	 */
	public List getStorePayType(Map<String, String> map);

	/*
	 * ======门店支付方式分析报表========================================================
	 */
	public Map<String, List<Object>> queryReppaymode(Map<String, String> map, List<Object> storePayMModelList,
			List<Object> storePayTypeList);

	/*
	 * ======门店支付方式明细报表========================================================
	 */
	public Map<String, List<Object>> queryReppayDetail(Map<String, String> map);

	/*
	 * ======门店渠道营收分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepChannelSales(Map<String, String> map);

	/*
	 * ======门店分时销售分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepTimeSales(Map<String, String> map);

	/*
	 * ======门店活动销售分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepActiveSales(Map<String, String> map);

	/*
	 * ======门店产品销售分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepProductSales(Map<String, String> map);

	/*
	 * ======门店半个小时销售分析报表======================================================
	 */
	public Map<String, List<Object>> queryRepHalfTimeSales(Map<String, String> map);

	/*
	 * ======门店交易流水明细报表========================================================
	 */
	public Map<String, List<Object>> queryRepSalesDetail(Map<String, String> map);

	
	/*
	 * ======门店交易流水单据详细明细======================================================
	 */
	public Map<String, List<Object>> queryRepSalesDetailOrderDetail(Map<String, String> map);


	/*
	 * ======门店杯量统计报表(杯量汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepSalesCup(Map<String, String> map);

	/*
	 * ======门店杯量统计报表(杯量销售明细)==================================================
	 */
	public Map<String, List<Object>> queryRepSalesCupDetail(Map<String, String> map);
	
	
	/*
	 * ======门店杯量统计报表(杯量销售明细)导出Excel===========================================
	 */
	public Map<String, List<Object>> queryRepSalesCupDetailExportExcel(Map<String, String> map);
	
}
