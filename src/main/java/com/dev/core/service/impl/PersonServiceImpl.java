package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.PersonDao;
import com.dev.core.service.PersonService;
@Service
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	PersonDao personDao;
	
	//作业日志分析
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map) {
		return personDao.queryRepjobDetail(map);
	}
	//作业日志统计分析
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map) {
		return personDao.queryRepjobStatistics(map);
	}
}
