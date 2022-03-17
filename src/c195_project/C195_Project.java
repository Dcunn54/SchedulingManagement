/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195_project;

import javafx.application.Application;
import utilities.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;
import utilities.Language;


/**
 *
 * @author Daniel
 */
public class C195_Project extends Application{

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        
        DBConnection.startConnection();

        launch(args);
        
        DBConnection.closeConnection();
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("utilities/Nat", Language.getLocale());
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"), bundle);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(Language.translate("User Login, Timezone-") + ZoneId.systemDefault());
        stage.show();
    }
    
}
