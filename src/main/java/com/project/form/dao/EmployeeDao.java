package com.project.form.dao;

import java.util.List;

import com.project.form.model.Employee;

public interface EmployeeDao {

	Employee findById(String id);
	
	Employee findEmpByLogin(String login);

	List<Employee> findAll();

	void save(Employee emp);

	void update(Employee emp);

	void delete(String id);

	List<Employee> findAllByType(String columnType, String salaryFrom, String salaryTo);

}