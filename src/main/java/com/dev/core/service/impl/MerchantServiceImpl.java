package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.MerchantDao;
import com.dev.core.service.MerchantService;
@Service
@SuppressWarnings("rawtypes")
public class MerchantServiceImpl implements MerchantService {
	
	@Autowired
	MerchantDao merchantDao;
	public Map<String, List<Object>> queryMerchantDetail(Map<String, String> map) {
		
		return merchantDao.queryMerchantDetail(map);
	}
	public Map<String, List<Object>> queryMerchantDetail2(Map<String, String> map) {
		
		return merchantDao.queryMerchantDetail2(map);
	}

}
