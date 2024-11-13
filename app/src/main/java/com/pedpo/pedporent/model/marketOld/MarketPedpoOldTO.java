package com.pedpo.pedporent.model.marketOld;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketPedpoOldTO {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("toolID")
    @Expose
    private String toolID;

    @SerializedName("isBuy")
    private boolean isBuy;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("salePrice")
    @Expose
    private double salePrice;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("rentPrice")
    @Expose
    private double rentPrice;

    @SerializedName("photo")
    @Expose
    private String[] photo;

    @SerializedName("Price")
    @Expose
    private double price;

    @SerializedName("contactToQuote")
    @Expose
    private boolean contactToQuote;

    @SerializedName("free")
    @Expose
    private boolean free;

    @SerializedName("LoginUserID")
    @Expose
    private String loginUserID;

    @SerializedName("id")
    private String id;


    public boolean isContactToQuote() {
        return contactToQuote;
    }

    public void setContactToQuote(boolean contactToQuote) {
        this.contactToQuote = contactToQuote;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToolID() {
        return toolID;
    }

    public void setToolID(String toolID) {
        this.toolID = toolID;
    }


    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public int getSalePrice() {
        return (int)salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }




    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public int getRentPrice() {
        return (int) rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLoginUserID() {
        return loginUserID;
    }

    public void setLoginUserID(String loginUserID) {
        this.loginUserID = loginUserID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
