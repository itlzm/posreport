package com.dev.core.dao;

import java.util.List;
import java.util.Map;

public interface PersonDao {
	
	//��ҵ��־����
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map);
	//��ҵ��־ͳ�Ʒ���
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map);
}
