package com.ewcms.security.dao;

import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;

public interface RoleDaoCustom {
	/**
	 * 因为Group中没有建立与User的关联,因此需要以较低效率的方式进行删除User与Group的多对多中间表中的数据.
	 */
	void deleteWithReference(Long id);
	
	Role findSelectedRole(Long userId, Long roleId);
	
	Permission findSelectedPermission(Long roleId, Long permissionId);
}
