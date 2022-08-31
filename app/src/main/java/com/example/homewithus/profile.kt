package com.example.homewithus

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.homewithus.firestore.firestoreclass
import com.example.homewithus.module.user
import com.example.homewithus.utils.constants
import com.example.homewithus.utils.glideloader

class profile :BaseActivity(), View.OnClickListener{
    lateinit var first_name:EditText
    lateinit var second_name:EditText
    lateinit var userimage:ImageView
    lateinit var phone_number:EditText
    lateinit var save:Button

   private var userdetails: user = user()
    private var mSelectionImageFileuri: Uri? = null
    private var muserprofileURL : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        first_name = findViewById(R.id.first_name)
        second_name = findViewById(R.id.second_name)
        userimage=findViewById(R.id.user_image)
        phone_number=findViewById(R.id.phone_number)
        save=findViewById(R.id.save)



        if (intent.hasExtra(constants.EXTRA_USER_DETAILS)) {
            userdetails = intent.getParcelableExtra(constants.EXTRA_USER_DETAILS)!!

            userimage.setOnClickListener(this@profile)
            save.setOnClickListener(this@profile)

        }

    }

    override fun onClick (v: View){
        if ( v != null){
            when(v.id){
                R.id.user_image-> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_GRANTED
                    ){
//                       showerrorsnackbar("You already have storage permission", false)
                        constants.showimagechoice(this)
                    }else{
                       ActivityCompat.requestPermissions(
                           this,
                           arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                           constants.READ_STORAGE_PERMISSION_CODE
                       )
                    }
                }

                R.id.save ->{

                    if (validateuserpofilesitails()){

                        if (mSelectionImageFileuri != null)
                            firestoreclass().uplodimagetocloudstorage(this, mSelectionImageFileuri)
                        else {
                            updateuserprofiledetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateuserprofiledetails(){
        val usermshup = HashMap<String,Any>()
        val phone_number = phone_number.text.toString().trim(){ it <= ' '}

        if (muserprofileURL?.isNotEmpty() == true){
            usermshup[constants.IMAGE] = muserprofileURL!!
        }

        if (phone_number.isNotEmpty()){
            usermshup[constants.MOBILE] = phone_number.toLong()
        }

        usermshup[constants.COMPLETE_PROFILE] = 1

        firestoreclass().updateuserprofiledata(this,usermshup)
    }

    fun userprofleupdatesuccessful(){
        Toast.makeText(
            this@profile,
            resources.getString(R.string.msg_update_success),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                showerrorsnackbar("This storage permission is granted", false)
                constants.showimagechoice(this)
            }else{
                Toast.makeText(
                    this, resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()

                startActivity(Intent(this@profile,home::class.java))
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null){
                try {
                    mSelectionImageFileuri = data.data!!
//                    userimage.setImageURI(Uri.parse(selectImagefileur.toString()))
                    glideloader(this).loaderuserimage(mSelectionImageFileuri!!, userimage)

                } catch (e : WindowManager.InvalidDisplayException){
                    e.printStackTrace()
                    Toast.makeText(
                        this@profile,
                        resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "Image selection cancelled")

        }
    }

    private fun validateuserpofilesitails(): Boolean {
        return when {
            TextUtils.isEmpty(phone_number.text.toString().trim{ it <= ' '}) ->{
                showerrorsnackbar(resources.getString(R.string.err_msg_mobile_number),true)
                false
            }else -> {
                true
            }
        }
    }
    fun imageuploadsuccess(imageURL:String){

        muserprofileURL = imageURL
        updateuserprofiledetails()
    }

}