package com.lagar.chatunitbv.ui.fragments.authentication.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.LoginFragmentBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.User
import com.lagar.chatunitbv.preferences.UserSharedPreferencesRepository
import com.lagar.chatunitbv.util.checks.InternetCheck


class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {

            if (view?.let { it1 -> InternetCheck.isOnline(it1.context) } == true) {

                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)
                } else {
                    Toast.makeText(
                        it.context,
                        "Please fill in all the required fields",
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


        binding.resetPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_resetPasswordFragment)

        }


        return binding.root
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

                        val directions = LoginFragmentDirections.navigateToMainActivity()
                        rememberMe(binding.checkboxRememberMe.isChecked)
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

    private fun rememberMe(checked: Boolean) {
        val preferences =
            this.activity?.getSharedPreferences("REMEMBER_USER", Context.MODE_PRIVATE)
                ?.edit()
        preferences?.putBoolean("user", checked)
        preferences?.apply()
    }

}