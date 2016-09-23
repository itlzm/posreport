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
import com.dev.core.service.ActivityService;
import com.dev.core.util.ExportExcelUtils;

/*
 * 
 * 营销活动效果分析控制器
 * */
@Controller
@RequestMapping(value = "/activity")
@SessionAttributes("currentUser")
public class ActivityController {

	@Autowired
	ActivityService activityService;

	// 记录日志信息
	private static Logger logger = Logger.getLogger(ActivityController.class);

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
				return new ModelAndView("activity" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ======渠道活动引流分析报表(品牌汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityBrand")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityBrand(HttpServletRequest request,
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// 查询条件
		map = activityService.queryRepChannelActivityBrand(mappampay);
		return map;
	}

	/*
	 * ======渠道活动引流分析报表(区域汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityBrandChildren(HttpServletRequest request,
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
		// 区域
		String AreaCode = request.getParameter("AreaCode");
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);

		// 查询条件
		map = activityService.queryRepChannelActivityBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======渠道活动引流分析报表(门店汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivity")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivity(HttpServletRequest request,
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

		String favourableName = request.getParameter("favourableName");

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
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = activityService.queryRepChannelActivity(mappampay);
		return map;
	}

	/*
	 * ======渠道活动引流分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityExportExcel(HttpServletRequest request,
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

		map = activityService.queryRepChannelActivityExportExcel(mappampay);
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("渠道活动引流分析明细", "渠道活动引流分析明细.xls", "渠道活动引流分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======活动横向对比分析报表(品牌汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseBrand")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseBrand(HttpServletRequest request,
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// 查询条件
		map = activityService.queryRepActivityTransverseBrand(mappampay);
		return map;
	}

	/*
	 * ======活动横向对比分析报表(区域汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseBrandChildren(HttpServletRequest request,
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
		// 区域
		String AreaCode = request.getParameter("AreaCode");
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);

		// 查询条件
		map = activityService.queryRepActivityTransverseBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======活动横向对比分析报表(门店汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverse")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverse(HttpServletRequest request,
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

		String favourableName = request.getParameter("favourableName");

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
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = activityService.queryRepActivityTransverse(mappampay);
		return map;
	}

	/*
	 * ======活动横向对比分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseExportExcel(HttpServletRequest request,
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

		map = activityService.queryRepActivityTransverseExportExcel(mappampay);
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("活动横向对比分析明细", "活动横向对比分析明细.xls", "活动横向对比分析", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======套餐及单品折扣分析报表(品牌汇总)=================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepMealSingleBrand")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleBrand(HttpServletRequest request,
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
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		// 查询条件
		map = activityService.queryRepMealSingleBrand(mappampay);
		return map;
	}

	/*
	 * ======套餐及单品折扣分析报表(区域汇总)=================================================
	 */
	@RequestMapping(value = "/queryRepMealSingleBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleBrandChildren(HttpServletRequest request,
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
		// 查询条件
		map = activityService.queryRepMealSingleBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======套餐及单品折扣分析报表(门店汇总)=================================================
	 */
	@RequestMapping(value = "/queryRepMealSingle")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingle(HttpServletRequest request, HttpSession session) {

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

		String AreaName = request.getParameter("AreaName");

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
		mappampay.put(new String("AreaName"), AreaName);

		// 查询条件
		map = activityService.queryRepMealSingle(mappampay);
		return map;
	}

	/*
	 * ======套餐及单品折扣分析报表导出Excle================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepMealSingleExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleExportExcel(HttpServletRequest request,
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
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");

		mappampay.put(new String("BrandCode"), BrandCode);

		// 查询条件
		map = activityService.queryRepMealSingleExportExcel(mappampay);
		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("套餐及单品折扣分析明细", "套餐及单品折扣分析明细.xls", "套餐及单品折扣分析明细", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======活动营收贡献分析报表(品牌汇总)===================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueBrand")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueBrand(HttpServletRequest request,
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// 查询条件
		map = activityService.queryRepActivityRevenueBrand(mappampay);

		logger.info("map=" + map);

		return map;
	}

	/*
	 * ======活动营收贡献分析报表(区域汇总)===================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueBrandChildren(HttpServletRequest request,
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
		// 区域
		String AreaCode = request.getParameter("AreaCode");
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
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);
		// 查询条件
		map = activityService.queryRepActivityRevenueBrandChildren(mappampay);

		return map;
	}

	/*
	 * ======活动营收贡献分析报表(门店汇总)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenue")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenue(HttpServletRequest request,
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

		String favourableName = request.getParameter("favourableName");

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
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		// 查询条件
		map = activityService.queryRepActivityRevenue(mappampay);

		logger.info("map=" + map);

		return map;
	}

	/*
	 * ======活动营收贡献分析报表导出Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueExportExcel(HttpServletRequest request,
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

		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("favourableName"), favourableName);

		// 查询条件
		map = activityService.queryRepActivityRevenueExportExcel(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("活动营收贡献分析明细", "活动营收贡献分析明细.xls", "活动营收贡献分析明细", queryText, listData, listDataSum, response);

		return null;
	}

}
