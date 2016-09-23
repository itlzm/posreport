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
	 * ======获取功能菜单及菜单按钮信息========================================
	 */
	
	public Map<String, List<Object>> getFunButTreeData(Map<String, String> map) {
		return roleDao.getFunButTreeData(map);
	}

	/*
	 * ======保存角色权限==================================================
	 */
	
	public Map<String, List<Object>> saveRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm) {
		return roleDao.saveRoleData(mapData, mapPrm);
	}

	/*
	 * ======删除角色权限==================================================
	 */
	
	public Map<String, List<Object>> delRoleData(Map<String, String> mapPrm) {
		return roleDao.delRoleData(mapPrm);
	}

	/*
	 * ======查询用户列表==================================================
	 */
	
	public Map<String, List<Object>> queryEmployeeData(Map<String, String> mapPrm) {
		return roleDao.queryEmployeeData(mapPrm);
	}

	/*
	 * ======查询用户信息==================================================
	 */
	
	public Map<String, List<Object>> queryEmployeeCodeData(Map<String, String> mapPrm){
		return roleDao.queryEmployeeCodeData(mapPrm);
	}

	
	/*
	 * ======保存用户角色权限================================================
	 */
	
	public Map<String, List<Object>> saveEmployeeRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm){
		return roleDao.saveEmployeeRoleData(mapData,mapPrm);
	}

	
}
