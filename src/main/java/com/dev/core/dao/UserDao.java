package com.dev.core.dao;


import java.util.Map;

import com.dev.core.model.UserModel;

public interface UserDao {

	//�û���¼��֤
	public UserModel getLoginCheck(Map<String, String> map);
	
	
}
