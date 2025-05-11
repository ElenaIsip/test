package com.example.travel

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val services = listOf(
        Triple(R.id.txtExpenses, R.id.btnExpenses, ExpensesActivity::class.java),
        Triple(R.id.txtReceipts, R.id.btnReceipts, ReceiptsActivity::class.java),
        Triple(R.id.txtBuyTickets, R.id.btnBuyTickets, BuyTicketsActivity::class.java),
        Triple(R.id.txtTickets, R.id.btnTickets, TicketDetailsActivity::class.java),
        Triple(R.id.txtPasses, R.id.btnPasses, PassesActivity::class.java),
        Triple(R.id.txtBenefits, R.id.btnBenefits, BenefitsActivity::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_service)

        setupClickListeners()
        setupSearch()
    }

    private fun setupClickListeners() {
        services.forEach { (textId, buttonId, activityClass) ->
            findViewById<ImageButton>(buttonId).setOnClickListener {
                startActivity(Intent(this, activityClass))
            }
        }
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.searchEditText).addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = filterServices(s.toString())
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
        )
    }

    private fun filterServices(query: String) {
        services.forEach { (textId, buttonId, _) ->
            val textView = findViewById<TextView>(textId)
            val container = findViewById<View>(buttonId).parent as ViewGroup

            val isVisible = textView.text.toString()
                .lowercase()
                .contains(query.lowercase())

            container.visibility = if (query.isEmpty() || isVisible) View.VISIBLE else View.GONE
        }
    }
}