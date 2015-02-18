package com.example.haiping.myapplication;


import java.util.*;

public class Board {
    // 8x8 array
    Disc[][] board = new Disc[8][8]; // 2-Dimensional Array max(8x8) of type Disc
    //Vector<Disc> boards = new Vector<Disc>();
    Vector<Location> changed = new Vector<Location>();
    // Default Constructor (Initializes the board)
    //player color
    Disc pl_clr = Disc.Black;
    int blacks = 0;
    int whites = 0;
    // Store the valid locations in a vector
    Vector<Location> valid = new Vector<Location>();
    public Board() {
        initialize();
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //returns disc
    public Disc getDisc(int i, int j) {
        return board[i][j];
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //flips player color
    public void flip(){
        if(pl_clr == Disc.Black) pl_clr=Disc.White;
        else if(pl_clr==Disc.White)pl_clr = Disc.Black;
        else System.out.println("Wrong opeation on flip. \n");
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // Initialize board
    public void initialize() {
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                board[i][j] = Disc.Empty;
            }
        }
        board[3][4] = Disc.Black;
        board[4][3] = Disc.Black;
        board[3][3] = Disc.White;
        board[4][4] = Disc.White;
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // Prints game-board
    public void printBoard() {
        System.out.println("  _ _ _ _ _ _ _ _ ");
        for (int i = 0; i < 8; i++) {
            System.out.print(i+1 + "|");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // Takes coordinates and verifies location is a valid move and returns it, otherwise it prints out 'invalid move', or can't find location;
    public Location find_loc(int i, int j){
        search();
        Iterator<Location> it = valid.iterator();
        for (; it.hasNext();){
            Location tmp = it.next();
            Location returnvalue = new Location();
            returnvalue.i = tmp.i;
            returnvalue.j = tmp.j;
            //System.out.printf("row: %d, column: %d", returnvalue.i, returnvalue.j);
            if((tmp.i==i) && (tmp.j==j)) return tmp;
        }
        //System.out.println("Can't find the location GVTFVFVFG.\n");
        return new Location(-1,-1);
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //inserts locations into a vector of valid moves
    void insert_loc(Location loc){
        Iterator<Location> it = valid.iterator();
        for(;it.hasNext();){
            if(it.next() == loc){
                return;
            }
        }
        valid.add(loc);
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //checks for valid moves on the upper left corner of an empty space
    boolean check_ul(Location loc){
        Location current = loc.getul();
        if(current.i < 0 || current.j < 0){
            changed.clear();
            return false;
        }
        else if(board[current.i][current.j] != pl_clr && board[current.i][current.j] != Disc.Empty){
            changed.add(loc);
            return check_ul(current);
        }
        else if(board[current.i][current.j] == pl_clr && board[current.getlr().i][current.getlr().j] != pl_clr && board[current.getlr().i][current.getlr().j] != Disc.Empty){
            changed.add(loc);
            return true;
        }
        else{
            changed.clear();
            return false;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //checks for valid moves on the lower left corner of an empty space
    boolean check_ll(Location loc){
        Location current = loc.getll();
        if(current.i > 7 || current.j <0){
            changed.clear();
            return false;
        }
        else if(board[current.i][current.j] != pl_clr && board[current.i][current.j] != Disc.Empty ){
            changed.add(loc);
            return check_ll(current);
        }
        else if(board[current.i][current.j] == pl_clr && board[current.getur().i][current.getur().j] != pl_clr && board[current.getur().i][current.getur().j] != Disc.Empty){
            changed.add(loc);
            return true;
        }
        else{
            changed.clear();
            return false;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //checks for valid moves on the upper right corner of an empty space
    boolean check_ur(Location loc){
        changed.add(loc);
        Location current = loc.getur();
        if(current.i < 0 || current.j >7){
            changed.clear();
            return false;
        }
        else if(board[current.i][current.j] != pl_clr && board[current.i][current.j] != Disc.Empty ){
            changed.add(loc);
            return check_ur(current);
        }
        else if(board[current.i][current.j] == pl_clr && board[current.getll().i][current.getll().j] != pl_clr && board[current.getll().i][current.getll().j] != Disc.Empty){
            changed.add(loc);
            return true;
        }
        else{
            changed.clear();
            return false;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //checks for valid moves on the lower right corner of an empty space
    boolean check_lr(Location loc){
        Location current = loc.getlr();
        if(current.i > 7 || current.j > 7){
            changed.clear();
            return false;
        }
        else if(board[current.i][current.j] != pl_clr && board[current.i][current.j] != Disc.Empty ){
            changed.add(loc);
            return check_lr(current);
        }
        else if(board[current.i][current.j] == pl_clr && board[current.getul().i][current.getul().j] != pl_clr && board[current.getul().i][current.getul().j] != Disc.Empty){
            changed.add(loc);
            return true;
        }
        else{
            changed.clear();
            return false;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //checks for valid moves
    public void search(){
        valid.clear();
        // horizontal
        for(int i=0; i<8;i++){
            for(int j=0; j< 8; j++){
                if(board[i][j]==Disc.Empty){
                    if(((j+2)<8) && board[i][j+1]!=pl_clr && board[i][j+1]!=Disc.Empty){
                        int tm=j+2;
                        while(tm<8){
                            if(board[i][tm]==pl_clr ||(board[i][tm]==pl_clr && tm==j+2) ) {insert_loc(new Location(i,j)); break;}
                            if(board[i][tm]==Disc.Empty){break;}
                            tm++;
                        }
                    }
                    if( ((j-2)>=0) && board[i][j-1]!=pl_clr  && board[i][j-1]!=Disc.Empty){
                        int tm=j-2;
                        while(tm>=0){
                            if(board[i][tm]==pl_clr) {insert_loc(new Location(i,j)); break;}
                            if(board[i][tm]==Disc.Empty){break;}
                            tm--;
                        }
                    }
                }
            }
        }

        //vertical
        for(int j=0; j<8;j++){
            for(int i=0; i< 8; i++){
                if(board[i][j]==Disc.Empty){
                    if(((i+2)<8) && board[i+1][j]!=pl_clr && board[i+1][j]!=Disc.Empty){
                        int tm=i+2;
                        while(tm<8){
                            if(board[tm][j]==pl_clr) {insert_loc(new Location(i,j)); break;}
                            if(board[tm][j]==Disc.Empty){break;}
                            tm++;
                        }
                    }
                    if( ((i-2)>=0) && board[i-1][j]!=pl_clr  && board[i-1][j]!=Disc.Empty){
                        int tm=i-2;
                        while(tm>=0){
                            if(board[tm][j]==pl_clr) {insert_loc(new Location(i,j)); break;}
                            if(board[tm][j]==Disc.Empty){break;}
                            tm--;
                        }
                    }
                }
            }
        }

        //checks for four corners for valid moves
        for(int row= 0; row < 8; ++row){
            for(int column = 0; column <8; ++column){
                if(board[row][column] == Disc.Empty){
                    Location temp = new Location(row,column);
                    if(check_lr(temp))
                        insert_loc(temp);
                    if(check_ll(temp))
                        insert_loc(temp);
                    if(check_ur(temp))
                        insert_loc(temp);
                    if(check_ul(temp))
                        insert_loc(temp);
                }
            }
        }

    }

    //------------------------------------------------------------------------------------------------------------------------------
    void output_valids(){
        Iterator<Location> valids = valid.iterator();
        for(;valids.hasNext();){
            Location current = valids.next();
            board[current.i][current.j] = Disc.Valid;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    void clear_board(){
        Iterator<Location> va = valid.iterator();
        for(;va.hasNext();){
            Location a = va.next();
            board[a.i][a.j] = Disc.Empty;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //replaces board token to reflect a new move
    void check_score(){
        whites = 0;
        blacks = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] == Disc.Black)
                    blacks++;
                if(board[i][j] == Disc.White)
                    whites++;
            }
        }
        //System.out.printf("White: %d, Black: %d \n", whites, blacks);
    }

    void print_score(){
        System.out.printf("White: %d, Black: %d \n", whites, blacks);
    }
    //------------------------------------------------------------------------------------------------------------------------------
    boolean exit_fun(){
        int total = blacks+whites;
        if(total == 64){
            if(blacks>whites){
                System.out.println("Black Won!");
                System.out.println("White lost.");
            }
            else if(whites>blacks){
                System.out.println("White Won!");
                System.out.println("black lost.");
            }
            else if(whites == blacks){
                System.out.println("Tie!");
            }
            return true;
        }
        else if(blacks == 0 || whites == 0){
            if(blacks>whites){
                System.out.println("Black Won!");
                System.out.println("White lost.");
            }
            else if(whites>blacks){
                System.out.println("White Won!");
                System.out.println("black lost.");
            }
            return true;
        }
        else
            return false;
    }
    //------------------------------------------------------------------------------------------------------------------------------
    Vector<Location> replace_board(Location loc){
        //check for horizontal changes to replace
        Vector<Location> tbchanged = new Vector<Location>();
        int row = loc.i;
        int column = loc.j;
        int flag1 = 0;
        int flag2 = 0;

        if((loc.j+2)< 8 && board[loc.i][loc.j+1] != pl_clr && board[loc.i][loc.j+1]!=Disc.Empty && flag1==0){
            int tm = loc.j+2;
            while(tm<8){
                if(board[loc.i][tm] == pl_clr) {
                    for(int k = loc.j; k < tm; ++k){
                        Location temp = new Location(loc.i, k);
                        tbchanged.add(temp);
                    }
                    flag1 = 1;
                    break;
                }
                if(board[loc.i][tm] == Disc.Empty){break;}
                tm++;
            }
        }

        if((loc.j-2)>=0 && board[loc.i][loc.j-1] != pl_clr && board[loc.i][loc.j-1]!=Disc.Empty && flag2 == 0){
            int tm = loc.j-2;
            while(tm>=0){
                if(board[loc.i][tm] == pl_clr){
                    for(int k = loc.j ; k >tm ; --k){
                        Location temp = new Location(loc.i, k);
                        tbchanged.add(temp);
                    }
                    flag2 = 1;
                    break;
                }
                if(board[loc.i][tm] == Disc.Empty){break;}
                tm--;
            }
        }

        //checks for vertical places to replace
        flag1 = 0;
        flag2 = 0;

        if((loc.i+2)< 8 && board[loc.i+1][loc.j] != pl_clr && board[loc.i+1][loc.j]!=Disc.Empty && flag1 == 0){
            int tm = loc.i+2;
            while(tm<8){
                if(board[tm][loc.j] == pl_clr) {
                    for(int k = loc.i; k < tm; ++k){
                        Location temp = new Location(k, loc.j);
                        tbchanged.add(temp);
                    }
                    flag1 = 1;
                    break;
                }
                if(board[tm][loc.j] == Disc.Empty){break;}
                tm++;
            }
        }


        if((loc.i-2)>=0 && board[loc.i-1][loc.j] != pl_clr  && board[loc.i-1][loc.j]!=Disc.Empty && flag2 == 0){
            int tm = loc.i-2;
            while(tm>=0){
                if(board[tm][loc.j] == pl_clr){
                    for(int k = loc.i ; k > tm ; --k){
                        Location temp = new Location(k, loc.j);
                        tbchanged.add(temp);
                    }
                    flag2 = 1;
                    break;
                }
                if(board[tm][loc.j] == Disc.Empty){break;}
                tm--;
            }
        }

        //check for both diagonals for changes to replace
        Location temp = new Location(row,column);
        if(check_lr(temp)){
            Iterator<Location> it = changed.iterator();
            for (; it.hasNext();){
                Location tmp=it.next();
                tbchanged.add(tmp);
            }
            changed.clear();
        }
        if(check_ll(temp)){
            Iterator<Location> it = changed.iterator();
            for (; it.hasNext();){
                Location tmp=it.next();
                tbchanged.add(tmp);
            }
            changed.clear();
        }
        if(check_ur(temp)){
            Iterator<Location> it = changed.iterator();
            for (; it.hasNext();){
                Location tmp=it.next();
                tbchanged.add(tmp);
            }
            changed.clear();
        }
        if(check_ul(temp)){
            Iterator<Location> it = changed.iterator();
            for (; it.hasNext();){
                Location tmp=it.next();
                tbchanged.add(tmp);
            }
            changed.clear();
        }

        //changes replaced
        Iterator<Location> iter = tbchanged.iterator();
        for(;iter.hasNext();){
            Location current = iter.next();
            board[current.i][current.j] = pl_clr;

        }
        return tbchanged;
    }
}
