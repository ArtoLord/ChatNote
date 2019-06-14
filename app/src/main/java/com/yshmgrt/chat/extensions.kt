package com.yshmgrt.chat

import android.content.Context
import com.yshmgrt.chat.data_base.Helper.Helper

val Context.database: Helper
    get() = Helper.getInstance(applicationContext)
