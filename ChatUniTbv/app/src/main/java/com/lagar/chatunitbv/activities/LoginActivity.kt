package com.lagar.chatunitbv.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ActivityLoginBinding
import com.lagar.chatunitbv.firebase.autentification.Authenticator.instance
import com.lagar.chatunitbv.ui.authentication.login.LoginFragmentDirections

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (getSharedPreferences("REMEMBER_USER", Context.MODE_PRIVATE).getBoolean("user", false)) {
            if (instance.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}