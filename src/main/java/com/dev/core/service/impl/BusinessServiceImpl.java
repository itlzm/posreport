package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.BusinessDao;
import com.dev.core.service.BusinessService;

@Service
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	BusinessDao businessDao;

	/*
	 * ======营业收入趋势分析报表(品牌汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendBrand(Map<String, String> map) {
		return businessDao.queryRepTrendBrand(map);
	}

	/*
	 * ======营业收入趋势分析报表(区域汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendBrandChildren(Map<String, String> map) {
		return businessDao.queryRepTrendBrandChildren(map);
	}

	/*
	 * ======营业收入趋势分析报表(门店汇总)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrend(Map<String, String> map) {
		return businessDao.queryRepTrend(map);
	}

	/*
	 * ======营业收入趋势分析报表导出Excel==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendExportExcel(Map<String, String> map) {
		return businessDao.queryRepTrendExportExcel(map);
	}

	/*
	 * ======营业收入环比分析报表========================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHB(Map<String, String> map) {
		return businessDao.queryRepBusinessHB(map);
	}

	/*
	 * ======营业收入环比分析点击品牌报表====================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildren(map);
	}

	/*
	 * ======营业收入环比分析点击区域门店报表==================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildrenStore(map);
	}

	/*
	 * ======营业收入环比分析点击区域门店报表导出Excel============================================
	 * ======
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildrenStoreExportExcel(map);
	}

	/*
	 * ======营业收入同比分析报表========================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTB(Map<String, String> map) {
		return businessDao.queryRepBusinessTB(map);
	}

	/*
	 * ======营业收入同比分析点击品牌报表=====================================================
	 * ===
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessTBBrandChildren(map);
	}

	/*
	 * ======营业收入同比分析点击区域门店报表==================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessTBBrandChildrenStore(map);
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析报表===============================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDate(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDate(map);
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击品牌报表===========================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildren(map);
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表=========================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildrenStore(map);
	}

	/*
	 * ======(自定义时段)营业收入同比分析日期点击区域门店报表导出Excel==================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildrenStoreExportExcel(map);
	}

	/*
	 * ======产品分类同比分析(品牌汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrand(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrand(map);
	}

	/*
	 * ======产品分类同比分析(分类汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrandChildren(map);
	}

	/*
	 * ======产品分类同比分析(产品汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrandChildrenProduct(map);
	}

	/*
	 * ======产品分类同比分析导出Excel====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(Map<String, String> map){
		return businessDao.queryRepProductCatTBBrandChildrenProductExportExcel(map);
	}

	
	/*
	 * ======产品分类环比分析(品牌汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrand(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrand(map);
	}

	/*
	 * ======产品分类环比分析(分类汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrandChildren(map);
	}

	/*
	 * ======产品分类环比分析(产品汇总)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrandChildrenProduct(map);
	}

	/*
	 * ======产品分类环比分析导出Excel===================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(Map<String, String> map){
		return businessDao.queryRepProductCatHBBrandChildrenProductExportExcel(map);
	}

}
