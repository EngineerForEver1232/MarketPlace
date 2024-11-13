package com.pedpo.pedporent.model.marketOld;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MarketPedpoOldList {

    @SerializedName("tool_ViewMoldel")
    private ArrayList<MarketPedpoOldTO> marketPedpoOldTOS;

    @SerializedName("status")
    private boolean status ;

    @SerializedName("errorMessage")
    private String errorMessage ;

    @SerializedName("userMessage")
    private String userMessage ;

    public ArrayList<MarketPedpoOldTO> getMarketTOS() {
        return marketPedpoOldTOS;
    }

    public boolean isStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
