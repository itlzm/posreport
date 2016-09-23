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
 * ��¼������
 * */
@Controller
@SessionAttributes("currentUser")
public class LoginController {

	// ע���û�����
	@Autowired
	UserService userService;

	@Autowired
	StoreService storeService;

	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(LoginController.class);

	// ��¼ҳ��ת��
	@RequestMapping(value = "/login")
	public ModelAndView RedirectView() {
		return new ModelAndView("login");
	}

	// ��ʱҳ��ת��
	@RequestMapping(value = "/lockpage")
	public ModelAndView RedirectViewlockpage() {
		return new ModelAndView("lockpage");
	}

	// ��ѯ�ŵ�ҳ��
	@RequestMapping(value = "/queryStoreList")
	public ModelAndView RedirectViewQueryStore() {
		return new ModelAndView("queryStoreList");
	}

	// ��ѯ����ҳ��
	@RequestMapping(value = "/queryCategoryList")
	public ModelAndView RedirectViewQueryCategory() {
		return new ModelAndView("queryCategoryList");
	}

	// ��¼ҳ��ת��
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

			model.addAttribute("currentUser", userModel); // ����ModelMap�����һ������

			userModelList.add(userModel);

			return new ModelAndView("/store/reppaymode");
		} else {
			logger.info("no");
			return new ModelAndView("login");
		}

	}

	// �û���¼��֤
	@RequestMapping(value = "/handleLogin")
	public @ResponseBody List<UserModel> HandleLogin(HttpServletRequest request, ModelMap model) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String companyCode = request.getParameter("companycode");
		// �洢һ������
		// String companyCode = "e67ed104-fb63-4d1d-a4ff-63aaef75112d";
		// ��֤�û���¼
		List<UserModel> userModelList = new ArrayList<UserModel>();

		Map<String, String> map = new HashMap<String, String>();
		map.put(new String("username"), username);
		map.put(new String("password"), password);
		map.put(new String("companyCode"), companyCode);

		UserModel userModel = userService.getLoginCheck(map);
		if (userModel != null) {

			logger.info(username + "��¼�ɹ�");
			logger.info(userModel.getRoleList());
			model.addAttribute("currentUser", userModel); // ����ModelMap�����һ������
			userModelList.add(userModel);
		} else {
			logger.info("no");
		}
		return userModelList;
	}

}
