package com.DualTech.PhoMix_Filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class MyActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */
    ImageButton btCamera, btEditor, btGrid, overFlow;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title);
        initialize();
    }

    public void initialize(){ //Initialize all Buttons
        btCamera = (ImageButton) findViewById(R.id.cam);
        btEditor = (ImageButton) findViewById(R.id.effect);
        btGrid = (ImageButton) findViewById(R.id.grid);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        btCamera.setOnClickListener(this);
        btEditor.setOnClickListener(this);
        btGrid.setOnClickListener(this);
        overFlow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cam:
                i = new Intent("com.DualTech.PhoMix_Filter.CAMERA");
                startActivity(i);
                break;
            case R.id.effect:
                i = new Intent("com.DualTech.PhoMix_Filter.EDITOR");
                Editor.call=0;
                startActivity(i);
                break;
            case R.id.grid:
                i = new Intent("com.DualTech.PhoMix_Filter.CHOOSE_GRID");
                startActivity(i);
                break;
            case R.id.overflow:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(this, overFlow);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new MenuItemListener(this));
                popup.show();
                break;
        }
    }
}
