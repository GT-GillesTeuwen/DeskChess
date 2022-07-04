/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessSelectionHelpScreen;

import chessjavafxmlbeta.User;
import deskchessHomeHelpScreen.HomeHelpScreenController;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import deskchessSelectionScreen.SelectionScreenFXMLController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
public class SelectionHelpScreenController implements Initializable {

    /**
     * The imaqe view that is used to display the background image
     */
    @FXML
    private ImageView viewImageBG;

    /**
     * The user currently signed in
     */
    private User currentUser;

    /**
     * The image and image view of the un-hovered back button
     */
    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);

    /**
     * The image and image view of the hovered back button
     */
    Image backIconImageDark = new Image("/icons/DarkBackBtn.png");
    ImageView backImageViewDark = new ImageView(backIconImageDark);

    /**
     * The image and image view of the annotated help screen
     */
    Image homeHelpBgImage = new Image("/icons/HomeScreenHelp.png");
    ImageView homeHelpBgImageView = new ImageView(homeHelpBgImage);

    /**
     * The button used to return to the selection screen and close the help
     * screen
     */
    @FXML
    private Button backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/SelectionScreenHelp.png");
        viewImageBG.setImage(bgGameScreenImage);

        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * Sets the current user to the user from the previous screen
     *
     * @param user The user from the previous screen
     */
    public void setCurretnUser(User user) {
        this.currentUser = user;
    }

    /**
     * Sets the image used for the button to the light variant when the mouse
     * exits it
     *
     * @param event
     */
    @FXML
    private void lightBackBtn(MouseEvent event) {
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * Sets the image used for the button to the dark variant when the mouse
     * enters it
     *
     * @param event
     */
    @FXML
    private void darkBackBtn(MouseEvent event) {
        backImageViewDark.setFitWidth(179);
        backImageViewDark.setFitHeight(48);
        backBtn.setGraphic(backImageViewDark);
    }

    /**
     * Closes the help page and opens the saved game selection screen when the
     * back button is clicked
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void backToSelectionScreen(ActionEvent event) throws SQLException {
        try {
            Stage homeStage = (Stage) backBtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessSelectionScreen/SelectionScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            SelectionScreenFXMLController match = loadWelcomeScreen.getController();
            match.setCurrentUser(currentUser);
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/deskchessSelectionScreen/CSS.css");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
