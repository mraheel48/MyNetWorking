package com.example.networkinglibrary

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetWorking : ConnectivityManager.NetworkCallback() {

    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getNetworkLiveData(context: Context): LiveData<Boolean> {

        networkLiveData.postValue(false)

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(networkRequest, this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nw = connectivityManager.activeNetwork

            if (nw == null) {
                networkLiveData.postValue(false)
                return networkLiveData
            }

            val actNw = connectivityManager.getNetworkCapabilities(nw)

            if (actNw == null) {
                networkLiveData.postValue(false)
                return networkLiveData
            }

            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

        } else {

            @Suppress("DEPRECATION")
            (return if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                networkLiveData.postValue(true)
                networkLiveData
            } else {
                networkLiveData.postValue(false)
                networkLiveData
            })

        }

        return networkLiveData
    }

    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }

    private val Context.isNetworkConnected: Boolean
        get() {

            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || it.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    ) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                } ?: false
            else
                @Suppress("DEPRECATION")
                manager.activeNetworkInfo?.isConnected == true
        }

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

}