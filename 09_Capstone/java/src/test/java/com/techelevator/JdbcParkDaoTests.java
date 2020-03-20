package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.Jdbc.JdbcParkDao;
import com.techelevator.models.Park;

public class JdbcParkDaoTests {
	
	private static SingleConnectionDataSource dataSource;
	private JdbcParkDao dao;

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
		dao = new JdbcParkDao(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void create_new_park_and_verify_increase_in_list_size() {
		int size = dao.getAllParks().size();
		Park newPark = new Park();
		newPark.setName("newPark");
		newPark.setArea(10000);
		newPark.setDescription("A really fun park");
		newPark.setVisitors(2);
		newPark.setEstablishedDate(LocalDate.now());
		newPark.setLocation("Tech Town");
		dao.createNewPark(newPark);
		
		
		assertEquals(size + 1, dao.getAllParks().size());
	}

	
 
} 
