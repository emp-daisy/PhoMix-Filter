package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLSurfaceView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Editor extends Activity implements SelectColor.OnColorChangedListener{

    static ArrayList<Button> effectList;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Photo Mix/");
    Button btBright,btContrast,btNegative,btGrayScale,btRotate,btSaturation,btSepia, btFlip, btGrain, btFillLight,btBorder,btSave, btSelect;
    ImageButton  overFlow, share;
    private static int RESULT_LOAD_IMAGE = 1;
    GLSurfaceView glView;
    SurfaceViewRenderer surfaceViewRenderer;
    Intent i;
    File file;
    static int currentEffect;
    public static boolean effectOn, changeImage;
    static Bitmap lastPicTaken;
    public static Bitmap inputBitmap;
    float vBright, vContrast, vSat, vGrain, vFillLight;
    SeekBar seekBar;
    TextView effectText;
    LinearLayout l1;
    static int call=0;
    static int picsTaken = 0;
    int angle;
    FileOutputStream out; //Used for rotate
    //FileOutputStream ostream;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.effect);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);
        angle = 0;
        if(call == 0)
            inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.click_select);
        else if(call == 1)
            inputBitmap = Grid.img_bitmap;
        else if(call == 2)
            inputBitmap = Camera.img_bitmap;

        glView = (GLSurfaceView) findViewById(R.id.effectsView);
        surfaceViewRenderer = new SurfaceViewRenderer(this, glView);
        vBright = vContrast = 1;
        vSat = vGrain = vFillLight = 0f;

        effectOn = false;
        initialize();
        changeImage = false; //Check if user used select photo button
    }

    public void initialize() {
        l1 = (LinearLayout) findViewById(R.id.linny);
        share = (ImageButton) findViewById(R.id.share_icon);
        btSelect = (Button)findViewById(R.id.btSelect);
        btSave = (Button)findViewById(R.id.btSave);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        seekBar = (SeekBar) findViewById(R.id.skBar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(new SeekListener(this));
        effectList = new ArrayList<Button>();
        effectText = (TextView)findViewById(R.id.tvEffect);
        btBorder = (Button)findViewById(R.id.bt0);
        btBright = (Button)findViewById(R.id.bt1);
        btContrast = (Button)findViewById(R.id.bt2);
        btNegative = (Button)findViewById(R.id.bt3);
        btGrayScale = (Button)findViewById(R.id.bt4);
        btRotate = (Button)findViewById(R.id.bt5);
        btSaturation = (Button)findViewById(R.id.bt6);
        btSepia = (Button)findViewById(R.id.bt7);
        btFlip = (Button)findViewById(R.id.bt8);
        btGrain = (Button)findViewById(R.id.bt9);
        btFillLight = (Button)findViewById(R.id.bt10);
        effectList.add(btBorder);
        effectList.add(btBright);
        effectList.add(btContrast);
        effectList.add(btNegative);
        effectList.add(btGrayScale);
        effectList.add(btRotate);
        effectList.add(btSaturation);
        effectList.add(btSepia);
        effectList.add(btFlip);
        effectList.add(btGrain);
        effectList.add(btFillLight);
        btSelect.setOnClickListener(new ButtonListener(this));
        btSave.setOnClickListener(new ButtonListener(this));
        share.setOnClickListener(new ButtonListener(this));
        overFlow.setOnClickListener(new ButtonListener(this));
        for(Button x : effectList){
            x.setOnClickListener(new ButtonListener(this));
        }
    }

    public void selectPicture(){
        i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
        i = new Intent("com.DualTech.Photo_Mix.EDITOR");
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
            inputBitmap = BitmapFactory.decodeFile(picturePath);
            changeImage = true;
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        String file_sub = new SimpleDateFormat("ddMyy_hhmmss", Locale.getDefault()).format(new Date());
        String fname = "/PMX_"+ file_sub +".jpg";
        if (!DIR.exists()) {
            boolean bo = DIR.mkdir();
        }
        file = new File(DIR.getAbsolutePath(), fname);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                out.flush();
                out.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void share(String type, String caption){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        Uri uri = getImageUri(this,lastPicTaken);
        // Add the URI and the caption to the Intent.
        //if(uri != null)
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    //Used to get URI of bitmap image
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void colorChanged(int color) {
        Editor.this.findViewById(R.id.linny).setBackgroundColor(color);
    }

    public void getColor(){
        new SelectColor(this, Editor.this, Color.WHITE).show();
    }

}
