package com.lagar.chatunitbv.fragments.authentication.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.databinding.LoginFragmentBinding
import com.lagar.chatunitbv.firebase.autentification.Authenticator
import com.lagar.chatunitbv.util.InternetCheck


class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null

    private val binding get() = _binding!!

    companion object {
        private const val TAG = "EmailPassword"

        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

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

        return binding.root
    }


    private fun signIn(email: String, password: String) {

        val auth = Authenticator.instance

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val user = auth.currentUser
                val directions = LoginFragmentDirections.navigateToMainActivity()
                rememberMe(binding.checkboxRememberMe.isChecked)
                findNavController().navigate(directions)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        // TODO: Use the ViewModel
    }

}