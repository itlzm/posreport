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
	
	//��ҵ��־����
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map) {
		return personDao.queryRepjobDetail(map);
	}
	//��ҵ��־ͳ�Ʒ���
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map) {
		return personDao.queryRepjobStatistics(map);
	}
}
