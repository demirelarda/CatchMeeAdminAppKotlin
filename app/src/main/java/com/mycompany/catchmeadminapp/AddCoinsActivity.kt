package com.mycompany.catchmeadminapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_coins.*


class AddCoinsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore
    var userId = ""
    var currentCoins : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coins)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Coins"

        auth = Firebase.auth
        confirmSendButton.visibility = View.INVISIBLE
        userIdText.visibility = View.INVISIBLE



        getUserIdButton.setOnClickListener {

            //get userId from FB

            db.collection("gamevalues")
                .whereEqualTo("username", editTextUserName.text.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        userId = document.data.get("userId").toString()
                        currentCoins = document.data.get("coins").toString().toDouble()
                        println(userId)
                        confirmSendButton.visibility = View.VISIBLE
                        userIdText.visibility = View.VISIBLE
                        userIdText.text = "User ID: $userId "
                    }
                }
                .addOnFailureListener { exception ->

                }
        }


        confirmSendButton.setOnClickListener {

            val userRef = db.collection("gamevalues").document(userId)
            userRef
                .update("coins", (currentCoins + (editTextCoinAmount.text.toString().toDouble())))
                .addOnSuccessListener { Toast.makeText(this,"Successfully sent coins to: ${editTextUserName.text}",Toast.LENGTH_LONG).show() }
                .addOnFailureListener {Toast.makeText(this,"Failed!",Toast.LENGTH_LONG).show() }

        }










    }










}