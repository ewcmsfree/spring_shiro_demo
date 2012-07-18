package com.ewcms.security.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.common.query.jpa.QueryFactory;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;
import com.ewcms.security.service.AccountManager;
import com.ewcms.web.QueryParameter;

@Controller
@RequestMapping(value = "/security/roledetail")
public class RoleDetailController {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	protected QueryFactory queryFactory;
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/index")
	public String index(@RequestParam("id") Long id, Model model){
		model.addAttribute("id", id);
		return "security/role/detail";
	}

	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String, Object> query(@ModelAttribute QueryParameter params, @RequestParam("id") Long id){
		Role role = accountManager.getRole(id);
		Set<Permission> permissions = new HashSet<Permission>();
		if (role != null){
			permissions = role.getPermissions();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", permissions.size());
		resultMap.put("rows", permissions);
		return resultMap;
	}
	
	@RequestMapping(value = "/editPermission")
	@ResponseBody
	public String editPermission(@RequestParam(required = false) List<Long> permissionIds, @RequestParam("id") Long id, @RequestParam("isRemove") Boolean isRemove){
		if (id == null) return "角色未选择";
		
		Role role = accountManager.getRole(id);
		if (role == null) return "角色不存在";
		
		if (permissionIds != null && !permissionIds.isEmpty()){
			Set<Permission> permissions = role.getPermissions();
			if (isRemove){
				if (permissions == null || permissions.isEmpty()) return "未有移除的权限";
				for (Long permissionId : permissionIds){
					Permission permission = accountManager.getPermission(permissionId);
					permissions.remove(permission);
				}
			}else{
				for (Long permissionId : permissionIds){
					Permission permission = accountManager.getPermission(permissionId);
					permissions.add(permission);
				}
			}
			role.setPermissions(permissions);
			accountManager.saveRole(role);
		}
		return "success";
	}
}
