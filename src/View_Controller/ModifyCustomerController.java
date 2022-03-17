/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Country;
import Model.Customer;
import Model.State;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.DBQuery;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class ModifyCustomerController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    ObservableList<Country> countries = FXCollections.observableArrayList();
    ObservableList<State> states = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Country> modCountryComBox;
    @FXML
    private ComboBox<State> modStateComBox;
    @FXML
    private TextField modNameTxt;
    @FXML
    private TextField modPhoneNumTxt;
    @FXML
    private TextField modAddressTxt;
    @FXML
    private TextField modPostalCodeTxt;
    @FXML
    private Button saveModCustomer;
    @FXML
    private Button returnToMain;
    @FXML
    private TextField modCustomerID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Populates the Customer information based on the user's selection on the 
     * Customer Main Screen
     * @param customer 
     */
    public void sendCustomer(Customer customer){
        
        modCustomerID.setText(String.valueOf(customer.getID()));
        modNameTxt.setText(String.valueOf(customer.getName()));
        modPhoneNumTxt.setText(String.valueOf(customer.getPhoneNum()));
        modAddressTxt.setText(String.valueOf(customer.getAddress()));
        modPostalCodeTxt.setText(String.valueOf(customer.getPostalCode()));
        modCountryComBox.setValue(customer.getCountry());
        modStateComBox.setValue(customer.getState());
        
        countries.clear();
        try {
            setCountries();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'countries' not populated");
        }
        modCountryComBox.setItems(countries);
        
        states.clear();
        try {
            setStates();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'states' not populated");
        }
        modStateComBox.setItems(states);
    }
    
    /**
     * Updates the ObservableList of Countries from the database in order to 
     * populate the combo box
     * @throws SQLException 
     */
    public void setCountries() throws SQLException{
                
        String selectCountries = "SELECT * FROM countries";
        ResultSet countrySet = DBQuery.Query(selectCountries);
        
        while (countrySet.next()){
            String name = countrySet.getString("Country");
            int ID = countrySet.getInt("Country_ID");
            countries.add(new Country(name, ID));
        }
        
    }
    
    /**
     * Updates the ObservableList of States/Provinces from the database based on 
     * the user's Country selection in order to populate the combo box
     * @throws SQLException 
     */
    public void setStates() throws SQLException{
                
        String selectStates = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = "
                + modCountryComBox.getValue().getID();
        ResultSet stateSet = DBQuery.Query(selectStates);
        
        while(stateSet.next()){
            String name = stateSet.getString("Division");
            int ID = stateSet.getInt("Division_ID");
            int countryID = stateSet.getInt("COUNTRY_ID");
            states.add(new State(name, ID, countryID));
        }
    }

    /**
     * Populates the state/province combo box based on the user's country selection
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void onActionCountrySelected(ActionEvent event) throws SQLException {
        
        states.clear();
        modStateComBox.setValue(null);
        setStates();
    }

    /**
     * Saves the customer to the database
     * @param event 
     */
    @FXML
    private void onActionSaveCustomer(ActionEvent event) {
        
        try {
            
            if (modNameTxt.getText().isBlank() || modAddressTxt.getText().isBlank() 
                    || modPostalCodeTxt.getText().isBlank() || modPhoneNumTxt.getText().isBlank()){
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
                alert.showAndWait();
                
            } else {
                
                State state = modStateComBox.getValue();
                Country country = modCountryComBox.getValue();
        
                String updateCustomer = "UPDATE customers SET Customer_Name = '" + modNameTxt.getText() + 
                        "', Address = '" + modAddressTxt.getText() + "', Postal_Code = '" + 
                        modPostalCodeTxt.getText() + "', Phone = '" + modPhoneNumTxt.getText() + 
                        "', Division_ID = " + state.getID() + " WHERE Customer_ID = " + modCustomerID.getText();
               
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Customer");
                alert.setHeaderText(null);
                alert.setContentText("Save Customer and Return to Customer Main Screen?");
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.get() == ButtonType.OK){
                    DBQuery.Update(updateCustomer);
                    
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomerMainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.setTitle("");
                    stage.show();
                }
            }
            
        } catch (Exception e) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
            alert.showAndWait();
        } 
    }

    /**
     * Returns to the customer main screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionReturnToMain(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Main");
        alert.setHeaderText(null);
        alert.setContentText("Return to Customer Main Screen? All unsaved data will be lost");
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
