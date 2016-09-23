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
import com.dev.core.service.MerchantService;
import com.dev.core.util.ExportExcelUtils;

@Controller
@RequestMapping("/merchant")
@SessionAttributes("currentUser")
public class MerchantController {
	
	// 记录日志信息
	private static Logger logger = Logger.getLogger(StoreController.class);
	
	@Autowired
	MerchantService merchantService;
	
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
				return new ModelAndView("merchant" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}
	/*
	 * 导出商家信息1的Excel文件
	 */
	@RequestMapping("/queryMerchantDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetailExportExcel(HttpServletRequest request,
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

		Map<String, String> mapJob = new HashMap<String, String>();
		mapJob.put(new String("companyCode"), companyCode);
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("storeCode"), StoreCode);
		mapJob.put(new String("PageIndex"), "1");
		mapJob.put(new String("PageSize"), "100000");


		map = merchantService.queryMerchantDetail(mapJob);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("商家信息报表", "商家信息报表.xls", "商家信息报表",queryText, listData, listDataSum, response);
		
		return null;
	}
	//商家信息1
	@RequestMapping("/queryMerchantDetail")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetail(HttpServletRequest request, HttpSession session) {
		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// 商家名称
		String FullName = request.getParameter("FullName");
		// 联系人名称
		String ContractPerson = request.getParameter("ContractPerson");
		
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapMerchant = new HashMap<String, String>();
		
		mapMerchant.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapMerchant.put(new String("PageSize"), String.valueOf(PageSize));
		mapMerchant.put(new String("ContractPerson"), ContractPerson);
		mapMerchant.put(new String("FullName"), FullName);
		
		map = merchantService.queryMerchantDetail(mapMerchant);
		
		return map;
	}
	
	/*
	 * 导出商家信息2的Excel文件
	 */
	@RequestMapping("/queryMerchantDetailExportExcel2")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetailExportExcel2(HttpServletRequest request,
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
		//需要对js请求发过来的数据进行转码操作
		//商家名称
		String FullName = new String(request.getParameter("FullName").getBytes("ISO8859-1"),"UTF-8");
		//联系人名称
		String ContractPerson = request.getParameter("ContractPerson");

		String img1 = request.getParameter("img1");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String nowData = dateFormat.format(data);

		queryText += "\n" + "导出时间:" + nowData;
		logger.info("queryText=" + queryText);

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mapJob = new HashMap<String, String>();
		mapJob.put(new String("companyCode"), companyCode);
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("storeCode"), StoreCode);
		mapJob.put(new String("PageIndex"), "1");
		mapJob.put(new String("PageSize"), "100000");
		mapJob.put(new String("FullName"), FullName);
		mapJob.put(new String("ContractPerson"), ContractPerson);


		map = merchantService.queryMerchantDetail2(mapJob);

		// Excel数据
		List<Object> listData = map.get("listData");

		// Excel合计数据
		List<Object> listDataSum = map.get("listDataSum");

		// 实例化 导出Excel工具
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("商家信息报表2", "商家信息报表2.xls", "商家信息报表2",queryText, listData, listDataSum, response);
		
		
		return null;
	}
	
	//商家信息2
	@RequestMapping("/queryMerchantDetail2")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetail2(HttpServletRequest request, HttpSession session) {
		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// 商家名称 
		String FullName = request.getParameter("FullName");
		// 联系人名称
		String ContractPerson = request.getParameter("ContractPerson");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapMerchant = new HashMap<String, String>();
		
		mapMerchant.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapMerchant.put(new String("PageSize"), String.valueOf(PageSize));
		mapMerchant.put(new String("ContractPerson"), ContractPerson);
		mapMerchant.put(new String("FullName"), FullName);
		
		map = merchantService.queryMerchantDetail2(mapMerchant);
		
		return map;
	}
	
}
