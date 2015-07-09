package com.DualTech.PhoMix_Filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by Jesz on 05-Jul-15.
 */
public class MenuItemListener implements PopupMenu.OnMenuItemClickListener {
    Intent i;
    Context context;

    MenuItemListener(Context c){
        context = c;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.report:
                i = new Intent("com.DualTech.PhoMix_Filter.REPORT_BUG");
                context.startActivity(i);
                return true;
            case R.id.about:
                i = new Intent("com.DualTech.PhoMix_Filter.ABOUT_US");
                context.startActivity(i);
                return true;
            case R.id.gift:
                return true;
            default:
                return false;
        }
    }

}
