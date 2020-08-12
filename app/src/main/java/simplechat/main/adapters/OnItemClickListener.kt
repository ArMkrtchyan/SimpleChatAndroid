package simplechat.main.adapters

import android.view.View

interface OnItemClickListener<K> {
    fun onItemClick(view: View, position: Int, item: K)
    fun onItemLongClick(view: View, position: Int, item: K)
    fun deleteItem(view: View, position: Int, item: K)
    fun editItem(view: View, position: Int, item: K)
}