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

    ClipboardManager cb;
    ClipboardContents contents = new ClipboardContents();


    private final String TAG = "CLIPBOARD_SERVICE";
    //private final String packageName = this.getPackageName();
    private final String path = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + File.separator + "Amidsts";

    private final String FILE_CLIPBOARD = "/cblogs.txt";


    //Upon creation set up listener so that callback will be invoked
    public void onCreate() {

       cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
       cb.addPrimaryClipChangedListener(listener);

        Log.v(TAG, "Listener Created");

    }

    //Override clipboardmanagers onPrimaryClipChanged()
    ClipboardManager.OnPrimaryClipChangedListener listener =
            new ClipboardManager.OnPrimaryClipChangedListener() {

        @Override
        public void onPrimaryClipChanged() {

            ClipData clipData = cb.getPrimaryClip();
            writeToClipboardFile(clipData);

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Clipboard Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        cb.removePrimaryClipChangedListener(listener);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void writeToClipboardFile(ClipData clipData ) {


        Log.d(TAG, "Clipboard_Activated");

        contents.setClipData(clipData);
        String recentClip = clipData.getItemAt(0).getText().toString() + "\n";

        //Print in log what was copied
        Log.d(TAG, recentClip);

        String state = Environment.getExternalStorageState();

        /*      Check to make sure sd is mounted.
        *       Need to handle if external is not mounted!!!!
        */

        String mPreviousText = "";


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

                if (!mPreviousText.equals(recentClip)){



                /*
                *       Write string to file
                */
                    FileOutputStream fOut = new FileOutputStream(path + FILE_CLIPBOARD, true);
                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    writer.append(recentClip);
                    writer.flush();
                    writer.close();

                    Log.d(TAG, path + FILE_CLIPBOARD);

                    Toast.makeText(this, recentClip + " was copied.", Toast.LENGTH_SHORT).show();

                    //handle the second call of primarycliplistener
                    mPreviousText = recentClip;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
