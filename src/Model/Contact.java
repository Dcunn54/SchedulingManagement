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
 * Class for handling creation and functionality of Contact objects
 * @author Daniel
 */
public class Contact {
    
    protected int ID;
    protected String name;
    protected String email;
    
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    
    /**
     * Contact constructor
     * @param ID
     * @param name
     * @param email 
     */
    public Contact(int ID, String name, String email){
        this.ID = ID;
        this.name = name;
        this.email = email;
    }
    
    /**
     * Updates the ObservableList of all Contacts pulled from the database
     * @throws SQLException 
     */
    public static void updateContactList() throws SQLException{
        
        allContacts.clear();
        
        String selectContacts = "SELECT * FROM contacts";
        ResultSet contactSet = DBQuery.Query(selectContacts);
        
        while (contactSet.next()){
            Contact newContact = new Contact(contactSet.getInt("Contact_ID"), 
            contactSet.getString("Contact_Name"), contactSet.getString("Email"));
            allContacts.add(newContact);
        }
    }
    
    /**
     * Returns ObservableList of all Contacts
     * @return 
     */
    public ObservableList<Contact> getAllContacts(){
        return allContacts;
    }

    /**
     * Returns Contact ID
     * @return 
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns Contact name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Returns Contact email address
     * @return 
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Returns Contact name as a String, overrides standard toString
     * @return 
     */
    @Override
    public String toString(){
        return this.name;
    }
    
}
