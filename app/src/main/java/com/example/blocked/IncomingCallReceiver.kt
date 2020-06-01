package com.example.blocked

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import com.android.internal.telephony.ITelephony
import java.lang.Exception

class IncomingCallReceiver:BroadcastReceiver() {
    @SuppressLint("SoonBlockedPrivateApi")
    override fun onReceive(context: Context?, intent: Intent?) {

        var telephonyService: ITelephony
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            try {
                val m = tm.javaClass.getDeclaredMethod("getITelephony")
                m.isAccessible = true
                telephonyService =  m.invoke(tm) as ITelephony
                if(number !=null){
                    telephonyService.endCall()
                    Toast.makeText(context, "Ending the call from: " + number, Toast.LENGTH_SHORT).show();
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            Toast.makeText(context, "Ring " + number, Toast.LENGTH_SHORT).show();
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, "Answered " + number, Toast.LENGTH_SHORT).show();
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Toast.makeText(context, "Idle "+ number, Toast.LENGTH_SHORT).show();
        }


    }
}