package com.lagar.chatunitbv.ui.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.ChatsFragmentBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.Chat
import com.lagar.chatunitbv.models.User
import com.lagar.chatunitbv.preferences.UserSharedPreferencesRepository
import com.lagar.chatunitbv.ui.items.ChatItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*


class ChatsFragment : Fragment() {

    private lateinit var user: User
    private var _binding: ChatsFragmentBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter = ItemAdapter<ChatItem>()
    private lateinit var fastAdapter: FastAdapter<ChatItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = UserSharedPreferencesRepository(
            this.requireActivity().getSharedPreferences("USER", AppCompatActivity.MODE_PRIVATE)
        )
        user = sharedPreferences.read()

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

            val directions =
                ChatsFragmentDirections.actionNavigationChatsToMessagesActivity(item.chat!!)
            findNavController().navigate(directions)
//            activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

            false
        }
        binding.chatsRv.adapter = fastAdapter

        attachChatListener()
//        attachMessagesListener()

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
    }

//    private fun attachMessagesListener() {
//        Operations.db.collection("chats")
//    }

    private fun attachChatListener() {

//        val chats = Operations.db.collection("chats").get().addOnSuccessListener {
//            for(document in it){
//                document.reference.collection("messages").addSnapshotListener()
//            }
//        }

        Operations.db.collection("chats")
            .whereArrayContains("members", user.email!!)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { chatsDocsChanged, e ->
                if (e != null) {
                    Timber.w("Chats listen error: $e")
                    return@addSnapshotListener
                }

                for (chatDoc in chatsDocsChanged!!.documentChanges) {

                    chatDoc.document.reference.collection("messages")
                        .addSnapshotListener { messageDocsChanged, e ->
                            if (e != null) {
                                Timber.w("Chats>Messages listen error: $e")
                            }
                            val chat = chatDoc.document.toObject<Chat>()
                            val oldTimestamp = chat.timestamp
                            var mostRecentTimestamp = oldTimestamp

                            for (messageDoc in messageDocsChanged!!.documentChanges) {
                                val messageTimestamp =
                                    messageDoc.document.getTimestamp("timestamp")!!.toDate()
                                if (messageTimestamp.after(oldTimestamp)) {
                                    mostRecentTimestamp = messageTimestamp
                                }
                            }

                            if (mostRecentTimestamp != oldTimestamp) {
                                chat.timestamp = mostRecentTimestamp
                                updateChatTimestamp(mostRecentTimestamp, chat)
                            }
                        }

                    when (chatDoc.type) {
                        // REACT TO ADD EVENT ON SERVER/LOCAL
                        DocumentChange.Type.ADDED -> {

                            Timber.d("Chat document added: ${chatDoc.document.data}")
                            val chat = chatDoc.document.toObject<Chat>()

                            // ================================================================
                            if (chat.memberCount == 2) {
                                val otherUserEmail =
                                    chat.members!!.first { email -> email != user.email }

                                chat.image = "images/users/${otherUserEmail}.jpg"
                                Operations.db.collection("users").document(otherUserEmail!!).get()
                                    .addOnSuccessListener {
                                        chat.name = it.toObject<User>()!!.name
                                        itemAdapter[chatDoc.newIndex] = ChatItem(chat)
                                    }
                            }
                            // ================================================================


                            itemAdapter.add(chatDoc.newIndex, ChatItem(chat))
                        }
                        // REACT TO REMOVE EVENT ON SERVER/LOCAL
                        DocumentChange.Type.REMOVED -> {
                            Timber.d("Chat document removed: ${chatDoc.document.data}")
                            itemAdapter.remove(chatDoc.oldIndex)
                        }
                        // REACT TO MODIFY EVENT ON SERVER/LOCAL
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

    private fun updateChatTimestamp(newTimestamp: Date?, chat: Chat?) {
        Operations
            .db
            .collection("chats")
            .document(chat!!.id!!)
            .set(
                hashMapOf(
                    "timestamp" to newTimestamp
                ),
                SetOptions.merge()
            )
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
