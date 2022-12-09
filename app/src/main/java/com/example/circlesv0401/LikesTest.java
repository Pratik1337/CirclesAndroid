package com.example.circlesv0401;

import android.os.Parcel;
import android.os.Parcelable;

public class LikesTest  {

    String likeID;

    public LikesTest() {
    }

    public LikesTest(String likeID) {
        this.likeID = likeID;
    }

    public String getLikeID() {
        return likeID;
    }

    public void setLikeID(String likeID) {
        this.likeID = likeID;
    }


}



