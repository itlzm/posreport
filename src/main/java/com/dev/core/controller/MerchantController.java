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
import com.dev.core.service.MerchantService;
import com.dev.core.util.ExportExcelUtils;

@Controller
@RequestMapping("/merchant")
@SessionAttributes("currentUser")
public class MerchantController {
	
	// ��¼��־��Ϣ
	private static Logger logger = Logger.getLogger(StoreController.class);
	
	@Autowired
	MerchantService merchantService;
	
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
				return new ModelAndView("merchant" + "/" + pageName);
			}
		} catch (Exception ex) {
			return new ModelAndView("lockpage");
		}
	}
	/*
	 * �����̼���Ϣ1��Excel�ļ�
	 */
	@RequestMapping("/queryMerchantDetailExportExcel")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetailExportExcel(HttpServletRequest request,
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


		map = merchantService.queryMerchantDetail(mapJob);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("�̼���Ϣ����", "�̼���Ϣ����.xls", "�̼���Ϣ����",queryText, listData, listDataSum, response);
		
		return null;
	}
	//�̼���Ϣ1
	@RequestMapping("/queryMerchantDetail")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetail(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// �̼�����
		String FullName = request.getParameter("FullName");
		// ��ϵ������
		String ContractPerson = request.getParameter("ContractPerson");
		
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapMerchant = new HashMap<String, String>();
		
		mapMerchant.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapMerchant.put(new String("PageSize"), String.valueOf(PageSize));
		mapMerchant.put(new String("ContractPerson"), ContractPerson);
		mapMerchant.put(new String("FullName"), FullName);
		
		map = merchantService.queryMerchantDetail(mapMerchant);
		
		return map;
	}
	
	/*
	 * �����̼���Ϣ2��Excel�ļ�
	 */
	@RequestMapping("/queryMerchantDetailExportExcel2")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetailExportExcel2(HttpServletRequest request,
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
		//��Ҫ��js���󷢹��������ݽ���ת�����
		//�̼�����
		String FullName = new String(request.getParameter("FullName").getBytes("ISO8859-1"),"UTF-8");
		//��ϵ������
		String ContractPerson = request.getParameter("ContractPerson");

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
		mapJob.put(new String("FullName"), FullName);
		mapJob.put(new String("ContractPerson"), ContractPerson);


		map = merchantService.queryMerchantDetail2(mapJob);

		// Excel����
		List<Object> listData = map.get("listData");

		// Excel�ϼ�����
		List<Object> listDataSum = map.get("listDataSum");

		// ʵ���� ����Excel����
		ExportExcelUtils ee = new ExportExcelUtils();

		List<String> imgList = new ArrayList<String>();
		imgList.add(img1);

		ee.ExportExcel("�̼���Ϣ����2", "�̼���Ϣ����2.xls", "�̼���Ϣ����2",queryText, listData, listDataSum, response);
		
		
		return null;
	}
	
	//�̼���Ϣ2
	@RequestMapping("/queryMerchantDetail2")
	public @ResponseBody Map<String, List<Object>> queryMerchantDetail2(HttpServletRequest request, HttpSession session) {
		// ��ȡ��ǰ��¼�û�
		UserModel userModel = (UserModel) session.getAttribute("currentUser");
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		
		// �̼����� 
		String FullName = request.getParameter("FullName");
		// ��ϵ������
		String ContractPerson = request.getParameter("ContractPerson");
		int PageIndex = Integer.parseInt(request.getParameter("PageIndex"));
		int PageSize = Integer.parseInt(request.getParameter("PageSize"));
		
		Map<String, String> mapMerchant = new HashMap<String, String>();
		
		mapMerchant.put(new String("PageIndex"), String.valueOf(PageIndex));
		mapMerchant.put(new String("PageSize"), String.valueOf(PageSize));
		mapMerchant.put(new String("ContractPerson"), ContractPerson);
		mapMerchant.put(new String("FullName"), FullName);
		
		map = merchantService.queryMerchantDetail2(mapMerchant);
		
		return map;
	}
	
}
