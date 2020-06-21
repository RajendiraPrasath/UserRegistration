package com.example.userregistration

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.userregistration.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private lateinit var signUp: Button
    lateinit var image: ImageView
    lateinit var logoText: TextView
    lateinit var sloganText: TextView
    lateinit var userName: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var login: Button
    private lateinit var dbReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.loginscreen_layout)
        signUp = findViewById(R.id.signUp) as Button
        image = findViewById(R.id.logoImage) as ImageView
        logoText = findViewById(R.id.logo_name) as TextView
        sloganText = findViewById(R.id.slogan_name) as TextView
        userName = findViewById(R.id.username) as TextInputLayout
        password = findViewById(R.id.password) as TextInputLayout
        login = findViewById(R.id.login) as Button
        dbReference = FirebaseDatabase.getInstance().getReference("users");
        signUp.setOnClickListener(){
            val signupIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
            //startActivity(signupIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity,
                    androidx.core.util.Pair<View, String>(image,"Logo_image"),
                    androidx.core.util.Pair<View, String>(logoText,"Logo_text"),
                    androidx.core.util.Pair<View, String>(sloganText,"Logo_desc"),
                    androidx.core.util.Pair<View, String>(userName,"username_tran"),
                    androidx.core.util.Pair<View, String>(password,"password_tran"),
                    androidx.core.util.Pair<View, String>(login,"button_tran"),
                    androidx.core.util.Pair<View, String>(signUp,"login_signup_tran"))
                startActivity(signupIntent,options.toBundle())

            }
        }
        login.setOnClickListener(){
            if(!validateUserName() || !validateEmail())
            {
                return@setOnClickListener
            } else {
                isUser()
            }

        }

    }
    private fun validateUserName() : Boolean
    {
        val usernameValue = userName.editText!!.text.toString()

        if(usernameValue.isNullOrEmpty())
        {
            userName.error = "Field Cannot be empty"
            return false
        }
        else {
            userName.error = null
            return true
        }

    }
    private fun validateEmail() : Boolean
    {
        val emailValue = password.editText!!.text.toString()
        if(emailValue.isNullOrEmpty())
        {
            password.error = "Field Cannot be empty"
            return false
        }
        else {
            password.error = null
            return true
        }

    }
    private fun isUser()
    {
        val enteruserName = userName.editText!!.text.toString()
        val enterpassword = password.editText!!.text.toString()
        var checkUser = dbReference.orderByChild("userName").equalTo(enteruserName)
        checkUser.addListenerForSingleValueEvent( object :ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.exists())
                {
                    val dbPassword = dataSnapshot.child(enteruserName).child("password").value
                    Log.i("password",dbPassword.toString())
                    if(dbPassword!!.equals(enterpassword))
                    {
                        val name = dataSnapshot.child(enteruserName).child("fullName").value
                        val username = dataSnapshot.child(enteruserName).child("userName").value
                        val email = dataSnapshot.child(enteruserName).child("email").value
                        val phoneNo = dataSnapshot.child(enteruserName).child("phoneNo").value
                        val password = dataSnapshot.child(enteruserName).child("password").value
                        val databaseValue = User(name.toString(),username.toString(),email.toString(),phoneNo.toString(),password.toString())
                        val userProfile = Intent(this@LoginActivity,UserProfileActivity::class.java)
                        userProfile.putExtra("Obj",databaseValue)
                        startActivity(userProfile)

                    } else {
                        password.error = "Wrong Password"
                        password.requestFocus()
                    }
                } else {
                    userName.error = "No such User exist"
                    userName.requestFocus()
                }



            }

            override fun onCancelled(error: DatabaseError) {
                //Failed to read value
                Log.e("User Registran", "Failed to read user", error.toException())
            }
        })
    }

}