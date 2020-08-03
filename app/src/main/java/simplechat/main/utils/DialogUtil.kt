package simplechat.main.utils

import android.app.Activity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import simplechat.main.R
import simplechat.main.database.entity.ChatEntity
import simplechat.main.databinding.DialogAddChatBinding
import simplechat.main.models.Chat
import java.util.*

object DialogUtil {
    fun addChatDialog(context: Activity, addChatCallback: AddChatCallback?) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogBinding = DialogAddChatBinding.inflate(LayoutInflater.from(context), null, false)
        dialogBuilder.setView(dialogBinding.root)
        dialogBuilder.setCancelable(true)
        dialogBinding.inputField.requestFocus()
        val alertDialog = dialogBuilder.create()
        dialogBinding.confirm.setOnClickListener {
            if (dialogBinding.inputField.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter chat name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            alertDialog.dismiss()
            addChatCallback?.addChat(
                ChatEntity(0, dialogBinding.inputField.text.toString(), "", false, Utils.dateToStringWithTimeZone(Date()) ?: ""))
        }
        dialogBinding.cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
        alertDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        alertDialog.show()
        Utils.showKeyboard(context, dialogBinding.inputField)
    }

    fun editChatDialog(chat: Chat, context: Activity, addChatCallback: AddChatCallback?) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogBinding = DialogAddChatBinding.inflate(LayoutInflater.from(context), null, false)
        dialogBuilder.setView(dialogBinding.root)
        dialogBuilder.setCancelable(true)
        dialogBinding.inputField.requestFocus()
        dialogBinding.inputField.setText(chat.userName)
        dialogBinding.inputField.setSelection(chat.userName.length)
        Utils.showKeyboard(context, dialogBinding.inputField)
        dialogBinding.title.text = "Edit chat name"
        val alertDialog = dialogBuilder.create()
        dialogBinding.confirm.setOnClickListener {
            if (dialogBinding.inputField.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter chat name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            alertDialog.dismiss()
            addChatCallback?.editChat(
                ChatEntity(chat.id, dialogBinding.inputField.text.toString(), chat.userPhoto, chat.isNewMessageContain,
                    Utils.dateToStringWithTimeZone(Date()) ?: ""))
        }
        dialogBinding.cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
        alertDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        alertDialog.show()
    }

    interface AddChatCallback {
        fun addChat(chat: ChatEntity)
        fun editChat(chat: ChatEntity)
    }
}