package com.mirrorlabs.vince.amidst;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
    private List<ClipboardItem> clipboardItems = new LinkedList<>();
    private CustomListAdapter adapter;
    protected static ClipboardFileOperator clipboardFileOperator;
    final Context context = this;
    private EditText input = null;
    private ClipboardOperator clipboardOperator;
    String lastLine = "";

    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Amidsts";

     File file = new File(PATH + File.separator + "cblogs.txt");

    /*
    This needs moved to file operator
     */
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

                        Boolean starTrue = (Boolean) obj.get("Star");

                        String doctoredTitle = title.replaceAll("&holder" , "\n");

                        //create new clipboard item with parsed contextual info of clip
                        ClipboardItem parsedItem = new ClipboardItem(doctoredTitle, time, starTrue);
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

        //Intent serviceIntent = new Intent("com.mirrorlabs.vince.amidst.ClipboardListenerService" );
        //serviceIntent.putExtra("test" , false);
        //startService(serviceIntent);
        startService();

        setContentView(R.layout.activity_main);

        clipboardFileOperator = new ClipboardFileOperator();
        clipboardOperator = new ClipboardOperator(this);

        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.background));

        initApp();

        final List<ClipboardItem> clipboardItems = new LinkedList<>(getItems());

        adapter = new CustomListAdapter(this, clipboardItems);
        lView = (ListView) findViewById(R.id.clipboard);
        lView.setAdapter(adapter);

        if (context.getPackageName().equals("com.mirrorlabs.vince.amidst")) {


            lView.setOnItemClickListener(new ListView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ClipboardItem clipboardItem = (ClipboardItem) lView.getAdapter().getItem(position);
                    String clipItem = clipboardItem.getTitle();

                    ClipboardListenerService.fileOperator.setFlagForRepeat(true);
                    clipboardOperator.copyItemToClipboard(clipItem);

                    Toast.makeText(getBaseContext(), clipItem + "... has been copied.", Toast.LENGTH_SHORT).show();
                }
            });

            lView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ClipboardItem test = (ClipboardItem) lView.getAdapter().getItem(position);

                    int adapterSize = lView.getAdapter().getCount();
                    String clipItem = test.getTitle();
                    clipboardFileOperator.removeItemFromClipboardTest((position), adapterSize);

                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                    lView.setAdapter(adapter);
                    Toast.makeText(getBaseContext(), clipItem + " has been deleted.", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            final FloatingActionButton test = (FloatingActionButton) findViewById(R.id.addbutton);


            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setView(promptsView);

                    input = (EditText) promptsView.findViewById(R.id.userInput);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    String inputtedClip = input.getText().toString();
                                    clipboardFileOperator.createAndWriteClipJson(inputtedClip);

                                    populateAdapter();
                                    adapter.notifyDataSetChanged();
                                    lView.setAdapter(adapter);


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            });
        } // end of if to check if it is paid version or not


        if (context.getPackageName().equals("com.mirrorlabs.vince.amidst.paid")) {


            lView.setOnItemClickListener(new ListView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ClipboardItem clipboardItem = (ClipboardItem) lView.getAdapter().getItem(position);
                    String clipItem = clipboardItem.getTitle();

                    ClipboardListenerService.fileOperator.setFlagForRepeat(true);
                    clipboardOperator.copyItemToClipboard(clipItem);

                    Toast.makeText(getBaseContext(), clipItem + "... has been copied.", Toast.LENGTH_SHORT).show();
                }
            });


            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(lView,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {


                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }


                                @Override
                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        ClipboardItem test = (ClipboardItem) lView.getAdapter().getItem(position);

                                        int adapterSize = lView.getAdapter().getCount();
                                        String clipItem = test.getTitle();
                                        clipboardFileOperator.removeItemFromClipboardTest((position), adapterSize);

                                        adapter.remove(position);
                                        adapter.notifyDataSetChanged();
                                        lView.setAdapter(adapter);
                                        Toast.makeText(getBaseContext(), clipItem + " has been deleted.", Toast.LENGTH_SHORT).show();

                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });




            lView.setOnTouchListener(touchListener);
            lView.setOnScrollListener(touchListener.makeScrollListener());



            final FloatingActionButton test = (FloatingActionButton) findViewById(R.id.addbutton);


            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setView(promptsView);

                    input = (EditText) promptsView.findViewById(R.id.userInput);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    String inputtedClip = input.getText().toString();
                                    clipboardFileOperator.createAndWriteClipJson(inputtedClip);

                                    populateAdapter();
                                    adapter.notifyDataSetChanged();
                                    lView.setAdapter(adapter);


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            });

            }



    }


    public void populateAdapter(){
        adapter = new CustomListAdapter(this, getItems());

    }

    //ServiceConnection mConnection = new ServiceConnection() {
    //    public void onServiceDisconnected(ComponentName name) {
     //      // Toast.makeText(getBaseContext(), "Service is disconnected", Toast.LENGTH_SHORT).show();
     //   }

      //  public void onServiceConnected(ComponentName name, IBinder service) {
      //     // Toast.makeText(getBaseContext(), "Service is connected", Toast.LENGTH_SHORT).show();
      //      ClipboardListenerService.LocalBinder mLocalBinder = (ClipboardListenerService.LocalBinder)service;
       //     listenerService = mLocalBinder.getServerInstance();
      //  }
   // };



    /*
    Need to move this method to FILEOPERATOR class.
     */
    public void initApp(){
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

                ClipboardFileOperator fileOperator = new ClipboardFileOperator();

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
    }

    public void startService() {

        //UNDUE THESE TWO
        //Intent mIntent = new Intent(this, ClipboardListenerService.class);
       // bindService(mIntent, mConnection, BIND_AUTO_CREATE);


       startService(new Intent(getBaseContext(), ClipboardListenerService.class));


    }



    @Override
    protected void onResume(){
        super.onResume();
        lView.invalidate();
        CustomListAdapter difadapter = new CustomListAdapter(this, getItems());
        lView.setAdapter(difadapter);
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
