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


    private final String TAG = "CLIPBOARD";

    private final String DIR_CLIPBOARD = android.os.Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + "/Clipboard";
    private final String FILE_CLIPBOARD = "cblog.txt";



    //On cr``
    public void onCreate() {

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
                .addPrimaryClipChangedListener(listener);

        Log.v(TAG, "Listener created");

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

        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipboardContents contents = new ClipboardContents();

        Log.d(TAG, "Activated");

        //this shit dont work
        ClipData clipData = cb.getPrimaryClip();
        contents.setClipData(clipData);
        String recentClip = clipData.getItemAt(0).getText().toString();

        //Print in log what was copied
        Log.d(TAG, recentClip);

        //Check that clipboard dir exists
        File mediaDir = new File(  );
        if (!mediaDir.exists()){
            mediaDir.mkdirs();
            Toast.makeText(this, "Clipboard File Made", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Clipboard File Exists", Toast.LENGTH_SHORT).show();
        }

        //Create file object
        File clipLog = new File( + FILE_CLIPBOARD);

        Log.v(TAG, );

            //if file exists try to write to it
            if (clipLog.exists()) {


                try {

                    FileOutputStream fOut = openFileOutput( + FILE_CLIPBOARD
                            , MODE_WORLD_READABLE);

                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    writer.append(recentClip);
                    writer.flush();
                    writer.close();

                    Toast.makeText(this, "try file exists", Toast.LENGTH_LONG).show();

                }catch (IOException e) {
                    Log.e( TAG , "FILEEXISTSFAIL");
                }

            } else {




                    try {
                        clipLog.createNewFile();
                        FileOutputStream fOut = openFileOutput(FILE_CLIPBOARD, MODE_WORLD_READABLE);

                        OutputStreamWriter writer = new OutputStreamWriter(fOut);
                        writer.append(recentClip);
                        writer.flush();
                        writer.close();

                        Toast.makeText(this, "else file created", Toast.LENGTH_LONG).show();

                    }catch (IOException e) {
                        Log.e( TAG , .toString());
                        Toast.makeText(this, "DIDNT EXIST FAILED" , Toast.LENGTH_LONG).show();

                    }


            }

    }

}
