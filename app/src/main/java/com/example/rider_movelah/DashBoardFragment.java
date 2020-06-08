package com.example.rider_movelah;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rider_movelah.models.Orderlistmainpage;
import com.example.rider_movelah.models.Rides;
import com.google.android.material.switchmaterial.SwitchMaterial;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment implements OnItemClickListener {

    public DashBoardFragment() {
        // Required empty public constructor
    }

    Context mcontext;
    ArrayList<Rides> riderArrayList = new ArrayList<>();
    TextView textViewRiderCount;
    Preferences preferences;
    ArrayList<Orderlistmainpage> mCartList;
    private static final String TAG = "DashBoardFragment";
    int total_order_count = 0;
    int total_pending_count = 0;
    int total_inprocess_count = 0;
    int total_deliverd_count = 0;
    TextView textViewPendingCount, textViewInProcessCount, textViewTotalCount;
    LinearLayout noorder;
    ImageView refreshButton;
    SwitchMaterial switchMaterial;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
    public static final int notify = 1 * 1000 * 60 * 60;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);


        textViewRiderCount = (TextView) view.findViewById(R.id.totalRiderCount);
        textViewPendingCount = (TextView) view.findViewById(R.id.pendingOrderCount);
        textViewInProcessCount = (TextView) view.findViewById(R.id.inprocessOrderCount);
        textViewTotalCount = (TextView) view.findViewById(R.id.totalOrderCount);
        noorder = (LinearLayout) view.findViewById(R.id.mapL);
        refreshButton = (ImageView) view.findViewById(R.id.refreshButton);
        switchMaterial = (SwitchMaterial) view.findViewById(R.id.statusSwitch);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                getActivity().finish();
            }
        });
        preferences = new Preferences(getContext());
        if (preferences.getStatus().equals("Yes")) {
            switchMaterial.setChecked(true);
        } else {
            switchMaterial.setChecked(false);
        }
        Callwebservice1();
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.setRiderStatus("Yes");
                } else {
                    preferences.setRiderStatus("No");
                }
                if (mTimer != null) { // Cancel if already existed
                    mTimer.cancel();
                } else {
                    mTimer = new Timer();   //recreate new
                    mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
                }

            }
        });

        return view;
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    Intent in = new Intent(getContext(), HomeActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    getActivity().finish();
                    //Toast.makeText(NotificationServices.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    @Override
    public void OnItemClick(int position) {

    }

    public void Callwebservice1() {
        total_pending_count = 0;
        total_deliverd_count = 0;
        total_order_count = 0;
        total_inprocess_count = 0;
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                preferences = new Preferences(getActivity());
                mCartList = new ArrayList<>();
            }

            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "get_alltrip.php?riderid=" + preferences.getPref_id();

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

                try {

                    try {
                        if (!result.equals("{ \"service\" :}")) {


                            JSONObject jsonRootObject = new JSONObject(resultValue);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("service");
                            if (jsonArray != null) {
                                total_order_count = jsonArray.length();
                                textViewTotalCount.setText("" + total_order_count);
                            } else {
                                textViewTotalCount.setText("0");

                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String Orderid = jsonObject.optString("tripid").toString();
                                String Pickupdate = jsonObject.optString("schedule_date").toString() + " & " + jsonObject.optString("schedule_time").toString();
                                String Status = jsonObject.optString("status").toString();
                                String PickupAddress = jsonObject.optString("p_address").toString();
                                String AllDropLocation = jsonObject.optString("d_address");
                                String TypeofVehicle = jsonObject.optString("p_unit").toString();
                                String Delivereddate = jsonObject.optString("created_date").toString();
                                String p_latitude = jsonObject.optString("p_latitude").toString();
                                String p_longtitude = jsonObject.optString("p_longtitude").toString();
                                String d_longtitude = jsonObject.optString("d_longtitude").toString();
                                String d_latitude = jsonObject.optString("d_latitude").toString();
                                String riderid = jsonObject.optString("riderid");
                                String p_floor = jsonObject.optString("p_floor").toString();
                                String p_unit = jsonObject.optString("p_unit").toString();
                                String p_building = jsonObject.optString("p_building").toString();
                                String p_contactnumber = jsonObject.optString("p_contactnumber").toString();
                                String p_contactname = jsonObject.optString("p_contactname").toString();
                                String d_unit = jsonObject.optString("d_unit").toString();
                                String d_floor = jsonObject.optString("d_floor").toString();
                                String d_building = jsonObject.optString("d_building").toString();
                                String d_contactnumber = jsonObject.optString("d_contactnumber").toString();
                                String d_contactname = jsonObject.optString("d_contactname").toString();

                                if (riderid.equals("0")) {
                                    total_pending_count = total_pending_count + 1;
                                    mCartList.add(new Orderlistmainpage(Orderid, Pickupdate, Status, AllDropLocation, PickupAddress, TypeofVehicle, Delivereddate, p_latitude, p_longtitude, d_longtitude, d_latitude,p_unit,p_floor,p_building,p_contactnumber,p_contactname,d_unit,d_floor,d_building,d_contactnumber,d_contactname));
                                } else if (!riderid.equals("0") && !Status.equals("Delivered")) {
                                    total_inprocess_count = total_inprocess_count + 1;
                                } else if (Status.equals("Delivered")) {
                                    total_deliverd_count = total_deliverd_count + 1;

                                }


                            }
                            textViewRiderCount.setText("" + total_deliverd_count);
                            textViewInProcessCount.setText("" + total_inprocess_count);
                            textViewPendingCount.setText("" + total_pending_count);

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


}
