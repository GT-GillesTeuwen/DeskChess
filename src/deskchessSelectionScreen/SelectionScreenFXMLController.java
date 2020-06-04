/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessSelectionScreen;

import actualChess.ChessApp;
import actualChess.DBManager;
import chessjavafxmlbeta.Board;
import chessjavafxmlbeta.User;
import deskchessHomeHelpScreen.HomeHelpScreenController;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import deskchessMatchScreen.MatchScreenController;
import deskchessSelectionHelpScreen.SelectionHelpScreenController;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class SelectionScreenFXMLController implements Initializable {

    @FXML
    private ImageView MatchBG;
    @FXML
    private TableView<Board> loadGamesTbl;

    private User currentUser;
    @FXML
    private Button loadBtn;
    @FXML
    private Button backBtn;

    Image continueIconImage = new Image("/icons/ContinueBtn.png");
    ImageView continueImageView = new ImageView(continueIconImage);

    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);

    Image DcontinueIconImage = new Image("/icons/DarkContinueBtn.png");
    ImageView DcontinueImageView = new ImageView(DcontinueIconImage);

    Image DbackIconImage = new Image("/icons/DarkBackBtn.png");
    ImageView DbackImageView = new ImageView(DbackIconImage);

    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    Image helpTogIconImage = new Image("/icons/helpBtnTog.png");
    ImageView helpTogImageView = new ImageView(helpTogIconImage);
    @FXML
    private Button helpBtn;
    @FXML
    private MenuItem deleteBTN;
    @FXML
    private AnchorPane anchor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/GameScreenBG.jpg");
        MatchBG.setImage(bgGameScreenImage);

        DcontinueImageView.setFitWidth(179);
        DcontinueImageView.setFitHeight(48);

        DbackImageView.setFitWidth(179);
        DbackImageView.setFitHeight(48);

        continueImageView.setFitWidth(179);
        continueImageView.setFitHeight(48);
        loadBtn.setGraphic(continueImageView);

        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);

    }

    public void setCurrentUser(User currentUser) throws SQLException {
        this.currentUser = currentUser;

        TableColumn<Board, String> gameNameCol = new TableColumn<>("Name");
        gameNameCol.setMinWidth(145);
        gameNameCol.getStyleClass().add("gameNameCol");
        gameNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Board, String> gameDateCol = new TableColumn<>("Date");
        gameDateCol.setMinWidth(100);
        gameDateCol.getStyleClass().add("gameNameCol");
        gameDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Board, String> gameTimeCol = new TableColumn<>("Time");
        gameTimeCol.setMinWidth(100);
        gameTimeCol.getStyleClass().add("gameNameCol");
        gameTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        DBManager db = new DBManager();
        //loadGamesTbl.getStyleClass().add("gameNamecol");
        loadGamesTbl.setItems(db.getBoards(currentUser.getUserName()));
        loadGamesTbl.getColumns().addAll(gameNameCol, gameDateCol, gameTimeCol);

    }

    @FXML
    private void loadGame(ActionEvent event) {
        Board board = loadGamesTbl.getSelectionModel().getSelectedItem();
        Stage homeStage = (Stage) loadBtn.getScene().getWindow();
        homeStage.close();
        try {
            ChessApp brd = new ChessApp();
            Pane boardPane = brd.loadContent(board.getConfiguration());
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessMatchScreen/MatchScreen.fxml"));

            Pane root = loadWelcomeScreen.load();
            MatchScreenController match = loadWelcomeScreen.getController();

            match.setGame(brd);
            match.setCurrentUser(currentUser);
            match.setLoaded(true);
            match.setGameName(board.getName());
            brd.setScreen(match);
            root.getChildren().add(boardPane);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void backToHome(ActionEvent event) throws IOException {
        Stage homeStage = (Stage) backBtn.getScene().getWindow();
        homeStage.close();
        FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
        Parent root = (Parent) loadWelcomeScreen.load();
        HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
        match.setCurrentUser(currentUser);
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    private void light(MouseEvent event) {
        continueImageView.setFitWidth(179);
        continueImageView.setFitHeight(48);
        loadBtn.setGraphic(continueImageView);
    }

    @FXML
    private void dark(MouseEvent event) {
        continueImageView.setFitWidth(179);
        continueImageView.setFitHeight(48);
        loadBtn.setGraphic(DcontinueImageView);
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
    private void displayHelp(ActionEvent event) {
        try {
            Stage homeStage = (Stage) helpBtn.getScene().getWindow();
            homeStage.close();

            FXMLLoader loadHomeHelp = new FXMLLoader(getClass().getResource("/deskchessSelectionHelpScreen/SelectionHelpScreen.fxml"));
            Parent root = (Parent) loadHomeHelp.load();
            SelectionHelpScreenController help = loadHomeHelp.getController();
            help.setCurretnUser(currentUser);
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/deskchessSelectionHelpScreen/selectionhelpscreen.css");
            newStage.setScene(scene);
            newStage.show();
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

    @FXML
    private void DeleteGame(ActionEvent event) throws SQLException {
        Board board = loadGamesTbl.getSelectionModel().getSelectedItem();
        DBManager db = new DBManager();
        System.out.println(board.getName() + " " + board.getUser());
        db.deleteBoard(board.getName(), board.getUser());
        loadGamesTbl.setItems(db.getBoards(currentUser.getUserName()));

    }

    @FXML
    private void displayBrd(MouseEvent event) {
        System.out.println("AAAAAAA");
        try {
            Board board = loadGamesTbl.getSelectionModel().getSelectedItem();
            System.out.println(board.getConfiguration());
            Stage homeStage = (Stage) helpBtn.getScene().getWindow();
            ChessApp display = new ChessApp();
            Pane brd = display.display(board.getConfiguration());
            anchor.getChildren().add(brd);
        } catch (Exception e) {
        }

    }

}
