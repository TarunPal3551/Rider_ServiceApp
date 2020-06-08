package com.example.rider_movelah.adapters;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rider_movelah.HomeActivity;
import com.example.rider_movelah.Preferences;
import com.example.rider_movelah.R;
import com.example.rider_movelah.models.Orderlistmainpage;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context mcontext;

    private ArrayList<Orderlistmainpage> mCartList;
    private static final String TAG = "HistoryAdapter";
    String status;
    ProgressDialog progressDialog;


    public HistoryAdapter(Context mcontext, ArrayList<Orderlistmainpage> mCartList, String status) {
        this.mcontext = mcontext;
        this.mCartList = mCartList;
        this.status = status;
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Processing...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tripid, date, pickaddress, dropaddress, status, viewAllDetails;
        public RelativeLayout layoutq;
        ImageView navigationImageView;
        MaterialButton delieverdButton, cancelButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripid = itemView.findViewById(R.id.text_trip_id);
            date = itemView.findViewById(R.id.text_date);
            pickaddress = itemView.findViewById(R.id.pick_text);
            dropaddress = itemView.findViewById(R.id.drop_text);
            delieverdButton = itemView.findViewById(R.id.assignRidersButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            navigationImageView = (ImageView) itemView.findViewById(R.id.navigationimage);
            viewAllDetails = itemView.findViewById(R.id.viewAllDetails);


        }


    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Glide.with(mcontext)
//                .asBitmap()
//                .load(images.get(position))
//                .into(holder.imgCardView);

        //  holder.textView.setText(names.get(position));
//        holder.textViewMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mcontext, OrderDetailsActivity.class);
//                mcontext.startActivity(intent);
//            }
//
//        });
        final Orderlistmainpage currentitem = mCartList.get(position);

        // String Name =currentitem.getName();

        holder.tripid.setText(currentitem.getorederid());
        holder.date.setText(currentitem.getdate());
        holder.pickaddress.setText(currentitem.getPickupAddress());
        holder.dropaddress.setText(currentitem.getdroplocation());
        if (status.equals("InProcess")) {
            holder.delieverdButton.setVisibility(View.VISIBLE);
            holder.navigationImageView.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.viewAllDetails.setVisibility(View.VISIBLE);
        } else {
            holder.delieverdButton.setVisibility(View.GONE);
            holder.navigationImageView.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
            holder.viewAllDetails.setVisibility(View.GONE);
        }
        holder.navigationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences preferences = new Preferences(mcontext);
                String riderId = preferences.getPref_id();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + currentitem.getP_latitude() + "," + currentitem.getP_longtitude() + "&daddr=" + currentitem.getD_latitude() + "," + currentitem.getD_longtitude() + "&mode=d"));
                mcontext.startActivity(intent);
                //  Callwebservice(mCartList.get(position).getorederid(),riderId);
            }
        });

        holder.delieverdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.status_layout);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                    }
                });
                MaterialButton buttonSave = (MaterialButton) dialog.findViewById(R.id.assignRidersButton);
                MaterialButton buttonCancel = (MaterialButton) dialog.findViewById(R.id.navigationButton);
                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radioGroup.getCheckedRadioButtonId() == R.id.orderDeliverRadio) {
                            dialog.dismiss();
                            CallwebserviceDelivered(mCartList.get(position).getorederid());
                        } else {
                            dialog.dismiss();
                            Callwebservice(mCartList.get(position).getorederid(), "Picked Up");
                        }
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


                //Callwebservice(mCartList.get(position).getorederid(), "Delivered");

            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want Cancel this Order?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Callwebservicerider(currentitem.getorederid());
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
        holder.viewAllDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.dialog_alldetails);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textViewPUnit = (TextView) dialog.findViewById(R.id.textViewPUnit);
                TextView textViewDUnit = (TextView) dialog.findViewById(R.id.textViewDUnit);
                TextView textViewPFloor = (TextView) dialog.findViewById(R.id.textViewPFloor);
                TextView textViewDFloor = (TextView) dialog.findViewById(R.id.textViewDFloor);
                TextView textViewPBuilding = (TextView) dialog.findViewById(R.id.textViewPBuilding);
                TextView textViewDBuilding = (TextView) dialog.findViewById(R.id.textViewDBuilding);
                TextView textViewPContactName = (TextView) dialog.findViewById(R.id.textViewPContactName);
                TextView textViewDContactName = (TextView) dialog.findViewById(R.id.textViewDContactName);
                TextView textViewPContactNumber = (TextView) dialog.findViewById(R.id.textViewPContactNumber);
                TextView textViewDContactNumber = (TextView) dialog.findViewById(R.id.textViewDContactNumber);
                ImageView imageViewCall = (ImageView) dialog.findViewById(R.id.callImageView);
                ImageView imageViewCall1 = (ImageView) dialog.findViewById(R.id.callImageView2);
                imageViewCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                imageViewCall1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+currentitem.getD_contactnumber()));
                        mcontext.startActivity(intent);
                    }
                });
                imageViewCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+currentitem.getP_contactnumber()));
                        mcontext.startActivity(intent);
                    }
                });

                textViewPUnit.setText("" + currentitem.getP_unit());
                textViewPFloor.setText("" + currentitem.getP_floor());
                textViewPBuilding.setText("" + currentitem.getP_building());
                textViewPContactName.setText("" + currentitem.getP_contactname());
                textViewPContactNumber.setText("" + currentitem.getP_contactnumber());
                textViewDUnit.setText("" + currentitem.getD_unit());
                textViewDFloor.setText("" + currentitem.getD_floor());
                textViewDBuilding.setText("" + currentitem.getD_building());
                textViewDContactName.setText("" + currentitem.getD_contactname());
                textViewDContactNumber.setText("" + currentitem.getD_contactnumber());


                MaterialButton materialButton = (MaterialButton) dialog.findViewById(R.id.cancelButton);
                materialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });


    }

    public void Callwebservicerider(final String orderid) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();

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
                String resultValue = result;
                progressDialog.dismiss();
                //Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();
                CallwebserviceCancel(orderid);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    public void Callwebservice(final String id, final String status) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();


            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "add_teamtrip";
                try {
                    String finalurl_setstatus = "";
                    {
                        finalurl_setstatus = mcontext.getResources().getString(R.string.webpath) + "update_TripRider.php?" +
                                "riderid=" + new Preferences(mcontext).getPref_id() + "&tripid=" + id +
                                "&status=" + status + "&randromriderid= 0";
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
                progressDialog.dismiss();
                Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();
                Intent in = new Intent(mcontext, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    public void CallwebserviceDelivered(final String id) {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();


            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "add_teamtrip";
                try {
                    String finalurl_setstatus = "";
                    {
                        finalurl_setstatus = mcontext.getResources().getString(R.string.webpath) + "update_Delivered.php?" +
                                "riderid=0" + "&tripid=" + id +
                                "&status=Delivered" + "&finalriderid=" + new Preferences(mcontext).getPref_id();
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
                progressDialog.dismiss();
                Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();
                Intent in = new Intent(mcontext, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }

    public void CallwebserviceCancel(final String id) {

        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();


            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "add_teamtrip";
                try {
                    String finalurl_setstatus = "";
                    {
                        finalurl_setstatus = mcontext.getResources().getString(R.string.webpath) + "update_AssignRider.php?" +
                                "riderid=0" + "&tripid=" + id +
                                "&status=Search Rider";

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
                Log.d(TAG, "onPostExecute: Cancel after Confirm " + result);

                progressDialog.dismiss();

//                Toast.makeText(mcontext, resultValue, Toast.LENGTH_LONG).show();
                Intent in = new Intent(mcontext, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);


            }


        }

        Callwebservice uv = new Callwebservice();
        uv.execute();
    }


    @Override
    public int getItemCount() {
        return mCartList.size();
    }


}
