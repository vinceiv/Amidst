package com.mirrorlabs.vince.amidst;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    protected List<String> clipboardList = new LinkedList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView lView;


    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Amidsts";

    File file = new File(PATH + File.separator + "cblogs.txt");


    protected List getClipboardList() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();


            while (line != null) {
                clipboardList.add(line);
                line = br.readLine();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return clipboardList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startService();
        setContentView(R.layout.activity_main);

        registerReceiver(mReceiver, new IntentFilter("clip"));





        /*     Check if file exists if not create the path
         *      Need to remove toast!
         */
        boolean exists = (new File(PATH)).exists();

        if (!exists) {
            new File(PATH).mkdirs();
            Toast.makeText(this, "Clipboard Path Made", Toast.LENGTH_LONG).show();
        }

        /*
        *       If the clipboardlog file does not exist create it
        *
        */
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*

        Read file line by line,
        add each line to link list.
            !!!! Moved to method temporarily
         */
        // try {
        //     BufferedReader br = new BufferedReader(new FileReader(file));
        //    String line = br.readLine();


        //   while (line != null) {
        //      clipboardList.add(line);
        //      line = br.readLine();

        //  }


        // }catch (IOException e){
        //     e.printStackTrace();


        // for (int i = clipboardList.size(); i >= 0 ;  --i){

        //   String temp = clipboardList.get(i);
        // reverseClipboard.add(temp);
        List<String> clipboardlists = new LinkedList<>(getClipboardList());

        Collections.reverse(clipboardlists);

        lView = (ListView)findViewById(R.id.clipboard);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  clipboardlists);
        lView.setAdapter(arrayAdapter);

    }


    public void startService() {
        startService(new Intent(getBaseContext(), ClipboardManipulater.class));
        startService(new Intent(getBaseContext(), RecentApps.class));
    }


    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();

            String clip = b.getString("clip");
            //clipboardList.add(0, clip);
            Log.v("BROADCAST RECEIVED: ", clip);
            lView.invalidate();
        }
    };


    @Override
    protected void onStop(){
        super.onStop();

        List<String> newcliplists = new LinkedList<>(getClipboardList());
        Collections.reverse(newcliplists);
        ArrayAdapter<String> temparrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  newcliplists);



        lView.setAdapter(temparrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
