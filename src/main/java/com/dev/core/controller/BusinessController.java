package com.dev.core.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
import com.dev.core.service.BusinessService;
import com.dev.core.util.ExportExcelUtils;

/*
 * 
 * 营业收入汇总分析控制器
 * */
@Controller
@RequestMapping(value = "/business")
@SessionAttributes("currentUser")
public class BusinessController {

	@Autowired
	BusinessService businessService;

	// 记录日志信息
	private static Logger logger = Logger.getLogger(BusinessController.class);

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
				return new ModelAndView("business" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ======营业收入趋势分析报表(品牌汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepTrendBrand")
	public @ResponseBody Map<String, List<Object>> queryRepTrendBrand(HttpServletRequest request, HttpSession session) {

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
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);

		map = businessService.queryRepTrendBrand(mappampay);

		return map;
	}

	/*
	 * ======营业收入趋势分析报表(区域汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepTrendBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepTrendBrandChildren(HttpServletRequest request,
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
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelCode = request.getParameter("ChannelCode");
		String AreaCode = request.getParameter("AreaCode");

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
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepTrendBrandChildren(mappampay);

		return map;
	}

	/*
	 * ======营业收入趋势分析报表(门店汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepTrend")
	public @ResponseBody Map<String, List<Object>> queryRepTrend(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 品牌
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");
		// 门店
		String StoreCode = request.getParameter("StoreCode");
		// 渠道
		String ChannelCode = request.getParameter("ChannelCode");

		// 区域
		String AreaCode = request.getParameter("AreaCode");

		String AreaName = request.getParameter("AreaName");

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
		mappampay.put(new String("ChannelCode"), ChannelCode);

		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = businessService.queryRepTrend(mappampay);

		return map;
	}

	/*
	 * ======营业收入趋势分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepTrendExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepTrendExportExcel(HttpServletRequest request,
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
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);

		map = businessService.queryRepTrendExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("营业收入趋势分析明细", "营业收入趋势分析明细.xls", "营业收入趋势分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======营业收入环比分析报表========================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHB")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHB(HttpServletRequest request, HttpSession session) {

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
		String ChannelName = request.getParameter("ChannelName");

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

		map = businessService.queryRepBusinessHB(mappampay);
		return map;
	}

	/*
	 * ======营业收入环比分析点击品牌报表====================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildren(HttpServletRequest request,
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
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepBusinessHBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======营业收入环比分析点击品牌门店报表==================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(HttpServletRequest request,
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
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepBusinessHBBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======营业收入环比分析点击品牌门店报表导出Excel===========================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildrenStoreExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepBusinessHBBrandChildrenStoreExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("营业收入环比分析明细", "营业收入环比分析明细.xls", "营业收入环比分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======营业收入同比分析报表========================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTB")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTB(HttpServletRequest request, HttpSession session) {

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

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("BrandCode"), BrandCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTB(mappampay);
		return map;
	}

	/*
	 * ======营业收入同比分析点击品牌报表====================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBBrandChildren(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======营业收入同比分析点击品牌门店报表==================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("AreaCode"), AreaCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析报表===============================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDate")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDate(HttpServletRequest request,
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

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("BrandCode"), BrandCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDate(mappampay);
		return map;
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析点击品牌报表===========================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析点击品牌门店报表=========================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("AreaCode"), AreaCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======(自定义时段)营业收入同比日期分析点击品牌门店报表导出Excel==================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildrenStoreExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildrenStoreExportExcel(mappampay);
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("营业收入日期范围同比分析明细", "营业收入日期范围同比分析明细.xls", "营业收入日期范围同比分析明细", queryText, listData, listDataSum,
				response);

		return null;
	}

	/*
	 * ======产品分类同比分析(品牌汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrand")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrand(HttpServletRequest request,
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
		String ChannelName = request.getParameter("ChannelName");

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepProductCatTBBrand(mappampay);
		return map;

	}

	/*
	 * ======产品分类同比分析(分类汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildren(HttpServletRequest request,
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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

		String companyCode = userModel.getCompanyCode();
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		map = businessService.queryRepProductCatTBBrandChildren(mappampay);
		return map;

	}

	/*
	 * ======产品分类同比分析(产品汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildrenProduct")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(HttpServletRequest request,
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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");
		
		
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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		map = businessService.queryRepProductCatTBBrandChildrenProduct(mappampay);
		return map;

	}

	/*
	 * ======产品分类同比分析导出Excel===================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildrenProductExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		
		map = businessService.queryRepProductCatTBBrandChildrenProductExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("产品分类同比分析明细", "产品分类同比分析明细.xls", "产品分类同比分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======产品分类环比分析(品牌汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrand")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrand(HttpServletRequest request,
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
		String ChannelName = request.getParameter("ChannelName");

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrand(mappampay);
		return map;
	}

	/*
	 * ======产品分类环比分析(分类汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildren(HttpServletRequest request,
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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======产品分类环比分析(产品汇总)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildrenProduct")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(HttpServletRequest request,
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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildrenProduct(mappampay);
		return map;
	}

	/*
	 * ======产品分类环比分析导出Excel===================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildrenProductExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		// 分类
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildrenProductExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("产品分类环比分析明细", "产品分类环比分析明细.xls", "产品分类环比分析明细", queryText, listData, listDataSum, response);

		return null;
	}
}
