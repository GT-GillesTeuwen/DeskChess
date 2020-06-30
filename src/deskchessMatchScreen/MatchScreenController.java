/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessMatchScreen;

import actualChess.ChessApp;
import actualChess.DBManager;
import actualChess.Tile;
import chessjavafxmlbeta.User;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import deskchessMatchHelpScreen.MatchHelpScreenController;
import deskchessNamingScreen.NamingScreenFXMLController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class MatchScreenController implements Initializable {

    /**
     * The Database manager used to save the game and increment the wins/losses
     */
    private final DBManager db = new DBManager();

    /**
     * A boolean that indicates whether the board needs to be loaded in or can
     * be set up normally
     */
    private boolean loaded;

    /**
     * Stores the name of the the current game if it loaded in
     */
    private String gameName;

    /**
     * The imageview that holds the background image
     */
    @FXML
    private ImageView MatchBG;

    /**
     * Button that allows the user to resign from the game
     */
    @FXML
    private Button resignBtn;

    /**
     * Button that allows the user to save and quit
     */
    @FXML
    private Button saveAndQuitBtn;

    /**
     * Holds the current user that is using the app
     */
    private User currentUser;

    /**
     * Links the match screen to the ChessApp so that data can be parsed between
     * them
     */
    private ChessApp game;

    /**
     * The imageview and image of the non-toggled resign button
     */
    Image resignIconImage = new Image("/icons/Resignbtn.png");
    ImageView resignImageView = new ImageView(resignIconImage);

    /**
     * The imageview and image of the tooggled resign button
     */
    Image resignDarkIconImage = new Image("/icons/Resignbtn(Hover).png");
    ImageView resignDarkImageView = new ImageView(resignDarkIconImage);

    /**
     * The imageview and image of the non-toggled save and quit button
     */
    Image saveAndQuitIconImage = new Image("/icons/saveAndQuitbtn.png");
    ImageView saveAndQuitImageView = new ImageView(saveAndQuitIconImage);

    /**
     * The imageview and image of the toggled save and quit button
     */
    Image saveAndQuitDarkIconImage = new Image("/icons/saveAndQuitbtn(Hover).png");
    ImageView saveAndQuitDarkImageView = new ImageView(saveAndQuitDarkIconImage);

    /**
     * The imageview and image of the toggled help button
     */
    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    /**
     * The imageview and image of the non-toggled help button
     */
    Image helpTogIconImage = new Image("/icons/helpBtnTog.png");
    ImageView helpTogImageView = new ImageView(helpTogIconImage);

    /**
     * A label that displays when white wins
     */
    @FXML
    private Label whiteWinsLbl;

    /**
     * A progress bar that shows who is winning
     */
    @FXML
    private ProgressBar winningSlider;

    /**
     * A button that opens the help page
     */
    @FXML
    private Button helpBtn;

    /**
     * A circle that changes colour to indicate whose turn it is
     */
    @FXML
    private Circle turnIndicator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Image bgGameScreenImage = new Image("/icons/GameScreenBG.jpg");
        MatchBG.setImage(bgGameScreenImage);

        resignImageView.setFitWidth(179);
        resignImageView.setFitHeight(48);

        resignDarkImageView.setFitWidth(179);
        resignDarkImageView.setFitHeight(48);

        saveAndQuitImageView.setFitWidth(179);
        saveAndQuitImageView.setFitHeight(48);

        saveAndQuitDarkImageView.setFitWidth(179);
        saveAndQuitDarkImageView.setFitHeight(48);

        resignBtn.setGraphic(resignImageView);
        saveAndQuitBtn.setGraphic(saveAndQuitImageView);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);

    }

    /**
     * Set the resign button image to a light image when the mouse exits it
     * @param event
     */
    @FXML
    private void resignbtnLight(MouseEvent event) {
        resignBtn.setGraphic(resignImageView);
    }

    /**
     * Set the resign button image to a dark image when the mouse enters it
     * @param event
     */
    @FXML
    private void resignbtnDark(MouseEvent event) {
        resignBtn.setGraphic(resignDarkImageView);
    }

    /**
     * Set the save and quit button image to a light image when the mouse exits it
     * @param event
     */
    @FXML
    private void saveAndQuitbtnLight(MouseEvent event) {
        saveAndQuitBtn.setGraphic(saveAndQuitImageView);
    }

    /**
     * Set the save and quit button image to a dark image when the mouse enters it
     * @param event
     */
    @FXML
    private void saveAndQuitbtnDark(MouseEvent event) {
        saveAndQuitBtn.setGraphic(saveAndQuitDarkImageView);
    }

    
    /**
     * Opens the naming screen if the game is new
     * Overwrites the old game configuration in the database if it was loaded in
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    private void saveAndQuit(ActionEvent event) throws IOException, SQLException {
        System.out.println(loaded);
        if (loaded) {
            game.overwrite(currentUser, gameName);

            Stage homeStage = (Stage) resignBtn.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
            match.setCurrentUser(currentUser);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            Stage homeStage = (Stage) saveAndQuitBtn.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessNamingScreen/NamingScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            NamingScreenFXMLController match = loadWelcomeScreen.getController();
            match.setAll(game, currentUser);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        }
    }

    /**
     * Sets the ChessApp of this screen to the one being displayed to allow for interaction
     * @param game 
     */
    public void setGame(ChessApp game) {
        this.game = game;

    }

    /**
     * Sets the user to the user currently using the app
     * @param currentUser 
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Executes when checkmate has occurred in the ChessApp
     * @throws SQLException 
     */
    public void checkCheckMate() throws SQLException {
        if (game.getCheckMate() == 1) {
            whiteWinsLbl.setTextFill(Color.BLACK);
            currentUser.addWin();
            db.deleteBoard(gameName, currentUser.getUserName());
        }
        winningSlider.setProgress(game.getScore());
        if (game.isTurn()) {
            turnIndicator.setFill(Color.WHITE);
        } else {
            turnIndicator.setFill(Color.BLACK);
        }
    }

    /**
     * Changes the help button to the un-toggled picture
     * @param event 
     */
    @FXML
    private void helpBtnUntog(MouseEvent event) {
        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
    }

    /**
     * Changes the help button to the toggled picture
     * @param event 
     */
    @FXML
    private void helpBtnTog(MouseEvent event) {
        helpTogImageView.setFitWidth(50);
        helpTogImageView.setFitHeight(50);
        helpBtn.setGraphic(helpTogImageView);
    }

    /**
     * Opens the help screen
     * @param event 
     */
    @FXML
    private void displayHelp(ActionEvent event) {
        try {
            Stage homeStage = (Stage) helpBtn.getScene().getWindow();
            //homeStage.close();

            FXMLLoader loadHomeHelp = new FXMLLoader(getClass().getResource("/deskchessMatchHelpScreen/MatchHelpScreen.fxml"));
            Parent root = (Parent) loadHomeHelp.load();
            MatchHelpScreenController help = loadHomeHelp.getController();
            help.setCurretnUser(currentUser);
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/deskchessMatchHelpScreen/matchhelpscreen.css");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the loaded boolean if the game is loaded in
     * @param loaded 
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Sets the name of the game if it's loaded in
     * @param gameName 
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Quits the game and deletes it then counts it as a loss to the user
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void resign(ActionEvent event) throws SQLException {
        currentUser.addLoss();
        db.deleteBoard(gameName, currentUser.getUserName());
        try {
            Stage homeStage = (Stage) helpBtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadHomeHelp = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadHomeHelp.load();
            HomeScreenFXMLDocumentController help = loadHomeHelp.getController();
            help.setCurrentUser(currentUser);
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/deskchessHomeScreen/homescreen.css");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
