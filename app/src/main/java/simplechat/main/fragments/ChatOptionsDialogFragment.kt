package simplechat.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import simplechat.main.R
import simplechat.main.adapters.OnChatClickListener
import simplechat.main.databinding.FragmentChatOptionsDialogBinding

class ChatOptionsDialogFragment(private val chatClickListener: OnChatClickListener) : BottomSheetDialogFragment() {

    private lateinit var dataBinding: FragmentChatOptionsDialogBinding


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentChatOptionsDialogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onEditClick = View.OnClickListener {
                chatClickListener.editChat()
            }
            onDeleteClick = View.OnClickListener {
                chatClickListener.deleteChat()
            }
        }
        return dataBinding.root
    }

}