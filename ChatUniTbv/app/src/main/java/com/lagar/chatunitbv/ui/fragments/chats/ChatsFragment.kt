package com.lagar.chatunitbv.ui.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.ChatsFragmentBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.ui.items.ChatItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*


class ChatsFragment : Fragment() {

    private var _binding: ChatsFragmentBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter = ItemAdapter<ChatItem>()
    private lateinit var fastAdapter: FastAdapter<ChatItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChatsFragmentBinding.inflate(layoutInflater)

        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->
            // view, adapter, item, position =>
//            Toast.makeText(context, "Clicked ${item.chat?.id}", Toast.LENGTH_SHORT).show()
//
//            Operations.db
//                .collection("chats")
//                .document(item.chat!!.id!!)
//                .update("time", FieldValue.serverTimestamp())

            val directions = ChatsFragmentDirections.actionNavigationChatsToMessagesActivity()
            findNavController().navigate(directions)
//            activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

            false
        }
        binding.chatsRv.adapter = fastAdapter

        attachListener()

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
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
