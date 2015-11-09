package com.mirrorlabs.vince.amidst;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vince on 11/8/15.
 */
public class ClipboardContents {

    //Declare ArrayList type ClipData
    private List<ClipData> cbData = new ArrayList<ClipData>();


    //Append Clipdata to the end of the list.
    protected void setClipData(ClipData data){

        cbData.add(data);

    }


    protected List<ClipData> getClipboardData(){

        return cbData;

    }
}
