package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.ewcms.common.query.cache.CacheResultable;
import com.ewcms.common.query.jpa.EntityQueryable;
import com.ewcms.common.query.jpa.QueryFactory;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;
import com.ewcms.security.service.AccountManager;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;

@Controller
@RequestMapping(value = "/security/role")
public class RoleController {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	protected QueryFactory queryFactory;
	
	@RequiresPermissions("role:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/role/index";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false)List<Long> selections, Model model){
		if(selections == null || selections.isEmpty()){
			model.addAttribute("role", new Role());
			model.addAttribute("selections", new ArrayList<Long>(0));
		}else{
			Role role = accountManager.getRole(selections.get(0));
			role = (role == null ? new Role() : role);
			model.addAttribute("role", role);
			model.addAttribute("selections", selections);
		}
		return "security/role/edit";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("role")Role role, @RequestParam(required = false) List<Long> selections, @RequestParam(required = false) List<Long> permissionIds, Model model){
		Boolean close = Boolean.FALSE;
		Set<Permission> permissions = new HashSet<Permission>();
		if (permissionIds != null && !permissionIds.isEmpty()){
			for (Long permissionId : permissionIds){
				Permission permission = accountManager.getPermission(permissionId);
				permissions.add(permission);
			}
		}
		role.setPermissions(permissions);
		if (role.getId() != null && StringUtils.hasText(role.getId().toString())){
			accountManager.saveRole(role);
			selections.remove(0);
			if(selections == null || selections.isEmpty()){
				close = Boolean.TRUE;
			}else{
				role = accountManager.getRole(selections.get(0));
				model.addAttribute("role", role);
				model.addAttribute("selections", selections);
			}
		}else{
			accountManager.saveRole(role);
			selections = selections == null ? new ArrayList<Long>() : selections;
			selections.add(0, role.getId());
			model.addAttribute("role", new Role());
			model.addAttribute("selections", selections);
		}
		model.addAttribute("close",close);
		
		return "security/role/edit";
	}
	
	@RequiresPermissions("role:delete")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections){
		for (Long id : selections){
			accountManager.deleteRole(id);
    	}
    	return "security/role/index";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/checkRoleName")
	@ResponseBody
	public String checkRoleName(@RequestParam("oldRoleName") String oldRoleName, @RequestParam("roleName") String roleName) {
		if (roleName.equals(oldRoleName) || accountManager.findRoleByRoleName(roleName) == null) {
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/checkCaption")
	@ResponseBody
	public String checkCaption(@RequestParam("oldCaption") String oldCaption, @RequestParam("caption") String caption){
		if (caption.equals(oldCaption) || accountManager.findRoleByCaption(caption) == null){
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/findAllPermission")
	@ResponseBody
	public List<ComboBox> findAllPermission(@RequestParam("id") Long id){
		List<Permission> permissions = accountManager.getAllPermission();				
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (permissions == null || permissions.isEmpty()) return comboBoxs;
		for (Permission permission : permissions){
			ComboBox comboBox = new ComboBox();
			Permission entity = accountManager.findSelectedPermission(id, permission.getId());
			if (entity != null) comboBox.setSelected(true);
			comboBox.setId(permission.getId());
			comboBox.setText(permission.getCaption());
			comboBoxs.add(comboBox);
		}
		return comboBoxs;
	}

	
	@RequiresPermissions("role:view")
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String, Object> query(@ModelAttribute QueryParameter params) {
		int page =  params.getPage() - 1;
		int pageSize = params.getRows();
		
		EntityQueryable query = queryFactory.createEntityQuery(Role.class).setPage(page).setRow(pageSize);

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
