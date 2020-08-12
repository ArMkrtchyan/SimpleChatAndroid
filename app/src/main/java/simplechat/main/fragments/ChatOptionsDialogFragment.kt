package simplechat.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import simplechat.main.R
import simplechat.main.adapters.OnItemClickListener
import simplechat.main.databinding.FragmentChatOptionsDialogBinding
import simplechat.main.models.Chat

class ChatOptionsDialogFragment(private val chatClickListener: OnItemClickListener<Chat>) : BottomSheetDialogFragment() {

    private lateinit var dataBinding: FragmentChatOptionsDialogBinding
    private var mView: View? = null
    private var mPosition: Int? = null
    private var mItem: Chat? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentChatOptionsDialogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onEditClick = View.OnClickListener {
                chatClickListener.editItem(mView!!, mPosition!!, mItem!!)
            }
            onDeleteClick = View.OnClickListener {
                chatClickListener.deleteItem(mView!!, mPosition!!, mItem!!)
            }
        }
        return dataBinding.root
    }

    fun setItem(view: View, position: Int, item: Chat) {
        mView = view
        mPosition = position
        mItem = item
    }

}