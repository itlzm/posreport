package com.dev.core.dao;


import java.util.Map;

import com.dev.core.model.UserModel;

public interface UserDao {

	//用户登录验证
	public UserModel getLoginCheck(Map<String, String> map);
	
	
}
