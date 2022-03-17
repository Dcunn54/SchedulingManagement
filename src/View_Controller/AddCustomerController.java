/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Country;
import Model.State;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utilities.DBQuery;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class AddCustomerController implements Initializable{
    
    Stage stage;
    Parent scene;
    
    ObservableList<Country> countries = FXCollections.observableArrayList();
    ObservableList<State> states = FXCollections.observableArrayList();
    
    @FXML
    private ComboBox<Country> countryComBox;
    @FXML
    private ComboBox<State> stateComBox;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField phoneNumTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField postalCodeTxt;
    @FXML
    private Button saveCustomer;
    @FXML
    private Button returnToMain;
    
    /**
     * Initializes the controller class.
     * @param arg0
     * @param arg1 
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        
        countries.clear();
        try {
            setCountries();
        } catch (SQLException ex) {
            System.out.println("OBSERVABLE LIST 'countries' not populated");
        }
        countryComBox.setItems(countries);
        
    }
    
    /**
     * Populates ObservableList of countries from the database to show in the 
     * combo box
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
     * Populates the ObservableList of states/provinces from the database based 
     * on the user's country selection to show in the combo box
     * @throws SQLException 
     */
    public void setStates() throws SQLException{
        
        String selectStates = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = "
                + countryComBox.getValue().getID();
        ResultSet stateSet = DBQuery.Query(selectStates);
        
        while(stateSet.next()){
            String name = stateSet.getString("Division");
            int ID = stateSet.getInt("Division_ID");
            int countryID = stateSet.getInt("COUNTRY_ID");
            states.add(new State(name, ID, countryID));
        }
    }
    
    /**
     * Populates the state/province combo box once a country is selected
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onActionCountrySelected(ActionEvent event) throws SQLException {
        
        states.clear();
        stateComBox.setValue(null);
        setStates();
        stateComBox.setItems(states);

    }

    /**
     * Returns to the main screen
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        
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

    /**
     * Saves the customer to the database
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws SQLException {
        
        try {
            
            if (nameTxt.getText().isBlank() || addressTxt.getText().isBlank() 
                    || postalCodeTxt.getText().isBlank() || phoneNumTxt.getText().isBlank()){
                
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
                alert.showAndWait();
                
            } else {
                
                State state = stateComBox.getValue();
                Country country = countryComBox.getValue();
        
                String addCustomer = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) "
                        + "VALUES ('" + nameTxt.getText() + "', '" + addressTxt.getText() + "', '" + 
                        postalCodeTxt.getText() + "', '" + phoneNumTxt.getText() + "', " + state.getID() + ")";
        
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Customer");
                alert.setHeaderText(null);
                alert.setContentText("Save Customer and Return to Customer Main Screen?");
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.get() == ButtonType.OK){
                    DBQuery.Update(addCustomer);
                    
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomerMainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.setTitle("");
                    stage.show();
                }
            }
            
        } catch (Exception e) {
            
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong, please check the information"
                    + " entered and try again");
            alert.showAndWait();
        }
    }
    
}
