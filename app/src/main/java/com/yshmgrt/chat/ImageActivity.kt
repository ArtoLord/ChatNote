package com.yshmgrt.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.yshmgrt.chat.message.attachments.images.GlideApp
import kotlinx.android.synthetic.main.image_activity.*
import kotlinx.android.synthetic.main.image_attachment.view.*
import java.io.File

class ImageActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.image_activity)
        val path = intent.extras["ImageLink"].toString()
        GlideApp
            .with(applicationContext)
            .load(File(path))
            .fitCenter()
            .into(image_holder)


    }
}