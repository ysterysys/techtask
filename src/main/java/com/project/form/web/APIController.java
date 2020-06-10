package com.project.form.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.form.model.Employee;
import com.project.form.service.EmployeeService;

@Controller
public class APIController {

	private final Logger logger = LoggerFactory.getLogger(APIController.class);

	private EmployeeService employeeService;

	@Autowired
	public void setUserService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	///////////////////// THIS IS CREATED FOR API HANDLING TEST
	///////////////////// //////////////////////////////////////////////////////

	@RequestMapping(value = "/users/upload", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public void upload(@RequestParam("file") MultipartFile inputFile, HttpServletResponse response) {
		logger.debug("file upload()");
		InputStream is = null; 
		InputStreamReader isr = null;
	     
		BufferedReader br = null;
		try {
			
			String line;
			is = inputFile.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			logger.debug("reading file");
			// skip first line
			br.readLine();
			while ((line = br.readLine()) != null) {
				
				String[] values = line.split(",");
				if (values[0].charAt(0) == '#') {
					continue;
				}

				if (values.length > 4) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				if (values.length < 4) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				Employee emp = new Employee();
				emp.setId(values[0]);
				emp.setLogin(values[1]);
				emp.setName(values[2]);
				emp.setSalary(values[3]);
				if (validateInput(emp)) {
					employeeService.saveOrUpdate(emp);
					System.out.println("Employee Inserted");
					System.out.println(emp.getName());
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;

				}
				
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				is.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	// list page
	// user story 2
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> searchUserAPI(@RequestParam(value = "minSalary") String minSalary,
			@RequestParam(value = "maxSalary") String maxSalary, @RequestParam(value = "offset") int offset,
			@RequestParam(value = "limit") int limit, @RequestParam(value = "sort") String sort,
			HttpServletResponse response) {
		logger.debug("User list API()");
		try {
			if (limit >= 30) {
				limit = 30;
			}
			return employeeService.findAllByType(sort, minSalary, maxSalary, offset, limit);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

	// user story 3

	// create user api
	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void addUserAPI(@PathVariable(value = "id") String id, @RequestParam(value = "name") String name,
			@RequestParam(value = "login") String login, @RequestParam(value = "salary") String salary,
			HttpServletResponse response) {

		logger.debug("User information: " + id + " " + name + " " + " " + login + " " + salary);
		Employee employee = new Employee();
		try {
			employee.setId(id);
			employee.setLogin(login);
			employee.setName(name);
			if (Integer.parseInt(salary) >= 0) {
				employee.setSalary(salary);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			employeeService.saveOrUpdate(employee);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

	}

	// delete user api
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public void deleteUserAPI(@PathVariable(value = "id") String id, HttpServletResponse response) {

		logger.debug("deleteUser() : {}", id);
		try {
			employeeService.delete(id);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	// get user api
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee getUserAPI(@PathVariable(value = "id") String id, HttpServletResponse response) {
		logger.debug("RETRIEVE USER API () : {}", id);
		try {
			return employeeService.findById(id);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

	}

	// update user api
	@RequestMapping(value = "/users/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
	public void patchUserAPI(@PathVariable(value = "id") String id, @RequestParam(value = "name") String name,
			@RequestParam(value = "login") String login, @RequestParam(value = "salary") String salary,
			HttpServletResponse response) {

		logger.debug("patchUser API : {}", id);
		try {
			Employee employee = employeeService.findById(id);
			if (employee != null) {
				Employee existingLogin = employeeService.findEmpByLogin(employee.getLogin());
				if (existingLogin == null || existingLogin.getId().equals(employee.getId())) {
					employee.setId(id);
					employee.setLogin(login);
					employee.setName(name);
					employee.setSalary(salary);
					employeeService.saveOrUpdate(employee);
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	public boolean validateInput(Employee employee) {
		// boolean valid = false;
		if (!employee.getName().isEmpty() && employee.getId() != null && !employee.getLogin().isEmpty()
				&& !employee.getSalary().isEmpty()) {

		} else {
			return false;
		}

		Employee findExisting = employeeService.findEmpByLogin(employee.getLogin());

		if (findExisting == null) {
			return true;
		}

		return false;
	}
}