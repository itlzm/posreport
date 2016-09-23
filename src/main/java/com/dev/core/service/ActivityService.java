package com.dev.core.service;

import java.util.List;
import java.util.Map;

public interface ActivityService {

	/*
	 * ======渠道活动引流分析报表(品牌汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityBrand(Map<String, String> map);

	/*
	 * ======渠道活动引流分析报表(区域汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityBrandChildren(Map<String, String> map);

	/*
	 * ======渠道活动引流分析报表(门店汇总)===================================================
	 * =====
	 */
	public Map<String, List<Object>> queryRepChannelActivity(Map<String, String> map);

	/*
	 * ======渠道活动引流分析报表导出Excel=================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityExportExcel(Map<String, String> map);

	/*
	 * ======活动横向对比分析报表(品牌汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseBrand(Map<String, String> map);

	/*
	 * ======活动横向对比分析报表(区域汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseBrandChildren(Map<String, String> map);

	/*
	 * ======活动横向对比分析报表(门店汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverse(Map<String, String> map);

	/*
	 * ======活动横向对比分析报表导出Excel=================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseExportExcel(Map<String, String> map);

	
	
	/*
	 * ======套餐及单品折扣分析报表(品牌汇总)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleBrand(Map<String, String> map);

	/*
	 * ======套餐及单品折扣分析报表(区域汇总)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleBrandChildren(Map<String, String> map);

	/*
	 * ======套餐及单品折扣分析报表(门店汇总)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingle(Map<String, String> map);

	/*
	 * ======套餐及单品折扣分析报表导出Excel=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleExportExcel(Map<String, String> map);

	
	/*
	 * ======活动营收贡献分析报表(品牌汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueBrand(Map<String, String> map);
	
	/*
	 * ======活动营收贡献分析报表(区域汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueBrandChildren(Map<String, String> map);
	
	
	/*
	 * ======活动营收贡献分析报表(门店汇总)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenue(Map<String, String> map);
	
	/*
	 * ======活动营收贡献分析报表导出Excel=================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueExportExcel(Map<String, String> map);

	
}
