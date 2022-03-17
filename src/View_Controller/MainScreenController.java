/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Contact;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.DBQuery;

interface NextScreen{
    void nextScreen(String path, ActionEvent event) throws IOException;
}

/**
 * FXML Controller class.
 * Lambda expression used here for navigating to the next screen based on which 
 * button is clicked
 * @author Daniel
 */
public class MainScreenController implements Initializable{
   
    Stage stage;
    Parent scene;
    
    NextScreen nextScreen = (String path, ActionEvent event) ->{
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(path));
        stage.setScene(new Scene(scene));
        stage.setTitle("");
        stage.show();
    };
    
    public static boolean justLoggedIn;

    @FXML
    private TableView<Appointment> appTable;
    @FXML
    private TableColumn<Appointment, Integer> appIDCol;
    @FXML
    private TableColumn<Appointment, String> appTitleCol;
    @FXML
    private TableColumn<Appointment, String> appDescCol;
    @FXML
    private TableColumn<Appointment, String> appLocCol;
    @FXML
    private TableColumn<Appointment, Contact> appContactCol;
    @FXML
    private TableColumn<Appointment, String> appTypeCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appStartTimeCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEndTimeCol;
    @FXML
    private TableColumn<Appointment, Integer> appCustomerIDCol;
    @FXML
    private Button viewCustomersBtn;
    @FXML
    private Button addAppBtn;
    @FXML
    private Button modAppBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private RadioButton viewAllRadio;
    @FXML
    private RadioButton viewByMonthRadio;
    @FXML
    private RadioButton viewByWeekRadio;
    @FXML
    private Button delAppBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            Appointment.updateAppsList();
        } catch (SQLException ex){
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        appTable.setItems(Appointment.getAllApps());
        appIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeFormatted"));
        appEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeFormatted"));
        appCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        
        viewAllRadio.setSelected(true);
        
        LocalDateTime now = LocalDateTime.now();
        for (Appointment a : Appointment.allApps){
            if (a.getStartTime().isAfter(now) && a.getStartTime().isBefore(now.plusMinutes(15))
                    && justLoggedIn){
                
                justLoggedIn = false;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment within 15 minutes!");
                alert.setHeaderText(null);
                alert.setContentText("Appointment '" + a.getTitle() + "' is within 15 mins from now!"
                + "\n" + "Appointment ID: " + a.getID() + "\nAppointment date/time: " + a.getStartTimeFormatted());
                alert.showAndWait();
                
            }
        }
        if (justLoggedIn){
            
            justLoggedIn = false;
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Appointment within 15 minutes");
            alert.setHeaderText(null);
            alert.setContentText("There are no upcoming appointments within "
                + "15 minutes");
            alert.showAndWait();
        }
        
        justLoggedIn = false;
    }    

    /**
     * Navigates to the customer main screen window. Lambda is used here
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionViewCustomers(ActionEvent event) throws IOException {
        
        nextScreen.nextScreen("/View_Controller/CustomerMainScreen.fxml", event);
        
    }

    /**
     * Navigates to the add appointment window. Lambda is used here
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionAddApp(ActionEvent event) throws IOException {
        
        nextScreen.nextScreen("/View_Controller/AddAppointment.fxml", event);
    }

    /**
     * Navigates to the modify appointment window. Lambda is used here
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    private void onActionModApp(ActionEvent event) throws IOException, SQLException {
        
        if (appTable.getSelectionModel().getSelectedItem() == null){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No appointment selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to modify");
            alert.showAndWait();
            
        } else {
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/ModifyAppointment.fxml"));
            loader.load();
        
            ModifyAppointmentController MAController = loader.getController();
            MAController.sendAppointment(appTable.getSelectionModel().getSelectedItem());
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     * Deletes appointment from the database
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onActionDelApp(ActionEvent event) throws SQLException {
        
        if (appTable.getSelectionModel().getSelectedItem() == null){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No appointment selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete");
            alert.showAndWait();
            
        } else {
            
            Appointment app = appTable.getSelectionModel().getSelectedItem();
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Permanently delete appointment?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete appointment "
                    + app.getTitle() + "? This cannot be undone");
            Optional<ButtonType> result = alert.showAndWait();
                
            if (result.get() == ButtonType.OK){
                
                String delApp = "DELETE FROM appointments WHERE Appointment_ID = "
                    + app.getID();
                
                //System.out.println(delApp);
                DBQuery.Update(delApp);
                
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Appointment deleted");
                alert2.setHeaderText(null);
                alert2.setContentText("Appointment " + app.getTitle() + " was deleted"
                + "\n" + "\n" + "Appointment ID: " + app.getID() + "\n" + "Appointment Type: "
                + app.getType());
                alert2.showAndWait();
                
                Appointment.updateAppsList();
            }
        }
    }

    /**
     * Navigates to the reports window. Lambda is used here
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionReports(ActionEvent event) throws IOException {
        
        nextScreen.nextScreen("/View_Controller/Reports.fxml", event);
    }

    /**
     * Closes the program
     * @param event 
     */
    @FXML
    private void onActionExitProgram(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
                
        if (result.get() == ButtonType.OK){    
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Modifies the ObservableList to show all appointments in the TableView
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void onActionViewAll(ActionEvent event) throws SQLException {
        
        viewByWeekRadio.setSelected(false);
        viewByMonthRadio.setSelected(false);
        viewAllRadio.setSelected(true);
        Appointment.updateAppsList();
        appTable.setItems(Appointment.getAllApps());
    }

    /**
     * Modifies the ObservableList to show all appointments scheduled for the 
     * current month in the TableView
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void onActionViewByMonth(ActionEvent event) throws SQLException {
        
        Appointment.updateAppsList();
        viewAllRadio.setSelected(false);
        viewByWeekRadio.setSelected(false);
        viewByMonthRadio.setSelected(true);
        Appointment.viewByMonth();
        appTable.setItems(Appointment.getAllApps());
    }

    /**
     * Modifies the ObservableList to show all appointments scheduled for the 
     * current week in the TableView
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void onActionViewByWeek(ActionEvent event) throws SQLException {
        
        Appointment.updateAppsList();
        viewAllRadio.setSelected(false);
        viewByMonthRadio.setSelected(false);
        viewByWeekRadio.setSelected(true);
        Appointment.viewByWeek();
        appTable.setItems(Appointment.getAllApps());
    }
    
}
