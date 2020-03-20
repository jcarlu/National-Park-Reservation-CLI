package com.techelevator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.Jdbc.JdbcCampgroundDao;
import com.techelevator.Jdbc.JdbcParkDao;
import com.techelevator.Jdbc.JdbcReservationDao;
import com.techelevator.Jdbc.JdbcSiteDao;
import com.techelevator.models.Campground;
import com.techelevator.models.Park;
import com.techelevator.models.Reservation;
import com.techelevator.models.Site;
import com.techelevator.view.Menu;

public class CampgroundCLI {
	
	
	private JdbcParkDao parkDao;
	private JdbcCampgroundDao campgroundDao;
	private JdbcSiteDao siteDao;
	private JdbcReservationDao reservationDao;
	
	private Menu menu;

	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
			
		}
	
	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		parkDao = new JdbcParkDao(dataSource);
		campgroundDao = new JdbcCampgroundDao(dataSource);
		siteDao = new JdbcSiteDao(dataSource);
		reservationDao = new JdbcReservationDao(dataSource);
	}
	

	public void run() {
			while(true) {
				int chosenParkId = mainMenu();
				parkMenu(chosenParkId);
			}
		}
	
	
	private int mainMenu() {
	printHeading("Select Park For Further Details");
	List<Park> parkList = parkDao.getAllParks();
	String[] parkName = new String [parkList.size() + 1];
	int i ; 
	for (i = 0; i < parkList.size(); i ++) {
		parkName[i] = parkList.get(i).getName();
	}
	parkName[i] = "QUIT";
	String choice = (String)menu.getChoiceFromOptions(parkName); 
	int chosenParkId = 0;
	for(Park park: parkList) {
		if(choice.equals(park.getName())) {
			System.out.println(park.toString());
			chosenParkId = park.getParkId();
		} 
		else if (choice.equals("QUIT")){
			System.exit(0);
		}
	}
	return chosenParkId;
	}
	
	
	
	public void parkMenu(int chosenParkId) {
	printHeading("Please Select a Command");
	List <Campground> campgroundList = campgroundDao.getAllCampgroundsByParkId(chosenParkId);

	String [] parkMenu = new String [3];
	parkMenu[0] = "View Campgrounds";
	parkMenu[1] = "Search Reservations";
	parkMenu[2] = "Return to Previous Screen";
	String parkMenuChoice = (String)menu.getChoiceFromOptions(parkMenu);
	
	if(parkMenuChoice.equals("View Campgrounds")){
		String parkName = parkDao.getParkNameById(chosenParkId).getName();
		System.out.println(parkName + " National Park Campgrounds");
		String numberSpace = "";
		String name = "Name";
		String open = "Open";
		String close = "Close";
		String fee = "Daily Fee";
		System.out.printf("%-6s %-22s %-16s %-16s %-16s\n", numberSpace, name, open, close, fee);
		int x = 1;
		for(Campground camp: campgroundList) {
			System.out.println("#" + x + camp.toString());
			x++;
			}
		
		//start campground menu
		System.out.println("\n");
		campgroundMenu(chosenParkId);
		//end campground menu
		
		//begins second choice of park menu
	} else if (parkMenuChoice.equals("Search Reservations")) {
		System.out.println("\n");
		reservationMenu(chosenParkId);
		}
	
	//third choice of park menu directs back to the main menu
	
	}
	
	
	public void campgroundMenu(int chosenParkId) {
		printHeading("Select a Command");
		String [] campgroundMenu = new String [2];
		campgroundMenu[0] = "Search for Available Reservation";
		campgroundMenu[1] = "Return to Previous Screen";
		String campgroundMenuChoice = (String)menu.getChoiceFromOptions(campgroundMenu);
		if(campgroundMenuChoice.equals("Search for Available Reservation")){
			System.out.println("\n");
			reservationMenu(chosenParkId);
		}
		else if (campgroundMenuChoice.equals("Return to Previous Screen")) {
			System.out.println(parkDao.getParkNameById(chosenParkId).toString());
			parkMenu(chosenParkId);
		}
		
	}
	
	
	public void reservationMenu(int chosenParkId) {
		List <Campground> campgroundList = campgroundDao.getAllCampgroundsByParkId(chosenParkId);
				Scanner input = new Scanner(System.in);
				String parkName = parkDao.getParkNameById(chosenParkId).getName();
				System.out.println("\n" + parkName + " National Park Campgrounds");
				String numberSpace = "";
				String name = "Name";
				String open = "Open";
				String close = "Close";
				String fee = "Daily Fee";
				System.out.printf("%-6s %-22s %-16s %-16s %-16s\n", numberSpace, name, open, close, fee);
				
				int x = 1;
				for(Campground camp: campgroundList) {
					System.out.println("#" + x + camp.toString());
					x++;
				}
				
				int daysOfStay = 0;
			
				try {
				System.out.println("\nWhich Campground # (enter 0 to cancel)?");
				int campgroundNumberChoice = Integer.parseInt(input.nextLine());
				System.out.println("");
				int campgroundId = 0; 
				double campgroundDailyFee = 0;
				
				for(Campground camp: campgroundList) {
					if(campgroundList.get(campgroundNumberChoice-1).getName().equals(camp.getName())) {
						campgroundId = camp.getCampgroundID();
						campgroundDailyFee = camp.getDailyFee();
					}
				}
				
				if(campgroundId == 0) {
					campgroundMenu(chosenParkId);
				}
				
				
				
				if(campgroundNumberChoice > campgroundList.size() || campgroundNumberChoice < 0) {
					System.out.println("\nThat is not a valid campsite.\n");
					reservationMenu(chosenParkId);
				}
				
				System.out.println("What is the arrival date? (Enter as: YYYY-MM-DD)");
				LocalDate arrivalDateChoice = LocalDate.parse(input.nextLine());
				System.out.println("");

				
				if (arrivalDateChoice.compareTo(LocalDate.now()) < 0) {
					System.out.println("\nThat is not a valid arrival date.\n");
					reservationMenu(chosenParkId);
				}
				
				System.out.println("What is the departure date? (Enter as: YYYY-MM-DD)");
				LocalDate departureDateChoice = LocalDate.parse(input.nextLine());
				System.out.println("");

				
				daysOfStay = Math.toIntExact(ChronoUnit.DAYS.between(arrivalDateChoice, departureDateChoice));
				
				
				 if (arrivalDateChoice.compareTo(departureDateChoice) >= 0) {
					System.out.println("\nThat is not a valid departure date.\n");
					
					reservationMenu(chosenParkId);
				 	}
				 
					createReservation(chosenParkId, campgroundId, arrivalDateChoice, departureDateChoice,
							daysOfStay, campgroundDailyFee);

				} catch (DateTimeException e) {
					System.out.println("\nYou typed in an invalid date.\n");
					
					reservationMenu(chosenParkId);
			}
				

				
				input.close();
 		}	
	
				
	public void createReservation(int chosenParkId, int campgroundId, LocalDate arrivalDateChoice,
			LocalDate departureDateChoice, int daysOfStay, double campgroundDailyFee) {
		
		//PRINT OUT List of sites from method  
		List <Site> siteList = siteDao.getSiteByCriteria(campgroundId, arrivalDateChoice, departureDateChoice);
		
		double subtotal = daysOfStay * campgroundDailyFee;
		
		System.out.println("Results Matching Your Search Criteria");
		String siteNumber = "Site";
		String occupancy = "Max Occup.";
		String accessible = "Accessible?";
		String maxRv = "RV Length";
		String utilities = "Utility"; 
		String cost = "Cost";
		System.out.printf("%-8s %-10s %-12s %-10s %-8s %-6s\n", siteNumber, occupancy, accessible, maxRv, utilities, cost);

		Scanner input = new Scanner(System.in);
		for(Site site: siteList) {
			System.out.printf(site.toString() + " $%.2f\n", subtotal);
		}

		try {
		System.out.println("\nWhat site #?");
		int siteNumberChoice = Integer.parseInt(input.nextLine());
		
		boolean isRealSite = false;
		for(Site site : siteList) {
			if(site.getSiteId() == siteNumberChoice) {
				isRealSite = true;
			}
		} if(isRealSite == false) {
			System.out.println("That is an invalid site number.\n");
			createReservation(chosenParkId, campgroundId, arrivalDateChoice, departureDateChoice, daysOfStay, campgroundDailyFee);
		}
		
		
		System.out.println("\nWhat name would you like to book the reservation under?");
		String reservationName = input.nextLine();
		
		Reservation newReservation = new Reservation(siteNumberChoice, reservationName, arrivalDateChoice, departureDateChoice);
		
		
		//add reservation to database with method
		reservationDao.createReservation(newReservation);
		int reservationId = reservationDao.getMaxReservationId();

		//check to make sure reservation was made, if successful:
		System.out.println("\nReservation " + reservationId + " for " +  
		reservationName + " has been created!" );
		
		} catch (Exception e) {
			System.out.println("That is not a valid site number.");
			createReservation(chosenParkId, campgroundId, arrivalDateChoice,
					departureDateChoice, daysOfStay, campgroundDailyFee);
		}	
		input.close();
		System.exit(0);
	}
	
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
