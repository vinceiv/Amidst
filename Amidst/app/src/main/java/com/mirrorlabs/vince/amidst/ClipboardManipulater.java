package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by vince on 11/8/15.
 */
public class ClipboardManipulater extends Service {

    private final String TAG = "CLIPBOARD_SERVICE";
    private final String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "Amidsts";

    private final String FILE_CLIPBOARD = "/cblogs.txt";
    private String mPreviousText = "";



    protected ClipboardManager cb;
    List<ClipboardItem> clipItems = new ArrayList<ClipboardItem>();


    public void broadcastCustomIntent(String clipData) {

        Intent intent = new Intent("ClipboardUpdated");
        intent.putExtra("clip", clipData);
        sendBroadcast(intent);
        Log.v("BROADCASTED: ", clipData);

    }

    /*
    *    Upon creation set up listener so that callback will be invoked
    */
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

                    //need to setup handle for images and other forms of clipdata
                    ClipData clipData = cb.getPrimaryClip();
                    //broadcastCustomIntent(temp);
                    String clips = clipData.getItemAt(0).getText().toString();
                    Long tsLong = System.currentTimeMillis() / 1000;

                    JSONObject jsobj = new JSONObject();

                    try {
                        jsobj.put("Clip", clips);
                        jsobj.put("Time", tsLong);
                        jsobj.put("Star", false);
                        Log.d("json" , "json");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    writeToClipboardFile(jsobj);


                }

            };





    public void writeToClipboardFile(JSONObject jsonObject) {


        Log.d(TAG, "Clipboard_Activated");

                    //dont think i need if i remember correctly
                    // contents.setClipData(clipData);

                    //String recentClip = clipData.getItemAt(0).getText().toString() + "\n";

                    //Print in log what was copied
                    //Log.d(TAG, recentClip);


        /*      Check to make sure sd is mounted.
        *      Need to handle if external is not mounted!!!!
        */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {





            /*     Check if file exists if not create the path
             *      Need to remove toast!
             */
            boolean exists = (new File(path)).exists();

            if (!exists) {
                new File(path).mkdirs();
                // Toast.makeText(this, "Clipboard Path Made", Toast.LENGTH_LONG).show();
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
            Retrieve clip string using Clip key.
            If key is the same as previous string do not write to file
            Checks to not print twice.

            !!!Need to check if bug still exists
             */
            try {
                if (!mPreviousText.equals(jsonObject.get("Clip").toString())) {

                    String trueClip = jsonObject.toString();

                    /*
                    *       Write string to file
                    */
                    FileWriter fwriter = new FileWriter(path + FILE_CLIPBOARD, true);
                    BufferedWriter bW = new BufferedWriter(fwriter);
                    bW.write(trueClip);

                                Log.d(TAG, path + FILE_CLIPBOARD);

                                Toast.makeText(this, trueClip + " was written.", Toast.LENGTH_SHORT).show();

                                //handle the second call of primarycliplistener
                                mPreviousText = trueClip;
                    bW.newLine();
                    bW.close();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        // Toast.makeText(this, "Clipboard Service Started", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        cb.removePrimaryClipChangedListener(listener);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}