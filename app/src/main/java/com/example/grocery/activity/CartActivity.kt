package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.CartListAdapter
import com.example.grocery.manager.DBHelper
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.text_view_delivery
import kotlinx.android.synthetic.main.activity_cart.text_view_discount
import kotlinx.android.synthetic.main.activity_cart.text_view_pay
import kotlinx.android.synthetic.main.activity_cart.text_view_total
import kotlinx.android.synthetic.main.app_bar.*

class CartActivity : AppCompatActivity() , CartListAdapter.onAdapterListener{
    var cart:ArrayList<CartItem> = ArrayList()
    var db=DBHelper(this)
    var rowAdapter:CartListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
    }

    private fun init(){

        var toolbar=my_toolbar
        toolbar.title="Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        cart=db.readCartItem()
        if (cart.size>0){}

        rowAdapter= CartListAdapter(this,cart)
        rowAdapter?.setData()

        recycler_view.adapter=rowAdapter
        recycler_view.layoutManager= LinearLayoutManager(this)
        rowAdapter?.setOnAdapterListener(this)
        setPrice()

        //only user can checkout
        button_checkout.setOnClickListener {
            var sharedPreferences=getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE)
            var isLoggedIn=sharedPreferences.getBoolean(User.IS_LOGGED_IN,false)
            if (isLoggedIn){
                var userId=sharedPreferences.getString(User.ID,null)
                if(userId==null){
                    startActivity(Intent(this,LoginActivity::class.java))
                    Toast.makeText(applicationContext,"User information not found, please login again.",Toast.LENGTH_SHORT).show()
                }
                var intent=Intent(this,AddressActivity::class.java)
                intent.putExtra(Address.USERID,userId)
                startActivity(intent)
                finish()
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                Toast.makeText(applicationContext,"Please login before check out.", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun setPrice(){
        var subtotal=db.getCartTotal()
        var cartItemPay=db.getCartPay()

        var delivery=0F
        if (cartItemPay<75){delivery=10F }
        text_view_pay.text=subtotal.toString()
        text_view_discount.text="- "+String.format("%.2f", (subtotal-cartItemPay))
        text_view_delivery.text=delivery.toString()
        text_view_total.text=(cartItemPay+delivery).toString()
    }

    override fun onItemDelete(cartItem: CartItem) {
        db.deleteCartItem(cartItem.id)
        rowAdapter?.setData()
        setPrice()
        if(db.getCartTotal()==0F){
            startActivity(Intent(this,CategoryActivity::class.java))
            finish()
            Toast.makeText(applicationContext,"Empty Cart, let's add something to it.",Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemLess(cartItem: CartItem) {
        if(cartItem.quantity==1){onItemDelete(cartItem)}
        else{
            cartItem.quantity-=1
            onItemUpdate(cartItem)
        }
    }

    override fun onItemMore(cartItem: CartItem) {
        cartItem.quantity+=1
        onItemUpdate(cartItem)
    }

    override fun onItemClicked(cartItem: CartItem) {
        var intent=Intent(this,ProductDetailActivity()::class.java)

        var productResponse: ProductResponse?=null
        var requestQueue = Volley.newRequestQueue(this)
        var request= StringRequest(
            Request.Method.GET,
            Endpoints.getProductURL(cartItem.id),
            Response.Listener{
                var gson= Gson()
                productResponse = gson.fromJson(it.toString(), ProductResponse::class.java)

                Log.i("product response: " , productResponse.toString())

                var p=productResponse?.Product
                intent.putExtra(Product.KEY,p)
                startActivity(intent)
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,it.message, Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
        //get product
    }

    override fun onItemUpdate(cartItem: CartItem?) {
        if (cartItem==null){return}
        db.updateCartItem(cartItem)
        rowAdapter?.setData()
        setPrice()
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


