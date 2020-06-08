package com.example.rider_movelah.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rider_movelah.HomeActivity;
import com.example.rider_movelah.Neworderlistmainpage;
import com.example.rider_movelah.Preferences;
import com.example.rider_movelah.R;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.CartviewHolder> {
    Context mcontext;
    private ArrayList<Neworderlistmainpage> mCartList;
    private View.OnClickListener mOnItemClickListener;
    Preferences pref;


    public PendingAdapter(Context mcontext, ArrayList<Neworderlistmainpage> cartList) {

        this.mcontext = mcontext;
        this.mCartList = cartList;

    }

    @NonNull
    @Override
    public CartviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pending_item, viewGroup, false);
        return new PendingAdapter.CartviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartviewHolder holder, int i) {

        final Neworderlistmainpage currentitem = mCartList.get(i);

        // String Name =currentitem.getName();

        holder.tripid.setText(currentitem.getorederid());
        holder.date.setText(currentitem.getdate());
        holder.conformbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callwebservice(currentitem.getorederid());
            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callwebservicerider(currentitem.getorederid());
            }
        });


        //holder .ProductNames .setText( Name );

    }

    public void Callwebservicerider(final String orderid) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   progressDialog.show();
            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = mcontext.getResources().getString(R.string.webpath) + "get_riderdetails.php?tripid=" + orderid + "&activity=Cancel";

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
                Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();

                Intent in = new Intent(mcontext, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);
                ((Activity) mcontext).finish();

                //  Callwebservice(0, riderid, formattedDate.split(" ")[0], formattedDate.split(" ")[1]);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartviewHolder extends RecyclerView.ViewHolder {
        public TextView tripid, date;
        MaterialButton conformbutton, cancelButton;


        public CartviewHolder(@NonNull View itemView) {
            super(itemView);
            //ImageNames= itemView.findViewById( R.id.image_view_cart );

            tripid = itemView.findViewById(R.id.text_trip_id);
            date = itemView.findViewById(R.id.text_date);
            conformbutton = itemView.findViewById(R.id.assignRidersButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);


        }
    }

    public void Callwebservice(final String id) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pref = new Preferences(mcontext);

            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "add_teamtrip";
                try {
                    String finalurl_setstatus = "";
                    {
                        finalurl_setstatus = mcontext.getResources().getString(R.string.webpath) + "update_TripRider.php?" +
                                "riderid=" + pref.getPref_id() + "&tripid=" + id +
                                "&status= In Process" + "&randromriderid= 0";
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

                Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();

                Intent in = new Intent(mcontext, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);
                ((Activity) mcontext).finish();


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }
}
