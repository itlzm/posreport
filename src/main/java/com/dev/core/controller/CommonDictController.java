package com.dev.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.dev.core.model.UserModel;
import com.dev.core.service.CommonDictService;

/*
 * 公共字典数据控制器
 * */
@Controller
@RequestMapping(value = "/common")
@SessionAttributes("currentUser")
public class CommonDictController {

	// 记录日志信息
	private static Logger logger = Logger.getLogger(CommonDictController.class);

	@Autowired
	CommonDictService commonDictService;

	/*
	 * 品牌字典下拉数据获取
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/brand")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictBrandList(HttpSession session) {

		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		Map<String, String> mapPam = new HashMap<String, String>();

		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_brand");
		mapPam.put(new String("sqlWhere"), "where companyCode='" + userModel.getCompanyCode() + "'");

		// 获取品牌字典
		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> brandList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			String id = dataMap.get("brandCode").toString();
			String text = dataMap.get("brandCodeName").toString();
			Map<String, String> brandNameMap = new HashMap<String, String>();
			brandNameMap.put(new String("id"), id);
			brandNameMap.put(new String("text"), text);
			brandList.add(brandNameMap);
		}
		map.put(new String("brandName"), brandList);
		return map;
	}

	/*
	 * 支付方式下拉数据获取
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value = "/dict/paymentkey")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictPaymentkeyList(HttpSession session) {

		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		Map<String, String> mapPam = new HashMap<String, String>();

		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "rep_dict_paymentkey");
		mapPam.put(new String("sqlWhere"), "");

		// 获取品牌字典
		@SuppressWarnings("unchecked")
		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> paymentkeyList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			String id = dataMap.get("paymentKey").toString();
			String text = dataMap.get("paymentKeyName").toString();
			Map<String, String> paymentkeyMap = new HashMap<String, String>();
			paymentkeyMap.put(new String("id"), id);
			paymentkeyMap.put(new String("text"), text);
			paymentkeyList.add(paymentkeyMap);
		}
		map.put(new String("paymentkey"), paymentkeyList);

		return map;
	}

	/*
	 * 根据品牌获取该品牌下所有渠道 brandCode 品牌主键
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/brand/change/{brandCode}")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictBranChangeList(@PathVariable String brandCode,
			HttpSession session) {

		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		brandCode = brandCode.replace("|", "','");

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), " distinct channelKey,channelName");
		mapPam.put(new String("moduleName"), "vrep_dict_brand_channel");
		mapPam.put(new String("sqlWhere"),
				"Where brandCode in('" + brandCode + "') and companyCode='" + userModel.getCompanyCode() + "'");

		// 品牌下所属渠道
		List<Object> dataListChannel = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> channelList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataListChannel.size(); i++) {
			Map dataMap = (Map) dataListChannel.get(i);
			String id = dataMap.get("channelKey").toString();
			String text = dataMap.get("channelName").toString();
			Map<String, String> channelNameMap = new HashMap<String, String>();
			channelNameMap.put(new String("id"), id);
			channelNameMap.put(new String("text"), text);
			channelList.add(channelNameMap);
		}
		map.put(new String("channelName"), channelList);

		Map<String, String> mapPamStore = new HashMap<String, String>();
		mapPamStore.put(new String("selectCol"), "storeCode,storeCodeName");
		mapPamStore.put(new String("moduleName"), "vrep_dict_store");
		mapPamStore.put(new String("sqlWhere"),
				"Where brandCode in('" + brandCode + "') and companyCode='" + userModel.getCompanyCode() + "'");

		// 品牌下所有门店
		List<Object> dataListStore = commonDictService.getDropDownDictList(mapPamStore);
		List<Map<String, String>> storeList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataListStore.size(); i++) {
			Map dataMap = (Map) dataListStore.get(i);
			String id = dataMap.get("storeCode").toString();
			String text = dataMap.get("storeCodeName").toString();
			Map<String, String> storeMap = new HashMap<String, String>();
			storeMap.put(new String("id"), id);
			storeMap.put(new String("text"), text);
			storeList.add(storeMap);
		}
		map.put(new String("storeName"), storeList);

		return map;
	}

	/*
	 * 根据品牌获取该品牌下所有门店 brandCode 品牌主键
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/dict/brand/getStoreList")
	public @ResponseBody Map<String, List<Object>> getDictBranStoreList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "channelKey,channelName");
		mapPam.put(new String("moduleName"), "vrep_dict_brand_channel");

		// 门店类型
		String strStoreType = request.getParameter("StoreType");

		String companyCode = userModel.getCompanyCode();
		String roleID = userModel.getRoleList();
		roleID = roleID.replace("'", "");
		String strSelect = "";
		strSelect += " * ";

		String sqlWhere = " ORDER BY CLASS";

		String strFrom = "";
		strFrom = "";
		strFrom += " ( ";
		strFrom += " SELECT ";
		strFrom += "		CASE M.level_  ";
		strFrom += "           WHEN '1' THEN (SELECT CONCAT(brandCode,'=',M.areaCode)  FROM vrep_dict_brand AS BB WHERE BB.brandCode = M.brandCode )  ";
		strFrom += "            ELSE M.areaCode END AS ID ";
		strFrom += "       ,M.areaNo AS AreaNo ";
		strFrom += "       ,CASE M.level_  ";
		strFrom += "           WHEN '1' THEN (SELECT CONCAT(brandCode,'=',brandCodeName)  FROM vrep_dict_brand AS BB WHERE BB.brandCode = M.brandCode )  ";
		strFrom += "            ELSE M.areaName END AS NAME ";
		strFrom += "       ,CASE M.level_ WHEN '1' THEN '1' ELSE M.parentCode END AS PID ";
		strFrom += " 		  ,M.level_ AS CLASS ";
		strFrom += "       ,'N' AS ISLAST ";
		strFrom += "   FROM master_n_area AS M, ";
		strFrom += " ( ";
		strFrom += " SELECT companyCode,areaCode  ";
		strFrom += "   FROM rep_role_area AS A,rep_role_employee AS B ";
		strFrom += "  WHERE A.roleID = B.roleID ";
		strFrom += "    AND A.companyCode = '" + userModel.getCompanyCode() + "' ";
		strFrom += "    AND B.employeeCode = '" + userModel.getEmployeeCode() + "' ";
		strFrom += " GROUP BY companyCode,areaCode) AS T ";
		strFrom += " WHERE M.companyCode = T.companyCode ";
		strFrom += "   AND M.areaCode = T.areaCode ";
		strFrom += "   AND M.delFlag = 0 ";
		strFrom += " UNION ALL ";
		strFrom += " SELECT S.storeCode AS ID ";
		strFrom += "       ,S.storeNo AS  AreaNo ";
		strFrom += "       ,S.`name` AS NAME ";
		strFrom += "       ,S.areaCode AS PID  ";
		strFrom += "       ,'999' AS CLASS ";
		strFrom += "       ,'Y' AS ISLAST ";
		strFrom += "   FROM master_n_store AS S, ";
		strFrom += " (SELECT A.companyCode ";
		strFrom += "       ,A.areaCode ";
		strFrom += "       ,A.storeCode ";
		strFrom += "   FROM rep_role_area_store AS A,rep_role_employee AS B ";
		strFrom += "  WHERE A.roleID = B.roleID ";
		strFrom += "    AND A.companyCode = '" + userModel.getCompanyCode() + "' ";
		strFrom += "    AND B.employeeCode = '" + userModel.getEmployeeCode() + "' ";
		strFrom += "  GROUP BY A.companyCode ";
		strFrom += "       ,A.areaCode ";
		strFrom += "       ,A.storeCode) AS RS ";
		strFrom += " WHERE S.companyCode = RS.companyCode ";
		strFrom += "   AND S.storeCode = RS.storeCode ";
		strFrom += "   AND S.delFlag = 0 ";

		if (!strStoreType.equals("ALL") && strStoreType != null) {
			strFrom += "   AND S.storeType = " + strStoreType + " ";
		}

		strFrom += " )AS M ";
		/*
		 * 查询明细数据列表 带分页
		 */

		logger.info(strFrom);

		Map<String, String> mapPamStore = new HashMap<String, String>();
		mapPamStore.put(new String("selectCol"), strSelect);
		mapPamStore.put(new String("moduleName"), strFrom);
		mapPamStore.put(new String("sqlWhere"), sqlWhere);

		List<Object> dataListStore = commonDictService.getDropDownDictList(mapPamStore);

		map.put(new String("storeName"), dataListStore);

		return map;
	}

	/*
	 * 根据品牌获取该品牌下所有产品分类 brandCode 品牌主键
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dict/brand/getCategoryList")
	public @ResponseBody Map<String, List<Object>> getCategoryList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		// 获取选择的品牌ID
		String strSelectBrandCode = request.getParameter("SelectBrandCode");

		String companyCode = userModel.getCompanyCode();

		String strSelect = "";
		strSelect += " * ";

		String sqlWhere = " ORDER BY CLASS";

		String strFrom = "";
		strFrom += " ( ";
		
		strFrom += " SELECT CASE level_  ";
		strFrom += "             WHEN '1' THEN CONCAT(brandCode, '=', productCategoryCode)  ";
		strFrom += "             ELSE productCategoryCode END AS 'ID' ";
		strFrom += "       ,productCategoryNo AS 'CategoryNo' ";
		strFrom += "       ,CASE level_ ";
		strFrom += "             WHEN '1' THEN CONCAT( brandCode, '=', CONCAT(brandCodeName,'-',productCategoryCodeName) )  ";
		strFrom += "             ELSE productCategoryCodeName END AS 'NAME' ";
		strFrom += "       ,CASE level_  ";
		strFrom += "             WHEN '1' THEN '1'  ";
		strFrom += "             ELSE parentCode END AS 'PID' ";
		strFrom += " 			,level_ AS 'CLASS' ";
		strFrom += "       ,fn_CheckCategoryIsLast(companyCode,brandCode,productCategoryCode) AS ISLAST ";
		strFrom += " FROM vrep_dict_master_productcategory ";
		strFrom += " WHERE companyCode = '" + companyCode + "' ";
		
		if (!strSelectBrandCode.equals("ALL") && strSelectBrandCode != null) {
			
			strSelectBrandCode = strSelectBrandCode.replace("|", "','");
			strSelectBrandCode = "'" + strSelectBrandCode + "'";
			strFrom += "   AND brandCode IN (" + strSelectBrandCode + ")";
		}
		strFrom += " ) AS M ";
		/*
		 * 查询明细数据列表 带分页
		 */

		logger.info(strFrom);

		Map<String, String> mapPamCategory = new HashMap<String, String>();
		mapPamCategory.put(new String("selectCol"), strSelect);
		mapPamCategory.put(new String("moduleName"), strFrom);
		mapPamCategory.put(new String("sqlWhere"), sqlWhere);

		List<Object> dataListStore = commonDictService.getDropDownDictList(mapPamCategory);

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		map.put(new String("CategoryName"), dataListStore);

		return map;
	}
	
	
	
	
	/*
	 * 商户公司下拉字典获取
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/companycode")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictCompanyCodeList() {

		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
		// 获取品牌字典

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "vrep_dict_company");
		mapPam.put(new String("sqlWhere"), "");

		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> companyCodeList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			String id = dataMap.get("companyCode").toString();
			String text = dataMap.get("companyCodeName").toString();
			Map<String, String> companyCodeMap = new HashMap<String, String>();
			companyCodeMap.put(new String("id"), id);
			companyCodeMap.put(new String("text"), text);
			companyCodeList.add(companyCodeMap);
		}
		map.put(new String("companyCode"), companyCodeList);

		return map;
	}

	/*
	 * 优惠活动下拉字典获取
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/favourableName")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictFavourableNameList(HttpServletRequest request,
			HttpSession session) {

		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		// 获取品牌字典
		UserModel userModel = (UserModel) session.getAttribute("currentUser");

		String companyCode = userModel.getCompanyCode();

		// 开始时间
		String TimeStart = request.getParameter("TimeStart");
		// 结束时间
		String TimeEnd = request.getParameter("TimeEnd");

		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";
		sqlWhere += " AND (businessDate >= '" + TimeStart + "' AND businessDate <= '" + TimeEnd + "')";

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), " DISTINCT favourableName ");
		mapPam.put(new String("moduleName"), "vrep_sales_daily_store_favourable");
		mapPam.put(new String("sqlWhere"), sqlWhere);

		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			String id = dataMap.get("favourableName").toString();
			String text = dataMap.get("favourableName").toString();
			Map<String, String> codeMap = new HashMap<String, String>();
			codeMap.put(new String("id"), id);
			codeMap.put(new String("text"), text);
			codeList.add(codeMap);
		}
		map.put(new String("favourableName"), codeList);

		return map;
	}

	/*
	 * 角色权限下拉字典获取
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/role")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictRoleList(HttpServletRequest request,
			HttpSession session) {

		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		String companyCode = userModel.getCompanyCode();

		// 获取角色权限
		String sqlWhere = "";
		sqlWhere += " WHERE companyCode = '" + companyCode + "'";

		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("selectCol"), "*");
		mapPam.put(new String("moduleName"), "rep_role");
		mapPam.put(new String("sqlWhere"), sqlWhere);

		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		List<Map<String, String>> companyCodeList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			String id = dataMap.get("roleID").toString();
			String text = dataMap.get("roleName").toString();
			Map<String, String> companyCodeMap = new HashMap<String, String>();
			companyCodeMap.put(new String("id"), id);
			companyCodeMap.put(new String("text"), text);
			companyCodeList.add(companyCodeMap);
		}
		map.put(new String("role"), companyCodeList);

		return map;
	}
	/*
	 * 作业日志步骤下拉字典获取
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/jobStep")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictJobStep(HttpServletRequest request,
			HttpSession session) {
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, String> mapPam = new HashMap<String, String>();

		mapPam.put(new String("selectCol"), "jobStep");
		mapPam.put(new String("moduleName"), "rep_job_log");
		mapPam.put(new String("sqlWhere"), " GROUP BY jobStep");
		//获取sql语句查询的结果集
		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		
		List<Map<String, String>> jobStepList = new ArrayList<Map<String, String>>();
		//遍历结果集，取出JobStep的值
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			//下拉控件显示text值下面绑定的id值，选中每一个text值，实质是选中id值
			int id = (Integer) dataMap.get("jobStep");
			int text = (Integer) dataMap.get("jobStep");
			Map<String, String> jobStepMap = new HashMap<String, String>();
			jobStepMap.put(new String("id"), String.valueOf(id));
			jobStepMap.put(new String("text"), String.valueOf(text));
			jobStepList.add(jobStepMap);
		}
		map.put(new String("jobStep"), jobStepList);
		return map;
	}
	/*
	 * 支付方式名称下拉字典获取：线下支付，线上支付，线下支付方式
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dict/paymentTypeName")
	public @ResponseBody Map<String, List<Map<String, String>>> getDictPaymentTypeName(HttpServletRequest request,
			HttpSession session) {
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, String> mapPam = new HashMap<String, String>();
		
		mapPam.put(new String("selectCol"), "paymentTypeName");
		mapPam.put(new String("moduleName"), "rep_dict_paymentkey");
		mapPam.put(new String("sqlWhere"), " GROUP BY paymentTypeName");
		
		List<Object> dataList = commonDictService.getDropDownDictList(mapPam);
		
		List<Map<String, String>> paymentTypeNameList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = (Map) dataList.get(i);
			//下拉控件显示text值下面绑定的id值，选中每一个text值，实质是选中id值
			String id =  dataMap.get("paymentTypeName").toString();
			String text = dataMap.get("paymentTypeName").toString();
			Map<String, String> paymentTypeNameMap = new HashMap<String, String>();
			paymentTypeNameMap.put(new String("id"), id);
			paymentTypeNameMap.put(new String("text"), text);
			paymentTypeNameList.add(paymentTypeNameMap);
		}
		
		map.put(new String("paymentTypeName"), paymentTypeNameList);
		return map;
	}

}
