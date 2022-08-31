package com.example.homewithus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.homewithus.utils.constants

class item : BaseActivity(), View.OnClickListener {
    lateinit var phone_number :EditText

    private var mitemimageURL: String? = null
    private var mitemprofile : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        phone_number=findViewById(R.id.phone_number_item)
    }
    override fun onClick (v: View){
        if ( v != null){
            when(v.id){
                R.id.item_image-> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
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
                R.id.upload -> {

                }


            }
        }
    }
    private fun updateitemrprofiledetails(){
        val usermshup = HashMap<String,Any>()
        val phone_number = phone_number.text.toString().trim(){ it <= ' '}

        if (mitemprofile?.isNotEmpty() == true){
            usermshup[constants.IMAGE] = mitemimageURL!!
        }

        if (phone_number.isNotEmpty()){
            usermshup[constants.MOBILE] = phone_number.toLong()
        }

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

                startActivity(Intent(this@item,MainActivity::class.java))
                finish()
            }
        }
    }

    fun itemimageuploadsuccess(imageURL:String){

        mitemimageURL = imageURL
        updateitemrprofiledetails()

    }


}