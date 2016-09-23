package com.dev.core.dao;

import java.util.List;
import java.util.Map;

public interface RoleDao {

	/*
	 * ======��ѯ��ɫȨ��==================================================
	 */
	public Map<String, List<Object>> querySystemRoleData(Map<String, String> map);

	/*
	 * ======��ȡ���ܲ˵����˵���ť��Ϣ========================================
	 */
	public Map<String, List<Object>> getFunButTreeData(Map<String, String> map);

	/*
	 * ======�����ɫȨ��==================================================
	 */
	public Map<String, List<Object>> saveRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm);

	/*
	 * ======ɾ����ɫȨ��==================================================
	 */
	public Map<String, List<Object>> delRoleData(Map<String, String> mapPrm);

	/*
	 * ======��ѯ�û��б�==================================================
	 */
	public Map<String, List<Object>> queryEmployeeData(Map<String, String> mapPrm);

	/*
	 * ======��ѯ�û���Ϣ==================================================
	 */
	public Map<String, List<Object>> queryEmployeeCodeData(Map<String, String> mapPrm);
	
	/*
	 * ======�����û���ɫȨ��================================================
	 */
	public Map<String, List<Object>> saveEmployeeRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm);

}
