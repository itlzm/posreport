package com.dev.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dev.core.model.UserModel;
import com.dev.core.service.RoleService;

import net.sf.json.JSONObject;

/*
 * Ȩ�޿�����
 * */
@Controller
@RequestMapping(value = "/systemui")
@SessionAttributes("currentUser")
public class RoleController {
	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(RoleController.class);

	@Autowired
	RoleService roleService;

	// ͨ��ģ��ת��ҳ�涨��
	@RequestMapping(value = "/{pageName}")
	public ModelAndView RedirectView(@PathVariable String pageName, HttpSession session) {
		try {
			logger.info("pageName: " + pageName);
			// �ж��û��Ƿ��Ѿ���¼
			UserModel userModel = (UserModel) session.getAttribute("currentUser");
			if (userModel == null) {
				return new ModelAndView("lockpage");
			} else {
				return new ModelAndView("systemui" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ��ѯ��ɫȨ���б�
	 */
	@RequestMapping(value = "/queryRoleData")
	public @ResponseBody Map<String, List<Object>> queryRoleData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreName = request.getParameter("StoreName");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		// ֧����ʽ
		String Paymentkey = request.getParameter("Paymentkey");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreName);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("Paymentkey"), Paymentkey);

		// ��ѯ����
		logger.info("��ѯ����:TimeStart:" + TimeStart + ",TimeEnd:" + TimeEnd + ",BrandName:" + BrandName + ",StoreName:"
				+ StoreName + ",ChannelName:" + ChannelName + ",Paymentkey:" + Paymentkey);
		map = roleService.querySystemRoleData(mappampay);
		return map;
	}

	/*
	 * ��ȡ�˵����ܼ��˵���ť����
	 */
	@RequestMapping(value = "/getFunButTreeData")
	public @ResponseBody Map<String, List<Object>> getFunButTreeData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ɫRoleID
		String roleID = request.getParameter("roleID");
		// �Ƿ�Ϊ�޸�״̬
		String isEdit = request.getParameter("isEdit");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("roleID"), roleID);
		mappampay.put(new String("isEdit"), isEdit);

		map = roleService.getFunButTreeData(mappampay);

		return map;
	}

	/*
	 * �����ɫȨ��
	 */
	@RequestMapping(value = "/saveRoleData")
	@SuppressWarnings({"unchecked" })
	public @ResponseBody Map<String, List<Object>> saveRoleData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String sendData = request.getParameter("sendData");
		JSONObject jasonObject = JSONObject.fromObject(sendData);
		Map<String, List<Object>> mapRoleData =( Map<String, List<Object>>)jasonObject;
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);

		map = roleService.saveRoleData(mapRoleData,mapPam);
		return map;
	}
	
	/*
	 * ɾ����ɫȨ��
	 */
	@RequestMapping(value = "/delRoleData")
	public @ResponseBody Map<String, List<Object>> delRoleData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		String roleID = request.getParameter("roleID");
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);
		mapPam.put(new String("roleID"), roleID);
		map = roleService.delRoleData(mapPam);
		return map;
	}
	
	/*
	 * ��ѯ�û������б�
	 */
	@RequestMapping(value = "/queryEmployeeData")
	@SuppressWarnings({ "unused" })
	public @ResponseBody Map<String, List<Object>> queryEmployeeData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreName = request.getParameter("StoreName");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreName);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		
		map = roleService.queryEmployeeData(mappampay);
		return map;
	}
	
	/*
	 * ��ѯ�û���Ϣ����Ӧ��ɫȨ��
	 */
	@RequestMapping(value = "/queryEmployeeCodeData")
	public @ResponseBody Map<String, List<Object>> queryEmployeeCodeData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// �û�����
		String employeeCode = request.getParameter("employeeCode");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("employeeCode"), employeeCode);
		
		map = roleService.queryEmployeeCodeData(mappampay);
		
		return map;
	}
	
	/*
	 * �����û���ɫȨ��
	 */
	@RequestMapping(value = "/saveEmployeeRoleData")
	@SuppressWarnings({ "unchecked" })
	public @ResponseBody Map<String, List<Object>> saveEmployeeRoleData(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String sendData = request.getParameter("sendData");
		JSONObject jasonObject = JSONObject.fromObject(sendData);
		Map<String, List<Object>> mapRoleData =( Map<String, List<Object>>)jasonObject;
		
		String companyCode = userModel.getCompanyCode();
		Map<String, String> mapPam = new HashMap<String, String>();
		mapPam.put(new String("companyCode"), companyCode);

		map = roleService.saveEmployeeRoleData(mapRoleData,mapPam);
		return map;
	}
	

}
