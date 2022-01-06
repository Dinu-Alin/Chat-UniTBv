package com.lagar.chatunitbv.ui.fragments.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.FragmentMessagesBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.Chat
import com.lagar.chatunitbv.models.Message
import com.lagar.chatunitbv.models.User
import com.lagar.chatunitbv.preferences.UserSharedPreferencesRepository
import com.lagar.chatunitbv.ui.activities.MessagesActivity
import com.lagar.chatunitbv.ui.items.ReceivedMessageItem
import com.lagar.chatunitbv.ui.items.SentMessageItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import timber.log.Timber
import java.util.*

class MessagesFragment : Fragment() {

    private lateinit var chat: Chat
    private lateinit var user: User


    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter = GenericItemAdapter()
    private lateinit var fastAdapter: GenericFastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chat = (activity as MessagesActivity).getChat()

        val sharedPreferences = UserSharedPreferencesRepository(
            this.requireActivity().getSharedPreferences("USER", AppCompatActivity.MODE_PRIVATE)
        )
        user = sharedPreferences.read()

        _binding = FragmentMessagesBinding.inflate(layoutInflater)

        fastAdapter = FastAdapter.with(itemAdapter)

        binding.messagesRv.adapter = fastAdapter
        binding.sendButton.setOnClickListener {
//            Toast.makeText(context, "Sent!${binding.newMessageEditText.text}", Toast.LENGTH_SHORT)
//                .show()
            if (binding.newMessageEditText.text.isNotEmpty()) {
                val chatRef = Operations.db.collection("chats").document(chat.id!!)
                // add message
                chatRef.collection("messages").add(
                    Message(
                        sender = user.name,
                        content = binding.newMessageEditText.text.toString(),
                        chatId = chat.id
                    )
                )

                // query last message, so we get the timestamp
                chatRef
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .limit(1)
                    .get()
                    .addOnCompleteListener {
                        val querySnapshot = it.result
                        if (!querySnapshot!!.isEmpty) {
                            val lastMessage = querySnapshot.documents[0].toObject<Message>()
                            // then we update the chat's timestamp
                            updateChatTimestamp(lastMessage!!.timestamp!!)
                        }
                    }

                binding.newMessageEditText.text.clear()
                if (itemAdapter.itemList.size() >= 1) {
                    binding.messagesRv.smoothScrollToPosition(itemAdapter.itemList.size() - 1)
                }
            }
        }
        attachListener()

        binding.messagesRv.layoutManager = LinearLayoutManager(context)
    }

    private fun updateChatTimestamp(timestamp: Date) {
        Operations.db
            .collection("chats")
            .document(chat.id!!)
            .update("timestamp", timestamp)
    }

    private fun attachListener() {
        Operations.db
            .collection("chats").document(chat.id!!)
            .collection("messages")
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

                            val message = messageDoc.document.toObject<Message>()
                            val messageItem = getMessageType(message)

                            itemAdapter.add(
                                messageDoc.newIndex,
                                messageItem
                            )
                        }
                        DocumentChange.Type.REMOVED -> {
                            Timber.d("Message document deleted: ${messageDoc.document.data}")
                            itemAdapter.remove(messageDoc.oldIndex)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val modifiedMessage = messageDoc.document.toObject<Message>()
                            val messageItem = getMessageType(modifiedMessage)

                            if (messageDoc.newIndex != messageDoc.oldIndex) {
                                itemAdapter.remove(messageDoc.oldIndex)
                                itemAdapter.add(
                                    messageDoc.newIndex,
                                    messageItem
                                )
                            } else {
                                val oldMessage =
                                    when (val oldMessageItemFromAdapter =
                                        itemAdapter.itemList[messageDoc.newIndex]) {
                                        is SentMessageItem -> oldMessageItemFromAdapter.message
                                        else -> (oldMessageItemFromAdapter as ReceivedMessageItem).message
                                    }
                                if (oldMessage != modifiedMessage) {
                                    itemAdapter[messageDoc.newIndex] =
                                        messageItem
                                }
                            }
                        }
                    }
                }
                // scroll to last message
                if (itemAdapter.itemList.size() >= 1) {
                    binding.messagesRv.scrollToPosition(itemAdapter.itemList.size() - 1)
                }
            }
    }

    private fun getMessageType(message: Message) = when (message.sender) {
        user.name -> SentMessageItem(message)
        else -> ReceivedMessageItem(message)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(activity, chat.id, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}