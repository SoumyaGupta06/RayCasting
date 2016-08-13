package com.algo.raycast;

/**
 * Created by os on 8/6/2016.
 */
public class GeofenceState {

    public static final boolean GEOFENCE_OUTSIDE=false;
    public static final boolean GEOFENCE_INSIDE=true;

    private String mId;
    private boolean mState;

    public GeofenceState(String pId, boolean pState){
        mId=pId;
        mState=pState;
    }

    public String getId(){
        return mId;
    }
    public boolean getState(){
        return mState;
    }
    public void setId(String pId){
        mId=pId;
    }
    public void setState(boolean pState){
        mState = pState;
    }
}
