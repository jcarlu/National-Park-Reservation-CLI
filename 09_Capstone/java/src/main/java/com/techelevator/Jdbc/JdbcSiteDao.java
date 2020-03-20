package com.techelevator.Jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.models.Site;
import com.techelevator.models.SiteDAO;

public class JdbcSiteDao implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcSiteDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		} 

	@Override
	public List<Site> getSiteByCriteria(int campgroundId, LocalDate fromDate, LocalDate toDate) {
		List <Site> siteList = new ArrayList <> ();
		String sqlGetAllSites = "SELECT * FROM site s WHERE campground_id = ? AND " +
		"site_id NOT IN (SELECT r.site_id FROM reservation r WHERE " +
		"(? BETWEEN r.from_date AND r.to_date) "
		+ "OR (? BETWEEN r.from_date AND r.to_date) OR "
		+ "(? < r.from_date AND ? > r.to_date)) LIMIT 5;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites, campgroundId, fromDate, toDate, fromDate, toDate);
		while(results.next()) {
			Site nextSite = mapRowToSite(results);
			siteList.add(nextSite);
		}
		return siteList;
	}
	

	private Site mapRowToSite(SqlRowSet results) {
		Site site = new Site();
		site.setSiteId(results.getInt("site_id"));  
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setHandicapAccess(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setHasUtilities(results.getBoolean("utilities"));
		return site;

	}

		
}
