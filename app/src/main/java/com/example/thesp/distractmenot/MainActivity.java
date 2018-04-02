package com.example.thesp.distractmenot;

import android.app.Dialog;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity {

    // Stores which mode is currently active
    private Mode currentMode;

    // All modes currently available
    private ArrayList<Mode> modes;

    final String PREFS_NAME = "MyPrefsFile";

    /**
     * Switches the user's active mode
     *
     * @param newMode The mode that we are switching to - or if this mode is
     *                already active, the mode we are disabling
     */

    private void changeMode(Mode newMode) {
        Log.i(this.getLocalClassName(), "Switching active mode");

        // Set the button colors
        if (currentMode != null) {
            // Whatever is the current selected mode should be deactivated
            int resID = getResources().getIdentifier(currentMode.getModeName(), "id", getPackageName());
            Button b = findViewById(resID);
            b.getBackground().setColorFilter(null); // Sets ColorFilter back to default
        }

        if (currentMode != newMode) {
            if (currentMode != null)
                Log.d(this.getLocalClassName(), "Switching from " + currentMode.getModeName() + " to " + newMode.getModeName());
            currentMode = newMode;

            int resID = getResources().getIdentifier(newMode.getModeName(), "id", getPackageName());
            Button b = findViewById(resID);
            b.getBackground().setColorFilter(0xffd84098 /* AARRGGBB (pink) */, PorterDuff.Mode.DARKEN);
        } else {
            Log.d(this.getLocalClassName(), "Deactivating mode " + currentMode.getModeName());
            currentMode = null;
        }
    }

    public void blockApp(String appID) {
        // Send broadcast message to the App Notification Service to start blocking an app
        Intent intent = new Intent("com.example.thesp.distractmenot.Broadcasts");
        intent.putExtra("block_app", appID);
        sendBroadcast(intent);
        Log.i("AppControl2","Sending broadcast from activity to block.");
    }

    /* Temporary functions for the buttons
        In the future buttons will be added dynamically. I think they should be stored in a list,
        and there can be some kind of function that will connect to all buttons in the list (somehow)
        and it can be applied to the dynamically generated buttons.      -Tyler
     */

    // When a button is pressed, the mode gets set to that one
    public void onButtonPreset1(View view) { changeMode(modes.get(0)); }
    public void onButtonPreset2(View view) { changeMode(modes.get(1)); }

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

        // The two default buttons
        modes = new ArrayList<Mode>(20);

        modes.add(0, new Mode("button_preset1", new ArrayList<AppObject>(), this));
        modes.add(1, new Mode("button_preset2", new ArrayList<AppObject>(), this));

      LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutTwo);

        Button newButton = new Button(this);
        newButton.setText(buttonNameTest);

        newButton.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(newButton);
/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED{
             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setMessage("Distract Me Not needs access to notifications. Do you give us permission?").setPositiveButton("Yes", dialogClickListener)
             .setNegativeButton("No", dialogClickListener).show();
        }
*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Because this is using SharedPreferences, if you want to make changes to this
        //you will have to manually delete the saved file if you want the popup to show again.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        //If first time being launched
        if (settings.getBoolean("my_first_time", true)) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog);
            dialog.setTitle("Dialog Box");

            Button button = (Button) dialog.findViewById(R.id.Button01);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            });

            dialog.show();
        }

        //You can also just change the false value to true if you don't want to
        //edit the save folder
        settings.edit().putBoolean("my_first_time", false).apply();
    }

    //What happens when they come back to our app after visiting someplace else?
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getLocalClassName()+ "onResume", "Activity state change: onResume");
    }

    //what happens when they change to a new screen leaving our app
    //It needs to save Shared Preferences
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.getLocalClassName()+ "onPause", "Activity state change: onPause");
    }

    private String loadSharedPref(){
        Toast.makeText(getApplicationContext(),"Loading User Settings", Toast.LENGTH_SHORT).show();
        Log.i(this.getLocalClassName(), "Loading from SharedPreferences");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("Setting", "");
        return name; //this will be changed to the JSON string once we have that
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}
