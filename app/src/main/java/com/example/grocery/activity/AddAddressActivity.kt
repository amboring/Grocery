package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.AddressListAdapter
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.Address
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

//INTENT EXPECTING : userId:String ${Address.USERID} , address? ${Address.KEY}

class AddAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        init()
    }
    private fun init(){
        var toolbar=my_toolbar
        toolbar.title="Edit Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        var userId=intent.getStringExtra(Address.USERID)

        var address=intent.getSerializableExtra(Address.KEY)
        var addressType:String?=null

        if(address!=null){
            address=address as Address
            edittext_house.setText(address.houseNo)
            edittext_street.setText(address.streetName)
            edittext_city.setText(address.city)
            edittext_zipcode.setText(address.pincode.toString())
        }

        Log.i("*******", userId.toString())

        var types= arrayOf<String>("Address Type","Home","Office","Other")

        var adapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types)
        spinner_type.setAdapter(adapter)
        spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addressType = parent?.getItemAtPosition(position).toString()
                if (addressType=="Address Type"){
                    Toast.makeText(applicationContext,"Please select a valid address type.",Toast.LENGTH_SHORT)
                    addressType=null
                }
            }
        }

        button_save.setOnClickListener {
            Log.i("*******", userId.toString())
            var type=addressType.toString()
            var houseNum=edittext_house.text.toString()
            var street=edittext_street.text.toString()
            var city=edittext_city.text.toString()
            var zip=edittext_zipcode.text.toString()
            if (type!=null && houseNum.length>0 && street.length>0 && city.length>0 && zip.length>0)
                addNewAddress(Address(null,null,city,houseNum,zip.toInt(),street,type,userId))
        }
    }

    fun addNewAddress(addr: Address) {
        var reqQueue = Volley.newRequestQueue(this)
        var jsonBody = JSONObject()
        Log.i("****",addr.toString())
        jsonBody.put("pincode", addr.pincode)
        jsonBody.put("city",addr.city)
        jsonBody.put("streetName", addr.streetName)
        jsonBody.put("houseNo", addr.houseNo)
        jsonBody.put("type", addr.type)
        jsonBody.put("userId", addr.userId)


        var req=JsonObjectRequest(
            Request.Method.POST,
            Endpoints.setAddressURL(),
            jsonBody,
            Response.Listener {
                Log.i("*******", it.toString())
                setResult(RESULT_OK,Intent())
                finish()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        )
        reqQueue.add(req)
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
                else{Toast.makeText(applicationContext,"Aready logged in.",Toast.LENGTH_SHORT)}
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }
}