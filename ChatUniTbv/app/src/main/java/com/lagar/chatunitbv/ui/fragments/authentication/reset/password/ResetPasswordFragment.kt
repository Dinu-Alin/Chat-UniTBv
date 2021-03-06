package com.lagar.chatunitbv.ui.fragments.authentication.reset.password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.FragmentResetPasswordBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.ui.fragments.authentication.register.RegisterStep1Fragment
import com.lagar.chatunitbv.util.checks.InternetCheck
import com.lagar.chatunitbv.util.validators.FieldValidators


class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnResetPassword.setOnClickListener {

            if (view?.let { it1 -> InternetCheck.isOnline(it1.context) } == true) {

                val email = binding.editTextEmail.text.toString()

                if (email.isNotEmpty()) {
                    resetPassword(email)
                } else {
                    Toast.makeText(
                        it.context,
                        "Please fill email field",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            } else {
                Toast.makeText(
                    it.context,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        setupListeners()
        return root
    }

    private fun setupListeners() {
        binding.editTextEmail.addTextChangedListener(TextFieldValidation(binding.editTextEmail))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.

            when (view.id) {

                binding.editTextEmail.id -> {
                    RegisterStep1Fragment.emailValid = validateEmail()
                }

            }
            if (RegisterStep1Fragment.emailValid) {
                binding.btnResetPassword.isEnabled = true
                binding.btnResetPassword.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_black,
                    null
                )
            } else {
                binding.btnResetPassword.isEnabled = false
                binding.btnResetPassword.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_gray,
                    null
                )
            }
        }
    }

    private fun resetPassword(email: String) {

        val auth = Operations.auth

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            binding.emailLayout.isErrorEnabled = false
            if (task.isSuccessful) {
                Toast.makeText(
                    context, "Success",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.emailLayout.error = "Invalid email"
                binding.editTextEmail.requestFocus()
            }
        }

    }


    private fun validateEmail(): Boolean {
        if (binding.editTextEmail.text.toString().trim().isEmpty()) {
            binding.emailLayout.error = "Required field"
            binding.editTextEmail.requestFocus()
            return false
        } else if (!FieldValidators.isValidEmail(binding.editTextEmail.text.toString())) {
            binding.emailLayout.error = "Invalid email"
            binding.editTextEmail.requestFocus()
            return false
        } else {
            binding.emailLayout.isErrorEnabled = false
        }
        return true
    }
}