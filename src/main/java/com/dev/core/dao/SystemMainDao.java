package com.dev.core.dao;

import java.util.List;
import java.util.Map;

import com.dev.core.model.UserModel;

@SuppressWarnings("rawtypes")
public interface SystemMainDao {

	/*
	 * ���ز˵��б���Ϣ
	 */
	public List getMenuFunctionList(UserModel userModel);

	/*
	 * ���ز˵���ť��Ϣ
	 */
	public List getMenuFunctionButtonList(Map<String, String> map);

}
