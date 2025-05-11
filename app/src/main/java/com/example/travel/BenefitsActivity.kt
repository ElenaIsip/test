package com.example.travel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class BenefitsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benefits)


        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}