package com.mycompany.catchmeadminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_send_notification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class SendNotificationActivity : AppCompatActivity() {

    val TAG = "SendNotificationActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notification)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)






        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Send Notifications"

        btnSend.setOnClickListener {
            val tokenFromFM = enterToken.text.toString()
            println(tokenFromFM)
            val title = enterTitle.text.toString()
            val message = enterMessage.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    tokenFromFM

                ).also {
                    sendNotification(it)
                }

            }

            val intent = Intent(this,SendNotificationActivity::class.java)
            startActivity(intent)
            finish()


        }
    }



    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {

            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }




}