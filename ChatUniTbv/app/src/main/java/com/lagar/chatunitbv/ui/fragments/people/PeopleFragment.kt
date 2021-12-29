package com.lagar.chatunitbv.ui.fragments.people

import Operations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.lagar.chatunitbv.databinding.PeopleFragmentBinding
import com.lagar.chatunitbv.ui.fragments.chats.ChatsFragmentDirections
import com.lagar.chatunitbv.ui.items.PeopleItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ItemFilterListener
import timber.log.Timber
import java.util.*

class PeopleFragment : Fragment(), ItemFilterListener<PeopleItem> {
    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter = ItemAdapter<PeopleItem>()
    private lateinit var fastAdapter: FastAdapter<PeopleItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = PeopleFragmentBinding.inflate(layoutInflater)

        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->
            Toast.makeText(context, "Clicked ${item.people?.name}", Toast.LENGTH_SHORT).show()

            Operations.db
                .collection("chats")
                .document(item.people!!.id!!)
                .update("time", FieldValue.serverTimestamp())

            val directions = ChatsFragmentDirections.actionNavigationChatsToMessagesActivity()
            findNavController().navigate(directions)
//            activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

            false
        }
        binding.peopleRv.adapter = fastAdapter

        attachListener()
        itemAdapter.itemFilter.filterPredicate = {item: PeopleItem, constraint: CharSequence? ->
            item.people?.name.toString().lowercase(Locale.getDefault())
                .contains(
                    constraint.toString().lowercase(
                        Locale.getDefault()
                    )
                )
        }
        itemAdapter.itemFilter.itemFilterListener = this
        val searchView = binding.peopleSearch
        binding.peopleRv.layoutManager = LinearLayoutManager(context)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                itemAdapter.filter(s)
                return false
            }
        })
    }
    private fun attachListener() {
        Operations.db.collection("users")
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { chatsDocsChanged, e ->
                if (e != null) {
                    Timber.w("People listen error: $e")
                    return@addSnapshotListener
                }

                for (peopleDoc in chatsDocsChanged!!.documentChanges) {
                    when (peopleDoc.type) {
                        DocumentChange.Type.ADDED -> {
                            Timber.d("People document added: ${peopleDoc.document.data}")
                            itemAdapter.add(peopleDoc.newIndex, PeopleItem(peopleDoc.document.toObject()))
                        }
                        DocumentChange.Type.REMOVED -> {
                            Timber.d("People document removed: ${peopleDoc.document.data}")
                            itemAdapter.remove(peopleDoc.oldIndex)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            Timber.d("People document modified: ${peopleDoc.document.data}")
                            if (peopleDoc.newIndex != peopleDoc.oldIndex) {

                                itemAdapter.remove(peopleDoc.oldIndex)

                                itemAdapter.add(
                                    peopleDoc.newIndex,
                                    PeopleItem(peopleDoc.document.toObject())
                                )
                            } else {
                                itemAdapter[peopleDoc.newIndex] =
                                    PeopleItem(peopleDoc.document.toObject())
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
    ): View? {

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun itemsFiltered(constraint: CharSequence?, results: List<PeopleItem>?) {
        TODO("Not yet implemented")
    }

    override fun onReset() {
        TODO("Not yet implemented")
    }

}
