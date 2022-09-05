package com.example.homewithus.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.homewithus.item
import com.example.homewithus.login
import com.example.homewithus.module.user
import com.example.homewithus.profile
import com.example.homewithus.utils.constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class firestoreclass {

    private val mfirestore =FirebaseFirestore.getInstance()

    fun registerUser(activity: login, userinfo: user){
        mfirestore.collection(constants.USERS)
            .document(userinfo.id)
            .set(userinfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.registrationsuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While Registering The Use",
                e
                )
            }
    }

    fun getcurrentuserID() :String {

        val currentuser = FirebaseAuth.getInstance().currentUser

        var currrentuserId = ""
        if (currentuser != null) {
            currrentuserId = currentuser.uid
        }
        return currrentuserId
    }

    fun getuserdetails(activity:Activity){
        mfirestore.collection(constants.USERS)
            .document(getcurrentuserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(user::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        constants.HOMEWITHUS_PREFRENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    constants.LOGGED_IN_USERNAME,
                    "${user.username}"
                )
                editor.apply()

                when(activity){
                    is login ->{
                        activity.loginsuccess(user)
                    }
                }
            }

    }

    fun updateuserprofiledata(activity: Activity, userHashMap: HashMap<String, Any>){

        mfirestore.collection(constants.USERS)
            .document(getcurrentuserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is profile ->{
                        activity.userprofleupdatesuccessful()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity){
                    is profile ->{

                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                "Error updating profile",
                e
                )
            }

    }

    fun uplodimagetocloudstorage(activity: Activity,imageFileURI: Uri?){
        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
        +constants.getfileextension(
                activity,
                imageFileURI
        )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { takesnapshot ->
            Log.e(
                "Firebase image URL",
                takesnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            takesnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable image url", uri.toString())
                    when(activity){
                        is profile -> {
                            activity.imageuploadsuccess(uri.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    when (activity){
                        is profile -> {

                        }
                    }
                    Log.e(
                        activity.javaClass.simpleName,
                    exception.message,
                        exception
                    )
                }
        }
    }

    fun uploaditemimagetocloudstorage(activity: Activity,imageFileURI: Uri?){
        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            constants.ITEM_IMAGE+ System.currentTimeMillis() + "."
                    +constants.getfileextension(
                activity,
                imageFileURI
            )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { takesnapshot ->
            Log.e(
                "Firebase image URL",
                takesnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            takesnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable image url", uri.toString())
                    when(activity){
                        is item -> {
                            activity.itemimageuploadsuccess(uri.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    when (activity){
                        is item -> {

                        }
                    }
                    Log.e(
                        activity.javaClass.simpleName,
                        exception.message,
                        exception
                    )
                }
        }
    }

}