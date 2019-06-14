package com.yshmgrt.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Link
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.attachments.Image
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*

class MainActivity : AppCompatActivity() {
    var adapter:MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        message_list.setAdapter(adapter)
        linearLayoutManager = LinearLayoutManager(this)
        message_list.layoutManager = linearLayoutManager
        setSupportActionBar(toolbar)
        val controller = Controller(applicationContext)
        controller.getAllMessageId(object:Callback<List<Long>>{
            override fun onFailure() {
                return
            }

            override fun onBegin() {

                return
            }

            override fun onEnd(exit: List<Long>) {
                adapter = MessageViewAdapter(exit,applicationContext)
                message_list.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        })



        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_navigate_up_description)
        drawer.addDrawerListener(drawerToggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val host = fragment as NavHostFragment
        navigationController = host.navController

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }
}
