package com.example.travel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val ads = listOf(
        R.drawable.ad1 to "",
        R.drawable.ad2 to "",
        R.drawable.ad3 to ""
    )

    private var currentAd = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_service)

        setupServiceClicks()
        setupSearch()
        startAdRotation()
    }

    private fun startAdRotation() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                showNextAd()
                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    private fun showNextAd() {
        currentAd = (currentAd + 1) % ads.size
        findViewById<ImageView>(R.id.imageView3).setImageResource(ads[currentAd].first)
        findViewById<TextView>(R.id.tvAdText).text = ads[currentAd].second
    }

    private fun setupServiceClicks() {
        listOf(
            R.id.btnExpenses to ExpensesActivity::class.java,
            R.id.btnReceipts to ReceiptsActivity::class.java,
            R.id.btnBuyTickets to BuyTicketsActivity::class.java,
            R.id.btnTickets to TicketDetailsActivity::class.java,
            R.id.btnPasses to PassesActivity::class.java,
            R.id.btnBenefits to BenefitsActivity::class.java
        ).forEach { (id, activity) ->
            findViewById<ImageView>(id).setOnClickListener {
                startActivity(Intent(this, activity))
            }
        }
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.searchEditText).addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = filterServices(s?.toString() ?: "")
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            }
        )
    }

    private fun filterServices(query: String) {
        listOf(R.id.txtExpenses, R.id.txtReceipts, R.id.txtBuyTickets,
            R.id.txtTickets, R.id.txtPasses, R.id.txtBenefits).forEach { id ->
            val view = findViewById<TextView>(id)
            (view.parent as View).visibility = if (view.text.contains(query, ignoreCase = true)) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}