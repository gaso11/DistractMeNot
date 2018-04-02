package com.example.thesp.distractmenot;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 3/2/2018.
 */

public class AppObject {

    private Drawable logo;
    private String name;
    private String stringID;
    public Drawable getLogo() { return logo; }
    public String getName() { return name; }
    public String getStringID() { return stringID; }

    // The cached list of all apps
    private static List<AppObject> appList;

    public static List<AppObject> getAllApps(Context context) {
      //  if (appList == null) {

            List<AppObject> list = new ArrayList<AppObject>();

            final PackageManager pm = context.getPackageManager();
            List<PackageInfo> pkgList = pm.getInstalledPackages(0);
            for (int i=0; i < pkgList.size(); i++)
            {
                PackageInfo pkgInfo = pkgList.get(i);
                if (  (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                {
                    AppObject app = new AppObject();
                    app.name = pkgInfo.applicationInfo.loadLabel(pm).toString();
                    // Don't block our own app
                    if (app.name.equals("Distract Me Not")) { continue; }
                    //app.logo = pkgInfo.applicationInfo.loadLogo(pm);
                    app.logo = pm.getApplicationIcon(pkgInfo.applicationInfo);
                    app.stringID = pkgInfo.applicationInfo.name;
                    list.add(list.size(), app);

                    String appName = pkgInfo.applicationInfo.loadLabel(pm).toString();
                    //Log.e("App # " + Integer.toString(i), appName);
                } else {
                    String appName = pkgInfo.applicationInfo.loadLabel(pm).toString();
                    //Log.i("App # " + Integer.toString(i), appName);
                }
            }

            appList = list;
      //  }

        return appList;
    }
}
