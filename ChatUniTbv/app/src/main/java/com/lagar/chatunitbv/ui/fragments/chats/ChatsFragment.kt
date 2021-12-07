package com.lagar.chatunitbv.ui.fragments.chats

import Operations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.ChatsFragmentBinding
import com.lagar.chatunitbv.ui.items.ChatItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*


class ChatsFragment : Fragment() {

    private lateinit var fastAdapter: FastAdapter<ChatItem>
    private var _binding: ChatsFragmentBinding? = null
    private lateinit var itemAdapter: ItemAdapter<ChatItem>

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChatsFragmentBinding.inflate(layoutInflater)

        itemAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(itemAdapter)

        fastAdapter.onClickListener = { view, adapter, item, position ->

            Toast.makeText(context, "Clicked ${item.chat?.id}", Toast.LENGTH_SHORT).show()
//
//            Operations.db
//                .collection("chats")
//                .document(item.chat!!.id!!)
//                .update("time", FieldValue.serverTimestamp())

            false
        }
        binding.chatsRv.adapter = fastAdapter

        attachListener()

        val layoutManager = LinearLayoutManager(context)

        binding.chatsRv.also {
            it.layoutManager = layoutManager
        }
    }

    private fun attachListener() {
        Operations.db.collection("chats")
            .orderBy("name", Query.Direction.DESCENDING)
            .addSnapshotListener { chatsDocsChanged, e ->
                if (e != null) {
                    Timber.w("Chats listen error: $e")
                    return@addSnapshotListener
                }

                for (chatDoc in chatsDocsChanged!!.documentChanges) {
                    when (chatDoc.type) {
                        DocumentChange.Type.ADDED -> {
                            Timber.d("Chat document added: ${chatDoc.document.data}")
                            itemAdapter.add(chatDoc.newIndex, ChatItem(chatDoc.document.toObject()))
                        }
                        DocumentChange.Type.REMOVED -> {
                            Timber.d("Chat document removed: ${chatDoc.document.data}")
                            itemAdapter.remove(chatDoc.oldIndex)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            Timber.d("Chat document modified: ${chatDoc.document.data}")
                            if (chatDoc.newIndex != chatDoc.oldIndex) {

                                itemAdapter.remove(chatDoc.oldIndex)

                                itemAdapter.add(
                                    chatDoc.newIndex,
                                    ChatItem(chatDoc.document.toObject())
                                )
                            } else {
                                itemAdapter[chatDoc.newIndex] =
                                    ChatItem(chatDoc.document.toObject())
                            }

                        }
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
