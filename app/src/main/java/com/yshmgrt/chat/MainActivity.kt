package com.yshmgrt.chat

import android.content.Intent
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
        val manager = supportFragmentManager.findFragmentById(R.id.fragment)
        if (manager!=null)
            (manager as MainChatFragment).test()
        else println("not works")
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

    lateinit var onFragmentResult:(Int,Int,Intent?)->Unit


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            onFragmentResult(requestCode, resultCode, data)
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
}
