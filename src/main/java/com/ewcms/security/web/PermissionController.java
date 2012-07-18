package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.common.query.cache.CacheResultable;
import com.ewcms.common.query.jpa.EntityQueryable;
import com.ewcms.common.query.jpa.QueryFactory;
import com.ewcms.security.model.Permission;
import com.ewcms.web.QueryParameter;

@Controller
@RequestMapping(value = "/security/permission")
public class PermissionController {
	@Autowired
	protected QueryFactory queryFactory;

	@RequiresPermissions("permission:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/permission/index";
	}
	
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String, Object> query(@ModelAttribute QueryParameter params) {
		int page =  params.getPage() - 1;
		int pageSize = params.getRows();
		
		EntityQueryable query = queryFactory.createEntityQuery(Permission.class).setPage(page).setRow(pageSize);

		String order = params.getOrder();
		if (order != null) {
			String sort = params.getSort();
			if (order.equals("asc")) {
				query.orderAsc(sort);
			} else {
				query.orderDesc(sort);
			}
		}
		query.orderAsc("id");
		
		List<String> selections = params.getSelections();
		if (selections != null && !selections.isEmpty()) {
			String[] selectArr = selections.toArray(new String[0]);
			List<Long> selectionArr = new ArrayList<Long>();
			for (String arr : selectArr) {
				selectionArr.add(Long.valueOf(arr));
			}
			query.in("id", selectionArr);
		}
		
		CacheResultable result = query.queryCacheResult(params.getCacheKey());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", result.getCount());
		resultMap.put("cacheKey", result.getCacheKey());
		resultMap.put("rows", result.getResultList());
		return resultMap;
	}

}
