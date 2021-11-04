package com.lagar.chatunitbv.ui.authentication.start

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.FragmentHomeBinding
import com.lagar.chatunitbv.databinding.StartFragmentBinding
import com.lagar.chatunitbv.ui.home.HomeViewModel

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel
    private var _binding: StartFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       viewModel=
            ViewModelProvider(this).get(StartViewModel::class.java)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}