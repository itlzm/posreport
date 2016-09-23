package com.dev.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dev.core.model.UserModel;
import com.dev.core.service.StoreService;
import com.dev.core.service.UserService;

/*
 * 登录控制器
 * */
@Controller
@SessionAttributes("currentUser")
public class LoginController {

	// 注入用户服务
	@Autowired
	UserService userService;

	@Autowired
	StoreService storeService;

	// 记录日志信息
	private static Logger logger = Logger.getLogger(LoginController.class);

	// 登录页面转向
	@RequestMapping(value = "/login")
	public ModelAndView RedirectView() {
		return new ModelAndView("login");
	}

	// 超时页面转向
	@RequestMapping(value = "/lockpage")
	public ModelAndView RedirectViewlockpage() {
		return new ModelAndView("lockpage");
	}

	// 查询门店页面
	@RequestMapping(value = "/queryStoreList")
	public ModelAndView RedirectViewQueryStore() {
		return new ModelAndView("queryStoreList");
	}

	// 查询分类页面
	@RequestMapping(value = "/queryCategoryList")
	public ModelAndView RedirectViewQueryCategory() {
		return new ModelAndView("queryCategoryList");
	}

	// 登录页面转向
	@SuppressWarnings("unused")
	@RequestMapping(value = "/loginhand")
	public ModelAndView RedirectViewLogin(HttpServletRequest request, ModelMap model) {

		String companyCode = request.getParameter("companyCode");
		String brandCode = request.getParameter("brandCode");
		String userCode = request.getParameter("userCode");

		List<UserModel> userModelList = new ArrayList<UserModel>();

		Map<String, String> map = new HashMap<String, String>();
		map.put(new String("username"), userCode);
		map.put(new String("password"), userCode);
		map.put(new String("companyCode"), companyCode);

		UserModel userModel = userService.getLoginCheck(map);
		if (userModel != null) {

			model.addAttribute("currentUser", userModel); // ②向ModelMap中添加一个属性

			userModelList.add(userModel);

			return new ModelAndView("/store/reppaymode");
		} else {
			logger.info("no");
			return new ModelAndView("login");
		}

	}

	// 用户登录验证
	@RequestMapping(value = "/handleLogin")
	public @ResponseBody List<UserModel> HandleLogin(HttpServletRequest request, ModelMap model) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String companyCode = request.getParameter("companycode");
		// 存储一个机构
		// String companyCode = "e67ed104-fb63-4d1d-a4ff-63aaef75112d";
		// 验证用户登录
		List<UserModel> userModelList = new ArrayList<UserModel>();

		Map<String, String> map = new HashMap<String, String>();
		map.put(new String("username"), username);
		map.put(new String("password"), password);
		map.put(new String("companyCode"), companyCode);

		UserModel userModel = userService.getLoginCheck(map);
		if (userModel != null) {

			logger.info(username + "登录成功");
			logger.info(userModel.getRoleList());
			model.addAttribute("currentUser", userModel); // ②向ModelMap中添加一个属性
			userModelList.add(userModel);
		} else {
			logger.info("no");
		}
		return userModelList;
	}

}
