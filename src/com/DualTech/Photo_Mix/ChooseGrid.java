package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class ChooseGrid extends Activity implements View.OnClickListener{

    private static int chosenGrid;
    public static ArrayList<Button> gridButtons;
    Button btGrid2a,btGrid2b, btGrid2c, btGrid3a;
    GridLayout grid2, grid3, grid4, grid5;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_grid);
        initialize();

    }

    public void initialize(){
        gridButtons = new ArrayList<Button>();
        grid2 = (GridLayout) findViewById(R.id.gL2);
        grid3 = (GridLayout) findViewById(R.id.gL3);
        grid4 = (GridLayout) findViewById(R.id.gL4);
        grid5 = (GridLayout) findViewById(R.id.gL5);
        btGrid2a = (Button) findViewById(R.id.btGrid2a);
        btGrid2b = (Button) findViewById(R.id.btGrid2b);
        btGrid2c = (Button) findViewById(R.id.btGrid2c);
        btGrid3a = (Button) findViewById(R.id.btGrid3a);
        btGrid2a.setOnClickListener(this);
        btGrid2c.setOnClickListener(this);
        btGrid3a.setOnClickListener(this);

    }

    public static int getChosenGrid(){
        return chosenGrid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btGrid2a:
                chosenGrid = 0;
                i = new Intent("com.DualTech.Photo_Mix.GRID");
                startActivity(i);
                break;
            case R.id.btGrid2b:
                chosenGrid = 1;
                i = new Intent("com.DualTech.Photo_Mix.GRID");
                startActivity(i);
                break;
            case R.id.btGrid2c:
                chosenGrid = 2;
                i = new Intent("com.DualTech.Photo_Mix.GRID");
                startActivity(i);
                break;
        }
    }
}
