package com.example.thesp.distractmenot;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

        // Display the list of apps
        new displayApps().execute(this);
    }

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

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Setting", buttonName);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(NEW_BUTTON_NAME, buttonName);
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
                Log.i("App #" + i, applist.get(i).getName());

                LinearLayout layout = findViewById(R.id.appListLayout);

                LinearLayout newLayout = new LinearLayout(getApplicationContext());
                /*
                newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                */
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
                layout.addView(newLayout);

                // TESTCODE
                ((ImageView)findViewById(R.id.exampleImage)).setImageDrawable(applist.get(i).getLogo());
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
