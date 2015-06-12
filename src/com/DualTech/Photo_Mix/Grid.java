package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by tunde_000 on 10/06/2015.
 */
public class Grid extends Activity implements View.OnClickListener {

    Button SaveGrid;
    Button EditGrid;
    Intent i;
    ArrayList<ImageButton> imgbuttons = new ArrayList<ImageButton>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap);
        initialize();
        initiliazeImg();
        selectPicture();
    }

    public void selectPicture(){
        i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, cameraData);
    }

    public void initialize(){
        SaveGrid = (Button) findViewById(R.id.savegrid);
        EditGrid = (Button) findViewById(R.id.grideffect);
        SaveGrid.setOnClickListener(this);
        EditGrid.setOnClickListener(this);
    }

    public void initiliazeImg(){
        String[] number_as_word = {"one", "two", "three", "four", "five", "six", "seven", "eight"};


        for(int i = 0; i < 10; i++){
            String v = "R.id."+ number_as_word[i];
            imgbuttons.add((ImageButton) findViewById();
        }
    }

    @Override
    public void onClick(View v) {

        for(int i = 0; i < 10; i++){
            if()
        }
        switch(v.getId()){
            case R.id.grideffect:
                i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                startActivity(i);
                break;
            case R.id.savegrid:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
        }
    }

}
