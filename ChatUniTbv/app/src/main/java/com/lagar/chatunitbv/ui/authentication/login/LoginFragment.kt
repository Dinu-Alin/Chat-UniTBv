package com.lagar.chatunitbv.ui.authentication.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.MainActivity
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private var _binding : LoginFragmentBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {

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
        }

        return binding.root
    }

    private fun signIn(email: String, password: String) {
//        val intent = Intent(activity, MainActivity::class.java)
//        startActivity(intent)
        val directions = LoginFragmentDirections.navigateToMainActivity()
        findNavController().navigate(directions)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}