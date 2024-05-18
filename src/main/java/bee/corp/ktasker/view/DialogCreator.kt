package bee.corp.ktasker.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

class DialogCreator {
    fun createViewedDialog(
        view: View,
        c: Context,
        title: String,
        positiveButtonClickListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(c)
        builder.setView(view)
        builder.setTitle(title)
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setPositiveButton("OK", positiveButtonClickListener)
        return builder.create()
    }
    fun createSimpleDialog(
        title: String,
        c: Context,
        message: String,
        positiveButtonClickListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(c)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setPositiveButton("OK", positiveButtonClickListener)
        return builder.create()
    }
}