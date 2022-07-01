package edu.iastate.cs472.proj2;

/**
 * 
 * @author 
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {

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

    private CSTree<CheckersData> tree = new CSTree<CheckersData>();
    public CheckersMove makeMove(CheckersMove[] legalMoves) {

        System.out.println(board);
        System.out.println();
        // tree <-- NODE(state)
        CSNode root = new CSNode(board);
        tree.root = root;
        root.setValue(0);

        for(CheckersMove x: legalMoves ){
            CheckersData data = clone(board);
            data.makeMove(x);
            CSNode node = new CSNode(data);
            node.setParent(root);
            node.setValue(0);
            if(root.getChild() == null){
                root.setChild(node);
            }
            else{
                root.getChild().setSibling(node);
            }
        }


        int player = BLACK;
        int iteration = 0;
        while (iteration < 100) {

            // leaf <-- SELECT(tree)
            CSNode<CheckersData> leaf = select(tree);
            // child <-- EXPAND(leaf)
            CSNode<CheckersData>child = expand(leaf,player);
            if(child == null){
                break;
            }
            // result <-- SIMULATE(child)
            // result = true if player of root node wins
            int result = simulate(child,player);
            // BACKPROPAGATE(result, child)
            backpropagate(child, result);

            // repeat the four steps for set number of iterations\
            iteration++;

        }
        // return the move in ACTIONS(state) whose node has highest number of playouts

        // TODO 
        
        // Return the move for the current state.
        // Here, we simply return the first legal move for demonstration.
        return bestAction(tree.root);
    }

    public CheckersMove bestAction(CSNode<CheckersData> root){
        CSNode<CheckersData> child = root.getChild();
        int i = 0;
        CSNode<CheckersData> result = child;


        while(child.getSibling() != null){
            if(child.getValue() < child.getSibling().getValue()){
                i++;
            }

            child = child.getSibling();
        }


        return root.getData().getLegalMoves(BLACK)[i];
    }

    public  CheckersData clone(CheckersData data){
        CheckersData result = new CheckersData();
        for(int i =0; i < 8; i ++){
            for(int j = 0; j < 8; j++){
                result.board[i][j] = data.board[i][j];
            }
        }

        return result;
    }
    public CSNode select(CSTree<CheckersData> tree) {
        CSNode<CheckersData> node = tree.root;
        while (!node.isLeaf()) {
            node = getChildWithMaxUCB(node);
        }
        return node;
    }

    public void backpropagate(CSNode<CheckersData> node,int value) {
        if(node == null){
            return;
        }
        node.setNumVisited(node.getNumVisited()+1);
      node.setValue(node.getValue()+value);
        if (node.getParent() != null)
            node.getParent().setValue(node.getParent().getValue()+value);
            backpropagate(node.getParent(), value);
        }

    private int simulate(CSNode<CheckersData> node,int player) {
        CheckersMove [] moves =node.getData().getLegalMoves(player);
        ArrayList<CheckersData>list = new ArrayList<CheckersData>();
        if(isTerminal(node.getData())){

            return evaluation(node.getData().board);
        }

        for(CheckersMove x: moves){
            CheckersData data = clone(node.getData());
            data.makeMove(x);
            list.add(data);
        }

        int i = 0;
        if(player == RED){
           i = BLACK;
        }
        else{
             i = RED;
        }
        Random rand = new Random();
      return simulate(new CSNode<CheckersData>(list.get(rand.nextInt(list.size()))), i);
    }

    private CSNode<CheckersData> expand(CSNode<CheckersData> leaf, int player) {
        CheckersMove [] moves =leaf.getData().getLegalMoves(player);
        if (moves == null){
            return null;
        }
        CheckersData child = clone(leaf.getData());
        child.makeMove(moves[0]);
        CSNode result =new CSNode(child);

        result.setParent(leaf);
        leaf.setChild(result);
        for(int i = 1; i <moves.length; i ++){
            CheckersData sibling = clone(leaf.getData());
            sibling.makeMove(moves[i]);
            CSNode r =new CSNode(sibling);
            result.setSibling(r);
            r = result;
        }
        return result;

    }

    public CSNode<CheckersData> getChildWithMaxUCB(CSNode<CheckersData> node) {
        List<CSNode<CheckersData>> best_children = new ArrayList<>();
        double max_uct = Double.NEGATIVE_INFINITY;
        CSNode child = node.getChild();
        while (child != null) {
            if(child.getNumVisited() == 0){
                return child;
            }
            double uct = (child.getValue() + Math.sqrt(2) * Math.sqrt(Math.log(node.getNumVisited())/child.getNumVisited()));
            if (uct > max_uct) {
                max_uct = uct;
                best_children = new ArrayList<>();
                best_children.add(child);
            } else if (uct == max_uct) {
                best_children.add(child);
            }
            child = child.getSibling();
        }
        Random rand = new Random();
        return best_children.get(rand.nextInt(best_children.size()));
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
