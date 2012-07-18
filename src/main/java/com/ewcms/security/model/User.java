package com.ewcms.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ewcms.util.Collections3;

/**
 * 用户信息
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>loginName:登录名</li>
 * <li>password:密码</li>
 * <li>realName:用户名</li>
 * <li>email:邮箱</li>
 * <li>status:使用状态</li>
 * <li>roles:角色对象集合</li>
 * <li>permissions:权限对象集合</li>
 * </ul>
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "acct_user")
@SequenceGenerator(name = "seq_acc_user", sequenceName = "seq_acct_user_id", allocationSize = 1)
public class User implements Serializable {

	private static final long serialVersionUID = 4456583773180927749L;

	@Id
    @GeneratedValue(generator = "seq_acc_user",strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "login_name", nullable = false, unique = true, length = 20)
	private String loginName;
	@Transient
	private String plainPassword;
	@Column(name = "pass_word")
	private String password;// 为简化演示使用明文保存的密码
	@Column(name = "salt")
    private String salt;
	@Column(name = "real_name", nullable = false)
	private String realName;
	@Column(name = "e_mail")
	private String email;
	@Column(name = "status")
	private Boolean status;
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "acct_user_role", joinColumns =@JoinColumn(name = "user_id"),inverseJoinColumns =@JoinColumn(name = "role_id"))
    @OrderBy("id")
	private Set<Role> roles = new HashSet<Role>();
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "acct_user_permission", joinColumns =@JoinColumn(name = "user_id"),inverseJoinColumns =@JoinColumn(name = "permission_id"))
    @OrderBy("id")
    private Set<Permission> permissions = new HashSet<Permission>();

    public User(){
    	status = false;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Transient
	public String getRoleNames() {
		return Collections3.extractToString(roles, "caption", ", ");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginName == null) ? 0 : loginName.hashCode());
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
		User other = (User) obj;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}
}
