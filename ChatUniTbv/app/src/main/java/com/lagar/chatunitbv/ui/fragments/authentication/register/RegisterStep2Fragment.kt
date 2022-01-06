package com.lagar.chatunitbv.ui.fragments.authentication.register

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.firebase.Operations

import com.lagar.chatunitbv.databinding.RegisterStep2FragmentBinding
import com.lagar.chatunitbv.models.User
import com.lagar.chatunitbv.preferences.UserSharedPreferencesRepository
import com.lagar.chatunitbv.ui.fragments.authentication.login.LoginFragmentDirections
import timber.log.Timber


class RegisterStep2Fragment : Fragment() {
    private var _binding: RegisterStep2FragmentBinding? = null

    private val binding get() = _binding!!
    private val args: RegisterStep2FragmentArgs by navArgs()

    companion object {
        var firstNameValid: Boolean = false
        var lastNameValid: Boolean = false
        var genderValid: Boolean = false

        const val TAG = "RegisterNameGender"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterStep2FragmentBinding.inflate(inflater, container, false)

        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val textField = binding.menuGender
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val root: View = binding.root

        binding.buttonSingUp.setOnClickListener {
            val email = args.email
            val password = args.password
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val gender = binding.itemGender.text.toString()


            val auth = Operations.auth

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("createUserWithEmail:success")
                        val user = auth.currentUser

                        Operations.db.collection("users").document(email).set(
                            hashMapOf(
                                "gender" to gender,
                                "group" to "unknown",
                                "name" to "$firstName $lastName"
                            )
                        ).addOnSuccessListener {
                            signIn(email, password)
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.exception, "createUserWithEmail:failure")
                        Toast.makeText(
                            this.context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        setupListeners()
        return root
    }

    private fun setupListeners() {
        binding.editTextFirstName.addTextChangedListener(TextFieldValidation(binding.editTextFirstName))
        binding.editTextLastName.addTextChangedListener(TextFieldValidation(binding.editTextLastName))
        binding.itemGender.addTextChangedListener(TextFieldValidation(binding.itemGender))
    }

    private fun validateFirstName(): Boolean {
        if (binding.editTextFirstName.text.toString().trim().isEmpty()) {
            binding.firstNameLayout.error = "Required field"
            binding.editTextFirstName.requestFocus()
            return false
        } else {
            binding.firstNameLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (binding.editTextFirstName.text.toString().trim().isEmpty()) {
            binding.firstNameLayout.error = "Required field"
            binding.editTextFirstName.requestFocus()
            return false
        } else {
            binding.firstNameLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateGender(): Boolean {
        if (binding.itemGender.text.toString() != "Male"
            && binding.itemGender.text.toString() != "Female"
        ) {
            binding.menuGender.error = "Required field"
            binding.itemGender.requestFocus()
            return false
        } else {
            binding.menuGender.isErrorEnabled = false
        }
        return true
    }


    private fun signIn(email: String, password: String) {

        val auth = Operations.auth

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val authUser = auth.currentUser

                Operations.db.collection("users").document(authUser?.email.toString()).get()
                    .addOnSuccessListener {
                        val user = it.toObject<User>()!!
                        val sharedPreferences = UserSharedPreferencesRepository(
                            this.requireActivity()
                                .getSharedPreferences("USER", Context.MODE_PRIVATE)
                        )
                        sharedPreferences.write(user)

                        val directions =
                            RegisterStep2FragmentDirections.actionRegisterStep2FragmentToMainActivity()
                        findNavController().navigate(directions)
                    }
            } else {
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    inner class TextFieldValidation(private val view: View) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.

            when (view.id) {

                binding.editTextFirstName.id -> {
                    firstNameValid = validateFirstName()
                }
                binding.editTextLastName.id -> {
                    lastNameValid = validateLastName()
                }
                binding.itemGender.id -> {
                    genderValid = validateGender()
                }
            }

            if (firstNameValid && lastNameValid && genderValid) {
                binding.buttonSingUp.isEnabled = true
                binding.buttonSingUp.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_black,
                    null
                )
            } else {
                binding.buttonSingUp.isEnabled = false
                binding.buttonSingUp.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_button_background_gray,
                    null
                )
            }
        }
    }
}