package com.example.thesp.distractmenot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
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
import java.util.List;
import java.util.Set;

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

    private Button selectedButton;
    private void changeMode(Mode newMode, View button) {
        Log.i(this.getLocalClassName(), "Switching active mode");
        assert(newMode != null);

        if (selectedButton != null) {
            // Deactivate the current button
            selectedButton.getBackground().setColorFilter(null);

            if (selectedButton.equals(button)) {
                selectedButton = null;
                return;
            }
        }

        // Switch modes
        selectedButton = (Button)button;
        selectedButton.getBackground().setColorFilter(0xffd84098 /* AARRGGBB (pink) */, PorterDuff.Mode.DARKEN);

        /*

        if (newMode.getButton() == null) {
            Log.w(getLocalClassName(), "Button for mode " + newMode.getModeName() + " is not connected");
            return;
        }

        if (currentMode != null) {
            // Deactivate the current mode
            Log.d(this.getLocalClassName(), "Deactivating mode " + currentMode.getModeName());
            currentMode.getButton().getBackground().setColorFilter(null);

            if (currentMode.getModeName().equals(newMode.getModeName())) {
                currentMode = null;
                Log.d(this.getLocalClassName(), "We're done here");
                return;
            }
        }
        // Switch modes
        Log.d(this.getLocalClassName(),"Switching to mode " + newMode.getModeName());
        currentMode = newMode;
        newMode.getButton().getBackground().setColorFilter(0xffd84098 /* AARRGGBB (pink) *//*, PorterDuff.Mode.DARKEN);
        */

        // These lines cause error so they are commented out

        // Activate do not disturb mode
        //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);

        // Block the apps
        new blockAllApps().execute(this);
    }

    public void blockApp(String appID) {
        // Send broadcast message to the App Notification Service to start blocking an app
        Intent intent = new Intent("com.example.thesp.distractmenot.Broadcasts");
        intent.putExtra("block_app", appID);
        sendBroadcast(intent);
        Log.v("blockApp","Sending broadcast from activity to block " + appID);
    }

    // When a button is pressed, the mode gets set to that one
    public void onButtonPreset1(View view) { changeMode(modes.get(0), view); }
    public void onButtonPreset2(View view) { changeMode(modes.get(1), view); }

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

        /*modes.add(0, new Mode(
                "Preset1",
                new ArrayList<AppObject>(),
                getResources().getIdentifier("button_preset1", "id", getPackageName()),
                this));
        modes.add(1, new Mode("Preset2",
                new ArrayList<AppObject>(),
                getResources().getIdentifier("button_preset2", "id", getPackageName()),
                this));*/

        Mode mode1 = new Mode("Preset1", new ArrayList<AppObject>());
        mode1.setButton(getResources().getIdentifier("button_preset1", "id", getPackageName()), this);
        modes.add(0, mode1);
        Mode mode2 = new Mode("Preset2", new ArrayList<AppObject>());
        mode1.setButton(getResources().getIdentifier("button_preset2", "id", getPackageName()), this);
        modes.add(1, mode2);



        //Load in buttons
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> buttonList = prefs.getStringSet("List", null);

        LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutOne);

        if (buttonList != null) {
            for (String s : buttonList) {
                final Button newButton = new Button(this);
                System.out.println(s);
                newButton.setText(s);
                layout.removeView(newButton);
                final int id = View.generateViewId();
                newButton.setId(id);
                newButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                layout.addView(newButton);

                // Connect the function so these buttons can be pressed
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //changeMode(new Mode("Test", new ArrayList<AppObject>(), id, MainActivity.this));
                        Log.d("CustomButton", "ID: " + newButton.getId());
                        changeMode(modes.get(newButton.getId()), v);
                    }
                });
            }
        }
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

    private class blockAllApps extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            List<AppObject> allApps = AppObject.getAllApps(contexts[0]);
            for (AppObject app : allApps) {
                blockApp(app.getStringID());
            }
            return null;
        }
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
