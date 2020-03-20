package com.techelevator.models;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> getAllParks();
	
	public Park getParkNameById(int parkId);
	
	public Park createNewPark(Park park);


}
