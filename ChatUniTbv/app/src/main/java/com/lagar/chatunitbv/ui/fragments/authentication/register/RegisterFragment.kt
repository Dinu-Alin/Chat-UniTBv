package com.lagar.chatunitbv.ui.fragments.authentication.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.RegisterFragmentBinding
import com.lagar.chatunitbv.util.validators.FieldValidators
import com.lagar.chatunitbv.util.validators.FieldValidators.containsNumber
import com.lagar.chatunitbv.util.validators.FieldValidators.isStringLowerAndUpperCase

class RegisterFragment : Fragment() {
    private var _binding : RegisterFragmentBinding? = null

    private val binding get() = _binding!!
    companion object {
        var emailValid: Boolean = false
        var passwordValid: Boolean = false
        var confirmPasswordValid: Boolean = false

        const val TAG = "RegisterEmailPassword"

        @JvmStatic
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.goToLoginButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_register_to_navigation_login)

        }

        setupListeners();
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
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

    private fun validateConfirmPassword(): Boolean {
        when {
            binding.editTextConfirmPassword.text.toString().trim().isEmpty() -> {
                binding.confirmPasswordToggleLayout.error = "Required Field!"
                binding.editTextConfirmPassword.requestFocus()
                return false
            }
            binding.editTextConfirmPassword.text.toString() != binding.editTextPassword.text.toString() -> {
                binding.confirmPasswordToggleLayout.error = "Passwords don't match"
                binding.editTextConfirmPassword.requestFocus()
                return false
            }
            else -> {
                binding.confirmPasswordToggleLayout.isErrorEnabled = false
            }
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.editTextPassword.text.toString().trim().isEmpty()) {
            binding.passwordToggleLayout.error = "Required field"
            binding.editTextPassword.requestFocus()
            return false
        } else if (binding.editTextPassword.text.toString().length < 6) {
            binding.passwordToggleLayout.error = "password can't be less than 6"
            binding.editTextPassword.requestFocus()
            return false
        } else if (!containsNumber(binding.editTextPassword.text.toString())) {
            binding.passwordToggleLayout.error = "Required at least 1 digit"
            binding.editTextPassword.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.editTextPassword.text.toString())) {
            binding.passwordToggleLayout.error =
                "Password must contain upper and lower case letters"
            binding.editTextPassword.requestFocus()
            return false
//        } else if (!isStringContainSpecialCharacter(binding.editTextPassword.text.toString())) {
//            binding.passwordToggleLayout.error = "1 special character required"
//            binding.editTextPassword.requestFocus()
//            return false
        } else {
            binding.passwordToggleLayout.isErrorEnabled = false
        }
        return true
    }

    private fun setupListeners() {
        binding.editTextEmail.addTextChangedListener(TextFieldValidation(binding.editTextEmail))
        binding.editTextPassword.addTextChangedListener(TextFieldValidation(binding.editTextPassword))
        binding.editTextConfirmPassword.addTextChangedListener(TextFieldValidation(binding.editTextConfirmPassword))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.

            when (view.id) {

                binding.editTextEmail.id -> {
                    emailValid = validateEmail()
                }
                binding.editTextPassword.id -> {
                    passwordValid = validatePassword()
                }
                binding.editTextConfirmPassword.id -> {
                    confirmPasswordValid = validateConfirmPassword()
                }
            }
            if (emailValid && passwordValid && confirmPasswordValid) {
                binding.buttonSingUp.isEnabled = true
                binding.buttonSingUp.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_button_background_black, null)
            } else {
                binding.buttonSingUp.isEnabled = false
                binding.buttonSingUp.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_button_background_gray, null)
            }
        }
    }

}