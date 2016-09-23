package com.dev.core.service;


import java.util.Map;

import com.dev.core.model.UserModel;

public interface UserService {

	// 用户登录验证
	public UserModel getLoginCheck(Map<String, String> map);

}
