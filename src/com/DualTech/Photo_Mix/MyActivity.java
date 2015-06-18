package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MyActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */
    ImageButton btCamera;
    ImageButton btEditor;
    ImageButton btGrid;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialize();
    }

    public void initialize(){ //Initialize all Buttons
        btCamera = (ImageButton) findViewById(R.id.cam);
        btEditor = (ImageButton) findViewById(R.id.effect);
        btGrid = (ImageButton) findViewById(R.id.grid);
        btCamera.setOnClickListener(this);
        btEditor.setOnClickListener(this);
        btGrid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cam:
                i = new Intent("com.DualTech.Photo_Mix.CAMERA");
                startActivity(i);
                break;
            case R.id.effect:
                i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                Editor.call=0;
                startActivity(i);
                break;
            case R.id.grid:
                i = new Intent("com.DualTech.Photo_Mix.CHOOSE_GRID");
                startActivity(i);
                break;
        }
    }
}
