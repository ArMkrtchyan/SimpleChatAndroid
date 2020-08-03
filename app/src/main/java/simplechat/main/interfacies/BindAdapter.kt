package simplechat.main.interfacies

interface BindAdapter<T, K> {
    fun setItems(items: List<T>?)
    fun setListener(listener: K?)
}