package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCEmployeeDAOTesting {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;
	private JDBCDepartmentDAO dao2;

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/department_projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		dataSource.setAutoCommit(false);

	}

	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
		
	}

	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		dao = new JDBCEmployeeDAO(dataSource);
		dao2 = new JDBCDepartmentDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void return_all_employees_size_increment_by_two() {
		int size = dao.getAllEmployees().size();
		Department newDep = new Department();
		newDep.setName("newDep");
		dao2.createDepartment(newDep);
		Employee emp1 = getEmployee(newDep.getId(), "James", "Carlu", LocalDate.of(1990, 11, 18), 'M', LocalDate.of(2016, 06, 19));
		dao.createNewEmployee(emp1);
		Employee emp2 = getEmployee(newDep.getId(), "Casey", "Tek", LocalDate.of(1993, 12, 18), 'M', LocalDate.of(2018, 06, 30));
		dao.createNewEmployee(emp2);
		
		assertEquals(size + 2, dao.getAllEmployees().size());
	}
	
	@Test
	public void name_search_returns_employee_with_name() {
		Department newDep = new Department();
		newDep.setName("newDep");
		dao2.createDepartment(newDep);
		Employee emp1 = getEmployee(newDep.getId(), "James", "Carlu", LocalDate.of(1990, 11, 18), 'M', LocalDate.of(2016, 06, 19));
		dao.createNewEmployee(emp1);
		boolean foundFlag = false;
		List<Employee> empList = dao.searchEmployeesByName("James", "Carlu");
		for (Employee employee : empList) {
			if (employee.getFirstName().equals("James") && employee.getLastName().equals("Carlu")) {
				foundFlag = true;
			}
		}
		assertEquals(true, foundFlag);
	} 
	
	@Test
	public void returns_employees_by_dept_id() {
		Department newDep = new Department(); 
		newDep.setName("newDep");
		dao2.createDepartment(newDep);
		Employee emp1 = getEmployee(newDep.getId(), "James", "Carlu", LocalDate.of(1990, 11, 18), 'M', LocalDate.of(2016, 06, 19));
		dao.createNewEmployee(emp1);
		Employee emp2 = getEmployee(newDep.getId(), "Casey", "Tek", LocalDate.of(1993, 12, 18), 'M', LocalDate.of(2018, 06, 30));
		dao.createNewEmployee(emp2);
	
		List<Employee> empList = dao.getEmployeesByDepartmentId(emp1.getDepartmentId());
		boolean isJamesThere = false;
		boolean isCaseyThere = false;
		boolean combined = false;
		for(Employee employee: empList) {
			if (employee.getFirstName().equals("James") && employee.getLastName().equals("Carlu")) {
				isJamesThere = true;
			}
			if (employee.getFirstName().equals("Casey") && employee.getLastName().equals("Tek")) {
				isCaseyThere = true;
			}
			if (isJamesThere && isCaseyThere) {
				combined = true;
			}
		}
		
		assertEquals(true, combined);
	}

	@Test
	public void returns_employee_without_project() {
		Department newDep = new Department();
		newDep.setName("newDep");
		dao2.createDepartment(newDep);
		Employee emp1 = getEmployee(newDep.getId(), "James", "Carlu", LocalDate.of(1990, 11, 18), 'M', LocalDate.of(2016, 06, 19));
		dao.createNewEmployee(emp1);
		boolean foundFlag = false;
		List<Employee> empList = dao.getEmployeesWithoutProjects();
		for (Employee employee : empList) {
			if (employee.getFirstName().equals("James") && employee.getLastName().equals("Carlu")) {
				foundFlag = true;
			}
		}
		assertEquals(true, foundFlag);
	} 
	
	@Test
	public void change_employee_department() {
		Department newDep = new Department();
		newDep.setName("newDep");
		dao2.createDepartment(newDep);
		Department newerDep = new Department();
		newerDep.setName("newerDep");
		dao2.createDepartment(newerDep);
		Employee emp1 = getEmployee(newDep.getId(), "James", "Carlu", LocalDate.of(1990, 11, 18), 'M', LocalDate.of(2016, 06, 19));
		dao.createNewEmployee(emp1);
		dao.changeEmployeeDepartment(emp1.getId(), newerDep.getId());
		boolean foundFlag = false;
		List<Employee> empList = dao.getEmployeesByDepartmentId(newerDep.getId());
		for (Employee employee : empList) {
			if (employee.getFirstName().equals("James") && employee.getLastName().equals("Carlu")) {
				foundFlag = true;
			}
		}
		assertEquals(true, foundFlag);
	} 
	
	private Employee getEmployee(Long departmentId, String firstName, String lastName, LocalDate birthday, char gender, LocalDate hireDate) {
		Employee theEmployee = new Employee();
		theEmployee.setDepartmentId(departmentId);
		theEmployee.setFirstName(firstName);
		theEmployee.setLastName(lastName);
		theEmployee.setBirthDay(birthday);
		theEmployee.setGender(gender);
		theEmployee.setHireDate(hireDate);
		return theEmployee;
	} 

}
