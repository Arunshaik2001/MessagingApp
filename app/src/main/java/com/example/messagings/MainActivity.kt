package com.example.messagings

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMsg(view:View){
        if(android.os.Build.VERSION.SDK_INT >=23) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                sendMessage();
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 102);
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==102 && grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendMessage()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),102)
        }
    }

    private fun sendMessage() {
        val no=phoneEditText.text.toString()
        val msg=msgEditText.text.toString()
        if(no.isNotEmpty() && msg.isNotEmpty()) {
            val sharedPreferences=getSharedPreferences("MessagingData", MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.putString("phoneNo",no)
            editor.putString("message",msg)
            editor.apply()
            finish()
        }else{
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTime(view:View) {
        val timePickerDialog=TimePickerDialog(
            this,  { _, hourOfDay, minute ->
                val calendar=Calendar.getInstance()
                calendar.set(
                    Calendar.DAY_OF_YEAR,Calendar.DAY_OF_MONTH,Calendar.DATE,hourOfDay, minute
                )
                setAlarm(calendar.timeInMillis)
            },12,0,false
        )

        timePickerDialog.show()
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager:AlarmManager= getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(this,MyMessageBroadCast::class.java)
        val pendingIntent=PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent)
        Toast.makeText(this,"Alarm Set",Toast.LENGTH_SHORT).show()
    }
}