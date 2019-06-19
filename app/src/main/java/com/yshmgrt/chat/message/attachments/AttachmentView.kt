package com.yshmgrt.chat.message.attachments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import kotlinx.android.synthetic.main.attachment_background_card.view.*

class AttachmentView(context: Context, attachment : Attachment) : LinearLayout(context){
    init {
        LayoutInflater.from(context).inflate(R.layout.attachment_background_card, this, true)
        val insideView = IAttachment.create(attachment)?.getPreview(context)!!
        this.constraint.addView(insideView)
        insideView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topToTop = ConstraintSet.PARENT_ID
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
            bottomToBottom = ConstraintSet.PARENT_ID
            elevation = 0f
        }
        this.delete_attachment.bringToFront()
    }
}