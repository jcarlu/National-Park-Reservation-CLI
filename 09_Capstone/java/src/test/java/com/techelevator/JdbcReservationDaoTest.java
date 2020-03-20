package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.Jdbc.JdbcReservationDao;
import com.techelevator.models.Campground;
import com.techelevator.models.Reservation;

public class JdbcReservationDaoTest {
	private static SingleConnectionDataSource dataSource;
	private JdbcReservationDao dao;

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
		dao = new JdbcReservationDao(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test() {
		Reservation newReservation = getReservation(1, "MJ", LocalDate.of(2020, 04, 15), LocalDate.of(2020, 04, 19));
		dao.createReservation(newReservation);
		boolean foundFlag = false;
		List<Reservation> resList = dao.getAllReservations();
		for (Reservation reservation : resList) {
			if (reservation.getName().equals("MJ")) {
				foundFlag = true;
			}
		}
		assertEquals(true, foundFlag);
	} 
	

	private Reservation getReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
		Reservation theReservation = new Reservation(siteId, name, fromDate, toDate);
		theReservation.setCreateDate(LocalDate.now());
		return theReservation;
	}

}
