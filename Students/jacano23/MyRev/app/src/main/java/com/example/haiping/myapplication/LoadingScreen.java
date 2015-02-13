package com.example.haiping.myapplication;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by JoshuaCano on 7/7/14.
 */
public class LoadingScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        Thread timer = new Thread() {
            public void run(){
                try {
                    sleep(4000);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent mainMenu = new Intent("com.example.haiping.myapplication.MAINMENU");

                    startActivity(mainMenu);
                }
            }
        };
        timer.start();
    }
    protected void onPause() {
        super.onPause();
        finish();
    }
}