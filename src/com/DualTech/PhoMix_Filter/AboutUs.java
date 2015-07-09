package com.DualTech.PhoMix_Filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

/**
 * Created by Jesz on 05-Jul-15.
 */
public class AboutUs extends Activity implements View.OnClickListener{

    ImageButton overFlow;
    TextView tv;
    String text1 = "PhoMix Filter is a photo editing app enabling you to design and edit your photos with complete control. " +
            "You may create photo grids combining multiple pictures into one or you may use the built in editor which boasts " +
            "many effects and filters to apply to photos. " +
            "PhoMix Filter also features the ability to share your photos on many different social networks.";

    String text2 = "PhoMix Filter is created by two software developers, Oyetunde Awotunde and Jessica Adachi. " +
            "Both of us are in university currently studying Computer Science. This app was made as a project between " +
            "the both of us as a hobby. " +
            "We strive to produce the best photo editing app on the app store and will produce strong updates in order to achieve this.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.about_us);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title);
        tv = (TextView) findViewById(R.id.textDescription);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        overFlow.setOnClickListener(this);
        tv.setText(text1);
        tv.append("\n\n" + text2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.overflow:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(this, overFlow);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new MenuItemListener(this));
                popup.show();
                break;
        }
    }
}
