package com.lagar.chatunitbv.fragments.chats

import Operations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.ChatFragmentBinding
import com.lagar.chatunitbv.items.ChatItem
import com.lagar.chatunitbv.models.Chat
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ChatFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null

    private lateinit var groupAdapter: GroupieAdapter

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChatFragmentBinding.inflate(layoutInflater)

        groupAdapter = GroupieAdapter()

//        createTempChats()

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
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { chatsDocs ->
                groupAdapter.add(Section().apply {
                    val updatingGroup = Section()
                    val updatableItems = ArrayList<ChatItem>()
                    chatsDocs.documents.forEach {
                        val chat = it.toObject<Chat>()
                        updatableItems.add(ChatItem(chat))
                    }

                    updatingGroup.update(updatableItems)
                    add(updatingGroup)
                })
            }
            .addOnFailureListener {
                Timber.w("Error getting document. Error: $it")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}