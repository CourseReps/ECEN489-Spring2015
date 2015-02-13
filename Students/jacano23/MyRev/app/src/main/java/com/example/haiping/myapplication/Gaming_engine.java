package com.example.haiping.myapplication;


//import java.util.Scanner;
import java.util.*;


public class Gaming_engine {
    //Board undo []= new Board[10];
    Vector<Board> undo = new Vector<Board>(10);
    Board current=new Board();
    Gaming_engine(){
        //	for(int i=0; i<10; i++) undo[i]=new Board();

    }

    //takes in coordinates and plays move
    public boolean move(int i, int j){
        Location tmp = current.find_loc(i,j);
        if(tmp.i==-1 || tmp.j==-1) {
            System.out.println("Invalid Position!\n");
            current.clear_board();
            return false;
        }
        else {
            if(current.pl_clr == Disc.Black){
                addNewUndoBoard(current);
            }
            current.clear_board();
            current.replace_board(tmp);
            current.flip();
            return true;
        }

    }
    public static boolean move(int i, int j, Board cur){
        Location tmp=cur.find_loc(i,j);
        if(tmp.i==-1 || tmp.j==-1) {
            System.out.println("Invalid Position! \n");
            cur.clear_board();
            return false;
        }
        else {
            cur.clear_board();
            cur.replace_board(tmp);
            cur.flip();
            return true;
        }

    }


    public boolean move_server(int i, int j){
        Location tmp=current.find_loc(i,j);
        if(tmp.i==-1 && tmp.j==-1) {
            //System.out.println("Invalid Position! DSFSDFS\n");
            current.clear_board();
            return false;
        }
        else {
            current.clear_board();
            current.replace_board(tmp);
            current.flip();
            return true;
        }

    }

    // undo function. returns true if undo worked and false if there is an error
    boolean undoMove(Board undoboard) {
        if(undo.size() <= 0){
            System.out.printf("Error: No previous gameplay states available.\n");
            return false;
        }
        int checksize = undo.size();
        int lastelement = undo.size()-1;
        current = undo.get(lastelement); // get the second to last element and make it the new current board state
        undo.removeElementAt(lastelement);
        //undo.remove(lastelement);	// remove the last element which is resized by the remove() function for ArrayList
        if(undo.size() < checksize) {
            return true;
        }
        else
            return false;
    }
    void addNewUndoBoard(Board newboard){
        // check if size of vector has 10 elements or not yet. so < 10 because 0 -> 9
        //int curindex = undo.size(); // gets number of current elements and adds to the next element in the ArrayList
        if(undo.size() == 10) {
            // removing the element shifts all other elements to the left
            undo.remove(0);
        }
        // add element by creating an exact replica of previous board. need new board because of referencing (Java variables passed
        // to functions act more as pointers than stand alone objects like in c)
        Board temp = copyBoard(newboard);
        undo.add(temp);
    }

    // copies the elements in the current board so that the undo function can store those elements
    // uses if statements to check what type to write to undo[index]
    Board copyBoard(Board board) {
        Board temp = new Board();
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

    // function passes in a vector of all possible moves for the ai. it then picks a random integer
    // and takes finds that location and passes it back to the function call made where it can then be
    // used to make the move for the ai
    Location randomAIMove(Vector<Location> validmoves) {
        Random rand = new Random();
        if(validmoves.size() == 1){
            Location tmp= validmoves.elementAt(0);
            return tmp;
        }
        int randlocation = rand.nextInt(validmoves.size()-1);
        Location tmp= validmoves.elementAt(randlocation);
        return tmp;
    }
}


