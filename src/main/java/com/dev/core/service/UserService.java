package com.dev.core.service;


import java.util.Map;

import com.dev.core.model.UserModel;

public interface UserService {

	// �û���¼��֤
	public UserModel getLoginCheck(Map<String, String> map);

}
