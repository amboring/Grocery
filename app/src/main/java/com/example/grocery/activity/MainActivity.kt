package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.grocery.R
import com.example.grocery.manager.DBHelper
import com.example.grocery.module.Order
import com.example.grocery.module.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header.view.*



//TO TEST
//UID: 60d4e925026b6b0017d01c19
//al@yahoo.com
//123321

class MainActivity : AppCompatActivity(), View.OnClickListener ,NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navView:NavigationView
    private var isLogin=false
    private lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_manu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_category->startActivity(Intent(this,CategoryActivity::class.java))
            R.id.menu_cart->startActivity(Intent(this,CartActivity::class.java))
            R.id.menu_login->{
                sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
                var isloggedin=sharedPreferences.getBoolean(User.IS_LOGGED_IN,false)
                if (!isloggedin) {startActivity( Intent(this,LoginActivity::class.java))}
                else{
                    Toast.makeText(applicationContext,"Aready logged in.", Toast.LENGTH_SHORT)}
            }
//            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init(){
//          TO TEST DB
//        var db=DBHelper(this)
//        db.dropTable()

        var sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
        isLogin=sharedPreferences.getBoolean(User.IS_LOGGED_IN,false)


        drawerLayout=drawer_layout
        navView=nav_view
        var headerView=navView.getHeaderView(0)

        if(isLogin){
            headerView.text_view_header_name.text=sharedPreferences.getString(User.FIRSTNAME,"")
            headerView.email.text=sharedPreferences.getString(User.EMAIL,"")
           // startActivity(Intent(this, HomeActivity::class.java))
        }
        else{
            headerView.text_view_header_name.text="Guest"
            headerView.email.text=""
        }

        var toggle = ActionBarDrawerToggle(this,drawerLayout,my_toolbar,0,0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener (this)

        button_register.setOnClickListener (this)
        button_login.setOnClickListener(this)
        button_catogory.setOnClickListener(this)

//        // set
//        var toolbar=my_toolbar
//        toolbar.title="Main"
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
//

    }

    override fun onClick(v: View?) {
        when (v){
            button_register->startActivity(Intent(this, RegisterActivity::class.java))
            button_login->startActivity(Intent(this, LoginActivity::class.java))
            button_catogory->startActivity(Intent(this, CategoryActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_account -> {
                if(isLogin){startActivity(Intent(this, HomeActivity::class.java)) }
                else {startActivity(Intent(this,LoginActivity::class.java))}
            }
            R.id.item_orders->{
                if(isLogin){
                    sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
                    var intent=Intent(this, OrderHistoryActivity::class.java)
                    intent.putExtra(Order.USERID,sharedPreferences.getString(User.ID,""))
                    startActivity(intent)
                }
                else {startActivity(Intent(this,LoginActivity::class.java))}
            }
            R.id.item_category->startActivity(Intent(this,CategoryActivity::class.java))
            R.id.item_logout->{
                if (isLogin){
                    var editor = sharedPreferences.edit()
                    editor.clear()
                    editor.putBoolean(User.IS_LOGGED_IN, false)
                    editor.commit()
                    var db=DBHelper(this)
                    db.clearCart()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}