package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.ActivityDao;
import com.dev.core.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	ActivityDao activityDao;

	/*
	 * ======渠道活动引流分析报表(品牌汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityBrand(Map<String, String> map) {
		return activityDao.queryRepChannelActivityBrand(map);
	}

	/*
	 * ======渠道活动引流分析报表(区域汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityBrandChildren(Map<String, String> map) {
		return activityDao.queryRepChannelActivityBrandChildren(map);
	}

	/*
	 * ======渠道活动引流分析报表(门店汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivity(Map<String, String> map) {

		return activityDao.queryRepChannelActivity(map);
	}

	/*
	 * ======渠道活动引流分析报表导出Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityExportExcel(Map<String, String> map) {
		return activityDao.queryRepChannelActivityExportExcel(map);
	}

	/*
	 * ======活动横向对比分析报表(品牌汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseBrand(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseBrand(map);
	}

	/*
	 * ======活动横向对比分析报表(区域汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseBrandChildren(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseBrandChildren(map);
	}

	/*
	 * ======活动横向对比分析报表(门店汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverse(Map<String, String> map) {
		return activityDao.queryRepActivityTransverse(map);
	}

	/*
	 * ======活动横向对比分析报表导出Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseExportExcel(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseExportExcel(map);
	}

	/*
	 * ======套餐及单品折扣分析报表(品牌汇总)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleBrand(Map<String, String> map) {
		return activityDao.queryRepMealSingleBrand(map);
	}

	/*
	 * ======套餐及单品折扣分析报表(区域汇总)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleBrandChildren(Map<String, String> map) {
		return activityDao.queryRepMealSingleBrandChildren(map);
	}

	/*
	 * ======套餐及单品折扣分析报表(门店汇总)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingle(Map<String, String> map) {
		return activityDao.queryRepMealSingle(map);
	}

	/*
	 * ======套餐及单品折扣分析报表导出Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleExportExcel(Map<String, String> map) {
		return activityDao.queryRepMealSingleExportExcel(map);
	}

	
	/*
	 * ======活动营收贡献分析报表(品牌汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueBrand(Map<String, String> map){
		return activityDao.queryRepActivityRevenueBrand(map);
	}
	
	/*
	 * ======活动营收贡献分析报表(区域汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueBrandChildren(Map<String, String> map){
		return activityDao.queryRepActivityRevenueBrandChildren(map);
	}
	
	
	/*
	 * ======活动营收贡献分析报表(门店汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenue(Map<String, String> map) {
		return activityDao.queryRepActivityRevenue(map);
	}
	
	/*
	 * ======活动营收贡献分析报表导出Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueExportExcel(Map<String, String> map){
		return activityDao.queryRepActivityRevenueExportExcel(map);
	}
	
}
