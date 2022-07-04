/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessMatchHelpScreen;

import chessjavafxmlbeta.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class MatchHelpScreenController implements Initializable {

    /**
     * 
     * Imageview that holds the background image
     */
    @FXML
    private ImageView viewImageBG;

    /**
     * The user currently signed in
     */
    private User currentUser;

    /**'
     * The image and image view of the back button
     */
    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);
    
    /**'
     * The image and image view of the toggled help button
     */
    Image backIconImageDark = new Image("/icons/DarkBackBtn.png");
    ImageView backImageViewDark = new ImageView(backIconImageDark);

    /**
     * The image and image view of the page's background
     */
    Image homeHelpBgImage = new Image("/icons/HomeScreenHelp.png");
    ImageView homeHelpBgImageView = new ImageView(homeHelpBgImage);

    /**
     * The button that allows the user to return to the match screen
     */
    @FXML
    private Button backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/MatchScreenHelp.png");
        viewImageBG.setImage(bgGameScreenImage);

        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * 
     * @param currentUser Sets the current user to the parsed in user
     */
    public void setCurretnUser(User user) {
        this.currentUser = user;
    }

    /**
     * Changes the image of the back button when the mouse exits it
     * @param event 
     */
    @FXML
    private void lightBackBtn(MouseEvent event) {
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    /**
     * Changes the image of the back button when the mouse exits it
     * @param event 
     */
    @FXML
    private void darkBackBtn(MouseEvent event) {
        backImageViewDark.setFitWidth(179);
        backImageViewDark.setFitHeight(48);
        backBtn.setGraphic(backImageViewDark);
    }

    /**
     * Closes the match help screen when the back button is clicked
     * @param event 
     */
    @FXML
    private void backToMatch(ActionEvent event) {
        Stage homeStage = (Stage) backBtn.getScene().getWindow();
        homeStage.close();
        //Pane scene = new Pane(ChessApp.createContent());

        /*try {
            ChessApp brd = new ChessApp();
            Pane boardPane = brd.createContent();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessMatchScreen/MatchScreen.fxml"));

            Pane root = loadWelcomeScreen.load();
            MatchScreenController match = loadWelcomeScreen.getController();
            match.setGame(brd);
            match.setCurrentUser(currentUser);
            root.getChildren().add(boardPane);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
        }*/
    }

}
