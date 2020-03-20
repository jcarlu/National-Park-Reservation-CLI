package com.techelevator.Jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.models.Campground;
import com.techelevator.models.CampgroundDAO;

public class JdbcCampgroundDao implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcCampgroundDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		} 

	@Override
	public List<Campground> getAllCampgroundsByParkId(int parkId) {
		List <Campground> campgroundList = new ArrayList <> ();
		String sqlFindCampgroundByParkId = "SELECT * " + 
		"FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundByParkId, parkId);
			while(results.next()) {
				campgroundList.add(mapRowToCampground(results));
			}
			return campgroundList;	
	} 
	
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setCampgroundID(results.getInt("campground_id"));
		campground.setParkID(results.getInt("park_id"));  
		campground.setOpenMonth(results.getInt("open_from_mm"));
		campground.setCloseMonth(results.getInt("open_to_mm"));
		campground.setDailyFee(results.getBigDecimal("daily_fee").doubleValue());
		campground.setName(results.getString("name"));
		
		return campground;
	}

}
