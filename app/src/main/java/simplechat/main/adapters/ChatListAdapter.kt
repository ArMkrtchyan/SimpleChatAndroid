package simplechat.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import simplechat.main.adapters.base.BaseAdapter
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.adapters.diffutils.ChatDiffUtilCallback
import simplechat.main.adapters.viewholders.ChatListViewHolder
import simplechat.main.databinding.ChatListItemBinding
import simplechat.main.models.Chat

class ChatListAdapter : BaseAdapter<BaseViewHolder<Chat, OnItemClickListener<Chat>>, Chat, OnItemClickListener<Chat>>() {

    private val data = ArrayList<Chat>()
    private var onChatClickListener: OnItemClickListener<Chat>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Chat, OnItemClickListener<Chat>> {
        return ChatListViewHolder(ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<Chat, OnItemClickListener<Chat>>, position: Int) {
        holder.bind(data[position], onChatClickListener)
    }

    override fun setItems(items: List<Chat>?) {
        items?.let {
            val oldData = ArrayList<Chat>().apply { addAll(data) }
            data.clear()
            data.addAll(it)
            val callback = ChatDiffUtilCallback(data, oldData)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            Log.i("ChatItemsTag", data.toString())
        }
    }

    override fun setListener(listener: OnItemClickListener<Chat>?) {
        onChatClickListener = listener
    }

}