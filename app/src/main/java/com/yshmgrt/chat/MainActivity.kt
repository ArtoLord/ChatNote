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
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yshmgrt.chat.view.BottomDrawerFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.bundleOf


class MainActivity : AppCompatActivity() {

    lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher_square)

        val host = fragment as NavHostFragment
        navigationController = host.navController

        if (intent.action== NOTIFICATION_CLICKED.toString()){
            val id = intent!!.extras["messageId"].toString().toLong()
            Log.d("ChatNote", id.toString())
            val bundle = bundleOf("messageId" to id)

            navigationController.navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
        }
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
        val CHANNEL_ID = "ChatNote"
        fun getRealPathFromUri(context: Context, contentUri: Uri): String {
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor!!.moveToFirst()
                return cursor!!.getString(column_index)
            } finally {
                if (cursor != null) {
                    cursor!!.close()
                }
            }
        }
    }

    lateinit var onFragmentResult:(Int,Int,Intent?)->Unit
    lateinit var onMessageDelete:()->Unit
    lateinit var onMessageUpdate:(Long)->Unit
    lateinit var onFragmentBackPressed:()->Unit
    lateinit var moveToMessageDetails: () -> Unit

    override fun onBackPressed() {
        onFragmentBackPressed()
    }
    fun onBackFromOtherPressed(){
        super.onBackPressed()
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
