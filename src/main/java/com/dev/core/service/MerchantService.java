package com.dev.core.service;

import java.util.List;
import java.util.Map;



public interface MerchantService {
	public Map<String, List<Object>> queryMerchantDetail(Map<String, String> map);
	public Map<String, List<Object>> queryMerchantDetail2(Map<String, String> map);
}
