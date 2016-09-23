package com.dev.core.dao;

import java.util.List;
import java.util.Map;

public interface JobDao {
	
	public Map<String, List<Object>> queryJobDetail(Map<String, String> map);
	public Map<String, List<Object>> queryPaymentKey(Map<String, String> map);
	public Map<String, List<Object>> queryRepjobstatistics(Map<String, String> map);
	public Map<String, List<Object>> queryDownGo(Map<String, String> map);
}
