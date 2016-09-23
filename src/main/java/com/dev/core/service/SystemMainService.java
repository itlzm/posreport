package com.dev.core.service;

import java.util.List;
import java.util.Map;

import com.dev.core.model.UserModel;
@SuppressWarnings("rawtypes")
public interface SystemMainService {

	/*
	 * 返回菜单列表信息
	 * */
	public List getMenuFunctionList(UserModel userModel);
	
	
	/*
	 * 返回菜单按钮信息
	 */
	public List getMenuFunctionButtonList(Map<String, String> map);
	
}
