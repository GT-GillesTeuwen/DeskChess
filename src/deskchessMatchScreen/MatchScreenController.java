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
    private DBManager db = new DBManager();
    private boolean loaded;
    private String gameName;
    @FXML
    private ImageView MatchBG;
    @FXML
    private Button resignbtn;
    @FXML
    private Button saveAndQuitbtn;
    private User currentUser;
    private ChessApp game;

    Image resignIconImage = new Image("/icons/Resignbtn.png");
    ImageView resignImageView = new ImageView(resignIconImage);

    Image resignDarkIconImage = new Image("/icons/Resignbtn(Hover).png");
    ImageView resignDarkImageView = new ImageView(resignDarkIconImage);

    Image saveAndQuitIconImage = new Image("/icons/saveAndQuitbtn.png");
    ImageView saveAndQuitImageView = new ImageView(saveAndQuitIconImage);

    Image saveAndQuitDarkIconImage = new Image("/icons/saveAndQuitbtn(Hover).png");
    ImageView saveAndQuitDarkImageView = new ImageView(saveAndQuitDarkIconImage);

    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    Image helpTogIconImage = new Image("/icons/helpBtnTog.png");
    ImageView helpTogImageView = new ImageView(helpTogIconImage);
    @FXML
    private Label whiteWinsLbl;
    @FXML
    private ProgressBar winningSlider;
    @FXML
    private Button helpBtn;
    @FXML
    private Circle turnIndicator;
    @FXML
    private ProgressBar thinkBar;

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

        resignbtn.setGraphic(resignImageView);
        saveAndQuitbtn.setGraphic(saveAndQuitImageView);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);

    }
    public static final int TILE_SIZE = 70;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    public Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Group legalGroup = new Group();

    private boolean turn = true;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void resignbtnLight(MouseEvent event) {
        resignbtn.setGraphic(resignImageView);
    }

    @FXML
    private void resignbtnDark(MouseEvent event) {
        resignbtn.setGraphic(resignDarkImageView);
    }

    @FXML
    private void saveAndQuitbtnLight(MouseEvent event) {
        saveAndQuitbtn.setGraphic(saveAndQuitImageView);
    }

    @FXML
    private void saveAndQuitbtnDark(MouseEvent event) {
        saveAndQuitbtn.setGraphic(saveAndQuitDarkImageView);
    }

    @FXML
    private void saveAndQuit(ActionEvent event) throws IOException, SQLException {
        System.out.println(loaded);
        if (loaded) {
            game.overwrite(currentUser, gameName);

            Stage homeStage = (Stage) resignbtn.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
            match.setCurrentUser(currentUser);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            Stage homeStage = (Stage) saveAndQuitbtn.getScene().getWindow();
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

    public void setGame(ChessApp game) {
        this.game = game;

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void checkCheckMate() throws SQLException {
        if (game.getCheckMate() == 1) {
            whiteWinsLbl.setTextFill(Color.BLACK);
            currentUser.addWin();
            db.deleteBoard(gameName, currentUser.getUserName());
        }
        winningSlider.setProgress(game.getScore());
        if (game.isTurn()) {
            turnIndicator.setFill(Color.WHITE);
        }else{
            turnIndicator.setFill(Color.BLACK);
        }
    }
   
    

    @FXML
    private void helpBtnUntog(MouseEvent event) {
        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
    }

    @FXML
    private void helpBtnTog(MouseEvent event) {
        helpTogImageView.setFitWidth(50);
        helpTogImageView.setFitHeight(50);
        helpBtn.setGraphic(helpTogImageView);
    }

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

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

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

    public void incrementThink(double progress){
        thinkBar.setProgress(progress);
    }

}
