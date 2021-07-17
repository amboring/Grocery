package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.AddressListAdapter
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.Address
import com.example.grocery.module.AddressResponse
import com.example.grocery.module.Payment
import com.example.grocery.module.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.app_bar.*


//INTENT EXPECTING : userId:String ${Address.USERID}

class AddressActivity : AppCompatActivity() , AddressListAdapter.onAdapterListener{

   val ADDING_ADDRESS=10086

    var listAdapter:AddressListAdapter?=null
    var userId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        init()
    }
    private fun init(){

        var toolbar=my_toolbar
        toolbar.title="Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        userId=intent.getStringExtra(Address.USERID)
        if(userId!=null){loadAddress(userId!!)}

        button_add_new_address.setOnClickListener {
            intent= Intent(this,AddAddressActivity::class.java)
            intent.putExtra(Address.USERID,userId)

            //registerForActivityResult()
            startActivityForResult(intent,ADDING_ADDRESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                ADDING_ADDRESS->{
                    userId?.let { listAdapter?.setData(it) }
                    listAdapter?.setOnAdapterListener(this)
                    listAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    fun loadAddress(userId:String){
        var addressResponse: AddressResponse?=null
        var requestQueue = Volley.newRequestQueue(this)
        var request= StringRequest(
            Request.Method.GET,
           Endpoints.getAddressURL(userId),
            Response.Listener{

                var gson= Gson()
                addressResponse = gson.fromJson(it.toString(), AddressResponse::class.java)


                var addressList=addressResponse?.data

                if (addressList!=null){
                    listAdapter=AddressListAdapter(this,addressList!!)
                    address_recycler_view.adapter = listAdapter
                    address_recycler_view.layoutManager = LinearLayoutManager(this)

                    listAdapter?.setOnAdapterListener(this)
                    listAdapter?.setData(userId)
                    listAdapter?.notifyDataSetChanged()
                }
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,it.message, Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
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

    override fun onItemDelete(address: Address) {
        var requestQueue = Volley.newRequestQueue(this)
        var aid=address._id.toString()
        var request= StringRequest(
            Request.Method.DELETE,
            Endpoints.getAddressURL(aid),
            Response.Listener{
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,it.message, Toast.LENGTH_SHORT).show()
            })
        if (aid!=null){
            requestQueue.add(request)
            Log.i("addressact***",userId.toString())
            if (userId!=null){
                listAdapter?.setData(userId!!)
                onRestart()
            }
        }
    }

    override fun onItemEdit(address: Address) {
        intent= Intent(this,AddAddressActivity::class.java)
        intent.putExtra(Address.USERID,userId)
        intent.putExtra(Address.KEY,address)
        startActivityForResult(intent,ADDING_ADDRESS)
        onItemDelete(address)
    }

    override fun onItemClicked(address: Address) {
        intent= Intent(this,PaymentActivity::class.java)
        intent.putExtra(Address.USERID,userId)
        intent.putExtra(Address.KEY,address)
        startActivity(intent)
        finish()

        //if work this way, add address need to load existing address
    }
}