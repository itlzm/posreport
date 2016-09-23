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
import com.dev.core.service.BusinessService;
import com.dev.core.util.ExportExcelUtils;

/*
 * 
 * Ӫҵ������ܷ���������
 * */
@Controller
@RequestMapping(value = "/business")
@SessionAttributes("currentUser")
public class BusinessController {

	@Autowired
	BusinessService businessService;

	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(BusinessController.class);

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
				return new ModelAndView("business" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}

	/*
	 * ======Ӫҵ�������Ʒ�������(Ʒ�ƻ���)==================================================
	 */
	@RequestMapping(value = "/queryRepTrendBrand")
	public @ResponseBody Map<String, List<Object>> queryRepTrendBrand(HttpServletRequest request, HttpSession session) {

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
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);

		map = businessService.queryRepTrendBrand(mappampay);

		return map;
	}

	/*
	 * ======Ӫҵ�������Ʒ�������(�������)==================================================
	 */
	@RequestMapping(value = "/queryRepTrendBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepTrendBrandChildren(HttpServletRequest request,
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
		String AreaCode = request.getParameter("AreaCode");

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
		mappampay.put(new String("ChannelCode"), ChannelCode);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepTrendBrandChildren(mappampay);

		return map;
	}

	/*
	 * ======Ӫҵ�������Ʒ�������(�ŵ����)==================================================
	 */
	@RequestMapping(value = "/queryRepTrend")
	public @ResponseBody Map<String, List<Object>> queryRepTrend(HttpServletRequest request, HttpSession session) {

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
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelCode"), ChannelCode);

		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("AreaName"), AreaName);

		map = businessService.queryRepTrend(mappampay);

		return map;
	}

	/*
	 * ======Ӫҵ�������Ʒ���������Excel=================================================
	 */
	@RequestMapping(value = "/queryRepTrendExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepTrendExportExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {

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
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);

		map = businessService.queryRepTrendExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("Ӫҵ�������Ʒ�����ϸ", "Ӫҵ�������Ʒ�����ϸ.xls", "Ӫҵ�������Ʒ�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======Ӫҵ���뻷�ȷ�������========================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHB")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHB(HttpServletRequest request, HttpSession session) {

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

		map = businessService.queryRepBusinessHB(mappampay);
		return map;
	}

	/*
	 * ======Ӫҵ���뻷�ȷ������Ʒ�Ʊ���====================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildren(HttpServletRequest request,
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

		map = businessService.queryRepBusinessHBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======Ӫҵ���뻷�ȷ������Ʒ���ŵ걨��==================================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildrenStore(HttpServletRequest request,
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

		map = businessService.queryRepBusinessHBBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======Ӫҵ���뻷�ȷ������Ʒ���ŵ걨����Excel===========================================
	 */
	@RequestMapping(value = "/queryRepBusinessHBBrandChildrenStoreExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessHBBrandChildrenStoreExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		map = businessService.queryRepBusinessHBBrandChildrenStoreExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("Ӫҵ���뻷�ȷ�����ϸ", "Ӫҵ���뻷�ȷ�����ϸ.xls", "Ӫҵ���뻷�ȷ�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ�������========================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTB")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTB(HttpServletRequest request, HttpSession session) {

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

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("BrandCode"), BrandCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTB(mappampay);
		return map;
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ������Ʒ�Ʊ���====================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBBrandChildren(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======Ӫҵ����ͬ�ȷ������Ʒ���ŵ걨��==================================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBBrandChildrenStore(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ�������===============================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDate")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDate(HttpServletRequest request,
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

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("BrandCode"), BrandCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDate(mappampay);
		return map;
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ������Ʒ�Ʊ���===========================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildren(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ������Ʒ���ŵ걨��=========================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildrenStore")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStore(HttpServletRequest request,
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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildrenStore(mappampay);
		return map;
	}

	/*
	 * ======(�Զ���ʱ��)Ӫҵ����ͬ�����ڷ������Ʒ���ŵ걨����Excel==================================
	 */
	@RequestMapping(value = "/queryRepBusinessTBDateBrandChildrenStoreExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepBusinessTBDateBrandChildrenStoreExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);

		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepBusinessTBDateBrandChildrenStoreExportExcel(mappampay);
		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("Ӫҵ�������ڷ�Χͬ�ȷ�����ϸ", "Ӫҵ�������ڷ�Χͬ�ȷ�����ϸ.xls", "Ӫҵ�������ڷ�Χͬ�ȷ�����ϸ", queryText, listData, listDataSum,
				response);

		return null;
	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(Ʒ�ƻ���)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrand")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrand(HttpServletRequest request,
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
		String ChannelName = request.getParameter("ChannelName");

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));

		String companyCode = userModel.getCompanyCode();

		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), String.valueOf(PageIndex));
		mappampay.put(new String("PageSize"), String.valueOf(PageSize));
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);

		map = businessService.queryRepProductCatTBBrand(mappampay);
		return map;

	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(�������)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildren(HttpServletRequest request,
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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

		String companyCode = userModel.getCompanyCode();
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

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
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		map = businessService.queryRepProductCatTBBrandChildren(mappampay);
		return map;

	}

	/*
	 * ======��Ʒ����ͬ�ȷ���(��Ʒ����)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildrenProduct")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildrenProduct(HttpServletRequest request,
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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");
		
		
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
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		map = businessService.queryRepProductCatTBBrandChildrenProduct(mappampay);
		return map;

	}

	/*
	 * ======��Ʒ����ͬ�ȷ�������Excel===================================================
	 */
	@RequestMapping(value = "/queryRepProductCatTBBrandChildrenProductExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatTBBrandChildrenProductExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");
		String TBTimeStart = request.getParameter("TBTimeStart");
		String TBTimeEnd = request.getParameter("TBTimeEnd");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);
		mappampay.put(new String("TBTimeStart"), TBTimeStart);
		mappampay.put(new String("TBTimeEnd"), TBTimeEnd);
		
		map = businessService.queryRepProductCatTBBrandChildrenProductExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("��Ʒ����ͬ�ȷ�����ϸ", "��Ʒ����ͬ�ȷ�����ϸ.xls", "��Ʒ����ͬ�ȷ�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}

	/*
	 * ======��Ʒ���໷�ȷ���(Ʒ�ƻ���)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrand")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrand(HttpServletRequest request,
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
		String ChannelName = request.getParameter("ChannelName");

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrand(mappampay);
		return map;
	}

	/*
	 * ======��Ʒ���໷�ȷ���(�������)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildren")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildren(HttpServletRequest request,
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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildren(mappampay);
		return map;
	}

	/*
	 * ======��Ʒ���໷�ȷ���(��Ʒ����)====================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildrenProduct")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildrenProduct(HttpServletRequest request,
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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

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
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildrenProduct(mappampay);
		return map;
	}

	/*
	 * ======��Ʒ���໷�ȷ�������Excel===================================================
	 */
	@RequestMapping(value = "/queryRepProductCatHBBrandChildrenProductExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepProductCatHBBrandChildrenProductExportExcel(
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

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

		// ����
		String CategoryCode = request.getParameter("CategoryCode");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("businessDateStart"), TimeStart);
		mappampay.put(new String("businessDateEnd"), TimeEnd);
		mappampay.put(new String("StoreCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "100000");
		mappampay.put(new String("ChannelName"), ChannelName);
		mappampay.put(new String("BrandCode"), BrandCode);
		mappampay.put(new String("BrandName"), BrandName);
		mappampay.put(new String("AreaCode"), AreaCode);
		mappampay.put(new String("CategoryCode"), CategoryCode);

		map = businessService.queryRepProductCatHBBrandChildrenProductExportExcel(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("��Ʒ���໷�ȷ�����ϸ", "��Ʒ���໷�ȷ�����ϸ.xls", "��Ʒ���໷�ȷ�����ϸ", queryText, listData, listDataSum, response);

		return null;
	}
}
