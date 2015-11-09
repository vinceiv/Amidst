package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by vince on 11/8/15.
 */
public class ClipboardManipulater extends Service{


    private final String tag = "ClipboardListener";
    ClipboardContents contents = new ClipboardContents();


    public void onCreate() {

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
        Log.v("HELLLLLO", "ASDFA");

    }

    ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {

        @Override
        public void onPrimaryClipChanged() {
            clipBoardActivated();

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        onCreate();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void clipBoardActivated(){

        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        Log.d(tag, "CLBOARDING");
        ClipData clipData = cb.getPrimaryClip();
        contents.setClipData(clipData);


        String recentClip = clipData.getItemAt(0).toString();
        Log.d( tag , recentClip);

    }


}
