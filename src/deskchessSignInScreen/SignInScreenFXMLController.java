/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessSignInScreen;

import chessjavafxmlbeta.User;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import deskchessSelectionScreen.SelectionScreenFXMLController;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class SignInScreenFXMLController implements Initializable {

    @FXML
    private PasswordField passwordFld;
    @FXML
    private TextField userNameFld;
    @FXML
    private Label userNameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private Button signInBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorFld;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void signIn(ActionEvent event) throws IOException, SQLException {
        User attempt = new User(userNameFld.getText(), passwordFld.getText());
        
        if (attempt.signIn() == true) {
            errorFld.setText("Success");
            Stage homeStage = (Stage) cancelBtn.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
            match.setCurrentUser(attempt);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            errorFld.setText(attempt.getErr());
        }

    }

    @FXML
    private void goToWelcome(ActionEvent event) throws IOException {
        Stage homeStage = (Stage) cancelBtn.getScene().getWindow();
        homeStage.close();
        FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessWelcomeScreen/WelcomeScreenFXML.fxml"));
        Parent root = (Parent) loadWelcomeScreen.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

}
