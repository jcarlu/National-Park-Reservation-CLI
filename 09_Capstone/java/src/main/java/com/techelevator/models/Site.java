package com.techelevator.models;

public class Site {
	
	private int siteId;
	private int campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean isHandicapAccess;
	private int maxRvLength;
	private boolean hasUtilities;
	
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isHandicapAccess() {
		return isHandicapAccess;
	}
	public void setHandicapAccess(boolean isHandicapAccess) {
		this.isHandicapAccess = isHandicapAccess;
	}
	public int getMaxRvLength() {
		return maxRvLength;
	}
	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	public boolean isHasUtilities() {
		return hasUtilities;
	}
	public void setHasUtilities(boolean hasUtilities) {
		this.hasUtilities = hasUtilities;
	}
	
	
	
	@Override
	public String toString() {
		
		//utilities: N/A or yes
		//access no or yes
		//rv length is n/a or #
		
		String handicapAccessString = null;
		String hasUtilitiesString = null;
		String rvLengthString = null;
		if(this.isHandicapAccess == false) {
			handicapAccessString = "No";
		} else {
			handicapAccessString = "Yes";
		}
		if(this.hasUtilities == true) {
			hasUtilitiesString = "Yes";
		} else {
			hasUtilitiesString = "N/A";
		}
		if(this.maxRvLength == 0) {
			rvLengthString = "N/A";
		} else {
			rvLengthString = "" + this.maxRvLength;
		}
		return String.format("%-8s %-10s %-12s %-10s %-8s", siteId, maxOccupancy, handicapAccessString, rvLengthString,
				hasUtilitiesString);
	}
	
	
	
	

}
