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

    /**
     * The field in which the user can enter their password to sign in
     */
    @FXML
    private PasswordField passwordFld;

    /**
     * The field in which the user can enter their username to sign in
     */
    @FXML
    private TextField userNameFld;

    /**
     * Label indicating the function of the username field
     */
    @FXML
    private Label userNameLbl;

    /**
     * Label indicating the function of the password field
     */
    @FXML
    private Label passwordLbl;

    /**
     * Button used to validate the user's credentials. Signs them in if the
     * credentials are valid, displays an error if not
     */
    @FXML
    private Button signInBtn;

    /**
     * Button that returns the user to the welcome screen
     */
    @FXML
    private Button cancelBtn;

    /**
     * Label that displays any errors with the sign in process.
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
     * Executed when the sign in button is clicked. Signs the user in (opens the
     * home screen and closes the sign in screen) if the credentials are valid,
     * displays an error if not.
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void signIn(ActionEvent event) throws IOException, SQLException {
        User attempt = new User(userNameFld.getText(), passwordFld.getText());

        if (attempt.signIn() == true) {
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
     * Executes when the cancel button is clicked. Closes the sign in screen and
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
