package com.example.travel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import kotlin.random.Random

class ExpensesActivity : AppCompatActivity() {

    private var baseBalance = 500.0
    private var generatedIncomes = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            finish()
        }

        generateUniqueIncomes()
        setupExpenses()
        updateBalance()
    }

    private fun generateUniqueIncomes() {
        val incomesContainer = findViewById<LinearLayout>(R.id.llIncomes)
        incomesContainer.removeAllViews()


        val incomesCount = Random.nextInt(3, 6)
        val usedAmounts = mutableSetOf<Double>()

        repeat(incomesCount) {
            var amount: Double
            do {
                amount = Random.nextDouble(500.0, 5000.0).roundToTen()
            } while (usedAmounts.contains(amount))

            usedAmounts.add(amount)

            createTransactionItem(
                container = incomesContainer,
                category = "Сбербанк",
                description = when (Random.nextInt(3)) {
                    0 -> "Кэшбэк"
                    1 -> "Перевод"
                    2 -> "Возврат средств"
                    else -> "Поступление"
                },
                amount = amount,
                isIncome = true
            )
            generatedIncomes += amount
        }
    }

    private fun setupExpenses() {
        val expensesContainer = findViewById<LinearLayout>(R.id.llExpenses)


        val expenses = listOf(
            Triple("Поезд РЖД", "Транспорт", 2450.0),
            Triple("Покупка белья", "Одежда", 250.0),
            Triple("Обед в кафе", "Еда", 850.0)
        )

        expenses.forEach { (description, category, amount) ->
            createTransactionItem(
                container = expensesContainer,
                category = category,
                description = description,
                amount = amount,
                isIncome = false
            )
            baseBalance -= amount
        }
    }


    private fun createTransactionItem(
        container: LinearLayout,
        category: String,
        description: String,
        amount: Double,
        isIncome: Boolean
    ) {

        val itemLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
            }
            orientation = LinearLayout.HORIZONTAL
            setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
        }


        val textContainer = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            orientation = LinearLayout.VERTICAL
        }


        TextView(this).apply {
            text = category
            setTextColor(ContextCompat.getColor(
                this@ExpensesActivity,
                if (isIncome) R.color.income_green else R.color.expense_red
            ))
            textSize = 16f
            textContainer.addView(this)
        }


        TextView(this).apply {
            text = description
            setTextColor(ContextCompat.getColor(this@ExpensesActivity, R.color.white))
            textSize = 14f
            textContainer.addView(this)
        }


        val amountView = TextView(this).apply {
            text = "${if (isIncome) "+" else "-"}${amount.toInt()} ₽"
            setTextColor(ContextCompat.getColor(
                this@ExpensesActivity,
                if (isIncome) R.color.income_green else R.color.expense_red
            ))
            textSize = 16f
            setPadding(16.dpToPx(), 0, 0, 0)
        }


        itemLayout.addView(textContainer)
        itemLayout.addView(amountView)

        container.addView(itemLayout)
    }

    private fun updateBalance() {
        val total = baseBalance + generatedIncomes
        findViewById<TextView>(R.id.tvBalance).text =
            "Баланс: ${"%,.0f".format(total).replace(",", " ")} ₽"
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun Double.roundToTen(): Double = (this / 10).roundToInt() * 10.0
}

