package com.example.thesp.distractmenot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
