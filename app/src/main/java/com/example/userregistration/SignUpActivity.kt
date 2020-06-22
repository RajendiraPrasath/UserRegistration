package com.example.userregistration

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.userregistration.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.loginscreen_layout.*
import kotlinx.android.synthetic.main.signup_layout.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var dbReference: DatabaseReference
    lateinit var userName : TextInputLayout
    lateinit var fullName : TextInputLayout
    lateinit var email : TextInputLayout
    lateinit var phoneno : TextInputLayout
    lateinit var password : TextInputLayout
    lateinit var image : ImageView
    lateinit var logoName : TextView
    lateinit var sloganName : TextView
    companion object {
        const val CONTACT_REQUEST = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.signup_layout)
        userName = findViewById(R.id.name) as TextInputLayout
        fullName = findViewById(R.id.username) as TextInputLayout
        email = findViewById(R.id.email) as TextInputLayout
        phoneno = findViewById(R.id.phoneNo) as TextInputLayout
        password = findViewById(R.id.password) as TextInputLayout
        image = findViewById(R.id.logoImage) as ImageView
        logoName = findViewById(R.id.logo_name) as TextView
        sloganName = findViewById(R.id.slogan_name) as TextView

        dbReference = FirebaseDatabase.getInstance().getReference()
        register.setOnClickListener(){

            if(!validateName() || !validateUserName() ||!validateEmail() || !validatePhoneNo() || !validatePassword()) {
                return@setOnClickListener
            } else {
                verifyPhone(phoneno.editText!!.text.toString())
                //registerUser()
            }

        }
        loginpage.setOnClickListener()
        {
            var loginIntent = Intent(this@SignUpActivity,LoginActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignUpActivity,
                    androidx.core.util.Pair<View, String>(image,"Logo_image"),
                    androidx.core.util.Pair<View, String>(logoName,"Logo_text"),
                    androidx.core.util.Pair<View, String>(sloganName,"Logo_desc"),
                    androidx.core.util.Pair<View, String>(userName,"username_tran"),
                    androidx.core.util.Pair<View, String>(password,"password_tran"))
                startActivity(loginIntent,options.toBundle())

            }

        }

    }
    private fun verifyPhone(phoneNo:String?){
        val phoneVerify = Intent(this@SignUpActivity,VerifyPhoneActivity::class.java)
        phoneVerify.putExtra("phoneNo",phoneNo)
        startActivityForResult(phoneVerify, CONTACT_REQUEST)

    }
    private fun validateName() : Boolean
    {
        val nameValue = userName.editText!!.text.toString()
        if(nameValue.isNullOrEmpty())
        {
            userName.error = "Field Cannot be empty"
            return false
        } else {
            userName.error = null
            return true
        }

    }
    private fun validateUserName() : Boolean
    {
        val usernameValue = fullName.editText!!.text.toString()
        val noWhiteSpace = "(?=\\S+$)"
        if(usernameValue.isNullOrEmpty())
        {
            fullName.error = "Field Cannot be empty"
            return false
        } else if(usernameValue.length >=15){
            fullName.error = "FullName too long"
            return false
        } else if(!isWhiteSpace(usernameValue))
        {
            fullName.error = "White Space are not allowed"
            return false
        }
        else {
            fullName.error = null
            return true
        }

    }
    private fun validateEmail() : Boolean
    {
        val emailValue = email.editText!!.text.toString()
        if(emailValue.isNullOrEmpty())
        {
            email.error = "Field Cannot be empty"
            return false
        } else if(!isValidEmail(emailValue)){
            email.error = "Invalid email Address"
            return false
        }
        else {
            email.error = null
            return true
        }

    }
    private fun validatePhoneNo() : Boolean
    {
        val phoneValue = phoneno.editText!!.text.toString()
        if(phoneValue.isNullOrEmpty())
        {
            phoneno.error = "Field Cannot be empty"
            return false
        } else if(phoneValue.length !=10){
            phoneno.error = "Enter 10 Digits Phone Number"
            return false
        }
        else {
            phoneno.error = null
            return true
        }

    }
    private fun validatePassword() : Boolean
    {
        val passwordValue = password.editText!!.text.toString()
        if(passwordValue.isNullOrEmpty())
        {
            password.error = "Field Cannot be empty"
            return false
        } else if(!isValidPassword(passwordValue)){
            password.error = "Password must be 1 special character and at least 4 character"
            return false
        }
        else {
            password.error = null
            return true
        }

    }
    private fun isValidEmail(mail : String ) : Boolean
    {
       return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
    private fun isValidPassword(password: String?) : Boolean {
        password?.let {
            //val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordPattern = "^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }
    private fun isWhiteSpace(userName : String? ) : Boolean{
        userName?.let {

            val passwordPattern = "^(?=\\S+$).{4,20}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(userName) != null
        } ?: return false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_REQUEST) {
           if (resultCode == Activity.RESULT_OK) {
                registerUser()
            }
        }
    }
    private fun registerUser()
    {

        val user= User( userName.editText!!.text.toString(),
            fullName.editText!!.text.toString(),
            email.editText!!.text.toString(),
            phoneno.editText!!.text.toString(),
            password.editText!!.text.toString())
           dbReference.child("users").child(phoneno.editText!!.text.toString()).setValue(user)
            dbReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val userData=dataSnapshot.getValue(User::class.java)

                //Check for null
                if(userData==null){
                    Log.e("Register","User data is null")
                    return
                } else {
                    Toast.makeText(this@SignUpActivity,"Sucessfully Registered",Toast.LENGTH_LONG).show()
                    val userProfile = Intent(this@SignUpActivity,UserProfileActivity::class.java)
                    userProfile.putExtra("Obj",user)
                    userProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@SignUpActivity,
                            androidx.core.util.Pair<View, String>(image, "Logo_image"),
                            androidx.core.util.Pair<View, String>(logoName, "Logo_text"),
                            androidx.core.util.Pair<View, String>(sloganName, "Logo_desc")
                        )
                        startActivity(userProfile)
                    }
                    //finish()
                }



            }
            override fun onCancelled(error: DatabaseError){
                //Failed to read value
                Log.e("User Registran","Failed to read user",error.toException())
            }
        })
    }
}
