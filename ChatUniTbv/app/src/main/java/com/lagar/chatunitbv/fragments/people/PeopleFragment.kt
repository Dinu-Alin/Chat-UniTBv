package com.lagar.chatunitbv.fragments.people

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // log out from shared preferences
        binding.btnLogout.setOnClickListener {
            val preferences =
                this.activity?.getSharedPreferences("REMEMBER_USER", Context.MODE_PRIVATE)
                    ?.edit()
            preferences?.putBoolean("user", false)
            preferences?.apply()
        }
    }
}
