package com.vaygoo.vaygootaxi;

/**
 * Created by Wizard on 11.05.2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.vaygoo.vaygootaxi.fragment.MapFragment;

import java.util.Iterator;

import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

import static android.content.Context.LOCATION_SERVICE;
import static com.vaygoo.vaygootaxi.fragment.MapFragment.reloadInterface;


public class GpsProviderMap implements IMyLocationProvider, LocationListener {
    private final LocationManager mLocationManager;
    private Location mLocation;
    private IMyLocationConsumer mMyLocationConsumer;
    private long mLocationUpdateMinTime = 0L;
    private float mLocationUpdateMinDistance = 0.0F;
    private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    private Context contextA;

    public GpsProviderMap(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        contextA = context;
    }

    public long getLocationUpdateMinTime() {
        return this.mLocationUpdateMinTime;
    }

    public void setLocationUpdateMinTime(long milliSeconds) {
        this.mLocationUpdateMinTime = milliSeconds;
    }

    public float getLocationUpdateMinDistance() {
        return this.mLocationUpdateMinDistance;
    }

    public void setLocationUpdateMinDistance(float meters) {
        this.mLocationUpdateMinDistance = meters;
    }
    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
        this.mMyLocationConsumer = myLocationConsumer;
        boolean result = false;
        Iterator i$ = this.mLocationManager.getProviders(false).iterator();

        while (true) {
            String provider;
            do {
                if (!i$.hasNext()) {
                    return result;
                }

                provider = (String) i$.next();

            } while (!"gps".equals(provider) && !"network".equals(provider));

            result = true;
            if (ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.v("Excel"," Provider2 "+ provider);
                return  (ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED); //TODO
            }
            Log.v("Excel"," Provider3 "+ provider);
            this.mLocationManager.requestLocationUpdates(provider, this.mLocationUpdateMinTime, this.mLocationUpdateMinDistance, this);
           ///////////
            Log.v("Excel", "EEEE "+ provider + " " +  this.mLocationManager.getLastKnownLocation(provider) + " "
            + this.mLocation);
          //  this.mLocation = this.mLocationManager.getLastKnownLocation(provider); //излишне?

            ////////////
        }

    }

    public void reloadLocationForResume()
    {
        if (ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, this);
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 0,
                this);

      //  this.mLocation = mLocationManager.getLastKnownLocation();  //излишне?
    }

    public void stopLocationProvider() {
        this.mMyLocationConsumer = null;
        this.mLocationManager.removeUpdates(this);
    }
    @Override
    public Location getLastKnownLocation() {

        return this.mLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

        if(!this.mIgnorer.shouldIgnore(location.getProvider(), System.currentTimeMillis())) {
            this.mLocation = location;
            if(this.mMyLocationConsumer != null) {
                this.mMyLocationConsumer.onLocationChanged(this.mLocation, this);
                reloadInterface.reloadGpsPosition(location);

            }

        }
       // reloadInterface.reloadGpsPosition(location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.v("Excel","PPPP11ыыпыпв");
        if (ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextA, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ///////////////
       mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, this);
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 0,
                this);
        if(!this.mIgnorer.shouldIgnore(s, System.currentTimeMillis())) {
            this.mLocation = mLocationManager.getLastKnownLocation(s);
            if(this.mMyLocationConsumer != null) {
                this.mMyLocationConsumer.onLocationChanged(this.mLocation, this);
                reloadInterface.reloadGpsPosition(mLocationManager.getLastKnownLocation(s));
                Log.v("Excel","PPPP1111");
            }

        }
        Log.v("Excel","PPPP11ыыпыпв11111");
////////////////////?????????????????????????????
    }

    @Override
    public void onProviderDisabled(String s) {

    }

}