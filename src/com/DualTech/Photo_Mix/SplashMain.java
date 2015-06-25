package com.DualTech.Photo_Mix;

/**
 * Created by Jesz on 09-Jun-15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent openMain = new Intent("com.DualTech.Photo_Mix.MAIN_ACTIVITY");
                    startActivity(openMain);
                }
            }
        };
        timer.start();
    }
}
