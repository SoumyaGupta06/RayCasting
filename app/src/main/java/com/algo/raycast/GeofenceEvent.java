package com.algo.raycast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by os on 8/6/2016.
 */
public class GeofenceEvent {

    public static final int GEO_TRANSITION_ENTER=1;
    public static final int GEO_TRANSITION_EXIT=0;
    public static final int GEO_TRANSITION_DWELL=-1;

    private int mTransitionType;
    private String mGeofenceId;
    private ArrayList<LatLng> mEdge;

    public GeofenceEvent(String pGeofenceId,int pTransitionType, ArrayList<LatLng> pEdges){
        this.mGeofenceId = pGeofenceId;
        this.mTransitionType = pTransitionType;
        this.mEdge=pEdges;

    }
    public int getTransitionType(){
        return this.mTransitionType;
    }

    public String getGeofenceId(){
        return this.mGeofenceId;
    }

    public ArrayList<LatLng> getEdges(){
        return mEdge;
    }

}
