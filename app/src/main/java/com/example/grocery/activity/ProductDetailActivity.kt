package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.grocery.R
import com.example.grocery.manager.Config
import com.example.grocery.module.Product
import com.squareup.picasso.Picasso
import android.widget.Toast
import com.example.grocery.manager.DBHelper
import com.example.grocery.module.CartItem
import com.example.grocery.module.User
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_buttons.view.*

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        init()
    }

    private fun init(){

        var p=intent.getSerializableExtra(Product.KEY) as Product
        var db=DBHelper(this)

        var toolbar=my_toolbar
        toolbar.title=p.productName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        Picasso.get().load(Config.Companion.IMGE_BASE_URL+p.image)
            .resize(1000, 600)
            .centerInside()
            .placeholder(R.drawable.no_img)
            .into(image_view_product)

        text_view_name.text=p.productName
        text_view_unit.text=p.unit
        text_view_new_price.text=p.price.toString()
        text_view_old_price.text=p.mrp.toString()
        text_view_old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)


        var quantity= DBHelper(this).getQuantity(p._id)
        if (quantity!=0){
            cart_buttons.visibility=View.VISIBLE
            button_add_to_chart.visibility=View.GONE
        }
        button_add_to_chart.setOnClickListener {
            db.addCartItem(CartItem(p._id,p.image,p.mrp,p.price,p.productName,1,p.unit))
            setQuntity(p._id)
        }
        cart_buttons.button_more.setOnClickListener {
            db?.addCartItem(CartItem(p._id,p.image,p.mrp,p.price,p.productName,db.getQuantity(p._id)+1,p.unit))
            setQuntity(p._id)
        }
        cart_buttons.button_less.setOnClickListener {
            if(db.getQuantity(p._id)==1){
                db?.deleteCartItem(p._id)
                button_add_to_chart.visibility=View.VISIBLE
                cart_buttons.visibility=View.GONE
            }
            else{
                db?.updateCartItem(CartItem(p._id,p.image,p.mrp,p.price,p.productName,db.getQuantity(p._id)-1,p.unit))
                setQuntity(p._id)
            }

        }
    }


    fun setQuntity(id:String){
        button_add_to_chart.visibility=View.GONE
        cart_buttons.visibility=View.VISIBLE
        cart_buttons.edit_text_quantity.setText(DBHelper(this).getQuantity(id).toString())
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
                else{ Toast.makeText(applicationContext,"Aready logged in.", Toast.LENGTH_SHORT)}
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        return super.onOptionsItemSelected(item)
    }

}