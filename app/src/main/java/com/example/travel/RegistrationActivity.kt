package com.example.travel


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class RegistrationActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registraration)

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        initViews()
        setupListeners()

        // Автоматический вход при наличии сохранённых данных
        if (isUserLoggedIn()) {
            startMainActivity()
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.loginEditText)
        etPassword = findViewById(R.id.passwordEditText)
        btnRegister = findViewById(R.id.registerButton)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            handleRegistration()
        }
    }

    private fun handleRegistration() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (isValidInput(email, password)) {
            if (isFirstTimeUser()) {
                // Сохраняем данные при первом входе
                saveUserCredentials(email, password)
                startMainActivity()
            } else {
                // Проверяем введённые данные
                if (checkCredentials(email, password)) {
                    startMainActivity()
                } else {
                    showLoginError()
                }
            }
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            "Введите логин".also { etEmail.error = it }
            isValid = false
        }

        if (password.isEmpty() || password.length < 6) {
            etPassword.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        }

        return isValid
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.contains("email")
    }

    private fun isFirstTimeUser(): Boolean {
        return !sharedPreferences.contains("email")
    }

    private fun saveUserCredentials(email: String, password: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        return email == savedEmail && password == savedPassword
    }

    private fun showLoginError() {
        etEmail.error = "Неверный логин или пароль"
        etPassword.error = "Неверный логин или пароль"
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}