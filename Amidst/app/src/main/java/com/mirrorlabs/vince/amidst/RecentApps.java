package com.mirrorlabs.vince.amidst;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.app.Service;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by vince on 11/12/15.
 */
public class RecentApps extends Activity {

  //
  private List<ActivityManager.RunningAppProcessInfo> process;
  private ActivityManager activityMan;



    public void onCreate(){
        activityMan = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        process = activityMan.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processes : process) {
            Log.v("ActivityManager: " , processes.toString()  );
        }
    }


}
