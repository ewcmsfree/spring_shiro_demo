package com.ewcms.security.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.security.dao.RoleDao;
import com.ewcms.security.dao.PermissionDao;
import com.ewcms.security.dao.UserDao;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;
import com.ewcms.security.model.User;
import com.ewcms.security.service.ShiroDbRealm.HashPassword;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class AccountManager {
	
	private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;

	// -- User Manager --//
	public User getUser(Long id) {
		return userDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveUser(User entity, Boolean isBasic) {
		if (!isBasic && entity.getId() != null && isSupervisor(entity.getId())) {
			logger.warn("操作员{}尝试修改超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能修改超级管理员用户");
		}
		//设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(entity.getPlainPassword()) && shiroRealm != null) {
			HashPassword hashPassword = shiroRealm.encrypt(entity.getPlainPassword());
			entity.setSalt(hashPassword.salt);
			entity.setPassword(hashPassword.password);
		}
		userDao.save(entity);
		if (shiroRealm != null) {
			shiroRealm.clearCachedAuthorizationInfo(entity.getLoginName());
		}
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userDao.delete(id);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	public List<User> getAllUser() {
		return (List<User>) userDao.findAll(new Sort(Direction.ASC, "id"));
	}

	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	// -- Role Manager --//
	public Role getRole(Long id) {
		return roleDao.findOne(id);
	}

	public List<Role> getAllRole() {
		return (List<Role>) roleDao.findAll((new Sort(Direction.ASC, "id")));
	}

	@Transactional(readOnly = false)
	public void saveRole(Role entity) {
		roleDao.save(entity);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteWithReference(id);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}
	
	public Role findSelectedRole(Long userId, Long roleId){
		return roleDao.findSelectedRole(userId, roleId);
	}
	
	public Role findRoleByRoleName(String roleName){
		return roleDao.findByRoleName(roleName);
	}
	
	public Role findRoleByCaption(String caption){
		return roleDao.findByCaption(caption);
	}
	
	//-- Permission Manager --//
	public Permission getPermission(Long id){
		return permissionDao.findOne(id);
	}
	
	public List<Permission> getAllPermission(){
		return (List<Permission>) permissionDao.findAll(new Sort(Direction.ASC, "id"));
	}
	
	public Permission findSelectedPermission(Long roleId, Long permissionId){
		return roleDao.findSelectedPermission(roleId, permissionId);
	}
	
	public Permission findPermissionByName(String name){
		return permissionDao.findByName(name);
	}
}
