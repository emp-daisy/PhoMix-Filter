package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
public class Editor extends Activity implements View.OnClickListener{

    static ArrayList<Button> effectList;
    Button b1,b2,b3,b4,b5,b6,b7, b8, b9, b10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect);
        initialize();
    }

    public void initialize() {
        effectList = new ArrayList<Button>();
        b1 = (Button)findViewById(R.id.bt1);
        b2 = (Button)findViewById(R.id.bt2);
        b3 = (Button)findViewById(R.id.bt3);
        b4 = (Button)findViewById(R.id.bt4);
        b5 = (Button)findViewById(R.id.bt5);
        b6 = (Button)findViewById(R.id.bt6);
        b7 = (Button)findViewById(R.id.bt7);
        b8 = (Button)findViewById(R.id.bt8);
        b9 = (Button)findViewById(R.id.bt9);
        b10 = (Button)findViewById(R.id.bt10);
        effectList.add(b1);
        effectList.add(b2);
        effectList.add(b3);
        effectList.add(b4);
        effectList.add(b5);
        effectList.add(b6);
        effectList.add(b7);
        for(Button x : effectList){
            x.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                break;
            case R.id.bt2:
                break;
            case R.id.bt3:
                break;
            case R.id.bt4:
                break;
            case R.id.bt5:
                break;
            case R.id.bt6:
                break;
            case R.id.bt7:
                break;
            case R.id.bt8:
                break;
            case R.id.bt9:
                break;
            case R.id.bt10:
                break;

        }
    }
}
