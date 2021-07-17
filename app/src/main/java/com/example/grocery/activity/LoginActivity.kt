package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    var view: View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init(){
        var toolbar=my_toolbar
        toolbar.title="Login"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        //edittext_email.addTextChangedListener()
            //itemView.edit_text_quantity.addTextChangedListener (MyTextWatcher(itemView,currentItem,listener))


        button_login.setOnClickListener {
            var email = edittext_email.text.toString()
            var password = edittext_password.text.toString()

            var jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            var requestQueue = Volley.newRequestQueue(this)
            var req = JsonObjectRequest(
                Request.Method.POST,
                "https://apolis-grocery.herokuapp.com/api/auth/login",
                jsonObject,
                Response.Listener {
                    edittext_email.text.clear()
                    edittext_password.text.clear()

                    var sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
                    var editor = sharedPreferences.edit()

                    jsonObject=it
                    editor.putString(User.TOKEN,jsonObject.getString(User.TOKEN))

                    jsonObject=it.getJSONObject(User.USER)
                    editor.putString(User.FIRSTNAME,jsonObject.getString(User.FIRSTNAME))
                    editor.putString(User.ID,jsonObject.getString(User.ID))
                    editor.putString(User.EMAIL,jsonObject.getString(User.EMAIL))
                    editor.putString(User.MOBILE,jsonObject.getString(User.MOBILE))
                    editor.putString(User.PASSWORD,jsonObject.getString(User.PASSWORD))
                    editor.putString(User.CREATEDAT,jsonObject.getString(User.CREATEDAT))
                    editor.putString(User.VERSION,jsonObject.getString(User.VERSION))
                    editor.putBoolean(User.IS_LOGGED_IN, true)

                    editor.commit()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                },
                Response.ErrorListener {
                    Log.i("**** error ****",it.message.toString())
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(req)
        }

        text_clickable_reg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var view=menuInflater.inflate(R.menu.main_manu,menu)
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
                else{Toast.makeText(applicationContext,"Aready logged in.",Toast.LENGTH_SHORT)}
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }

//    class MyTextWatcher (): TextWatcher {
//        override fun beforeTextChanged(charSequence:CharSequence, i:Int, i1:Int, i2:Int) {}
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        override fun afterTextChanged(s: Editable?) {
//            val quantity=view.edit_text_quantity.text.toString().toInt()
//            cartItem?.quantity=quantity
//            listener?.onItemUpdate(cartItem)
//        }
//    }
}