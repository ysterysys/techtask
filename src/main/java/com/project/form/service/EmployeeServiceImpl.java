package com.project.form.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.form.dao.EmployeeDao;
import com.project.form.model.Employee;

@Service("userService")
public class EmployeeServiceImpl implements EmployeeService {

	EmployeeDao EmployeeDao;

	@Autowired
	public void setEmployeeDao(EmployeeDao EmployeeDao) {
		this.EmployeeDao = EmployeeDao;
	}

	@Override
	public Employee findById(String id) {
		return EmployeeDao.findById(id);
	}
	
	@Override
	public Employee findEmpByLogin(String login) {
		return EmployeeDao.findEmpByLogin(login);
	}

	@Override
	public List<Employee> findAll() {
		return EmployeeDao.findAll();
	}
	
	@Override
	public List<Employee> findAllByType(String sort , String salaryFrom, String salaryTo, int offset, int limit) {
		List<Employee> emps = EmployeeDao.findAllByType(sort, salaryFrom, salaryTo);
		List<Employee> result = new ArrayList<Employee>();
		
		System.out.println(" SQL VALRIABLE ");
		System.out.println(" SORT :"+sort);
		System.out.println("Salary from "+salaryFrom);
		System.out.println("Salary To"+salaryTo);
		System.out.println("offset" +offset);
		System.out.println("limit" +limit);
		int maxRecord = 0;

		if(emps.size()-offset >= limit ) {
			maxRecord = limit;
		}else {
			maxRecord = emps.size()-offset;
		}
		for (int x = 0; x < maxRecord; x++) {
			result.add(emps.get(x+offset));
		}
		
		return result;
	}
	
	@Override
	public int findAllByTypeSize(String sort , String salaryFrom, String salaryTo) {
		List<Employee> emps = EmployeeDao.findAllByType(sort, salaryFrom, salaryTo);
		return emps.size();
	}

	@Override
	public void saveOrUpdate(Employee emp) {

		if (findById(emp.getId())==null) {
			EmployeeDao.save(emp);
		} else {
			EmployeeDao.update(emp);
		}

	}
	
	@Override
	public void save(Employee emp) {

		EmployeeDao.save(emp);

	}

	@Override
	public void delete(String id) {
		EmployeeDao.delete(id);
	}

}