package com.dev.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMessage {

	public static Map<String, List<Object>> ResultMess(String strStatus, String strRetMess) {
		
		Map<String, List<Object>> resultMapList =  new HashMap<String, List<Object>>();
		List<Object> ResultMessageList = new ArrayList<Object>();
		Map<String, String> map = new HashMap<String, String>();
		map.put(new String("Status"), strStatus);
		map.put(new String("retMess"), strRetMess);
		ResultMessageList.add(map);
		resultMapList.put(new String("ResultStatus"), ResultMessageList);
		return resultMapList;
	}

}
