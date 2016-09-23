package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.dev.core.dao.SystemMainDao;
import com.dev.core.model.UserModel;
import com.dev.core.service.SystemMainService;

@Service
@SuppressWarnings("rawtypes")
public class SystemMainServiceImpl implements SystemMainService {

	@Autowired
	SystemMainDao mainDao;

	
	public List getMenuFunctionList(UserModel userModel) {
		return mainDao.getMenuFunctionList(userModel);
	}

	/*
	 * ���ز˵���ť��Ϣ
	 */
	
	public List getMenuFunctionButtonList(Map<String, String> map) {
		return mainDao.getMenuFunctionButtonList(map);
	}
}
