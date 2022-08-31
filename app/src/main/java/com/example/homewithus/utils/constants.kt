package com.example.homewithus.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object constants {
    const val USERS : String = "Users"
    const val HOMEWITHUS_PREFRENCES : String = "HomeWthUsPrefs"
    const val LOGGED_IN_USERNAME : String = "logged_in_username"
    const val EXTRA_USER_DETAILS : String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val USER_PROFILE_IMAGE : String = "user_profile_image"

    const val MOBILE : String = "mobile"
    const val IMAGE : String = "image"
    const val COMPLETE_PROFILE :String = "complete_porofile"

    const val ITEM_IMAGE : String = "item_image"

    fun showimagechoice (activity:Activity){
        val galaryIntent = Intent (
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galaryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getfileextension(activity: Activity, uri: Uri?):String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}