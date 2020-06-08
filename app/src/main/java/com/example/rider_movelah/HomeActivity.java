package com.example.rider_movelah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Fragment fragment1 = new DashBoardFragment();
    final Fragment fragment2 = new HistoryFragment();
    final Fragment fragment3 = new AccountFragment();
    Fragment active = fragment1;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private boolean mLocationPermissionGranted = false;

    Geocoder geocoder;
    List<Address> addresses = new ArrayList<>();
    private static final String TAG = "Home_Activity";
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    private static final String CHANNEL_ID = "MoveLah_Rider";
    String rider_Id = "";
    Notification notification;
    public static final int notify = 1000 * 30 * 60;
    public static final int notifyLocation = 1000 * 1 * 60;
    public static final int notifCancel = 1 * 60 * 1000;
    //interval between two services(Here Service run every 5 seconds)
    int count = 0;  //number of times service is display
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null, lTimer = null;
    ArrayList<Neworderlistmainpage> lcartlist;
    Preferences preferences;
    Handler handler = new Handler();
    MediaPlayer r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        fm.beginTransaction().add(R.id.fragment_containerLayout, fragment3, "3").hide(fragment3).commit();
//        fm.beginTransaction().add(R.id.fragment_containerLayout, fragment2, "2").hide(fragment2).commit();
//        fm.beginTransaction().add(R.id.fragment_containerLayout, fragment1, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        LoadFragment(active);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        //   r = MediaPlayer.create(HomeActivity.this, R.raw.sound_notification);
        createNotificationChannel();
        preferences = new Preferences(this);
        lcartlist = new ArrayList<>();
        if (mTimer != null) { // Cancel if already existed
            mTimer.cancel();
//            mTimer = new Timer();   //recreate new
//            mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        } else {
            mTimer = new Timer();   //recreate new
            mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        }
        if (lTimer != null) { // Cancel if already existed
            lTimer.cancel();
            lTimer = new Timer();   //recreate new
            lTimer.scheduleAtFixedRate(new TimeLocation(), 0, notifyLocation);
        } else {
            lTimer = new Timer();   //recreate new
            lTimer.scheduleAtFixedRate(new TimeLocation(), 0, notifyLocation);
        }


    }


    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    if (preferences.isUserLoggedIn()) {
                        checkforNewOrders();
                    }


                    //Toast.makeText(NotificationServices.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    class TimeLocation extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    if (preferences.isUserLoggedIn()) {
                        getLastLocation();
                    }

                    //Toast.makeText(NotificationServices.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public void checkforNewOrders() {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lcartlist = new ArrayList<>();
            }

            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "get_alltrip.php?randromriderid=" + new Preferences(HomeActivity.this).getPref_id();
                    URL url_setstatus = new URL(finalurl_setstatus.replace(" ", "%20"));
                    URLConnection connectionurl_setstatus = url_setstatus.openConnection();
                    connectionurl_setstatus.setDoOutput(true);
                    connectionurl_setstatus.connect();


                    BufferedReader inpu = new BufferedReader(new InputStreamReader(connectionurl_setstatus.getInputStream()));
                    String inputL = "";


                    while ((inputL = inpu.readLine()) != null) {
                        resultValue = inputL;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return resultValue;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String resultValue = result;

                resultValue = "{ \"service\" :" + resultValue + "}";
                Log.d(TAG, "onPostExecute() returned: " + resultValue);
                try {


                    try {
                        JSONObject jsonRootObject = new JSONObject(resultValue);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("service");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int count = 0;
                            lcartlist.clear();
                            if (jsonObject.optString("randromriderid") != null&&!(jsonObject.optString("status").equals("Cancelled"))) {
                                if (jsonObject.optString("randromriderid").toString().equals(new Preferences(HomeActivity.this).getPref_id())) {
                                    count = count + 1;

                                    String Orderid = jsonObject.optString("tripid").toString();
                                    String Pickupdate = jsonObject.optString("schedule_date").toString() + " & " + jsonObject.optString("schedule_time").toString();
                                    String Status = jsonObject.optString("status").toString();
                                    String PickupAddress = jsonObject.optString("p_address").toString();
                                    String AllDropLocation = jsonObject.optString("d_address");
                                    String TypeofVehicle = jsonObject.optString("p_unit").toString();
                                    String Delivereddate = jsonObject.optString("created_date").toString();
                                    lcartlist.add(new Neworderlistmainpage(Orderid, Pickupdate, Status, AllDropLocation, PickupAddress, TypeofVehicle, Delivereddate));

                                }


                            }
                        }
                        if (lcartlist.size() > 0) {
//                            if (r != null) {
//                                r.start();
//                            } else {
//                                r = MediaPlayer.create(HomeActivity.this, R.raw.sound_notification);
//                                r.start();
//                            }
                            mTimer.cancel();
                            final NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sound_notification);
                            Intent notificationIntent = null;
                            if (preferences.isUserLoggedIn()) {
                                notificationIntent = new Intent(HomeActivity.this, HomeActivity.class);
                            } else {
                                notificationIntent = new Intent(HomeActivity.this, MainActivity.class);

                            }
                            PendingIntent pendingIntent = PendingIntent.getActivity(HomeActivity.this,
                                    0, notificationIntent, 0);
                            notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setContentTitle("MoveLah Rider")
                                    .setContentText("Order Received, Click to View")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentIntent(pendingIntent)
                                    .setDefaults(Notification.DEFAULT_LIGHTS)
                                    .setSound(sound)
                                    .setAutoCancel(true)
                                    .build();
                            myNotificationManager.notify(0, notification);

                            final Dialog dialog = new Dialog(HomeActivity.this);
                            dialog.setContentView(R.layout.pending_item);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView tripid = dialog.findViewById(R.id.text_trip_id);
                            TextView date = dialog.findViewById(R.id.text_date);
                            TextView pickupText = dialog.findViewById(R.id.pick_text);
                            TextView deliveredText = dialog.findViewById(R.id.drop_text);
                            pickupText.setText("" + lcartlist.get(0).getPickupAddress());
                            deliveredText.setText("" + lcartlist.get(0).getdroplocation());
                            final TextView timer = dialog.findViewById(R.id.timerTextView);
                            tripid.setText("" + lcartlist.get(0).getorederid());
                            date.setText("" + lcartlist.get(0).getdate());
                            Button cancelBTN = dialog.findViewById(R.id.cancelButton);
                            Button confirmBTN = dialog.findViewById(R.id.assignRidersButton);
                            final CountDownTimer countDownTimer = new CountDownTimer(notifCancel, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    long millis = millisUntilFinished;
                                    timer.setText(String.format("%02d Min %02d Sec", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                                    //here you can have your logic to set text to edittext
                                }

                                public void onFinish() {
                                    timer.setText("Ride Canceled");
                                    dialog.dismiss();
                                    myNotificationManager.cancelAll();
                                    if (lcartlist.size() > 0) {
                                        Callwebservicerider(lcartlist.get(0).getorederid());
                                    } else {
                                        checkforNewOrders();
                                    }

                                }

                            };
                            countDownTimer.start();
                            cancelBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myNotificationManager.cancelAll();
                                    dialog.dismiss();
                                    countDownTimer.cancel();
                                    if (lcartlist.size() > 0) {
                                        Callwebservicerider(lcartlist.get(0).getorederid());

                                    } else {
                                        checkforNewOrders();
                                    }

                                }
                            });
                            confirmBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myNotificationManager.cancelAll();
                                    dialog.dismiss();
                                    countDownTimer.cancel();

                                    if (lcartlist.size() > 0) {
                                        Callwebservice(lcartlist.get(0).getorederid());
                                    } else {
                                        checkforNewOrders();
                                    }
                                }
                            });
                            dialog.show();


                        } else {
                            mTimer.cancel();
                            mTimer = new Timer();   //recreate new
                            mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (r != null || r.isPlaying()) {
//            r.stop();
//        }
        //startService(new Intent(this, NotificationServices.class));
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (mLocationPermissionGranted) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    //  Toast.makeText(HomeActivity.this, "" + location.getLongitude() + "," + location.getLatitude(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: " + location);
                                    mLastLocation = location;
                                    CallwebserviceUpdate(preferences.getStatus());
                                    //   getAddressUsingLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                                }
                            }
                        }
                );
            } else {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
                SettingsClient settingsClient = LocationServices.getSettingsClient(HomeActivity.this);
                Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
                task.addOnSuccessListener(HomeActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        if (mLocationPermissionGranted) {

                            requestNewLocationData();

                        }
                    }
                });
                task.addOnFailureListener(HomeActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(HomeActivity.this, 40);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
            }
        } else {
            getLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //stopService(new Intent(this, NotificationServices.class));
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocations = locationResult.getLastLocation();
            Log.d(TAG, "onLocationResult: " + mLastLocations);

            if (mLastLocations != null) {
                // Toast.makeText(HomeActivity.this, "" + mLastLocations.getLongitude() + "," + mLastLocations.getLatitude(), Toast.LENGTH_SHORT).show();
                mLastLocation = mLastLocations;
                CallwebserviceUpdate(preferences.getStatus());
                // getAddressUsingLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            }
        }
    };

    private void getLocationPermission() {

        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                getLastLocation();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    getLastLocation();
                    //initialize our map

                }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.history:
                    // fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    break;

                case R.id.account:
                    //  fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    break;
                case R.id.dashboard:
                    //  fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    break;

            }
            return LoadFragment(active);
        }
    };

    private boolean LoadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_containerLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public void Callwebservice(final String id) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "add_teamtrip";
                try {
                    String finalurl_setstatus = "";
                    {
                        finalurl_setstatus = getResources().getString(R.string.webpath) + "update_TripRider.php?" +
                                "riderid=" + new Preferences(HomeActivity.this).getPref_id() + "&tripid=" + id +
                                "&status=Got Rider" + "&randromriderid= 0";
                    }
                    URL url_setstatus = new URL(finalurl_setstatus.replace(" ", "%20"));
                    Log.d(TAG, "doInBackground: " + finalurl_setstatus);
                    URLConnection connectionurl_setstatus = url_setstatus.openConnection();
                    connectionurl_setstatus.setDoOutput(true);
                    connectionurl_setstatus.connect();


                    BufferedReader inpu = new BufferedReader(new InputStreamReader(connectionurl_setstatus.getInputStream()));
                    String inputL = "";


                    while ((inputL = inpu.readLine()) != null) {
                        resultValue = inputL;

                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

                return resultValue;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String resultValue = result;
                lcartlist = new ArrayList<>();
                Log.d(TAG, "onPostExecute: Confirm " + resultValue);
                //  Toast.makeText(HomeActivity.this, resultValue, Toast.LENGTH_LONG).show();


                mTimer.cancel();
                mTimer = new Timer();   //recreate new
                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    public void Callwebservicerider(final String orderid) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   progressDialog.show();
                lcartlist = new ArrayList<>();
            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "get_riderdetails.php?tripid=" + orderid + "&activity=Cancel";

                    URL url_setstatus = new URL(finalurl_setstatus.replace(" ", "%20"));
                    URLConnection connectionurl_setstatus = url_setstatus.openConnection();
                    connectionurl_setstatus.setDoOutput(true);
                    connectionurl_setstatus.connect();


                    BufferedReader inpu = new BufferedReader(new InputStreamReader(connectionurl_setstatus.getInputStream()));
                    String inputL = "";


                    while ((inputL = inpu.readLine()) != null) {
                        resultValue = inputL;

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

                return resultValue;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String resultValue = result.replace("<pre>", "");
                String riderid = resultValue;
                lcartlist = new ArrayList<>();
                Log.d(TAG, "onPostExecute: Cancel " + resultValue);
                //  Toast.makeText(HomeActivity.this, resultValue, Toast.LENGTH_LONG).show();
//                Intent in = new Intent(HomeActivity.this, HomeActivity.class);
//                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(in);
//                finish();

                //  Callwebservice(0, riderid, formattedDate.split(" ")[0], formattedDate.split(" ")[1]);

                mTimer.cancel();
                mTimer = new Timer();   //recreate new
                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (r != null || r.isPlaying()) {
//            r.stop();
//        }
        // stopService(new Intent(this, NotificationServices.class));


    }

    public void CallwebserviceUpdate(final String riderStatus) {


        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String rider_status = preferences.getStatus();
//                textemail = URLEncoder.encode(email.getText().toString());
//                textpassword = URLEncoder.encode(password.getText().toString());
            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "update_rider.php?logitude=" + mLastLocation.getLongitude() + "&latitude=" + mLastLocation.getLatitude() + "&id=" + new Preferences(HomeActivity.this).getPref_id() + "&Status=" + riderStatus;

                    URL url_setstatus = new URL(finalurl_setstatus.replace(" ", "%20"));
                    URLConnection connectionurl_setstatus = url_setstatus.openConnection();
                    connectionurl_setstatus.setDoOutput(true);
                    connectionurl_setstatus.connect();


                    BufferedReader inpu = new BufferedReader(new InputStreamReader(connectionurl_setstatus.getInputStream()));
                    String inputL = "";

                    while ((inputL = inpu.readLine()) != null) {
                        resultValue = inputL;

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

                return resultValue;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String resultValue = result;
                resultValue = "{ \"service\" :" + resultValue + "}";
                Log.d(TAG, "onPostExecute: " + resultValue);

            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.sound_notification);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setDescription("MoveLah_Rider");
            serviceChannel.enableLights(true);
            serviceChannel.enableVibration(true);
            serviceChannel.setSound(sound, attributes);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }

    }


}
