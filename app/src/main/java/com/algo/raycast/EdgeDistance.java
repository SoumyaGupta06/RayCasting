package com.algo.raycast;

import android.util.Log;

import static java.lang.Math.*;

/**
 * Created by os on 8/9/2016.
 */
public class EdgeDistance {
    private Double mDist;
    private Edge mEdge;


    public EdgeDistance(Double pDist, Edge pEdge){
        this.mDist=pDist;
        this.mEdge=pEdge;
    }

    public Edge getEdge(){
        return mEdge;
    }

    public Double getDist(){
        return mDist;
    }


}
