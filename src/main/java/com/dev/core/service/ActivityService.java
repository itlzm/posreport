package com.dev.core.service;

import java.util.List;
import java.util.Map;

public interface ActivityService {

	/*
	 * ======�����������������(Ʒ�ƻ���)==================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityBrand(Map<String, String> map);

	/*
	 * ======�����������������(�������)==================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityBrandChildren(Map<String, String> map);

	/*
	 * ======�����������������(�ŵ����)===================================================
	 * =====
	 */
	public Map<String, List<Object>> queryRepChannelActivity(Map<String, String> map);

	/*
	 * ======�������������������Excel=================================================
	 */
	public Map<String, List<Object>> queryRepChannelActivityExportExcel(Map<String, String> map);

	/*
	 * ======�����Աȷ�������(Ʒ�ƻ���)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseBrand(Map<String, String> map);

	/*
	 * ======�����Աȷ�������(�������)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseBrandChildren(Map<String, String> map);

	/*
	 * ======�����Աȷ�������(�ŵ����)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverse(Map<String, String> map);

	/*
	 * ======�����Աȷ���������Excel=================================================
	 */
	public Map<String, List<Object>> queryRepActivityTransverseExportExcel(Map<String, String> map);

	
	
	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(Ʒ�ƻ���)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleBrand(Map<String, String> map);

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�������)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleBrandChildren(Map<String, String> map);

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�ŵ����)=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingle(Map<String, String> map);

	/*
	 * ======�ײͼ���Ʒ�ۿ۷���������Excel=================================================
	 */
	public Map<String, List<Object>> queryRepMealSingleExportExcel(Map<String, String> map);

	
	/*
	 * ======�Ӫ�չ��׷�������(Ʒ�ƻ���)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueBrand(Map<String, String> map);
	
	/*
	 * ======�Ӫ�չ��׷�������(�������)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueBrandChildren(Map<String, String> map);
	
	
	/*
	 * ======�Ӫ�չ��׷�������(�ŵ����)==================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenue(Map<String, String> map);
	
	/*
	 * ======�Ӫ�չ��׷���������Excel=================================================
	 */
	public Map<String, List<Object>> queryRepActivityRevenueExportExcel(Map<String, String> map);

	
}
