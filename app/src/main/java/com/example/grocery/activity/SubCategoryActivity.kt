package com.example.grocery.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.ProductListAdapter
import com.example.grocery.adapter.SubCatTabAdapter
import com.example.grocery.fragment.ProductListFragment
import com.example.grocery.manager.DBHelper
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_buttons.view.*

class SubCategoryActivity : AppCompatActivity() {
    var subcategories:ArrayList<Subcategory> = ArrayList()

    var category :Category?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        init()
    }
    private fun init() {
        var toolbar=my_toolbar
        toolbar.title="Sub-Category"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        category=intent.getSerializableExtra(Category.KEY) as Category
        var tabAdapter = SubCatTabAdapter(supportFragmentManager)

        if (category!=null){ loadCategory() }
        subcategories?.forEach { sub ->
            tabAdapter.addFragment(sub)
        }
        view_pager.adapter =tabAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    fun loadCategory(){
        var subcategoryResponse:SubcategoryResponse?=null
        var requestQueue = Volley.newRequestQueue(this)
        var request= StringRequest(
            Request.Method.GET,
            Endpoints.getSubCategoryURL(category!!.catId),
            Response.Listener{
                var gson= Gson()
                subcategoryResponse = gson.fromJson(it.toString(),SubcategoryResponse::class.java)
                subcategories=ArrayList(subcategoryResponse?.data)
                var fragAdaptor = SubCatTabAdapter(supportFragmentManager)
                for(c in subcategories){
                    fragAdaptor.mFragmentList.add(ProductListFragment.newInstance(c.subId) )
                }
                view_pager.adapter =fragAdaptor
                fragAdaptor?.setData(subcategories)
                progress_bar.visibility=View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(applicationContext,"Aready logged in.", Toast.LENGTH_SHORT)}
            }
            android.R.id.home -> finish()       //set back button functionality
        }
        finish()
        return super.onOptionsItemSelected(item)
    }

}