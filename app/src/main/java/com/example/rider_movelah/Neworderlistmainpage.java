package com.example.rider_movelah;

public class Neworderlistmainpage {

    private String Orderid;
    private String date ;
    private String Status;
    private String droplocation;
    private String PickupAddress;
    private String type;
    private String delivereddate;

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

    public Neworderlistmainpage(String orderid, String date, String status, String droplocation, String pickupAddress, String type, String delivereddate) {
        Orderid = orderid;
        this.date = date;
        Status = status;
        this.droplocation = droplocation;
        PickupAddress = pickupAddress;
        this.type = type;
        this.delivereddate = delivereddate;
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
