package com.project.form.model;

import java.util.List;

public class searchForm {
	// form:hidden - hidden value
	String type;

	// form:input - textbox
	String minSalary;
	
	String maxSalary;
	
	String orderBy;
	
	String offset;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(String minSalary) {
		this.minSalary = minSalary;
	}

	public String getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(String maxSalary) {
		this.maxSalary = maxSalary;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}


//	@Override
//	public String toString() {
//		return "User [id=" + id + ", name=" + name + ", email=" + email + isNew();
//	}

}
