package com.example.rider_movelah;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

public class Preferences {
    SharedPreferences sharedPref;
    Editor edit;
    String islogin = "islogin";
    String firsttime = "firsttime";
    String pref_username = "pref_username";
    String pref_emailid = "pref_emailid";
    String pref_phonenum = "pref_phonenum";
    String pref_password = "Pref_password";
    String pref_id = "Pref_id";
    String pref_profileimage = "pre_profileimage";
    String pref_cartcount = "pre_cartcount";
    String pref_addid = "pre_add_id";
    String pref_AddName = "Pref_add_name";
    String pref_AddAddress = "Pref_add_address";
    String pref_AddNumber = "pref_add_number";
    static ArrayList<String> dataModels2 = new ArrayList<>();
    static ArrayList<String> dataModels = new ArrayList<>();
    static String picklocationaddress = "";
    static String droplocationaddress = "";
    static String pick_lalitute = "";
    static String pick_longitute = "";
    static String drop_lalitute = "";
    static String drop_longitute = "";
    static String d_contactname = "", d_contactnumber = "", d_unit = "", d_floor = "", d_buliding = "";
    static String p_contactname = "", p_contactnumber = "", p_unit = "", p_floor = "", p_buliding = "";
    static String orderid;
    static String middlemanid;
    static String distance = "";
    static String vechile_type = "1";
    static String vechile_name = "";

    public Preferences(Context con) {
        sharedPref = con.getSharedPreferences("Move_Lah_MiddleMan", Context.MODE_PRIVATE);
        ;
        edit = sharedPref.edit();
    }

    public void setPref_profileimage(String pm) {
        edit.putString(pref_profileimage, pm);
        edit.commit();
    }

    public String getPref_profileimage() {
        return sharedPref.getString(pref_profileimage, null);
    }


    public void setPref_username(String un) {
        edit.putString(pref_username, un);
        edit.commit();
    }

    public String getPref_username() {
        return sharedPref.getString(pref_username, null);
    }

    public void setPref_emailid(String email) {
        edit.putString(pref_emailid, email);
        edit.commit();
    }

    public String getPref_emailid() {
        return sharedPref.getString(pref_emailid, null);
    }

    public void setPref_phonenum(String phoneno) {
        edit.putString(pref_phonenum, phoneno);
        edit.commit();
    }

    public String getPref_id() {
        return sharedPref.getString(pref_id, null);
    }

    public void setPref_id(String pv) {
        edit.putString(pref_id, pv);
        edit.commit();
    }

    public String getPref_AddId() {
        return sharedPref.getString(pref_addid, "");
    }

    public void setPref_AddId(String ai) {
        edit.putString(pref_addid, ai);
        edit.commit();
    }

    public String getPref_AddName() {
        return sharedPref.getString(pref_AddName, null);
    }

    public void setPref_AddName(String an) {
        edit.putString(pref_AddName, an);
        edit.commit();
    }

    public String getPref_AddNumber() {
        return sharedPref.getString(pref_AddNumber, null);
    }

    public void setPref_AddNumber(String ad) {
        edit.putString(pref_AddNumber, ad);
        edit.commit();
    }

    public String getPref_AddAddress() {
        return sharedPref.getString(pref_AddAddress, null);
    }

    public void setPref_AddAddress(String aa) {
        edit.putString(pref_AddAddress, aa);
        edit.commit();
    }

    public int getPref_cartcount() {
        return sharedPref.getInt(pref_cartcount, 0);
    }

    public void setPref_cartcount(int cc) {
        edit.putInt(pref_cartcount, cc);
        edit.commit();
    }

    public String getPref_phonenum() {
        return sharedPref.getString(pref_phonenum, null);
    }


    public void setpref_password(String password) {
        edit.putString(pref_password, password);
        edit.commit();
    }

    public String getpref_password() {
        return sharedPref.getString(pref_password, null);
    }


    public void setUserLoggedIn(boolean bol) {
        edit.putBoolean(islogin, bol);
        edit.commit();
    }

    public void setRiderStatus(String bol) {
        edit.putString("Rider_Status", bol);
        edit.commit();
    }

    public String getStatus() {
        return sharedPref.getString("Rider_Status", "Yes");
    }


    public boolean isUserLoggedIn() {
        return sharedPref.getBoolean(islogin, false);
    }

    public boolean isfirstuser() {
        return sharedPref.getBoolean(firsttime, false);
    }

    public void setisfirstuser(boolean bol) {
        edit.putBoolean(islogin, bol);
        edit.commit();
    }


}
