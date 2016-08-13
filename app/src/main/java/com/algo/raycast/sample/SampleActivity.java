package com.algo.raycast.sample;

import android.app.NotificationManager;
import android.content.IntentSender;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.algo.raycast.AltitudeChangeEvent;
import com.algo.raycast.AltitudeChangeEventListener;
import com.algo.raycast.Edge;
import com.algo.raycast.EdgeDistance;
import com.algo.raycast.Geofence;
import com.algo.raycast.GeofenceEvent;
import com.algo.raycast.GeofenceEventListener;
import com.algo.raycast.GeofenceState;
import com.algo.raycast.R;
import android.location.Location;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.algo.raycast.RaycastHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Created by Infernus on 05/07/16.
 */
public class SampleActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,GeofenceEventListener, AltitudeChangeEventListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAG = "location info ";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    EdgeDistance edgeDistance;
    int currentAlt=0;
    int prevAlt=0;
    EditText editText;

    ArrayList<LatLng> latLngs = new ArrayList<>();
    LatLng latLng1= new LatLng(28.525913, 77.574647);
    LatLng latLng2= new LatLng(28.525915, 77.574750);
    LatLng latLng3= new LatLng(28.525900, 77.574865);
    LatLng latLng4= new LatLng(28.525853, 77.574896);
    LatLng latLng5= new LatLng(28.525801, 77.574901);
    LatLng latLng6= new LatLng(28.525704, 77.574847);
    LatLng latLng7= new LatLng(28.525724, 77.574650);
    LatLng latLng8= new LatLng(28.525816, 77.574599);

    ArrayList<LatLng> latLngLib=  new ArrayList<>();
    LatLng latLng9= new LatLng(28.524834, 77.574691);
    LatLng latLng10= new LatLng(28.524858, 77.574796);
    LatLng latLng11= new LatLng(28.524790, 77.574842);
    LatLng latLng12= new LatLng(28.524710, 77.574850);
    LatLng latLng13= new LatLng(28.524665, 77.574791);
    LatLng latLng14= new LatLng(28.524663, 77.574657);
    LatLng latLng15= new LatLng(28.524781, 77.574603);

    ArrayList<Geofence> mGeofenceArrayList;
    ArrayList<GeofenceState> mGeofenceStateArrayList;
    GeofenceEventListener mGeofenceEventListener;
    AltitudeChangeEventListener mAltitudeChangeEventListener;
    ArrayList<EdgeDistance> mDistances;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_map_activity);

        mDistances= new ArrayList<EdgeDistance>();
        mGeofenceArrayList= new ArrayList<Geofence>();
        mGeofenceStateArrayList= new ArrayList<GeofenceState>();
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        mGeofenceArrayList.add( new Geofence("1",latLngs));


        latLngLib.add(latLng9);
        latLngLib.add(latLng10);
        latLngLib.add(latLng11);
        latLngLib.add(latLng12);
        latLngLib.add(latLng13);
        latLngLib.add(latLng14);
        latLngLib.add(latLng15);

        mGeofenceArrayList.add(new Geofence("2",latLngLib));


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)
                .setFastestInterval(2 * 1000);

        mGoogleApiClient.connect();
        this.registerGeofenceEventListener(this);
        this.registerAltitudeChangeEventListener(this);

    }

    public void registerGeofenceEventListener(GeofenceEventListener pGeofenceEventListener){
        this.mGeofenceEventListener = pGeofenceEventListener;
    }

    public void registerAltitudeChangeEventListener(AltitudeChangeEventListener pAltitudeChangeEventListener){
        mAltitudeChangeEventListener = pAltitudeChangeEventListener;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            if (location == null) {
                Log.i(TAG, "location is null");
            } else {
                onLocationChanged(location);
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat=location.getLatitude();
        Double lng= location.getLongitude();
        Log.i(TAG+"lat: ",lat.toString());
        Log.i(TAG+"lng: ",lng.toString());
        LatLng latLng= new LatLng(lat,lng);
        editText=(EditText)findViewById(R.id.editText);

        for(Geofence geofence:mGeofenceArrayList){
            int id = -1;
            for(GeofenceState geofenceState: mGeofenceStateArrayList) {
                if(geofence.getId()==geofenceState.getId()){
                    if(geofenceState.getState()){
                        id=1;
                    }
                }
                Log.d("RAY CAST", "************Id= " + geofenceState.getId() + " ,state= " + geofenceState.getState());
                Toast.makeText(SampleActivity.this, "id= "+geofenceState.getId()+", state= "+geofenceState.getState(), Toast.LENGTH_SHORT).show();

            }

            Log.d("RAY CAST", String.valueOf(id));
            boolean previousState = (id==-1)?GeofenceState.GEOFENCE_OUTSIDE:GeofenceState.GEOFENCE_INSIDE;//= true;//search for most recent recorded state of geofence with string geofence_id = geofence.getId;
            boolean currentState = RaycastHelper.isLatLngInside(geofence.getEdges(), latLng);




            if(previousState && currentState){

            }
            else
            if(previousState && !currentState){

                for(Edge e: RaycastHelper.getEdges(geofence.getEdges())){
                    mDistances.add(new EdgeDistance(distance(e.getStartX(),e.getStartY(),e.getEndX(),e.getEndY(),lat,lng),e));
                }
      //          mGeofenceStateArrayList.remove(new GeofenceState(geofence.getId(),GeofenceState.GEOFENCE_INSIDE));
                for(GeofenceState geofenceState: mGeofenceStateArrayList) {
                    if(geofence.getId()==geofenceState.getId()){
                        if(geofenceState.getState()){
                            mGeofenceStateArrayList.remove(geofenceState);
                            Log.i("RAY CAST ", "$$$$$ removed geofence state object $$$$$");
                        }
                    }
                }
                this.onGeofenceEvent(new GeofenceEvent(geofence.getId(),GeofenceEvent.GEO_TRANSITION_EXIT,closestEdge()));

            }
            else
            if(!previousState && currentState){

                for(Edge e: RaycastHelper.getEdges(geofence.getEdges())){
                    mDistances.add(new EdgeDistance(distance(e.getStartX(),e.getStartY(),e.getEndX(),e.getEndY(),lat,lng),e));
                }
                this.onGeofenceEvent(new GeofenceEvent(geofence.getId(),GeofenceEvent.GEO_TRANSITION_ENTER,closestEdge()));
                mGeofenceStateArrayList.add(new GeofenceState(geofence.getId(),GeofenceState.GEOFENCE_INSIDE));


            }
            else{

            }
            //mGeofenceStateArrayList.add(new GeofenceState(geofence.getId(),currentState));

            //compare previous and current states and call this.onGeofenceEvent with appropriate geofenceevent object


        }

        prevAlt= currentAlt ;
        try {
            currentAlt = Integer.parseInt(editText.getText().toString());
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        Log.d("RAY CAST : altitude ", String.valueOf(currentAlt));

        if(currentAlt>prevAlt){
            this.onAltitudeChangeEvent(new AltitudeChangeEvent(AltitudeChangeEvent.LEVEL_UP));
        }
        else if(currentAlt<prevAlt){
            this.onAltitudeChangeEvent(new AltitudeChangeEvent(AltitudeChangeEvent.LEVEL_DOWN));
        }
        else{
            this.onAltitudeChangeEvent(new AltitudeChangeEvent(AltitudeChangeEvent.LEVEL_SAME));
        }


    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onGeofenceEvent(GeofenceEvent pGeofenceEvent) {
        String notificationString = "";
        if(pGeofenceEvent.getTransitionType()==GeofenceEvent.GEO_TRANSITION_ENTER){
            notificationString = "ENTERED";
        }
        else
        if(pGeofenceEvent.getTransitionType()==GeofenceEvent.GEO_TRANSITION_EXIT){
            notificationString = "EXITED";
        }
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.Notification_Title))
                .setContentText(notificationString)
                .setSound(alarmSound);
        mNotifyMgr.notify(01, mBuilder.build());
    }


    @Override
    public void onAltitudeChangeEvent(AltitudeChangeEvent pAltitudeChangeEvent) {
        String notificationString="";
        if(pAltitudeChangeEvent.getLevelTrsnsition()==AltitudeChangeEvent.LEVEL_UP){
            notificationString= "LEVEL UP";
        }
        else if (pAltitudeChangeEvent.getLevelTrsnsition()==AltitudeChangeEvent.LEVEL_DOWN){
            notificationString= "LEVEL DOWN";
        }
        else if(pAltitudeChangeEvent.getLevelTrsnsition()==AltitudeChangeEvent.LEVEL_SAME){
            notificationString= "LEVEL SAME";
        }
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.Notification_Title))
                .setContentText(notificationString)
                .setSound(alarmSound);
        mNotifyMgr.notify(02, mBuilder.build());

    }

    public Double distance(double lat1,double lon1,double lat2,double lon2,double latp,double lonp){

        double y = sin(lonp - lon2) * cos(latp);
        double x = cos(lat2) * sin(latp) - sin(lat2) * cos(latp) * cos(latp - lat2);
        double bearing1 = toDegrees(atan2(y, x));
        bearing1 = 360 - (bearing1 + 360 % 360);

        double y2 = sin(lon1 - lon2) * cos(lat1);
        double x2 = cos(lat2) * sin(lat1) - sin(lat2) * cos(lat1) * cos(lat1 - lat2);
        double bearing2 = toDegrees(atan2(y2, x2));
        bearing2 = 360 - (bearing2 + 360 % 360);

        double lat2Rads = toRadians(lat2);
        double latpRads = toRadians(latp);
        double dLon = toRadians(lonp - lon2);

        double distanceAC = acos(sin(lat2Rads) * sin(latpRads)+cos(lat2Rads)*cos(latpRads)*cos(dLon)) * 6371;
        double distance = abs(asin(sin(distanceAC/6371)*sin(toRadians(bearing1)-toRadians(bearing2))) * 6371)*1000;

        Log.d("RAY CAST : dist ", String.valueOf(distance));
        return distance;

    }

    public ArrayList<LatLng> closestEdge(){

        ArrayList<LatLng> edge= new ArrayList<LatLng>();
        edgeDistance= mDistances.get(0);
        for(EdgeDistance d: mDistances){
            if(edgeDistance.getDist()>d.getDist()){
                edgeDistance=d;
            }
        }
        edge.add(new LatLng(edgeDistance.getEdge().getStartX(),edgeDistance.getEdge().getStartY()));
        edge.add(new LatLng(edgeDistance.getEdge().getEndX(),edgeDistance.getEdge().getEndY()));
        Log.d("RAY CAST", "Edge start:"+edge.get(0)+"Edge end"+edge.get(1));
        Toast.makeText(SampleActivity.this, "Edge start:"+edge.get(0)+"Edge end"+edge.get(1), Toast.LENGTH_SHORT).show();
        return edge;
    }
}
