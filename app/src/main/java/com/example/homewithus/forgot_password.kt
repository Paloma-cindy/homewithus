package com.example.homewithus

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class forgot_password : BaseActivity() {

    lateinit var toolbar_forgot_password:Toolbar
    lateinit var submit:Button
    lateinit var eMail_FP:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        toolbar_forgot_password=findViewById(R.id.toolbar_forgot_password)
        submit=findViewById(R.id.submit)
        eMail_FP=findViewById(R.id.eMail_FP)
    }

    private fun setupactionBar(){
        setSupportActionBar(toolbar_forgot_password)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        toolbar_forgot_password.setNavigationOnClickListener { onBackPressed() }

        submit.setOnClickListener {
            val email:String = eMail_FP.text.toString().trim{ it <= ' '}
            if (email.isEmpty()){
                showerrorsnackbar(resources.getString(R.string.err_msg_email), true)
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(
                                this@forgot_password,
                                resources.getString(R.string.err_msg_email_sent),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            showerrorsnackbar(task.exception!!.message.toString(), true)
                        }
                    }
            }

        }
    }

}