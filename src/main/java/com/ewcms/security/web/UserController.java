/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.common.query.cache.CacheResultable;
import com.ewcms.common.query.jpa.EntityQueryable;
import com.ewcms.common.query.jpa.QueryFactory;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountManager;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;

@Controller
@RequestMapping(value = "/security/user")
public class UserController {

	@Autowired
	private AccountManager accountManager;
	@Autowired
	protected QueryFactory queryFactory;
	
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/user/index";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false)List<Long> selections, Model model){
		if(selections == null || selections.isEmpty()){
			model.addAttribute("user", new User());
			model.addAttribute("selections", new ArrayList<Long>(0));
		}else{
			User user = accountManager.getUser(selections.get(0));
			user = (user == null ? new User() : user);
			model.addAttribute("user", user);
			model.addAttribute("selections", selections);
		}
		model.addAttribute("allRoles", accountManager.getAllRole());
		return "security/user/edit";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("user")User user, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (user.getId() != null && StringUtils.hasText(user.getId().toString())){
				User oldUser = accountManager.getUser(user.getId());
				
				user.setPermissions(oldUser.getPermissions());
				user.setRoles(oldUser.getRoles());
				
				accountManager.saveUser(user, true);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					user = accountManager.getUser(selections.get(0));
					model.addAttribute("user", user);
					model.addAttribute("selections", selections);
				}
			}else{
				accountManager.saveUser(user, true);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, user.getId());
				model.addAttribute("user", new User());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "security/user/edit";
	}
	
	@RequiresPermissions("user:delete")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		for (Long id : selections){
			try{
				accountManager.deleteUser(id);
			}catch(ServiceException e){
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
    	}
		return "redirect:/security/user/index";
    	//return "security/user/index";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("oldLoginName") String oldLoginName,	@RequestParam("loginName") String loginName) {
		if (loginName.equals(oldLoginName) || accountManager.findUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/findAllRole")
	@ResponseBody
	public List<ComboBox> findAllRole(@RequestParam("id") Long id){
		List<Role> roles = accountManager.getAllRole();
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (roles == null || roles.isEmpty()) return comboBoxs;
		for (Role role : roles){
			ComboBox comboBox = new ComboBox();
			Role entity = accountManager.findSelectedRole(id, role.getId());
			if (entity != null) comboBox.setSelected(true);
			comboBox.setId(role.getId());
			comboBox.setText(role.getRoleName());
			comboBoxs.add(comboBox);
		}
		return comboBoxs;
	}
	
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String, Object> query(@ModelAttribute QueryParameter params) {
		int page =  params.getPage() - 1;
		int pageSize = params.getRows();
		
		EntityQueryable query = queryFactory.createEntityQuery(User.class).setPage(page).setRow(pageSize);

		String order = params.getOrder();
		if (order != null) {
			String sort = params.getSort();
			if (order.equals("asc")) {
				query.orderAsc(sort);
			} else {
				query.orderDesc(sort);
			}
		}
		
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
