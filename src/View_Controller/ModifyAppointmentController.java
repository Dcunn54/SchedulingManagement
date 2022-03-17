/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.DBQuery;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 * @author Daniel
 */
public class ModifyAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    Appointment appointment;
    ObservableList<Contact> contacts = FXCollections.observableArrayList();
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<User> users = FXCollections.observableArrayList();
    
    @FXML
    private TextField modAppTitleTxt;
    @FXML
    private TextField modAppDescTxt;
    @FXML
    private TextField modAppLocTxt;
    @FXML
    private TextField modAppTypeTxt;
    @FXML
    private TextField modAppStartYearTxt;
    @FXML
    private TextField modAppEndYearTxt;
    @FXML
    private ComboBox<Customer> modAppCustComBox;
    @FXML
    private ComboBox<Contact> modAppContactComBox;
    @FXML
    private Button saveApp;
    @FXML
    private Button returnToMain;
    @FXML
    private TextField modAppStartMonthTxt;
    @FXML
    private TextField modAppStartDayTxt;
    @FXML
    private TextField modAppStartTimeHourTxt;
    @FXML
    private TextField modAppStartTimeMinTxt;
    @FXML
    private TextField modAppEndMonthTxt;
    @FXML
    private TextField modAppEndDayTxt;
    @FXML
    private TextField modAppEndTimeHourTxt;
    @FXML
    private TextField modAppEndTimeMinTxt;
    @FXML
    private ComboBox<User> modAppUserComBox;
    @FXML
    private TextField modAppID;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * Gets information from the user's appointment selection to populate the 
     * modify appointment window's fields
     * @param app
     */
    public void sendAppointment(Appointment app) {
        
        /*
        for (Appointment a : Appointment.allApps){
            if(app.getID() == a.getID()){
                Appointment.allApps.remove(a);
            }
        }
        */
        modAppTitleTxt.setText(String.valueOf(app.getTitle()));
        modAppDescTxt.setText(String.valueOf(app.getDescription()));
        modAppLocTxt.setText(String.valueOf(app.getLocation()));
        modAppTypeTxt.setText(String.valueOf(app.getType()));
        modAppStartYearTxt.setText(String.valueOf(app.getStartTime().getYear()));
        modAppStartMonthTxt.setText(String.format("%02d", app.getStartTime().getMonthValue()));
        modAppStartDayTxt.setText(String.format("%02d", app.getStartTime().getDayOfMonth()));
        modAppStartTimeHourTxt.setText(String.format("%02d", app.getStartTime().getHour()));
        modAppStartTimeMinTxt.setText(String.format("%02d", app.getStartTime().getMinute()));
        modAppEndYearTxt.setText(String.valueOf(app.getEndTime().getYear()));
        modAppEndMonthTxt.setText(String.format("%02d", app.getEndTime().getMonthValue()));
        modAppEndDayTxt.setText(String.format("%02d", app.getEndTime().getDayOfMonth()));
        modAppEndTimeHourTxt.setText(String.format("%02d", app.getEndTime().getHour()));
        modAppEndTimeMinTxt.setText(String.format("%02d", app.getEndTime().getMinute()));
        modAppCustComBox.setValue(app.getCustomer());
        modAppContactComBox.setValue(app.getContact());
        modAppUserComBox.setValue(app.getUser());
        modAppID.setText(String.valueOf(app.getID()));
        
        contacts.clear();
        try {
            setContacts();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'contacts' not populated");
        }
        modAppContactComBox.setItems(contacts);
        
        customers.clear();
        try {
            setCustomers();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'customers' not populated");
        }
        modAppCustComBox.setItems(customers);
        
        users.clear();
        try {
            setUsers();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'users' not populated");
        }
        modAppUserComBox.setItems(users);
    }
    
    /**
     * Updates the ObservableList of all Contacts from the database in order to 
     * populate the combo box
     * @throws SQLException 
     */
    public void setContacts() throws SQLException{
        
        String selectContacts = "SELECT * FROM contacts";
        ResultSet contactSet = DBQuery.Query(selectContacts);
        
        while (contactSet.next()){
            String name = contactSet.getString("Contact_Name");
            int ID = contactSet.getInt("Contact_ID");
            String email = contactSet.getString("Email");
            contacts.add(new Contact(ID, name, email));
        }
    }
    
    /**
     * Updates the ObservableList of all Customers from the database in order to 
     * populate the combo box
     * @throws SQLException 
     */
    public void setCustomers() throws SQLException{
        
        String selectCustomers = "SELECT * FROM customers";
        ResultSet customerSet = DBQuery.Query(selectCustomers);
        
        while (customerSet.next()){
            String name = customerSet.getString("Customer_Name");
            int ID = customerSet.getInt("Customer_ID");
            customers.add(new Customer(ID, name));
        }
    }
    
    /**
     * Updates the ObservableList of all Users from the database in order to 
     * populate the combo box
     * @throws SQLException 
     */
    public void setUsers() throws SQLException{
        
        String selectUsers = "SELECT * FROM users";
        ResultSet userSet = DBQuery.Query(selectUsers);
        
        while (userSet.next()){
            int ID = userSet.getInt("User_ID");
            String userName = userSet.getString("User_Name");
            users.add(new User(ID, userName));
        }
    }

    /**
     * Saves the Appointment to the database, checking to make sure the start time 
     * and end time fall within the company's business hours and checking that the 
     * customer doesn't have conflicting Appointments
     * @param event 
     */
    @FXML
    private void onActionSaveApp(ActionEvent event) {
        
        try{
            
            Contact contact = modAppContactComBox.getValue();
            Customer customer = modAppCustComBox.getValue();
            User user = modAppUserComBox.getValue();
            int appID = Integer.parseInt(modAppID.getText());
            
            ZonedDateTime businessOpen = ZonedDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
                    LocalDateTime.now().getDayOfMonth(), 8, 0, 0, 0, ZoneId.of("America/New_York"));
            businessOpen = businessOpen.withZoneSameInstant(ZoneId.systemDefault());
            LocalTime businessOpenLocal = businessOpen.toLocalTime();
            
            ZonedDateTime businessClose = ZonedDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
                    LocalDateTime.now().getDayOfMonth(), 22, 0, 0, 0, ZoneId.of("America/New_York"));
            businessClose = businessClose.withZoneSameInstant(ZoneId.systemDefault());
            LocalTime businessCloseLocal = businessClose.toLocalTime();
            
            LocalDateTime start = LocalDateTime.of(Integer.parseInt(modAppStartYearTxt.getText()), 
                        Integer.parseInt(modAppStartMonthTxt.getText()), Integer.parseInt(modAppStartDayTxt.getText()), 
                        Integer.parseInt(modAppStartTimeHourTxt.getText()), Integer.parseInt(modAppStartTimeMinTxt.getText()));
            LocalTime startTime = start.toLocalTime();
            
            LocalDateTime end = LocalDateTime.of(Integer.parseInt(modAppEndYearTxt.getText()), 
                        Integer.parseInt(modAppEndMonthTxt.getText()), Integer.parseInt(modAppEndDayTxt.getText()), 
                        Integer.parseInt(modAppEndTimeHourTxt.getText()), Integer.parseInt(modAppEndTimeMinTxt.getText()));
            LocalTime endTime = end.toLocalTime();
            
            boolean timeOverlaps = false;
            
            for (Appointment a : Appointment.allApps){
                if (start.getYear() == a.getStartTime().getYear() && end.getYear() == a.getEndTime().getYear()
                        && start.getMonthValue() == a.getStartTime().getMonthValue() && end.getMonthValue() == 
                        a.getEndTime().getMonthValue() && start.getDayOfMonth() == a.getStartTime().getDayOfMonth() 
                        && end.getDayOfMonth() == a.getEndTime().getDayOfMonth() && !end.isBefore(a.getStartTime())
                        && !start.isAfter(a.getEndTime()) && a.getCustomerID() == customer.getID()
                        && a.getID() != appID){
                    
                    timeOverlaps = true;
                }
            }
            
            if (modAppTitleTxt.getText().isBlank() || modAppDescTxt.getText().isBlank() || 
                    modAppLocTxt.getText().isBlank() || modAppTypeTxt.getText().isBlank() || 
                    modAppStartYearTxt.getText().isBlank() || modAppEndYearTxt.getText().isBlank() || 
                    modAppStartMonthTxt.getText().isBlank() || modAppStartDayTxt.getText().isBlank() || 
                    modAppStartTimeHourTxt.getText().isBlank() || modAppStartTimeMinTxt.getText().isBlank() || 
                    modAppEndMonthTxt.getText().isBlank() || modAppEndDayTxt.getText().isBlank() || 
                    modAppEndTimeHourTxt.getText().isBlank() || modAppEndTimeMinTxt.getText().isBlank()){
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
                alert.showAndWait();
                
            } else if (startTime.isBefore(businessOpenLocal) || endTime.isAfter(businessCloseLocal)
                    || startTime.isAfter(businessCloseLocal) || endTime.isBefore(businessOpenLocal)
                    || start.getDayOfWeek().equals(DayOfWeek.SATURDAY) || start.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    || end.getDayOfWeek().equals(DayOfWeek.SATURDAY) || end.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("unable to save appointment");
                alert.setHeaderText(null);
                alert.setContentText("Cannot schedule appointment outside of business hours: "
                        + "8:00am-10:00pm EST M-F");
                alert.showAndWait();

            } else if (timeOverlaps) {
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("unable to save appointment");
                alert.setHeaderText(null);
                alert.setContentText("Cannot schedule appointment, customer: " + 
                        customer + " has conflicting appointment");
                alert.showAndWait();
            
            } else {
                
                ZonedDateTime zonedStart = start.atZone(ZoneId.systemDefault());
                zonedStart = zonedStart.withZoneSameInstant(ZoneId.of("UTC"));
                start = zonedStart.toLocalDateTime();
                
                ZonedDateTime zonedEnd = end.atZone(ZoneId.systemDefault());
                zonedEnd = zonedEnd.withZoneSameInstant(ZoneId.of("UTC"));
                end = zonedEnd.toLocalDateTime();
                
                String addApp = "UPDATE appointments SET Title = '" + modAppTitleTxt.getText() 
                        + "', Description = '" + modAppDescTxt.getText() + "', Location = '"
                        + modAppLocTxt.getText() + "', Type = '" + modAppTypeTxt.getText()
                        + "', Start = '" + Timestamp.valueOf(start) + "', End = '"
                        + Timestamp.valueOf(end) + "', Customer_ID = " + customer.getID()
                        + ", User_ID = " + user.getID() + ", Contact_ID = " + contact.getID()
                        + " WHERE Appointment_ID = " + modAppID.getText();
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Appointment");
                alert.setHeaderText(null);
                alert.setContentText("Save Appointment and Return to Main Screen?");
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.get() == ButtonType.OK){
                    DBQuery.Update(addApp);
                    
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.setTitle("");
                    stage.show();
                }
            }
            
        } catch (Exception ex) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
            alert.showAndWait();
            
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns to the main screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionReturnToMain(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Main");
        alert.setHeaderText(null);
        alert.setContentText("Return to Main Screen? All unsaved data will be lost");
        Optional<ButtonType> result = alert.showAndWait();
                
        if (result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("");
            stage.show();
        }
    }
    
}
