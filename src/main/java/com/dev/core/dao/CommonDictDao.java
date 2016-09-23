package com.dev.core.dao;

import java.util.List;
import java.util.Map;

/*
 * 公共字典Dao接口
 * */
public interface CommonDictDao {
	
	/*
	 * 返回下拉字典数据
	 * */
	@SuppressWarnings("rawtypes")
	public List getDropDownDictList(Map<String, String> map);
	
	
}
