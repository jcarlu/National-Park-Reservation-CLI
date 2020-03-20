package com.techelevator.models;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
	
	public List <Site> getSiteByCriteria(int campground_id, LocalDate fromDate, LocalDate toDate);
	


}
