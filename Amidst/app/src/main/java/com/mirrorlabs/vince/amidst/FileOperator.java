package com.mirrorlabs.vince.amidst;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by vince on 12/1/15.
 */
public class FileOperator {


    private final String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "Amidsts";
    private final String FILE_CLIPBOARD = "/cblogs.txt";

    private String mPreviousText = "";
    private final String TAG = "FILE_OPERATOR";





    public void writeToClipboardFile(JSONObject jsonObject) {


        Log.d(TAG, "Wrote to clipboard file");


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

                    // Toast.makeText(this, trueClip + " was written.", Toast.LENGTH_SHORT).show();

                    //handle the second call of primarycliplistener
                    mPreviousText = trueClip;


                    bW.newLine();
                    bW.close();
                }
            } catch (IOException | JSONException e ) {
                e.printStackTrace();
            }

        }
    }
}
