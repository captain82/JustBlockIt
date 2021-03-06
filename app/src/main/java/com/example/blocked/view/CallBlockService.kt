package com.example.blocked.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.android.internal.telephony.ITelephony
import com.example.blocked.R
import com.example.blocked.local.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CallBlockService : Service() {
    val blockedList = mutableListOf<String>()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("android.intent.action.PHONE_STATE")
        val receiver = CallReceiver()
        registerReceiver(receiver, filter)
        AppDatabase.getDatabase(this)?.contactDao()?.query()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                it.forEach { blockedList.add(it.number) }
            }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createNotificationChannel()
        createNotification("0 contacts")
        return START_STICKY
    }

    private fun createNotification(number: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, "id")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Blocked")
            .setContentText("Blocked call from ${number}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()
        startForeground(1, builder)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class CallReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
            val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val telephonyService: ITelephony
            Log.i("log", number + blockedList)
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val tm = context?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                    if (blockedList.contains(number)) {
                        createNotification(number.toString())
                        Toast.makeText(
                            context,
                            "Blocking the call from ${number}",
                            Toast.LENGTH_SHORT)
                            .show();
                        tm.endCall()
                    }
                } else {
                    val tm =
                        context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    try {
                        val m = tm.javaClass.getDeclaredMethod("getITelephony")
                        m.isAccessible = true
                        telephonyService = m.invoke(tm) as ITelephony
                        if (blockedList.contains(number)) {
                            createNotification(number.toString())
                            Toast.makeText(
                                context,
                                "Blocking the call from ${number}",
                                Toast.LENGTH_SHORT
                            ).show()
                            telephonyService.endCall()
                        }
                    } catch (e: Exception) {
                        Log.i("Error", e.localizedMessage)
                        Toast.makeText(
                            context,
                            "Exception${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                }
            }
        }

    }

}