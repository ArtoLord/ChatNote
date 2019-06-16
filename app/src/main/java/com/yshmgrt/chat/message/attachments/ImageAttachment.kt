package com.yshmgrt.chat.message.attachments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.View
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import kotlinx.android.synthetic.main.image_attachment.view.*
import java.io.File
import android.Manifest.permission
import android.Manifest.permission.READ_CONTACTS
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat



class ImageAttachment(val context: Context, attachment: Attachment) :IAttachment {
    private val image:Image = Klaxon().parse<Image>(attachment.link)!!


    override fun getMessageView(): View {
        val view = ImageView(context)
        Glide.with(context).load(File(image.path)).into(view.source)
        return view
    }

    override fun getPreview(): View {
        val view = ImageView(context)
        Glide.with(context).load(File(image.path)).into(view.source)
        return view
    }


}