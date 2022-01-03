package com.lagar.chatunitbv.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ActivityStartBinding
import com.lagar.chatunitbv.firebase.Operations

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (getSharedPreferences("REMEMBER_USER", Context.MODE_PRIVATE).getBoolean("user", false)) {
            if (Operations.auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}