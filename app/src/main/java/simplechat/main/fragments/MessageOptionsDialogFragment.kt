package simplechat.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import simplechat.main.R
import simplechat.main.databinding.FragmentMessgaeOptionsDialogBinding
import simplechat.main.interfacies.MessageOptionsCallback

class MessageOptionsDialogFragment(private val messageOptionsCallback: MessageOptionsCallback) : BottomSheetDialogFragment() {

    private lateinit var dataBinding: FragmentMessgaeOptionsDialogBinding


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentMessgaeOptionsDialogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onGalleryClick = View.OnClickListener {
                messageOptionsCallback.openGallery()
            }
            onFilesClick = View.OnClickListener {
                messageOptionsCallback.openFiles()
            }
            onCameraClick = View.OnClickListener {
                messageOptionsCallback.openCamera()
            }
        }
        return dataBinding.root
    }

}