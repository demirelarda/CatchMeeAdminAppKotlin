package com.mycompany.catchmeadminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val currentUser = auth.currentUser



        if(currentUser != null){
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


    fun loginClicked(view:View){

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email != "" && password!=""){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
                if(task.isSuccessful){
                    val currentUser = auth.currentUser
                    val username = currentUser!!.displayName
                    if(auth.uid == "Dj09q1SbpmaaPQIkOqulihUaLeC3"){ //get this from firebase.
                        Toast.makeText(this,"Welcome $username !", Toast.LENGTH_LONG).show()
                        val intent = Intent(this,AdminPanelActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"You are not authorized!",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
        else{
            Toast.makeText(this,"Don't leave the entries blank!", Toast.LENGTH_LONG).show()
        }


    }


}





