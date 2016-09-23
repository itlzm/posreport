package com.dev.core.util;

public class PageModel {
	// 记录总数
	public int totalRows = 0;
	// 总页数
	public int totalPages = 0;
	
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
	public PageModel(){
		
	}
	
}
