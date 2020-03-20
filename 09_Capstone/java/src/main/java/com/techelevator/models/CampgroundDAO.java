package com.techelevator.models;

import java.util.List;

public interface CampgroundDAO {
	
	public List<Campground> getAllCampgroundsByParkId(int parkId);

}
