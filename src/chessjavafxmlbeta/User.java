/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessjavafxmlbeta;

import actualChess.DBManager;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Client
 */
public class User {

    /**
     * Holds the username that the user has chosen to associate with their
     * account
     */
    private String userName;

    /**
     * Holds the password that the user has chosen to associate with their
     * account
     */
    private String password;

    /**
     * Holds the string that the user entered to ensure that they did not make a
     * mistake when entering their password
     */
    private String confirmPassword;

    /**
     * Holds the email address that the user has chosen to associate with their
     * account
     */
    private String email;

    /**
     * Holds the error that was encountered by the user if the sign up or sign
     * in process did not succeed
     */
    private String err;

    
    private int wins;
    private int losses;
    /**
     * Initialises a Database Manager that facilitates communication with the
     * embedded database with regards to the user class
     */
    private DBManager db = new DBManager();

    /**
     * Creates a new user to be signed up and validated
     *
     * @param userName Receives the username that the user has chosen to
     * associate with their account
     * @param password Receives the password that the user has chosen to
     * associate with their account
     * @param confirmPassword Receives he string that the user entered to ensure
     * that they did not make a mistake when entering their password
     * @param email Receives the email address that the user has chosen to
     * associate with their account
     */
    public User(String userName, String password, String confirmPpassword, String email, int wins,int losses) {
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPpassword;
        this.email = email;
        this.wins=wins;
        this.losses=losses;       
    }

    /**
     * Creates a new user to be checked against the text file of existing users
     * and associated passwords
     *
     * @param userName Receives the username of the account that the user wants
     * to sign in to
     * @param password Receives the password of the account that the user wants
     * to sign in to
     */
    public User(String userName, String password) {
        try {
            this.userName = userName;
            this.password = password;
            this.wins=db.getWins(userName);
            this.losses=db.getLosses(userName);
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return Returns the username that the user has chosen to associate with
     * their account
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return Returns the email address that the user has chosen to associate
     * with their account
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return Returns the error that was encountered by the user if the sign up
     * or sign in process did not succeed
     */
    public String getErr() {
        return err;
    }

    /**
     * Used when signing up, this method ensures: that the username given is
     * unique, that the user's password entries match, that the email is valid
     * and unique. Once all the data is deemed valid the user's details are
     * added to the database via the DBManager
     *
     * @return Returns true if the details were added successfully and false if
     * an error was encountered. Also sets the error that was encountered to the
     * err variable
     *
     * @throws SQLException. This throws the exception to the prompt that called
     * it to inform the user of an error in their entry
     */
    public boolean signUp() throws SQLException {
        ObservableList<String> info = FXCollections.observableArrayList();
        info = db.getList("Username", "Users");
        if (info.contains(userName)) {
            err = "Username already in use";
            return false;
        }
        info = db.getList("email", "Users");
        if (info.contains(email)) {
            err = "Email already in use";
            return false;
        }
        if (password.equals("")) {
            err = "Password cannot be blank";
            return false;
        }

        if (password.equals(confirmPassword)) {
            if (email.contains("@")) {
                db.addUser(userName, password, email,0,0);
                return true;
            } else {
                err = "Invalid E-mail";
                return false;
            }
        } else {
            err = "Paswords do not match";
            return false;
        }

    }

    /**
     * Used when the user is signing in to an existing account, this method
     * checks if the username entered exists in the database then checks if the password entered
     * is associated with the username entered
     *
     * @return Returns true if the given username exists in the database and the given password
     * is associated with it and false if not
     * 
     * @throws SQLException This throws the exception to the prompt that called it to inform the user of an error in their entry
     * regarding the SQL
     */
    public boolean signIn() throws SQLException {
        ObservableList<String> info = db.signIn();
        for (int i = 0; i < info.size(); i++) {
            String[] details = info.get(i).split("~");
            if (userName.equals(details[0])) {
                if (password.equals(details[1])) {
                    return true;
                } else {
                    err = "Invalid Password";
                    return false;
                }
            }
        }
        err = "Invalid username";
        return false;
    }
    public void addWin() throws SQLException{
        db.addWin(userName);
        this.wins=db.getWins(userName);
    }
    public void addLoss() throws SQLException{
        db.addLoss(userName);
        this.losses=db.getLosses(userName);
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
    
    
    
    

}
