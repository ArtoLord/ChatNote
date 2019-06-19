package com.yshmgrt.chat

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yshmgrt.chat.view.BottomDrawerFragment
import com.yshmgrt.chat.view.MainChatFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import android.provider.MediaStore
import android.util.Log
import androidx.navigation.Navigation
import org.jetbrains.anko.bundleOf


class MainActivity : AppCompatActivity() {

    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_navigate_up_description)
        drawer.addDrawerListener(drawerToggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val host = fragment as NavHostFragment
        navigationController = host.navController

        if (intent.action== NOTIFICATION_CLICKED.toString()){
            val id = intent!!.extras["messageId"].toString().toLong()
            Log.d("ChatNote", id.toString())
            val bundle = bundleOf("messageId" to id)

            navigationController.navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
        }

        to_notifies_button.setOnClickListener {
            navigationController.navigate(R.id.notificationsFragment)
            drawer.closeDrawers()
        }
        to_bookmarks_button.setOnClickListener {
            navigationController.navigate(R.id.bookmarksFragment)
            drawer.closeDrawers()
        }
        to_main_button.setOnClickListener {
            navigationController.navigate(R.id.mainChatFragment)
            drawer.closeDrawers()
        }
    }

    fun openDrawer(onDrawerOpened:(View)->Unit) {
        val bottomNavDrawer = BottomDrawerFragment(onDrawerOpened)
        bottomNavDrawer.show(supportFragmentManager, bottomNavDrawer.tag)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    var searchVisable = false

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
    lateinit var onMessageUpdate:()->Unit


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
}
