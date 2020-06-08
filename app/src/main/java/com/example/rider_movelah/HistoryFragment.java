package com.example.rider_movelah;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.rider_movelah.adapters.HistoryAdapter;
import com.example.rider_movelah.models.Orderlistmainpage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    RecyclerView inProcessRecyclerView, deliverdRecyclerView;
    MaterialButton buttonPending, buttonDelivered;
    Preferences preferences;
    ArrayList<Orderlistmainpage> mCartList;
    ArrayList<Orderlistmainpage> mCartList1;
    private static final String TAG = "HistoryFragment";
    HistoryAdapter historyAdapter, historyAdapter1;
    LinearLayout emptyLayout, emptyLayoutDelivered;
    private ShimmerFrameLayout mShimmerViewContainer;
    ImageView refreshButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        inProcessRecyclerView = (RecyclerView) view.findViewById(R.id.inProcessRecyclerView);
        deliverdRecyclerView = (RecyclerView) view.findViewById(R.id.deliveredRecyclerView);
        emptyLayout = (LinearLayout) view.findViewById(R.id.emptyLayoutPending);
        emptyLayoutDelivered = (LinearLayout) view.findViewById(R.id.emptyLayoutDelivered);
        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmerLoading);
        refreshButton = (ImageView) view.findViewById(R.id.refersh_pending);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        emptyLayoutDelivered.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        inProcessRecyclerView.setHasFixedSize(true);
        inProcessRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        deliverdRecyclerView.setHasFixedSize(true);
        deliverdRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        deliverdRecyclerView.setVisibility(View.GONE);
        inProcessRecyclerView.setVisibility(View.VISIBLE);
        buttonDelivered = (MaterialButton) view.findViewById(R.id.button_delivered);
        buttonPending = (MaterialButton) view.findViewById(R.id.button_pending);
        //refreshButton=(ImageView)view.findViewById(R.id.refersh_pending);
        mCartList = new ArrayList<>();
        mCartList1 = new ArrayList<>();
        Callwebservice1("InProcess");
        buttonPending.setTextColor(getResources().getColor(android.R.color.white));
        buttonPending.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        buttonDelivered.setTextColor(getResources().getColor(android.R.color.black));
        buttonDelivered.setBackgroundColor(getResources().getColor(android.R.color.white));
        buttonDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonDelivered.setTextColor(getResources().getColor(android.R.color.white));
                buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                buttonPending.setTextColor(getResources().getColor(android.R.color.black));
                buttonPending.setBackgroundColor(getResources().getColor(android.R.color.white));
                deliverdRecyclerView.setVisibility(View.VISIBLE);
                inProcessRecyclerView.setVisibility(View.GONE);
                emptyLayoutDelivered.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                Callwebservice2("Delivered");


            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartList = new ArrayList<>();
                mCartList1 = new ArrayList<>();
                buttonPending.setTextColor(getResources().getColor(android.R.color.white));
                buttonPending.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonDelivered.setTextColor(getResources().getColor(android.R.color.black));
                buttonDelivered.setBackgroundColor(getResources().getColor(android.R.color.white));
                deliverdRecyclerView.setVisibility(View.GONE);
                inProcessRecyclerView.setVisibility(View.VISIBLE);
                emptyLayoutDelivered.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                Callwebservice1("InProcess");

            }
        });
        buttonPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonPending.setTextColor(getResources().getColor(android.R.color.white));
                buttonPending.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonDelivered.setTextColor(getResources().getColor(android.R.color.black));
                buttonDelivered.setBackgroundColor(getResources().getColor(android.R.color.white));
                deliverdRecyclerView.setVisibility(View.GONE);
                inProcessRecyclerView.setVisibility(View.VISIBLE);
                emptyLayoutDelivered.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                Callwebservice1("InProcess");
            }
        });


        return view;
    }

    public void Callwebservice1(final String status) {
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
                Log.d(TAG, "onPostExecute InProcess: " + resultValue);
                try {

                    try {

                        JSONObject jsonRootObject = new JSONObject(resultValue);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("service");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String Orderid = jsonObject.optString("tripid").toString();
                            String Pickupdate = jsonObject.optString("schedule_date").toString() + " & " + jsonObject.optString("schedule_time").toString();
                            String Status = jsonObject.optString("status").toString();
                            String PickupAddress = jsonObject.optString("p_address").toString();
                            String AllDropLocation = jsonObject.optString("d_address");
                            String TypeofVehicle = jsonObject.optString("p_unit").toString();
                            String Delivereddate = jsonObject.optString("created_date").toString();
                            String riderid = jsonObject.optString("riderid");
                            String p_latitude = jsonObject.optString("p_latitude").toString();
                            String p_longtitude = jsonObject.optString("p_longtitude").toString();
                            String d_longtitude = jsonObject.optString("d_longtitude").toString();
                            String d_latitude = jsonObject.optString("d_latitude").toString();
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
                            if (!riderid.equals("0") && !Status.equals("Delivered")) {
                                mCartList.add(new Orderlistmainpage(Orderid, Pickupdate, Status, AllDropLocation, PickupAddress, TypeofVehicle, Delivereddate, p_latitude, p_longtitude, d_longtitude, d_latitude, p_unit, p_floor, p_building, p_contactnumber, p_contactname, d_unit, d_floor, d_building, d_contactnumber, d_contactname));
                            }

                        }
                        mShimmerViewContainer.setVisibility(View.GONE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        if (mCartList.size() > 0) {
                            Log.d(TAG, "onPostExecute:Pending " + mCartList.size());
                            historyAdapter = new HistoryAdapter(getContext(), mCartList, status);
                            inProcessRecyclerView.setAdapter(historyAdapter);
                            emptyLayoutDelivered.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayoutDelivered.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
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

    public void Callwebservice2(final String status) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                preferences = new Preferences(getActivity());
                mCartList1 = new ArrayList<>();
            }

            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "get_alltrip.php?finalriderid=" + preferences.getPref_id();

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
                Log.d(TAG, "onPostExecute Delivered: " + resultValue);
                try {

                    try {

                        JSONObject jsonRootObject = new JSONObject(resultValue);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("service");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String Orderid = jsonObject.optString("tripid").toString();
                            String Pickupdate = jsonObject.optString("schedule_date").toString() + " & " + jsonObject.optString("schedule_time").toString();
                            String Status = jsonObject.optString("status").toString();
                            String PickupAddress = jsonObject.optString("p_address").toString();
                            String AllDropLocation = jsonObject.optString("d_address");
                            String TypeofVehicle = jsonObject.optString("p_unit").toString();
                            String Delivereddate = jsonObject.optString("created_date").toString();
                            String riderid = jsonObject.optString("finalriderid");
                            String p_latitude = jsonObject.optString("p_latitude").toString();
                            String p_longtitude = jsonObject.optString("p_longtitude").toString();
                            String d_longtitude = jsonObject.optString("d_longtitude").toString();
                            String d_latitude = jsonObject.optString("d_latitude").toString();
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
                            if (Status.equals("Delivered")) {
                                mCartList1.add(new Orderlistmainpage(Orderid, Pickupdate, Status, AllDropLocation, PickupAddress, TypeofVehicle, Delivereddate, p_latitude, p_longtitude, d_longtitude, d_latitude, p_unit, p_floor, p_building, p_contactnumber, p_contactname, d_unit, d_floor, d_building, d_contactnumber, d_contactname));

                            }


                        }
                        mShimmerViewContainer.setVisibility(View.GONE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        if (mCartList1.size() > 0) {
                            Log.d(TAG, "onPostExecute:Delivered " + mCartList1.size());
                            historyAdapter1 = new HistoryAdapter(getContext(), mCartList1, status);
                            deliverdRecyclerView.setAdapter(historyAdapter1);
                            emptyLayoutDelivered.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayoutDelivered.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
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
