package com.dev.core.dao;

import java.util.List;
import java.util.Map;

public interface PersonDao {
	
	//作业日志分析
	public Map<String, List<Object>> queryRepjobDetail(Map<String, String> map);
	//作业日志统计分析
	public Map<String, List<Object>> queryRepjobStatistics(Map<String, String> map);
}
