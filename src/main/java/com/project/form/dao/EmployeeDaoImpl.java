package com.project.form.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.form.model.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
			throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Employee findById(String id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT * FROM emp WHERE id=:id";

		Employee result = null;
		try {
			result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}

		return result;

	}

	@Override
	public Employee findEmpByLogin(String login) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);

		String sql = "SELECT * FROM emp WHERE login=:login";

		Employee result = null;
		try {
			result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}

		return result;

	}

	@Override
	public List<Employee> findAll() {

		String sql = "SELECT * FROM emp";
		List<Employee> result = namedParameterJdbcTemplate.query(sql, new UserMapper());

		return result;

	}

	@Override
	public List<Employee> findAllByType(String sort, String salaryFrom, String salaryTo) {
		String sortBy = null;

		if (sort.charAt(0) == '-') {
			sortBy = "desc";
		} else {
			sortBy = "asc";
		}

		String column = sort.substring(1, sort.length());

		String sql = "SELECT * FROM emp where salary between " + salaryFrom + " and " + salaryTo + " order by " + column
				+ " " + sortBy;

		List<Employee> result = namedParameterJdbcTemplate.query(sql, new UserMapper());

		return result;

	}

	@Override
	public void save(Employee emp) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql = "INSERT INTO EMP(ID, LOGIN,NAME,SALARY) " + "VALUES ( :id, :login, :name, :salary)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(emp), keyHolder);
		emp.setId(emp.getId());
	}

	@Override
	public void update(Employee emp) {

		String sql = "UPDATE emp SET ID=:id, LOGIN=:login, NAME=:name , SALARY=:salary WHERE id=:id";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(emp));

	}

	@Override
	public void delete(String id) {

		String sql = "DELETE FROM emp WHERE id= :id";
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));

	}

	private SqlParameterSource getSqlParameterByModel(Employee emp) {

		// Unable to handle List<String> or Array
		// BeanPropertySqlParameterSource

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", emp.getId());
		paramSource.addValue("login", emp.getLogin());
		paramSource.addValue("name", emp.getName());
		paramSource.addValue("salary", emp.getSalary());

		return paramSource;
	}

	private static final class UserMapper implements RowMapper<Employee> {

		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee user = new Employee();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setLogin(rs.getString("login"));
			user.setSalary(rs.getString("salary"));
			return user;
		}
	}

}