package com.example.messagings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

class MyMessageBroadCast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPreferences=context!!.getSharedPreferences("MessagingData",0x0000)
        val manager: SmsManager = SmsManager.getDefault()
        manager.sendTextMessage(sharedPreferences?.getString("phoneNo",""),null,sharedPreferences?.getString("message",""),null,null)
        Toast.makeText(context,"Sent", Toast.LENGTH_SHORT).show()
    }
}