package com.lagar.chatunitbv.ui.fragments.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.FragmentMessagesBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.Message
import com.lagar.chatunitbv.ui.items.SentMessageItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import timber.log.Timber
import java.util.*

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter = ItemAdapter<SentMessageItem>()
    private lateinit var fastAdapter: FastAdapter<SentMessageItem>

    private var lastClicked: SentMessageItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = FragmentMessagesBinding.inflate(layoutInflater)

        fastAdapter = FastAdapter.with(itemAdapter)

        fastAdapter.onClickListener = { _, _, item, _ ->
            // view, adapter, item, position =>

            if (lastClicked != null) {
                lastClicked!!.viewBinding.sentMessageDate.visibility = View.GONE
            }
            item.viewBinding.sentMessageDate.visibility = View.VISIBLE
            lastClicked = item

            false
        }

        binding.messagesRv.adapter = fastAdapter
        binding.sendButton.setOnClickListener {
//            Toast.makeText(context, "Sent!${binding.newMessageEditText.text}", Toast.LENGTH_SHORT)
//                .show()
            if (binding.newMessageEditText.text.isNotEmpty()) {
                Operations.db.collection("messages").add(
                    Message(
                        id = UUID.randomUUID().toString() + "_test",
                        sender = "User",
                        content = binding.newMessageEditText.text.toString()
                    )
                ).addOnSuccessListener {
                    binding.newMessageEditText.text.clear()
                    binding.messagesRv.smoothScrollToPosition(itemAdapter.itemList.size() - 1)
                }
            }
        }
        attachListener()

        binding.messagesRv.layoutManager = LinearLayoutManager(context)
    }

    private fun attachListener() {
        Operations.db.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { messagesDocs, e ->
                if (e != null) {
                    Timber.w("Messages listen error: $e")
                    return@addSnapshotListener
                }

                for (messageDoc in messagesDocs!!.documentChanges) {
                    when (messageDoc.type) {
                        DocumentChange.Type.ADDED -> {
                            Timber.d("Message document added: ${messageDoc.document.data}")
                            itemAdapter.add(
                                messageDoc.newIndex,
                                SentMessageItem(messageDoc.document.toObject())
                            )
                        }
                        DocumentChange.Type.REMOVED -> {
                            Timber.d("Message document deleted: ${messageDoc.document.data}")
                            itemAdapter.remove(messageDoc.oldIndex)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val modifiedMessage = messageDoc.document.toObject<Message>()
                            if (messageDoc.newIndex != messageDoc.oldIndex) {
                                itemAdapter.remove(messageDoc.oldIndex)
                                itemAdapter.add(
                                    messageDoc.newIndex,
                                    SentMessageItem(modifiedMessage)
                                )
                            } else {
                                val oldMessage = itemAdapter.itemList[messageDoc.newIndex]?.message
                                if (oldMessage != modifiedMessage) {
                                    itemAdapter[messageDoc.newIndex] =
                                        SentMessageItem(modifiedMessage)
                                }

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