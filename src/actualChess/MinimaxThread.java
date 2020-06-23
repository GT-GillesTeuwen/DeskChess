/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import static actualChess.RecursiveAI.arrayCopy;
import java.util.ArrayList;

/**
 *
 * @author gteuw
 */
public class MinimaxThread extends Thread {

    private int i;
    
    private int mainDepth = 3;

    private Tile[][] position;
    
    private RecursiveAI mainBrain;
   
    private int[] moveScores;
    
    private int depth;

    public MinimaxThread(Tile[][] position, int i,RecursiveAI main,int[] moveScores, int depth) {
        this.position = position;
        this.i =i;
        this.mainBrain=main;
        this.moveScores=moveScores;
        this.depth=depth;
    }

    @Override
    public void run() {
        moveScores[i]=minimax(position, depth, -99999, 99999, true);
    
      
    }

    public int minimax(Tile[][] position2, int depth, int alpha, int beta, boolean turn) {
        Tile[][] position = new Tile[8][8];
        position=arrayCopy(position2, position);
        if (depth == 0) {
           // System.out.println("ID: "+this.getId()+" Score: "+calculateScore(printBoard(position)));
            return calculateScore(printBoard(position));
        }
        ArrayList<RecursiveAIResult> moves = thinkMove(position, turn);
        if (turn) {
            int maxEval = -10000;
            for (int i = 0; i < moves.size(); i++) {

                int eval = minimax(moves.get(i).getRetBrd(), depth - 1, alpha, beta, true);
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                if (beta <= alpha) {

                    break;
                }
            }
            //System.out.println(this.getId()+"Working");
            return maxEval;
        } else {
            int minEval = 100000;
            for (int i = 0; i < moves.size(); i++) {
                int eval = minimax(moves.get(i).getRetBrd(), depth - 1, alpha, beta, false);
                
                minEval = min(minEval, eval);
                beta = min(beta, eval);

                if (beta <= alpha) {

                    break;
                }
            }
            //System.out.println(this.getId()+"Working");
            return minEval;
        }
    }

    /**
     * Finds the smaller of the two given inputs
     *
     * @param num1
     * @param num2
     * @return the smaller of the two given inputs
     */
    private int min(int num1, int num2) {
        if (num1 < num2) {
            return num1;
        }
        return num2;
    }

    /**
     * Finds the larger of the two given inputs
     *
     * @param num1
     * @param num2
     * @return the larger of the two given inputs
     */
    private int max(int num1, int num2) {
        if (num1 > num2) {
            return num1;
        }
        return num2;
    }

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
    
     public ArrayList<RecursiveAIResult> thinkMove(Tile[][] workingBrd, boolean aheadTurn) {
        boolean thinkAheadturn = aheadTurn;
        int compMoveOldX = 0;
        int compMoveOldY = 0;
        int compMoveNewX = 0;
        int compMoveNewY = 0;
        Tile[][] check = new Tile[8][8];
        check = arrayCopy(workingBrd, check);
        Tile[][] bestBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(check);

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

}
