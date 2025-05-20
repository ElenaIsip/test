package com.example.travel

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
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
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val PREFS_NAME = "expenses_prefs"
        private const val KEY_BASE_BALANCE = "base_balance"
        private const val KEY_GENERATED_INCOMES = "generated_incomes"
        private const val KEY_FIRST_RUN = "first_run"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        checkFirstRun()

        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            saveData()
            finish()
        }

        updateBalance()
    }

    private fun checkFirstRun() {
        if (prefs.getBoolean(KEY_FIRST_RUN, true)) {
            generateUniqueIncomes()
            setupExpenses()
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
            saveData()
        } else {
            loadData()
            restoreUI()
        }
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
            Triple("Электричка РЖД", "Транспорт", 2450.0),
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

    @SuppressLint("SetTextI18n")
    private fun restoreUI() {
        val incomesContainer = findViewById<LinearLayout>(R.id.llIncomes)
        val expensesContainer = findViewById<LinearLayout>(R.id.llExpenses)

        // Восстановление доходов
        repeat(prefs.getInt("incomes_count", 0)) { index ->
            createTransactionItem(
                container = incomesContainer,
                category = prefs.getString("income_category_$index", "") ?: "",
                description = prefs.getString("income_desc_$index", "") ?: "",
                amount = prefs.getFloat("income_amount_$index", 0f).toDouble(),
                isIncome = true
            )
        }

        // Восстановление расходов
        repeat(prefs.getInt("expenses_count", 0)) { index ->
            createTransactionItem(
                container = expensesContainer,
                category = prefs.getString("expense_category_$index", "") ?: "",
                description = prefs.getString("expense_desc_$index", "") ?: "",
                amount = prefs.getFloat("expense_amount_$index", 0f).toDouble(),
                isIncome = false
            )
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
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            textContainer.addView(this)
        }

        TextView(this).apply {
            text = description
            setTextColor(ContextCompat.getColor(this@ExpensesActivity, R.color.white))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            textContainer.addView(this)
        }

        TextView(this).apply {
            text = "${if (isIncome) "+" else "-"}${amount.toInt()} ₽"
            setTextColor(ContextCompat.getColor(
                this@ExpensesActivity,
                if (isIncome) R.color.income_green else R.color.expense_red
            ))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setPadding(16.dpToPx(), 0, 0, 0)
        }.also { amountView ->
            itemLayout.addView(textContainer)
            itemLayout.addView(amountView)
        }

        container.addView(itemLayout)
    }

    private fun saveData() {
        prefs.edit().apply {
            putFloat(KEY_BASE_BALANCE, baseBalance.toFloat())
            putFloat(KEY_GENERATED_INCOMES, generatedIncomes.toFloat())

            // Сохранение структуры доходов
            val incomesContainer = findViewById<LinearLayout>(R.id.llIncomes)
            putInt("incomes_count", incomesContainer.childCount)
            for (i in 0 until incomesContainer.childCount) {
                val item = incomesContainer.getChildAt(i) as LinearLayout
                val texts = (item.getChildAt(0) as LinearLayout)
                val categoryView = texts.getChildAt(0) as TextView
                val descView = texts.getChildAt(1) as TextView
                val amountView = item.getChildAt(1) as TextView

                putString("income_category_$i", categoryView.text.toString())
                putString("income_desc_$i", descView.text.toString())
                putFloat("income_amount_$i", amountView.text
                    .replace("[^\\d]".toRegex(), "")
                    .toFloat())
            }

            // Сохранение структуры расходов
            val expensesContainer = findViewById<LinearLayout>(R.id.llExpenses)
            putInt("expenses_count", expensesContainer.childCount)
            for (i in 0 until expensesContainer.childCount) {
                val item = expensesContainer.getChildAt(i) as LinearLayout
                val texts = (item.getChildAt(0) as LinearLayout)
                val categoryView = texts.getChildAt(0) as TextView
                val descView = texts.getChildAt(1) as TextView
                val amountView = item.getChildAt(1) as TextView

                putString("expense_category_$i", categoryView.text.toString())
                putString("expense_desc_$i", descView.text.toString())
                putFloat("expense_amount_$i", amountView.text
                    .replace("[^\\d]".toRegex(), "")
                    .toFloat())
            }

            apply()
        }
    }

    private fun loadData() {
        baseBalance = prefs.getFloat(KEY_BASE_BALANCE, 500.0f).toDouble()
        generatedIncomes = prefs.getFloat(KEY_GENERATED_INCOMES, 0.0f).toDouble()
    }

    private fun updateBalance() {
        val total = baseBalance + generatedIncomes
        findViewById<TextView>(R.id.tvBalance).text =
            "Баланс: ${"%,.0f".format(total).replace(",", " ")} ₽"
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun Int.dpToPx(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        resources.displayMetrics
    ).toInt()

    private fun Double.roundToTen(): Double = (this / 10).roundToInt() * 10.0
}