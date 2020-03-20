package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	} 
	 
	@Override
	public List<Employee> getAllEmployees() {
		List <Employee> employeeList = new ArrayList<>();
		String sqlGetAllEmployees = "SELECT * FROM employee";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllEmployees);
		while (results.next()) {
			Employee nextEmployee = new Employee();
			nextEmployee = mapRowToEmployee(results);
			employeeList.add(nextEmployee);
		}
		return employeeList;
	} 

	

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List <Employee> employeeList = new ArrayList<>();
		String sqlSearchEmployeesByName = "SELECT * " +
		     "FROM employee WHERE first_name = ? AND last_name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByName, firstNameSearch, lastNameSearch);
		while (results.next()) {
			Employee nextEmployee = new Employee();
			nextEmployee = mapRowToEmployee(results);
			employeeList.add(nextEmployee);		
			}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List <Employee> employeeList = new ArrayList<>();
		String sqlSearchEmployeesByDepartmentId = "SELECT * " +
		     "FROM employee WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByDepartmentId, id);
		while (results.next()) {
			Employee nextEmployee = new Employee();
			nextEmployee = mapRowToEmployee(results);
			employeeList.add(nextEmployee);		
			}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List <Employee> employeeList = new ArrayList<>();
		String sqlSearchEmployeesByName = "SELECT * FROM employee e WHERE e.employee_id NOT IN " +
				"(SELECT employee_id FROM project_employee)";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByName);
		while (results.next()) {
			Employee nextEmployee = new Employee();
			nextEmployee = mapRowToEmployee(results);
			employeeList.add(nextEmployee);		
			}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List <Employee> employeeList = new ArrayList<>();
		String sqlSearchEmployeesByProjectId = "SELECT * FROM employee e "
				+ "JOIN project_employee pe ON pe.employee_id = e.employee_id " +
				"WHERE pe.project_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByProjectId, projectId);
		while (results.next()) {
			Employee nextEmployee = new Employee();
			nextEmployee = mapRowToEmployee(results);
			employeeList.add(nextEmployee);		
			}
		return employeeList;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlChangeEmployeeDepartment = "UPDATE employee SET department_id = ? WHERE " +
				"employee_id = ?";
								
				jdbcTemplate.update(sqlChangeEmployeeDepartment,
							departmentId, employeeId);		
		
	}
	
	@Override
	public Employee createNewEmployee(Employee employee) {
	String sqlInsertEmployee = "INSERT INTO employee(employee_id, department_id, first_name, last_name, birth_date, " +
	"hire_date, gender) VALUES(?, ?, ?, ?, ?, ?, ?)";
		employee.setId(Long.valueOf(getNextEmployeeId()));
		
		jdbcTemplate.update(sqlInsertEmployee,
			employee.getId(),
			employee.getDepartmentId(), 
			employee.getFirstName(),
			employee.getLastName(),
			employee.getBirthDay(),
			employee.getHireDate(),
			employee.getGender());
			return employee;
		}
	
	
	private int getNextEmployeeId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_employee_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	} 
	private Employee mapRowToEmployee(SqlRowSet results) {
		Employee employee = new Employee();
		employee.setId(results.getLong("employee_id"));  
		employee.setDepartmentId(results.getLong("department_id"));
		employee.setFirstName(results.getString("first_name"));
		employee.setLastName(results.getString("last_name"));
		if(results.getDate("birth_date") != null) {
			employee.setBirthDay(results.getDate("birth_date").toLocalDate());
			}
		if(results.getDate("hire_date") != null) {
			employee.setHireDate(results.getDate("hire_date").toLocalDate());
		}
		employee.setGender(results.getString("gender").charAt(0));
		return employee;
	}



}
