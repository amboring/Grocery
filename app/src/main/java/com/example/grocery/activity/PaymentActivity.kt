package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.manager.DBHelper
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject


//INTENT EXPECTING : userId:String ${Address.USERID} , address ${Address.KEY}

class PaymentActivity : AppCompatActivity() {
    var userId:String?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {

        var toolbar = my_toolbar
        toolbar.title = "Payment"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button
        userId = intent.getStringExtra(Address.USERID)
        var p = intent.getSerializableExtra(Address.KEY) as Address

        setInfo(p)

        Log.i("********",DBHelper(this).readCartItem().toString())

        button_make_75.setOnClickListener { startActivity(Intent(this,CategoryActivity::class.java)) }
        button_cash.setOnClickListener { submitOrder(p,Payment.CASH) }
        button_online.setOnClickListener {
            // waiting for specification
        }
    }

        fun submitOrder(address: Address,paymentMode:String){//order: Order) {
            var reqQueue = Volley.newRequestQueue(this)
            var jsonBody = JSONObject()
            var db=DBHelper(this)
            var cartitems=db.readCartItem()
            var subtotal=db.getCartTotal()
            var cartItemPay = db.getCartPay()
            var delivery = 10F
            if (cartItemPay>=75){ delivery=0F }
            var orderSummary=OrderSummary(null,delivery,String.format("%.2f", (subtotal - cartItemPay)).toFloat(),subtotal,cartItemPay,cartItemPay+delivery)
            var payment=Payment(null,paymentMode,Payment.PENDING)
            var shippingAddress=ShippingAddress(null,address.city,address.houseNo,address.pincode,address.streetName)
            var sp=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
            var userInfo=UserInfo(sp.getString(User.ID,"").toString(),
                sp.getString(User.EMAIL,"").toString(),
                sp.getString(User.MOBILE,"").toString(),
                sp.getString(User.FIRSTNAME,"").toString()
            )

            jsonBody.put(Order.USERID, userId)
                .put(Order.PRODUCTS,Gson().toJson(cartitems))
               .put(Order.SHIPPINGADDRESS, Gson().toJson(shippingAddress))
                .put(Order.USERINFO,Gson().toJson(userInfo))
                .put(Order.ORDERSTATUS,Order.PROCESSING)
                .put(Order.ORDERSUMMARY,Gson().toJson(orderSummary))
                .put(Order.PAYMENT,Gson().toJson(payment))

            var str=jsonBody.toString().replace("\\\"","\"")
                .replace("\"[","[")
                .replace("]\"","]")
                .replace("\"{","{")
                .replace("}\"","}")

            jsonBody= JSONObject(str)

            var req = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.setOrderURL(),
                jsonBody,
                Response.Listener {
                    intent = Intent(this, OrderConfirmActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            reqQueue.add(req)
        }


        fun setInfo(p: Address) {
            var db = DBHelper(this)
            var subtotal = db.getCartTotal()
            var cartItemPay = db.getCartPay()
            var delivery = 10F
            if (cartItemPay>=75){
                free_shipping_holder.visibility= View.GONE
                delivery=0F
            }
            else{
                text_view_free_shipping.text="You are $%.2f away from free shipping.".format((75-cartItemPay))
            }
            text_view_pay.text = subtotal.toString()
            text_view_discount.text = "- " + String.format("%.2f", (subtotal - cartItemPay))
            text_view_delivery.text = delivery.toString()
            text_view_total.text = (cartItemPay + delivery).toString()

            text_view_house.text = p.houseNo
            text_view_street.text = p.streetName.uppercase()
            text_view_city.text = p.city.uppercase()
            text_view_zip.text = p.pincode.toString()

        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_manu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_category -> startActivity(Intent(this, CategoryActivity::class.java))
                R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
                R.id.menu_login -> {
                    var sharedPreferences =
                        getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
                    var isloggedin = sharedPreferences.getBoolean(User.IS_LOGGED_IN, false)
                    if (!isloggedin) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Toast.makeText(applicationContext, "Aready logged in.", Toast.LENGTH_SHORT)
                    }
                }
                android.R.id.home -> finish()       //set back button functionality
            }
            return super.onOptionsItemSelected(item)
        }

}