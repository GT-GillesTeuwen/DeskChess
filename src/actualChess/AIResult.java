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
public class AIResult {
    private Tile[][] retBrd;
    private MoveType type;
    private int oldX;
    private int newX;
    private int oldy;
    private int newy;

    
    public AIResult(Tile[][] retBrd, int oldX, int newX, int oldy, int newy, MoveType type) {
        this.retBrd = retBrd;
        this.oldX = oldX;
        this.newX = newX;
        this.oldy = oldy;
        this.newy = newy;
        this.type=type;
        
    }

    public Tile[][] getRetBrd() {
        return retBrd;
    }

    public int getOldX() {
        return oldX;
    }

    public int getNewX() {
        return newX;
    }

    public int getOldy() {
        return oldy;
    }

    public int getNewy() {
        return newy;
    }

    public MoveType getType() {
        return type;
    }



    
    
    
    
}
