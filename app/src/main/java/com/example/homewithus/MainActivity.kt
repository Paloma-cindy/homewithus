package com.example.homewithus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.homewithus.utils.constants

class MainActivity : AppCompatActivity() {
    lateinit var main:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main=findViewById(R.id.main)

        val intent = Intent(this, splashscreen::class.java)
        startActivity(intent)

         }
}