package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.grocery.R
import com.example.grocery.manager.DBHelper
import com.example.grocery.module.Order
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init(){
        var toolbar=my_toolbar
        toolbar.title="Home"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        var sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
        var userName=sharedPreferences.getString(User.FIRSTNAME,"")
        var greeting=text_view_greeting.text.toString()
        greeting= userName?.let { greeting.replace("CUSTOMER", it) }.toString()
        text_view_greeting.text=greeting

        button_logout.setOnClickListener {
            var editor = sharedPreferences.edit()
            editor.clear()
            editor.putBoolean(User.IS_LOGGED_IN, false)
            editor.commit()
            var db=DBHelper(this)
            db.clearCart()
            startActivity(Intent(this, MainActivity::class.java))
        }
        button_cate.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        button_order_history.setOnClickListener {
            var uid=sharedPreferences.getString(User.ID,"")
            if (uid!=""){


                var intent=Intent(this,OrderHistoryActivity::class.java)
                intent.putExtra(Order.USERID,uid)
                startActivity(intent)
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                Toast.makeText(applicationContext,"Please login to see your order history",Toast.LENGTH_LONG).show()
            }
        }
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
                var sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
                var isloggedin=sharedPreferences.getBoolean(User.IS_LOGGED_IN,false)
                if (!isloggedin) {startActivity( Intent(this,LoginActivity::class.java))}
                else{
                    Toast.makeText(applicationContext,"Aready logged in.", Toast.LENGTH_SHORT)}
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }
}