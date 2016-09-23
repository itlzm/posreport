package com.dev.core.service;

import java.util.List;
import java.util.Map;

public interface JobService {
	
	public Map<String, List<Object>> queryJobDetail(Map<String, String> map);
	public Map<String, List<Object>> queryPaymentKey(Map<String, String> map);
	public Map<String, List<Object>> queryRepjobstatistics(Map<String, String> map);
	public Map<String, List<Object>> queryDownGo(Map<String, String> map);
}
