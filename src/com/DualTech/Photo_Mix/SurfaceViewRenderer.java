package com.DualTech.Photo_Mix;
import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SurfaceViewRenderer implements GLSurfaceView.Renderer {
    Editor editor;
    GLSurfaceView glView;
    int currentEffect;
    public Effect mEffect;
    public EffectContext mEffectContext;
    public TextureRenderer mTexRenderer = new TextureRenderer();
    public int[] mTextures = new int[2];
    int textureWidth, textureHeight;
    public boolean saveFrame;
    public boolean mInitialized = false;
    public static boolean effectBool;
    static public boolean sendImage;
    static public boolean rotateOn;

    public SurfaceViewRenderer(Editor editor, GLSurfaceView glView){
        rotateOn = effectBool = false;
        this.editor = editor;
        this.glView = glView;
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        currentEffect = 0;
        sendImage = false;
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

    public void applyEffect() {
        mEffect.apply(mTextures[0], textureWidth, textureHeight, mTextures[1]);
    }

    public void renderResult() {
        if (Editor.effectOn) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            //saveFrame=true;
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
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
        Editor.picsTaken++;
        return mBitmap;
    }

    public void initEffect(){
        EffectFactory effectFactory = mEffectContext.getFactory();
        switch (Editor.currentEffect){
            case R.id.bt1:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", editor.vBright);
                break;
            case R.id.bt2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", editor.vContrast);
                break;
            case R.id.bt3:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
                break;
            case R.id.bt4:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;
            case R.id.bt5:
                /*mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", editor.angle);*/
                break;
            case R.id.bt6:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", editor.vSat);
                break;
            case R.id.bt7:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;
            case R.id.bt8:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;
            case R.id.bt9:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", editor.vGrain);
                break;
            case R.id.bt10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", editor.vFillLight);
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
        if (Editor.changeImage) {
            loadTextures();
            Editor.changeImage = false;
        }

        //Apply Effect if used
        if (Editor.currentEffect != 0) {
            //if an effect is chosen initialize it and apply it to the texture
            if(effectBool){
                effectBool = false;
                Editor.inputBitmap = takeScreenshot(gl);
                Editor.picsTaken = 0;
                loadTextures();
            }
            initEffect();
            applyEffect();
        }
        renderResult();

        //Save the Photo
        if (saveFrame) {
            if (Editor.picsTaken == 0) {
                editor.saveBitmap(takeScreenshot(gl));
                saveFrame = false;
            }
        }

        if (sendImage) {
            Editor.lastPicTaken = takeScreenshot(gl);
            editor.share("image/*", "cHIcken");
            sendImage = false;
        }

        if(rotateOn){
            Editor.inputBitmap = editor.rotate(takeScreenshot(gl));
            Editor.picsTaken = 0;
            rotateOn = false;
            //TextureRenderer.clearScreen();
            loadTextures();
        }
    }
}
