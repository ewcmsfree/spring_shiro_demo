package com.ewcms.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ewcms.util.Collections3;

/**
 * 角色
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>roleName:角色名称</li>
 * <li>caption:说明</li>
 * <li>permissins:权限集合对象</li>
 * </ul>
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "acct_role")
@SequenceGenerator(name = "seq_acc_role", sequenceName = "seq_acct_role_id", allocationSize = 1)
public class Role implements Serializable {

	private static final long serialVersionUID = -6368774703931624240L;
		
	@Id
    @GeneratedValue(generator = "seq_acc_role",strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "role_name", nullable = false, unique = true)
	private String roleName;
	@Column(name = "caption", nullable = false, unique = true)
	private String caption;
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "acct_role_permission", joinColumns =@JoinColumn(name = "role_id"),inverseJoinColumns =@JoinColumn(name = "permission_id"))
    @OrderBy("id")
	private Set<Permission> permissions = new HashSet<Permission>();
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String RoleName) {
		this.roleName = RoleName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	@Transient
	public String getPermissionNames() {
		return Collections3.extractToString(permissions, "caption", ", ");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
}
