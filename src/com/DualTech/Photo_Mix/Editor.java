package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Editor extends Activity implements View.OnClickListener, GLSurfaceView.Renderer, SeekBar.OnSeekBarChangeListener{

    static ArrayList<Button> effectList;
    final static File DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Photo Mix/");
    Button btBright,btContrast,btNegative,btGrayScale,btRotate,btSaturation,btSepia, btFlip, btGrain, btFillLight;
    GLSurfaceView glView;
    File file;
    private Effect mEffect;
    public static Bitmap inputBitmap;
    int currentEffect;
    private EffectContext mEffectContext;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int[] mTextures = new int[2];
    int textureWidth, textureHeight, effectCount;
    float vBright, vContrast, vSat, vGrain, vFillLight;
    Button btSave, btSelect;
    private boolean saveFrame;
    private boolean effectOn;
    private boolean mInitialized = false;
    SeekBar seekBar;
    TextView effectText;
    Intent i;
    static int call=0;
    static int picsTaken = 0;
    FileOutputStream out;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect);
        if(inputBitmap == null)
            inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chicken);
        if(call == 1)
            inputBitmap = Grid.img_bitmap;
        glView = (GLSurfaceView) findViewById(R.id.effectsView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        currentEffect = 0;
        effectCount = 0;
        vBright = vContrast = 0;
        vSat = vGrain = vFillLight = 0f;
        saveFrame = effectOn = false;
        initialize();
    }

    private void loadTextures(){
        GLES20.glGenTextures(2, mTextures, 0);
        textureHeight = inputBitmap.getHeight();
        textureWidth = inputBitmap.getWidth();

        mTexRenderer.updateTextureSize(textureWidth, textureHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, inputBitmap, 0);

        // Set texture parameters
        GLToolBox.initTexParams();
    }

    public void initEffect(){
        EffectFactory effectFactory = mEffectContext.getFactory();
        /*if(mEffect != null) {
            mEffect.release();
        }*/
        switch (currentEffect){
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
                mEffect.setParameter("angle", 180);
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

    public void initialize() {
        seekBar = (SeekBar) findViewById(R.id.skBar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(this);
        effectList = new ArrayList<Button>();
        effectText = (TextView)findViewById(R.id.tvEffect);
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
        btSelect.setOnClickListener(this);
        btSave.setOnClickListener(this);
        for(Button x : effectList){
            x.setOnClickListener(this);
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], textureWidth, textureHeight, mTextures[1]);
    }

    private void renderResult() {
        if (!effectOn) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[0]);
        }
        else {
            //saveFrame=true;
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[1]);
        }
    }


    @Override
    public void onClick(View v) {
        seekBar.setVisibility(View.INVISIBLE);
        picsTaken = 0;
        switch (v.getId()){
            case R.id.bt1:
                effectOn = true;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt1;
                break;
            case R.id.bt2:
                effectOn = true;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt2;
                break;
            case R.id.bt3:
                effectOn = true;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt3;
                break;
            case R.id.bt4:
                effectOn = true;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt4;
                break;
            case R.id.bt5:
                effectOn = true;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt5;
                break;
            case R.id.bt6:
                effectOn = true;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt6;
                break;
            case R.id.bt7:
                effectOn = true;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt7;
                break;
            case R.id.bt8:
                effectOn = true;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt8;
                break;
            case R.id.bt9:
                effectOn = true;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt9;
                break;
            case R.id.bt10:
                effectOn = true;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt10;
                break;
            case R.id.btSave:
                effectOn = true;
                saveFrame = true;
                Toast.makeText(getApplicationContext(), "Saved to app folder", Toast.LENGTH_SHORT ).show();
                break;
            case R.id.btSelect:
                SelectPhoto();
                break;
        }

        initEffect();
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

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (currentEffect != 0) {
            //if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        renderResult();
        if (saveFrame) {
            if(picsTaken == 0){
                saveBitmap(takeScreenshot(gl));
                saveFrame = false;
            }
        }
    }

    //SeekBar so use can use the bar to choose value
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(currentEffect){
            case R.id.bt1:
                vBright = (float)progress / 6;
                effectText.setText("Brightness: " + (vBright * 100) + "%");
                break;
            case R.id.bt2:
                vContrast = (float)progress / 10;
                effectText.setText("Contrast: " + (vContrast * 100) + "%");
                break;
            case R.id.bt6:
                if(progress <= 5){
                    vSat = -(progress / 20);
                }else{
                    vSat = progress / 20;
                }
                effectText.setText("Saturation: " + (vSat * 100) + "%");
                break;
            case R.id.bt9:
                vGrain = (float)progress / 12;
                effectText.setText("Grain: " + (vGrain * 100) + "%");
                break;
            case R.id.bt10:
                vFillLight = (float)progress / 40;
                effectText.setText("Fill-Light: " + (vFillLight * 100) + "%");
                break;
        }
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
        picsTaken++;
        return mBitmap;
    }

    private void saveBitmap(Bitmap bitmap) {
        String file_sub = new SimpleDateFormat("ddMyy_hhmmss", Locale.getDefault()).format(new Date());
        String fname = "/PMX_"+ file_sub +".jpg";
        if (!DIR.exists()) {
            boolean bo = DIR.mkdir();
        }
        file = new File (DIR.getAbsolutePath(), fname);
        if (file.exists ())
            file.delete ();
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
                Log.i("TAG", "Image SAVED=========="+file.getAbsolutePath());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void SelectPhoto(){
        i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
        }
    }
}
