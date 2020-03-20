package com.techelevator.models;

import java.time.Month;

public class Campground {
	
	private int campgroundID;
	private int parkID;
	private String name;
	private int openMonth;
	private int closeMonth;
	private double dailyFee;
	
	public int getCampgroundID() {
		return campgroundID;
	}
	public void setCampgroundID(int campgroundID) {
		this.campgroundID = campgroundID;
	}
	public int getParkID() {
		return parkID;
	}
	public void setParkID(int parkID) {
		this.parkID = parkID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(int openMonth) {
		this.openMonth = openMonth;
	}
	public int getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}
	public double getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(double dailyFee) {
		this.dailyFee = dailyFee;
	}
	@Override
	public String toString() {
		String numberSpace = "";
		return String.format("%-4s %-22s %-16s %-16s $%-16.2f", numberSpace,
				name, Month.of(openMonth).name(), Month.of(closeMonth).name(), dailyFee);
	}
	
	

}
