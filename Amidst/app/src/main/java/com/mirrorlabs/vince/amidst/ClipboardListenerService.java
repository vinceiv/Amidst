package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by vince on 11/8/15.
 */
public class ClipboardListenerService extends Service {

    private final String TAG = "CLIPBOARD_SERVICE";
    private ClipboardFileOperator fileOperator = new ClipboardFileOperator();
    protected ClipboardManager cb;
    private boolean flagForRepeate = false;

    IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class LocalBinder extends Binder {
        public ClipboardListenerService getServerInstance() {
            return ClipboardListenerService.this;
        }
    }

    public void setFlagForRepeate (boolean flag){
        flagForRepeate = flag;
    }

    public void copyItemToClipboard(String copyThisString){


        CharSequence item = copyThisString;

        ClipData newClip = ClipData.newPlainText("test" , item);
        cb.setPrimaryClip(newClip);

    }


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
                    String currentTime = String.valueOf(System.currentTimeMillis());


                    JSONObject jsobj = new JSONObject();


                    Boolean test = false;
                    try {

                        jsobj.put("Clip", clips);
                        jsobj.put("Time", currentTime);
                        jsobj.put("Star", test);
                        Log.d("json", "json");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (!flagForRepeate) {
                        fileOperator.writeToClipboardFile(jsobj);

                    }

                }

            };


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






}