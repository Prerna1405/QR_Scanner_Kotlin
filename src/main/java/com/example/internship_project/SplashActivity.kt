package com.example.internship_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.internship_project.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Find the ImageView
        val logoImage = findViewById<ImageView>(R.id.logoImage)

        // Load the zoom-in animation
        val zoomInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        logoImage.startAnimation(zoomInAnimation)

        // Move to MainActivity after animation
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500) // Wait for animation to complete
    }
}
