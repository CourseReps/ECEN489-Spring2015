package com.example.haiping.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;



public class DifficultyScreen extends Activity {
    String tt="EASY";
    ImageButton blackButton;
    ImageButton whiteButton;
    ImageButton hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_screen);
        blackButton= (ImageButton)findViewById(R.id.WhiteButton);
        whiteButton =(ImageButton)findViewById(R.id.BlackButton);
        hardButton =(ImageButton)findViewById(R.id.mainMenuButton);


    }
    //on click to decide difficulty
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.easy_mode:
                if (checked)
                    break;

            case R.id.medium_mode:
                if (checked)
                    tt= new String("MEDIUM");
                    break;

            case R.id.hard_mode:
                if (checked)
                    tt= new String("HARD");
                    break;

            default:
                break;
        }

    }

     //click listener for black color button
    public void onPlayBlackClick(View view) {
        Intent bb = new Intent(DifficultyScreen.this, Reversi.class);
        String temp=tt;
        bb.putExtra("lvl", temp ); // pass "str" to the next Activity
        bb.putExtra("plr","black");
        startActivity(bb);
    }

    //click listener for white color button
    public void onPlayWhiteClick(View view) {
        Intent wb = new Intent(DifficultyScreen.this, Reversi.class);
        String temp=tt;
        wb.putExtra("lvl", temp); // pass "str" to the next Activity
        wb.putExtra("plr", "white");
        startActivity(wb);
    }

    public void onMainMenuClick(View view) {
        Intent goingBack = new Intent();
        finish();
    }


}
