package com.mirrorlabs.vince.amidst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by vince on 12/17/15.
 */
public class BootReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context , Intent intent) {

        Toast.makeText(context, "Amidst Service has started" , Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, ClipboardListenerService.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
