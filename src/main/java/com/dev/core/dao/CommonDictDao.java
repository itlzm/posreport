package com.dev.core.dao;

import java.util.List;
import java.util.Map;

/*
 * �����ֵ�Dao�ӿ�
 * */
public interface CommonDictDao {
	
	/*
	 * ���������ֵ�����
	 * */
	@SuppressWarnings("rawtypes")
	public List getDropDownDictList(Map<String, String> map);
	
	
}
