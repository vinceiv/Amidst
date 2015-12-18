package com.mirrorlabs.vince.amidst;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by vince on 11/8/15.
 */
public class ClipboardListenerService extends Service {

    private final String TAG = "CLIPBOARD_SERVICE";
    private ClipboardFileOperator fileOperator;
    protected ClipboardManager cb;
    private boolean startedFromBoot = true;

    @Override
    public IBinder onBind(Intent intent) {
       return null;
   }



    /*
    *    Upon creation set up listener so that callback will be invoked
    */
    @Override
    public void onCreate() {


        super.onCreate();
        fileOperator = new ClipboardFileOperator();
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

                    String description = clipData.getDescription().getMimeType(0);

                    if (description.equals("text/plain") || description.equals("text/html")
                            ) {


                         try {

                             String clips = clipData.getItemAt(0).getText().toString();
                             String doctoredString = clips.replaceAll("\n", "&holder");



                             ClipboardFileOperator fileop = new ClipboardFileOperator();


                             fileop.createAndWriteClipJson(doctoredString);
                             //MainActivity.clipboardFileOperator.createAndWriteClipJson(doctoredString);


                         } catch (NullPointerException e) {
                             e.printStackTrace();
                         }
                    }


                }
            };

    private void setStartedFromBoot (boolean startedFromBoot) {
        this.startedFromBoot = startedFromBoot;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         Toast.makeText(this, "Clipboard Service Started", Toast.LENGTH_SHORT).show();

        if (intent == null) {
          setStartedFromBoot(false);
            //startService(new Intent(getBaseContext(), ClipboardListenerService.class));

            return START_STICKY;

        }

        else {
            Boolean setBootable = intent.getBooleanExtra("test", true);
            setStartedFromBoot(setBootable);
           // startService(new Intent(getBaseContext(), ClipboardListenerService.class));
            return START_STICKY;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        cb.removePrimaryClipChangedListener(listener);
    }


}