package com.example.userregistration

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private lateinit var signUp: Button
    lateinit var image: ImageView
    lateinit var logoText: TextView
    lateinit var sloganText: TextView
    lateinit var userName: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var login: Button
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

    }
}