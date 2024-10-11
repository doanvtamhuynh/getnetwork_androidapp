package com.example.getnetwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;

public class NetworkUtil {

    public static String getNetworkStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return "WiFi";
                    } else if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            // Kiểm tra khả năng hỗ trợ LTE
                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                                (networkCapabilities.getLinkDownstreamBandwidthKbps() > 0) &&
                                (networkCapabilities.getLinkUpstreamBandwidthKbps() > 0)) {
                            // Chúng ta có thể coi đây là LTE
                            return "LTE";
                        }
                    }
                }
            }
        }
        return "No connection";
    }

    public static long getMobileUpload(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        return networkCapabilities.getLinkUpstreamBandwidthKbps();
    }

    public static long getMobileDownload(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        return networkCapabilities.getLinkDownstreamBandwidthKbps();
    }
}