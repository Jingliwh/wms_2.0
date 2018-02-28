package com.gomi.bos.domain.base;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
@XmlRootElement(name="pageBean")
@XmlSeeAlso(Promotion.class)
public class PageBean<T> {
	private long totalCount;
	private List<T> pageData;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
	

}
