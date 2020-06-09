package com.project.form.service;

import java.util.List;

import com.project.form.model.Employee;

public interface EmployeeService {

	Employee findById(String id);
	
	Employee findEmpByLogin(String login);
	
	List<Employee> findAll();
	
	void saveOrUpdate(Employee emp);
	
	void save(Employee emp);
	
	void delete(String id);

	List<Employee> findAllByType(String columnType, String salaryFrom, String salaryTo, int fromWhere, int toWhere);
	
	public int findAllByTypeSize(String columnType, String salaryFrom, String salaryTo);
	
}