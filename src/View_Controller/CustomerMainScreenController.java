/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Country;
import Model.Customer;
import Model.State;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilities.DBQuery;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class CustomerMainScreenController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerPostalCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, Country> customerCountryCol;
    @FXML
    private TableColumn<Customer, State> customerStateCol;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private Button modCustomerBtn;
    @FXML
    private Button delCustomerBtn;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            Customer.updateCustomerList();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        customerTable.setItems(Customer.getAllCustomers());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
    }    

    /**
     * Navigates to the add customer window
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionAddCustomer(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle(" ");
        stage.show();
        
    }

    /**
     * Navigates to the modify customer window, loading the information from the 
     * selected customer
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    private void onActionModCustomer(ActionEvent event) throws IOException, SQLException {
        
        if (customerTable.getSelectionModel().getSelectedItem() == null){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customer selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to modify");
            alert.showAndWait();
            
        } else {
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
            loader.load();
        
            ModifyCustomerController MCController = loader.getController();
            MCController.sendCustomer(customerTable.getSelectionModel().getSelectedItem());
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Deletes customer selected, checking to make sure customer is not attached 
     * to any appointments
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void onActionDelCustomer(ActionEvent event) throws SQLException {
        
        if (customerTable.getSelectionModel().getSelectedItem() == null){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customer selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();
            
        } else {
        
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            Appointment.updateAppsList();
            boolean isDeletable = false;
            
            for (Appointment a : Appointment.allApps){
                
                if (a.getCustomerID() == customer.getID()){
                    
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Unable to delete customer");
                    alert.setHeaderText(null);
                    alert.setContentText("Please make sure this customer is not attached "
                            + "to any appointments, and try again");
                    alert.showAndWait();
                    isDeletable = false;
                    break;
                } else {
                    isDeletable = true;
                }
            }
            
            if (isDeletable){
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Permanently delete customer?");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete customer " 
                        + customer + "? This cannot be undone");
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.get() == ButtonType.OK){    
                    String delCustomer = "DELETE FROM customers WHERE Customer_ID = "
                        + customer.getID();
                    DBQuery.Update(delCustomer);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Customer deleted");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Customer " + customer + " was deleted");
                    alert2.showAndWait();
                    Customer.updateCustomerList();
                }
            }
        }
    }

    /**
     * Returns to the main screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionReturn(ActionEvent event) throws IOException {
        
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
