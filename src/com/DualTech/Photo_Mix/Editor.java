package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Editor extends Activity implements View.OnClickListener, GLSurfaceView.Renderer, SeekBar.OnSeekBarChangeListener{

    static ArrayList<Button> effectList;
    Button btBright,btContrast,btNegative,btGrayScale,btRotate,btSaturation,btSepia, btFlip, btGrain, btFillLight;
    GLSurfaceView glView;
    private Effect mEffect;
    public static Bitmap inputBitmap;
    int currentEffect;
    private EffectContext mEffectContext;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int[] mTextures = new int[2];
    int textureWidth, textureHeight, effectCount;
    float value;
    //Effect mEffectArray[] = new Effect[50];
    //private boolean saveFrame;
    private boolean mInitialized = false;
    SeekBar seekBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect);
        inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chicken);
        glView = (GLSurfaceView) findViewById(R.id.effectsView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        currentEffect = effectCount = 0;

        value = 1f;
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
                //mEffectArray[effectCount] = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                //mEffectArray[effectCount].setParameter("brightness", value);
                mEffect.setParameter("brightness", value);
                break;
            case R.id.bt2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                //mEffectArray[effectCount] = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                //mEffectArray[effectCount].setParameter("contrast", value);
                mEffect.setParameter("contrast", value);
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
                mEffect.setParameter("scale", value);
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
                mEffect.setParameter("strength", value);
                break;
            case R.id.bt10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", value);
                break;
        }
        glView.requestRender();
    }

    public void initialize() {
        //mEffectArray = new Effect[50];
        seekBar = (SeekBar) findViewById(R.id.skBar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(this);
        effectList = new ArrayList<Button>();
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
        for(Button x : effectList){
            x.setOnClickListener(this);
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], textureWidth, textureHeight, mTextures[1]);
        /*if (effectCount > 0) { // if there is any effect
            mEffectArray[1].apply(mTextures[0], textureWidth, textureHeight, mTextures[1]); // apply first effect
            for (int i = 2; i < effectCount; i++) { // if more that one effect
                int sourceTexture = mTextures[1];
                int destinationTexture = mTextures[2];
                mEffectArray[i].apply(sourceTexture, textureWidth, textureHeight, destinationTexture);
                mTextures[1] = destinationTexture; // changing the textures array, so 1 is always the texture for output,
                mTextures[2] = sourceTexture; // 2 is always the sparse texture
            }
        }*/
    }

    private void renderResult() {
        if (currentEffect != 0) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            //saveFrame=true;
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                effectCount++;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt1;
                break;
            case R.id.bt2:
                effectCount++;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt2;
                break;
            case R.id.bt3:
                effectCount++;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt3;
                break;
            case R.id.bt4:
                effectCount++;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt4;
                break;
            case R.id.bt5:
                effectCount++;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt5;
                break;
            case R.id.bt6:
                effectCount++;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt6;
                break;
            case R.id.bt7:
                effectCount++;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt7;
                break;
            case R.id.bt8:
                effectCount++;
                seekBar.setVisibility(View.INVISIBLE);
                currentEffect = R.id.bt8;
                break;
            case R.id.bt9:
                effectCount++;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt9;
                break;
            case R.id.bt10:
                effectCount++;
                seekBar.setVisibility(View.VISIBLE);
                currentEffect = R.id.bt10;
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
    }

    //SeekBar so use can use the bar to choose value
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(currentEffect){
            case R.id.bt1:
                value = (float)progress / 6;
                break;
            case R.id.bt2:
                value = (float)progress / 10;
                break;
            case R.id.bt6:
                float temp = progress;
                if(temp <= 5){
                    value = -(temp / 20);
                }else{
                    value = progress / 20;
                }
                break;
            case R.id.bt9:
                value = (float)progress / 12;
                break;
            case R.id.bt10:
                value = (float)progress / 40;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
