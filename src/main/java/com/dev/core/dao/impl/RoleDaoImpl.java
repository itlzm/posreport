package com.dev.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dev.core.dao.RoleDao;
import com.dev.core.model.RoleModel;
import com.dev.core.util.JdbcHelper;
import com.dev.core.util.PageModel;
import com.dev.core.util.Pagination;
import com.dev.core.util.ResultMessage;

@Repository
public class RoleDaoImpl implements RoleDao {

	private static Logger logger = Logger.getLogger(RoleDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	/*
	 * ======��ѯ��ɫȨ��========================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> querySystemRoleData(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String businessDateStart = map.get("businessDateStart");
		String businessDateEnd = map.get("businessDateEnd");
		String StoreName = map.get("storeCode");
		int PageIndex = Integer.parseInt(map.get("PageIndex"));
		int PageSize = Integer.parseInt(map.get("PageSize"));
		String Paymentkey = map.get("Paymentkey");

		String strSelect = "";
		strSelect += " IFNULL(CONCAT(roleID,'|=|',roleName),'') AS '��ɫ����'";

		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND roleStatus='1'";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel = new PageModel();

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "rep_role");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> dataList = JdbcHelper.getQueryData(mappam, jdbcTemplate, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);

		if (dataList.size() == 0) {

			Map<String, String> mappamEmpty = new HashMap<String, String>();
			mappamEmpty.put(new String("��ɫ����"), "");
			dataList.add(0, mappamEmpty);
		}

		queryDataMap.put(new String("listData"), dataList);
		queryDataMap.put(new String("pageData"), pageModelList);

		return queryDataMap;
	}

	/*
	 * ======��ȡ���ܲ˵����˵���ť��Ϣ===============================================
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, List<Object>> getFunButTreeData(Map<String, String> map) {

		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = map.get("companyCode");
		String roleID = map.get("roleID");
		String isEdit = map.get("isEdit");

		String strSelect = "";
		strSelect += " * ";

		String sqlWhere = "";
		if (isEdit.equals("Y")) {
			sqlWhere += "  WHERE ISCHECK = 'Y'";
		}
		sqlWhere += " ORDER BY PID,SORTORDER";

		// logger.info("companyCode:"+companyCode+",roleID:"+roleID+",isEdit:"+isEdit);

		String strFrom = "";
		strFrom += " ( ";
		strFrom += " SELECT A.FuncID AS ID,A.TitleText AS NAME,A.PID,A.ISLAST AS FLAG,";
		strFrom += " CASE A.ISLAST WHEN 'Y' THEN 4 ELSE 2 END AS PICIDX,";
		strFrom += " CASE A.ISLAST WHEN 'Y' THEN 4 ELSE 3 END AS SELPICIDX,";
		strFrom += " A.CLASSLEVEL AS CLASS,'N' AS ISLAST,";
		strFrom += " fn_ZsysChkRoleFuncRight('" + companyCode + "','" + roleID + "',A.FuncID) AS ISCHECK,A.SORTORDER";
		strFrom += " FROM rep_function as A";
		strFrom += " WHERE IsVisible = 'Y'";
		strFrom += " UNION ALL";
		strFrom += " SELECT B.ButtonID,B.DisplayText,B.FuncID,'Y' AS FLAG,4 AS PICIDX,4 AS SELPICIDX,4 AS CLASS,'Y' AS ISLAST,";
		strFrom += " fn_ZsysChkRoleFuncButtonRight('" + companyCode + "','" + roleID
				+ "',B.FUNCID,B.ButtonID) AS ISCHECK,B.SORTORDER";
		strFrom += " FROM rep_function_button AS B";
		strFrom += " WHERE B.ISVISIBLE = 'Y'";
		strFrom += " AND B.FuncID IN (SELECT FuncID FROM rep_function WHERE IsVisible = 'Y')";

		strFrom += " )AS M ";
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel = new PageModel();

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), strFrom);
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), "1");
		mappam.put(new String("pageSize"), "1000");

		List<Object> dataList = JdbcHelper.getQueryData(mappam, jdbcTemplate, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);

		/*
		 * ��ѯ��ɫ��Ϣ
		 */
		strSelect = "";
		strSelect += "*";

		sqlWhere = "";
		sqlWhere += " WHERE roleID='" + roleID + "'";
		sqlWhere += " AND companyCode='" + companyCode + "'";

		Map<String, String> mappamRole = new HashMap<String, String>();
		mappamRole.put(new String("selectCol"), strSelect);
		mappamRole.put(new String("tabName"), " rep_role");
		mappamRole.put(new String("sqlWhere"), sqlWhere);
		mappamRole.put(new String("pageIndex"), "1");
		mappamRole.put(new String("pageSize"), "10");

		List<Object> roleData = JdbcHelper.getQueryData(mappamRole, jdbcTemplate, pageModel);

		strSelect = "";
		strSelect += " * ";

		sqlWhere = "";
		if (isEdit.equals("Y")) {
			sqlWhere += "  WHERE ISCHECK = 'Y'";
		}
		sqlWhere += " ORDER BY PID,AreaNo";

		// logger.info("companyCode:"+companyCode+",roleID:"+roleID+",isEdit:"+isEdit);

		strFrom = "";
		strFrom += " ( ";
		strFrom += " SELECT areaCode AS ID,areaNo AS AreaNo,CASE level_ WHEN '1' THEN (SELECT brandCodeName FROM vrep_dict_brand AS B WHERE B.brandCode = A.brandCode ) ELSE areaName END AS NAME,CASE level_ WHEN '1' THEN '1' ELSE parentCode END AS PID";
		strFrom += " ,level_ AS CLASS,'N' AS ISLAST,delFlag";
		strFrom += " ,fn_CheckRoleArea('" + companyCode + "','" + roleID + "',areaCode) AS ISCHECK";

		strFrom += " FROM master_n_area AS A ";
		strFrom += " WHERE companyCode = '" + companyCode + "' AND delFlag = 0 ";
		strFrom += " UNION ALL";
		strFrom += " SELECT storeCode AS ID,storeNo AS  AreaNo,`name` AS NAME,areaCode AS PID";
		strFrom += " ,'6' AS CLASS,'Y' AS ISLAST,delFlag";
		strFrom += " ,fn_CheckRoleAreaStore('" + companyCode + "','" + roleID + "',areaCode,storeCode) AS ISCHECK ";

		strFrom += " FROM master_n_store";
		strFrom += "  WHERE companyCode = '" + companyCode + "' AND delFlag = 0 ";

		strFrom += " )AS M ";
		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		pageModel = new PageModel();

		Map<String, String> mappamArea = new HashMap<String, String>();
		mappamArea.put(new String("selectCol"), strSelect);
		mappamArea.put(new String("tabName"), strFrom);
		mappamArea.put(new String("sqlWhere"), sqlWhere);
		mappamArea.put(new String("pageIndex"), "1");
		mappamArea.put(new String("pageSize"), "5000");

		List<Object> dataListArea = JdbcHelper.getQueryData(mappamArea, jdbcTemplate, pageModel);

		queryDataMap.put(new String("roleData"), roleData);
		queryDataMap.put(new String("menuData"), dataList);
		queryDataMap.put(new String("areaData"), dataListArea);
		return queryDataMap;

	}

	/*
	 * ======�����ɫȨ��==================================================
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, List<Object>> saveRoleData(Map<String, List<Object>> mapData, Map<String, String> mapPrm) {
		Map<String, List<Object>> resultDataMap = new HashMap<String, List<Object>>();

		// ��ɫȨ������
		Map<String, String> table1Map = (Map<String, String>) mapData.get("Table1").get(0);

		// �˵���
		List<Object> funData = mapData.get("Table2");

		// �˵���ť��
		List<Object> funButData = mapData.get("Table3");

		// ��������
		List<Object> areaData = mapData.get("Table4");

		// �����ŵ��
		List<Object> areaStoreData = mapData.get("Table5");

		String RoleID = table1Map.get("RoleID");
		String RoleName = table1Map.get("RoleName");
		String companyCode = mapPrm.get("companyCode");
		String sql = "";

		// ����RoleID���ж������������޸�
		if (RoleID.equals("")) {

			UUID uuid = UUID.randomUUID();
			// �õ����������ID
			String strUuid = uuid.toString();
			// ת��Ϊ��д
			strUuid = strUuid.toUpperCase();
			// �滻 -
			strUuid = strUuid.replaceAll("-", "");

			// ���ݽ�ɫ���Ʋ�ѯ�Ƿ������ͬ��ɫ
			RoleModel userModel = null;
			try {
				sql = "";
				sql = "SELECT * FROM rep_role WHERE roleName='" + RoleName + "' AND companyCode='" + companyCode + "'";
				userModel = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(RoleModel.class));
			} catch (Exception ex) {
				userModel = null;
			}
			if (userModel != null) {
				// ������ͬ��;
				resultDataMap = ResultMessage.ResultMess("N", "��ɫȨ��[" + RoleName + "]�Ѿ�����,��������������");
			} else {

				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				TransactionStatus status = transactionManager.getTransaction(def);

				try {

					// ��ɫȨ������
					sql = "INSERT INTO rep_role(roleID,roleName,companyCode,createDate,modifyDate,roleStatus)";
					sql += "VALUES('" + strUuid + "','" + RoleName + "','" + companyCode + "',NOW(),NOW(),1)";

					jdbcTemplate.update(sql);

					// ��ɫȨ�޲˵�
					for (int i = 0; i < funData.size(); i++) {

						Map<String, String> funMap = (Map<String, String>) funData.get(i);
						sql = "INSERT INTO rep_role_function(companyCode,roleID,FuncID)";
						sql += "VALUES('" + companyCode + "','" + strUuid + "','" + funMap.get("FuncID") + "')";
						jdbcTemplate.update(sql);
					}

					// ��ɫȨ�޲˵���ť
					for (int i = 0; i < funButData.size(); i++) {

						Map<String, String> funButMap = (Map<String, String>) funButData.get(i);
						sql = "INSERT INTO rep_role_function_button(companyCode,roleID,FuncID,ButtonID)";
						sql += "VALUES('" + companyCode + "','" + strUuid + "','" + funButMap.get("FuncID") + "','"
								+ funButMap.get("ButtonID") + "')";
						jdbcTemplate.update(sql);
					}

					// ����Ȩ�ޱ�
					for (int i = 0; i < areaData.size(); i++) {

						Map<String, String> areaMap = (Map<String, String>) areaData.get(i);
						sql = "INSERT INTO rep_role_area(companyCode,roleID,areaCode)";
						sql += "VALUES('" + companyCode + "','" + strUuid + "','" + areaMap.get("areaCode") + "')";
						jdbcTemplate.update(sql);
					}

					// �����ŵ�Ȩ�ޱ�
					for (int i = 0; i < areaStoreData.size(); i++) {

						Map<String, String> areaStoreMap = (Map<String, String>) areaStoreData.get(i);
						sql = "INSERT INTO rep_role_area_store(companyCode,roleID,areaCode,storeCode)";
						sql += "VALUES('" + companyCode + "','" + strUuid + "','" + areaStoreMap.get("areaCode") + "','"
								+ areaStoreMap.get("storeCode") + "')";
						jdbcTemplate.update(sql);
					}

					transactionManager.commit(status);
					resultDataMap = ResultMessage.ResultMess("Y", "��ɫȨ�ޱ���ɹ�!");

				} catch (DataAccessException ex) {
					transactionManager.rollback(status); // Ҳ���Ԉ���status.setRollbackOnly();
					resultDataMap = ResultMessage.ResultMess("N", "����ʧ��!");
				}
			}

		} else {
			// �޸�Ȩ��

			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(def);

			try {

				// ��ɫȨ������
				sql = "UPDATE rep_role SET roleName='" + RoleName + "',modifyDate=NOW() ";
				sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";

				jdbcTemplate.update(sql);

				// ��ɫȨ�޲˵�
				sql = "DELETE FROM rep_role_function ";
				sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
				jdbcTemplate.update(sql);

				for (int i = 0; i < funData.size(); i++) {

					Map<String, String> funMap = (Map<String, String>) funData.get(i);
					sql = "INSERT INTO rep_role_function(companyCode,roleID,FuncID)";
					sql += "VALUES('" + companyCode + "','" + RoleID + "','" + funMap.get("FuncID") + "')";
					jdbcTemplate.update(sql);
				}

				// ��ɫȨ�޲˵���ť
				sql = "DELETE FROM rep_role_function_button ";
				sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
				jdbcTemplate.update(sql);
				for (int i = 0; i < funButData.size(); i++) {

					Map<String, String> funButMap = (Map<String, String>) funButData.get(i);
					sql = "INSERT INTO rep_role_function_button(companyCode,roleID,FuncID,ButtonID)";
					sql += "VALUES('" + companyCode + "','" + RoleID + "','" + funButMap.get("FuncID") + "','"
							+ funButMap.get("ButtonID") + "')";
					jdbcTemplate.update(sql);
				}

				// ����Ȩ�ޱ�
				sql = "DELETE FROM rep_role_area ";
				sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
				jdbcTemplate.update(sql);
				for (int i = 0; i < areaData.size(); i++) {

					Map<String, String> areaMap = (Map<String, String>) areaData.get(i);
					sql = "INSERT INTO rep_role_area(companyCode,roleID,areaCode)";
					sql += "VALUES('" + companyCode + "','" + RoleID + "','" + areaMap.get("areaCode") + "')";
					jdbcTemplate.update(sql);
				}

				// �����ŵ�Ȩ�ޱ�
				sql = "DELETE FROM rep_role_area_store ";
				sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
				jdbcTemplate.update(sql);
				for (int i = 0; i < areaStoreData.size(); i++) {

					Map<String, String> areaStoreMap = (Map<String, String>) areaStoreData.get(i);
					sql = "INSERT INTO rep_role_area_store(companyCode,roleID,areaCode,storeCode)";
					sql += "VALUES('" + companyCode + "','" + RoleID + "','" + areaStoreMap.get("areaCode") + "','"
							+ areaStoreMap.get("storeCode") + "')";
					jdbcTemplate.update(sql);
				}

				transactionManager.commit(status);
				resultDataMap = ResultMessage.ResultMess("Y", "��ɫȨ�ޱ���ɹ�!");

			} catch (DataAccessException ex) {
				transactionManager.rollback(status); // Ҳ���Ԉ���status.setRollbackOnly();
				resultDataMap = ResultMessage.ResultMess("N", "����ʧ��!");
			}

		}
		return resultDataMap;
	}

	/*
	 * ======ɾ����ɫȨ��==================================================
	 */
	
	public Map<String, List<Object>> delRoleData(Map<String, String> mapPrm) {
		Map<String, List<Object>> resultDataMap = new HashMap<String, List<Object>>();

		String RoleID = mapPrm.get("roleID");
		String companyCode = mapPrm.get("companyCode");
		String sql = "";

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			// ��ɫȨ������
			sql = "DELETE FROM  rep_role ";
			sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
			jdbcTemplate.update(sql);

			// ��ɫȨ�޲˵�
			sql = "DELETE FROM rep_role_function ";
			sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
			jdbcTemplate.update(sql);

			// ��ɫȨ�޲˵���ť
			sql = "DELETE FROM rep_role_function_button ";
			sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
			jdbcTemplate.update(sql);

			// ����Ȩ�ޱ�
			sql = "DELETE FROM rep_role_area ";
			sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
			jdbcTemplate.update(sql);

			// �����ŵ�Ȩ�ޱ�
			sql = "DELETE FROM rep_role_area_store ";
			sql += " WHERE roleID='" + RoleID + "' AND companyCode = '" + companyCode + "'";
			jdbcTemplate.update(sql);

			transactionManager.commit(status);
			resultDataMap = ResultMessage.ResultMess("Y", "��ɫȨ��ɾ���ɹ�!");

		} catch (DataAccessException ex) {
			transactionManager.rollback(status); // Ҳ���Ԉ���status.setRollbackOnly();
			resultDataMap = ResultMessage.ResultMess("N", "ɾ��ʧ��!");
		}

		return resultDataMap;
	}

	/*
	 * ======��ѯ�û��б�==================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> queryEmployeeData(Map<String, String> mapPrm) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();

		String companyCode = mapPrm.get("companyCode");
		String businessDateStart = mapPrm.get("businessDateStart");
		String businessDateEnd = mapPrm.get("businessDateEnd");
		String StoreName = mapPrm.get("storeCode");
		int PageIndex = Integer.parseInt(mapPrm.get("PageIndex"));
		int PageSize = Integer.parseInt(mapPrm.get("PageSize"));
		String Paymentkey = mapPrm.get("Paymentkey");
		String strSelect = "";
		strSelect += " IFNULL(CONCAT(employeeCode,'|=|',employNo),'') AS '�û�����'";
		strSelect += " ,employName AS '�û�����'";

		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";

		/*
		 * ��ѯ��ϸ�����б� ����ҳ
		 */
		PageModel pageModel = new PageModel();

		Map<String, String> mappam = new HashMap<String, String>();
		mappam.put(new String("selectCol"), strSelect);
		mappam.put(new String("tabName"), "vrep_dict_employee");
		mappam.put(new String("sqlWhere"), sqlWhere);
		mappam.put(new String("pageIndex"), String.valueOf(PageIndex));
		mappam.put(new String("pageSize"), String.valueOf(PageSize));

		List<Object> dataList = JdbcHelper.getQueryData(mappam, jdbcTemplate, pageModel);
		logger.info("PageModel:" + pageModel.getTotalPages() + "," + pageModel.getTotalRows());

		List<Object> pageModelList = new ArrayList<Object>();
		pageModelList.add(pageModel);

		if (dataList.size() == 0) {

			Map<String, String> mappamEmpty = new HashMap<String, String>();
			mappamEmpty.put(new String("�û�����"), "");
			mappamEmpty.put(new String("�û�����"), "");
			dataList.add(0, mappamEmpty);
		}

		queryDataMap.put(new String("listData"), dataList);
		queryDataMap.put(new String("pageData"), pageModelList);

		return queryDataMap;
	}

	/*
	 * ======��ѯ�û���Ϣ==================================================
	 */
	@SuppressWarnings({"unchecked" })
	public Map<String, List<Object>> queryEmployeeCodeData(Map<String, String> mapPrm) {
		Map<String, List<Object>> queryDataMap = new HashMap<String, List<Object>>();
		String companyCode = mapPrm.get("companyCode");
		String employeeCode = mapPrm.get("employeeCode");

		String strSql = "";
		String totalSQL = "";

		strSql += "SELECT employeeCode,employNo,employName FROM vrep_dict_employee ";
		strSql += " WHERE companyCode = '" + companyCode + "'";
		strSql += " AND employeeCode = '" + employeeCode + "'";

		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		PageModel listDataPage = new PageModel();
		Pagination page = new Pagination(strSql, 1, 1, jdbcTemplate, totalSQL, listDataPage);
		List<Object> listData = page.getResultList();

		// ��ѯ�û���Ӧ��ɫȨ��
		strSql = "";
		strSql += "SELECT roleID,employeeCode FROM rep_role_employee";
		strSql += " WHERE employeeCode = '" + employeeCode + "'";
		totalSQL = "select count(1) from (" + strSql + ") as Tab";

		listDataPage = new PageModel();
		page = new Pagination(strSql, 1, 100, jdbcTemplate, totalSQL, listDataPage);
		List<Object> roleData = page.getResultList();
		queryDataMap.put(new String("oneData"), listData);
		queryDataMap.put(new String("roleData"), roleData);

		return queryDataMap;
	}

	/*
	 * ======�����û���ɫȨ��================================================
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, List<Object>> saveEmployeeRoleData(Map<String, List<Object>> mapData,
			Map<String, String> mapPrm) {
		Map<String, List<Object>> resultDataMap = new HashMap<String, List<Object>>();

		// ��ɫȨ������
		Map<String, String> table1Map = (Map<String, String>) mapData.get("Table1").get(0);

		String employeeCode = table1Map.get("employeeCode");
		String roleID = table1Map.get("roleID");
		String companyCode = mapPrm.get("companyCode");
		String sql = "";

		String[] roleIDAry = roleID.split(",");

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			// ��ɫȨ�޲˵�
			sql = "DELETE FROM rep_role_employee ";
			sql += " WHERE employeeCode='" + employeeCode + "'";
			jdbcTemplate.update(sql);

			for (int i = 0; i < roleIDAry.length; i++) {
				// ��ɫȨ������
				sql = "INSERT INTO rep_role_employee(roleID,employeeCode) VALUES('" + roleIDAry[i] + "','"
						+ employeeCode + "') ";
				jdbcTemplate.update(sql);
			}

			transactionManager.commit(status);
			resultDataMap = ResultMessage.ResultMess("Y", "�û���ɫȨ�����óɹ�!");

		} catch (DataAccessException ex) {
			transactionManager.rollback(status); // Ҳ���Ԉ���status.setRollbackOnly();
			resultDataMap = ResultMessage.ResultMess("N", "�û���ɫȨ������ʧ��!");
		}

		return resultDataMap;
	}
}
