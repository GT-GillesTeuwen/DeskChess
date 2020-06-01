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

    @FXML
    private ImageView viewImageBG;

    private User currentUser;

    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);

    Image DbackIconImage = new Image("/icons/DarkBackBtn.png");
    ImageView DbackImageView = new ImageView(DbackIconImage);

    Image homeHelpBgImage = new Image("/icons/HomeScreenHelp.png");
    ImageView homeHelpBgImageView = new ImageView(homeHelpBgImage);
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

    public void setCurretnUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void lightB(MouseEvent event) {
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    @FXML
    private void darkB(MouseEvent event) {
        DbackImageView.setFitWidth(179);
        DbackImageView.setFitHeight(48);
        backBtn.setGraphic(DbackImageView);
    }

    @FXML
    private void backToMatch(ActionEvent event) throws SQLException {
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
