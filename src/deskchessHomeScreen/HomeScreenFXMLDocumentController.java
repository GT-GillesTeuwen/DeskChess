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

    @FXML
    private ImageView viewImageBG;
    @FXML
    private Button newGamebtn;
    @FXML
    private Button continueGamebtn;
    @FXML
    private Button helpBtn;

    private User currentUser;

    //Variables
    Image newGameIconImage = new Image("/icons/NewGamebtn.png");
    ImageView newGameImageView = new ImageView(newGameIconImage);

    Image continueGameIconImage = new Image("/icons/ContinueGamebtn.png");
    ImageView continueGameImageView = new ImageView(continueGameIconImage);

    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    Image helpTogIconImage = new Image("/icons/helpBtnTog.png");
    ImageView helpTogImageView = new ImageView(helpTogIconImage);

    public static final int TILE_SIZE = 65;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private final Group tileGroup = new Group();
    private final Group pieceGroup = new Group();

    private final boolean turn = true;
    private Pane root1;
    @FXML
    private Label winsLbl;
    @FXML
    private Label lossesLbl;

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
        newGamebtn.setGraphic(newGameImageView);

        continueGameImageView.setFitWidth(270);
        continueGameImageView.setFitHeight(119);
        continueGamebtn.setGraphic(continueGameImageView);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
       
        
    }

    boolean btnTog = true;

    @FXML
    private void changeColour(ActionEvent event) {

    }

    @FXML
    private void btn1Smaller(MouseEvent event) {
        newGameImageView.setFitWidth(270);
        newGameImageView.setFitHeight(119);
        newGamebtn.setGraphic(newGameImageView);

    }

    @FXML
    private void btn1Bigger(MouseEvent event) {
        newGameImageView.setFitWidth(295);
        newGameImageView.setFitHeight(130);
        newGamebtn.setGraphic(newGameImageView);
    }

    @FXML
    private void btn2Smaller(MouseEvent event) {
        continueGameImageView.setFitWidth(270);
        continueGameImageView.setFitHeight(119);
        continueGamebtn.setGraphic(continueGameImageView);
    }

    @FXML
    private void btn2Bigger(MouseEvent event) {
        continueGameImageView.setFitWidth(295);
        continueGameImageView.setFitHeight(130);
        continueGamebtn.setGraphic(continueGameImageView);
    }

    public Tile[][] board = new Tile[WIDTH][HEIGHT];

    @FXML
    private void goToWelcome(ActionEvent event) {
        Stage homeStage = (Stage) continueGamebtn.getScene().getWindow();
        homeStage.close();
        //Pane scene = new Pane(ChessApp.createContent());

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

    @FXML
    private void contGame(MouseEvent event) {
        try {
            Stage homeStage = (Stage) continueGamebtn.getScene().getWindow();
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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        winsLbl.setText(currentUser.getWins()+"");
        lossesLbl.setText(currentUser.getLosses()+"");
    }

    @FXML
    private void displayHelp(ActionEvent event) {
        try {
            Stage homeStage = (Stage) continueGamebtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadHomeHelp = new FXMLLoader(getClass().getResource("/deskchessHomeHelpScreen/HomeHelpScreen.fxml"));
            Parent root = (Parent) loadHomeHelp.load();
            HomeHelpScreenController help = loadHomeHelp.getController();
            help.setCurretnUser(currentUser);
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/deskchessHomeHelpScreen/homehelpscreen.css");
            newStage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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

}
