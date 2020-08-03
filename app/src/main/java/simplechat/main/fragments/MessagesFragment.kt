package simplechat.main.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import simplechat.main.database.entity.MessageEntity
import simplechat.main.databinding.FragmentMessagesBinding
import simplechat.main.fragments.base.BaseFragment
import simplechat.main.interfacies.OnFragmentActionListener
import simplechat.main.models.Chat
import simplechat.main.utils.Utils
import simplechat.main.viewmodels.MessagesViewModel
import java.util.*

class MessagesFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentMessagesBinding
    private lateinit var viewModel: MessagesViewModel
    private var listener: OnFragmentActionListener? = null
    private val navController by lazy { findNavController() }
    private var chat: Chat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java].apply {
            setContext(requireContext())
            setLifeCycleOwner(this@MessagesFragment)
        }
        dataBinding = FragmentMessagesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            messageViewModel = viewModel
        }
        arguments?.let { bundle ->
            chat = Gson().fromJson(bundle.getString("chat"), Chat::class.java)
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.let { it ->
                dataBinding.toolbar.title = it.userName
                viewModel.getMessages(chat ?: Chat(0, "", "", false, ""))
            }
        }
        return dataBinding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentActionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        listener?.setWhiteStatusBar()
        setListeners()
    }

    override fun onStop() {
        super.onStop()
        removeListeners()
    }

    private fun setListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            hideKeyboard(requireActivity())
            listener?.onBackPress()
        }
        dataBinding.more.setOnClickListener {
            showShortToast("More clicked.")
            hideKeyboard(requireActivity())
        }
        dataBinding.send.setOnClickListener {
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.lastMessageDate = Utils.dateToStringWithTimeZone(Date()) ?: ""
            if (dataBinding.inputField.text.toString().isNotEmpty()) viewModel.addMessage(
                MessageEntity(0, chat?.id ?: 0, dataBinding.inputField.text.toString().trim(),
                    Utils.dateToStringWithTimeZone(Date()) ?: "", 0, 1), chat!!).observe(this, androidx.lifecycle.Observer { success ->
                success?.let {
                    if (it) {
                        dataBinding.inputField.setText("")
                        dataBinding.messagesRecycler.scrollToPosition(0)
                    }
                }
            })
        }

        dataBinding.moreReceived.setOnClickListener {
            showShortToast("More clicked.")
            hideKeyboard(requireActivity())
        }
        dataBinding.sendReceived.setOnClickListener {
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.lastMessageDate = Utils.dateToStringWithTimeZone(Date()) ?: ""
            if (dataBinding.inputFieldReceived.text.toString().isNotEmpty()) viewModel.addMessage(
                MessageEntity(0, chat?.id ?: 0, dataBinding.inputFieldReceived.text.toString().trim(),
                    Utils.dateToStringWithTimeZone(Date()) ?: "", 0, 2), chat!!).observe(this, androidx.lifecycle.Observer { success ->
                success?.let {
                    if (it) {
                        dataBinding.inputFieldReceived.setText("")
                        dataBinding.messagesRecycler.scrollToPosition(0)
                    }
                }
            })
        }
    }

    private fun removeListeners() {
        dataBinding.toolbar.setNavigationOnClickListener(null)
        dataBinding.more.setOnClickListener(null)
        dataBinding.send.setOnClickListener(null)
    }

}