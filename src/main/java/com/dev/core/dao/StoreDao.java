package com.dev.core.dao;

import java.util.List;
import java.util.Map;


import com.dev.core.util.PageModel;
@SuppressWarnings("rawtypes")
public interface StoreDao {

	/*
	 * �����������ݼ���
	 * 
	 * @moduleName ģ�����
	 */
	
	public List getSelectDropDownList(Map<String, String> map);

	/*
	 * ���ݲ�ѯ��䷵�ز�ѯ��Ϣ
	 */
	public List getQueryData(Map<String, String> map, PageModel pageModel);

	/*
	 * ֧����ʽ�ṹ����
	 */
	public List getStorePayMModel(Map<String, String> map);

	/*
	 * ֧����ʽ
	 */
	public List getStorePayType(Map<String, String> map);

	/*
	 * ======�ŵ�֧����ʽ��������========================================================
	 */
	public Map<String, List<Object>> queryReppaymode(Map<String, String> map, List<Object> storePayMModelList,
			List<Object> storePayTypeList);

	/*
	 * ======�ŵ�֧����ʽ��ϸ����========================================================
	 */
	public Map<String, List<Object>> queryReppayDetail(Map<String, String> map);

	/*
	 * ======�ŵ�����Ӫ�շ�������========================================================
	 */
	public Map<String, List<Object>> queryRepChannelSales(Map<String, String> map);

	/*
	 * ======�ŵ��ʱ���۷�������========================================================
	 */
	public Map<String, List<Object>> queryRepTimeSales(Map<String, String> map);

	/*
	 * ======�ŵ����۷�������========================================================
	 */
	public Map<String, List<Object>> queryRepActiveSales(Map<String, String> map);

	/*
	 * ======�ŵ��Ʒ���۷�������========================================================
	 */
	public Map<String, List<Object>> queryRepProductSales(Map<String, String> map);

	/*
	 * ======�ŵ���Сʱ���۷�������======================================================
	 */
	public Map<String, List<Object>> queryRepHalfTimeSales(Map<String, String> map);

	/*
	 * ======�ŵ꽻����ˮ��ϸ����========================================================
	 */
	public Map<String, List<Object>> queryRepSalesDetail(Map<String, String> map);

	
	/*
	 * ======�ŵ꽻����ˮ������ϸ��ϸ======================================================
	 */
	public Map<String, List<Object>> queryRepSalesDetailOrderDetail(Map<String, String> map);


	/*
	 * ======�ŵ걭��ͳ�Ʊ���(��������)====================================================
	 */
	public Map<String, List<Object>> queryRepSalesCup(Map<String, String> map);

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)==================================================
	 */
	public Map<String, List<Object>> queryRepSalesCupDetail(Map<String, String> map);
	
	
	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)����Excel===========================================
	 */
	public Map<String, List<Object>> queryRepSalesCupDetailExportExcel(Map<String, String> map);
	
}
