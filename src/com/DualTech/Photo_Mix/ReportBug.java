package com.DualTech.Photo_Mix;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

/**
 * Created by Jesz on 05-Jul-15.
 */
public class ReportBug extends Activity implements View.OnClickListener {
    Button sendEmail;
    EditText personsName, report;
    String rName, message;
    ImageButton overFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.report_bug);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title);

        report = (EditText) findViewById(R.id.etAction);
        personsName = (EditText) findViewById(R.id.etName);
        sendEmail = (Button) findViewById(R.id.bSentEmail);
        overFlow = (ImageButton) findViewById(R.id.overflow);
        sendEmail.setOnClickListener(this);
        overFlow.setOnClickListener(this);

        message = report.getText().toString();
        rName = personsName.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bSentEmail:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
                String mailTo[] = new String[] { "adajess01@gmail.com", "tundetunde000@gmail.com" };
                emailIntent.setType("text/plain");
                emailIntent.setData(Uri.parse("mailto:tundetunde000@gmail.com,adajess01@gmail.com"));;
                //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mailTo);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bug Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, report.getText());
                emailIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Choose an email client from..."));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No email client installed.", Toast.LENGTH_LONG).show();
                }
                break;
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
