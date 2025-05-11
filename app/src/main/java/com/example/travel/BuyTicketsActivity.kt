package com.example.travel


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class BuyTicketsActivity : AppCompatActivity() {
    private lateinit var fromCity: AutoCompleteTextView
    private lateinit var toCity: AutoCompleteTextView
    private lateinit var ticketsListView: ListView
    private lateinit var tvPrice: TextView
    private lateinit var editTextNumber: EditText
    private lateinit var textView14: TextView
    private lateinit var buyButton: Button
    private var filteredTickets = listOf<Ticket>()

    private val allTickets = listOf(
        Ticket("Москва", "Санкт-Петербург", 2500),
        Ticket("Москва", "Нижний Новгород", 1500),
        Ticket("Москва", "Томск", 3500),
        Ticket("Москва", "Сочи", 5500),
        Ticket("Москва", "Воронеж", 6900)
    )

    private var selectedTicket: Ticket? = null
    private val cities = listOf(
        "Москва",
        "Санкт-Петербург",
        "Нижний Новгород",
        "Томск",
        "Сочи",
        "Воронеж"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_tickets)

        initViews()
        setupDateInputs()
        setupAutocompleteAdapters()
        setupListeners()
        updateTicketsList()
    }

    private fun initViews() {
        fromCity = findViewById(R.id.fromCity)
        toCity = findViewById(R.id.toCity)
        ticketsListView = findViewById(R.id.ticketsListView)
        tvPrice = findViewById(R.id.tvPrice)
        editTextNumber = findViewById(R.id.editTextNumber)
        textView14 = findViewById(R.id.textView14)
        buyButton = findViewById(R.id.buyButton)
    }



    private fun setupAutocompleteAdapters() {
        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )
        fromCity.setAdapter(cityAdapter)
        toCity.setAdapter(cityAdapter)
    }


    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateTicketsList()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        fromCity.addTextChangedListener(textWatcher)
        toCity.addTextChangedListener(textWatcher)

        ticketsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            selectedTicket = filteredTickets.getOrNull(position)
            selectedTicket?.let {
                tvPrice.text = "${it.price}"
                updateTotalPrice()
            } ?: run {
                Toast.makeText(this, "Ошибка выбора билета", Toast.LENGTH_SHORT).show()
            }
        }

    editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateTotalPrice()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buyButton.setOnClickListener {
            selectedTicket?.let {
                val passengers = editTextNumber.text.toString().toIntOrNull() ?: 0
                if (passengers > 0) {
                    showTicketDetails(passengers)
                } else {
                    Toast.makeText(this, "Введите количество пассажиров", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Выберите билет из списка", Toast.LENGTH_SHORT).show()
            }

        }
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateTicketsList() {
            filteredTickets = allTickets.filter { ticket ->
                ticket.from.equals(fromCity.text.toString(), ignoreCase = true) &&
                        ticket.to.equals(toCity.text.toString(), ignoreCase = true)
            }


        if (filteredTickets.isEmpty()) {
            Toast.makeText(this, "Билеты по вашему запросу не найдены", Toast.LENGTH_SHORT).show()
        }


        val adapter = TicketAdapter(this, filteredTickets)
        ticketsListView.adapter = adapter
    }

    private fun updateTotalPrice() {
        selectedTicket?.let { ticket ->
            val passengers = editTextNumber.text.toString().toIntOrNull() ?: 0
            textView14.text = "${ticket.price * passengers} руб"
        }
    }


    private fun showTicketDetails(passengers: Int) {
        val intent = Intent(this, TicketDetailsActivity::class.java).apply {
            putExtra("PASSENGERS", passengers)
        }
        startActivity(intent)
    }

    private fun setupDateInputs() {
        val dateFormat = "dd.MM.yyyy"

        fun setupDateEditText(editText: EditText) {
            editText.addTextChangedListener(object : TextWatcher {
                private var current = ""

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() != current) {
                        editText.removeTextChangedListener(this)

                        val cleanString = s.toString().replace("[^\\d]".toRegex(), "")
                        var result = ""

                        when (cleanString.length) {
                            in 1..2 -> result = cleanString
                            in 3..4 -> result = "${cleanString.substring(0,2)}.${cleanString.substring(2)}"
                            else -> {
                                if (cleanString.length > 8) {
                                    result = "${cleanString.substring(0,2)}." +
                                            "${cleanString.substring(2,4)}." +
                                            "${cleanString.substring(4,8)}"
                                } else {
                                    result = "${cleanString.substring(0,2)}." +
                                            "${cleanString.substring(2,4)}." +
                                            cleanString.substring(4)
                                }
                            }
                        }

                        current = result
                        editText.setText(current)
                        editText.setSelection(current.length)

                        editText.addTextChangedListener(this)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        setupDateEditText(findViewById(R.id.editTextDate2))
        setupDateEditText(findViewById(R.id.editTextDate3))
    }



}