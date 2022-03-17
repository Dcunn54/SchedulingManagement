/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import utilities.DBQuery;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import utilities.Language;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

interface LoginAlert{
    void alert();
}

/**
 * FXML Controller class.
 * Lambda expression is used for the alert that shows when a login is unsuccessful
 * @author Daniel
 */
public class LoginController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    LoginAlert alert = () -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Language.translate("Login Unsuccessful"));
        alert.setHeaderText(null);
        alert.setContentText(utilities.Language.translate("The Username or Password you entered is incorrect"));
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.show();
    };
    
    @FXML
    private TextField userNameField;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordField;
        
    /**
     * Logs the user in provided they enter a correct username/password combination. 
     * Lambda expression is used here for alerting when a login is unsuccessful
     * @param event
     * @throws Exception 
     */
    @FXML
    void onActionLogin(ActionEvent event) throws Exception {
        
        String filename = "login_activity.txt", login;
        FileWriter loginActivityFW = new FileWriter(filename, true);
        PrintWriter loginActivityPW = new PrintWriter(loginActivityFW);
        
        String selectUserName = "SELECT * FROM users WHERE User_Name = '" + userNameField.getText() + "'";
        String selectPass = "SELECT * FROM users WHERE Password = '" + passwordField.getText() + "'";
        ResultSet resultSet1 = DBQuery.Query(selectUserName);
        if (resultSet1.next()){
            String pass = resultSet1.getString("Password");
            ResultSet resultSet2 = DBQuery.Query(selectPass);
            //resultSet2.next();
            if (resultSet2.next() && resultSet2.getString("Password").equals(pass)){
                
                login = "user: " + resultSet1.getString("User_Name")
                + " Date/Time: " + LocalDateTime.now() + " LOGIN SUCCESSFUL";
                loginActivityPW.println(login);
                loginActivityPW.close();
                
                MainScreenController.justLoggedIn = true;
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("");
                stage.show();
                
            }else{
                
                alert.alert();
                
                login = "user: " + resultSet1.getString("User_Name")
                + " Date/Time: " + LocalDateTime.now() + " LOGIN UNSUCCESSFUL";
                loginActivityPW.println(login);
                loginActivityPW.close();
            }
        }else{
            
            alert.alert();
            
            login = "user: " + userNameField.getText()
                + " Date/Time: " + LocalDateTime.now() + " LOGIN UNSUCCESSFUL";
            loginActivityPW.println(login);
            loginActivityPW.close();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
    }    
    
}
