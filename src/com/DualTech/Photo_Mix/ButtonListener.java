package com.DualTech.Photo_Mix;
import android.view.View;

/**
 * Created by tunde_000 on 21/06/2015.
 */
public class ButtonListener implements View.OnClickListener {

    Editor editor;

    public ButtonListener(Editor editor){
        this.editor = editor;
    }

    @Override
    public void onClick(View v) {
        editor.seekBar.setVisibility(View.INVISIBLE);
        editor.seekBar.setProgress(10);
        editor.effectText.setText("");
        Editor.picsTaken = 0;
        switch (v.getId()){
           /* case R.id.bt0:
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.color_pop, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                break;*/
            case R.id.bt1:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt1;
                break;
            case R.id.bt2:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt2;
                break;
            case R.id.bt3:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt3;
                break;
            case R.id.bt4:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt4;
                break;
            case R.id.bt5:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt5;
                break;
            case R.id.bt6:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt6;
                break;
            case R.id.bt7:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt7;
                break;
            case R.id.bt8:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt8;
                break;
            case R.id.bt9:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt9;
                break;
            case R.id.bt10:
                editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt10;
                break;
            case R.id.btSave:
                editor.saveFrame = true;
                //Toast.makeText(getApplicationContext(), "Saved to app folder", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btSelect:
                editor.selectPicture();
                break;
            case R.id.icon:
                editor.sendImage = true;
                editor.shareInstagram("image/*", "cHIcken");
                break;
        }
        editor.initEffect();
    }
}
