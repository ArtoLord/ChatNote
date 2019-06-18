package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.notification_attachment.view.*
import java.util.*

class NotificationAttachmentView(context : Context) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.notification_attachment, this, true)
    }

    fun setContent(notification: Notification) {
        val c = GregorianCalendar()
        c.time = notification.time
        date.text = "${c[Calendar.DAY_OF_MONTH]} ${c[Calendar.MONTH]}, ${c[Calendar.YEAR]}"
        time.text = "${c[Calendar.HOUR_OF_DAY]} ${c[Calendar.MINUTE]}"
    }
}