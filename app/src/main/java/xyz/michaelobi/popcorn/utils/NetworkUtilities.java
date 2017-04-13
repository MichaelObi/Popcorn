package xyz.michaelobi.popcorn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtilities {

    public static Boolean isNetworkEnabled(Context c) {
        ConnectivityManager cManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager != null) {

            NetworkInfo[] netInfo = cManager.getAllNetworkInfo();

            if (netInfo != null) {
                for (int i = 0; i < netInfo.length; i++) {
                    if (netInfo[i].getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
