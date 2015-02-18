package com.example.haiping.myapplication;


import java.util.*;

public class AI {
    Disc player;
    int al = -9999;
    int be = 9999;
    // board values which are used depending on turn within the game
    int[][] strategyone = {
            {100,-30,20,15,15,20,-30,100},
            {-30,-100,2,1,1,2,-100,-30},
            {20,5,3,2,2,3,5,20},
            {15,1,2,1,1,2,1,15},
            {15,1,2,1,1,2,1,15},
            {20,5,3,2,2,3,5,20},
            {-30,-100,2,1,1,2,-100,-30},
            {100,-30,20,15,15,20,-30,100}};
    int[][] strategytwo = {
            {50,-25,15,10,10,15,-25,50},
            {-25,-50,2,1,1,2,-50,-25},
            {15,2,5,2,2,5,2,15},
            {10,1,2,1,1,2,1,10},
            {10,1,2,1,1,2,1,10},
            {15,2,5,2,2,5,2,15},
            {-25,-50,2,1,1,2,-50,-25},
            {50,-25,15,10,10,15,-25,50}};

    public AI(){}

    // this is the interface which the parser uses to call a move for the ai
    // it takes the level of difficulty, current board state, and the ai disc color
    // as input and returns a valid location for the AI to move with
    Location interfaceAI(String lvl, Board cur, Disc color) {
        Location move = new Location();
        cur.clear_board();
        //cur.search();
        Board ai_board = copyBoard(cur);
        player = color;
        ai_board.search();

        if(lvl.equals("EASY")){
            int maxdepth = 1;
            move = getBestMove(maxdepth, ai_board, color);
        }
        else if(lvl.equals("MEDIUM")){
            int maxdepth = 2;
            move = getBestMove(maxdepth, ai_board, color);
        }
        else if(lvl.equals("HARD")){
            int maxdepth = 3;
            move = getBestMove(maxdepth, ai_board, color);
        }
        else{
            System.out.println("Error: Incorrect level indicator.\n");
            Location temp = new Location(-1,-1);
            return temp;
        }
        return move;
    }


    // This function returns a board so that we are not using a referenced copy of the original board
    // Since replace_board in the Board class did not return a type this function handles that
    // ** NOTE: should check that the board passed is not a reference to the original.
    Board updateBoard(Board present, Location next){
        present.replace_board(next);
        return present;
    }

    // copies the elements in the current board so that the undo function can store those elements
    // uses if statements to check what type to write to undo[index]
    Board copyBoard(Board board) {
        // create a new board so that a reference is not passed to the original object
        // instantiate the objects within the board object
        Board temp = new Board();
        Integer black = board.blacks;
        temp.blacks= black.intValue();
        Integer white= board.whites;
        temp.whites= white.intValue();

        // initialize the valid vector for the new board temp
        for(int i = 0; i <board.valid.size(); ++i){
            Location l = new Location();
            l.i = board.valid.get(i).i;// row.intValue();
            l.j = board.valid.get(i).j;//column.intValue();
            temp.valid.addElement(l);
        }
        if(board.pl_clr == Disc.Black){
            temp.pl_clr = Disc.Black;
        }
        else if(board.pl_clr == Disc.White){
            temp.pl_clr = Disc.White;
        }
        else {
            System.out.printf("Error: Invalid player color\n");
        }
        // copy the values of the original board using if statements to determine what information
        // gets saved (ie. deep copy instead of shallow)
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board.board[i][j] == Disc.Black){
                    temp.board[i][j] = Disc.Black;
                }
                else if(board.board[i][j] == Disc.White){
                    temp.board[i][j] = Disc.White;
                }
                else if(board.board[i][j] == Disc.Empty){
                    temp.board[i][j] = Disc.Empty;
                }
                else if(board.board[i][j] == Disc.Valid){
                    temp.board[i][j] = Disc.Empty;
                }
                else {
                    System.out.printf("Error: Invalid elements stored in current game board.\n");
                }
            }
        }
        return temp;
    }

    // takes in the depth for each level, the current board state and the maximizing player color
    // does not return anything but at the end assigns the best current valid move for the turn
    // to move which is used later for the actual ai to move
    Location getBestMove(int depthmax, Board cur, Disc color){
        Location maxloc = new Location(-1,-1);
        int curmoveval = 0;
        int maxmovevalue = -9999;

        cur.search();
        for(int i = 0; i < cur.valid.size(); i++){
            // create a new board so that the original is not overwritten and updated until the
            // best move is found with the maximum value returned from the minimax tree function
            Board temp = new Board();
            temp = copyBoard(cur);
            Location nextvalid = temp.valid.get(i);
            Gaming_engine.move(nextvalid.i,nextvalid.j,temp);
            temp.search(); // search after move takes place for next available locations

            // search for the value of the current location
            curmoveval = miniMax(depthmax, temp, color, al, be, 0);
            if(curmoveval > maxmovevalue){
                maxloc = cur.valid.get(i);
                maxmovevalue = curmoveval;
            }
        }
        //System.out.printf("This is AI row and column: %d , %d", maxloc.i, maxloc.j);
        return maxloc;
    }

    // the minmax tree function takes in a max depth, a board representing the current turn,
    // the player color, alpha and beta values (for determining best move), and the value of
    // the current board (which is initially 0)
    // function returns an integer displaying the value of a possible move to be done for the
    // ai's current turn
    int miniMax(int depthmax,Board cur, Disc color,int alpha,int beta, int value) {
        // initialize the score for the initial termination conditions
        cur.check_score();
        int position_value = 0;
        if(depthmax == 0 || cur.valid.size() == 0){ //leaf node;
            cur.check_score();
            if(color == Disc.White){
                return  (cur.whites+value);
            }
            else if(color == Disc.Black ){
                return  (cur.blacks+value);
            }
        }
        // if the color disc is that of the maximizing player copy the board and iterate through
        // the next possible moves and get the value of those moves
        if(color == player){
            Iterator<Location> it = cur.valid.iterator();
            for (; it.hasNext();){
                Location tmp = it.next();
                Board m = copyBoard(cur);
                Gaming_engine.move(tmp.i,tmp.j,m);
                m.search();
                // strategy one is an array which stores the value of the board positions for evaluation
                // for the beginning to late middle of the game turns (indicated by the first if statement)
                if((cur.blacks+cur.whites) < 40){
                    position_value = strategyone[tmp.i][tmp.j];
                }
                else{
                    position_value = strategytwo[tmp.i][tmp.j];
                }
                // recursive call to get the next value if it exists
                int compare = miniMax(depthmax-1, m, m.pl_clr, alpha, beta, (value+position_value));
                alpha = max(alpha, compare);
                if(beta <= alpha) break; //alpha-beta pruning
            }
            return alpha; // return the value of alpha as it represents the best possible move for the child of its parent node
        }
        else{	// find the best move for the opponent of the maximizing player; uses similar logic to that above
            Iterator<Location> it = cur.valid.iterator();
            for (; it.hasNext();){
                Location tmp = it.next();
                Board m = copyBoard(cur);
                Gaming_engine.move(tmp.i,tmp.j,m);
                m.search();
                if((cur.blacks+cur.whites) < 40){
                    position_value = strategyone[tmp.i][tmp.j];
                }
                else{
                    position_value = strategytwo[tmp.i][tmp.j];
                }
                int compare = miniMax(depthmax-1, m, m.pl_clr, alpha, beta, (value+position_value));
                beta=min(beta, compare);
                if(beta <= alpha) break; //beta pruning
            }
            return beta;	// returns the best value for the opponent of the maximizing player to ensure best board position is
            // maintained by the maximizing ai
        }
    }

    // these two functions find either the max or min of 2 integers
    int max(int a, int b){
        if(a >= b){
            return a;
        }
        else{
            return b;
        }
    }
    int min(int a, int b){
        if(a <= b){
            return a;
        }
        else{
            return b;
        }
    }
}

