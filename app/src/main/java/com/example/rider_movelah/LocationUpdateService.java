package com.example.rider_movelah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

public class LocationUpdateService extends BroadcastReceiver {
    private static final String TAG = "LocationUpdateService";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (TAG.equals(action)){
                LocationResult result=LocationResult.extractResult(intent);
                if (result!=null){
                    Location location=result.getLastLocation();

                }
            }
        }
    }
}
