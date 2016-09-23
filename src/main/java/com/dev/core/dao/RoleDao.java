package com.dev.core.dao;

import java.util.List;
import java.util.Map;

public interface RoleDao {

	/*
	 * ======查询角色权限==================================================
	 */
	public Map<String, List<Object>> querySystemRoleData(Map<String, String> map);

	/*
	 * ======获取功能菜单及菜单按钮信息========================================
	 */
	public Map<String, List<Object>> getFunButTreeData(Map<String, String> map);

	/*
	 * ======保存角色权限==================================================
	 */
	public Map<String, List<Object>> saveRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm);

	/*
	 * ======删除角色权限==================================================
	 */
	public Map<String, List<Object>> delRoleData(Map<String, String> mapPrm);

	/*
	 * ======查询用户列表==================================================
	 */
	public Map<String, List<Object>> queryEmployeeData(Map<String, String> mapPrm);

	/*
	 * ======查询用户信息==================================================
	 */
	public Map<String, List<Object>> queryEmployeeCodeData(Map<String, String> mapPrm);
	
	/*
	 * ======保存用户角色权限================================================
	 */
	public Map<String, List<Object>> saveEmployeeRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm);

}
