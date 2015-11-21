package com.mirrorlabs.vince.amidst;

import android.content.ClipboardManager;
import android.content.Intent;
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
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    protected List<String> clipboardList = new LinkedList <>();
    protected RecentApps rApps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
        setContentView(R.layout.activity_main);

        rApps = new RecentApps();


        clipboardList.add("Welcome to Amidst!");

        String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "Amidsts" ;

        File file = new File(PATH + File.separator + "cblogs.txt");

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

         */
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();


            while (line != null) {
                clipboardList.add(line);
                line = br.readLine();

            }

        }catch (IOException e){
            e.printStackTrace();
        }

        ListView lView = (ListView)findViewById(R.id.clipboard);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  clipboardList);
        lView.setAdapter(arrayAdapter);
    }

    public void startService() {
        startService(new Intent(getBaseContext(), ClipboardManipulater.class));
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
