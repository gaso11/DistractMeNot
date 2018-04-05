package com.example.thesp.distractmenot;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static com.example.thesp.distractmenot.StringConstants.*;

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

        // Display the list of apps
        new displayApps().execute(this);
    }

    /**
     * This function is called when the button to apply changes is pressed
     * @param view
     */
    public void onNewMode(View view) {
        EditText settingName = findViewById(R.id.settingName);
        String buttonName = settingName.getText().toString();

        if (buttonName.equals("")) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_no_name_error);
            dialog.setTitle("Dialog Box");

            Button button = (Button) dialog.findViewById(R.id.Button02);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } else {

            //Save Settings
            Toast.makeText(getApplicationContext(), "Saving Settings", Toast.LENGTH_SHORT).show();

            //Load in set
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Set<String> buttonList = prefs.getStringSet("List", null);
            if (buttonList == null) {
                buttonList = new HashSet<>();
            }

            //Add name to list
            buttonList.add(buttonName);

            //Save list
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet("List", buttonList);
            editor.commit();

            /*
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Setting", buttonName);
            editor.commit();
            */

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(NEW_BUTTON_NAME, buttonName);

            // Add all the apps that need to be blocked
            LinearLayout layout = findViewById(R.id.appListLayout);
            for (int i = 0; i < layout.getChildCount(); i++) {
                LinearLayout container = (LinearLayout)layout.getChildAt(i);

                String appname = "";
                boolean blocked = false;

                for (int j = 0; j < container.getChildCount(); j++) {
                    View child = container.getChildAt(j);

                    // Get the name of the app
                    if (child instanceof TextView && !(child instanceof ToggleButton))
                        //intent.putExtra(NEW_BUTTON_APPS + i, ((TextView) container.getChildAt(j)).getText());
                        appname = (String)((TextView) child).getText();

                    // Get whether this button has been pressed
                    if (child instanceof ToggleButton)
                        if (((ToggleButton) child).isChecked())
                            blocked = true;
                }

                // Put in the setting, if necessary
                assert(!appname.equals(""));
                if (blocked) {
                    Log.d("BLOCKING APP:", appname);
                    intent.putExtra(NEW_BUTTON_APPS + i, appname);
                }
            }
            startActivity(intent);
        }
    }

    // Gets the list of apps (which can take a while) in the background
    private class displayApps extends AsyncTask<Context, Void, List<AppObject> > {
        @Override
        protected List<AppObject> doInBackground(Context... contexts) {
            return AppObject.getAllApps(contexts[0]);
        }

        @Override
        protected void onPostExecute(List<AppObject> applist) {
            super.onPostExecute(applist);

            // Temporary loop to display the apps.
            // Eventually these should be put in the ScrollView
            for (int i = 0; i < applist.size(); i++) {
                Log.v("App #" + i, applist.get(i).getName());

                LinearLayout newLayout = new LinearLayout(getApplicationContext());
                newLayout.setLayoutParams((findViewById(R.id.exampleLayout)).getLayoutParams());

                // 1: The logo
                ImageView newImage = new ImageView(getApplicationContext());
                newImage.setLayoutParams((findViewById(R.id.exampleImage)).getLayoutParams());
                newImage.setImageDrawable(applist.get(i).getLogo());
                newLayout.addView(newImage);

                // 2: The textview
                TextView newText = new TextView(getApplicationContext());
                newText.setLayoutParams((findViewById(R.id.exampleText)).getLayoutParams());
                newText.setText(applist.get(i).getName());
                newLayout.addView(newText);

                // 3: The toggle button
                ToggleButton newToggle = new ToggleButton(getApplicationContext());
                newToggle.setLayoutParams((findViewById(R.id.exampleButton)).getLayoutParams());
                newToggle.setTextOff(getString(R.string.enable));
                newToggle.setTextOn(getString(R.string.blocked));
                newToggle.setText(getString(R.string.enable));

                //newToggle.setLayoutParams(layout.getLayoutParams());
                newLayout.addView(newToggle);

                // Add the new entry to the list
                newLayout.setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.appListLayout)).addView(newLayout);
            }
        }
    }

    /**
     * This opens a clock for the user to choose a time that our app will be active until.
     * @param view
     * @author Emily T
     */
    public void showTimePickerDialog(View view) {

    }
}
