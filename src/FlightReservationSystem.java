/*
Course: CS 2336.001
Name: Jaswin S. Kohli
Net ID: jsk140230
Date:12/13/2015
Term Project
Description: The following program simulates a flight reservation system. It allows the user to 
search for flights. the program searches for possible flights and displays the result.
Then it prompts the user to select a particular flight. After flight selection, it taes the user
to passenger information form. the program also allows the user to enter seat number. Finally it 
generates a flight ticket for the user by creating a text file.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightReservationSystem {
    
    public static void main(String[] args) {
        //the following function call reads the flight data from a file and creates an arraylist of flight objects
        populateFlightList();
        Scanner cin = new Scanner(System.in);              //initialising scanner
        System.out.println("\t\t\tWELCOME TO FLIGHT RESERVATION CENTER");
        int ans = 1;                                      //used to control flow of program
        while(ans!=0) {
            SearchFlight test = new SearchFlight();         //creates a new search object
            test.getSearchData();                           //prompts the user to enter deails such as destination, departure date etc.
            boolean found = test.getSearchResults();        //creates a list of flights available using search details entered by user. returns false if no flights could be found
            if(found) {
                System.out.println("\n\t\t\tSEARCH RESULTS\n");
                test.displayResults();                      //displays the results of the search to the user in the form of table
                
                System.out.print("Enter the index number of flight you wish to book (1-" 
                        + (test.getList().size())+ "): ");        //prompts the user to select a flight from results
                int index = cin.nextInt()-1;
                
                if(index >= 0 && index < test.getList().size()) {   //if the index entered is valid
                    Flight reservedFlight = test.getList().get(index);      //stores the flight selected by user in flight object

                    if(reservedFlight.getAvailableSeats() <= 0){            //if all the seats are occupied then
                        System.out.println("Sorry! Not enough seats.");
                        System.out.print("\nDo you want to search again?? (Enter 1 to continue or 0 to exit): ");
                        ans = cin.nextInt(); 
                    }
                    else {                                                  
                        System.out.print("\nSELECTED FLIGHT: "+(index+1)+". ");
                        reservedFlight.display();                           //displays the flight selected by user
                        System.out.println("\n\t\t\tENTER PASSENGER INFORMATION");
                        Passenger passenger = new Passenger(reservedFlight);        //creates a passenger object to make reservation
                        ans = 0;
                    }
                }
                else {
                    System.out.print("ERROR: invalid index");
                    System.out.print("\nDo you want to search again?? (Enter 1 to continue or 0 to exit): ");
                    ans = cin.nextInt();
                }
            }
            else    //notifies the user that no flights were found and asks if he/she wants to perform another search
            {
                System.out.print("Sorry! There are no flights flying from " + test.getFrom() + " to " + test.getTo() + " on ");
                System.out.printf("%tD%n", test.getDepartureDate());
                System.out.print("\nDo you want to search again?? (Enter 1 to continue or 0 to exit): ");
                ans = cin.nextInt();               
            }
        }
        System.out.println("\n\t\t\tTHANK YOU FOR USING OUR SYSTEM!\n");        //Ending message
    } 
    
    /**
     * The following class is the most important class in this project. It holds all the information
     * about a flight such as Flight number, airlines, departure date/time/place, arrival date/time/place
     * It provides accessor and mutator methods to access this information.
     * It also contains a class called time. Time is a custom class to make it easier to display/read time
     */
    public static class Flight {
        private String to;                      //Arrival airport
        private String from;                    //Departure airport
        private Date departureDate;             
        private Date arrivalDate;
        private char[][] seats;                 //array representing seat planof the plane. '_' signifies vacancy. 'R' signifies reserved

        //Accessor methods for flight class
        public String getAirlines() {
            return airlines;
        }
        /*The following class is a class inside the flight class.
        It makes it easier to display/access time information*/
        private class Time {
            private int hour;               //stores hour in 24 hour format
            private int minutes;            //stores minutes
            
            //default constructor of Time class
            public Time() {
                hour = 0;
                minutes = 0;
            }
            //parameterised constructor
            public Time(int hour, int minutes) {
                this.hour = hour;
                this.minutes = minutes;
            }
            //returns the hour in 24 hour fromat
            public int getHour() {
                return hour;
            }
            //returns minutes
            public int getMinutes() {
                return minutes;
            }
            
            //mutator function to set time to passed values
            public void setTime(int hour, int minutes) {
                this.hour = hour;
                this.minutes = minutes;
            }
            //prints time in 24 hour format
            public void printTime() {
                System.out.printf("%02d:%02d", hour, minutes);
            }
            //prints time in 12 hour format
            public void printTime12() {
                int temp = hour%12;
                String ch;
                if (hour/12 == 1)
                    ch = "PM";
                else
                    ch = "AM";
                System.out.printf("%02d:%02d %s", temp, minutes, ch);
            }
            
        }
        
        private Time departureTime;             //Time object storing departure time
        private Time arrivalTime;               //time object storing arrival time of flight
        private String airlines;
        private int duration;                   //stores flight duration
        private int number;                     //stores flight number
        private int capacity;                   //stores maximum capacity of the plane
        private int bookedSeats;                //stores the number of seats booked
        private double cost;                    //stores the cost of a flight. all seats are of the same cost. No premium class
        
        //Default constructor. Stores default values as flight data
        public Flight() {
            to = "BOM";
            from = "DFW";
            departureDate = new Date();
            arrivalDate = new Date();
            departureTime = new Time();
            arrivalTime = new Time();
            duration = 0;
            airlines = "Americal Airlines";
            number = 1;
            capacity = 100;
            bookedSeats=0;
            cost = 100.0;
            
            seats = new char[10][10];
            
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 10; j++)
                    seats[i][j] = '_';
        }
        
        public Flight(int number, String from, String to, String departureDateText, 
                String departureTime, String arrivalDateText,String arrivalTime,
                double cost, String airlines, int capacity) {
            
            this.to = to;
            this.from = from;
            
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            try { 
                this.departureDate = sdt.parse(departureDateText);
               
                this.arrivalDate = sdt.parse(arrivalDateText);
            } 
            catch (ParseException e) { 
                System.out.println("Unparseable using " + sdt);
                
            }
            
            String[] parts = departureTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);            
            this.departureTime = new Time(hour, minutes);
            
            parts = arrivalTime.split(":");
            hour = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            this.arrivalTime = new Time(hour, minutes);
            
            //this.duration = ;
            this.cost = cost;
            this.airlines = airlines;
            this.number = number;
            this.capacity = capacity;
            this.bookedSeats=0;
            
            seats = new char[10][10];
            
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 10; j++)
                    seats[i][j] = '_';
        }
        
        public String getTo() {
            return to;
        }
        public String getFrom() {
            return from;
        }
        public Date getDepartureDate() {
            return departureDate;
        }
        public Date getArrivalDate() {
            return arrivalDate;
        }
        public String getDepartureTime() {
            String time = departureTime.getHour()+":"+departureTime.getMinutes();
            return time;
        }
        public String getArrivalTime() {
            String time = arrivalTime.getHour()+":"+arrivalTime.getMinutes();
            return time;
        }
        public int getBookedSeats(){
            return bookedSeats;
        }
        public int getAvailableSeats(){
            return capacity-bookedSeats;
        }
        public void reserveSeats(int row, char col) {
            this.bookedSeats--;
            if(seats[row][(int)Character.toUpperCase(col)-65]=='R')
                System.out.println("Error: This seat is Reserved.");
            else {
                seats[row][(int)Character.toUpperCase(col)-65]='R';
                printSeats();
            }
        }
        
        public void display() {
            System.out.printf("%-20s | %-5s  %tB %<te, %<tY ",airlines, from, departureDate);
            departureTime.printTime();
            System.out.printf("\t-->\t%-5s  %tB %<te, %<tY ", to, arrivalDate);
            arrivalTime.printTime();
            System.out.printf("\t$%.2f%n", cost);
        }
        
        public void printSeats() {
            System.out.println("\n\t\tRESERVE A SEAT");
            System.out.print("\n\t    A B C    D E F G    H I J\n\t");
            for(int i = 0; i < 10; i++) {
                System.out.printf("%2d  ",(i+1));
                for(int j = 0; j < 10; j++) {
                    if(j == 3 || j == 7)
                        System.out.print("   ");
                    System.out.print(seats[i][j] + " ");
                }
                System.out.print("\n\t");
            }
            System.out.printf("%n%n");
        }
    }
    
    static ArrayList<Flight> flightList= new ArrayList<>();
    /**
     * the following function opens a file containing flight data and creates a list of flight objects      
     */
    static void populateFlightList() {
        String fileName = "flight.txt";                     //file containg flight data
        File file = new File(fileName);
        try {
            Scanner fin = new Scanner(file);                //connects the scanner to the file flight.txt
            
            /*the following loop reads from the file and parses data into flight objects
            by creating a new object in each iteration and adding it to an arraylist*/
            while(fin.hasNext()) {
                int number = fin.nextInt();                 //flight numer
                String from = fin.next();                   //Departure airport
                String to = fin.next();                     //Arrival airport

                String departureDateText = fin.next();      //departure date as STring
                String departureTime = fin.next();          //Departure Time as String
                String arrivalDateText = fin.next();        //Arrival Date as String
                String arrivalTime = fin.next();            //Arrival Time as String

                double cost = fin.nextDouble();             //cost of the flight
                String airlines = fin.next();               //name of airlines

                int capacity = fin.nextInt();               //maximum capacity of the flight
                
                /*the following statement passes the data read from file as arguments into
                parameterised constructor of the flight object*/
                Flight flight = new Flight(number, from, to, departureDateText, 
                        departureTime, arrivalDateText, arrivalTime, cost, airlines, capacity);
                flightList.add(flight);                     //appends the flight object to ArrayList
            }
            
        } catch (FileNotFoundException ex) {                //If the file could not be found
            Logger.getLogger(FlightReservationSystem.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: Flight data file not found!");
        }
        
        
    }
    
    /**
     * The following class is a class for query objects for searching flights
     * It has source, destination, and flight date as its data members
     * The class has member functions which get data from user, search flights 
     * based on that information, store it in an arraylist, and display the results.
     */
    public static class SearchFlight {
        String from;                            //departure airport
        String to;                              //arrival flight
        Date departureDate;                     //departure date
        ArrayList<Flight> searchResults;        //This arrayList stores flights from the user search
            
        //returns arrival city
        public String getTo() {
            return to;
        }
        //returns departure city
        public String getFrom() {
            return from;
        }
        /**
         * returns the date of flight Departure
         * @return 
         */
        public Date getDepartureDate() {
            return departureDate;
        }
        /**
         * returns the list of flight results from the user search
         * @return 
         */
        public ArrayList<Flight> getList() {
            return searchResults;
        }
        /**
         * traverses the list containing data for all the flights
         * and displays it to the console
         */
        public SearchFlight() {
            System.out.println("\nList of all the flights:\n");
            System.out.println("AIRLINES             | FROM   DEPARTURE DATE/TIME       -->     TO     ARRIVAL DATE/TIME        PRICE PER ADULT");
        System.out.println("------------------------------------------------------------------------------------------------------------");
            
        for(int i = 0; i < flightList.size(); i++)
            flightList.get(i).display();        
        }
        /**
         * Mutator function to set Departure city
         * @param from 
         */
        public void setFrom(String from) {
            this.from = from;
        }
        /**
         * Mutator function to set arrival city
         * @param to 
         */
        public void setTo(String to) {
            this.to = to;
        }
        /**
         * Mutator function to set date of departure.
         * @param departureDateText 
         */
        public boolean setDepartureDate(String departureDateText) {
            SimpleDateFormat sdt = new SimpleDateFormat("MM-dd-yyyy");
            try { 
                this.departureDate = sdt.parse(departureDateText);
                return true;
            } 
            catch (ParseException e) { 
                System.out.println("Invalid date format!");
                return false;
            }
        }
        /**
         * Prompts user for City of Departure and Arrival and
         * date on which the user wishes to travel.
         * Then the function traverses the arrayList containing flight data
         * and creates a new list which match the user's search parameters.
         */
        public void getSearchData() {
            Scanner cin = new Scanner(System.in);               //iniatilizing scanner
            System.out.println("\n\n\t\tSEARCH FLIGHTS");
            System.out.print("FROM: ");                         //prompts user for city of departure
            from = cin.next();
            System.out.print("TO: ");                           //prompts user for city of arrival
            to = cin.next();
            System.out.print("DEPARTURE(MM-DD-YYYY): ");        //prompts user for Date of departure
            String tempDate = cin.next();
            while(!setDepartureDate(tempDate)) {
                System.out.print("DEPARTURE(MM-DD-YYYY): ");        //prompts user for Date of departure
                tempDate = cin.next();            
            }           
        }    
        
        /**
         * returns a list of flight objects containing information of flights
         * which match the user's search.
         * @return 
         */
        public boolean getSearchResults() {
            searchResults = new ArrayList<>();           
            
            for(int i = 0; i < flightList.size(); i++) {
                if(flightList.get(i).getFrom().equalsIgnoreCase(from) && flightList.get(i).getTo().equalsIgnoreCase(to)) {
                    if(flightList.get(i).getDepartureDate().equals(departureDate)) {
                        searchResults.add(flightList.get(i));               
                    }
                }
            }
            
            if(searchResults.isEmpty())                
                return false;
            else
                return true;
        }
    
        /**
         * Prints the results of the user's search on to console
         */
        public void displayResults() {
            for(int i = 0; i < searchResults.size(); i++) {
                    System.out.print((i+1) + ". ");
                    searchResults.get(i).display();                //calls the display function of flight object to print info
            }                
        }
    }
    
    /**
     * Base class for Passenger class. Stores basic information such as
     * name, dob, age, address, contact, email etc.
     * only has getter and setter functions.
     */
    public static class Person {
        protected String firstName;
        protected String lastName;
        protected int age;
        protected Date dateOfBirth;
        protected String address;
        protected String nationality;
        protected long contactNumber;
        protected String emailID;
        
        /**
         * Prompts user for his personal information
         * and stores the information in class objects
         */
        public Person(){
            Scanner cin = new Scanner(System.in);
            System.out.print("FIRST NAME: ");
            firstName = cin.nextLine();
            System.out.print("LAST NAME: ");
            lastName = cin.nextLine();
            System.out.print("AGE: ");
            age = cin.nextInt();
            System.out.print("DATE OF BIRTH(mm-dd-yyyy): ");
            String tempDateOfBirth = cin.next();
            SimpleDateFormat sdt = new SimpleDateFormat("MM-dd-yyyy");
            try { 
                this.dateOfBirth= sdt.parse(tempDateOfBirth);               
            } 
            catch (ParseException e) { 
                System.out.println("Unparseable using " + sdt); 
            }
            String ch = cin.nextLine();
            System.out.print("ADDRESS: ");
            address = cin.nextLine();
            System.out.print("NATIONALITY: ");
            nationality = cin.next();
            System.out.print("CONTACT NUMBER: ");
            contactNumber = cin.nextLong();
           
            
        }
        
        /**
         * parameterized constructor for person class.
         * @param firstName
         * @param lastName
         * @param age
         * @param dateOfBirth
         * @param address
         * @param nationality
         * @param contactNumber
         * @param emailID 
         */
        Person(String firstName, String lastName, int age, Date dateOfBirth, 
                String address, String nationality, long contactNumber, String emailID) {
            
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.nationality = nationality;
            this.contactNumber = contactNumber;
            this.emailID = emailID;
        }        
        
        //Getter methods
        public String getFirstName() {
            return firstName;            
        }
        public String getLastName() {
            return lastName;            
        }
        public int getAge() {
            return age;
        }
        public String getaddress() {
            return address;            
        }
        public String getNationality() {
            return nationality;            
        }
        public long getContactNumber() {
            return contactNumber;
        }
        public String getEmailID() {
            return emailID;            
        }
        
        //setter methods
        public void setFirstName(String firstName) {
            this.firstName = firstName;            
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;            
        }
        public void setAge(int age) {
            this.age = age;
        }
        public void setaddress(String address) {
            this.address = address;            
        }
        public void setNationality(String nationality) {
            this.nationality = nationality;            
        }
        public void setContactNumber(long contactNumber) {
            this.contactNumber = contactNumber;
        }
        public void setEmailID(String emailID) {
            this.emailID = emailID;            
        }
        
    }
    
    /**
     * The following class inherits from class person.
     * It has extra attributes such as the seat number in flight.
     * The class has methods to reserve a seat for the passenger.
     */
    public static final class Passenger extends Person{
        private int rowSeat;                        //stores row number of the flight seat
        public char colSeat;                        //stores column number of flight seat
        Passenger(Flight reservedFlight) {
            reservedFlight.printSeats();            //prints the seat plan of flight to console. '_' represents vacancy.
                                                    //'R' represents Reserved.
            Scanner cin = new Scanner(System.in);
            System.out.print("Enter seat number (row-column, ex: 1-D): ");
            String ch = cin.nextLine();
            String[] temp = ch.split("-");            
            rowSeat = Integer.parseInt(temp[0]);
            rowSeat--;
            colSeat = temp[1].charAt(0);
            reservedFlight.reserveSeats(rowSeat, colSeat);
            System.out.println("Your Seat has been reserved. Your flight ticket has been generated!");
            generateTicket(reservedFlight);           //calls a function which creates a ticket in the form of a text file
            
        }
        /**
         * This function generates a ticket for the user in the form of 
         * text file which contains relevant flight information
         * @param reservedFlight 
         */
        public void generateTicket(Flight reservedFlight) {
            File ticket = new File("FlightTicket.txt");
            try {
                PrintWriter fout = new PrintWriter(ticket, "UTF-8");
                fout.println("\t\t\t"+reservedFlight.getAirlines().toUpperCase());
                fout.println("\t\t\tFLIGHT TICKET\n\n");
                fout.printf("name: "+this.lastName.toUpperCase()+", "+this.firstName.toUpperCase()+"%n");
                fout.printf("%nfrom: %-5s  on %tB %<te, %<tY at %-7s hrs   ", reservedFlight.getFrom().toUpperCase(), reservedFlight.getDepartureDate(),reservedFlight.getDepartureTime());
                fout.printf("-->    to: %-5s  on %tB %<te, %<tY at %-7s hrs", reservedFlight.getTo().toUpperCase(), reservedFlight.getArrivalDate(),reservedFlight.getArrivalTime());
                fout.printf("%nSEAT: %-2d%s",(rowSeat+1),Character.toString(colSeat));
                fout.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FlightReservationSystem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FlightReservationSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
