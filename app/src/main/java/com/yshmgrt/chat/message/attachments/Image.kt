package com.yshmgrt.chat.message.attachments

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.image_attachment.view.*

class Image:LinearLayout{
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int)
    : super(context, attrs, defStyleAttr, defStyleRes)
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.image_attachment, this, true)
    }
    fun setImage(drawable: Drawable?){
        source.setImageDrawable(drawable)
    }
}