package com.example.homewithus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class home : AppCompatActivity() {
    lateinit var add:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        add=findViewById(R.id.add)

        add.setOnClickListener {
            val intent = Intent(this@home, item::class.java)
            startActivity(intent)
            finish()
        }
    }
}