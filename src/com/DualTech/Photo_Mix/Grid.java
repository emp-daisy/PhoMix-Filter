package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Grid extends Activity implements View.OnClickListener {

    Button SaveGrid;
    Button EditGrid;
    static int currentImgID = 0;
    LinearLayout l1, l2, l3;
    Intent i;
    static ArrayList<ImageButton> imgbuttons;
    private static int RESULT_LOAD_IMAGE = 1;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Photo Mix/");
    File file;
    Bitmap img_bitmap;
    FileOutputStream ostream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (ChooseGrid.getChosenGrid()){
            case 21:
                setContentView(R.layout.grid_2a);
                break;
            case 22:
                setContentView(R.layout.grid_2b);
                break;
            case 23:
                setContentView(R.layout.grid_2c);
                break;
            case 31:
                setContentView(R.layout.grid_3a);
                break;
            case 32:
                setContentView(R.layout.grid_3b);
                break;
            case 33:
                setContentView(R.layout.grid_3c);
                break;
            case 41:
                setContentView(R.layout.grid_4a);
                break;
            case 42:
                setContentView(R.layout.grid_4b);
                break;
            case 51:
                setContentView(R.layout.grid_5a);
                break;
            default:
                setContentView(R.layout.grid);
                break;
        }
        initialize();
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
        l2 = (LinearLayout) findViewById(R.id.linny2);
        l3 = (LinearLayout) findViewById(R.id.linny3);
        imgbuttons = new ArrayList<ImageButton>();
        if(l1 != null)
            initiliazeImg(l1);
        if(l2 != null)
            initiliazeImg(l2);
        if(l3 != null)
            initiliazeImg(l3);
        for(ImageButton x: imgbuttons){
            x.setOnClickListener(this);
        }

    }

    //Initializes buttons according to how many there are
    //Very Flexible!!
    private void initiliazeImg(LinearLayout l){
        //Uses tags for getting all the Image Buttons
        int imgCount = l.getChildCount();
        for(int i = 0; i < imgCount; i++){
            View child = l.getChildAt(i);
            if(child instanceof ImageButton){
                ImageButton im = (ImageButton)child;
                final Object tagObj = im.getTag();
                if (tagObj != null && tagObj.equals("imgButtons")) {
                    imgbuttons.add(im);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.grideffect: //Goes to Editor
                i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                startActivity(i);
                break;

            case R.id.savegrid: //Saves the Image
                String file_sub = new SimpleDateFormat("ddMyy_hhmmss", Locale.getDefault()).format(new Date());
                String file_name =  "/PMX_"+ file_sub + ".jpg";
                //creates the directory if it doesn't exist
                if (!DIR.exists()) {
                    boolean bo = DIR.mkdir();
                }
                file = new File(DIR.getAbsolutePath(), file_name);
                try
                {
                    l1.setDrawingCacheEnabled(true);
                    img_bitmap = l1.getDrawingCache();
                    boolean b = file.createNewFile();
                    ostream = new FileOutputStream(file);
                    img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    try {
                        ostream.flush();
                        ostream.close();
                        Toast.makeText(getApplicationContext(), "Saved to app folder", Toast.LENGTH_SHORT ).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
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
