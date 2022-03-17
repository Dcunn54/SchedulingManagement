/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class ReportsController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    
    ObservableList<Month> months = FXCollections.observableArrayList();
    ObservableList<String> types = FXCollections.observableArrayList();
    ObservableList<String> locations = FXCollections.observableArrayList();
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<Contact> contacts = FXCollections.observableArrayList();
    
    ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();


    @FXML
    private ComboBox<Month> appMonthComBox;
    @FXML
    private ComboBox<String> appTypeComBox;
    @FXML
    private TextField appCountTypeTxt;
    @FXML
    private TextField appCountMonthTxt;
    @FXML
    private ComboBox<Contact> appContactComBox;
    @FXML
    private TextArea contactScheduleTxt;
    @FXML
    private ComboBox<Customer> appCustComBox;
    @FXML
    private ComboBox<String> appLocComBox;
    @FXML
    private TextField appCountCustTxt;
    @FXML
    private TextField appCountLocTxt;
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Appointment> contactSchecduleTbl;
    @FXML
    private TableColumn<Appointment, Integer> appIDCol;
    @FXML
    private TableColumn<Appointment, String> appTitleCol;
    @FXML
    private TableColumn<Appointment, String> appTypeCol;
    @FXML
    private TableColumn<Appointment, String> appDescCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appStartCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEndCol;
    @FXML
    private TableColumn<Appointment, Integer> appCustIDCol;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            Customer.updateCustomerList();
            Appointment.updateAppsList();
            Contact.updateContactList();
            
            for (Month m : Month.values()){
                months.add(m);
            }
            appMonthComBox.setItems(months);
            
            for (Appointment a : Appointment.allApps){
                
                if (!types.contains(a.getType().strip())){
                    types.add(a.getType());
                }
                if(!locations.contains(a.getLocation().strip())){
                    locations.add(a.getLocation());
                }
            }
            for (Customer c : Customer.allCustomers){
                if(!customers.contains(c)){
                    customers.add(c);
                }
            }
            for (Contact ct : Contact.allContacts){
                if(!contacts.contains(ct)){
                    contacts.add(ct);
                }
            }
            appTypeComBox.setItems(types);
            appLocComBox.setItems(locations);
            appCustComBox.setItems(customers);
            appContactComBox.setItems(contacts);
            
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contactSchecduleTbl.setItems(contactSchedule);
        appIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("startTimeFormatted"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("endTimeFormatted"));
        appCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        
    }

    /**
     * Displays how many appointments are scheduled within the selected month
     * @param event 
     */
    @FXML
    private void onActionAppsByMonth(ActionEvent event) {
        
        int monthCount = 0;
        for (Appointment a : Appointment.allApps){
            if (a.getStartTime().getMonth().equals(appMonthComBox.getValue())){
                monthCount++;
            }
        }
        appCountMonthTxt.setText(String.valueOf(monthCount));
    }

    /**
     * Displays how many appointments are of the selected type
     * @param event 
     */
    @FXML
    private void onActionAppsByType(ActionEvent event) {
        
        int typeCount = 0;
        for (Appointment a : Appointment.allApps){
            if (a.getType().equals(appTypeComBox.getValue())){
                typeCount++;
            }
        }
        appCountTypeTxt.setText(String.valueOf(typeCount));
    }

    /**
     * Displays the selected contact's schedule in the TableView
     * @param event 
     */
    @FXML
    private void onActionContactSchedule(ActionEvent event) {
        
        contactSchedule.clear();
        for (Appointment a : Appointment.allApps){
            if (a.getContactID() == appContactComBox.getValue().getID()){
                contactSchedule.add(a);
            }
        }
    }

    /**
     * Displays how many appointments the selected customer has
     * @param event 
     */
    @FXML
    private void onActionAppsByCust(ActionEvent event) {
        
        int custCount = 0;
        for (Appointment a : Appointment.allApps){
            if (a.getCustomerID() == appCustComBox.getValue().getID()){
                custCount++;
            }
        }
        appCountCustTxt.setText(String.valueOf(custCount));
    }

    /**
     * Displays how many appointments are in the selected location
     * @param event 
     */
    @FXML
    private void onActionAppsByLoc(ActionEvent event) {
        
        int locCount = 0;
        for (Appointment a : Appointment.allApps){
            if (a.getLocation().equals(appLocComBox.getValue())){
                locCount++;
            }
        }
        appCountLocTxt.setText(String.valueOf(locCount));
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
        alert.setContentText("Return to Main Screen?");
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
