package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Grid extends Activity implements View.OnClickListener, SelectColor.OnColorChangedListener {

    Button SaveGrid, EditGrid, ColBorder;
    ImageButton btShare;
    static int currentImgID = 0;
    LinearLayout l1, l2, l3;
    Intent i;
    static ArrayList<ImageButton> imgbuttons;
    private static int RESULT_LOAD_IMAGE = 1;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Photo Mix/");
    File file;
    static Bitmap img_bitmap;
    FileOutputStream ostream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //l1.setLayoutParams().height = l1.getWidth();
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

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        initialize();
    }

    //Goes to Gallery
    public void selectPicture(){
        i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //Typical Initializing of buttons
    public void initialize(){
        SaveGrid = (Button) findViewById(R.id.btSave);
        EditGrid = (Button) findViewById(R.id.grideffect);
        ColBorder = (Button) findViewById(R.id.grid_col);
        btShare = (ImageButton) findViewById(R.id.share_icon);
        btShare.setOnClickListener(this);
        ColBorder.setOnClickListener(this);
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

    //Used to get URI of bitmap image
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void share(String type, String caption){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        Uri uri = getImageUri(this,img_bitmap);
        // Add the URI and the caption to the Intent.
        //if(uri != null)
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.grid_col: //Border color
                new SelectColor(this, Grid.this, Color.WHITE).show();
                break;

            case R.id.share_icon:
                l1.setDrawingCacheEnabled(true);
                img_bitmap = l1.getDrawingCache();
                share("image/*","My grid");
                break;

            case R.id.grideffect: //Goes to Editor
                try
                {
                    l1.setDrawingCacheEnabled(true);
                    img_bitmap = l1.getDrawingCache();
                    i = new Intent("com.DualTech.Photo_Mix.EDITOR");
                    Editor.call = 1;
                    startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.btSave: //Saves the Image
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
                        Toast.makeText(getApplicationContext(), "Saved to app folder as "+ file_name, Toast.LENGTH_SHORT ).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(file));
                    sendBroadcast(intent);
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
            int width = myPhotoImage.getWidth();
            int height = myPhotoImage.getHeight();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), width, height, true);
            myPhotoImage.setImageBitmap(scaledBitmap);

        }
    }

    @Override
    public void colorChanged(int color) {
        Grid.this.findViewById(R.id.linny).setBackgroundColor(color);
    }

}
