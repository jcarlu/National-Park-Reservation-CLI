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
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectDaoTesting {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;
	private JDBCEmployeeDAO dao2;
	private JDBCDepartmentDAO dao3;

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
		dao = new JDBCProjectDAO(dataSource);
		dao2 = new JDBCEmployeeDAO(dataSource);
		dao3 = new JDBCDepartmentDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	
	@Test
	public void return_all_active_projects_size_increment_by_two() {
		int size = dao.getAllActiveProjects().size();
		Project project1 = getProject("project1", LocalDate.now(), LocalDate.of(2020, 12, 12));
		dao.addProject(project1);
		Project project2 = getProject("project2", LocalDate.now(), LocalDate.of(2020, 12, 13));
		dao.addProject(project2);
		assertEquals(size + 2, dao.getAllActiveProjects().size());
	} 

	@Test 
	public void add_employee_to_project_and_verify() {
		Department newDep = new Department();
		newDep.setName("newDep");
		dao3.createDepartment(newDep);
		Employee newEmployee = new Employee();
		newEmployee.setDepartmentId(Long.parseLong("1"));
		newEmployee.setFirstName("Casey");
		newEmployee.setLastName("Tek");
		newEmployee.setBirthDay(LocalDate.of(1993, 12, 18));
		newEmployee.setHireDate(LocalDate.now());
		newEmployee.setGender('F'); 
		dao2.createNewEmployee(newEmployee);
		Project newProject = new Project();
		newProject.setName("Fun");
		newProject.setStartDate(LocalDate.now());
		newProject.setEndDate(LocalDate.of(2020, 04, 01));
		dao.addProject(newProject);
		
		dao.addEmployeeToProject(newProject.getId(), newEmployee.getId());
		
		List <Employee> employeeList = dao2.getEmployeesByProjectId(newProject.getId());
		
		boolean isCasey = false;
		for(Employee employee: employeeList){
			if(employee.getFirstName().equals("Casey")) {
				isCasey = true;
			}
		}
		assertEquals(true, isCasey);
	}
	
	@Test
	public void remove_employee_from_project_and_verify() {
		Department newDep = new Department();
		newDep.setName("newDep");
		dao3.createDepartment(newDep);
		Employee newEmployee = new Employee();
		newEmployee.setDepartmentId(Long.parseLong("1"));
		newEmployee.setFirstName("Casey");
		newEmployee.setLastName("Tek");
		newEmployee.setBirthDay(LocalDate.of(1993, 12, 18));
		newEmployee.setHireDate(LocalDate.now());
		newEmployee.setGender('F'); 
		dao2.createNewEmployee(newEmployee);
		Project newProject = new Project();
		newProject.setName("Fun");
		newProject.setStartDate(LocalDate.now());
		newProject.setEndDate(LocalDate.of(2020, 04, 01));
		dao.addProject(newProject);
		
		dao.addEmployeeToProject(newProject.getId(), newEmployee.getId());
		
		List <Employee> employeeList = dao2.getEmployeesByProjectId(newProject.getId());
		
		boolean isCasey = false;
		for(Employee employee: employeeList){
			if(employee.getFirstName().equals("Casey")) {
				isCasey = true;
			}
		}
		assertEquals(true, isCasey);
	
		dao.removeEmployeeFromProject(newProject.getId(), newEmployee.getId());
		List <Employee> employeeListRemoved = dao2.getEmployeesByProjectId(newProject.getId());
		
		boolean isCaseyStillThere = false;
		for(Employee employee: employeeListRemoved){
			if(employee.getFirstName().equals("Casey")) {
				isCaseyStillThere = true;
			}
		}
		
		assertEquals(false, isCaseyStillThere);
	}
	

	private Project getProject(String name, LocalDate startDate, LocalDate endDate) {
		Project theProject = new Project();
		theProject.setName(name);
		theProject.setStartDate(startDate);
		theProject.setEndDate(endDate);

		return theProject;
	}  
  

}
