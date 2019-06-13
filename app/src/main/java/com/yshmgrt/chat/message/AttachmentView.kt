package com.yshmgrt.chat.message

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.image_attachment.view.*
import kotlinx.android.synthetic.main.tag_view.view.*

class AttachmentView : LinearLayout {
    val NO_ATTACHMENT = 0
    val IMAGE = 1
    val DOC = 2
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
    fun setImage(drawable: Drawable){
        LayoutInflater.from(context)
            .inflate(R.layout.image_attachment, this, true)
        source.setImageDrawable(drawable)
    }
}