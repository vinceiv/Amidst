package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Created by vince on 11/8/15.
 */
public class ClipboardManipulater extends Service {


    private final String TAG = "CLIPBOARDLISTENER";
    private final File filePath = android.os.Environment.getExternalStorageDirectory();
    private final String file_ClipBoard = "cb.txt";




    public void onCreate() {

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
                .addPrimaryClipChangedListener(listener);

        Log.v(TAG, "CREATED");

    }

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
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        onCreate();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void clipBoardActivated() {

        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipboardContents contents = new ClipboardContents();

        Log.d(TAG, "CLIPBOARDING");

        ClipData clipData = cb.getPrimaryClip();
        contents.setClipData(clipData);
        String recentClip = clipData.getItemAt(0).getText().toString();

        Log.d(TAG, recentClip);

        //Create file object
        File file = new File(filePath.getAbsoluteFile() + file_ClipBoard);

            //if file exists try to wite to it
            if (file.exists()) {


                try {

                    FileOutputStream fOut = openFileOutput(file_ClipBoard, MODE_WORLD_READABLE);

                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    writer.append(recentClip);
                    writer.flush();

                    writer.close();
                }catch (IOException e) {
                    Log.e( TAG , "FILEEXISTSFAIL");
                }

            } else {


                    file = new File( filePath , file_ClipBoard);

                    try {
                        file.createNewFile();
                        FileOutputStream fOut = openFileOutput(file_ClipBoard, MODE_WORLD_READABLE);

                        OutputStreamWriter writer = new OutputStreamWriter(fOut);
                        writer.append(recentClip);
                        writer.flush();
                        writer.close();
                    }catch (IOException e) {
                        Log.e( TAG , filePath.toString());
                    }


            }

    }

}
