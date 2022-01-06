package com.lagar.chatunitbv.ui.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
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

    private lateinit var chatsListener: ListenerRegistration
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
        attachMessagesListener()

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
    }

    private fun attachMessagesListener() {
        Operations.db.collection("chats")
            .whereArrayContains("members", user.email!!)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { chatsDocs, e ->
                if (e != null) {
                    Timber.w("Chats listen error: $e")
                    return@addSnapshotListener
                }
                for (chatDoc in chatsDocs!!.documentChanges) {

                    chatDoc.document.reference.collection("messages")
                        .addSnapshotListener { messageDocsChanged, error ->
                            if (error != null) {
                                Timber.w("Chats>Messages listen error: $error")
                            }
                            val chat = chatDoc.document.toObject<Chat>()
                            val oldTimestamp = chat.timestamp
                            var mostRecentTimestamp = oldTimestamp

                            for (messageDoc in messageDocsChanged!!.documentChanges) {
                                val messageTimestamp =
                                    messageDoc.document.getTimestamp("timestamp")?.toDate()
                                if (messageTimestamp != null) {
                                    if (messageTimestamp.after(oldTimestamp)) {
                                        mostRecentTimestamp = messageTimestamp
                                    }
                                }
                            }

                            if (mostRecentTimestamp != oldTimestamp) {
                                chat.timestamp = mostRecentTimestamp
                                updateChatTimestamp(mostRecentTimestamp, chat)
                            }
                        }
                }
            }
    }

    private fun attachChatListener() {

        chatsListener = Operations.db.collection("chats")
            .whereArrayContains("members", user.email!!)
//            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { chatsDocsChanged, e ->
                if (e != null) {
                    Timber.w("Chats listen error: $e")
                    return@addSnapshotListener
                }

                for (chatDoc in chatsDocsChanged!!.documentChanges) {


                    when (chatDoc.type) {
                        // REACT TO ADD EVENT ON SERVER/LOCAL
                        DocumentChange.Type.ADDED -> {

                            Timber.d("Chat document added: ${chatDoc.document.data}")
                            val chat = chatDoc.document.toObject<Chat>()
                            var otherImage = ""
                            // ================================================================
                            if (chat.memberCount == 2) {
                                val otherUserEmail =
                                    chat.members!!.first { email -> email != user.email }
                                if (otherUserEmail != null) {
                                    otherImage = otherUserEmail
                                }

                                val reference =
                                    Operations.store.getReference("images/users/${otherUserEmail}.jpg")

                                reference.downloadUrl.addOnSuccessListener {
                                    chat.image = "images/users/${otherUserEmail}.jpg"
                                }
                                    .addOnFailureListener {
                                        chat.image = "images/avatar.jpg"
                                    }
                                Operations.db.collection("users").document(otherUserEmail!!).get()
                                    .addOnSuccessListener {
                                        chat.name = it.toObject<User>()?.name

                                        if (itemAdapter.itemList.size() > chatDoc.newIndex) {
                                            itemAdapter[chatDoc.newIndex] =
                                                ChatItem(chat, otherUserEmail)
                                        }
                                    }
                            }
                            // ================================================================


                            itemAdapter.add(chatDoc.newIndex, ChatItem(chat, otherImage))
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

                if (_binding != null) {
                    binding.chatsRv.smoothScrollToPosition(0)
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
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    itemAdapter.clear()
                    chatsListener.remove()
                    attachChatListener()
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
