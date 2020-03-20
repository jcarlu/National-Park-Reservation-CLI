package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
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
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCDepartmentDAOTesting {

	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentDAO dao;

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
		dao = new JDBCDepartmentDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	 
	@Test
	public void return_all_departments_size_increment_by_two() {
		int size = dao.getAllDepartments().size();
		Department dep1 = getDepartment("dep1");
		dao.createDepartment(dep1);
		Department dep2 = getDepartment("dep2");
		dao.createDepartment(dep2);
		
		assertEquals(size + 2, dao.getAllDepartments().size());
	}

	@Test
	public void create_department_and_read_it_back() {
		Department theDep = getDepartment("theSales");
		dao.createDepartment(theDep);
		Department savedDep = dao.getDepartmentById(theDep.getId());
		// assertNotEquals(null, theDep.getId());
		assertDepartmentsAreEquals(theDep, savedDep);
	}

	@Test
	public void update_department_and_read_it_back() {

		Department newDept = dao.createDepartment(getDepartment("Department Of Fun"));
		assertEquals("Department Of Fun", newDept.getName());
		newDept.setName("Department Of Not Fun");
		boolean foundFlag = false;
		dao.saveDepartment(newDept);
		List<Department> deptList = dao.searchDepartmentsByName("Department Of Not Fun");

		for (Department dept : deptList) {

			if (dept.getName().equals("Department Of Not Fun")) {
				foundFlag = true;
			}
		}

		assertEquals(true, foundFlag);
	}
	
	@Test
	public void word_search_returns_department_with_word() {
		Department newDept = dao.createDepartment(getDepartment("New Department"));	
		boolean foundFlag = false;
		List<Department> deptList = dao.searchDepartmentsByName("New Department");

		for (Department dept : deptList) {
			if (dept.getName().equals("New Department")) {
				foundFlag = true;
			}
		}
		assertEquals(true, foundFlag);
	} 
	
	@Test
	public void returns_department_by_id() {
		Department theDep = getDepartment("theSales");
		dao.createDepartment(theDep);
		Department savedDep = dao.getDepartmentById(theDep.getId());
		assertDepartmentsAreEquals(theDep, savedDep);
	}

	private void assertDepartmentsAreEquals(Department theDep, Department savedDep) {
		// asserts to make sure each attribute is the same for the cities to be equal
		assertEquals(theDep.getId(), savedDep.getId());
		assertEquals(theDep.getName(), savedDep.getName());
	}

	private Department getDepartment(String name) {
		Department theDep = new Department();
		theDep.setName(name);

		return theDep;
	} 
 
}
