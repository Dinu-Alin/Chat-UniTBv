package com.lagar.chatunitbv.ui.chats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatFragmentBinding
import com.lagar.chatunitbv.databinding.PeopleFragmentBinding

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var _binding: ChatFragmentBinding? = null

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        _binding = ChatFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnView: Button = binding.goBtn
        btnView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_people)
        }
        _binding = ChatFragmentBinding.inflate(inflater, container, false)


//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}