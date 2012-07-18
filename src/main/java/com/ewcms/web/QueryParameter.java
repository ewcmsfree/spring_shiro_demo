/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 * 
 * spring mvc直接映射查询参数类，页面查询参数构造在jquery.ewcms.js中$.ewcms.query()方法。
 * 
 * @author wangwei
 */
public class QueryParameter {

	private int page = 1;
	private int rows = 30;
	private String sort;
	private String order;
	private String cacheKey;
	private Map<String,String> parameters = new HashMap<String,String>(0);
	private List<String> selections = new ArrayList<String>();
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getCacheKey() {
		return cacheKey;
	}
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public List<String> getSelections() {
		return selections;
	}
	public void setSelections(List<String> selections) {
		this.selections = selections;
	}
	
	public String getParameterValue(String parameterName){
		return parameters.get(parameterName);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + page;
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + rows;
		result = prime * result
				+ ((selections == null) ? 0 : selections.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryParameter other = (QueryParameter) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (page != other.page)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (rows != other.rows)
			return false;
		if (selections == null) {
			if (other.selections != null)
				return false;
		} else if (!selections.equals(other.selections))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		return true;
	}
}
