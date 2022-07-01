package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.


    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
      //fill black
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 8; j ++){
               if( j % 2 == i % 2){
                   board[i][j] = BLACK;
               }
               else{
                   board[i][j] = EMPTY;
               }
            }
        }

        for(int i = 3; i <5; i ++){
            for(int j = 0; j < 8; j ++){
                board[i][j] = EMPTY;
            }
        }


         // fill red
        for(int i = 5; i < 8; i ++){
            for(int j = 0; j < 8; j ++){
                if( j % 2 == i % 2){
                    board[i][j] = RED;
                }
                else{
                    board[i][j] = EMPTY;
                }
            }
        }

        // TODO
    	// 
    	// Set up the board with pieces BLACK, RED, and EMPTY
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Update 03/18: make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;
        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            int jumpCol=(fromCol + toCol)/2;
            int jumpRow=(fromRow + toRow)/2;
            board[jumpRow][jumpCol] = EMPTY;
        }
        if (toRow == 0 && board[toRow][toCol] == RED)
            board[toRow][toCol] = RED_KING;
        if (toRow == 7 && board[toRow][toCol] == BLACK)
            board[toRow][toCol] = BLACK_KING;

    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        // TODO
        if (player != RED && player != BLACK)
            return null;

        int king = 0;
        if(player == RED){
            king = RED_KING;
        }
        if(player == BLACK){
            king = BLACK_KING;
        }
        ArrayList moves = new ArrayList();  // Moves will be stored in this vector.
        CheckersMove[] jump= null;
        for(int i = 0; i < 8; i++){
            for(int j= 0; j < 8; j ++){
                if(board [i][j] ==player || board [i][j] == king){
                jump =getLegalJumpsFrom(board [i][j],i,j);
                if(jump!= null) {
                    for (CheckersMove x : jump) {
                        moves.add(x);
                    }
                }
                }
            }
        }
        if(moves.size() ==0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == king) {
                        int[] x = {-1, 1, 1, -1};
                        int[] y = {1, 1, -1, -1};
                        for (int a = 0; a < 4; a++) {
                            int newX = i + x[a];
                            int newY = j + y[a];
                            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                                if (board[newX][newY] == EMPTY) {
                                    moves.add(new CheckersMove(i, j, newX, newY));
                                }
                            }
                        }
                    } else {
                        if (player == RED) {
                            if (board[i][j] == RED) {
                                if (i - 1 >= 0 && j-1 >= 0) {
                                    if (board[i - 1][j - 1] == EMPTY) {
                                        moves.add(new CheckersMove(i, j, i - 1, j - 1));
                                    }
                                }  if (i-1 >= 0 && j+1 < 8) {
                                    if (board[i - 1][j + 1] == EMPTY) {
                                        moves.add(new CheckersMove(i, j, i - 1, j + 1));
                                    }
                                }
                            }
                        } else if (player == BLACK) {
                            if (board[i][j] == BLACK) {
                                if (i + 1 < 8 && j - 1 >= 0) {
                                    if (board[i + 1][j - 1] == EMPTY) {
                                        moves.add(new CheckersMove(i, j, i + 1, j - 1));
                                    }
                                }  if (i + 1 < 8 && j + 1 < 8) {
                                    if (board[i + 1][j + 1] == EMPTY) {
                                        moves.add(new CheckersMove(i, j, i + 1, j + 1));
                                    }
                                }
                            }
                        }
                    }

                }

            }

        }
        if (moves.size() == 0)
            return null;

        CheckersMove[] moveArray = new CheckersMove[moves.size()];
        moves.toArray(moveArray);
        return moveArray;
    }




    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Update 03/18: Note that each CheckerMove may contain multiple jumps.
     * Each move returned in the array represents a sequence of jumps
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        // TODO
        ArrayList<CheckersMove> jumpList= new ArrayList<CheckersMove>();
        if(player == RED_KING){
            int[] x = {-1,1,1,-1};
            int[] y = {1,1,-1,-1};
            for(int a= 0 ; a < 4; a ++){
                int newX = row + x[a];
                int newY = col + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== BLACK_KING|| board[newX][newY] ==BLACK && board[newX +x[a]][newY+y[a]] == EMPTY){
                            ArrayList<CheckersMove> data = new ArrayList<CheckersMove>();
                            expandMove(new CheckersMove(row, col, newX +x[a],newY+y[a]), player, data);
                            for(CheckersMove i: data){
                                jumpList.add(i);
                            }
                        }
                    }
                }

            }

    }
        else if(player== BLACK_KING){
            int[] x = {-1,1,1,-1};
            int[] y = {1,1,-1,-1};
            for(int a= 0 ; a < 4; a ++){
                int newX = row + x[a];
                int newY = col + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== RED_KING|| board[newX][newY] ==RED && board[newX +x[a]][newY+y[a]] == EMPTY){
                            ArrayList<CheckersMove> data = new ArrayList<CheckersMove>();
                            expandMove(new CheckersMove(row, col, newX +x[a],newY+y[a]), player, data);
                            for(CheckersMove i: data){
                                jumpList.add(i);
                            }
                        }

                    }
                }

            }
        }


        if(player == RED){
            int[] x = {-1,-1};
            int[] y = {-1,1};
            for(int a= 0 ; a < 2; a ++){
                int newX = row + x[a];
                int newY = col + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== BLACK_KING|| board[newX][newY] ==BLACK && board[newX +x[a]][newY+y[a]] == EMPTY){
                            ArrayList<CheckersMove> data = new ArrayList<CheckersMove>();
                            expandMove(new CheckersMove(row, col, newX +x[a],newY+y[a]), player, data);
                            for(CheckersMove i: data){
                                jumpList.add(i);
                            }

                        }

                    }
                }

            }
        }
        else if(player == BLACK){
            int[] x = {1,1};
            int[] y = {-1,1};
            for(int a= 0 ; a < 2; a ++){
                int newX = row + x[a];
                int newY = col + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== RED_KING|| board[newX][newY] ==RED && board[newX +x[a]][newY+y[a]] == EMPTY){
                            ArrayList<CheckersMove> data = new ArrayList<CheckersMove>();
                            expandMove(new CheckersMove(row, col, newX +x[a],newY+y[a]), player, data);
                            for(CheckersMove i: data){
                                jumpList.add(i);
                            }
                        }

                    }
                }

            }
        }


        CheckersMove[] list = new CheckersMove[jumpList.size()];
        jumpList.toArray(list);

        return list;
    }

    CheckersMove expandMove(CheckersMove data, int player,  ArrayList<CheckersMove> list){
        ArrayList<CheckersMove> result = new ArrayList<CheckersMove>();

        if(player == RED){
            int[] x = {-1,-1};
            int[] y = {-1,1};
            for(int a= 0 ; a < 2; a ++){
                int newX = data.rows.get(data.rows.size()-1) + x[a];
                int newY = data.cols.get(data.cols.size()-1) + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== BLACK_KING|| board[newX][newY] ==BLACK && board[newX +x[a]][newY+y[a]] == EMPTY){
                            CheckersMove c = data.clone();
                            c.addMove(newX +x[a],newY+y[a]);
                            expandMove(c, player,list);

                        }

                    }
                }

            }
        }
        else if(player == BLACK){
            int[] x = {1,1};
            int[] y = {-1,1};
            for(int a= 0 ; a < 2; a ++){
                int newX = data.rows.get(data.rows.size()-1) + x[a];
                int newY = data.cols.get(data.cols.size()-1) + y[a];
                if(newX >=0 && newX <8 && newY >=0 && newY <8){
                    if(newX +x[a]>=0 && newX+x[a] <8 && newY+y[a] >=0 && newY+y[a] <8){
                        if(board[newX][newY]== RED_KING|| board[newX][newY] ==RED && board[newX +x[a]][newY+y[a]] == EMPTY){
                            CheckersMove c = data.clone();
                            c.addMove(newX +x[a],newY+y[a]);
                            expandMove(c, player,list);

                        }

                    }
                }

            }
        }
        list.add(data);
        return data;
    }





}
