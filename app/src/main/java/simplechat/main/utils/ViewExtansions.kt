package simplechat.main.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import simplechat.main.adapters.base.BaseAdapter
import simplechat.main.interfacies.BindAdapter

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

@BindingAdapter("app:adapter")
fun setRecyclerAdapter(recyclerView: View, adapter: BaseAdapter<*, *, *>) {
    if (recyclerView is RecyclerView) {
        recyclerView.adapter = adapter
    } else if (recyclerView is ViewPager2) {
        recyclerView.adapter = adapter
    }
}

@BindingAdapter("app:items")
fun <T> setRecyclerItems(recyclerView: View, items: List<T>?) {
    if (recyclerView is RecyclerView) {
        if (recyclerView.adapter is BindAdapter<*, *>) {
            (recyclerView.adapter as BindAdapter<T, *>).setItems(items)
        }
    } else if (recyclerView is ViewPager2) {
        if (recyclerView.adapter is BindAdapter<*, *>) {
            (recyclerView.adapter as BindAdapter<T, *>).setItems(items)
        }
    }

}

@BindingAdapter("app:visibility")
fun visibleIf(anyView: View, show: Boolean) {
    if (show) anyView.visible() else anyView.gone()
}


@BindingAdapter("app:listener")
fun <K> setListener(recyclerView: View, listener: K?) {
    if (recyclerView is RecyclerView) {
        if (recyclerView.adapter is BindAdapter<*, *>) {
            (recyclerView.adapter as BindAdapter<*, K>).setListener(listener)
        }
    } else if (recyclerView is ViewPager2) {
        if (recyclerView.adapter is BindAdapter<*, *>) {
            (recyclerView.adapter as BindAdapter<*, K>).setListener(listener)
        }
    }
}

