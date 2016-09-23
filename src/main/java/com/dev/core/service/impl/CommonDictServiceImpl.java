package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.CommonDictDao;
import com.dev.core.service.CommonDictService;



/*
 * 公共字典Service接口实现
 * */
@Service
public class CommonDictServiceImpl implements CommonDictService {

	@Autowired
	CommonDictDao commonDictDao;
	
	
	@SuppressWarnings("rawtypes")
	public List getDropDownDictList(Map<String, String> map) {
		return commonDictDao.getDropDownDictList(map);
	}

}
