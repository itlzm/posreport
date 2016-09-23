package com.dev.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.dev.core.service.RoleService;

import net.sf.json.JSONObject;

/*
 * 权限控制器
 * */
@Controller
@RequestMapping(value = "/systemui")
@SessionAttributes("currentUser")
public class RoleController {
	// 记录日志信息
	private static Logger logger = Logger.getLogger(RoleController.class);

	@Autowired
	RoleService roleService;

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
				return new ModelAndView("systemui" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * 查询角色权限列表
	 */
	@RequestMapping(value = "/queryRoleData")
	public @ResponseBody Map<String, List<Object>> queryRoleData(HttpServletRequest request, HttpSession session) {

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
		String StoreName = request.getParameter("StoreName");
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
		mappampay.put(new String("storeCode"), StoreName);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("Paymentkey"), Paymentkey);

		// 查询条件
		logger.info("查询条件:TimeStart:" + TimeStart + ",TimeEnd:" + TimeEnd + ",BrandName:" + BrandName + ",StoreName:"
				+ StoreName + ",ChannelName:" + ChannelName + ",Paymentkey:" + Paymentkey);
		map = roleService.querySystemRoleData(mappampay);
		return map;
	}

	/*
	 * 获取菜单功能及菜单按钮数据
	 */
	@RequestMapping(value = "/getFunButTreeData")
	public @ResponseBody Map<String, List<Object>> getFunButTreeData(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 角色RoleID
		String roleID = request.getParameter("roleID");
		// 是否为修改状态
		String isEdit = request.getParameter("isEdit");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("roleID"), roleID);
		mappampay.put(new String("isEdit"), isEdit);

		map = roleService.getFunButTreeData(mappampay);

		return map;
	}

	/*
	 * 保存角色权限
	 */
	@RequestMapping(value = "/saveRoleData")
	@SuppressWarnings({"unchecked" })
	public @ResponseBody Map<String, List<Object>> saveRoleData(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String sendData = request.getParameter("sendData");
		JSONObject jasonObject = JSONObject.fromObject(sendData);
		Map<String, List<Object>> mapRoleData =( Map<String, List<Object>>)jasonObject;
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);

		map = roleService.saveRoleData(mapRoleData,mapPam);
		return map;
	}
	
	/*
	 * 删除角色权限
	 */
	@RequestMapping(value = "/delRoleData")
	public @ResponseBody Map<String, List<Object>> delRoleData(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		String roleID = request.getParameter("roleID");
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);
		mapPam.put(new String("roleID"), roleID);
		map = roleService.delRoleData(mapPam);
		return map;
	}
	
	/*
	 * 查询用户管理列表
	 */
	@RequestMapping(value = "/queryEmployeeData")
	@SuppressWarnings({ "unused" })
	public @ResponseBody Map<String, List<Object>> queryEmployeeData(HttpServletRequest request, HttpSession session) {

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
		String StoreName = request.getParameter("StoreName");
		// 渠道
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreName);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		
		map = roleService.queryEmployeeData(mappampay);
		return map;
	}
	
	/*
	 * 查询用户信息及对应角色权限
	 */
	@RequestMapping(value = "/queryEmployeeCodeData")
	public @ResponseBody Map<String, List<Object>> queryEmployeeCodeData(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// 用户编码
		String employeeCode = request.getParameter("employeeCode");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("employeeCode"), employeeCode);
		
		map = roleService.queryEmployeeCodeData(mappampay);
		
		return map;
	}
	
	/*
	 * 保存用户角色权限
	 */
	@RequestMapping(value = "/saveEmployeeRoleData")
	@SuppressWarnings({ "unchecked" })
	public @ResponseBody Map<String, List<Object>> saveEmployeeRoleData(HttpServletRequest request, HttpSession session) {

		// 获取当前登录用户
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String sendData = request.getParameter("sendData");
		JSONObject jasonObject = JSONObject.fromObject(sendData);
		Map<String, List<Object>> mapRoleData =( Map<String, List<Object>>)jasonObject;
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);

		map = roleService.saveEmployeeRoleData(mapRoleData,mapPam);
		return map;
	}
	

}
