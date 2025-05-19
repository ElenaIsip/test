package com.example.travel

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipts)


        val tvTrainNumber = findViewById<TextView>(R.id.tvTrainNumber)
        val tvPaymentTime = findViewById<TextView>(R.id.tvPaymentTime)
        val tvBankName = findViewById<TextView>(R.id.tvBankName)
        val tvBankDetails = findViewById<TextView>(R.id.tvBankDetails)
        val tvCustomerDetails = findViewById<TextView>(R.id.tvCustomerDetails)
        val tvOrderStatus = findViewById<TextView>(R.id.tvOrderStatus)



        val trainNumber = generateTrainNumber()
        val paymentTime = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val (bankName, bankDetails) = generateBankDetails()
        val customerDetails = generateCustomerDetails()
        val orderStatus = if (Random.nextBoolean()) "Оплачено ✓" else "В обработке..."


        tvTrainNumber.text = "Поезд: $trainNumber"
        tvPaymentTime.text = "Оплата: $paymentTime"
        tvBankName.text = "Банк: $bankName"
        tvBankDetails.text = "Реквизиты: $bankDetails"
        tvCustomerDetails.text = "Покупатель:\n$customerDetails"
        tvOrderStatus.text = "Статус: $orderStatus"


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