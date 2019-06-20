package com.yshmgrt.chat

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yshmgrt.chat.view.BottomDrawerFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.bundleOf
import android.provider.DocumentsContract
import android.content.ContentUris
import android.content.pm.ActivityInfo
import android.os.Environment.getExternalStorageDirectory
import android.os.Build
import android.os.Environment


class MainActivity : AppCompatActivity() {

    lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.elevation = 24F

        val host = fragment as NavHostFragment
        navigationController = host.navController

    }

    private var bottomNavDrawer = BottomDrawerFragment().apply { onCreated = {} }

    fun openDrawer(onDrawerOpened:(View)->Unit) {
        bottomNavDrawer.onCreated = onDrawerOpened
        bottomNavDrawer.show(supportFragmentManager, bottomNavDrawer.tag)
    }

    fun closeDrawer() {
        bottomNavDrawer.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
    companion object{
        val PIC_IMAGE_REQUEST = 0
        val PERMISSION_REQUEST = 1
        val NOTIFICATION_CLICKED = 2
        val PICK_DATABASE = 3
        val PIC_FILE_REQUEST = 4
        val PREFERENCES = "ChatNotePref"
        val CHANNEL_ID = "ChatNote"
    }

    lateinit var onFragmentResult:(Int,Int,Intent?)->Unit
    lateinit var onMessageDelete:()->Unit
    lateinit var onMessageUpdate:(Long)->Unit
    lateinit var onFragmentBackPressed:()->Unit
    lateinit var moveToMessageDetails: () -> Unit

    fun resolvIntent(){

    }

    override fun onBackPressed() {
        onFragmentBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            if (requestCode == NOTIFICATION_CLICKED){


            }
            else {
                onFragmentResult(requestCode, resultCode, data)
            }
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
