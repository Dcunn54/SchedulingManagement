/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBQuery;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for handling creation and functionality of Customer objects
 * @author Daniel
 */
public class Customer {
    
    protected int ID;
    protected String name;
    protected String address;
    protected String postalCode;
    protected String phoneNum;
    protected Country country;
    protected State state;
    
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
    /**
     * Customer constructor
     * @param ID
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNum
     * @param country
     * @param state 
     */
    public Customer(int ID, String name, String address, String postalCode, String phoneNum, 
            Country country, State state){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNum = phoneNum;
        this.country = country;
        this.state = state;
    }
    
    /**
     * Customer constructor 
     * @param ID
     * @param name 
     */
    public Customer(int ID, String name){
        this.ID = ID;
        this.name = name;
    }
    
    /**
     * Updates the ObservableList of all Customers pulled from the database
     * @throws SQLException 
     */
    public static void updateCustomerList() throws SQLException{
        
        allCustomers.clear();
        String selectCustomers = "SELECT * FROM customers";
        ResultSet customerSet = DBQuery.Query(selectCustomers);
        
        while (customerSet.next()){
                
            String selectState = "SELECT * FROM first_level_divisions WHERE Division_ID = "
                    + customerSet.getInt("Division_ID");
            ResultSet stateSet = DBQuery.Query(selectState);
            stateSet.next();
            String stateName = stateSet.getString("Division");
            int stateID = stateSet.getInt("Division_ID");
            int countryID = stateSet.getInt("COUNTRY_ID");
            State state = new State(stateName, stateID, countryID);
                
            String selectCountry = "SELECT * FROM countries WHERE Country_ID = "
                    + state.getCountryID();
            ResultSet countrySet = DBQuery.Query(selectCountry);
            countrySet.next();
            String countryName = countrySet.getString("Country");
            int ID = countrySet.getInt("Country_ID");
            Country country = new Country(countryName, ID);
                
            Customer newCustomer = new Customer(customerSet.getInt("Customer_ID"), customerSet.getString("Customer_Name"), 
                customerSet.getString("Address"), customerSet.getString("Postal_Code"), customerSet.getString("Phone"), 
                country, state);
                
            allCustomers.add(newCustomer);
        }
    }
    
    /**
     * Returns ObservableList of all Customers
     * @return 
     */
    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }

    /**
     * Returns Customer ID
     * @return 
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns Customer name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Returns Customer address
     * @return 
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns Customer postal code
     * @return 
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns Customer phone number
     * @return 
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Returns Customer Country
     * @return 
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Returns Customer state/province
     * @return 
     */
    public State getState() {
        return state;
    }
    
    /**
     * Returns Customer name as a String, overrides standard toString
     * @return 
     */
    @Override
    public String toString(){
        return this.name;
    }
}
