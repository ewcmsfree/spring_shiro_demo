package com.ewcms.web.vo;

import java.io.Serializable;

public class ComboBox implements Serializable {
	private static final long serialVersionUID = 3457686163852753575L;

	private Object id;
	private Object text;
	private Boolean selected;

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getText() {
		return text;
	}

	public void setText(Object text) {
		this.text = text;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ComboBox other = (ComboBox) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
