package com.mirrorlabs.vince.amidst;

import android.content.ClipboardManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by vince on 12/1/15.
 */
public class ClipboardFileOperator {


    private final String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "Amidsts";
    private final String FILE_CLIPBOARD = "/cblogs.txt";

    private String mPreviousText = "";
    private final String TAG = "FILE_OPERATOR";
    private boolean fromClick = false;


    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Amidsts";

    File file = new File(PATH + File.separator + "cblogs.txt");
    File tempFile = new File(PATH + File.separator + "tempfile.txt");



    public void setFlagForRepeat(boolean repeat){
        fromClick = repeat;
    }

    public void createAndWriteClipJson (String clipboardString) {


        String currentTime = String.valueOf(System.currentTimeMillis());
        JSONObject jsobj = new JSONObject();

        //This needs replaced with handling of starred
        Boolean test = false;

        try {
            jsobj.put("Clip", clipboardString);
            jsobj.put("Time", currentTime);
            jsobj.put("Star", test);
            Log.d("json", "json");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (!fromClick) {
            writeToClipboardFile(jsobj);
            Log.v("ISSUE" , "from click is " + fromClick);
        }

        fromClick = false;
    }


    public void removeItemFromClipboardTest(int position , int adapterSize) {

        int lineToRemove = (adapterSize - position)-1;
        String lineToWrite;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            for ( int i = 0 ; i < adapterSize; i++ ) {

                lineToWrite = bufferedReader.readLine();

                if ( i != lineToRemove ){
                    bufferedWriter.write(lineToWrite);
                    bufferedWriter.newLine();
                }

                bufferedWriter.flush();
            }

            bufferedWriter.close();
            tempFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


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
