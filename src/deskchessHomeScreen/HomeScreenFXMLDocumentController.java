/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessHomeScreen;

import actualChess.ChessApp;
import actualChess.Tile;
import chessjavafxmlbeta.User;
import deskchessMatchScreen.MatchScreenController;
import deskchessSelectionScreen.SelectionScreenFXMLController;
import deskchessHomeHelpScreen.HomeHelpScreenController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class HomeScreenFXMLDocumentController implements Initializable {

    /**
     * The imageview that holds the background image
     */
    @FXML
    private ImageView viewImageBG;
    
    /**
     * The button that allows the user to go to the saved game selection screen
     */
    @FXML
    private Button continueGameBtn;
    
    /**
     * The button that allows the user open the help page
     */
    @FXML
    private Button helpBtn;

    /**
     * The current user using the app
     */
    private User currentUser;

   
    
    /**
     * The image and imageview of the new game button
     */
    Image newGameIconImage = new Image("/icons/NewGamebtn.png");
    ImageView newGameImageView = new ImageView(newGameIconImage);

    /**'
     * The image and imageview of the continue game button
     */
    Image continueGameIconImage = new Image("/icons/ContinueGamebtn.png");
    ImageView continueGameImageView = new ImageView(continueGameIconImage);

    /**'
     * The image and imageview of the help button
     */
    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    /**'
     * The image and imageview of the toggled help button
     */
    Image helpTogIconImage = new Image("/icons/helpBtnTog.png");
    ImageView helpTogImageView = new ImageView(helpTogIconImage);

    /**
     * The pixel width and height of a tile
     */
    public static final int TILE_SIZE = 65;
    
    /**
     * The dimensions of the board
     */
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    /**
    * A label that displays the users total wins  
    */
    @FXML
    private Label winsLbl;
    /**
     * A label that displays the users total losses
     */
    @FXML
    private Label lossesLbl;

    /**
     * Boolean that holds whether the the help button is toggled
     */
    private boolean btnTog = true;
    
    /**
     * The button used to start a new game
     */
    @FXML
    private Button newGameBtn;
   

    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        Image bgHomeScreenImage = new Image("/icons/HomeScreenBG.png");
        viewImageBG.setImage(bgHomeScreenImage);

        newGameImageView.setFitWidth(270);
        newGameImageView.setFitHeight(119);
        newGameBtn.setGraphic(newGameImageView);

        continueGameImageView.setFitWidth(270);
        continueGameImageView.setFitHeight(119);
        continueGameBtn.setGraphic(continueGameImageView);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
       
        
    }

    

    
     /**
     * 
     * @param event makes the new game button smaller when the mouse exits it
     */
    @FXML
    private void newGameBtnSmaller(MouseEvent event) {
        newGameImageView.setFitWidth(270);
        newGameImageView.setFitHeight(119);
        newGameBtn.setGraphic(newGameImageView);

    }

    /**
     * 
     * @param event makes the new game button bigger when the mouse enters it
     */
    @FXML
    private void newGameBtnBigger(MouseEvent event) {
        newGameImageView.setFitWidth(295);
        newGameImageView.setFitHeight(130);
        newGameBtn.setGraphic(newGameImageView);
    }

    /**
     * 
     * @param event makes the continue game button smaller when the mouse exits it
     */
    @FXML
    private void continueGameBtnSmaller(MouseEvent event) {
        continueGameImageView.setFitWidth(270);
        continueGameImageView.setFitHeight(119);
        continueGameBtn.setGraphic(continueGameImageView);
    }

    /**
     * 
     * @param event makes the continue game button bigger when the mouse enters it
     */
    @FXML
    private void continueGameBtnBigger(MouseEvent event) {
        continueGameImageView.setFitWidth(295);
        continueGameImageView.setFitHeight(130);
        continueGameBtn.setGraphic(continueGameImageView);
    }

    
    /**
     * 
     * @param currentUser Sets the current user to the parsed in user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        winsLbl.setText(currentUser.getWins()+"");
        lossesLbl.setText(currentUser.getLosses()+"");
    }

    /**
     * Opens the help screen when clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void displayHelp(ActionEvent event) throws IOException {
        
            try {
            Stage homeStage = (Stage) continueGameBtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeHelpScreen/HomeHelpScreen.fxml"));
            Parent root = loadWelcomeScreen.load();
            HomeHelpScreenController match = loadWelcomeScreen.getController();
            match.setCurretnUser(currentUser);
            Stage newStage = new Stage();
           
            Scene scene = new Scene(root);
            
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Changes the image of the help button when the mouse enters it
     * @param event 
     */
    @FXML
    private void helpBtnUntog(MouseEvent event) {
        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
    }

    /**
     * Changes the image of the help button when the mouse exits it
     * @param event 
     */
    @FXML
    private void helpBtnTog(MouseEvent event) {
        helpTogImageView.setFitWidth(50);
        helpTogImageView.setFitHeight(50);
        helpBtn.setGraphic(helpTogImageView);
    }

    /**
     * Opens the match screen with the default setup and closes the home screen
     * @param event 
     */
    @FXML
    private void goToNewMatch(ActionEvent event) {
        Stage homeStage = (Stage) continueGameBtn.getScene().getWindow();
        homeStage.close();

        try {
            ChessApp brd = new ChessApp();
            Pane boardPane = brd.createContent();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessMatchScreen/MatchScreen.fxml"));

            Pane root = loadWelcomeScreen.load();
            MatchScreenController match = loadWelcomeScreen.getController();
            brd.setScreen(match);
            match.setGame(brd);
            match.setCurrentUser(currentUser);
            
            System.out.println(currentUser.getLosses());
            
            match.setLoaded(false);
            root.getChildren().add(boardPane);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
        }
    }

    
    /**
     *  Opens the selection screen with and closes the home screen
     * @param event 
     */
    @FXML
    private void continueGame(ActionEvent event) {
         try {
            Stage homeStage = (Stage) continueGameBtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessSelectionScreen/SelectionScreenFXML.fxml"));
            Parent root = loadWelcomeScreen.load();
            SelectionScreenFXMLController match = loadWelcomeScreen.getController();
            try {
                match.setCurrentUser(currentUser);
            } catch (SQLException ex) {
                Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
