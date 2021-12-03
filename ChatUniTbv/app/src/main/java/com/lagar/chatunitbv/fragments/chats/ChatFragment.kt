package com.lagar.chatunitbv.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lagar.chatunitbv.databinding.ChatFragmentBinding
import com.lagar.chatunitbv.items.ChatItem
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section

class ChatFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null

    private lateinit var groupAdapter: GroupieAdapter

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChatFragmentBinding.inflate(layoutInflater)

        groupAdapter = GroupieAdapter()

        populateAdapter()

        val layoutManager = LinearLayoutManager(context)

        binding.chatsRv.also {
            it.layoutManager = layoutManager
            it.adapter = groupAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    private fun populateAdapter() {
        groupAdapter.add(Section().apply {
            val updatingGroup = Section()
            val updatableItems = ArrayList<ChatItem>()

            for (i in 1..24) {
                updatableItems.add(ChatItem("", "Chat#$i", "10:2${i % 10} AM"))
            }

            updatingGroup.update(updatableItems)
            add(updatingGroup)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}