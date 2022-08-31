package com.example.homewithus.utils

import android.content.Context
import android.net.Uri
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.homewithus.R

class glideloader(val context : Context) {

    fun loaderuserimage(imageUri: Uri, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(Uri.parse(imageUri.toString()))
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(imageView)
        }catch (e:WindowManager.InvalidDisplayException){
            e.printStackTrace()
        }
    }
}