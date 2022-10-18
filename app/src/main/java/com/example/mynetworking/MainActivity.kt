package com.example.mynetworking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networkinglibrary.NetWorking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetWorking.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (isConnected) {
                Log.d("myNetWork", "Net Work is connect")
            } else {
                Log.d("myNetWork", "Net Work is not connect")
            }
        }

    }
}