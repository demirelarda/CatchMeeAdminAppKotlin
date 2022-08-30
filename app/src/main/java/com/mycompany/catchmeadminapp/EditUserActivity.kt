package com.mycompany.catchmeadminapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class EditUserActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    lateinit var timestamp: Timestamp
    var userEmail : String = ""
    var username : String = ""
    var lastOnline : Long = 0
    var userId: String = ""
    var easyScore: Int = 0
    var mediumScore: Int = 0
    var hardScore: Int = 0
    var notiToken: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Edit User"

        lastOnlineText.visibility = View.INVISIBLE
        scoresText.visibility = View.INVISIBLE
        notiTokenText.visibility = View.INVISIBLE
        userIDText.visibility = View.INVISIBLE
        saveChangesButton.visibility = View.INVISIBLE
        deleteUserButton.visibility = View.INVISIBLE
        userEmailEditText.visibility = View.INVISIBLE
        usernameeditEditText.visibility = View.INVISIBLE








    }

    fun getUserDatas(view:View){

        db.collection("gamevalues")
            .whereEqualTo("username", usernameEditText.text.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    username = document.data.get("username").toString()
                    userId = document.data.get("userId").toString()
                    easyScore = document.data.get("easyScore").toString().toInt()
                    mediumScore = document.data.get("mediumScore").toString().toInt()
                    hardScore = document.data.get("hardScore").toString().toInt()
                    notiToken = document.data.get("notiToken").toString()
                    timestamp = document.data.get("date") as Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val netDate = Date(milliseconds)
                    val date = sdf.format(netDate).toString()

                    setAllVisible(view)
                    lastOnlineText.text = "Last Online: $date"
                    scoresText.text = "Easy Score: $easyScore , Medium Score: $mediumScore , Hard Score: $hardScore"
                    notiTokenText.text = "Notification Token(Click to copy): $notiToken"
                    userIDText.text = "User ID(Click to copy): $userId"
                    usernameeditEditText.setText("Username : $username")

                }
            }
            .addOnFailureListener { exception ->
                println("Error!")
            }



    }


    fun setAllVisible(view: View){

        lastOnlineText.visibility = View.VISIBLE
        scoresText.visibility = View.VISIBLE
        notiTokenText.visibility = View.VISIBLE
        userIDText.visibility = View.VISIBLE
        saveChangesButton.visibility = View.VISIBLE
        deleteUserButton.visibility = View.VISIBLE
        usernameeditEditText.visibility = View.VISIBLE
        deleteUserButton.visibility = View.VISIBLE
        saveChangesButton.visibility = View.VISIBLE


    }


    fun copyText(view: View){
        copy2clipboard(notiTokenText.text.toString())
    }
    fun copyText2(view: View){
        copy2clipboard(userIDText.text.toString())
    }

    fun copy2clipboard(text:CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copy text",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this,"Copied To Clipboard",Toast.LENGTH_LONG).show()

    }

    fun deleteUser(view: View){


        val mBuilder = AlertDialog.Builder(this)
            .setTitle("Deleting User")
            .setMessage("Are you sure want to delete user : $username?")
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)


        val mAlertDialog = mBuilder.create()
        mAlertDialog.show()


        val mPositiveButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            db.collection("gamevalues").document(userId)
                .delete()
                .addOnSuccessListener { Toast.makeText(this,"User: $username, deleted succesfully.",Toast.LENGTH_LONG).show()}
                .addOnFailureListener { Toast.makeText(this,"Failed!",Toast.LENGTH_LONG).show() }
                val intent = Intent(this,EditUserActivity::class.java)
                startActivity(intent)
                finish()

            mAlertDialog.cancel()
        }
    }











}