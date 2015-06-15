package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Grid extends Activity implements View.OnClickListener {

    Button SaveGrid;
    Button EditGrid;
    static int currentImgID = 0;
    LinearLayout l1;
    Intent i;
    ArrayList<ImageButton> imgbuttons;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (ChooseGrid.getChosenGrid()){
            case 0:
                setContentView(R.layout.grid);
            case 1:
            case 2:
            case 3:
                break;
            default:
                setContentView(R.layout.grid);
                break;
        }
        //setContentView(R.layout.grid);
        initialize();
        initiliazeImg();
    }

    //Goes to Gallery
    public void selectPicture(){
        i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //Typical Initializing of buttons
    public void initialize(){
        SaveGrid = (Button) findViewById(R.id.savegrid);
        EditGrid = (Button) findViewById(R.id.grideffect);
        SaveGrid.setOnClickListener(this);
        EditGrid.setOnClickListener(this);
        l1 = (LinearLayout) findViewById(R.id.linny);
        imgbuttons = initiliazeImg();
        for(ImageButton x: imgbuttons){
            x.setOnClickListener(this);
        }
    }

    //Initializes buttons according to how many there are
    //Very Flexible!!
    private ArrayList<ImageButton> initiliazeImg(){
        //Uses tags for getting all the Image Buttons
        ArrayList<ImageButton> imgViews = new ArrayList<ImageButton>();
        int imgCount = l1.getChildCount();
        for(int i = 0; i < imgCount; i++){
            ImageButton child = (ImageButton)l1.getChildAt(i);
            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals("imgButtons")) {
                imgViews.add(child);
            }
        }
        return imgViews;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.grideffect: //Goes to Editor
                i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                startActivity(i);
                break;
            case R.id.savegrid: //Saves the Image

                break;
        }
        if(v.getTag().equals("imgButtons")){
            for(ImageButton ib: imgbuttons){
                if(v.getId() == ib.getId()) {
                    //get currentID of image button clicked
                    currentImgID = ib.getId();
                }
            }
            selectPicture();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Code to use selected image
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // my ImageView
            ImageButton myPhotoImage = (ImageButton) findViewById(currentImgID);
            myPhotoImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

}
