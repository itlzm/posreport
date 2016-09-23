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
 * 门店营收日报分析 Controller
 * */
@Controller
@RequestMapping(value = "/store")
@SessionAttributes("currentUser")
public class StoreController {

	// 记录日志信息
	private static Logger logger = Logger.getLogger(StoreController.class);

	@Autowired
	StoreService storeService;

	// 通用模块转向页面定义
	@RequestMapping(value = "/{pageName}")
	public ModelAndView RedirectView(@PathVariable String pageName, HttpSession session) {
		try {
			logger.info("pageName: " + pageName);
			// 判断用户是否已经登录
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
	 * ======门店支付方式分析报表========================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/queryReppaymode")
	public @ResponseBody Map<String, List<Object>> queryReppaymode(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
		 * 查询支付方式结构分析
		 */
		List<Object> storePayMModelList = storeService.getStorePayMModel(mappampay);

		/*
		 * 查询支付方式
		 */
		List<Object> storePayTypeList = storeService.getStorePayType(mappampay);

		map = storeService.queryReppaymode(mappampay, storePayMModelList, storePayTypeList);

		return map;
	}

	/*
	 * ======门店支付方式分析报表导出Excle==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/queryReppaymodeExportExcel")
	public @ResponseBody Map<String, List<Object>> queryReppaymodeExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String img1 = request.getParameter("img1");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;
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
		 * 查询支付方式结构分析
		 */
		List<Object> storePayMModelList = storeService.getStorePayMModel(mappampay);

		/*
		 * 查询支付方式
		 */
		List<Object> storePayTypeList = storeService.getStorePayType(mappampay);

		map = storeService.queryReppaymode(mappampay, storePayMModelList, storePayTypeList);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("门店支付方式分析明细", "门店支付方式分析明细.xls", "门店支付方式分析明细", queryText, listData, listDataSum, response);

		return null;

	}

	/*
	 * ======门店支付方式明细报表========================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryReppayDetail")
	public @ResponseBody Map<String, List<Object>> queryReppayDetail(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店 | 分割
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		// 支付方式
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
	 * ======门店支付方式明细报表导出Excel=================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryReppayDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryReppayDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

		logger.info("queryText=" + queryText);

		// 支付方式
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

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店支付方式明细", "门店支付方式明细.xls", "门店支付方式明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店渠道营收分析报表========================================================
	 */
	@RequestMapping(value = "/queryRepChannelSales")
	public @ResponseBody Map<String, List<Object>> queryRepChannelSales(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店渠道营收分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepChannelSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepChannelSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelCode = request.getParameter("ChannelCode");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("channelSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店渠道营收分析明细", "门店渠道营收分析明细.xls", "门店渠道营收分析", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店分时销售分析报表========================================================
	 */
	@RequestMapping(value = "/queryRepTimeSales")
	public @ResponseBody Map<String, List<Object>> queryRepTimeSales(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BranCode = request.getParameter("BranCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店分时销售分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepTimeSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepTimeSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BranCode = request.getParameter("BranCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店分时销售分析明细", "门店分时销售分析明细.xls", "门店分时销售分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店活动销售分析报表========================================================
	 */
	@RequestMapping(value = "/queryRepActiveSales")
	public @ResponseBody Map<String, List<Object>> queryRepActiveSales(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店活动销售分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActiveSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActiveSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = URLDecoder.decode(request.getParameter("favourableName"), "UTF-8");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店活动销售分析明细", "门店活动销售分析明细.xls", "门店活动销售分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店产品销售分析报表========================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepProductSales")
	public @ResponseBody Map<String, List<Object>> queryRepProductSales(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店产品销售分析报表导出Excel==================================================
	 * ======
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepProductSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String companyCode = userModel.getCompanyCode();
		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String OrderByCol = request.getParameter("OrderByCol");
		String OrderBySX = request.getParameter("OrderBySX");
		String TopNum = request.getParameter("TopNum");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店产品销售分析明细", "门店产品销售分析明细.xls", "门店产品销售分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店半个小时销售分析报表======================================================
	 */
	@RequestMapping(value = "/queryRepHalfTimeSales")
	public @ResponseBody Map<String, List<Object>> queryRepHalfTimeSales(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BranCode = request.getParameter("BranCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店半个小时销售分析报表导出Excel===============================================
	 */
	@RequestMapping(value = "/queryRepHalfTimeSalesExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepHalfTimeSalesExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BranCode = request.getParameter("BranCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店半小时销售分析明细", "门店半小时销售分析明细.xls", "门店半小时销售分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店交易流水明细报表========================================================
	 */
	@RequestMapping(value = "/queryRepSalesDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetail(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店交易流水单据详细明细======================================================
	 */
	@RequestMapping(value = "/queryRepSalesDetailOrderDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetailOrderDetail(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
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
	 * ======门店交易流水单据详细明细导出Excel===============================================
	 */
	@RequestMapping(value = "/queryRepSalesDetailOrderDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepSalesDetailOrderDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelCode = request.getParameter("ChannelCode");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店交易流水明细", "门店交易流水明细.xls", "门店交易流水明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======门店杯量统计报表(杯量汇总)====================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepSalesCup")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCup(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		// 门店
		String StoreCode = request.getParameter("StoreCode");

		// 渠道
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
		// 查询条件
		map = storeService.queryRepSalesCup(mappampay);
		return map;
	}

	/*
	 * ======门店杯量统计报表(杯量销售明细)==================================================
	 */
	@RequestMapping(value = "/queryRepSalesCupDetail")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCupDetail(HttpServletRequest request,
			HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		// 区域
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
		// 查询条件
		map = storeService.queryRepSalesCupDetail(mappampay);
		return map;
	}

	/*
	 * ======门店杯量统计报表(杯量销售明细)导出Excel===========================================
	 */
	@RequestMapping(value = "/queryRepSalesCupDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepSalesCupDetailExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// 品牌
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;

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

		// 查询条件
		map = storeService.queryRepSalesCupDetailExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("门店杯量统计报表明细", "门店杯量统计报表明细.xls", "门店杯量统计报表明细", queryText, listData, listDataSum, response);

		return null;
	}

}
