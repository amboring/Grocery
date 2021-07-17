package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.OrderHistoryRowAdapter
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.app_bar.*

//INTENT EXPECTING : userId:String ${Order.USERID}


class OrderHistoryActivity  : AppCompatActivity() , OrderHistoryRowAdapter.onAdapterListener{
    var orders:ArrayList<Order>? = ArrayList()
    var rowAdapter: OrderHistoryRowAdapter?=null
    var userId:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        init()
    }

    private fun init(){

        var toolbar=my_toolbar
        toolbar.title="Order History"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        userId = intent.getStringExtra(Order.USERID)

        var orderResponse: OrderResponse?=null
        var requestQueue = Volley.newRequestQueue(this)

        if (userId==null){
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(applicationContext,"Please login to view your order history.", Toast.LENGTH_LONG)
            finish()
        }

        var request= StringRequest(
            Request.Method.GET,
            Endpoints.getOrderByUserURL(userId?:""),
            Response.Listener{
                var gson= Gson()
                orderResponse = gson.fromJson(it.toString(), OrderResponse::class.java)

                Log.i("product response: " , orderResponse.toString())
                orders=orderResponse?.data?.toCollection(ArrayList())
                if(orders!=null){
                    orders=(orders!!.reversed()).toCollection(ArrayList())
                    rowAdapter= OrderHistoryRowAdapter(this, orders!!)
                    rowAdapter?.setData()
Log.i("*******",order_recycler_view.toString())
                    order_recycler_view.adapter=rowAdapter
                    order_recycler_view.layoutManager= LinearLayoutManager(this)
                    rowAdapter?.setOnAdapterListener(this)
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
                else{
                    Toast.makeText(applicationContext,"Aready logged in.", Toast.LENGTH_SHORT).show()
                }
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemDelete(cartItem: Order) {
        Toast.makeText(applicationContext, "Sorry, we are not able to cancel the order by now, please contact us to canel the order by calling xxx-xxx-xxxx",Toast.LENGTH_LONG).show()
        //API CANT CANCEL ORDER YET
    }

    override fun onItemClicked(order: Order) {
        //display order detail

//        var intent= Intent(this,ProductDetailActivity()::class.java)
//
//        var productResponse: ProductResponse?=null
//        var requestQueue = Volley.newRequestQueue(this)
//        var request= StringRequest(
//            Request.Method.GET,
//            Endpoints.getProductURL(cartItem.id),
//            Response.Listener{
//                var gson= Gson()
//                productResponse = gson.fromJson(it.toString(), ProductResponse::class.java)
//
//                Log.i("product response: " , productResponse.toString())
//
//                var p=productResponse?.Product
//                intent.putExtra(Product.KEY,p)
//                startActivity(intent)
//            },
//            Response.ErrorListener {
//                Toast.makeText(applicationContext,it.message, Toast.LENGTH_SHORT).show()
//            })
//        requestQueue.add(request)
//        //get product
    }

}


