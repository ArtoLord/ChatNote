package com.yshmgrt.chat.message.attachments.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.SplashActivity
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Message

class NotificationsBrodcast():BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val controller = Controller(context)
        val _id = intent.extras["messageId"].toString().toLong()
        controller.getMessageById(_id){
            sendNotif(context,it)
        }
    }

    private fun sendNotif(context: Context, message: Message){
        Log.d("ChatNote", message._id.toString())
        val notificManage = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, SplashActivity::class.java)
        intent.action = MainActivity.NOTIFICATION_CLICKED.toString()
        intent.putExtra("messageId",message._id)
        val pendingIntent = PendingIntent.getActivity(context, MainActivity.NOTIFICATION_CLICKED, intent,PendingIntent.FLAG_ONE_SHOT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChatNote"
            val descriptionText = "ChatNote notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificManage.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_event_notification)
                .setContentTitle("ChatNote notification")
                .setContentText(message.text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(message._id.toInt(), builder.build())
            }

        }
        else{
            val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.placeholder)
                .setContentTitle("ChatNote notification")
                .setContentText(message.text)
                .setSmallIcon(R.drawable.ic_event_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(message._id.toInt(), builder.build())
            }
        }


    }
}