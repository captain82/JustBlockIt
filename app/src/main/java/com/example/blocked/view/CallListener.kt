package com.example.blocked.view

import android.telecom.TelecomManager
import android.telephony.TelephonyManager

interface CallListener {
    fun blockCall(number: String?,telecomManager: TelecomManager?= null,telephonyManager: TelephonyManager?=null)
}