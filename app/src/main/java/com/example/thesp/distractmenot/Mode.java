package com.example.thesp.distractmenot;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;

import java.lang.ref.WeakReference;

/**
 * Created by Tyler on 2/26/2018.
 */

public class Mode {

    // Pass in the context so this class can get view elements by ID
    public Mode(String name, Activity context) {
        modeName = name;
        button = context.findViewById(context.getResources().getIdentifier(name, "id", context.getPackageName()));

    }

    private String modeName;
    private Button button;

    public String getModeName() {
        return modeName;
    }
    public Button getButton() { return button; }
}
