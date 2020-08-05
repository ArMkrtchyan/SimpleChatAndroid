package simplechat.main.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import simplechat.main.databinding.FragmentMessagesBinding
import simplechat.main.fragments.base.BaseFragment
import simplechat.main.interfacies.MessageOptionsCallback
import simplechat.main.interfacies.OnFragmentActionListener
import simplechat.main.models.Chat
import simplechat.main.models.Message
import simplechat.main.utils.Utils
import simplechat.main.viewmodels.MessagesViewModel
import java.util.*


class MessagesFragment : BaseFragment(), MessageOptionsCallback {

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val FILES_REQUEST_CODE = 2
        private const val CAMERA_REQUEST_CODE = 3
        private const val GALLERY_PERMISSION_REQUEST_CODE = 4
        private const val FILES_PERMISSION_REQUEST_CODE = 5
        private const val CAMERA_PERMISSION_REQUEST_CODE = 6
    }

    private lateinit var dataBinding: FragmentMessagesBinding
    private lateinit var viewModel: MessagesViewModel
    private var listener: OnFragmentActionListener? = null
    private val navController by lazy { findNavController() }
    private var chat: Chat? = null
    private val messageOptionsDialogFragment by lazy { MessageOptionsDialogFragment(this) }
    private var isForSend = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java].apply {
            setContext(requireContext())
            setLifeCycleOwner(this@MessagesFragment)
        }
        dataBinding = FragmentMessagesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            messageViewModel = viewModel
        }
        arguments?.let { bundle ->
            chat = Gson().fromJson(bundle.getString("chat"), Chat::class.java)
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.let { it ->
                dataBinding.toolbar.title = it.userName
                viewModel.getMessages(chat ?: Chat(0, "", "", false, ""))
            }
        }
        return dataBinding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentActionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        listener?.setWhiteStatusBar()
        setListeners()
    }

    override fun onStop() {
        super.onStop()
        removeListeners()
    }

    private fun setListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            hideKeyboard(requireActivity())
            listener?.onBackPress()
        }
        dataBinding.more.setOnClickListener {
            isForSend = true
            messageOptionsDialogFragment.show(childFragmentManager, "MessageOptions")
            hideKeyboard(requireActivity())
        }
        dataBinding.send.setOnClickListener {
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.lastMessageDate = Utils.dateToStringWithTimeZone(Date()) ?: ""
            if (dataBinding.inputField.text.toString().isNotEmpty()) viewModel.addMessage(
                Message(0, chat?.id ?: 0, dataBinding.inputField.text.toString().trim(), Utils.dateToStringWithTimeZone(Date()) ?: "",
                    0, 1, false, "", "", ""), chat!!).observe(this, androidx.lifecycle.Observer { success ->
                success?.let {
                    if (it) {
                        dataBinding.inputField.setText("")
                        dataBinding.messagesRecycler.scrollToPosition(0)
                    }
                }
            })
        }

        dataBinding.moreReceived.setOnClickListener {
            isForSend = false
            messageOptionsDialogFragment.show(childFragmentManager, "MessageOptions")
            hideKeyboard(requireActivity())
        }
        dataBinding.sendReceived.setOnClickListener {
            Log.i("ChatListTag", "chat: " + chat.toString())
            chat?.lastMessageDate = Utils.dateToStringWithTimeZone(Date()) ?: ""
            if (dataBinding.inputFieldReceived.text.toString().isNotEmpty()) viewModel.addMessage(
                Message(0, chat?.id ?: 0, dataBinding.inputFieldReceived.text.toString().trim(),
                    Utils.dateToStringWithTimeZone(Date()) ?: "", 0, 2, false, "", "", ""), chat!!)
                .observe(this, androidx.lifecycle.Observer { success ->
                    success?.let {
                        if (it) {
                            dataBinding.inputFieldReceived.setText("")
                            dataBinding.messagesRecycler.scrollToPosition(0)
                        }
                    }
                })
        }
    }

    private fun removeListeners() {
        dataBinding.toolbar.setNavigationOnClickListener(null)
        dataBinding.more.setOnClickListener(null)
        dataBinding.send.setOnClickListener(null)
    }

    override fun openGallery() {
        messageOptionsDialogFragment.dismiss()
        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_REQUEST_CODE)
        } else {
            startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }, "Select Picture"), GALLERY_REQUEST_CODE)

        }
    }

    override fun openFiles() {
        messageOptionsDialogFragment.dismiss()
        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                FILES_PERMISSION_REQUEST_CODE)
        } else {
            startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
            }, "Select File"), FILES_REQUEST_CODE)
        }
    }

    override fun openCamera() {
        messageOptionsDialogFragment.dismiss()
        if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startActivity(Intent("android.media.action.IMAGE_CAPTURE"))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        type = "image/*"
                    }, "Select Picture"), GALLERY_REQUEST_CODE)
                }
            }
            FILES_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        type = "*/*"
                    }, "Select File"), FILES_REQUEST_CODE)
                }
            }
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent("android.media.action.IMAGE_CAPTURE"))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            if (it.data != null) {
                                viewModel.addMessage(Message(0, chat?.id ?: 0, "", Utils.dateToStringWithTimeZone(Date()) ?: "", 0,
                                    if (isForSend) 3 else 4, false, "", "", it.data!!.toString()), chat!!)
                                    .observe(this, androidx.lifecycle.Observer { success ->
                                        success?.let {
                                            if (it) {
                                                dataBinding.inputFieldReceived.setText("")
                                                dataBinding.messagesRecycler.scrollToPosition(0)
                                            }
                                        }
                                    })
                            }
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
            FILES_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            if (it.data != null) {
                                viewModel.addMessage(Message(0, chat?.id ?: 0, "", Utils.dateToStringWithTimeZone(Date()) ?: "", 0,
                                    if (isForSend) 5 else 6, false, "", "", it.data!!.toString()), chat!!)
                                    .observe(this, androidx.lifecycle.Observer { success ->
                                        success?.let {
                                            if (it) {
                                                dataBinding.inputFieldReceived.setText("")
                                                dataBinding.messagesRecycler.scrollToPosition(0)

                                            }
                                        }
                                    })
                            }
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
            CAMERA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            if (it.data != null) {
                                val mImageUri: Uri = it.data!!
                                Log.v("LOG_TAG", "Camera uri:$mImageUri")
                            }
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
        }
    }
}