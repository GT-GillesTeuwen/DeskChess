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

    /**
     * Label prompting the user to enter their chosen username
     */
    @FXML
    private Label userNameLbl;

    /**
     * Label prompting the user to enter their chosen password
     */
    @FXML
    private Label passwordLbl;

    /**
     * Label prompting the user to confirm their chosen password
     */
    @FXML
    private Label confirmPasswordLbl;

    /**
     * Label prompting the user to enter their chosen email address
     */
    @FXML
    private Label emailLbl;

    /**
     * Text field that allows the user to enter their chosen username
     */
    @FXML
    private TextField userNameFld;

    /**
     * Password field that allows the user to enter their chosen password
     */
    @FXML
    private PasswordField passwordFld;

    /**
     * Password field that allows the user to confirm their chosen password
     */
    @FXML
    private PasswordField confirmPasswordFld;

    /**
     * Text field that allows the user to enter their chosen email
     */
    @FXML
    private TextField emailFld;

    /**
     * Anchor pane that holds all the other UI elements
     */
    @FXML
    private AnchorPane pane;

    /**
     * Button that allows the user to sign up and validate their credentials
     */
    @FXML
    private Button signUpBtn;

    /**
     * Button that allows the user to return to the welcome screen
     */
    @FXML
    private Button cancelBtn;

    /**
     * Label that displays any errors encountered in the sign up process
     */
    @FXML
    private Label errorLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Executes when the sign up button is clicked Creates a user object to
     * validate. If the data is valid the user is created and signed in, then the sign up screen is
     * closed and the home screen is opened.
     *
     * If the data is invalid the relevant error is displayed in the error label
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void createUser(ActionEvent event) throws IOException, SQLException {
        User attempt = new User(userNameFld.getText(), passwordFld.getText(), confirmPasswordFld.getText(), emailFld.getText(), 0, 0);
        if (attempt.signUp() == true) {
            errorLbl.setText("Success");
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
            errorLbl.setText(attempt.getErr());
        }

    }

    /**
     * Executes when the cancel button is clicked. Closes the sign up screen and
     * open the welcome screen.
     *
     * @param event
     * @throws IOException
     */
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
