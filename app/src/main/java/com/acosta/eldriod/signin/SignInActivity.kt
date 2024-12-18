package com.acosta.eldriod.signin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.acosta.eldriod.HomeActivity
import com.acosta.eldriod.R
import com.acosta.eldriod.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private val authViewModel: SignInViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpTV = findViewById<TextView>(R.id.signUpTV)

        signUpTV.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener { view ->
            onLoginClick(view)
        }

        // Observing LiveData
        authViewModel.loginResponse.observe(this) { response ->
            response?.let {
//                Toast.makeText(this, "Welcome, ${it.user.name}", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        authViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun onLoginClick(view: View) {
        val email = findViewById<EditText>(R.id.emailET).text.toString()
        val password = findViewById<EditText>(R.id.passwordET).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch {
                authViewModel.login(email, password, sharedPreferences)
            }
        }
    }
}
