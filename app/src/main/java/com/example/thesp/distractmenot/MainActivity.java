package com.example.thesp.distractmenot;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Stores which mode is currently active
    private Mode currentMode;

    // Changes to a new mode
    private void changeMode(String modeName) {
        // Set the button colors
        if (currentMode != null && currentMode.getModeName() != modeName) {
            // Whatever is the current selected mode should be deactivated
            int resID = getResources().getIdentifier(currentMode.getModeName(), "id", getPackageName());
            Button b = findViewById(resID);
            b.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        currentMode = new Mode(modeName);

        int resID = getResources().getIdentifier(modeName, "id", getPackageName());
        Button b = findViewById(resID);
        b.setBackgroundColor(Color.parseColor("#d84098"));
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
    }
}
