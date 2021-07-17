package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edittext_email
import kotlinx.android.synthetic.main.activity_register.edittext_password
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }
    private fun init(){
        var toolbar=my_toolbar
        toolbar.title="Register"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        button_register.setOnClickListener {
            var firstname=edittext_firstname.text.toString()
            var email=edittext_email.text.toString()
            var password=edittext_password.text.toString()
            var mobile=edittext_mobile.text.toString()

            var jsonObject=JSONObject()
            jsonObject.put("firstName",firstname)
            jsonObject.put("email",email)
            jsonObject.put("mobile",mobile)
            jsonObject.put("password",password)

            var requestQueue=Volley.newRequestQueue(this)
            var req=JsonObjectRequest(
                Request.Method.POST,
                Endpoints.getRegisterURL(),
                jsonObject,
                Response.Listener {
                    edittext_firstname.text.clear()
                    edittext_email.text.clear()
                    edittext_password.text.clear()
                    edittext_mobile.text.clear()
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext,it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            requestQueue.add(req)

        }
        text_clickable_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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