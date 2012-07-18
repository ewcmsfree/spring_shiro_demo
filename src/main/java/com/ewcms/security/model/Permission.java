package com.ewcms.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 权限
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>name:名称</li>
 * <li>expression:表达式</li>
 * <li>caption:说明</li>
 * </ul>
 * 
 * @author wuzhijun
 */
@Entity
@Table(name = "acct_permission")
@SequenceGenerator(name = "seq_acc_permission", sequenceName = "seq_acct_permission_id", allocationSize = 1)
public class Permission implements Serializable {

	private static final long serialVersionUID = 6992071754156726733L;

	@Id
	@GeneratedValue(generator = "seq_acc_permission", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "expression", nullable = false, unique = true)
	private String expression;
	@Column(name = "caption", nullable = false, unique = true)
	private String caption;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Permission other = (Permission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
