package com.DualTech.PhoMix_Filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tunde_000 on 10/06/2015.
 */
public class Camera extends Activity implements View.OnClickListener{

    Button btTakeAgain,btEdit, btSave;
    ImageButton btShare, overFlow;
    Intent i;
    static Bitmap bmp,img_bitmap;
    ImageView iv;
    final static int cameraData = 0;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PhoMix Filter/");
    File file;
    FileOutputStream ostream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.snap);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
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
        btSave = (Button) findViewById(R.id.btSave);
        btEdit = (Button) findViewById(R.id.edit);
        btShare = (ImageButton) findViewById(R.id.share_icon);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        btShare.setOnClickListener(this);
        btSave.setOnClickListener(this);
        btTakeAgain.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        overFlow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.again:
                takePicture();
                break;
            case R.id.edit:
                iv.setDrawingCacheEnabled(true);
                img_bitmap = iv.getDrawingCache();
                i = new Intent("com.DualTech.PhoMix_Filter.EDITOR");
                Editor.call = 2;
                startActivity(i);
                break;
            case R.id.share_icon:
                iv.setDrawingCacheEnabled(true);
                img_bitmap = iv.getDrawingCache();
                share("image/*","My grid");
                break;
            case R.id.overflow:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(this, overFlow);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new MenuItemListener(this));
                popup.show();
                break;
            case R.id.btSave:
                String file_sub = new SimpleDateFormat("ddMyy_hhmmss", Locale.getDefault()).format(new Date());
                String file_name =  "/PMX_"+ file_sub + ".jpg";
                //creates the directory if it doesn't exist
                if (!DIR.exists()) {
                    boolean bo = DIR.mkdir();
                }
                file = new File(DIR.getAbsolutePath(), file_name);
                try
                {
                    iv.setDrawingCacheEnabled(true);
                    boolean b = file.createNewFile();
                    ostream = new FileOutputStream(file);
                    iv.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    try {
                        ostream.flush();
                        ostream.close();
                        Toast.makeText(getApplicationContext(), "Saved to app folder as " + file_name, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(file));
                    sendBroadcast(intent);
                }
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            Editor.inputBitmap = bmp;
            iv.setImageBitmap(bmp);
        }
    }
}
