package simplechat.main.adapters.base

import androidx.recyclerview.widget.RecyclerView
import simplechat.main.interfacies.BindAdapter

abstract class BaseAdapter<VH : RecyclerView.ViewHolder, T, K> : RecyclerView.Adapter<VH>(), BindAdapter<T, K>