package com.dev.core.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;

import com.dev.core.model.UserModel;
import com.dev.core.service.ActivityService;
import com.dev.core.util.ExportExcelUtils;

/*
 * 
 * Ӫ���Ч������������
 * */
@Controller
@RequestMapping(value = "/activity")
@SessionAttributes("currentUser")
public class ActivityController {

	@Autowired
	ActivityService activityService;

	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(ActivityController.class);

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
				return new ModelAndView("activity" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ======�����������������(Ʒ�ƻ���)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityBrand")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityBrand(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");

		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// ��ѯ����
		map = activityService.queryRepChannelActivityBrand(mappampay);
		return map;
	}

	/*
	 * ======�����������������(�������)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityBrandChildren(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String AreaCode = request.getParameter("AreaCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);

		// ��ѯ����
		map = activityService.queryRepChannelActivityBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======�����������������(�ŵ����)==================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivity")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivity(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");

		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = request.getParameter("favourableName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		String AreaName = request.getParameter("AreaName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = activityService.queryRepChannelActivity(mappampay);
		return map;
	}

	/*
	 * ======�������������������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepChannelActivityExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepChannelActivityExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = URLDecoder.decode(request.getParameter("favourableName"), "UTF-8");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);

		map = activityService.queryRepChannelActivityExportExcel(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("���������������ϸ", "���������������ϸ.xls", "���������������ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�����Աȷ�������(Ʒ�ƻ���)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseBrand")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseBrand(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");

		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// ��ѯ����
		map = activityService.queryRepActivityTransverseBrand(mappampay);
		return map;
	}

	/*
	 * ======�����Աȷ�������(�������)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseBrandChildren(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String AreaCode = request.getParameter("AreaCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);

		// ��ѯ����
		map = activityService.queryRepActivityTransverseBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======�����Աȷ�������(�ŵ����)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverse")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverse(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");

		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = request.getParameter("favourableName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		String AreaName = request.getParameter("AreaName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = activityService.queryRepActivityTransverse(mappampay);
		return map;
	}

	/*
	 * ======�����Աȷ���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActivityTransverseExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActivityTransverseExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = URLDecoder.decode(request.getParameter("favourableName"), "UTF-8");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);

		map = activityService.queryRepActivityTransverseExportExcel(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�����Աȷ�����ϸ", "�����Աȷ�����ϸ.xls", "�����Աȷ���", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(Ʒ�ƻ���)=================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepMealSingleBrand")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleBrand(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");

		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		// ��ѯ����
		map = activityService.queryRepMealSingleBrand(mappampay);
		return map;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�������)=================================================
	 */
	@RequestMapping(value = "/queryRepMealSingleBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleBrandChildren(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		// ��ѯ����
		map = activityService.queryRepMealSingleBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷�������(�ŵ����)=================================================
	 */
	@RequestMapping(value = "/queryRepMealSingle")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingle(HttpServletRequest request, HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");

		String BrandCode = request.getParameter("BrandCode");

		// Ʒ��
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		String AreaName = request.getParameter("AreaName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		// ��ѯ����
		map = activityService.queryRepMealSingle(mappampay);
		return map;
	}

	/*
	 * ======�ײͼ���Ʒ�ۿ۷���������Excle================================================
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryRepMealSingleExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepMealSingleExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelName = request.getParameter("ChannelName");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");

		mappampay.put(new String("BrandCode"), BrandCode);

		// ��ѯ����
		map = activityService.queryRepMealSingleExportExcel(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�ײͼ���Ʒ�ۿ۷�����ϸ", "�ײͼ���Ʒ�ۿ۷�����ϸ.xls", "�ײͼ���Ʒ�ۿ۷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======�Ӫ�չ��׷�������(Ʒ�ƻ���)===================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueBrand")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueBrand(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");

		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);

		// ��ѯ����
		map = activityService.queryRepActivityRevenueBrand(mappampay);

		logger.info("map=" + map);

		return map;
	}

	/*
	 * ======�Ӫ�չ��׷�������(�������)===================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueBrandChildren(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String AreaCode = request.getParameter("AreaCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");
		String favourableName = request.getParameter("favourableName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("AreaCode"), AreaCode);
		// ��ѯ����
		map = activityService.queryRepActivityRevenueBrandChildren(mappampay);

		return map;
	}

	/*
	 * ======�Ӫ�չ��׷�������(�ŵ����)==================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenue")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenue(HttpServletRequest request,
			HttpSession session) {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		String BrandName = request.getParameter("BrandName");

		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = request.getParameter("favourableName");

		// ����
		String AreaCode = request.getParameter("AreaCode");

		String AreaName = request.getParameter("AreaName");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);

		mappampay.put(new String("favourableName"), favourableName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		// ��ѯ����
		map = activityService.queryRepActivityRevenue(mappampay);

		logger.info("map=" + map);

		return map;
	}

	/*
	 * ======�Ӫ�չ��׷���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepActivityRevenueExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepActivityRevenueExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// Ʒ��
		String BrandCode = request.getParameter("BrandCode");
		// �ŵ�
		String StoreCode = request.getParameter("StoreCode");
		// ����
		String ChannelCode = request.getParameter("ChannelCode");

		String favourableName = URLDecoder.decode(request.getParameter("favourableName"), "UTF-8");

		String companyCode = userModel.getCompanyCode();

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");

		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("favourableName"), favourableName);

		// ��ѯ����
		map = activityService.queryRepActivityRevenueExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("�Ӫ�չ��׷�����ϸ", "�Ӫ�չ��׷�����ϸ.xls", "�Ӫ�չ��׷�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

}
