package com.mycompany.catchmeadminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_admin_panel.*
import kotlinx.android.synthetic.main.activity_main.*

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        supportActionBar!!.title = "Admin Panel"

        auth = Firebase.auth
        val currentUser = auth.currentUser



    }






    fun editUser(view: View){

        val intent = Intent(this,EditUserActivity::class.java)
        startActivity(intent)


    }

    fun addCoins(view: View){

        val intent = Intent(this,AddCoinsActivity::class.java)
        startActivity(intent)



    }

    fun sendNotification(view: View){

        val intent = Intent(this,SendNotificationActivity::class.java)
        startActivity(intent)



    }

    fun addItem(view: View){

        val intent = Intent(this,AddItemToStoreActivity::class.java)
        startActivity(intent)



    }

    fun editItem(view: View){

        val intent = Intent(this,EditStoreItemActivity::class.java)
        startActivity(intent)



    }


}