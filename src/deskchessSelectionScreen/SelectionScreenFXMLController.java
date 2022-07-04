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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class SelectionScreenFXMLController implements Initializable {

    /**
     * The image view that holds the background image of the page
     */
    @FXML
    private ImageView MatchBG;

    /**
     * The table view populated with board objects that holds the user's
     * unfinished games
     */
    @FXML
    private TableView<Board> loadGamesTbl;

    /**
     * The user currently signed in
     */
    private User currentUser;

    /**
     * Button used by the user to load and continue the selected saved game
     */
    @FXML
    private Button loadBtn;

    /**
     * Button used by the user to return to the home screen
     */
    @FXML
    private Button backBtn;

    /**
     * Image and image view of the load/continue button
     */
    Image continueIconImageLight = new Image("/icons/ContinueBtn.png");
    ImageView continueImageViewLight = new ImageView(continueIconImageLight);

    /**
     * Image and image view of the back button
     */
    Image backIconImageLight = new Image("/icons/BackBtn.png");
    ImageView backImageViewLight = new ImageView(backIconImageLight);

    /**
     * Image and image view of the hovered load/continue button
     */
    Image continueIconImageDark = new Image("/icons/DarkContinueBtn.png");
    ImageView continueImageViewDark = new ImageView(continueIconImageDark);

    /**
     * Image and image view of the hovered back button
     */
    Image backIconImageDark = new Image("/icons/DarkBackBtn.png");
    ImageView backImageViewDark = new ImageView(backIconImageDark);

    /**
     * Image and image view of the help button
     */
    Image helpIconImage = new Image("/icons/helpBtn.png");
    ImageView helpImageView = new ImageView(helpIconImage);

    /**
     * Image and image view of the hovered help button
     */
    Image helpIconImageToggled = new Image("/icons/helpBtnTog.png");
    ImageView helpImageViewToggled = new ImageView(helpIconImageToggled);

    /**
     * The help button used to open the associated help page
     */
    @FXML
    private Button helpBtn;

    /**
     * Button used by the user to delete saved games they wish to abandon
     */
    @FXML
    private MenuItem deleteBTN;

    /**
     * Anchor pane holding all other UI elements
     */
    @FXML
    private AnchorPane anchor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/GameScreenBG.jpg");
        MatchBG.setImage(bgGameScreenImage);

        continueImageViewDark.setFitWidth(179);
        continueImageViewDark.setFitHeight(48);

        backImageViewDark.setFitWidth(179);
        backImageViewDark.setFitHeight(48);

        continueImageViewLight.setFitWidth(179);
        continueImageViewLight.setFitHeight(48);
        loadBtn.setGraphic(continueImageViewLight);

        backImageViewLight.setFitWidth(179);
        backImageViewLight.setFitHeight(48);
        backBtn.setGraphic(backImageViewLight);

        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);

    }

    /**
     * Sets the screen user to the parsed in user
     *
     * Initializes and populates the table with that users games read in from
     * the database
     *
     * @param currentUser The user currently signed in
     * @throws SQLException
     */
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

        loadGamesTbl.setItems(db.getBoards(currentUser.getUserName()));
        loadGamesTbl.getColumns().addAll(gameNameCol, gameDateCol, gameTimeCol);

    }

    /**
     * Executes when the load/continue button is clicked Fetches the selected
     * game's name and configuration then parses it through to the match screen
     * that is opened. Sets the user of the match screen to the signed in user.
     * Closes the selection screen.
     *
     * @param event
     */
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

    /**
     * Executes when the back button is clicked.
     *
     * Open the home screen and sets the user to the signed in user.
     *
     * Closes the selection screen.
     *
     * @param event
     * @throws IOException
     */
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

    /**
     * Changes the continue button to the light image when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void continueBtnLight(MouseEvent event) {
        continueImageViewLight.setFitWidth(179);
        continueImageViewLight.setFitHeight(48);
        loadBtn.setGraphic(continueImageViewLight);
    }

    /**
     * Changes the continue button to the dark image when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void continueBtnDark(MouseEvent event) {
        continueImageViewLight.setFitWidth(179);
        continueImageViewLight.setFitHeight(48);
        loadBtn.setGraphic(continueImageViewDark);
    }

    /**
     * Changes the back button to the light image when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void backBtnLight(MouseEvent event) {
        backImageViewLight.setFitWidth(179);
        backImageViewLight.setFitHeight(48);
        backBtn.setGraphic(backImageViewLight);
    }

    /**
     * Changes the back button to the dark image when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void backBtnDark(MouseEvent event) {
        backImageViewDark.setFitWidth(179);
        backImageViewDark.setFitHeight(48);
        backBtn.setGraphic(backImageViewDark);
    }

    /**
     * Executes when the help button is clicked Opens the help screen associated
     * with this page Closes this page
     *
     * @param event
     */
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

    /**
     * Set the help button to the un-toggled when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void helpBtnUntog(MouseEvent event) {
        helpImageView.setFitWidth(50);
        helpImageView.setFitHeight(50);
        helpBtn.setGraphic(helpImageView);
    }

    /**
     * Set the help button to the toggled when the mouse exits it
     *
     * @param event
     */
    @FXML
    private void helpBtnTog(MouseEvent event) {
        helpImageViewToggled.setFitWidth(50);
        helpImageViewToggled.setFitHeight(50);
        helpBtn.setGraphic(helpImageViewToggled);
    }

    /**
     * Executed when the user clicks the delete game button. Deletes the
     * selected game from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void deleteGame(ActionEvent event) throws SQLException {
        Board board = loadGamesTbl.getSelectionModel().getSelectedItem();
        DBManager db = new DBManager();
        System.out.println(board.getName() + " " + board.getUser());
        db.deleteBoard(board.getName(), board.getUser());
        loadGamesTbl.setItems(db.getBoards(currentUser.getUserName()));

    }

    /**
     * Called when the user selects a game from the table.
     *
     * A representation of the board is loaded in next to the table to preview
     * the state of the selected game.
     */
    @FXML
    private void displayBrd(MouseEvent event) {
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
