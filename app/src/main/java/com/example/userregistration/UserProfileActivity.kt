package com.example.userregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.userregistration.model.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProfileActivity : AppCompatActivity() {

    lateinit var fullName : TextInputEditText
    lateinit var email : TextInputEditText
    lateinit var phoneno : TextInputEditText
    lateinit var password : TextInputEditText
    lateinit var userfullname : TextView
    lateinit var userName : TextView
    lateinit var update : Button
    lateinit var databasefullname : String
    lateinit var databaseemail : String
    lateinit var databasephone : String
    lateinit var databasepassword : String
    lateinit var databaseusername : String
    private lateinit var dbReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.userprofile_layout)
        fullName = findViewById(R.id.name) as TextInputEditText
        email = findViewById(R.id.email) as TextInputEditText
        phoneno = findViewById(R.id.phoneNo) as TextInputEditText
        password = findViewById(R.id.password) as TextInputEditText
        userfullname = findViewById(R.id.fullname) as TextView
        userName = findViewById(R.id.username) as TextView
        update = findViewById(R.id.update) as Button
        dbReference = FirebaseDatabase.getInstance().getReference("users");
        setUserDate()
        update.setOnClickListener(){
            updateData()
        }

    }
    private fun setUserDate()
    {

            val userDetails: User? = intent.getParcelableExtra("Obj")

        databasefullname = userDetails!!.fullName.toString()
        databaseemail = userDetails!!.email.toString()
        databasephone = userDetails!!.phoneNo.toString()
        databasepassword = userDetails!!.password.toString()
        databaseusername = userDetails!!.userName.toString()
            userfullname.text = databasefullname
            userName.text = databaseusername
            fullName.setText(databasefullname)
            email.setText(databaseemail)
            phoneno.setText(databasephone)
            password.setText(databasepassword)

    }
    private fun updateData()
    {
        if(isNameChanged() || isEmailChanged())
        {
                Toast.makeText(this@UserProfileActivity,"Data has been updated",Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@UserProfileActivity,"Data is same and can not be updated",Toast.LENGTH_LONG).show()
        }
    }
    private fun isNameChanged() : Boolean{
        if(fullName.editableText.toString().isNullOrEmpty())
        {
            fullName.error = "Name can not empty"
            return false
        }else if(!databasefullname.equals(fullName.editableText.toString()))
        {
            dbReference.child(databaseusername).child("fullName").setValue(fullName.editableText.toString())
            userfullname.text = fullName.editableText.toString()
            databasefullname = fullName.editableText.toString()
            email.error = null
            return true
        } else {
            return false
        }
    }
    private fun isEmailChanged() : Boolean{
        if(email.editableText.toString().isNullOrEmpty())
        {
            email.error = "Email can not empty"
            return false
        }else if(!databaseemail.equals(email.editableText.toString()))
        {
            if(isValidEmail(email.editableText.toString())) {
                dbReference.child(databaseusername).child("email")
                    .setValue(email.editableText.toString())
                databaseemail = email.editableText.toString()
                email.error = null
                return true
            } else {
                email.error = "Enter valid Email id"
                return false
            }

        } else {
            return false
        }
    }


    private fun isValidEmail(mail : String ) : Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
}