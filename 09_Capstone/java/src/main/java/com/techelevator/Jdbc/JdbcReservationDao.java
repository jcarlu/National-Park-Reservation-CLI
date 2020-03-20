package com.techelevator.Jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.models.Reservation;
import com.techelevator.models.ReservationDAO;


public class JdbcReservationDao implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcReservationDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		} 

	
	@Override
	public int createReservation(Reservation newReservation) {
	String sqlCreateReservation = ("INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
			+ "VALUES(?, ?, ?, ?, ?)");
	
	LocalDate date = LocalDate.now();	
	
	
	jdbcTemplate.update(sqlCreateReservation,
			newReservation.getSiteId(),
			newReservation.getName(),
			newReservation.getFromDate(),
			newReservation.getToDate(),
			date);
	return newReservation.getReservationId();
	}

	
	public int getMaxReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT MAX(reservation_id) " +
	 "FROM reservation");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	} 
	
	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> resList = new ArrayList<>();
		String sqlGetAllReservations = "SELECT * FROM reservation";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllReservations);
		while (results.next()) {
			Reservation nextReservation = new Reservation();
				nextReservation = mapRowToReservation(results);
				resList.add(nextReservation);		
				}
			return resList; 
	} 
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setSiteId(results.getInt("site_id"));  
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		reservation.setCreateDate(LocalDate.now());
		return reservation;
	}
	
}
	



