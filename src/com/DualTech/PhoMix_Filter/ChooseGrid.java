package com.DualTech.PhoMix_Filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.ArrayList;

public class ChooseGrid extends Activity implements View.OnClickListener{

    private static int chosenGrid;
    public static ArrayList<Button> gridButtons;
    Button btGrid2a,btGrid2b, btGrid2c, btGrid3a, btGrid3b, btGrid3c, btGrid4a, btGrid4b, btGrid5a;
    GridLayout grid2, grid3, grid4, grid5;
    ImageButton overFlow;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.choose_grid);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title);
        initialize();
    }

    public void initialize(){
        gridButtons = new ArrayList<Button>();
        grid2 = (GridLayout) findViewById(R.id.gL2);
        grid3 = (GridLayout) findViewById(R.id.gL3);
        grid4 = (GridLayout) findViewById(R.id.gL4);
        grid5 = (GridLayout) findViewById(R.id.gL5);
        //buttons initialization
        btGrid2a = (Button) findViewById(R.id.btGrid2a);
        btGrid2b = (Button) findViewById(R.id.btGrid2b);
        btGrid2c = (Button) findViewById(R.id.btGrid2c);
        btGrid3a = (Button) findViewById(R.id.btGrid3a);
        btGrid3b = (Button) findViewById(R.id.btGrid3b);
        btGrid3c = (Button) findViewById(R.id.btGrid3c);
        btGrid4a = (Button) findViewById(R.id.btGrid4a);
        btGrid4b = (Button) findViewById(R.id.btGrid4b);
        btGrid5a = (Button) findViewById(R.id.btGrid5a);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        btGrid2a.setOnClickListener(this);
        btGrid2b.setOnClickListener(this);
        btGrid2c.setOnClickListener(this);
        btGrid3a.setOnClickListener(this);
        btGrid3b.setOnClickListener(this);
        btGrid3c.setOnClickListener(this);
        btGrid4a.setOnClickListener(this);
        btGrid4b.setOnClickListener(this);
        btGrid5a.setOnClickListener(this);
        overFlow.setOnClickListener(this);
    }

    public static int getChosenGrid(){
        return chosenGrid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btGrid2a:
                chosenGrid = 21;
                break;
            case R.id.btGrid2b:
                chosenGrid = 22;
                break;
            case R.id.btGrid2c:
                chosenGrid = 23;
                break;
            case R.id.btGrid3a:
                chosenGrid = 31;
                break;
            case R.id.btGrid3b:
                chosenGrid = 32;
                break;
            case R.id.btGrid3c:
                chosenGrid = 33;
                break;
            case R.id.btGrid4a:
                chosenGrid = 41;
                break;
            case R.id.btGrid4b:
                chosenGrid = 42;
                break;
            case R.id.btGrid5a:
                chosenGrid = 51;
                break;
            /*case R.id.overflow:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(this, overFlow);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new MenuItemListener(this));
                popup.show();
                break;*/
        }
        if(v.getId() != R.id.overflow){
            //star grid activity
            i = new Intent("com.DualTech.PhoMix_Filter.GRID");
            startActivity(i);
        }
        else{
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(this, overFlow);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(new MenuItemListener(this));
            popup.show();
        }
    }

}
