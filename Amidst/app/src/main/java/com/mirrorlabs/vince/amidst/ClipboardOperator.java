package com.mirrorlabs.vince.amidst;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by vince on 12/17/15.
 */
public class ClipboardOperator{

    protected ClipboardManager cb;

    Context mContext;

    public ClipboardOperator(Context context) {
        this.mContext = context;
    }

    public void copyItemToClipboard(String copyThisString) {

        cb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        CharSequence item = copyThisString;

        ClipData newClip = ClipData.newPlainText("test", item);
        cb.setPrimaryClip(newClip);



    }


}
