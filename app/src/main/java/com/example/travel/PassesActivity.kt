package com.example.travel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PassesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passes)


        val imageView4 = findViewById<ImageView>(R.id.imageView4)
        val imageView5 = findViewById<ImageView>(R.id.imageView5)

        imageView4.setOnClickListener {
            it.isSelected = !it.isSelected

        }

        imageView5.setOnClickListener {
            it.isSelected = !it.isSelected

        }


        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}