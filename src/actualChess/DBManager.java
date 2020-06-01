/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import chessjavafxmlbeta.Board;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author SD10005375
 */
public class DBManager {

    /**
     * Holds the class's connection to the database file
     */
    private static Connection con;
    /**
     * Holds whether or not the database file has been created by querying
     * SQLite
     */
    private static boolean hasData = false;

    /**
     * Connects to the database file if not already connected
     *
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if (con == null) {
            getConnection();
        }
    }

    /**
     * Gets a connection to the database file and establishes con
     *
     * @throws SQLException
     */
    private void getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Deskchess.db");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        initialise();
    }

    /**
     * Creates the required table if they do not already exist
     *
     * @throws SQLException
     */
    private void initialise() throws SQLException {
        if (!hasData) {
            hasData = true;

            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Users'");

            if (!res.next()) {
                System.out.println("No Database Yet");
                state = con.createStatement();
                //state2.execute("CREATE TABLE user(id integer,fname varchar(60),lname varchar(60),primary key(id));");
                state.execute("CREATE TABLE \"Users\" (\n"
                        + "	\"Username\"	VARCHAR(25),\n"
                        + "	\"Password\"	VARCHAR(30),\n"
                        + "	\"email\"	VARCHAR(70),\n"
                        + "	PRIMARY KEY(\"Username\")\n"
                        + ");");
                state.execute("CREATE TABLE \"Boards\" (\n"
                        + "	\"Name\"	VARCHAR(35),\n"
                        + "	\"Date\"	DATE,\n"
                        + "	\"Time\"	VARCHAR(8),\n"
                        + "	\"User\"	VARCHAR(25),\n"
                        + "	\"Configuration\"	VARCHAR(71),\n"
                        + "	PRIMARY KEY(\"Name\",\"User\")\n"
                        + ");");

            }
        }
    }

    /**
     * Adds a new user to the database
     *
     * @param inUn receives the username chosen by the user
     * @param inPass receives the password chosen by the user
     * @param inEmail receives the email chosen by the user
     * @throws SQLException
     */
    public void addUser(String inUn, String inPass, String inEmail, int inWins, int inLosses) throws SQLException {
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("INSERT INTO Users values(?,?,?,?,?);");
        prep.setString(1, inUn);
        prep.setString(2, inPass);
        prep.setString(3, inEmail);
        prep.setString(4, inWins + "");
        prep.setString(5, inLosses + "");
        prep.execute();
    }

    /**
     * Adds a new game to the board table for later use
     *
     * @param inName receives the name to be given to the game
     * @param inDate receives the date on which the board was created to be
     * stored
     * @param inTime receives the time at which the board was created to be
     * stored
     * @param inUser receives the name of the user associated with the board
     * @param inConfig receives the piece layout of the board to be stored and
     * loaded
     * @throws SQLException
     */
    public void addBoard(String inName, String inDate, String inTime, String inUser, String inConfig) throws SQLException {
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("INSERT INTO Boards values(?,?,?,?,?);");
        prep.setString(1, inName);
        prep.setString(2, inDate);
        prep.setString(3, inTime);
        prep.setString(4, inUser);
        prep.setString(5, inConfig);
        prep.execute();
    }

    /**
     * Updates an existing board to reflect changes in the game configuration
     *
     * @param name receives the name of the board to be updated (partial key)
     * @param user receives the user that the game belongs to (partial key)
     * @param board receives the new configuration to be stored with the board
     * @throws SQLException
     */
    public void updateBoard(String name, String user, String board) throws SQLException {
        if (con == null) {
            getConnection();
        }
        String SQL = "UPDATE"
                + " Boards " + "SET Configuration=" + "\"" + board + "\" " + "WHERE Name=\"" + name + "\" AND User=\"" + user + "\"";
        System.out.println(SQL);
        PreparedStatement prep = con.prepareStatement(SQL);
        prep.execute();
    }

    /**
     * Returns a list of distinct values from a table
     *
     * @param search name of column
     * @param tbl name of table
     * @return Returns a list of distinct values from the table.
     * @throws SQLException
     */
    public ObservableList<String> getList(String search, String tbl) throws SQLException {
        ObservableList<String> info = FXCollections.observableArrayList();
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            res = state.executeQuery("SELECT DISTINCT " + tbl + "." + search + "\n"
                    + "FROM " + tbl);

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {

            String temp = res.getString(search);

            info.add(temp);

        }
        return info;
    }

    /**
     * Returns the names of all the boards associated with the given user
     *
     * @param user User who's boards are being queried
     * @return Returns the names of all the boards associated with the given
     * user
     * @throws SQLException
     */
    public ObservableList<String> getBoardNames(String user) throws SQLException {
        ObservableList<String> info = FXCollections.observableArrayList();
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            res = state.executeQuery("SELECT DISTINCT " + "Boards" + "." + "Name" + "\n"
                    + "FROM " + "Boards " + "WHERE User=\"" + user + "\";");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {

            String temp = res.getString("Name");

            info.add(temp);

        }
        return info;
    }

    /**
     * Returns a list of all the usernames and passwords to be checked against
     * inputs
     *
     * @return Returns a list of all the usernames and passwords to be checked
     * against inputs
     * @throws SQLException
     */
    public ObservableList<String> signIn() throws SQLException {
        ObservableList<String> info = FXCollections.observableArrayList();
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            res = state.executeQuery("SELECT DISTINCT " + "Users" + "." + "Username" + ", Users" + "." + "Password" + "\n"
                    + "FROM " + "Users");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {

            String un = res.getString("Username");
            String pass = res.getString("Password");

            info.add(un + "~" + pass);

        }
        return info;
    }

    /**
     * Returns all the boards associated with the given user
     *
     * @param user User whose games are being queried
     * @return Returns all the boards associated with the given user
     * @throws SQLException
     */
    public ObservableList<Board> getBoards(String user) throws SQLException {
        ObservableList<Board> info = FXCollections.observableArrayList();
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            res = state.executeQuery("SELECT * FROM " + "Boards WHERe User=\"" + user + "\";");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {

            Board temp = new Board(res.getString("Name"), res.getString("Date"), res.getString("Time"), res.getString("User"), res.getString("Configuration"));

            info.add(temp);

        }
        return info;
    }

    /**
     * Deletes the game selected by the user
     *
     * @param name Name of the board to be deleted (PK)
     * @param user User deleting the board (PK)
     * @throws SQLException
     */
    public void deleteBoard(String name, String user) throws SQLException {
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("Delete"
                + " FROM Boards "
                + "WHERE Name=\"" + name + "\" AND User=\"" + user + "\"");
        prep.execute();
    }

    public void addWin(String user) throws SQLException {
        if (con == null) {
            getConnection();
        }
        String SQL = "UPDATE Users\n"
                + "SET Wins = ((SELECT WINS FROM Users WHERE Username=\"" + user + "\")+1)\n"
                + "WHERE Username=\"" + user + "\"";
        System.out.println(SQL);
        PreparedStatement prep = con.prepareStatement(SQL);
        prep.execute();
    }

    public void addLoss(String user) throws SQLException {
        if (con == null) {
            getConnection();
        }
        String SQL = "UPDATE Users\n"
                + "SET Losses = ((SELECT Losses FROM Users WHERE Username=\"" + user + "\")+1)\n"
                + "WHERE Username=\"" + user + "\"";
        System.out.println(SQL);
        PreparedStatement prep = con.prepareStatement(SQL);
        prep.execute();
    }

    public int getWins(String user) throws SQLException {
        int wins = 0;
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            System.out.println(user);
            res = state.executeQuery("SELECT Wins FROM Users WHERE Username=\"" + user + "\";");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {
            System.out.println(res.getString("Wins"));
            wins = Integer.parseInt(res.getString("Wins"));

        }
        return wins;
    }

    public int getLosses(String user) throws SQLException {
        int losses = 0;
        ResultSet res = null;
        if (con == null) {
            getConnection();
        }
        try {
            Statement state = con.createStatement();
            res = state.executeQuery("SELECT Losses FROM Users WHERE Username=\"" + user + "\";");

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (res.next()) {
            losses = Integer.parseInt(res.getString("Losses"));

        }
        return losses;
    }
}
