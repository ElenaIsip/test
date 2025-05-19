@file:Suppress("DEPRECATION")

package com.example.travel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


class TicketDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        val passengers = intent.getIntExtra("PASSENGERS", 1)


        val tvNumber = findViewById<TextView>(R.id.tvTicketNumber)
        val tvPassport = findViewById<TextView>(R.id.tvPassport)
        val btnClose = findViewById<ImageButton>(R.id.imageButton)
        val btnViewReceipts = findViewById<MaterialButton>(R.id.btnViewReceipts)


        tvNumber.text = "Билет №${Random.nextInt(1000, 9999)}"
        tvPassport.text = generatePassportInfo(passengers)


        btnClose.setOnClickListener {
            navigateToMain()
        }

        btnViewReceipts.setOnClickListener {

            showReceipts()
        }
    }

    private fun generatePassportInfo(passengers: Int): String {
        return buildString {
            repeat(passengers) {
                append("Пассажир ${it + 1}:\n")
                append("Серия ${"%04d".format(Random.nextInt(0, 9999))} ")
                append("№${"%06d".format(Random.nextInt(0, 999999))}\n\n")
            }
        }.trim()
    }

    private fun showReceipts() {

        val intent = Intent(this, ReceiptsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}