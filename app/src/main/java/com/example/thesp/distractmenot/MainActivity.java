package com.example.thesp.distractmenot;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.thesp.distractmenot.StringConstants.SHARED_PREF_FILE;

public class MainActivity extends AppCompatActivity {

    // Stores which mode is currently active
    private Mode currentMode;

    // All modes currently available
    private ArrayList<Mode> modes;

    // Changes to a new mode
    private void changeMode(String modeName) {
        // Set the button colors
        if (currentMode != null && currentMode.getModeName() != modeName) {
            // Whatever is the current selected mode should be deactivated
            int resID = getResources().getIdentifier(currentMode.getModeName(), "id", getPackageName());
            Button b = findViewById(resID);
            b.getBackground().setColorFilter(null); // Sets ColorFilter back to default
        }

        currentMode = new Mode(modeName);

        int resID = getResources().getIdentifier(modeName, "id", getPackageName());
        Button b = findViewById(resID);
        b.getBackground().setColorFilter(0xffd84098 /* AARRGGBB (pink) */, PorterDuff.Mode.DARKEN);
    }

    /* Temporary functions for the buttons
        In the future buttons will be added dynamically. I think they should be stored in a list,
        and there can be some kind of function that will connect to all buttons in the list (somehow)
        and it can be applied to the dynamically generated buttons.      -Tyler
     */
    // When a button is pressed, the mode gets set to that one
    public void onButtonPreset1(View view) { changeMode("button_preset1"); }
    public void onButtonPreset2(View view) { changeMode("button_preset2"); }

    // Switch to About activity when the about button is pressed
    public void onAboutButton(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    // Switch to Setup activity when the set up preset button is pressed
    public void onSetUpButton(View view) {
        startActivity(new Intent(this, SetUpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String buttonNameTest = loadSharedPref();

        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonAreaLayout);
        Button newButton = new Button(this);
        newButton.setText(buttonNameTest);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(newButton);
    }

    //What happens when they come back to our app after visiting someplace else?
    @Override
    protected void onResume() {
        super.onResume();
    }

    //what happens when they change to a new screen leaving our app
    //It needs to save Shared Preferences
    @Override
    protected void onPause() {
        super.onPause();
    }

    private String loadSharedPref(){
        Toast.makeText(getApplicationContext(),"Loading User Settings", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("Setting", "");
        return name; //this will be changed to the JSON string once we have that
    }
}
