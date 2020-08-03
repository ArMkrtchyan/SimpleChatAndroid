package simplechat.main.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import simplechat.main.R
import simplechat.main.adapters.OnChatClickListener
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.databinding.FragmentChatListBinding
import simplechat.main.fragments.base.BaseFragment
import simplechat.main.interfacies.OnFragmentActionListener
import simplechat.main.models.Chat
import simplechat.main.utils.DialogUtil
import simplechat.main.viewmodels.ChatListViewModel

class ChatListFragment : BaseFragment(), OnChatClickListener {

    private lateinit var dataBinding: FragmentChatListBinding
    private lateinit var viewModel: ChatListViewModel
    private var listener: OnFragmentActionListener? = null
    private val navController by lazy { findNavController() }
    private val chatOptionsDialogFragment by lazy { ChatOptionsDialogFragment(this) }
    private var chat: Chat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java].apply {
            setContext(requireContext())
            setLifeCycleOwner(this@ChatListFragment)
            getChats()
        }
        dataBinding = FragmentChatListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            chatListViewModel = viewModel
            onChatClickListener = this@ChatListFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.addNewChat.setOnClickListener {
            DialogUtil.addChatDialog(requireActivity(), object : DialogUtil.AddChatCallback {
                override fun addChat(chat: ChatEntity) {
                    viewModel.addChat(chat)
                    dataBinding.chatListRecycler.scrollToPosition(0)
                }

                override fun editChat(chat: ChatEntity) {

                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.setWhiteStatusBar()
    }

    override fun onChatClick(chat: Chat) {
        if (navController.currentDestination?.id == R.id.chatListFragment) navController.navigate(
            ChatListFragmentDirections.actionChatListFragmentToMessagesFragment(Gson().toJson(chat)))
    }

    override fun onChatLongClick(chat: Chat) {
        this.chat = chat
        chatOptionsDialogFragment.show(childFragmentManager, "ChatOptionsDialogFragment")
    }

    override fun deleteChat() {
        chatOptionsDialogFragment.dismiss()
        chat?.let {
            viewModel.deleteChat(ChatsMapper.chatToChatEntity(it))
        }
    }

    override fun editChat() {
        chatOptionsDialogFragment.dismiss()
        chat?.let {
            DialogUtil.editChatDialog(it, requireActivity(), object : DialogUtil.AddChatCallback {
                override fun addChat(chat: ChatEntity) {

                }

                override fun editChat(chat: ChatEntity) {
                    viewModel.updateChat(chat)
                    dataBinding.chatListRecycler.scrollToPosition(0)
                }
            })
        }
    }
}