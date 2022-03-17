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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.DBQuery;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class AddAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<Contact> contacts = FXCollections.observableArrayList();
    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private TextField appTitleTxt;
    @FXML
    private TextField appDescTxt;
    @FXML
    private TextField appLocTxt;
    @FXML
    private TextField appTypeTxt;
    @FXML
    private TextField appStartYearTxt;
    @FXML
    private TextField appEndYearTxt;
    @FXML
    private ComboBox<Customer> appCustComBox;
    @FXML
    private ComboBox<Contact> appContactComBox;
    @FXML
    private ComboBox<User> appUserComBox;
    @FXML
    private Button saveApp;
    @FXML
    private Button returnToMain;
    @FXML
    private TextField appStartMonthTxt;
    @FXML
    private TextField appStartDayTxt;
    @FXML
    private TextField appStartTimeHourTxt;
    @FXML
    private TextField appStartTimeMinTxt;
    @FXML
    private TextField appEndMonthTxt;
    @FXML
    private TextField appEndDayTxt;
    @FXML
    private TextField appEndTimeHourTxt;
    @FXML
    private TextField appEndTimeMinTxt;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        contacts.clear();
        try {
            setContacts();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'contacts' not populated");
        }
        appContactComBox.setItems(contacts);
        
        customers.clear();
        try {
            setCustomers();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'customers' not populated");
        }
        appCustComBox.setItems(customers);
        
        users.clear();
        try {
            setUsers();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'users' not populated");
        }
        appUserComBox.setItems(users);
    }

    /**
     * Adds all Contacts to the contacts ObservableList from the database
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
     * Adds all Customers to the customers ObservableList from the database
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
     * Adds all Users to the users ObservableList from the database
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
            Contact contact = appContactComBox.getValue();
            Customer customer = appCustComBox.getValue();
            User user = appUserComBox.getValue();
            
            ZonedDateTime businessOpen = ZonedDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
                    LocalDateTime.now().getDayOfMonth(), 8, 0, 0, 0, ZoneId.of("America/New_York"));
            businessOpen = businessOpen.withZoneSameInstant(ZoneId.systemDefault());
            LocalTime businessOpenLocal = businessOpen.toLocalTime();
            
            ZonedDateTime businessClose = ZonedDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
                    LocalDateTime.now().getDayOfMonth(), 22, 0, 0, 0, ZoneId.of("America/New_York"));
            businessClose = businessClose.withZoneSameInstant(ZoneId.systemDefault());
            LocalTime businessCloseLocal = businessClose.toLocalTime();
            
            LocalDateTime start = LocalDateTime.of(Integer.parseInt(appStartYearTxt.getText()), 
                        Integer.parseInt(appStartMonthTxt.getText()), Integer.parseInt(appStartDayTxt.getText()), 
                        Integer.parseInt(appStartTimeHourTxt.getText()), Integer.parseInt(appStartTimeMinTxt.getText()));
            LocalTime startTime = start.toLocalTime();
            
            LocalDateTime end = LocalDateTime.of(Integer.parseInt(appEndYearTxt.getText()), 
                        Integer.parseInt(appEndMonthTxt.getText()), Integer.parseInt(appEndDayTxt.getText()), 
                        Integer.parseInt(appEndTimeHourTxt.getText()), Integer.parseInt(appEndTimeMinTxt.getText()));
            LocalTime endTime = end.toLocalTime();
            
            boolean timeOverlaps = false;
            
            for (Appointment a : Appointment.allApps){
                if (start.getYear() == a.getStartTime().getYear() && end.getYear() == a.getEndTime().getYear()
                        && start.getMonthValue() == a.getStartTime().getMonthValue() && end.getMonthValue() == 
                        a.getEndTime().getMonthValue() && start.getDayOfMonth() == a.getStartTime().getDayOfMonth() 
                        && end.getDayOfMonth() == a.getEndTime().getDayOfMonth() && !end.isBefore(a.getStartTime())
                        && !start.isAfter(a.getEndTime()) && a.getCustomerID() == customer.getID()){
                    
                    timeOverlaps = true;
                }
            }
            
            if (appTitleTxt.getText().isBlank() || appDescTxt.getText().isBlank() || 
                    appLocTxt.getText().isBlank() || appTypeTxt.getText().isBlank() || 
                    appStartYearTxt.getText().isBlank() || appEndYearTxt.getText().isBlank() || 
                    appStartMonthTxt.getText().isBlank() || appStartDayTxt.getText().isBlank() || 
                    appStartTimeHourTxt.getText().isBlank() || appStartTimeMinTxt.getText().isBlank() || 
                    appEndMonthTxt.getText().isBlank() || appEndDayTxt.getText().isBlank() || 
                    appEndTimeHourTxt.getText().isBlank() || appEndTimeMinTxt.getText().isBlank()){
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
                alert.showAndWait();
                
            } else if (startTime.isBefore(businessOpenLocal) || endTime.isAfter(businessCloseLocal)
                    || startTime.isAfter(businessCloseLocal) || endTime.isBefore(businessOpenLocal)){
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("unable to save appointment");
                alert.setHeaderText(null);
                alert.setContentText("Cannot schedule appointment outside of business hours: "
                        + "8:00am-10:00pm EST M-F");
                alert.showAndWait();
                
            } else if (timeOverlaps){
                
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
                
                String addApp = "INSERT INTO appointments (Title, Description, Location, "
                        + "Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                        + "VALUES ('" + appTitleTxt.getText() + "', '" + appDescTxt.getText() 
                        + "', '" + appLocTxt.getText() + "', '" + appTypeTxt.getText() + "', '" 
                        + Timestamp.valueOf(start) + "', '" + Timestamp.valueOf(end) + "', "
                        + customer.getID() + ", " + user.getID() + ", " + contact.getID() + ")";
                
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
