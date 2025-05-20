package com.example.travel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


class ReceiptsActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "ReceiptsPrefs"
        private const val KEY_TRAIN_NUMBER = "trainNumber"
        private const val KEY_PAYMENT_TIME = "paymentTime"
        private const val KEY_BANK_NAME = "bankName"
        private const val KEY_BANK_DETAILS = "bankDetails"
        private const val KEY_CUSTOMER_DETAILS = "customerDetails"
        private const val KEY_ORDER_STATUS = "orderStatus"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipts)

        val tvTrainNumber = findViewById<TextView>(R.id.tvTrainNumber)
        val tvPaymentTime = findViewById<TextView>(R.id.tvPaymentTime)
        val tvBankName = findViewById<TextView>(R.id.tvBankName)
        val tvBankDetails = findViewById<TextView>(R.id.tvBankDetails)
        val tvCustomerDetails = findViewById<TextView>(R.id.tvCustomerDetails)
        val tvOrderStatus = findViewById<TextView>(R.id.tvOrderStatus)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        if (sharedPreferences.contains(KEY_TRAIN_NUMBER)) {
            // Load saved data
            tvTrainNumber.text = "Поезд: ${sharedPreferences.getString(KEY_TRAIN_NUMBER, "")}"
            tvPaymentTime.text = "Оплата: ${sharedPreferences.getString(KEY_PAYMENT_TIME, "")}"
            tvBankName.text = "Банк: ${sharedPreferences.getString(KEY_BANK_NAME, "")}"
            tvBankDetails.text = "Реквизиты: ${sharedPreferences.getString(KEY_BANK_DETAILS, "")}"
            tvCustomerDetails.text = "Покупатель:\n${sharedPreferences.getString(KEY_CUSTOMER_DETAILS, "")}"
            tvOrderStatus.text = "Статус: ${sharedPreferences.getString(KEY_ORDER_STATUS, "")}"
        } else {
            // Generate and save new data
            val trainNumber = generateTrainNumber()
            val paymentTime = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val (bankName, bankDetails) = generateBankDetails()
            val customerDetails = generateCustomerDetails()
            val orderStatus = if (Random.nextBoolean()) "Оплачено ✓" else "В обработке..."

            sharedPreferences.edit().apply {
                putString(KEY_TRAIN_NUMBER, trainNumber)
                putString(KEY_PAYMENT_TIME, paymentTime)
                putString(KEY_BANK_NAME, bankName)
                putString(KEY_BANK_DETAILS, bankDetails)
                putString(KEY_CUSTOMER_DETAILS, customerDetails)
                putString(KEY_ORDER_STATUS, orderStatus)
                apply()
            }

            // Display new data
            tvTrainNumber.text = "Поезд: $trainNumber"
            tvPaymentTime.text = "Оплата: $paymentTime"
            tvBankName.text = "Банк: $bankName"
            tvBankDetails.text = "Реквизиты: $bankDetails"
            tvCustomerDetails.text = "Покупатель:\n$customerDetails"
            tvOrderStatus.text = "Статус: $orderStatus"
        }

        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun generateTrainNumber(): String {
        val letters = ('A'..'Z').toList()
        return buildString {
            append(letters.random())
            append(Random.nextInt(100, 999))
            append(letters.random())
        }
    }

    private fun generateBankDetails(): Pair<String, String> {
        val banks = listOf(
            "Сбербанк" to "БИК 044525225, счет 40817810500000012345",
            "Тинькофф" to "БИК 044525974, счет 40701810400000054321",
            "Альфа-Банк" to "БИК 044525593, счет 40702810900000098765"
        )
        return banks.random()
    }

    private fun generateCustomerDetails(): String {
        val names = listOf(
            "Иванов Иван Иванович\nivanov@mail.ru\n+7 912 345-67-89",
            "Петрова Анна Сергеевна\npetrova@gmail.com\n+7 987 654-32-10",
            "Сидоров Алексей Владимирович\nsidorov@yandex.ru\n+7 905 123-45-67"
        )
        return names.random()
    }
}