package com.DualTech.Photo_Mix;

import android.widget.SeekBar;

/**
 * Created by tunde_000 on 21/06/2015.
 */
public class SeekListener implements SeekBar.OnSeekBarChangeListener {
    Editor editor;

    public SeekListener(Editor editor){
        this.editor = editor;
    }

    //SeekBar so use can use the bar to choose value
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(Editor.currentEffect){
            case R.id.bt1:
                editor.vBright = (float)progress / 50;
                editor.effectText.setText("Brightness: " + (editor.vBright * 100) + "%");
                break;
            case R.id.bt2:
                editor.vContrast = (float)progress / 20;
                editor.effectText.setText("Contrast: " + (editor.vContrast * 100) + "%");
                break;
            case R.id.bt6:
                if(progress <= 5){
                    editor.vSat = -(progress / 40);
                }else{
                    editor.vSat = progress / 40;
                }
                editor.effectText.setText("Saturation: " + (editor.vSat * 100) + "%");
                break;
            case R.id.bt9:
                editor.vGrain = (float)progress / 18;
                editor.effectText.setText("Grain: " + (editor.vGrain * 100) + "%");
                break;
            case R.id.bt10:
                editor.vFillLight = (float)progress / 60;
                editor.effectText.setText("Fill-Light: " + (editor.vFillLight * 100) + "%");
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
