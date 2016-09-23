package com.dev.core.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.UserDao;
import com.dev.core.model.UserModel;
import com.dev.core.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	// 登录用户验证;
	
	public UserModel getLoginCheck(Map<String, String> map) {
		return userDao.getLoginCheck(map);
	}

}
