package com.example.userregistration

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat



class MainActivity : AppCompatActivity() {
    private val SPLASH_SCREEN : Int = 5000
    lateinit var topAnimation : Animation
    lateinit var bottomAnimation : Animation
    lateinit var splashImage : ImageView
    lateinit var logo : TextView
    lateinit var slogon : TextView
    lateinit private var pairs: Array<android.util.Pair<View, String>>
    val pairList = ArrayList<android.util.Pair<View, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.splashscreen_layout)
        splashImage = findViewById(R.id.splashimage) as ImageView
        logo = findViewById(R.id.textView) as TextView
        slogon = findViewById(R.id.textView2) as TextView
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)
        splashImage.startAnimation(topAnimation)
        logo.startAnimation(bottomAnimation)
        slogon.startAnimation(bottomAnimation)

            Handler().postDelayed({
                val mainIntent = Intent(this@MainActivity, LoginActivity::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                        androidx.core.util.Pair<View, String>(splashImage,"Logo_image"),
                        androidx.core.util.Pair<View, String>(logo,"Logo_text"))
                        startActivity(mainIntent,options.toBundle())

                }

            }, SPLASH_SCREEN.toLong())

    }
}