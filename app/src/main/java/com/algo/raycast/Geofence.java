package com.algo.raycast;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;



public class Geofence implements Parcelable{

    private String mId;
    private ArrayList<LatLng> mEdges;


    public Geofence(String pId, ArrayList<LatLng> pEdges){
        mId=pId;
        mEdges=pEdges;
    }

    protected Geofence(Parcel in) {
        mId = in.readString();
        mEdges = in.createTypedArrayList(LatLng.CREATOR);
    }

    @Override
    public String toString() {
        return "Geofence{" +
                "mId=" + mId +
                ", mEdges=" + mEdges +
                '}';
    }

    public ArrayList<LatLng> getEdges(){
        return mEdges;
    }

    public String getId (){
        return mId;
    }


    public static final Creator<Geofence> CREATOR = new Creator<Geofence>() {
        @Override
        public Geofence createFromParcel(Parcel in) {
            return new Geofence(in);
        }

        @Override
        public Geofence[] newArray(int size) {
            return new Geofence[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeTypedList(mEdges);
    }
}
