package com.dev.core.util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import sun.misc.BASE64Decoder;

/*
 * ����Excel������
 * */
@SuppressWarnings("unused")
public class ExportExcelUtils {
	private static Logger logger = Logger.getLogger(ExportExcelUtils.class);

	/**
	 * ����������
	 */
	private SXSSFWorkbook wb;

	/**
	 * ���������
	 */
	private Sheet sheet;

	/**
	 * ��ʽ�б�
	 */
	private Map<String, CellStyle> styles;

	/**
	 * ��ǰ�к�
	 */
	private int rownum;

	/**
	 * ���캯��
	 * 
	 * @param title
	 *            �����⣬������ֵ������ʾ�ޱ���
	 * @param headerList
	 *            ��ͷ�б�
	 */
	public ExportExcelUtils() {

	}

	/**
	 * ��ʼ������
	 * 
	 * @param title
	 *            �����⣬������ֵ������ʾ�ޱ���
	 * @param headerList
	 *            ��ͷ�б�
	 */
	private void initialize(String title, String sheetName, String queryContent, List<String> headerList) {
		this.wb = new SXSSFWorkbook(500);

		this.sheet = wb.createSheet(sheetName);
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)) {
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(),
					headerList.size() - 1));
		}
		if (StringUtils.isNotBlank(queryContent)) {
			Row queryRow = sheet.createRow(rownum++);
			queryRow.setHeightInPoints(100);
			Cell queryCell = queryRow.createCell(0);
			queryCell.setCellStyle(styles.get("query"));
			queryCell.setCellValue(queryContent);

			sheet.addMergedRegion(new CellRangeAddress(queryRow.getRowNum(), queryRow.getRowNum(),
					queryRow.getRowNum() - 1, headerList.size() - 1));
		}

		// Create header
		if (headerList == null) {
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(20);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length == 2) {
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch()
						.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			} else {
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) {
			int colWidth = sheet.getColumnWidth(i) * 2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}

	}

	/**
	 * ���������ʽ
	 *
	 * @param wb
	 *            ����������
	 * @return ��ʽ�б�
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		// style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("query", style);

		return styles;
	}

	/**
	 * ���һ��
	 * 
	 * @return �ж���
	 */
	public Row addRow() {
		return sheet.createRow(rownum++);
	}

	/**
	 * ���һ����Ԫ��
	 * 
	 * @param row
	 *            ��ӵ���
	 * @param column
	 *            ����к�
	 * @param val
	 *            ���ֵ
	 * @return ��Ԫ�����
	 */
	public Cell addCell(Row row, int column, Object val) {
		return this.addCell(row, column, val, 0, Class.class);
	}

	/**
	 * ���һ����Ԫ��
	 * 
	 * @param row
	 *            ��ӵ���
	 * @param column
	 *            ����к�
	 * @param val
	 *            ���ֵ
	 * @param align
	 *            ���뷽ʽ��1������2�����У�3�����ң�
	 * @return ��Ԫ�����
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType) {
		Cell cell = row.createCell(column);
		CellStyle style = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
		try {
			if (val == null) {
				cell.setCellValue("");
			} else if (val instanceof String) {
				cell.setCellValue((String) val);
			} else if (val instanceof Integer) {
				cell.setCellValue((Integer) val);
			} else if (val instanceof Long) {
				cell.setCellValue((Long) val);
			} else if (val instanceof Double) {
				cell.setCellValue((Double) val);
			} else if (val instanceof Float) {
				cell.setCellValue((Float) val);
			} else if (val instanceof Date) {
				DataFormat format = wb.createDataFormat();
				style.setDataFormat(format.getFormat("yyyy-MM-dd"));
				cell.setCellValue((Date) val);
			} else {
				if (fieldType != Class.class) {
					cell.setCellValue((String) fieldType.getMethod("setValue", Object.class).invoke(null, val));
				} else {
					cell.setCellValue((String) Class
							.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
									"fieldtype." + val.getClass().getSimpleName() + "Type"))
							.getMethod("setValue", Object.class).invoke(null, val));
				}
			}
		} catch (Exception ex) {

			cell.setCellValue(val.toString());
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * ������ļ�
	 * 
	 * @param name
	 *            ����ļ���
	 */
	public ExportExcelUtils writeFile(String name) throws FileNotFoundException, IOException {
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

	/**
	 * ������ͻ���
	 *
	 * @param fileName
	 *            ����ļ���
	 */
	public ExportExcelUtils write(HttpServletResponse response, String fileName) throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");

		/*
		 * response.setHeader("Content-Disposition", "attachment; filename=" +
		 * java.net.URLEncoder.encode(fileName, "UTF-8"));
		 */

		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO-8859-1"));

		write(response.getOutputStream());
		return this;
	}

	/**
	 * ���������
	 *
	 * @param os
	 *            ���������
	 */
	public ExportExcelUtils write(OutputStream os) throws IOException {
		wb.write(os);
		return this;
	}

	/**
	 * ������ʱ�ļ�
	 */
	public ExportExcelUtils dispose() {
		wb.dispose();
		return this;
	}

	/**
	 * �ϲ���Ԫ��
	 * 
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCell(int sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		Sheet mSheet = wb.getSheetAt(sheet);
		CellRangeAddress address = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		mSheet.addMergedRegion(address);
	}

	/**
	 * ���� �ϲ���Ԫ��
	 * 
	 * @param rownum
	 *            ��ʼ��
	 * @param domListSize
	 *            ����
	 * @param col
	 *            ��
	 */
	public void mergeRow(int rownum, int domListSize, int col) {
		mergeCell(0, rownum - domListSize + 1, rownum, col, col);
	}

	@SuppressWarnings("unchecked")
	public void ExportExcel(String titleName, String fileName, String sheetName, String queryContent,
			List<Object> listData, List<Object> listDataSum, HttpServletResponse response) throws IOException {

		// Excel����
		List<String> headerList = new ArrayList<String>();
		// ������ѯ�������ֶ�����
		for (int i = 0; i < listDataSum.size(); i++) {
			if (i == 0) {
				Map<String, String> mapCols = new HashMap<String, String>();
				mapCols = (Map<String, String>) listDataSum.get(i);

				// ��ȡ�ֶ�����
				for (Map.Entry<String, String> entry : mapCols.entrySet()) {
					String colKey = entry.getKey();
					colKey = colKey.replace("<br>", "\n");
					headerList.add(colKey);
				}
				break;
			}
		}

		initialize(titleName, sheetName, queryContent, headerList);

		for (int i = 0; i < listData.size(); i++) {
			Row row = addRow();

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listData.get(i);

			int j = 0;
			// ��ȡ�ֶ�����
			for (Map.Entry<String, String> entry : mapValue.entrySet()) {
				addCell(row, j, entry.getValue());
				j++;
			}
			
		}
		
		

		for (int i = 0; i < listDataSum.size(); i++) {
			Row row = addRow();

			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue = (Map<String, String>) listDataSum.get(i);

			int j = 0;
			// ��ȡ�ֶ�����
			for (Map.Entry<String, String> entry : mapValue.entrySet()) {
				addCell(row, j, entry.getValue());
				j++;
			}
		}

		write(response, fileName);
		dispose();

	}

	/*
	 * ����Excel����ͼƬ
	 */
	public void ExportExcel(String titleName, String fileName, String sheetName, String queryContent,
			List<Object> listData, List<Object> listDataSum, List<String> listImg, HttpServletResponse response)
			throws IOException {

	
		logger.info(listImg.get(0));

		String[] arr = listImg.get(0).split("base64,");

		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buffer = decoder.decodeBuffer(arr[1]);

		int pictureIdx = wb.addPicture(buffer, Workbook.PICTURE_TYPE_PNG);
		Sheet sheetPic = wb.createSheet(sheetName + "ͼ��");

		Drawing drawing = sheetPic.createDrawingPatriarch();

		CreationHelper helper = wb.getCreationHelper(); // add a picture shape
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(3);  
		anchor.setRow1(2); 
		
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		
		pict.resize();

		//write(response, fileName);
		dispose();

	}


	private static int getAnchorX(int px, int colWidth) {
		return (int) Math.round(((double) 701 * 16000.0 / 301) * ((double) 1 / colWidth) * px);
	}

	private static int getAnchorY(int px, int rowHeight) {
		return (int) Math.round(((double) 144 * 8000 / 301) * ((double) 1 / rowHeight) * px);
	}

	private static int getRowHeight(int px) {
		return (int) Math.round(((double) 4480 / 300) * px);
	}

	private static int getColWidth(int px) {
		return (int) Math.round(((double) 10971 / 300) * px);
	}

}
