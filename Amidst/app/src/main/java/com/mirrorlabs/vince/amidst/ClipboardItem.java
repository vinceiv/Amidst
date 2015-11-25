package com.mirrorlabs.vince.amidst;

/**
 * Created by vince on 11/24/15.
 */
public class ClipboardItem {


    private String title;
    private String timestamp;
    private boolean isStarred;

    public ClipboardItem(){
    }

    public ClipboardItem (String title, String timestamp, boolean isStarred){
        this.title = title;
        this.timestamp = timestamp;
        this.isStarred = isStarred;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public boolean getIsStarred(){
        return isStarred;
    }

    public void setIsStarred(boolean isStarred){
        this.isStarred = isStarred;
    }

}
