package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Created by vince on 11/8/15.
 */
public class ClipboardManipulater extends Service {


    private final String TAG = "CLIPBOARD_Service";
    //private final String packageName = this.getPackageName();
    private final String path = android.os.Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + "/Amidst";

    private final String FILE_CLIPBOARD = "/cblog.txt";




    //Upon creation set up listener so that callback will be invoked
    public void onCreate() {

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
                .addPrimaryClipChangedListener(listener);

        Log.v(TAG, "Listener created");

    }

    //Override clipboardmanagers onPrimaryClipChanged()
    ClipboardManager.OnPrimaryClipChangedListener listener =
            new ClipboardManager.OnPrimaryClipChangedListener() {

        @Override
        public void onPrimaryClipChanged() {
            clipBoardActivated();

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Clipboard Service Started", Toast.LENGTH_LONG).show();
        onCreate();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void clipBoardActivated() {

        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipboardContents contents = new ClipboardContents();

        Log.d(TAG, "Activated");

        //this shit dont work
        ClipData clipData = cb.getPrimaryClip();
        contents.setClipData(clipData);
        String recentClip = clipData.getItemAt(0).getText().toString() + "\n";

        //Print in log what was copied
        Log.d(TAG, recentClip);

        String state = Environment.getExternalStorageState();

        /*      Check to make sure sd is mounted.
        *       Need to handle if external is not mounted!!!!
        */

        if (Environment.MEDIA_MOUNTED.equals(state)) {


            try {

                 /*     Check if file exists if not create the path
                 *      Need to remove toast!
                 */
                 boolean exists = (new File(path)).exists();
                 if (!exists) {
                     new File(path).mkdirs();
                     Toast.makeText(this, "Clipboard Path Made", Toast.LENGTH_LONG).show();
                 }

                /*
                *       If the clipboardlog file does not exist create it
                *
                */
                 File file = new File(path + FILE_CLIPBOARD);
                 if (!file.exists()) {
                     try {
                     file.createNewFile();
                     } catch (IOException e) {
                        e.printStackTrace();
                     }
                 }


                /*
                *       Write string to file
                */
                FileOutputStream fOut = new FileOutputStream(path + FILE_CLIPBOARD, true);
                OutputStreamWriter writer = new OutputStreamWriter(fOut);
                writer.append(recentClip);
                writer.flush();
                writer.close();

                Log.d(TAG, path + FILE_CLIPBOARD);

                Toast.makeText(this, recentClip + " was written?", Toast.LENGTH_LONG).show();

        } catch (IOException e){
                e.printStackTrace();
        }

        }
    }

}
