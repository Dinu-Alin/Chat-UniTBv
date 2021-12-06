package com.lagar.chatunitbv.ui.fragments.chats

import Operations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.ChatsFragmentBinding
import com.lagar.chatunitbv.items.ChatItem
import com.lagar.chatunitbv.models.Chat
import com.lagar.chatunitbv.util.extensions.update
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import kotlinx.coroutines.*
import timber.log.Timber


class ChatsFragment : Fragment() {

    private var _binding: ChatsFragmentBinding? = null

    private lateinit var groupAdapter: GroupieAdapter

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChatsFragmentBinding.inflate(layoutInflater)

        groupAdapter = GroupieAdapter()

//        createTempChats()

        populateAdapter()

        val layoutManager = LinearLayoutManager(context)

        binding.chatsRv.also {
            it.layoutManager = layoutManager
            it.adapter = groupAdapter
        }
    }

    var updatableItems = ArrayList<ChatItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    private fun createTempChats() {
        for (i in 1..24) {
            Operations.database.collection("chats")
                .add(Chat("", "Chat#$i", "10:2${i % 10} AM"))
                .addOnSuccessListener {
                    Timber.d("DocumentSnapshot added with ID: ${it.id}")
                }
                .addOnFailureListener {
                    Timber.w("Error adding document. Error: $it")
                }
        }
    }

    private fun populateAdapter() {
        Operations.database.collection("chats")
            .orderBy("name", Query.Direction.DESCENDING)
            .addSnapshotListener { chatsDocs, e ->
                if (e != null) {
                    Timber.w("Listen failed: $e")
                    return@addSnapshotListener
                }
                val updatingGroup = Section()
                updatableItems.clear()

                for (chat in chatsDocs!!) {
                    updatableItems.add(ChatItem(chat.toObject()))
                }

                updatingGroup.update(updatableItems)

                groupAdapter.update(updatingGroup)

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
