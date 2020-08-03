package simplechat.main.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import simplechat.main.models.Chat
import java.util.*

class ChatDiffUtilCallback(val data: ArrayList<Chat>, private val oldData: ArrayList<Chat>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[newItemPosition].id == oldData[oldItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return data.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[newItemPosition].userName == oldData[oldItemPosition].userName && data[newItemPosition].lastMessageDate == oldData[oldItemPosition].lastMessageDate
    }

}
