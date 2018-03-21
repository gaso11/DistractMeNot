package com.example.thesp.distractmenot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.thesp.distractmenot.AppObject.getAllApps;
import static com.example.thesp.distractmenot.StringConstants.NEW_BUTTON_NAME;

/**
 * This class corresponds to our activity_set_up.xml. It has the java code for all operations on
 * that page.
 * @author Emily T
 */
public class SetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        // Get the list of apps
        List<AppObject> applist = AppObject.getAllApps(this);
        for (int i = 0; i < applist.size(); i++) {
            Log.i("App #" + i, applist.get(i).getName());
        }
    }

    public void onNewMode(View view) {
        EditText settingName = findViewById(R.id.settingName);
        String buttonName = settingName.getText().toString();

        //Save Settings
        Toast.makeText(getApplicationContext(),"Saving Settings", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Setting", buttonName);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NEW_BUTTON_NAME, buttonName);
        startActivity(intent);
    }

    /**
     * This opens a clock for the user to choose a time that our app will be active until.
     * @param view
     * @author Emily T
     */
    public void showTimePickerDialog(View view) {

    }
}
