package com.example.rider_movelah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialButton materialButtonLogin;
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    String textemail, textpassword;
    private static final String TAG = "MainActivity";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        materialButtonLogin = (MaterialButton) findViewById(R.id.loginButton);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        materialButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayoutEmail.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter UserId...", Toast.LENGTH_LONG).show();
                    return;
                }

                if (textInputLayoutPassword.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Password....", Toast.LENGTH_LONG).show();
                    return;
                }

                Callwebservice();
            }
        });

    }

    public void Callwebservice() {
        class Callwebservice extends AsyncTask<Void, List<String>, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textemail = textInputLayoutEmail.getEditText().getText().toString();
                textpassword = textInputLayoutPassword.getEditText().getText().toString();
                progressDialog.show();
            }


            @Override
            protected String doInBackground(Void... params) {


                String resultValue = "";
                try {
                    String finalurl_setstatus = getResources().getString(R.string.webpath) + "userlogin.php?userid=" + textemail + "&password=" + textpassword;

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

                    Preferences pref = new Preferences(getApplicationContext());
                    try {

                        JSONObject jsonRootObject = new JSONObject(resultValue);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("service");
                        if (jsonArray.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Wrong Credentials!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String user_name = jsonObject.optString("name").toString();
                            String memberId = jsonObject.optString("id").toString();
                            String phone = jsonObject.optString("mobile").toString();
                            String passweord = jsonObject.optString("password").toString();
                            pref.setPref_username(user_name);
                            pref.setPref_id(memberId);
                            pref.setPref_phonenum(phone);
                            pref.setpref_password(passweord);
                            pref.setUserLoggedIn(true);
                            pref.setPref_emailid(jsonObject.optString("user_id"));
                            Toast.makeText(MainActivity.this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.show();


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
