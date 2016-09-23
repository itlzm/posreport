package com.dev.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.dev.core.model.SystemMainModel;
import com.dev.core.model.UserModel;
import com.dev.core.service.SystemMainService;

/*
 * 处理业务逻辑控制器
 * */
@Controller
@RequestMapping(value = "/api")
@SessionAttributes("currentUser")
public class SystemMainController {

	// 记录日志信息
	@SuppressWarnings({ "unused" })
	private static Logger logger = Logger.getLogger(SystemMainController.class);

	@Autowired
	SystemMainService systemMainService;

	@RequestMapping(value = "/menu")
	@SuppressWarnings({ "unchecked" })
	public @ResponseBody List<SystemMainModel> getMenuFunctionList(HttpSession session) {

		// 获取登录成功的用户信息;
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		List<SystemMainModel> mainModelList = systemMainService.getMenuFunctionList(userModel);
		return mainModelList;
	}

	@RequestMapping(value = "/menubutton")
	@SuppressWarnings({ "unchecked" })
	public @ResponseBody Map<String, List<Object>> getMenuFunctionButtonList(HttpServletRequest request,
			HttpSession session) {

		// 获取登录成功的用户信息;
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		String companyCode = userModel.getCompanyCode();
		String roleID = userModel.getRoleList();
		String funcID = request.getParameter("funcID");

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("companyCode"), companyCode);
		mappam.put(new String("roleID"), roleID);
		mappam.put(new String("funcID"), funcID);

		@SuppressWarnings("rawtypes")
		List queryList = systemMainService.getMenuFunctionButtonList(mappam);

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		List<Object> userModelList = new ArrayList<Object>();

		userModelList.add(userModel);

		map.put(new String("listData"), queryList);
		map.put(new String("userList"), userModelList);

		return map;
	}
}
