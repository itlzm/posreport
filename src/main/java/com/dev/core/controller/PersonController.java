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
import com.dev.core.service.PersonService;
import com.dev.core.util.ExportExcelUtils;


/*
 * ������־����Controller
 */
@Controller
@RequestMapping(value = "/person")
@SessionAttributes("currentUser")
public class PersonController {
	
	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(StoreController.class);

	@Autowired
	PersonService personService;
	
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
				return new ModelAndView("person" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}
	
	//��ҵ��־����
	@RequestMapping("/queryRepjobDetail")
	public @ResponseBody Map<String, List<Object>> queryRepjobDetail(HttpServletRequest request, HttpSession session) {
		
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// ��ʼʱ��
		String TimeStart = request.getParameter("TimeStart");
		// ����ʱ��
		String TimeEnd = request.getParameter("TimeEnd");
		// ִ�в���
		String JobStep = request.getParameter("JobStep");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		// ��ȡjobTimeֵ������ʱʹ�á�
		String JobTime = request.getParameter("JobTime");
		
		Map<String, String> mapJob = new HashMap<String, String>();
		
		mapJob.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapJob.put(new String("PageSize"), String.valueOf(PageSize));
		mapJob.put(new String("jobDateStart"), TimeStart);
		mapJob.put(new String("jobDateEnd"), TimeEnd);
		mapJob.put(new String("jobStep"), JobStep);
		mapJob.put(new String("jobTime"), JobTime);
		
		map = personService.queryRepjobDetail(mapJob);
		
		return map;
	}
	
	//������ҵ��־����Excel
	@RequestMapping("/queryRepjobDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryReppayDetailExportExcel(HttpServletRequest request,
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

		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");
		// ��ȡִ�в���
		String JobStep = request.getParameter("JobStep");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		logger.info("queryText=" + queryText);

		// ֧����ʽ
		String Paymentkey = request.getParameter("Paymentkey");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("jobDateStart"), TimeStart);
		mappampay.put(new String("jobDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "1000000");
		mappampay.put(new String("Paymentkey"), Paymentkey);
		mappampay.put(new String("jobStep"), JobStep);

		map = personService.queryRepjobDetail(mappampay);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("��ҵ��־����", "��ҵ��־����.xls", "��ҵ��־����", queryText, listData, listDataSum, response);
		
		return null;
	}
	
	//��ҵ��־ͳ�Ʒ���
	@RequestMapping("queryRepjobStatistics")
	public @ResponseBody Map<String, List<Object>> queryRepjobStatistics(HttpServletRequest request, HttpSession session) {
		
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
		
		map = personService.queryRepjobStatistics(mapJob);
		
		return map;
	}
	//������ҵ��־ͳ�Ʒ���Excel
	@RequestMapping("/queryRepjobStatisticsExportExcel")
	public @ResponseBody Map<String, List<Object>> queryRepjobStatisticsExportExcel(HttpServletRequest request,
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
		// ��ȡjobTime��������ʱ����Excelʹ�á�
		String JobTime = request.getParameter("JobTime");
		
		String queryText = URLDecoder.decode(request.getParameter("queryText"), "UTF-8");

		Date data = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ
		String nowData = dateFormat.format(data);

		queryText += "\n" + "����ʱ��:" + nowData;

		logger.info("queryText=" + queryText);

		// ֧����ʽ
		String Paymentkey = request.getParameter("Paymentkey");
		String companyCode = userModel.getCompanyCode();

		Map<String, String> mappampay = new HashMap<String, String>();
		mappampay.put(new String("companyCode"), companyCode);
		mappampay.put(new String("jobDateStart"), TimeStart);
		mappampay.put(new String("jobDateEnd"), TimeEnd);
		mappampay.put(new String("storeCode"), StoreCode);
		mappampay.put(new String("PageIndex"), "1");
		mappampay.put(new String("PageSize"), "1000000");
		mappampay.put(new String("Paymentkey"), Paymentkey);
		mappampay.put(new String("jobTime"), JobTime);

		//�ж�jobTime�Ƿ�Ϊ�գ���������ʱExcel��񣬷��򵼳���һ��ҳ���ά�������
		if(JobTime != ""){
			map = personService.queryRepjobDetail(mappampay);
		}else{
			map = personService.queryRepjobStatistics(mappampay);
		}

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		ee.ExportExcel("��ҵ��־����", "��ҵ��־����.xls", "��ҵ��־����", queryText, listData, listDataSum, response);
		
		return null;
	}
}
