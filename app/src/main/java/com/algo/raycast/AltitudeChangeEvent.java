package com.algo.raycast;

/**
 * Created by os on 8/8/2016.
 */
public class AltitudeChangeEvent {

    public static final int LEVEL_UP=1;
    public static final int LEVEL_DOWN=2;
    public static final int LEVEL_SAME=0;

    private int mLevelTrsnsition;

    public AltitudeChangeEvent(int pLevelTransition){
        mLevelTrsnsition=pLevelTransition;
    }

    public int getLevelTrsnsition(){
        return this.mLevelTrsnsition;
    }
}
