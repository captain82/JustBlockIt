package com.example.blocked

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.android.internal.telephony.ITelephony
import com.example.blocked.Utils.Util


class IncomingCallReceiver:BroadcastReceiver() {

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyService: ITelephony
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val tm = context?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                if (number in Util.blockedNumberList){
                    Toast.makeText(context, "Blocking the call from ${number}", Toast.LENGTH_SHORT).show();
                    tm.endCall()
                }
            }else {
                val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                try {
                    val m = tm.javaClass.getDeclaredMethod("getITelephony")
                    m.isAccessible = true
                    telephonyService = m.invoke(tm) as ITelephony
                    if (number in Util.blockedNumberList) {
                        Toast.makeText(
                            context,
                            "Blocking the call from ${number}",
                            Toast.LENGTH_SHORT
                        ).show()
                        telephonyService.endCall()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}