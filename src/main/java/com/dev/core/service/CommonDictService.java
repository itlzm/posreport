package com.dev.core.service;

import java.util.List;
import java.util.Map;

/*
 * 公共字典Service接口
 * */
@SuppressWarnings("rawtypes")
public interface CommonDictService {


	/*
	 * 返回下拉字典数据
	 * */
	public List getDropDownDictList(Map<String, String> map);
	
}
