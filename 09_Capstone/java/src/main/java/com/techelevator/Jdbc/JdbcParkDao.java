package com.techelevator.Jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.models.Park;
import com.techelevator.models.ParkDAO;

public class JdbcParkDao implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcParkDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		} 
  
	@Override
	public List<Park> getAllParks() {
		List <Park> parkList = new ArrayList <> ();
		String sqlGetAllParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while(results.next()) {
			Park nextPark = mapRowToPark(results);
			parkList.add(nextPark);
		}
		return parkList;
	}
	
	public Park getParkNameById(int parkId) {
		Park park = null;
		String sqlFindParkById = "SELECT * " + 
		"FROM park WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParkById, parkId);
			if(results.next()) {
				park = mapRowToPark(results);
			}
			return park;		
	}
	
	@Override
	public Park createNewPark(Park park) {
	String sqlInsertPark = "INSERT INTO park(name, location, establish_date, area, " +
	"visitors, description) VALUES(?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sqlInsertPark,
			park.getName(), 
			park.getLocation(),
			park.getEstablishedDate(),
			park.getArea(),
			park.getVisitors(),
			park.getDescription());
			return park;
		}
	
	
	
	
	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkId(results.getInt("park_id"));  
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishedDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;

	}

}
