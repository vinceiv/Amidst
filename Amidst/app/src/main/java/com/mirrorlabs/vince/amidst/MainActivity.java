package com.mirrorlabs.vince.amidst;

import android.content.BroadcastReceiver;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView lView;
    protected LinkedList<String> clipboardList = new LinkedList<>();
    ArrayAdapter<String> arrayAdapter;
    private List<ClipboardItem> clipboardItems = new LinkedList<>();
    private CustomListAdapter adapter;


    String lastLine = "";

    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Amidsts";

     File file = new File(PATH + File.separator + "cblogs.txt");


    protected List getItems() {

        clipboardItems.clear();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            Log.v("CLIPBOARDITEMS" , "BEFORE IF");

                while (line != null) {


                    //Throws JSON error
                    try {

                        JSONParser jsonParser = new JSONParser();
                        JSONObject obj = (JSONObject) jsonParser.parse(line);


                        String title = (String) obj.get("Clip");
                        String timestamp = (String) obj.get("Time");

                        Long time = Long.valueOf(timestamp);

                        Boolean test = (Boolean) obj.get("Star");
                        //Boolean test = Boolean.getBoolean(isStarred);


                        //create new clipboard item with parsed contextual info of clip
                        ClipboardItem parsedItem = new ClipboardItem(title, time, test);
                        //add item to clipboardList that will populate the custom adapter
                        clipboardItems.add(0, parsedItem);


                        //clipboardList.add(line);
                        lastLine = line;
                        line = br.readLine();


                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                }


                //close and flush buffered reader to push out last saved string
                br.close();




            //Collections.reverse(clipboardList);
            //clipboardList.add(0, lastLine);

        }catch(IOException e){
            e.printStackTrace();
        }

        return clipboardItems;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         //attempting to put logo next to app title in titlebar
         //getSupportActionBar().setDisplayUseLogoEnabled(true);
         //getSupportActionBar().setLogo(R.drawable.ic_launcher);
         //getSupportActionBar().setDisplayUseLogoEnabled(true);
         //actionBar.setDisplayHomeAsUpEnabled(true);
         //actionBar.setDisplayUseLogoEnabled(true);
         //actionBar.setLogo(R.mipmap.ic_launcher);

        startService();
        setContentView(R.layout.activity_main);

        //catch intents -- still needs work
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
        *       Leave first initial clipboard entry
        */
        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, "Clipboard File Made", Toast.LENGTH_SHORT).show();

                FileOperator fileOperator = new FileOperator();

                org.json.JSONObject jsobj = new org.json.JSONObject();
                try {
                    jsobj.put("Clip", "Thank you for using Amidsts, all recent clips will be shown here :)");
                    jsobj.put("Time", String.valueOf(System.currentTimeMillis()));
                    jsobj.put("Star", true);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                fileOperator.writeToClipboardFile(jsobj);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ClipboardItem> clipboardItems = new LinkedList<>(getItems());

        adapter = new CustomListAdapter(this, clipboardItems);



        lView = (ListView)findViewById(R.id.clipboard);



        //CustomListAdapter updatedadapter = new CustomListAdapter(this, getItems());
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String main = lView.getSelectedItem().toString();
                Log.v("Item was clicked on" , main);
            }


        });
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
    protected void onResume(){
        super.onResume();


        Toast.makeText(this, "ONRESUME CALLED", Toast.LENGTH_SHORT).show();

    }

    //set to false to remove options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
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
