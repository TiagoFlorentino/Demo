package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    var logoImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity_layout)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        logoImageView = findViewById(R.id.logo_image_view)
        var alphaAnimation : AlphaAnimation = AlphaAnimation(0.2f, 1.0f)
        alphaAnimation.duration = 4000
        logoImageView?.startAnimation(alphaAnimation)

        Handler().postDelayed({
            startActivity(Intent(this,ListActivity::class.java))
            finish()
        }, 4300)
    }
}
