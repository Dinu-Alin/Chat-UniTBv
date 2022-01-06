package com.lagar.chatunitbv.ui.fragments.authentication.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthCredential
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.RegisterStep1FragmentBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.ui.fragments.authentication.start.StartFragmentDirections
import com.lagar.chatunitbv.ui.fragments.chats.ChatsFragmentDirections
import com.lagar.chatunitbv.util.validators.FieldValidators
import com.lagar.chatunitbv.util.validators.FieldValidators.containsNumber
import com.lagar.chatunitbv.util.validators.FieldValidators.isStringLowerAndUpperCase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentSnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.firestore.DocumentReference

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class RegisterStep1Fragment : Fragment() {
    private var _binding: RegisterStep1FragmentBinding? = null

    private val binding get() = _binding!!

    companion object {
        var emailValid: Boolean = false
        var passwordValid: Boolean = false
        var confirmPasswordValid: Boolean = false

        const val TAG = "RegisterEmailPassword"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterStep1FragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.goToLoginButton.setOnClickListener {
            val direction =
                RegisterStep1FragmentDirections.actionNavigationRegisterToNavigationLogin()
            findNavController().navigate(direction)

        }

        binding.buttonNext.setOnClickListener {

            Operations.auth.fetchSignInMethodsForEmail(binding.editTextEmail.text.toString())
                .addOnCompleteListener {
                    if (it.result?.signInMethods?.contains("password") == false) {
                        val directions =
                            RegisterStep1FragmentDirections.actionNavigationRegisterToRegisterStep2Fragment(
                                email = binding.editTextEmail.text.toString(),
                                password = binding.editTextPassword.text.toString()
                            )
                        findNavController().navigate(directions)
                    } else {
                        binding.emailLayout.error = "Email already in use!"
                        binding.editTextEmail.requestFocus()
                    }
                }


        }

        setupListeners()
        return root
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
                binding.buttonNext.isEnabled = true
                binding.buttonNext.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_black,
                    null
                )
            } else {
                binding.buttonNext.isEnabled = false
                binding.buttonNext.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_gray,
                    null
                )
            }
        }
    }

    // TODO add user shared preferences as in LoginFragment
}