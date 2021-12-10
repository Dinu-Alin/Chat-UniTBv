package com.lagar.chatunitbv.ui.fragments.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lagar.chatunitbv.databinding.FragmentMessagesBinding
import com.lagar.chatunitbv.ui.items.SentMessageItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

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

        attachListener()

        binding.messagesRv.layoutManager = LinearLayoutManager(context)
    }

    private fun attachListener() {
//        TODO("Not yet implemented")
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