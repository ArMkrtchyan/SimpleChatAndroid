package simplechat.main.adapters.base

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T, K>(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T, onClickListener: K?)
}