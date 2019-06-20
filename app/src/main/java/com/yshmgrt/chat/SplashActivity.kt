package com.yshmgrt.chat

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val int = Intent(this,MainActivity::class.java)
        int.action = intent.action
        int.type = intent.type
        int.putExtras(intent)
        startActivity(int)
        finish()
    }
}