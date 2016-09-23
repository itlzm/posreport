package com.dev.core.service;

import java.util.List;
import java.util.Map;

public interface BusinessService {

	/*
	 * ======营业收入趋势分析报表(品牌汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepTrendBrand(Map<String, String> map);

	/*
	 * ======营业收入趋势分析报表(区域汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepTrendBrandChildren(Map<String, String> map);

	/*
	 * ======营业收入趋势分析报表(门店汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepTrend(Map<String, String> map);

	/*
	 * ======营业收入趋势分析报表导出Excel==================================================
	 */
	public Map<String, List<Object>> queryRepTrendExportExcel(Map<String, String> map);

	/*
	 * ======营业收入环比分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHB(Map<String, String> map);

	/*
	 * ======营业收入环比分析点击品牌报表====================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildren(Map<String, String> map);

	/*
	 * ======营业收入环比分析点击区域门店报表==================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(Map<String, String> map);

	/*
	 * ======营业收入环比分析点击区域门店报表导出Excel============================================
	 * ======
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(Map<String, String> map);

	/*
	 * ======营业收入同比分析报表========================================================
	 */
	public Map<String, List<Object>> queryRepBusinessTB(Map<String, String> map);

	/*
	 * ======营业收入同比分析点击品牌报表=====================================================
	 * ===
	 */
	public Map<String, List<Object>> queryRepBusinessTBBrandChildren(Map<String, String> map);

	/*
	 * ======营业收入同比分析点击区域门店报表==================================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(Map<String, String> map);

	/*
	 * ======(自定义时段)营业收入同比日期分析报表===============================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDate(Map<String, String> map);

	/*
	 * ======(自定义时段)营业收入同比分析日期点击品牌报表===========================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(Map<String, String> map);

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表=========================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(Map<String, String> map);

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表导出Excel==================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(Map<String, String> map);

	/*
	 * ======产品分类同比分析(品牌汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrand(Map<String, String> map);

	/*
	 * ======产品分类同比分析(分类汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildren(Map<String, String> map);

	/*
	 * ======产品分类同比分析(产品汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(Map<String, String> map);

	
	/*
	 * ======产品分类同比分析导出Excel====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(Map<String, String> map);

	
	
	/*
	 * ======产品分类环比分析(品牌汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrand(Map<String, String> map);

	/*
	 * ======产品分类环比分析(分类汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildren(Map<String, String> map);

	/*
	 * ======产品分类环比分析(产品汇总)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(Map<String, String> map);

	/*
	 * ======产品分类环比分析导出Excel===================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(Map<String, String> map);

}
