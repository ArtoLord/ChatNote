package com.yshmgrt.chat.message.attachments.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.message.attachments.IAttachment
import java.util.*

class NotificationAttachment(val attachment: Attachment) : IAttachment {
    override fun onSended(context: Context) {
        val controller = Controller(context)
        controller.getMessageById(attachment.parentId){
            val notification = Klaxon().parse<Notification>(attachment.link)
            controller.resendMessage(attachment.parentId,notification!!.time)
            sendBroadcast(context,it._id,maxOf(notification.time-Date().time, 0))
            Log.d("MyDebug",(notification.time-Date().time).toString())
        }
    }

    fun sendBroadcast(context: Context, _id:Long, time:Long){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,NotificationsBrodcast::class.java)
        intent.putExtra("messageId",_id)
        intent.action = "ChatNoteBrodcast"
        val pIntent = PendingIntent.getBroadcast(context,0,intent,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+time,pIntent)
    }

    private val notification = Klaxon().parse<Notification>(attachment.link)!!

    override fun getMessageView(context : Context) = NotificationAttachmentView(context).apply { setContent(notification) }

    override fun getPreview(context : Context) = NotificationAttachmentView(context, true).apply { setContent(notification) }

    companion object {
        fun create(notification: Notification) : Attachment {
            return Attachment(
                1,
                Attachment.EVENT_TYPE.toString(),
                Klaxon().toJsonString(notification),
                1
            )
        }
    }
}