/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

/**
 * Class for handling creation and functionality for Appointment objects
 * @author Daniel
 */
public class Appointment {
    
    protected int ID;
    protected String title;
    protected String description;
    protected String location;
    protected Contact contact;
    protected int contactID;
    protected String type;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Customer customer;
    protected int customerID;
    protected User user;
    protected int userID;
    
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    public static ObservableList<Appointment> allApps = FXCollections.observableArrayList();
    
    /**
     * Appointment constructor
     * @param ID
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param startTime
     * @param endTime
     * @param customer
     * @param user 
     */
    public Appointment(int ID, String title, String description, String location, Contact contact, 
            String type, LocalDateTime startTime, LocalDateTime endTime, Customer customer, User user){
 
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.contactID = contact.getID();
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customer = customer;
        this.customerID = customer.getID();
        this.user = user;
        this.userID = user.getID();
    }
    
    /**
     * Updates the ObservableList of all Appointments pulled from the database
     * @throws SQLException 
     */
    public static void updateAppsList() throws SQLException{
        
        allApps.clear();
        String selectApps = "SELECT * FROM appointments";
        ResultSet appSet = DBQuery.Query(selectApps);
        
        while (appSet.next()){
            
            String selectContact = "SELECT * FROM contacts WHERE Contact_ID = " 
                    + appSet.getInt("Contact_ID");
            ResultSet contactSet = DBQuery.Query(selectContact);
            contactSet.next();
            int contactID = contactSet.getInt("Contact_ID");
            String contactName = contactSet.getString("Contact_Name");
            String contactEmail = contactSet.getString("Email");
            Contact contact = new Contact(contactID, contactName, contactEmail);
            
            String selectCustomer = "SELECT * FROM customers WHERE Customer_ID = "
                + appSet.getInt("Customer_ID");
            ResultSet customerSet = DBQuery.Query(selectCustomer);
            customerSet.next();
            int customerID = customerSet.getInt("Customer_ID");
            String customerName = customerSet.getString("Customer_Name");
            Customer customer = new Customer(customerID, customerName);
            
            String selectUser = "SELECT * FROM users WHERE User_ID = "
                    + appSet.getInt("User_ID");
            ResultSet userSet = DBQuery.Query(selectUser);
            userSet.next();
            int userID = userSet.getInt("User_ID");
            String userName = userSet.getString("User_Name");
            User user = new User(userID, userName);
            
            Appointment newApp = new Appointment(appSet.getInt("Appointment_ID"), appSet.getString("Title"), 
            appSet.getString("Description"), appSet.getString("Location"), contact, appSet.getString("Type"), 
            appSet.getTimestamp("Start").toLocalDateTime(), appSet.getTimestamp("End").toLocalDateTime(),
            customer, user);
            
            allApps.add(newApp);
        }
    }
    
    /**
     * Updates the ObservableList to show only Appointments scheduled 
     * for the current week
     * @throws SQLException 
     */
    public static void viewByWeek() throws SQLException{

        updateAppsList();
        ObservableList<Appointment> weekView = FXCollections.observableArrayList();
        LocalDateTime startOfWeek = LocalDateTime.now();
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        
        while(dayOfWeek != DayOfWeek.SUNDAY){
            
            dayOfWeek = dayOfWeek.minus(1);
            startOfWeek = startOfWeek.minusDays(1);
        }
        
        for (Appointment a : allApps){
            if (a.getStartTime().isAfter(startOfWeek) && a.getStartTime().isBefore(startOfWeek.plusDays(7))){
                
                weekView.add(a);
            }
        }
        
        allApps = weekView;
    }
    
    /**
     * Updates the ObservableList to show only Appointments scheduled 
     * for the current month
     * @throws SQLException 
     */
    public static void viewByMonth() throws SQLException{
        
        updateAppsList();
        ObservableList<Appointment> monthView = FXCollections.observableArrayList();
        int month = LocalDateTime.now().getMonthValue();
        int year = LocalDateTime.now().getYear();
        for (Appointment a : allApps){
            if (a.getStartTime().getMonthValue() == month && a.getStartTime().getYear() == year){
                monthView.add(a);
            }
        }
        allApps = monthView;
    }

    /**
     * Returns Appointment ID
     * @return 
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns Appointment title
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns Appointment description
     * @return 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns Appointment location
     * @return 
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns Appointment contact
     * @return 
     */
    public Contact getContact() {
        return contact;
    }
    
    /**
     * Returns Appointment contact's ID
     * @return 
     */
    public int getContactID(){
        return contactID;
    }

    /**
     * Returns Appointment type
     * @return 
     */
    public String getType() {
        return type;
    }

    /**
     * Returns Appointment start time (not formatted)
     * @return 
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /**
     * returns the start time for the Appointment formatted in the style 
     * of "MM/dd/yyyy HH:mm"
     * @return 
     */
    public String getStartTimeFormatted(){
        return startTime.format(formatter);
    }

    /**
     * Returns Appointment end time (not formatted)
     * @return 
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    /**
     * returns the end time for the Appointment formatted in the style 
     * of "MM/dd/yyyy HH:mm"
     * @return 
     */
    public String getEndTimeFormatted() {
        return endTime.format(formatter);
    }

    /**
     * Returns Appointment customer
     * @return 
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Returns Appointment customer's ID
     * @return 
     */
    public int getCustomerID(){
        return customerID;
    }
    
    /**
     * Returns the user who created this Appointment
     * @return 
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Returns the ID of the user who created this Appointment
     * @return 
     */
    public int getUserID(){
        return userID;
    }

    /**
     * Returns the ObservableList of all Appointments
     * @return 
     */
    public static ObservableList<Appointment> getAllApps() {
        return allApps;
    }
    
    
    
}
