package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.yshmgrt.chat.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationAttachmentView(context : Context, isPreview : Boolean = false) : LinearLayout(context) {
    init {
        if (isPreview)
            LayoutInflater.from(context).inflate(R.layout.notification_attachment_preview, this, true)
        else
            LayoutInflater.from(context).inflate(R.layout.notification_attachment, this, true)
    }

    fun setContent(notification: Notification) {
        val c = GregorianCalendar()
        c.timeInMillis = notification.time
        findViewById<TextView>(R.id.time).text = SimpleDateFormat("HH:mm").format(c.time)
        findViewById<TextView>(R.id.date).text = SimpleDateFormat("dd MM, yyyy").format(c.time)
    }
}