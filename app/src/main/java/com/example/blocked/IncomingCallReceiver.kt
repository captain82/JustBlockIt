package com.example.blocked

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.android.internal.telephony.ITelephony
import com.example.blocked.Utils.Util
import com.example.blocked.view.CallBlockService
import java.util.ArrayList


class IncomingCallReceiver:BroadcastReceiver() {

    lateinit var arrayList: ArrayList<String>


    @SuppressLint("SoonBlockedPrivateApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
        val list = intent?.getStringArrayListExtra("LIST")

        //Toast.makeText(context,arrayList.last(),Toast.LENGTH_SHORT).show()
        /*if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            val intent = Intent(context,CallBlockService::class.java)
            intent.putExtra("NUMBER" , number)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //log("Starting the service in >=26 Mode from a BroadcastReceiver")
                context?.startForegroundService(intent)
            }else
            {
                context?.startService(intent)
            }
        }*/
    }
}