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
	 * ======Ӫҵ�������Ʒ�������(Ʒ�ƻ���)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendBrand(Map<String, String> map) {
		return businessDao.queryRepTrendBrand(map);
	}

	/*
	 * ======Ӫҵ�������Ʒ�������(�������)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendBrandChildren(Map<String, String> map) {
		return businessDao.queryRepTrendBrandChildren(map);
	}

	/*
	 * ======Ӫҵ�������Ʒ�������(�ŵ����)==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrend(Map<String, String> map) {
		return businessDao.queryRepTrend(map);
	}

	/*
	 * ======Ӫҵ�������Ʒ���������Excel==================================================
	 */
	
	public Map<String, List<Object>> queryRepTrendExportExcel(Map<String, String> map) {
		return businessDao.queryRepTrendExportExcel(map);
	}

	/*
	 * ======Ӫҵ���뻷�ȷ�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHB(Map<String, String> map) {
		return businessDao.queryRepBusinessHB(map);
	}

	/*
	 * ======Ӫҵ���뻷�ȷ������Ʒ�Ʊ���====================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildren(map);
	}

	/*
	 * ======Ӫҵ���뻷�ȷ�����������ŵ걨��==================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildrenStore(map);
	}

	/*
	 * ======Ӫҵ���뻷�ȷ�����������ŵ걨����Excel============================================
	 * ======
	 */
	
	public Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(Map<String, String> map) {
		return businessDao.queryRepBusinessHBBrandChildrenStoreExportExcel(map);
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTB(Map<String, String> map) {
		return businessDao.queryRepBusinessTB(map);
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ������Ʒ�Ʊ���=====================================================
	 * ===
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessTBBrandChildren(map);
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ�����������ŵ걨��==================================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessTBBrandChildrenStore(map);
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ�������===============================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDate(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDate(map);
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ��Ʒ�Ʊ���===========================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildren(map);
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ�������ŵ걨��=========================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildrenStore(map);
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�ȷ������ڵ�������ŵ걨����Excel==================================
	 */
	
	public Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(Map<String, String> map) {
		return businessDao.queryRepBusinessTBDateBrandChildrenStoreExportExcel(map);
	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(Ʒ�ƻ���)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrand(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrand(map);
	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(�������)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrandChildren(map);
	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(��Ʒ����)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(Map<String, String> map) {
		return businessDao.queryRepProductCatTBBrandChildrenProduct(map);
	}

	/*
	 * ======��Ʒ����ͬ�ȷ�������Excel====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(Map<String, String> map){
		return businessDao.queryRepProductCatTBBrandChildrenProductExportExcel(map);
	}

	
	/*
	 * ======��Ʒ���໷�ȷ���(Ʒ�ƻ���)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrand(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrand(map);
	}

	/*
	 * ======��Ʒ���໷�ȷ���(�������)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildren(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrandChildren(map);
	}

	/*
	 * ======��Ʒ���໷�ȷ���(��Ʒ����)====================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(Map<String, String> map) {
		return businessDao.queryRepProductCatHBBrandChildrenProduct(map);
	}

	/*
	 * ======��Ʒ���໷�ȷ�������Excel===================================================
	 */
	
	public Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(Map<String, String> map){
		return businessDao.queryRepProductCatHBBrandChildrenProductExportExcel(map);
	}

}
