@file:Suppress("DEPRECATION")

package com.example.travel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

        val tvNumber = findViewById<TextView>(R.id.tvTicketNumber)
        val tvPassport = findViewById<TextView>(R.id.tvPassport)
        val btnClose = findViewById<ImageButton>(R.id.imageButton)
        val btnViewReceipts = findViewById<MaterialButton>(R.id.btnViewReceipts)

        // Инициализация SharedPreferences
        val prefs = getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE)
        val intentPassengers = intent.getIntExtra("PASSENGERS", -1)

        if (intentPassengers != -1) {
            // Сценарий 1: Активность запущена с новыми данными пассажиров
            handleIntentWithPassengers(prefs, intentPassengers, tvNumber, tvPassport)
        } else {
            // Сценарий 2: Активность перезапущена (поворот экрана или повторный вход)
            handleRestoredData(prefs, tvNumber, tvPassport)
        }

        btnClose.setOnClickListener { navigateToMain() }
        btnViewReceipts.setOnClickListener { showReceipts() }
    }

    private fun handleIntentWithPassengers(
        prefs: SharedPreferences,
        passengers: Int,
        tvNumber: TextView,
        tvPassport: TextView
    ) {
        val savedPassengers = prefs.getInt("PASSENGERS", -1)
        val savedTicket = prefs.getString("TICKET_NUMBER", null)
        val savedPassport = prefs.getString("PASSPORT_INFO", null)

        if (savedPassengers != passengers || savedTicket == null || savedPassport == null) {
            // Генерация и сохранение новых данных
            generateAndSaveNewTicket(prefs, passengers, tvNumber, tvPassport)
        } else {
            // Использование сохраненных данных
            tvNumber.text = savedTicket
            tvPassport.text = savedPassport
        }
    }

    private fun handleRestoredData(
        prefs: SharedPreferences,
        tvNumber: TextView,
        tvPassport: TextView
    ) {
        val savedPassengers = prefs.getInt("PASSENGERS", -1)
        val savedTicket = prefs.getString("TICKET_NUMBER", null)
        val savedPassport = prefs.getString("PASSPORT_INFO", null)

        if (savedPassengers != -1 && savedTicket != null && savedPassport != null) {
            // Восстановление сохраненных данных
            tvNumber.text = savedTicket
            tvPassport.text = savedPassport
        } else {
            // Резервный сценарий: генерация данных по умолчанию
            generateAndSaveNewTicket(prefs, 1, tvNumber, tvPassport)
        }
    }

    private fun generateAndSaveNewTicket(
        prefs: SharedPreferences,
        passengers: Int,
        tvNumber: TextView,
        tvPassport: TextView
    ) {
        val ticketNumber = "Билет №${Random.nextInt(1000, 9999)}"
        val passportInfo = generatePassportInfo(passengers)

        prefs.edit().apply {
            putInt("PASSENGERS", passengers)
            putString("TICKET_NUMBER", ticketNumber)
            putString("PASSPORT_INFO", passportInfo)
            apply()
        }

        tvNumber.text = ticketNumber
        tvPassport.text = passportInfo
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
        startActivity(Intent(this, ReceiptsActivity::class.java))
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}