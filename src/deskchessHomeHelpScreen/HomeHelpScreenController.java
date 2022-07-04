/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessHomeHelpScreen;

import chessjavafxmlbeta.User;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class HomeHelpScreenController implements Initializable {

    /**'
     * Image view that holds the background image
     */
    @FXML
    private ImageView viewImageBG;

    /**
     * The user that is currently signed in
     */
    private User currentUser;
    
    /**
     * The image and image view for the back button
     */
    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);
    
    /**
     * The image and image view for the hovered back button
     */
    Image backIconImageDark = new Image("/icons/DarkBackBtn.png");
    ImageView backImageViewDark = new ImageView(backIconImageDark);
    
  
    @FXML
    /**
     * The button that allows the user to return to the home screen
     */
    private Button backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/HomeScreenHelp.png");
        viewImageBG.setImage(bgGameScreenImage);
        
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * 
     * @param user Sets the current user  
     */
    public void setCurretnUser(User user){
        this.currentUser=user;
    }
    
    /**
     * Makes the button light when not hovered over
     * @param event 
     */
    @FXML
    private void lightBtn(MouseEvent event) {
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * Makes the button dark when hovered over
     * @param event 
     */
    @FXML
    private void darkBtn(MouseEvent event) {
        backImageViewDark.setFitWidth(179);
        backImageViewDark.setFitHeight(48);
        backBtn.setGraphic(backImageViewDark);
    }

    @FXML
    
    /**
     * Allows the user to close the help page and return to the home page
     */
    private void backToHome(ActionEvent event) {
        try {
            Stage homeStage = (Stage) backBtn.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
            match.setCurrentUser(currentUser);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeHelpScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
