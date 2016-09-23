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
	 * ======�����������������(Ʒ�ƻ���)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityBrand(Map<String, String> map) {
		return activityDao.queryRepChannelActivityBrand(map);
	}

	/*
	 * ======�����������������(�������)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityBrandChildren(Map<String, String> map) {
		return activityDao.queryRepChannelActivityBrandChildren(map);
	}

	/*
	 * ======�����������������(�ŵ����)==================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivity(Map<String, String> map) {

		return activityDao.queryRepChannelActivity(map);
	}

	/*
	 * ======�������������������Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelActivityExportExcel(Map<String, String> map) {
		return activityDao.queryRepChannelActivityExportExcel(map);
	}

	/*
	 * ======�����Աȷ�������(Ʒ�ƻ���)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseBrand(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseBrand(map);
	}

	/*
	 * ======�����Աȷ�������(�������)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseBrandChildren(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseBrandChildren(map);
	}

	/*
	 * ======�����Աȷ�������(�ŵ����)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverse(Map<String, String> map) {
		return activityDao.queryRepActivityTransverse(map);
	}

	/*
	 * ======�����Աȷ���������Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityTransverseExportExcel(Map<String, String> map) {
		return activityDao.queryRepActivityTransverseExportExcel(map);
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(Ʒ�ƻ���)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleBrand(Map<String, String> map) {
		return activityDao.queryRepMealSingleBrand(map);
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�������)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleBrandChildren(Map<String, String> map) {
		return activityDao.queryRepMealSingleBrandChildren(map);
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�ŵ����)=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingle(Map<String, String> map) {
		return activityDao.queryRepMealSingle(map);
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷���������Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepMealSingleExportExcel(Map<String, String> map) {
		return activityDao.queryRepMealSingleExportExcel(map);
	}

	
	/*
	 * ======�Ӫ�չ��׷�������(Ʒ�ƻ���)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueBrand(Map<String, String> map){
		return activityDao.queryRepActivityRevenueBrand(map);
	}
	
	/*
	 * ======�Ӫ�չ��׷�������(�������)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueBrandChildren(Map<String, String> map){
		return activityDao.queryRepActivityRevenueBrandChildren(map);
	}
	
	
	/*
	 * ======�Ӫ�չ��׷�������(�ŵ����)==================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenue(Map<String, String> map) {
		return activityDao.queryRepActivityRevenue(map);
	}
	
	/*
	 * ======�Ӫ�չ��׷���������Excel=================================================
	 */
	
	public Map<String, List<Object>> queryRepActivityRevenueExportExcel(Map<String, String> map){
		return activityDao.queryRepActivityRevenueExportExcel(map);
	}
	
}
