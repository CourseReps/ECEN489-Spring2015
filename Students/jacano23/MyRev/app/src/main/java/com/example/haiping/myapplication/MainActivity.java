package com.example.haiping.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] statistic = new String[18];
        File f = new File(Environment.getExternalStorageDirectory().getPath(),"schema.xml");
        if(f.exists() && !f.isDirectory()) { /* do something */ }
        else{
            statistic = intialstatistic();
            Reversi.write_xml(statistic);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Initialize buttons
        ImageButton newGame = (ImageButton)findViewById(R.id.newGameButton);
        ImageButton highScores = (ImageButton)findViewById(R.id.highScoresButton);
        ImageButton about = (ImageButton) findViewById(R.id.aboutButton);
        ImageButton exit = (ImageButton) findViewById(R.id.exitButton);
        exit.setOnClickListener(exithandler);
        ImageButton ab = (ImageButton) findViewById(R.id.aboutButton);
        ab.setOnClickListener(abouthandler);
    }

    // When you click newGame button go to the Difficulty screen
    public void onGameClick(View view) {

        Intent newGame = new Intent(this,DifficultyScreen.class);
        startActivity(newGame);
    }

    View.OnClickListener abouthandler = new View.OnClickListener(){
        public void onClick(View view) {

           Intent About = new Intent(MainActivity.this, AboutScreen.class);
            startActivity(About);
        }
    };

    // When you click the highScores button
    public void onStatsClick(View view) {
        Intent highScores = new Intent(this, StatisticsScreen.class);
        startActivity(highScores);

    }
    public void onMainClick(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
       //moveTaskToBack(true);

    }
    View.OnClickListener exithandler = new View.OnClickListener(){
        public void onClick(View v){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        {

            switch (item.getItemId())
            {
                case R.id.menu_about:
                    Toast.makeText(MainActivity.this, "You Clicked About", 3000).show();
                    Intent intent = new Intent(this, DifficultyScreen.class);
                    startActivity(intent);
                    return true;
                case R.id.menu_exit:
                    Toast.makeText(MainActivity.this, "You Clicked Exit", 3000).show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }
    public String[] intialstatistic(){
        String[] newstat = new String[18];
        for(int i = 0; i < 18; ++i){
            if(i == 0 ){
                newstat[i] = "easy";
            }
            else if(i == 6){
                newstat[i] = "medium";
            }
            else if(i == 12){
                newstat[i] = "hard";
            }
            else{
                newstat[i] =  "0";
            }
        }
        return newstat;
    }


}
