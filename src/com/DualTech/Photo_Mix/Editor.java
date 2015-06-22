package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Editor extends Activity implements GLSurfaceView.Renderer,  SelectColor.OnColorChangedListener{

    static ArrayList<Button> effectList;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Photo Mix/");
    Button btBright,btContrast,btNegative,btGrayScale,btRotate,btSaturation,btSepia, btFlip, btGrain, btFillLight,btBorder;
    private static int RESULT_LOAD_IMAGE = 1;
    GLSurfaceView glView;
    Intent i;
    File file;
    static int currentEffect;
    Bitmap lastPicTaken;
    public static Bitmap inputBitmap;
    float vBright, vContrast, vSat, vGrain, vFillLight;
    Button btSave, btSelect;
    SeekBar seekBar;
    TextView effectText;
    ImageButton share;
    static int call=0;
    static int picsTaken = 0;
    FileOutputStream out;
    public Effect mEffect;
    public EffectContext mEffectContext;
    public TextureRenderer mTexRenderer = new TextureRenderer();
    public int[] mTextures = new int[2];
    int textureWidth, textureHeight;
    public boolean saveFrame;
    public boolean effectOn, changeImage;
    public boolean mInitialized = false;
    public boolean sendImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.effect);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);

        if(call == 0)
            inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chicken);
        else
            inputBitmap = Grid.img_bitmap;

        glView = (GLSurfaceView) findViewById(R.id.effectsView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        vBright = vContrast = 1;
        vSat = vGrain = vFillLight = 0f;

        effectOn = false;
        initialize();
        changeImage = false; //Check if user used select photo button
    }

    public void initialize() {
        share = (ImageButton) findViewById(R.id.icon);
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
        btSelect = (Button)findViewById(R.id.btSelect);
        btSave = (Button)findViewById(R.id.btSave);
        btSelect.setOnClickListener(new ButtonListener(this));
        btSave.setOnClickListener(new ButtonListener(this));
        for(Button x : effectList){
            x.setOnClickListener(new ButtonListener(this));
        }
        share.setOnClickListener(new ButtonListener(this));
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

                Toast.makeText(getApplicationContext(), "Saved to app folder as "+ fname, Toast.LENGTH_SHORT ).show();
                out.flush();
                out.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);
                Log.i("TAG", "Image SAVED==========" + file.getAbsolutePath());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void shareInstagram(String type, String caption){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        Uri uri = getImageUri(this,lastPicTaken);
        // Add the URI and the caption to the Intent.
        if(uri != null)
            share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    //Used to get URI of bitmap image
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        //pauseThread();
        if(inImage == null)
            return null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void initEffect(){
        EffectFactory effectFactory = mEffectContext.getFactory();
        /*if(mEffect != null) {
            mEffect.release();
        }*/
        switch (Editor.currentEffect){
            case R.id.bt1:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", vBright);
                break;
            case R.id.bt2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", vContrast);
                break;
            case R.id.bt3:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
                break;
            case R.id.bt4:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;
            case R.id.bt5:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", 90);
                break;
            case R.id.bt6:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", vSat);
                break;
            case R.id.bt7:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;
            case R.id.bt8:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;
            case R.id.bt9:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", vGrain);
                break;
            case R.id.bt10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", vFillLight);
                break;
        }
        glView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    public void applyEffect() {
        mEffect.apply(mTextures[0], textureWidth, textureHeight, mTextures[1]);
    }

    public void renderResult() {
        if (effectOn) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            //saveFrame=true;
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    public void loadTextures(){
        GLES20.glGenTextures(2, mTextures, 0);
        textureHeight = Editor.inputBitmap.getHeight();
        textureWidth = Editor.inputBitmap.getWidth();

        mTexRenderer.updateTextureSize(textureWidth, textureHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, Editor.inputBitmap, 0);

        // Set texture parameters
        GLToolBox.initTexParams();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//Initializes in the beginning
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }

        //If user selects photo using select button
        //Load the textures again so the textures uses new bitmap
        if (changeImage) {
            loadTextures();
            changeImage = false;
        }

        //Apply Effect if used
        if (currentEffect != 0) {
            //if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        renderResult();

        //Save the Photo
        if (saveFrame) {
            if (picsTaken == 0) {
                saveBitmap(takeScreenshot(gl));
                saveFrame = false;
            }
        }

        if (sendImage) {
            lastPicTaken = takeScreenshot(gl);
            sendImage = false;
        }
    }

    @Override
    public void colorChanged(int color) {
        Editor.this.findViewById(R.id.linny).setBackgroundColor(color);
    }

    public void getColor(){
        new SelectColor(this, Editor.this, Color.WHITE).show();
    }

    public Bitmap takeScreenshot(GL10 mGL) {
        final int width = glView.getWidth();
        final int height = glView.getHeight();
        IntBuffer ib = IntBuffer.allocate(width * height);
        IntBuffer ibt = IntBuffer.allocate(width * height);
        mGL.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

        // Convert upside down mirror-reversed image to right-side up normal
        // image.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ibt.put((height - i - 1) * width + j, ib.get(i * width + j));
            }
        }

        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(ibt);
        Editor.picsTaken++;
        return mBitmap;
    }

    /*private void pauseThread(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 2000);
    }*/
}
