package com.example.homewithus

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

        fun showerrorsnackbar(message:String, errorMassage:Boolean){
            val snackbar=
                Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
            val snackBarView=snackbar.view

            if (errorMassage){
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                    R.color.colorSnackBarError
                    )
                )
            }else{
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                    R.color.colorSnackBarSuccess
                    )
                )
            }
            snackbar.show()
        }
    }
