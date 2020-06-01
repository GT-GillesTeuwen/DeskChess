/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessjavafxmlbeta;

/**
 *
 * @author Client
 */
public class Board {

    /**
     * Holds the name given to the board as a string
     */
    private String name;

    /**
     * Holds the date on which the board was created as a string
     */
    private String date;

    /**
     * Holds the time at which the board was created as a string
     */
    private String time;

    /**
     * Holds the name of the user that created the board as a string
     */
    private String user;

    /**
     * Holds a string which represents the layout of the board to be
     * saved/loaded
     */
    private String configuration;

    /**
     * Creates a new Board object with its given name, created date, created
     * time, and the user that created it
     *
     * @param name Receives the name given to the board as a string
     * @param date Receives the date on which the board was created as a string
     * @param time Receives the time at which the board was created as a string
     * @param user Receives the name of the user that saved the board as a
     * string
     */
    public Board(String name, String date, String time, String user, String config) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.user = user;
        this.configuration = config;
    }

    /**
     * @return Returns the name given to the board as a string
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the name of the user that saved the board as a string
     */
    public String getUser() {
        return user;
    }

    /**
     * @return Returns the date on which the board was created as a string
     */
    public String getDate() {
        return date;
    }

    /**
     * @return Returns the time at which the board was created as a string
     */
    public String getTime() {
        return time;
    }

    /**
     * @returns Returns a string which represents the layout of the board to be
     * saved/loaded
     */
    public String getConfiguration() {
        return configuration;
    }

}
