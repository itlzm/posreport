package com.dev.core.service;

import java.util.List;
import java.util.Map;

public interface BusinessService {

	/*
	 * ======Ӫҵ�������Ʒ�������(Ʒ�ƻ���)==================================================
	 */
	public Map<String, List<Object>> queryRepTrendBrand(Map<String, String> map);

	/*
	 * ======Ӫҵ�������Ʒ�������(�������)==================================================
	 */
	public Map<String, List<Object>> queryRepTrendBrandChildren(Map<String, String> map);

	/*
	 * ======Ӫҵ�������Ʒ�������(�ŵ����)==================================================
	 */
	public Map<String, List<Object>> queryRepTrend(Map<String, String> map);

	/*
	 * ======Ӫҵ�������Ʒ���������Excel==================================================
	 */
	public Map<String, List<Object>> queryRepTrendExportExcel(Map<String, String> map);

	/*
	 * ======Ӫҵ���뻷�ȷ�������========================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHB(Map<String, String> map);

	/*
	 * ======Ӫҵ���뻷�ȷ������Ʒ�Ʊ���====================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildren(Map<String, String> map);

	/*
	 * ======Ӫҵ���뻷�ȷ�����������ŵ걨��==================================================
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(Map<String, String> map);

	/*
	 * ======Ӫҵ���뻷�ȷ�����������ŵ걨����Excel============================================
	 * ======
	 */
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(Map<String, String> map);

	/*
	 * ======Ӫҵ����ͬ�ȷ�������========================================================
	 */
	public Map<String, List<Object>> queryRepBusinessTB(Map<String, String> map);

	/*
	 * ======Ӫҵ����ͬ�ȷ������Ʒ�Ʊ���=====================================================
	 * ===
	 */
	public Map<String, List<Object>> queryRepBusinessTBBrandChildren(Map<String, String> map);

	/*
	 * ======Ӫҵ����ͬ�ȷ�����������ŵ걨��==================================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(Map<String, String> map);

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ�������===============================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDate(Map<String, String> map);

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ��Ʒ�Ʊ���===========================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(Map<String, String> map);

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ�������ŵ걨��=========================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(Map<String, String> map);

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ�������ŵ걨����Excel==================================
	 */
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(Map<String, String> map);

	/*
	 * ======��Ʒ����ͬ�ȷ���(Ʒ�ƻ���)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrand(Map<String, String> map);

	/*
	 * ======��Ʒ����ͬ�ȷ���(�������)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildren(Map<String, String> map);

	/*
	 * ======��Ʒ����ͬ�ȷ���(��Ʒ����)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(Map<String, String> map);

	
	/*
	 * ======��Ʒ����ͬ�ȷ�������Excel====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(Map<String, String> map);

	
	
	/*
	 * ======��Ʒ���໷�ȷ���(Ʒ�ƻ���)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrand(Map<String, String> map);

	/*
	 * ======��Ʒ���໷�ȷ���(�������)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildren(Map<String, String> map);

	/*
	 * ======��Ʒ���໷�ȷ���(��Ʒ����)====================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(Map<String, String> map);

	/*
	 * ======��Ʒ���໷�ȷ�������Excel===================================================
	 */
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(Map<String, String> map);

}
