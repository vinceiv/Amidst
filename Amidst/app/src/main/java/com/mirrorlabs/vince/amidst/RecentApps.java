package com.mirrorlabs.vince.amidst;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import java.util.List;

/**
 * Created by vince on 11/12/15.
 */
public class RecentApps extends Service {

  //
  private List<ActivityManager.RunningAppProcessInfo> process;
  private ActivityManager activityMan;



    public void onCreate(){
        activityMan = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        process = activityMan.getRunningAppProcesses();

        for (int i = 0 ; i < process.size() ; i++ ){
            Log.v("banana: " , process.toString()  );
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Started recent apps", Toast.LENGTH_SHORT).show();
        onCreate();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
