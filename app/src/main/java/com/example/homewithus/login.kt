package com.example.homewithus

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.homewithus.firestore.firestoreclass
import com.example.homewithus.module.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class login : BaseActivity() {
    lateinit var signUp: TextView
    lateinit var login: TextView
    lateinit var forgot_password: TextView
    lateinit var eMails: EditText
    lateinit var passwords: EditText
    lateinit var password1: EditText
    lateinit var sign_in: Button
    lateinit var log_in: Button
    lateinit var eMail: EditText
    lateinit var passwword: EditText
    lateinit var username: EditText

    lateinit var signInlayout: LinearLayout
    lateinit var loginlayout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginlayout = findViewById(R.id.loginlayout)
        signInlayout = findViewById(R.id.signinlayout)
        eMails = findViewById(R.id.eMails)
        passwords = findViewById(R.id.passwords)
        password1 = findViewById(R.id.password1)
        sign_in = findViewById(R.id.sign_in)
        log_in = findViewById(R.id.log_in)
        eMail = findViewById(R.id.eMail)
        passwword = findViewById(R.id.password)
        username = findViewById(R.id.Username)

        forgot_password = findViewById(R.id.forgot_password)
        forgot_password.setOnClickListener {
            val intent = Intent(this, com.example.homewithus.forgot_password::class.java)
            startActivity(intent)
        }

        signUp = findViewById(R.id.signUp)
        signUp.setOnClickListener {
            signUp.background =
                ResourcesCompat.getDrawable(resources, R.drawable.switch_trucks, null)
            login.background = null
            signInlayout.visibility = View.VISIBLE
            loginlayout.visibility = View.GONE
            login.setTextColor(ContextCompat.getColor(this, R.color.pinkColor))
        }

        login = findViewById(R.id.login)
        login.setOnClickListener {
            signUp.background = null
            login.background =
                ResourcesCompat.getDrawable(resources, R.drawable.switch_trucks, null)
            signInlayout.visibility = View.GONE
            loginlayout.visibility = View.VISIBLE
            login.setTextColor(ContextCompat.getColor(this, R.color.pinkColor))
        }

        sign_in.setOnClickListener {
            registerUser()
            val intent=Intent(this@login,home::class.java)
            startActivity(intent)
        }

        log_in.setOnClickListener {
            loginregisterduser()
            val intent=Intent(this@login,home::class.java)
            startActivity(intent)
        }
    }


    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(eMails.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(username.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar("Please Enter Username", true)
                false
            }
            TextUtils.isEmpty(passwords.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_password), true)
                false
            }
            TextUtils.isEmpty(password1.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_confirm_password), true)
                false
            }
            else -> {
                showerrorsnackbar("Valid Details", false)
                true
            }
        }
    }

    private fun validateRegisterDetails2(): Boolean {
        return when {

            TextUtils.isEmpty(eMails.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(passwords.text.toString().trim() { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_password), true)
                false
            }
            else -> {
                showerrorsnackbar("Valid Details", false)
                true
            }
        }
    }

    private fun registerUser() {

        if (validateRegisterDetails()) {
            val username: String = username.text.toString().trim() { it <= ' ' }
            val email: String = eMails.text.toString().trim() { it <= ' ' }
            val password: String = passwords.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    MediaPlayer.OnCompletionListener { task ->

                        if (it.isSuccessful) {

                            val firbaseuser: FirebaseUser? = it.result!!.user!!

                            val user = firbaseuser?.let { it1 ->
                                user(
                                    it1.uid,
                                    eMails.text.toString().trim() { it <= ' ' }
                                )
                            }

                            firestoreclass().registerUser(this@login, user())

                            FirebaseAuth.getInstance().signOut()
                            finish()

                        } else {
                            showerrorsnackbar(it.exception!!.message.toString(), true)
                        }
                    }
                }
        }
    }

    private fun loginregisterduser() {

        if (validateRegisterDetails2()) {

            val email = eMail.text.toString().trim { it <= ' ' }
            val password = passwword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firestoreclass().getuserdetails(this@login)
                    } else {
                        showerrorsnackbar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun registrationsuccess() {
        Toast.makeText(
            this@login,
            resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
    }

    fun loginsuccess(user: user) {

        if (user.profilecomplete == 0) {

            val intent = Intent(this@login, profile::class.java)

            startActivity(intent)

        } else {
            startActivity(Intent(this@login, home::class.java))
        }
        finish()
    }
}
