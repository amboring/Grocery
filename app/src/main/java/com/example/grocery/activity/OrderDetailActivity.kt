package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.R
import com.example.grocery.adapter.OrderListAdapter
import com.example.grocery.module.Order
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.app_bar.*

//INTENT EXPECTING : order:Order ${Order.KEY}

class OrderDetailActivity : AppCompatActivity() {
    var order:Order?=null
    var rowAdapter: OrderListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        init()
    }
    private fun init(){
        var toolbar = my_toolbar
        toolbar.title = "Payment"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        order= intent.getSerializableExtra(Order.KEY) as Order

        if(order!=null){setInfo(order!!)}
    }
    private fun setInfo(o:Order){
        var toolbar=my_toolbar
        toolbar.title="Category"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button


        var cartItems=o.products.toCollection(ArrayList())

        rowAdapter= OrderListAdapter(this, cartItems)

        recycler_view.adapter=rowAdapter
        recycler_view.layoutManager= LinearLayoutManager(this)

        text_view_pay.text = o.orderSummary.orderAmount.toString()
        text_view_discount.text = "- " + String.format("%.2f", (o.orderSummary.discount))
        text_view_delivery.text = o.orderSummary.deliveryCharges.toString()
        text_view_total.text = (o.orderSummary.totalAmount).toString()

        text_view_status.text=o.orderStatus

        text_view_payment_mode.text=o.payment.paymentMode

        text_view_house.text = o.shippingAddress.houseNo
        text_view_street.text = o.shippingAddress.streetName.uppercase()
        text_view_city.text = o.shippingAddress.city.uppercase()
        text_view_zip.text = o.shippingAddress.pincode.toString()

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