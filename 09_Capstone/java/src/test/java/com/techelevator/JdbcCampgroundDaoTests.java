package com.techelevator;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.Jdbc.JdbcCampgroundDao;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class JdbcCampgroundDaoTests {
	
	private static SingleConnectionDataSource dataSource;
	private JdbcCampgroundDao dao;

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
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
		dao = new JdbcCampgroundDao(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

//	@Test
//	public void test() {
//		int size = dao.getAllCampgroundsByParkId().size();
//		Campground camp1 = getCampground("project1", LocalDate.now(), LocalDate.of(2020, 12, 12));
//		dao.addCampground(camp1);
//		Project project2 = getProject("project2", LocalDate.now(), LocalDate.of(2020, 12, 13));
//		dao.addProject(project2);
//		assertEquals(size + 2, dao.getAllActiveProjects().size());
//	} 
//	
//	private Campground getCampground(int ParkId, String name, LocalDate startDate, LocalDate endDate) {
//		Project theProject = new Project();
//		theProject.setParkId(name);
//		theProject.setName(name);
//		theProject.setStartDate(startDate);
//		theProject.setEndDate(endDate);
//		return theProject;
//	}  

}
