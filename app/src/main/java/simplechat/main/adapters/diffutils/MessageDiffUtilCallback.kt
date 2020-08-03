package simplechat.main.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import simplechat.main.models.Message
import java.util.*

class MessageDiffUtilCallback(val data: ArrayList<Message>, val oldData: ArrayList<Message>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[newItemPosition].id == oldData[oldItemPosition].id
    }

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = data.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[newItemPosition].id == oldData[oldItemPosition].id
    }

}
