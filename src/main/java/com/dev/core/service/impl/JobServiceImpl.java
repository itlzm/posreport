package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.JobDao;
import com.dev.core.service.JobService;
@Service
public class JobServiceImpl implements JobService {
	
	@Autowired
	JobDao jobDao;
	
	public Map<String, List<Object>> queryJobDetail(Map<String, String> map) {
		return jobDao.queryJobDetail(map);
	}
	public Map<String, List<Object>> queryPaymentKey(Map<String, String> map) {
		return jobDao.queryPaymentKey(map);
	}
	public Map<String, List<Object>> queryRepjobstatistics(Map<String, String> map) {
		return jobDao.queryRepjobstatistics(map);
	}
	public Map<String, List<Object>> queryDownGo(Map<String, String> map) {
		return jobDao.queryDownGo(map);
	}

}
