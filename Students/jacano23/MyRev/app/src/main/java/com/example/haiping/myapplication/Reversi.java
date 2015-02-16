package com.example.haiping.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Created by Jorge and Haiping on 7/4/2014.
 */
public class Reversi extends Activity {
    int blacks = 2;
    int whites = 2;
    public Button buttonarray[][] = new Button[8][8];
    ImageButton newgamebutton;
    ImageButton undobutton;
    Board game = new Board();
    String lvl=new String();
    Gaming_engine current_game = new Gaming_engine();
    Disc plr;
    String[] highscores = new String[18];


    //Intializes all items on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        game.search();
        game.pl_clr = Disc.Black;

        setContentView(R.layout.reversi_board);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String tmp = button_name(i, j);
                int resID = getResources().getIdentifier(tmp, "id", getPackageName());
                buttonarray[i][j] = (Button) findViewById(resID);
                our_listener current = new our_listener(i, j);
                buttonarray[i][j].setOnClickListener(current);
            }
        }
        set_start();
        newgamebutton = (ImageButton) findViewById(R.id.newGameButton);
        newgamebutton.setOnClickListener(newgamehandler);
        undobutton = (ImageButton) findViewById(R.id.undobutton);
        undobutton.setOnClickListener(myhandler1);
        //newTextview = (TextView) findViewById(R.id.test);
        lvl=getIntent().getExtras().getString("lvl");
        String com= getIntent().getExtras().getString("plr");
        if(com.equals("white")) plr=Disc.White;
         else plr=Disc.Black;
         game.pl_clr=plr;
        //current_game.addNewUndoBoard(game);
       // newTextview.setText();
    }

    //sets the board on the screen
    public void set_start(){
        Drawable dw = getResources().getDrawable(R.drawable.roundw);
        Drawable db = getResources().getDrawable(R.drawable.round);
        buttonarray[3][3].setBackground(dw);
        buttonarray[3][3].setEnabled(false);
        buttonarray[3][4].setBackground(db);
        buttonarray[3][4].setEnabled(false);
        buttonarray[4][3].setBackground(db);
        buttonarray[4][3].setEnabled(false);
        buttonarray[4][4].setBackground(dw);
        buttonarray[4][4].setEnabled(false);
    }

    //updates the scoreboard
    public void updateTextView(String toThis) {

        TextView textView = (TextView) findViewById(R.id.Scoreboard);
        textView.setText(toThis);
        return;
    }

    //update turn

    public void updateturn(Disc d){
        TextView turnview = (TextView) findViewById(R.id.turn);
        if(d == plr) {

            turnview.setText("Your Turn!");
        }
        else if (d != plr){
            turnview.setText(" ");
        }
        return;
    }

    //custom listener for the game pieces
    public class our_listener implements View.OnClickListener{
        int row;
        int column;
        our_listener(int i, int j){
            row = i;
            column = j;
        }

        @Override
        public void onClick(View v){
            boolean exitflag = false;
            Drawable d1= getResources().getDrawable(R.drawable.roundw);
            Drawable d2 = getResources().getDrawable(R.drawable.round);
            if(!is_valid_move(row, column))return;
            updateturn(Disc.Empty);
            current_game.addNewUndoBoard(game);
            Location temp = new Location(row, column);
            Vector<Location> button_changed = game.replace_board(temp);
            if(game.pl_clr == Disc.White) {
                buttonarray[row][column].setBackground(d1);
            }
            else if(game.pl_clr == Disc.Black){
                buttonarray[row][column].setBackground(d2);
            }
            for(int i = 0; i < button_changed.size(); ++i){
                if(game.pl_clr == Disc.White)
                    buttonarray[button_changed.elementAt(i).i][button_changed.elementAt(i).j].setBackground(d1);
                else if(game.pl_clr == Disc.Black){
                    buttonarray[button_changed.elementAt(i).i][button_changed.elementAt(i).j].setBackground(d2);
                }
            }
            game.flip();
            game.search();
            game.check_score();
                if(end_flag()){
                    Context context = getApplicationContext();
                    CharSequence text = "Game Over!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                    Integer white = game.whites;
                    Integer black = game.blacks;
                    String playerwin = new String();
                    if(white > black){
                        playerwin = "White won!";
                    }
                    else if(black > white){
                        playerwin = "Black won!";
                    }
                    else{

                        playerwin = "Draw!";
                    }
                    updateturn(Disc.Empty);
                    String insert = playerwin +"\n" +"Black     " + black.toString() + " - "+ white.toString()+"     White";
                    updateTextView(insert);
                    exitflag = true;
                    undobutton.setEnabled(false);
                    try {
                       highscores =  StatisticsScreen.read();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    replace_highscore(lvl);
                    write_xml(highscores);
                    //buttonarray[row][column].setEnabled(false);
                }
                else if (game.valid.isEmpty()) {
                    game.flip();
                    game.search();
                }
                else{
                    while(true){
                    //ai turn;
                    updateturn(game.pl_clr);
                    AI tmp = new AI();
                    Location loc=tmp.interfaceAI(lvl, game, game.pl_clr);
                    Vector<Location> locs = game.replace_board(loc);
                        if(game.pl_clr == Disc.White) {
                            buttonarray[loc.i][loc.j].setBackground(d1);
                        }
                        else if(game.pl_clr == Disc.Black){
                            buttonarray[loc.i][loc.j].setBackground(d2);
                        }
                        for(int i = 0; i < locs.size(); ++i){
                            if(game.pl_clr == Disc.White)
                                buttonarray[locs.elementAt(i).i][locs.elementAt(i).j].setBackground(d1);
                            else if(game.pl_clr == Disc.Black){
                                buttonarray[locs.elementAt(i).i][locs.elementAt(i).j].setBackground(d2);
                            }
                        }
                    game.flip();
                    game.search();
                    if(end_flag()){
                        Context context = getApplicationContext();
                        CharSequence text = "Game Over!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                        Integer white = game.whites;
                        Integer black = game.blacks;
                        String playerwin = new String();
                        if(white > black){
                            playerwin = "White won!";
                        }
                        else if(black > white){
                            playerwin = "Black won!";
                        }
                        else{

                            playerwin = "Draw!";
                        }
                        updateturn(Disc.Empty);
                        String insert = playerwin +"\n" +"Black     " + black.toString() + " - "+ white.toString()+"     White";
                        updateTextView(insert);
                        exitflag = true;
                        undobutton.setEnabled(false);
                        try {
                            highscores =  StatisticsScreen.read();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        replace_highscore(lvl);
                        write_xml(highscores);
                        break;
                    }
                    if(!game.valid.isEmpty()){
                        game.check_score();
                        break;
                    }
                    game.flip();
                    game.search();
                }
            }
            //whites++;
            //blacks++;
            Integer white = game.whites;
            Integer black = game.blacks;
            if(exitflag == false) {
                updateturn(game.pl_clr);
                String insert = "Black     " + black.toString() + " - " + white.toString() + "     White";
                updateTextView(insert);
            }
            buttonarray[row][column].setEnabled(false);
        }
    }

    //listener for the undobutton
   View.OnClickListener myhandler1 = new View.OnClickListener() {
       public void onClick(View v) {
            Disc temp = game.pl_clr;
            if (current_game.undoMove(game)){
                game = current_game.current;
                game.search();
                game.check_score();
                game.pl_clr = temp;
                Integer white = game.whites;
                Integer black = game.blacks;
                String insert = "Black     " + black.toString() + " - "+ white.toString()+"     White";
                updateTextView(insert);
                change_board();
            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "No Undo Moves!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            }
       }
    };

     //listener for the newgamebutton
    View.OnClickListener newgamehandler = new View.OnClickListener(){
        public void onClick(View v){
            Intent newgame = new Intent(Reversi.this, DifficultyScreen.class);
            startActivity(newgame);

        }
    };

    //this is used to initialize all the game buttons
    String button_name(int i, int j){
        Integer row = i;
        Integer column = j;
        String btnID = "button"+row.toString() + column.toString();
        return new String(btnID);
    }

    //ending function
    boolean end_flag(){
        Board tmp= copyBoard(game);
        if(tmp.valid.isEmpty()){
            tmp.flip();
            tmp.search();
            return tmp.valid.isEmpty();
        }
        else return false;

    }

    //valid moves
    boolean is_valid_move(int i, int j){
        Location tmp=game.find_loc(i,j);
        if(tmp.i==-1 || tmp.j==-1) {
            return false;
        }
        return true;
    }
    //change board for undo
    void change_board(){
        Drawable d1= getResources().getDrawable(R.drawable.roundw);
        Drawable d2 = getResources().getDrawable(R.drawable.round);
        Drawable d3 = getResources().getDrawable(R.drawable.tranparant);
        for(int i = 0; i < 8; ++i){
            for(int j = 0 ;j < 8;j++){
                if(game.board[i][j] == Disc.Black){
                    buttonarray[i][j].setBackground(d2);
                }
                else if(game.board[i][j] == Disc.White){
                    buttonarray[i][j].setBackground(d1);
                }
                else{
                    buttonarray[i][j].setBackground(d3);
                    buttonarray[i][j].setEnabled(true);
                }
            }
        }
    }

    //copyboard to pass the board by value
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

    public static void write_xml(String[] array){
        String[] storedata = new String[18];
        storedata = array;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);
            int index = 0;
            for(int i = 0;  i < 3; i++){
                //level elements
                Element level = doc.createElement("level");
                rootElement.appendChild(level);

                //set attribute to level element
                Attr attr = doc.createAttribute("difficulty");
                if (i == 0)
                    attr.setValue("easy");
                else if (i == 1)
                    attr.setValue("medium");
                else if(i == 2)
                    attr.setValue("hard");
                level.setAttributeNode(attr);

                //first element
                Element first = doc.createElement("First");
                first.appendChild(doc.createTextNode(storedata[++index]));
                level.appendChild(first);

                //second element
                Element second = doc.createElement("Second");
                second.appendChild(doc.createTextNode(storedata[++index]));
                level.appendChild(second);

                //third element
                Element third  = doc.createElement("Third");
                third.appendChild(doc.createTextNode(storedata[++index]));
                level.appendChild(third);

                //fourth element
                Element fourth = doc.createElement("Fourth");
                fourth.appendChild(doc.createTextNode(storedata[++index]));
                level.appendChild(fourth);

                //fifth element
                Element fifth = doc.createElement("Fifth");
                fifth.appendChild(doc.createTextNode(storedata[++index]));
                level.appendChild(fifth);
                ++index;
            }

            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(Environment.getExternalStorageDirectory().getPath(),"schema.xml"));

            //Output to console for testing
            //StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

        }
        catch  (ParserConfigurationException pce){
            pce.printStackTrace();
        }
        catch   (TransformerException tfe){
            tfe.printStackTrace();
        }
    }

    void replace_highscore(String level){
        //Integer highscore = new Integer(0);
        int highscore = 0;
        Integer insertint;
        String[] temp = new String [5];
        String[] temp2 = new String[18];
        for(int i = 0; i < 18; ++i){
            if(highscores[i].equals("xxxx")){
                temp2[i] = "0";
            }
            else
                temp2[i] = highscores[i];

        }
        highscores = temp2;
        int start = 0;
        if(plr == Disc.Black) {
            highscore = game.blacks;
        }
        else if (plr == Disc.White){
            highscore = game.whites;
        }

        if(level.equals("EASY")){
            start = 1;
        }
        else if (level.equals("MEDIUM")){
            start = 7;
        }

        else if (level.equals("HARD")){
            start = 13;
        }
        int compare = Integer.parseInt(temp2[start]);
        int compare2 = Integer.parseInt(temp2[start+1]);
        int compare3 = Integer.parseInt(temp2[start+2]);
        int compare4 = Integer.parseInt(temp2[start+3]);
        int compare5 = Integer.parseInt(temp2[start+4]);
        if(highscore > compare ){
            insertint = highscore;
            temp[0] = insertint.toString();
            temp[1] = temp2[start];
            temp[2] = temp2[start+1];
            temp[3] = temp2[start+2];
            temp[4] = temp2[start+3];
        }
        else if( highscore > compare2){
            insertint = highscore;
            temp[0] = temp2[start];
            temp[1] = insertint.toString();
            temp[2] = temp2[start+1];
            temp[3] = temp2[start+2];
            temp[4] = temp2[start+3];
        }
        else if(highscore > compare3){
            insertint = highscore;
            temp[0] = temp2[start+0];
            temp[1] = temp2[start+1];
            temp[2] = insertint.toString();
            temp[3] = temp2[start+2];
            temp[4] = temp2[start+3];
        }
        else if (highscore > compare4){
            insertint = highscore;
            temp[0] = temp2[start];
            temp[1] = temp2[start+1];
            temp[2] = temp2[start+2];
            temp[3] = insertint.toString();
            temp[4] = temp2[start+3];
        }
        else if(highscore > compare5){
            insertint = highscore;
            temp[0] = temp2[start];
            temp[1] = temp2[start+1];
            temp[2] = temp2[start+2];
            temp[3] = temp2[start+3];
            temp[4] = insertint.toString();

        }
        else{
            return;
        }

        highscores[start] = temp[0];
        highscores[start+1] = temp[1];
        highscores[start+2] = temp[2];
        highscores[start+3] = temp[3];
        highscores[start+4] = temp[4];

    }
}

