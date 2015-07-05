package com.DualTech.Photo_Mix;

import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

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
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt1;
                break;
            case R.id.bt2:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt2;
                break;
            case R.id.bt3:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt3;
                break;
            case R.id.bt4:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt4;
                break;
            case R.id.bt5:
                if(editor.angle == 360)
                    editor.angle = 0;
                else
                    editor.angle += 90;
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt5;
                break;
            case R.id.bt6:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt6;
                break;
            case R.id.bt7:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt7;
                break;
            case R.id.bt8:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.INVISIBLE);
                Editor.currentEffect = R.id.bt8;
                break;
            case R.id.bt9:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt9;
                break;
            case R.id.bt10:
                Editor.effectOn = true;
                editor.seekBar.setVisibility(View.VISIBLE);
                Editor.currentEffect = R.id.bt10;
                break;
            case R.id.btSave:
                editor.surfaceViewRenderer.saveFrame = true;
                Toast.makeText(editor.getApplicationContext(), "Saved to app folder", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btSelect:
                editor.selectPicture();
                break;
            case R.id.share_icon:
                SurfaceViewRenderer.sendImage = true;
                break;
            case R.id.bt0:
                editor.getColor();
                break;
            case R.id.overflow:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(v.getContext(), editor.overFlow);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new MenuItemListener(editor));
                popup.show();
                break;
        }
        editor.surfaceViewRenderer.initEffect();
    }
}
