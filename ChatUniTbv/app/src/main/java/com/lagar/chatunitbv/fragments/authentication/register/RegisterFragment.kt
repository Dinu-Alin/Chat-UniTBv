package com.lagar.chatunitbv.fragments.authentication.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {
    private var _binding : RegisterFragmentBinding? = null

    private val binding get() = _binding!!
    companion object {
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
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}