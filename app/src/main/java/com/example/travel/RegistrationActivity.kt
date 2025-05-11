package com.example.travel


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity



class RegistrationActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        initViews()
        setupListeners()
    }

    private fun initViews() {
        etEmail = this.findViewById(R.id.emailEditText)
        etPassword = findViewById(R.id.passwordEditText)
        etConfirmPassword = findViewById(R.id.confirmPasswordEditText)
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
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (isValidInput(email, password, confirmPassword)) {
            // Переход на главный экран
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun isValidInput(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            etEmail.error = "Введите логин"
            isValid = false
        }

        if (password.isEmpty() || password.length < 6) {
            etPassword.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Пароли не совпадают"
            isValid = false
        }

        return isValid

    }
}




