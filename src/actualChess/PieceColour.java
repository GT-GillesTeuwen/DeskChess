/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

/**
 *
 * @author Client
 */
public enum PieceColour {
    BLACK(1), WHITE(-1);
    public final int moveDir;

    PieceColour(int moveDir) {
        this.moveDir=moveDir;
    }
}
