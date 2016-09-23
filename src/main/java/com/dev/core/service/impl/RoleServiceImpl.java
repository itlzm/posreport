package com.dev.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.core.dao.RoleDao;
import com.dev.core.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;

	
	public Map<String, List<Object>> querySystemRoleData(Map<String, String> map) {
		// TODO Auto-generated method stub
		return roleDao.querySystemRoleData(map);
	}

	/*
	 * ======��ȡ���ܲ˵����˵���ť��Ϣ========================================
	 */
	
	public Map<String, List<Object>> getFunButTreeData(Map<String, String> map) {
		return roleDao.getFunButTreeData(map);
	}

	/*
	 * ======�����ɫȨ��==================================================
	 */
	
	public Map<String, List<Object>> saveRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm) {
		return roleDao.saveRoleData(mapData, mapPrm);
	}

	/*
	 * ======ɾ����ɫȨ��==================================================
	 */
	
	public Map<String, List<Object>> delRoleData(Map<String, String> mapPrm) {
		return roleDao.delRoleData(mapPrm);
	}

	/*
	 * ======��ѯ�û��б�==================================================
	 */
	
	public Map<String, List<Object>> queryEmployeeData(Map<String, String> mapPrm) {
		return roleDao.queryEmployeeData(mapPrm);
	}

	/*
	 * ======��ѯ�û���Ϣ==================================================
	 */
	
	public Map<String, List<Object>> queryEmployeeCodeData(Map<String, String> mapPrm){
		return roleDao.queryEmployeeCodeData(mapPrm);
	}

	
	/*
	 * ======�����û���ɫȨ��================================================
	 */
	
	public Map<String, List<Object>> saveEmployeeRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm){
		return roleDao.saveEmployeeRoleData(mapData,mapPrm);
	}

	
}
