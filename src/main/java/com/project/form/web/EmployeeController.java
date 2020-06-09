package com.project.form.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.form.model.Employee;
import com.project.form.model.searchForm;
import com.project.form.service.EmployeeService;

@Controller
public class EmployeeController {

	private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);



	private EmployeeService employeeService;

	@Autowired
	public void setUserService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		logger.debug("index()");
		return "redirect:/emp";
	}

	@RequestMapping(value = "/emp/dashboard", method = RequestMethod.GET)
	public String indexDashBoard(Model model) {
		logger.debug("index()");

		searchForm sf = new searchForm();
		sf.setMinSalary("");
		sf.setMaxSalary("");
		sf.setType("login");

		model.addAttribute("userSearchForm", sf);

		return "emp/dashboard";
	}

	// list page
	@RequestMapping(value = "/emp/searchUserBySalary", method = RequestMethod.GET)
	public String searchUser(@ModelAttribute("userSearchForm") searchForm searchForm, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("showAllUsers()");
		model.addAttribute("users", employeeService.findAllByType(searchForm.getOrderBy() + searchForm.getType(),
				searchForm.getMinSalary(), searchForm.getMaxSalary(), 0, 30));

		int totalSize = employeeService.findAllByTypeSize(searchForm.getOrderBy() + searchForm.getType(),
				searchForm.getMinSalary(), searchForm.getMaxSalary());
		model.addAttribute("startpage", 1);
		model.addAttribute("endpage", Math.ceil(totalSize / 30.0));

		model.addAttribute("minSalary", searchForm.getMinSalary());
		model.addAttribute("maxSalary", searchForm.getMaxSalary());
		model.addAttribute("type", searchForm.getType());
		model.addAttribute("orderBy", searchForm.getOrderBy());
		// model.addAttribute("sort",searchForm.getOrderBy()+searchForm.getType());
		System.out.println("AFTER CEILING");
		System.out.println(Math.ceil(totalSize / 30));
		return "emp/dashboard";
	}

	@RequestMapping(value = "/emp/searchUserBySalaryByPage", method = RequestMethod.GET)
	public String searchUserByPage(@ModelAttribute("userSearchForm") searchForm searchForm,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "minSalary", required = false) String minSalary,
			@RequestParam(value = "maxSalary", required = false) String maxSalary,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "orderBy", required = false) String orderBy, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.debug("showAllUsers()");
		model.addAttribute("users",
				employeeService.findAllByType(orderBy + type, minSalary, maxSalary, (page - 1) * 30, 30));

		int totalSize = employeeService.findAllByTypeSize(orderBy + type, minSalary, maxSalary);
		model.addAttribute("startpage", 1);
		model.addAttribute("endpage", Math.ceil(totalSize / 30.0));
		model.addAttribute("minSalary", searchForm.getMinSalary());
		model.addAttribute("maxSalary", searchForm.getMaxSalary());
		model.addAttribute("type", searchForm.getType());
		model.addAttribute("orderBy", searchForm.getOrderBy());

		return "emp/dashboard";
	}

	// list page
	@RequestMapping(value = "/emp", method = RequestMethod.GET)
	public String showAllUsers(Model model) {

		logger.debug("showAllUsers()");
		model.addAttribute("users", employeeService.findAll());
		return "emp/list";

	}

	// save or update user
	@RequestMapping(value = "/emp/savefile", method = RequestMethod.POST)

	public String userFileUpload(@RequestParam("file") MultipartFile[] files, final RedirectAttributes redirectAttributes,
			HttpServletResponse response) throws Exception {
		logger.debug("file upload()");
		for(MultipartFile file: files ) {
		if(file.getBytes().length <=3) {
			throw new Exception("File is Empty ! ");
		};
		logger.debug("nunmber of times");
		BufferedReader br;
		try {
			String line;
			InputStream is = file.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			logger.debug("reading file");
			// skip first line
			br.readLine();
			while ((line = br.readLine()) != null) {
		
				String[] values = line.split(",");
				if (values[0].charAt(0) == '#') {
					continue;
				}
				if(values.length>4) {
					throw new Exception("record contain incorrect number of fields");
				}
				if(values.length<4) {
					throw new Exception("record contain incorrect number of fields");
				}
				Employee emp = new Employee();
				emp.setId(values[0]);
				emp.setLogin(values[1]);
				emp.setName(values[2]);
				if(Integer.parseInt(values[3])>=0) {
					emp.setSalary(values[3]);
				}else {
					throw new Exception("Salary must be >= 0");
				}
				
				if (validateInput(emp)) {
					employeeService.saveOrUpdate(emp);
				
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					throw new Exception("User Login already existed, file will not be processed");
				}

			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
		}
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "File Uploaded successfully!");
		return "redirect:/emp";

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

	// save or update user
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	public String saveOrUpdateUser(@ModelAttribute("userForm") Employee employee, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {

		logger.debug("saveOrUpdateUser() : {}", employee);

		try {
			redirectAttributes.addFlashAttribute("css", "success");
			
			employeeService.saveOrUpdate(employee);
			
			if (employee.isNew()) {
				redirectAttributes.addFlashAttribute("msg", "User added successfully!");
			} else {
				redirectAttributes.addFlashAttribute("msg", "User updated successfully!");
			}

			
			return "redirect:/emp/" + employee.getId();
		}catch(Exception e) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "Error ! Unable to update");
			return "redirect:/emp/" + employee.getId();
		}
			


	}

	// show update form
	@RequestMapping(value = "/emp/{id}/update", method = RequestMethod.GET)
	public String showUpdateUserForm(@PathVariable("id") String id, Model model) {

		logger.debug("showUpdateUserForm() : {}", id);

		Employee emp = employeeService.findById(id);
		model.addAttribute("userForm", emp);

		return "emp/userform";

	}
	
	// show update form
	// delete user
	// delete user
		@RequestMapping(value = "/emp/{id}/delete", method = RequestMethod.POST)
		public String deleteUser(@PathVariable("id") String id, final RedirectAttributes redirectAttributes) {

			logger.debug("deleteUser() : {}", id);

			employeeService.delete(id);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "User is deleted!");
			
			return "redirect:/emp/dashboard";

		}
	

	// show user
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	public String showUser(@PathVariable("id") String id, Model model) {

		logger.debug("showUser() id: {}", id);

		Employee emp = employeeService.findById(id);
		if (emp == null) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "User not found");
		}
		model.addAttribute("user", emp);

		return "emp/show";

	}


	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ModelAndView handleEmptyData(HttpServletRequest req, Exception ex) {

		logger.debug("handleEmptyData()");
		logger.error("Request: {}, error ", req.getRequestURL(), ex);

		ModelAndView model = new ModelAndView();
		model.setViewName("emp/show");
		model.addObject("msg", "user not found");

		return model;

	}


//	@RequestMapping(value = "/emp", method = RequestMethod.GET, produces = "application/json")
//	@ResponseBody
//	public List<Employee> searchUser(@RequestParam(value = "minSalary") String minSalary,
//			@RequestParam(value = "maxSalary") String maxSalary, @RequestParam(value = "offset") int offset,
//			@RequestParam(value = "limit") int limit, @RequestParam(value = "sort") String sort,
//			HttpServletResponse response) {
//		logger.debug("userss11 list()");
//		System.out.println(minSalary);
//		try {
//			return employeeService.findAllByType(sort, minSalary, maxSalary, offset, limit);
//		} catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return null;
//		}
//		
//
//	}
	
	@ModelAttribute("sortList")
	   public Map<String, String> getSortList() {
	      Map<String, String> sortList = new HashMap<String, String>();
	      sortList.put("+", "Ascending");
	      sortList.put("-", "Descending");
	     
	      return sortList;
	   }

	
	@ModelAttribute("columnList")
	   public Map<String, String> getColumnList() {
	      Map<String, String> columnList = new HashMap<String, String>();
	      columnList.put("id", "ID");
	      columnList.put("name", "NAME");
	      columnList.put("login", "LOGIN");
	      columnList.put("salary", "SALARY");
	     
	      return columnList;
	   }
}