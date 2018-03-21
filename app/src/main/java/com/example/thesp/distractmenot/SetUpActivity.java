package com.example.thesp.distractmenot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

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
