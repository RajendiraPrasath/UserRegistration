package com.example.userregistration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {
    lateinit var verifyButton : Button
    lateinit var userEnterPhone : EditText
    lateinit var progerss : ProgressBar
    lateinit var userPhoneNumber : String
    lateinit var verificationCodeBySystem : String
    private lateinit var auth: FirebaseAuth
    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            var code = credential.smsCode
            if(code!=null)
            {
                progerss.visibility = View.VISIBLE
                verifyCode(code)
            }

        }
        override fun onVerificationFailed(e: FirebaseException) {
         Toast.makeText(this@VerifyPhoneActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
        }
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationCodeBySystem = verificationId

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.verifyphone_layout)
        verifyButton = findViewById(R.id.verify) as Button
        userEnterPhone = findViewById(R.id.verifcation_code) as EditText
        progerss = findViewById(R.id.progressbar) as ProgressBar
        progerss.visibility = View.GONE
       userPhoneNumber = intent.getStringExtra("phoneNo")
       // Log.i("phone ",userPhoneNumber)
        senVerificationCodeToUser(userPhoneNumber)
        verifyButton.setOnClickListener(){
            var manualEnterCode = userEnterPhone.text.toString()
            if(manualEnterCode.isNullOrEmpty() || manualEnterCode.length<6)
            {
                userEnterPhone.setError("Wrong OTP...")
                userEnterPhone.requestFocus()
                return@setOnClickListener
            } else {
                progerss.visibility = View.VISIBLE
                verifyCode(manualEnterCode)
            }
        }
    }
    private fun senVerificationCodeToUser(phoneNo : String)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91"+phoneNo, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            TaskExecutors.MAIN_THREAD, // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks
    }
    private fun verifyCode(verifycode : String){
        var credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,verifycode)
        signInUserByCredential(credential)
    }
    private fun signInUserByCredential(credential : PhoneAuthCredential)
    {
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential).addOnCompleteListener(this@VerifyPhoneActivity) { task ->
            if (task.isSuccessful) {
                //val user = task.result?.user

                //var code = user.
                //val userProfile = Intent(this@VerifyPhoneActivity,SignUpActivity::class.java)
                //userProfile.putExtra("Obj",databaseValue)
                //userProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                //startActivity(userProfile)
                var signUpIntent = Intent()
                signUpIntent.putExtra("message","Success")
                setResult(Activity.RESULT_OK,signUpIntent)
                finish()
            } else {
                Toast.makeText(this@VerifyPhoneActivity,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}