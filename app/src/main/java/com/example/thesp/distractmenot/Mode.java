package com.example.thesp.distractmenot;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Tyler on 2/26/2018.
 */

public class Mode {

    // Pass in the context so this class can get view elements by ID
    public Mode(String name, List<AppObject> apps, int id, Activity context) {
        modeName = name;
        blockedApps = apps;
        button = context.findViewById(id);

    }

    private String modeName;
    private List<AppObject> blockedApps;
    private Button button;

    public String getModeName() {
        return modeName;
    }
    public List<AppObject> getBlockedApps() { return blockedApps; }
    public Button getButton() { return button; }
}
