/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessSignUpscreen;

import chessjavafxmlbeta.User;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class SignUpScreenFXMLController implements Initializable {

    @FXML
    private Label userNameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private Label confirmPasswordLbl;
    @FXML
    private Label emailLbl;
    @FXML
    private TextField userNameFld;
    @FXML
    private PasswordField confirmPasswordFld;
    @FXML
    private TextField emailFld;
    @FXML
    private AnchorPane Pane;
    @FXML
    private PasswordField passwordFld;
    @FXML
    private Button signUpBtn;
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
    private void createUser(ActionEvent event) throws IOException, SQLException {
        User attempt = new User(userNameFld.getText(), passwordFld.getText(), confirmPasswordFld.getText(), emailFld.getText(),0,0);
        if (attempt.signUp() == true) {
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
