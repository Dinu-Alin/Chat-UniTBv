package com.lagar.chatunitbv.ui.people

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.PeopleFragmentBinding

class PeopleFragment : Fragment() {
    private lateinit var peopleViewModel: PeopleViewModel
    private var _binding: PeopleFragmentBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        peopleViewModel =
            ViewModelProvider(this)[PeopleViewModel::class.java]
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
