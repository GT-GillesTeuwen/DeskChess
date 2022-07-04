/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import deskchessMatchScreen.MatchScreenController;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Client
 */
public class RecursiveAI extends Thread {

    /**
     * Holds the 2d tile array that represents the current state of the board
     */
    private Tile[][] currentBoard;
    /**
     * Holds a String representation of the current board
     */
    private String stringVersionOfBoard;
    /**
     * Holds the score of the current board
     */
    private int score;

    /**
     * Holds the move the computer has decided to make
     */
    private RecursiveAIResult result;
    
    /**
     * An array list holding all the possible moves the AI could make
     */
    private ArrayList<RecursiveAIResult> viableMoves;
    
    /**
     * A ChessApp allowing the AI to communicate with the chess game
     */
    private ChessApp chessApp;
    
    /**
     * The Match screen controller allowing the AI to communicate with the Match UI
     */
    private MatchScreenController screen;

    /**
     * Constructor, Creates a new RecursiveAI linking it to the screen and ChessApp
     */
    public RecursiveAI(ChessApp chessApp, MatchScreenController screen) {
        this.chessApp = chessApp;
        this.screen = screen;

    }

    /**
     *
     * @return Returns the 2d tile array that represents the current state of
     * the board
     */
    public Tile[][] getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Overrides the current board in favor of an updated one
     *
     * @param currentBoard Receives the current board to be evaluated for the
     * best move
     */
    public void setCurrentBoard(Tile[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     *
     * @return Returns a String representation of the current board
     */
    public String getBrdPrint() {
        return stringVersionOfBoard;
    }

    /**
     * Overrides the current String representation of the current board with an
     * up to date one
     *
     * @param brdPrint Receives a String version of the board to assign a score
     * to
     */
    public void setBrdPrint(String brdPrint) {
        this.stringVersionOfBoard = brdPrint.replace("\n", "");
        this.score = calculateScore(this.stringVersionOfBoard);
    }

    /**
     *
     * @return Returns a numerical value to indicate how good or bad the current
     * board is for the computer player
     */
    public int getScore() {
        return score;
    }

    /**
     * Called by the AI player on the current board stored in order to decide
     * which move is best to make, then makes that move.
     *
     */
    public void makeMove() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("1");
        // Populates an array list with all the valid moves
        viableMoves = thinkMove(currentBoard, false);
        ArrayList<Integer> good = new ArrayList<>();
        int[] moveScores = new int[viableMoves.size()];

        int bestMove = 0;

        int minScore = 10000000;
        
        //Creating threads that look n moves ahead of each viable move and adding them to the array
        MinimaxThread[] allThreads = new MinimaxThread[viableMoves.size()];
        for (int i = 0; i < viableMoves.size(); i++) {
            System.out.println("MiniMax " + i + "/" + viableMoves.size());
            MinimaxThread temp = new MinimaxThread(viableMoves.get(i).getRetBrd(), i, moveScores,2);
            allThreads[i] = temp;
            allThreads[i].start();
            System.out.println("ID " + allThreads[i].getId() + " State " + allThreads[i].getState());
        }
        
        boolean flag = false;
        
        //Waiting for threads to finish
        while (flag == false) {
            flag = true;
            for (int i = 0; i < allThreads.length; i++) {
                
                if (allThreads[i].getState() == Thread.State.RUNNABLE) {
                    
                    flag = false;
                }
            }
            
        }
        //FInding the best move/moves and selecting one.
        for (int i = 0; i < viableMoves.size(); i++) {           
            if (moveScores[i] < minScore) {
                bestMove = i;
                minScore = moveScores[i];
            }
        }
        for (int i = 0; i < viableMoves.size(); i++) {
            if (moveScores[i] == minScore) {
                good.add(i);
            }
        }
        if (good.size() > 0) {
            int rand = good.get((int) ((Math.random()) * (good.size())));
            result = viableMoves.get(rand);
            //return viableMoves.get(rand);
        } else {
            result = new RecursiveAIResult(currentBoard, 0, 0, 0, 0, MoveType.NONE, true);
        }

        //Making the move on the ChessApp
        Tile[][] checkBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(arrayCopy(currentBoard, checkBrd));       
        Piece oppPiece = chessApp.getBoard()[result.getOldX()][result.getOldy()].getPiece();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        System.out.println(dateFormat.format(date));
        if (null != result.getType()) {
            switch (result.getType()) {
                case KILL:

                    oppPiece.setMoved(true);
                    Piece otherPiece2 = chessApp.getBoard()[result.getNewX()][result.getNewy()].getPiece();
                    chessApp.pieceGroup.getChildren().lastIndexOf(otherPiece2);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            chessApp.pieceGroup.getChildren().remove(otherPiece2);
                        }
                    });

                    oppPiece.move(result.getNewX(), result.getNewy());
                    chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(oppPiece);
                    chessApp.getBoard()[result.getOldX()][result.getOldy()].setPiece(null);

                    if (result.getNewy() == 7 && oppPiece.getColour() == PieceColour.BLACK && oppPiece.getType() == PieceType.PAWN) {
                        chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(null);
                        Piece promtePiece = chessApp.makePiece(PieceColour.BLACK, PieceType.QUEEN, result.getNewX(), result.getNewy());
                        chessApp.pieceGroup.getChildren().remove(oppPiece);
                        chessApp.pieceGroup.getChildren().add(promtePiece);
                    }
                    bl.setTilesThreat(chessApp.board);
                    break;
                case NORMAL:

                    oppPiece.setMoved(true);
                    oppPiece.move(result.getNewX(), result.getNewy());
                    chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(oppPiece);
                    chessApp.getBoard()[result.getOldX()][result.getOldy()].setPiece(null);

                    if (result.getNewy() == 7 && oppPiece.getColour() == PieceColour.BLACK && oppPiece.getType() == PieceType.PAWN) {
                        chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(null);
                        Piece promtePiece = chessApp.makePiece(PieceColour.BLACK, PieceType.QUEEN, result.getNewX(), result.getNewy());
                        chessApp.pieceGroup.getChildren().remove(oppPiece);
                        chessApp.pieceGroup.getChildren().add(promtePiece);
                    }
                    bl.setTilesThreat(chessApp.board);
                    break;
                case NONE:
                    chessApp.setCheckMate(1);
                    break;
                default:
                    break;
            }

        }
        chessApp.setTurn(true);

    }

    /**
     * Called to find all the valid moves that the AI player can make
     *
     * @param workingBrd Receives the board that the computer is evaluating for
     * all valid moves
     * @param aheadTurn Receives whose turn it is so that the computer can
     * predict all the possible moves
     * @return Returns an array list containing all the valid moves as well as
     * their scores for the computer to determine which is the best for it to
     * make
     */
    public ArrayList<RecursiveAIResult> thinkMove(Tile[][] workingBrd, boolean aheadTurn) {
        boolean thinkAheadturn = aheadTurn;
        int compMoveOldX = 0;
        int compMoveOldY = 0;
        int compMoveNewX = 0;
        int compMoveNewY = 0;

        Tile[][] bestBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(workingBrd);

        Piece piece = null;
        //Initialises a new array liost of Recursive AI results 
        ArrayList<RecursiveAIResult> moves2 = new ArrayList<>();
        //Loops through all the tiles to find pieces on them
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (workingBrd[x][y].hasPiece()) {
                    piece = workingBrd[x][y].getPiece();

                } else {
                    piece = null;
                }

                // Loops through all the tiles to see if the selected piece can move there
                //If the piece can move there the AI moves it on a temporoary board and evauluates the score 
                //Then it is stored as a RecursiveAIResult (with the correct type of move, locations etc) in the array list
                for (int newy = 0; newy < 8; newy++) {

                    for (int newx = 0; newx < 8; newx++) {
                        int x0 = x;
                        int y0 = y;
                        Tile[][] tempBrd = new Tile[8][8];

                        if (piece != null && piece.getColour() == PieceColour.BLACK && thinkAheadturn == false) {
                            if (piece != null) {
                                tempBrd = arrayCopy(workingBrd, tempBrd);
                                tempBrd[x][y].setPiece(null);
                                tempBrd[newx][newy].setPiece(null);
                                tempBrd[newx][newy].setPiece(piece);

                            }
                            switch (piece.getType()) {

                                case PAWN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }

                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case ROOK:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case KNIGHT:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));

                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {

                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case BISHOP:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case QUEEN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case KING:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }

                                    break;
                                default:
                                    break;
                            }
                        }
                        if (piece != null && piece.getColour() == PieceColour.WHITE && thinkAheadturn == true) {
                            if (piece != null) {
                                tempBrd = arrayCopy(workingBrd, tempBrd);
                                tempBrd[x][y].setPiece(null);
                                tempBrd[newx][newy].setPiece(null);
                                tempBrd[newx][newy].setPiece(piece);

                            }
                            switch (piece.getType()) {
                                case PAWN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case ROOK:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == false) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case KNIGHT:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));

                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case BISHOP:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case QUEEN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case KING:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }

                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }
            }
        }
        return moves2;
    }

    /**
     * Using pieces left on the board, total dominance is calculated according
     * to the value of the pieces remaining
     *
     * @param brd Receives a string representation of a board
     * @return Returns the integer value of the calculated score for the board
     * that was received as a parameter
     */
    private int calculateScore(String brd) {
        int calculatedScore = 0;
        for (int y = 0; y < brd.length(); y++) {
            switch (brd.charAt(y)) {
                case 'P':
                    calculatedScore += 1;
                    break;
                case 'R':
                    calculatedScore += 5;
                    break;
                case 'N':
                    calculatedScore += 4;
                    break;
                case 'B':
                    calculatedScore += 5;
                    break;
                case 'Q':
                    calculatedScore += 8;
                    break;
                case 'K':
                    calculatedScore += 50;
                    break;
                case 'p':
                    calculatedScore += -1;
                    break;
                case 'r':
                    calculatedScore += -5;
                    break;
                case 'n':
                    calculatedScore += -4;
                    break;
                case 'b':
                    calculatedScore += -5;
                    break;
                case 'q':
                    calculatedScore += -8;
                    break;
                case 'k':
                    calculatedScore += -50;
                    break;
                default:
                    break;
            }
        }
        return calculatedScore;
    }

    /**
     * Prints out a graphical representation of a game board with pieces in
     * their respective places
     *
     * @param board Receives a 2d tile array representation of a board
     * @return Returns a string representation of the 2d tile array parsed in as
     * a parameter
     */
    public String printBoard(Tile[][] board) {
        String brdPrint = "";
        //Loops through all the tiles on the board and adds the specfied character denoting which piece is on the tile if any 
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (null == board[j][i].getPiece()) {
                    brdPrint += ("0");
                } else {
                    switch (board[j][i].getPiece().getColour()) {
                        case BLACK:
                            if (board[j][i].getPiece().getType() == PieceType.PAWN) {
                                brdPrint += ("p");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.ROOK) {
                                brdPrint += ("r");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KNIGHT) {
                                brdPrint += ("n");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.BISHOP) {
                                brdPrint += ("b");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.QUEEN) {
                                brdPrint += ("q");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KING) {
                                brdPrint += ("k");
                            }
                            break;
                        case WHITE:
                            if (board[j][i].getPiece().getType() == PieceType.PAWN) {
                                brdPrint += ("P");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.ROOK) {
                                brdPrint += ("R");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KNIGHT) {
                                brdPrint += ("N");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.BISHOP) {
                                brdPrint += ("B");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.QUEEN) {
                                brdPrint += ("Q");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KING) {
                                brdPrint += ("K");
                            }
                            break;
                        default:
                            brdPrint += ("0");
                            break;
                    }
                }
            }
            brdPrint += ("");
        }
        brdPrint += ("");
        return brdPrint;
    }

    /**
     * Duplicates a 2d tile array in order to manipulate the duplicate without
     * altering the original
     *
     * @param aSource Receives the original array
     * @param aDestination Receives the empty array that will store the values
     * of the source array
     * @return Returns a 2d Tile array identical to the input
     */
    public static Tile[][] arrayCopy(Tile[][] aSource, Tile[][] aDestination) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (aSource[j][i].hasPiece()) {
                    aDestination[j][i] = new Tile(true, j, i);
                    aDestination[j][i].setPiece(aSource[j][i].getPiece());

                } else {
                    aDestination[j][i] = new Tile(true, j, i);
                    aDestination[j][i].setPiece(null);
                }

            }
        }
        return aDestination;
    }

    /**
     *
     * @return Returns the best move for black as a RecursiveAIResult
     */
    public RecursiveAIResult getResult() {
        return result;
    }

    /**
     * Begins running Object this as a thread
     */
    @Override
    public void run() {
        makeMove();
        try {
            if (screen != null) {
                screen.checkCheckMate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(RecursiveAI.class.getName()).log(Level.SEVERE, null, ex);
        }
        chessApp.allowMoves();
    }

}
