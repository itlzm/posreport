package com.dev.core.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.dev.core.service.JobService;
import com.dev.core.util.ExportExcelUtils;
import com.sun.javafx.binding.StringFormatter;

@Controller
@RequestMapping("/job")
@SessionAttributes("currentUser")
public class JobController {
	
	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(StoreController.class);
	
	@Autowired
	JobService jobService;
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
				return new ModelAndView("job" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}
	
	/*
	 * ======��ҵ��־����������Excle==================================================
	 */

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "/queryJobDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryJobDetailExportExcel(HttpServletRequest request,
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

		String img1 = request.getParameter("img1");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;
		logger.info("queryText=" + queryText);

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mapJob = new HashMap<String, String>();
		mapJob.put(new String("companyCode"), companyCode);
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("storeCode"), StoreCode);
		mapJob.put(new String("PageIndex"), "1");
		mapJob.put(new String("PageSize"), "100000");

		map = jobService.queryJobDetail(mapJob);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("��ҵ��־����������ϸ", "��ҵ��־����������ϸ.xls", "��ҵ��־����������ϸ",queryText, listData, listDataSum, response);

		return null;

	}

	
	@RequestMapping("/queryJobDetail")
	public @ResponseBody Map<String, List<Object>> queryJobDetail(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		String jobStep = request.getParameter("jobStep");
		
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("jobStep"), jobStep);
		map = jobService.queryJobDetail(mapJob);
		
		return map;
	}
	@RequestMapping("/queryPaymentKey")
	public @ResponseBody Map<String, List<Object>> queryPaymentKey(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		String paymentTypeName = request.getParameter("PaymentTypeName");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapPayment = new HashMap<String, String>();
		
		mapPayment.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapPayment.put(new String("PageSize"), String.valueOf(PageSize));
		mapPayment.put(new String("PaymentTypeName"), paymentTypeName);
		map = jobService.queryPaymentKey(mapPayment);
		
		return map;
	}
	
	/*
	 * ======��ҵ��־ͳ�Ʒ�������Excle==================================================
	 */
	@RequestMapping("/queryRepjobstatisticsExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepjobstatisticsExportExcel(HttpServletRequest request,
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

		String img1 = request.getParameter("img1");

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;
		logger.info("queryText=" + queryText);

		String companyCode = userModel.getCompanyCode();

		Map<String, String> mapJob = new HashMap<String, String>();
		mapJob.put(new String("companyCode"), companyCode);
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("storeCode"), StoreCode);
		mapJob.put(new String("PageIndex"), "1");
		mapJob.put(new String("PageSize"), "100000");

		map = jobService.queryRepjobstatistics(mapJob);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("��ҵ��־ͳ�Ʒ���", "��ҵ��־ͳ�Ʒ���.xls", "��ҵ��־ͳ�Ʒ���",queryText, listData, listDataSum, response);
		
		
		
		
		return null;
	}
	
	@RequestMapping("/queryRepjobstatistics")
	public @ResponseBody Map<String, List<Object>> queryRepjobstatistics(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		
		map = jobService.queryRepjobstatistics(mapJob);
		return map;
	}
	@RequestMapping("/queryDownGo")
	public @ResponseBody Map<String, List<Object>> queryDownGo(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		// ��ʼʱ��
		String jobTime = request.getParameter("jobTime");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobTime"), jobTime);
		map = jobService.queryDownGo(mapJob);
		return map;
	}
}
