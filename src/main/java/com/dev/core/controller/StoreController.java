package com.dev.core.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dev.core.model.UserModel;
import com.dev.core.service.StoreService;
import com.dev.core.util.ExportExcelUtils;


/*
 * �ŵ�Ӫ���ձ����� Controller
 * */
@Controller
@RequestMapping(value = "/store")
@SessionAttributes("currentUser")
public class StoreController {

	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(StoreController.class);

	@Autowired
	StoreService storeService;

	// ͨ��ģ��ת��ҳ�涨��
	@RequestMapping(value = "/{pageName}")
	public ModelAndView RedirectView(@PathVariable String pageName, HttpSession session) {
		try {
			logger.info("pageName: " + pageName);
			// �ж��û��Ƿ��Ѿ���¼
			UserModel userModel = (UserModel) session.getAttribute("currentUser");
			if (userModel == null) {
				return new ModelAndView("lockpage");
			} else {
				return new ModelAndView("store" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ======�ŵ�֧����ʽ��������========================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/queryReppaymode")
	public @ResponseBody Map<String, List<Object>> queryReppaymode(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		/*
		 * ��ѯ֧����ʽ�ṹ����
		 */
		List<Object> storePayMModelList = storeService.getStorePayMModel(mappampay);

		/*
		 * ��ѯ֧����ʽ
		 */
		List<Object> storePayTypeList = storeService.getStorePayType(mappampay);

		map = storeService.queryReppaymode(mappampay, storePayMModelList, storePayTypeList);

		return map;
	}

	/*
	 * ======�ŵ�֧����ʽ����������Excle==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/queryReppaymodeExportExcel")
	public @ResponseBody Map<String, List<Object>> queryReppaymodeExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String img1 = request.getParameter("img1");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;
		logger.info("queryText=" + queryText);

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		/*
		 * ��ѯ֧����ʽ�ṹ����
		 */
		List<Object> storePayMModelList = storeService.getStorePayMModel(mappampay);

		/*
		 * ��ѯ֧����ʽ
		 */
		List<Object> storePayTypeList = storeService.getStorePayType(mappampay);

		map = storeService.queryReppaymode(mappampay, storePayMModelList, storePayTypeList);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("�ŵ�֧����ʽ������ϸ", "�ŵ�֧����ʽ������ϸ.xls", "�ŵ�֧����ʽ������ϸ", queryText, listData, listDataSum, response);

		return null;

	}

	/*
	 * ======�ŵ�֧����ʽ��ϸ����========================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryReppayDetail")
	public @ResponseBody Map<String, List<Object>> queryReppayDetail(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ� | �ָ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		// ֧����ʽ
		String Paymentkey = request.getParameter("Paymentkey");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("Paymentkey"), Paymentkey);

		map = storeService.queryReppayDetail(mappampay);
		return map;
	}

	/*
	 * ======�ŵ�֧����ʽ��ϸ������Excel=================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryReppayDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryReppayDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		logger.info("queryText=" + queryText);

		// ֧����ʽ
		String Paymentkey = request.getParameter("Paymentkey");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "1000000");
		mappampay.put(new String("Paymentkey"), Paymentkey);

		map = storeService.queryReppayDetail(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ�֧����ʽ��ϸ", "�ŵ�֧����ʽ��ϸ.xls", "�ŵ�֧����ʽ��ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ�����Ӫ�շ�������========================================================
	 */
	@RequestMapping(value = "/queryRepChannelSales")
	public @ResponseBody Map<String, List<Object>> queryRepChannelSales(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);

		map = storeService.queryRepChannelSales(mappampay);
		return map;
	}

	/*
	 * ======�ŵ�����Ӫ�շ���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepChannelSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepChannelSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);

		map = storeService.queryRepChannelSales(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("channelSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ�����Ӫ�շ�����ϸ", "�ŵ�����Ӫ�շ�����ϸ.xls", "�ŵ�����Ӫ�շ���", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ��ʱ���۷�������========================================================
	 */
	@RequestMapping(value = "/queryRepTimeSales")
	public @ResponseBody Map<String, List<Object>> queryRepTimeSales(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BranCode = request.getParameter("BranCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BranCode"), BranCode);

		map = storeService.queryRepTimeSales(mappampay);
		return map;
	}

	/*
	 * ======�ŵ��ʱ���۷���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepTimeSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepTimeSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BranCode = request.getParameter("BranCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BranCode"), BranCode);

		map = storeService.queryRepTimeSales(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ��ʱ���۷�����ϸ", "�ŵ��ʱ���۷�����ϸ.xls", "�ŵ��ʱ���۷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ����۷�������========================================================
	 */
	@RequestMapping(value = "/queryRepActiveSales")
	public @ResponseBody Map<String, List<Object>> queryRepActiveSales(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);

		map = storeService.queryRepActiveSales(mappampay);
		return map;
	}

	/*
	 * ======�ŵ����۷���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActiveSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActiveSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = URLDecoder.decode(request.getParameter("favourableName"), "UTF-8");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);

		map = storeService.queryRepActiveSales(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ����۷�����ϸ", "�ŵ����۷�����ϸ.xls", "�ŵ����۷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ��Ʒ���۷�������========================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepProductSales")
	public @ResponseBody Map<String, List<Object>> queryRepProductSales(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String OrderByCol = request.getParameter("OrderByCol");
		String OrderBySX = request.getParameter("OrderBySX");
		String TopNum = request.getParameter("TopNum");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));

		mappampay.put(new String("OrderByCol"), OrderByCol);
		mappampay.put(new String("OrderBySX"), OrderBySX);
		mappampay.put(new String("TopNum"), TopNum);

		map = storeService.queryRepProductSales(mappampay);
		return map;
	}

	/*
	 * ======�ŵ��Ʒ���۷���������Excel==================================================
	 * ======
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepProductSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String companyCode = userModel.getCompanyCode();
		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String OrderByCol = request.getParameter("OrderByCol");
		String OrderBySX = request.getParameter("OrderBySX");
		String TopNum = request.getParameter("TopNum");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("OrderByCol"), OrderByCol);
		mappampay.put(new String("OrderBySX"), OrderBySX);
		mappampay.put(new String("TopNum"), TopNum);

		map = storeService.queryRepProductSales(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ��Ʒ���۷�����ϸ", "�ŵ��Ʒ���۷�����ϸ.xls", "�ŵ��Ʒ���۷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ���Сʱ���۷�������======================================================
	 */
	@RequestMapping(value = "/queryRepHalfTimeSales")
	public @ResponseBody Map<String, List<Object>> queryRepHalfTimeSales(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BranCode = request.getParameter("BranCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BranCode"), BranCode);

		map = storeService.queryRepHalfTimeSales(mappampay);
		return map;
	}

	/*
	 * ======�ŵ���Сʱ���۷���������Excel===============================================
	 */
	@RequestMapping(value = "/queryRepHalfTimeSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepHalfTimeSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BranCode = request.getParameter("BranCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BranCode"), BranCode);

		map = storeService.queryRepHalfTimeSales(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ��Сʱ���۷�����ϸ", "�ŵ��Сʱ���۷�����ϸ.xls", "�ŵ��Сʱ���۷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ꽻����ˮ��ϸ����========================================================
	 */
	@RequestMapping(value = "/queryRepSalesDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetail(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("IsExportExcel"), "N");
		map = storeService.queryRepSalesDetail(mappampay);
		return map;
	}

	/*
	 * ======�ŵ꽻����ˮ������ϸ��ϸ======================================================
	 */
	@RequestMapping(value = "/queryRepSalesDetailOrderDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetailOrderDetail(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String OrderCode = request.getParameter("OrderCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("OrderCode"), OrderCode);

		map = storeService.queryRepSalesDetailOrderDetail(mappampay);
		return map;
	}

	/*
	 * ======�ŵ꽻����ˮ������ϸ��ϸ����Excel===============================================
	 */
	@RequestMapping(value = "/queryRepSalesDetailOrderDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetailOrderDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("IsExportExcel"), "Y");
		map = storeService.queryRepSalesDetail(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ꽻����ˮ��ϸ", "�ŵ꽻����ˮ��ϸ.xls", "�ŵ꽻����ˮ��ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(��������)====================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepSalesCup")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCup(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");

		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		// ��ѯ����
		map = storeService.queryRepSalesCup(mappampay);
		return map;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)==================================================
	 */
	@RequestMapping(value = "/queryRepSalesCupDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCupDetail(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("ProductUomCode"), AreaCode);
		// ��ѯ����
		map = storeService.queryRepSalesCupDetail(mappampay);
		return map;
	}

	/*
	 * ======�ŵ걭��ͳ�Ʊ���(����������ϸ)����Excel===========================================
	 */
	@RequestMapping(value = "/queryRepSalesCupDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCupDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		logger.info("queryText=" + queryText);

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		// ��ѯ����
		map = storeService.queryRepSalesCupDetailExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ŵ걭��ͳ�Ʊ�����ϸ", "�ŵ걭��ͳ�Ʊ�����ϸ.xls", "�ŵ걭��ͳ�Ʊ�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

}
