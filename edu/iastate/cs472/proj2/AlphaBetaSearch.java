package edu.iastate.cs472.proj2;

/**
 * 
 * @author Hoi_Phung
 *
 */


/**
 * This class implements the Alpha-Beta pruning algorithm to find the best 
 * move at current state.
*/
public class AlphaBetaSearch extends AdversarialSearch {

    /**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     * 
     * Update 03/18: each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        System.out.println(board);
        System.out.println();

        // TODO


        CheckersMove result = legalMoves[0];

        double resultValue = Double.NEGATIVE_INFINITY;

        for (CheckersMove action : legalMoves) {
            CheckersData data = clone(board);
            data.makeMove(action);
            double value = minValue(data, BLACK,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
        
        // Return the move for the current state.
        // Here, we simply return the first legal move for demonstration.
        //return legalMoves[0];
    }
    
    // TODO
    // Implement your helper methods here.
    public  CheckersData clone(CheckersData data){
        CheckersData result = new CheckersData();
        for(int i =0; i < 8; i ++){
            for(int j = 0; j < 8; j++){
                result.board[i][j] = data.board[i][j];
            }
        }

        return result;
    }
    public double minValue(CheckersData data, int player, double alpha, double beta) {

        if (getUtility(data) == 1 || getUtility(data) == -1 || getUtility(data) == 0)
            return evaluation(data.board);
        double value = Double.POSITIVE_INFINITY;
        CheckersMove[] moves = data.getLegalMoves(player);
        for (CheckersMove action : moves) {
            CheckersData result = clone(data);
            result.makeMove(action);
            value = Math.min(value, maxValue( //
                    result, BLACK, alpha, beta));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }

    public double maxValue(CheckersData data, int player, double alpha, double beta) {

        if (getUtility(data) == 1 || getUtility(data) == -1 || getUtility(data) == 0)
            return evaluation(data.board);
        double value = Double.NEGATIVE_INFINITY;
        CheckersMove[] moves = data.getLegalMoves(player);
        for (CheckersMove action : moves) {
            CheckersData result = clone(data);
            result.makeMove(action);
            value = Math.max(value, minValue( //
                    result, RED, alpha, beta));
            if (value >= beta)
                return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }


    public int  getUtility(CheckersData data){

            if(data== null ||data.getLegalMoves(BLACK) == null || data.getLegalMoves(RED)== null){
                return 0;
            }
           if (data.getLegalMoves(BLACK).length > 0 && data.getLegalMoves(RED).length == 0) {
               return 1;
           } else if (data.getLegalMoves(BLACK).length == 0 && data.getLegalMoves(RED).length > 0) {
               return -1;
           }

        return 0;
    }




    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;

    public boolean isTerminal(CheckersData data){

        if(data== null ||data.getLegalMoves(BLACK) == null || data.getLegalMoves(RED)== null){
            return true;
        }

        return false;
    }
  public int evaluation(int[][] board){
        int numBlack=0;
        int numRed = 0;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j ++){
                if(board[i][j] == RED){
                    numRed ++;
                }

                else if(board[i][j] == BLACK ){
                    numBlack ++;
                }
                else if(board[i][j] == RED_KING ){
                    numRed +=2;
                }

                else if(board[i][j] == BLACK_KING ){
                    numBlack +=2;
                }

            }
        }

       return numBlack -numRed;
   }



}
