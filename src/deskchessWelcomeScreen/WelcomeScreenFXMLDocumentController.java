/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessWelcomeScreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.awt.Graphics;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Client
 */
public class WelcomeScreenFXMLDocumentController implements Initializable {
    /**
     * Button that allows the user to create a new account
     */
    @FXML
    private Button signUpBtn;
    
    /**
     * Button that allows the user to sign in to an existing account
     */
    @FXML
    private Button signInBtn;
    
    /**
     * Image view used to display the logo
     */
    @FXML
    private ImageView viewImage;

    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image("/icons/Logo.PNG");

        viewImage.setImage(image);
    }

    /**
     * Opens the sign up screen 
     * Closes the welcome screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void openSignUpScreen(ActionEvent event) throws IOException {
        Stage homeStage = (Stage) signInBtn.getScene().getWindow();
        homeStage.close();
        FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessSignUpscreen/signUpScreenFXML.fxml"));
        Parent root = (Parent) loadWelcomeScreen.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    /**
     * Opens the sign in screen 
     * Closes the welcome screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void openSignInscreen(ActionEvent event) throws IOException {
        Stage homeStage = (Stage) signInBtn.getScene().getWindow();
        homeStage.close();
        FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessSignInScreen/signInScreenFXML.fxml"));
        Parent root = (Parent) loadWelcomeScreen.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

}
