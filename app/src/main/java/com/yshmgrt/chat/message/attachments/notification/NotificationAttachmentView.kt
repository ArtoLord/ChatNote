package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.notification_attachment.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationAttachmentView(context : Context) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.notification_attachment, this, true)
    }

    fun setContent(notification: Notification) {
        val c = GregorianCalendar()
        c.timeInMillis = notification.time
        time.text = SimpleDateFormat("HH:mm").format(c.time)
        date.text = SimpleDateFormat("dd MM, yyyy").format(c.time)
    }
}