package com.example.userregistration.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userregistration.UserProfileActivity
import com.example.userregistration.model.User
import com.google.firebase.database.*

class FirebaseViewModel(application : Application) : AndroidViewModel(application){
    public lateinit var firebaseResponse: MutableLiveData<String>
    lateinit var dbReference: DatabaseReference
    lateinit var user: MutableLiveData<User>

    init {
        dbReference = FirebaseDatabase.getInstance().getReference("users")
        firebaseResponse = MutableLiveData()
        user = MutableLiveData()

    }
    fun firebaseInsert(user : User)
    {
        dbReference.child(user.phoneNo.toString()).setValue(user)
        dbReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val userData=dataSnapshot.getValue(User::class.java)
                if(userData!=null){
                    firebaseResponse("Sucessfully Registered")


                }
            }
            override fun onCancelled(error: DatabaseError){
                //Failed to read value
                Log.e("User Registran","Failed to read user",error.toException())
                firebaseResponse(error.message.toString())

            }
        })


    }
    fun firebaseResponse(response : String){
        firebaseResponse.value = response

    }
    fun getLoginUserDetails(enteruserName : String, enterpassword : String) {
        var checkUser = dbReference.orderByChild("phoneNo").equalTo(enteruserName)
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
                        user.value = databaseValue
                        } else {
                        firebaseResponse("Wrong Password")

                    }
                } else {
                    firebaseResponse("No such User exist")
                 }
            }

            override fun onCancelled(error: DatabaseError) {
                //Failed to read value
                Log.e("User Registran", "Failed to read user", error.toException())
                firebaseResponse(error.message.toString())
            }
        })
    }
    fun nameUpdate(updatePath : String, updateValue : String)
    {
        dbReference.child(updatePath).child("fullName").setValue(updateValue)
    }
    fun emailUpdate(updatePath : String, updateValue : String)
    {
        dbReference.child(updatePath).child("email").setValue(updateValue)
    }

}