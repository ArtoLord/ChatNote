package com.yshmgrt.chat.message.attachments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.View
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import kotlinx.android.synthetic.main.image_attachment.view.*
import java.io.File
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R


@GlideModule
class AppGlideModule : AppGlideModule()


class ImageAttachment( attachment: Attachment) :IAttachment{
    override fun onSended(context: Context) {
    }

    private val image:Image = Klaxon().parse<Image>(attachment.link)!!


    override fun getMessageView(context:Context): View {
        val view = ImageView(context)
        GlideApp
            .with(context)
            .load(File(image.path))
            .placeholder(context.getDrawable(R.drawable.placeholder))
            .into(view.source)
        return view
    }
    override fun getPreview(context:Context): View {
        val a = com.yshmgrt.chat.message.attachments.AppGlideModule()
        val view = ImageView(context)
        GlideApp
            .with(context)
            .load(File(image.path))
            .override(300,300)
            .centerCrop()
            .into(view.source)
        return view
    }

    companion object{
        fun sendIntentToPick(activity: Activity){
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MainActivity.PERMISSION_REQUEST)
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                activity.startActivityForResult(intent, MainActivity.PIC_IMAGE_REQUEST)
            }

        }
    }


}