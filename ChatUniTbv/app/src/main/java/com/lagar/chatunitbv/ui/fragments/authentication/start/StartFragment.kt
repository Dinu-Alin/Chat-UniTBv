package com.lagar.chatunitbv.ui.fragments.authentication.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.StartFragmentBinding

class StartFragment : Fragment() {

    private var _binding: StartFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = StartFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val loginBtn: Button = binding.buttonLogIn
        loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
        val registerBtn: Button = binding.buttonRegister
        registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_registerFragment)
        }
        return root
    }
}