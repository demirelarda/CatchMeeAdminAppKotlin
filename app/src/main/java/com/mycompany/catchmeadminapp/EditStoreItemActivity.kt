package com.mycompany.catchmeadminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditStoreItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_store_item)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Edit Store Items"
    }
}