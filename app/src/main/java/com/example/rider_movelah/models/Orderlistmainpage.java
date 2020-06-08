package com.example.rider_movelah.models;

public class Orderlistmainpage {

    private String Orderid;
    private String date ;
    private String Status;
    private String droplocation;
    private String PickupAddress;
    private String type;
    private String delivereddate;
    private String p_latitude;
    private String p_longtitude;
    private String d_longtitude;
    private String d_latitude;
    private String p_unit;
    private String p_floor;
    private String p_building;
    private String p_contactnumber;
    private String p_contactname;
    private String d_unit;
    private String d_floor;
    private String d_building;
    private String d_contactnumber;
    private String d_contactname;

    public String getP_unit() {
        return p_unit;
    }

    public void setP_unit(String p_unit) {
        this.p_unit = p_unit;
    }

    public String getP_floor() {
        return p_floor;
    }

    public void setP_floor(String p_floor) {
        this.p_floor = p_floor;
    }

    public String getP_building() {
        return p_building;
    }

    public void setP_building(String p_building) {
        this.p_building = p_building;
    }

    public String getP_contactnumber() {
        return p_contactnumber;
    }

    public void setP_contactnumber(String p_contactnumber) {
        this.p_contactnumber = p_contactnumber;
    }

    public String getP_contactname() {
        return p_contactname;
    }

    public void setP_contactname(String p_contactname) {
        this.p_contactname = p_contactname;
    }

    public String getD_unit() {
        return d_unit;
    }

    public void setD_unit(String d_unit) {
        this.d_unit = d_unit;
    }

    public String getD_floor() {
        return d_floor;
    }

    public void setD_floor(String d_floor) {
        this.d_floor = d_floor;
    }

    public String getD_building() {
        return d_building;
    }

    public void setD_building(String d_building) {
        this.d_building = d_building;
    }

    public String getD_contactnumber() {
        return d_contactnumber;
    }

    public void setD_contactnumber(String d_contactnumber) {
        this.d_contactnumber = d_contactnumber;
    }

    public String getD_contactname() {
        return d_contactname;
    }

    public void setD_contactname(String d_contactname) {
        this.d_contactname = d_contactname;
    }

    public void setOrderids(String orderids) {
        Orderid = orderids;
    }

    public void setdate(String dates) {
        date = dates;
    }

    public void setstatus(String statuss) {
        Status = statuss;
    }

    public void setdroplocation(String droplocations) {
        droplocation = droplocations;
    }

    public void settype(String types) {
        type = types;
    }

    public void setdelivereddate(String delivereddates) {
        this.delivereddate = delivereddates;
    }

    public Orderlistmainpage(String orderid, String date, String status, String droplocation, String pickupAddress, String type, String delivereddate, String p_latitude, String p_longtitude, String d_longtitude, String d_latitude,
                             String p_unit, String p_floor, String p_building, String p_contactnumber, String p_contactname, String d_unit, String d_floor, String d_building, String d_contactnumber, String d_contactname) {
        Orderid = orderid;
        this.date = date;
        Status = status;
        this.droplocation = droplocation;
        PickupAddress = pickupAddress;
        this.type = type;
        this.delivereddate = delivereddate;
        this.p_latitude = p_latitude;
        this.p_longtitude = p_longtitude;
        this.d_longtitude = d_longtitude;
        this.d_latitude = d_latitude;
        this.p_unit = p_unit;
        this.p_floor = p_floor;
        this.p_building = p_building;
        this.p_contactnumber = p_contactnumber;
        this.p_contactname = p_contactname;
        this.d_unit = d_unit;
        this.d_floor = d_floor;
        this.d_building = d_building;
        this.d_contactnumber = d_contactnumber;
        this.d_contactname = d_contactname;
    }

    public String getorederid ()
    {
        return Orderid;
    }

    public String getdate ()
    {
        return date;
    }
    public String getstatus()
    {
        return Status;
    }

    public String getdroplocation()
    {
        return droplocation;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        Orderid = orderid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDroplocation() {
        return droplocation;
    }

    public void setDroplocation(String droplocation) {
        this.droplocation = droplocation;
    }

    public void setPickupAddress(String pickupAddress) {
        PickupAddress = pickupAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivereddate() {
        return delivereddate;
    }

    public void setDelivereddate(String delivereddate) {
        this.delivereddate = delivereddate;
    }

    public String getP_latitude() {
        return p_latitude;
    }

    public void setP_latitude(String p_latitude) {
        this.p_latitude = p_latitude;
    }

    public String getP_longtitude() {
        return p_longtitude;
    }

    public void setP_longtitude(String p_longtitude) {
        this.p_longtitude = p_longtitude;
    }

    public String getD_longtitude() {
        return d_longtitude;
    }

    public void setD_longtitude(String d_longtitude) {
        this.d_longtitude = d_longtitude;
    }

    public String getD_latitude() {
        return d_latitude;
    }

    public void setD_latitude(String d_latitude) {
        this.d_latitude = d_latitude;
    }

    public String getPickupAddress()
    {
        return PickupAddress;
    }
    public String gettype()
    {
        return type;
    }



    public String getdelivereddate ()

    {
        return delivereddate;
    }




}
