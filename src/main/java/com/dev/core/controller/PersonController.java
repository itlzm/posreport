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
import com.dev.core.service.PersonService;
import com.dev.core.util.ExportExcelUtils;


/*
 * 个人日志分析Controller
 */
@Controller
@RequestMapping(value = "/person")
@SessionAttributes("currentUser")
public class PersonController {
	
	// 记录日志信息
	private static Logger logger = Logger.getLogger(StoreController.class);

	@Autowired
	PersonService personService;
	
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
				return new ModelAndView("person" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}
	
	//作业日志分析
	@RequestMapping("/queryRepjobDetail")
	public @ResponseBody Map<String, List<Object>> queryRepjobDetail(HttpServletRequest request, HttpSession session) {
		
		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		// 执行步骤
		String JobStep = request.getParameter("JobStep");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		// 获取jobTime值，下钻时使用。
		String JobTime = request.getParameter("JobTime");
		
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("jobStep"), JobStep);
		mapJob.put(new String("jobTime"), JobTime);
		
		map = personService.queryRepjobDetail(mapJob);
		
		return map;
	}
	
	//导出作业日志分析Excel
	@RequestMapping("/queryRepjobDetailExportExcel")
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
		// 获取执行步骤
		String JobStep = request.getParameter("JobStep");

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
		mappampay.put(new String("jobDateStart"), TimeStart);
		mappampay.put(new String("jobDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "1000000");
		mappampay.put(new String("Paymentkey"), Paymentkey);
		mappampay.put(new String("jobStep"), JobStep);

		map = personService.queryRepjobDetail(mappampay);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("作业日志分析", "作业日志分析.xls", "作业日志分析", queryText, listData, listDataSum, response);
		
		return null;
	}
	
	//作业日志统计分析
	@RequestMapping("queryRepjobStatistics")
	public @ResponseBody Map<String, List<Object>> queryRepjobStatistics(HttpServletRequest request, HttpSession session) {
		
		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		
		map = personService.queryRepjobStatistics(mapJob);
		
		return map;
	}
	//导出作业日志统计分析Excel
	@RequestMapping("/queryRepjobStatisticsExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepjobStatisticsExportExcel(HttpServletRequest request,
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
		// 获取jobTime，在下钻时导出Excel使用。
		String JobTime = request.getParameter("JobTime");
		
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
		mappampay.put(new String("jobDateStart"), TimeStart);
		mappampay.put(new String("jobDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "1000000");
		mappampay.put(new String("Paymentkey"), Paymentkey);
		mappampay.put(new String("jobTime"), JobTime);

		//判断jobTime是否为空，导出下钻时Excel表格，否则导出第一级页面二维表格内容
		if(JobTime != ""){
			map = personService.queryRepjobDetail(mappampay);
		}else{
			map = personService.queryRepjobStatistics(mappampay);
		}

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("作业日志分析", "作业日志分析.xls", "作业日志分析", queryText, listData, listDataSum, response);
		
		return null;
	}
}
