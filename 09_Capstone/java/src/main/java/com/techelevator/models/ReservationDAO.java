package com.techelevator.models;

import java.util.List;

public interface ReservationDAO {
	
	public int createReservation(Reservation newReservation);
	
	public List<Reservation> getAllReservations();

	

}
