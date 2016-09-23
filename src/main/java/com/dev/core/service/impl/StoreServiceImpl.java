package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.StoreDao;

import com.dev.core.service.StoreService;
import com.dev.core.util.PageModel;

@Service
@SuppressWarnings("rawtypes")
public class StoreServiceImpl implements StoreService {

	@Autowired
	StoreDao storeDao;

	
	public List getSelectDropDownList(Map<String, String> map) {
		return storeDao.getSelectDropDownList(map);
	}

	/*
	 * ���ݲ�ѯ��䷵�ز�ѯ��Ϣ
	 */
	
	public List getQueryData(Map<String, String> map, PageModel pageModel) {
		return storeDao.getQueryData(map, pageModel);
	}

	/*
	 * ֧����ʽ�ṹ����
	 */
	
	public List getStorePayMModel(Map<String, String> map) {
		return storeDao.getStorePayMModel(map);
	}

	/*
	 * ֧����ʽ
	 */
	
	public List getStorePayType(Map<String, String> map) {
		return storeDao.getStorePayType(map);
	}

	/*
	 * ======�ŵ�֧����ʽ��������========================================================
	 */
	
	public Map<String, List<Object>> queryReppaymode(Map<String, String> map, List<Object> storePayMModelList,
			List<Object> storePayTypeList) {
		return storeDao.queryReppaymode(map, storePayMModelList, storePayTypeList);
	}

	/*
	 * ======�ŵ�֧����ʽ��ϸ����========================================================
	 */
	
	public Map<String, List<Object>> queryReppayDetail(Map<String, String> map) {
		return storeDao.queryReppayDetail(map);
	}

	/*
	 * ======�ŵ�����Ӫ�շ�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepChannelSales(Map<String, String> map) {
		return storeDao.queryRepChannelSales(map);
	}

	/*
	 * ======�ŵ��ʱ���۷�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepTimeSales(Map<String, String> map) {
		return storeDao.queryRepTimeSales(map);
	}

	
	/*
	 * ======�ŵ����۷�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepActiveSales(Map<String, String> map) {
		return storeDao.queryRepActiveSales(map);
	}

	
	/*
	 * ======�ŵ��Ʒ���۷�������========================================================
	 */
	
	public Map<String, List<Object>> queryRepProductSales(Map<String, String> map){
		return storeDao.queryRepProductSales(map);
	}
	

	/*
	 * ======�ŵ���Сʱ���۷�������======================================================
	 */
	
	public Map<String, List<Object>> queryRepHalfTimeSales(Map<String, String> map){
		return storeDao.queryRepHalfTimeSales(map);
	}
	
	
	/*
	 * ======�ŵ꽻����ˮ��ϸ����========================================================
	 */
	
	public Map<String, List<Object>> queryRepSalesDetail(Map<String, String> map){
		return storeDao.queryRepSalesDetail(map);
	}

	
	/*
	 * ======�ŵ꽻����ˮ������ϸ��ϸ======================================================
	 */
	
	public Map<String, List<Object>> queryRepSalesDetailOrderDetail(Map<String, String> map){
		return storeDao.queryRepSalesDetailOrderDetail(map);
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(��������)====================================================
	 */
	
	public Map<String, List<Object>> queryRepSalesCup(Map<String, String> map){
		return storeDao.queryRepSalesCup(map);
	}
	
	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)==================================================
	 */
	
	public Map<String, List<Object>> queryRepSalesCupDetail(Map<String, String> map){
		return storeDao.queryRepSalesCupDetail(map);
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)����Excel===========================================
	 */
	public Map<String, List<Object>> queryRepSalesCupDetailExportExcel(Map<String, String> map){
		return storeDao.queryRepSalesCupDetailExportExcel(map);
	}
	
}
