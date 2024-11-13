package com.pedpo.pedporent.utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.pedpo.pedporent.di.utill.ApplicationPedpo;


public class NetConnection {

    private static NetConnection netConnection = new NetConnection();
    public static NetConnection newInstance() {
        return netConnection;
    }

    public boolean isDisconnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationPedpo.Companion.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.e("networkk", "23 or later  ");
            Network activeNetwork;
            activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
//                Log.e("networkk", "getActiveNetwork is null ");
//                DialogConnection.newInstance.showProgress(context);
                return true;
            }
            NetworkCapabilities networkCapabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork);

            boolean validated =  networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);

            boolean internet = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//            Log.e("networkk", "internet : "+internet + " && validate : "+ validated);

            if (validated || internet)
                return false;
            else
            {
                Toast.makeText(context, "check your connection", Toast.LENGTH_SHORT).show();
//                DialogConnection.newInstance.showProgress(context);
                return true;
            }
        } else {
//            Log.e("networkk", "22 ");
//                NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            return isDisconnectApi22();
        }
    }

    private boolean isDisconnectApi22() {
        ConnectivityManager contextSystemService = (ConnectivityManager) ApplicationPedpo.Companion.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //  user is online
            return false;
        } else if (contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            // user is offline
            //Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();


//            DialogConnection.newInstance.showProgress(context);
            return true;
        }
        return true;
    }


    // Network Check

}
