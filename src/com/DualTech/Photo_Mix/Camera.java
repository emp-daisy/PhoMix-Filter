package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by tunde_000 on 10/06/2015.
 */
public class Camera extends Activity implements View.OnClickListener{

    Button btTakeAgain;
    Button btEdit;
    Button btSave;
    Intent i;
    Bitmap bmp;
    ImageView iv;
    final static int cameraData = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap);
        initialize();
        takePicture();


    }

    public void takePicture(){
        i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, cameraData);
    }

    public void initialize(){
        iv = (ImageView) findViewById(R.id.capture);
        btTakeAgain = (Button) findViewById(R.id.again);
        btSave = (Button) findViewById(R.id.save);
        btEdit = (Button) findViewById(R.id.edit);
        btSave.setOnClickListener(this);
        btTakeAgain.setOnClickListener(this);
        btEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.again:
                takePicture();
                break;
            case R.id.edit:
                i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                startActivity(i);
                break;
            case R.id.save:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            iv.setImageBitmap(bmp);
        }
    }
}
