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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapter.CategoryGridAdapter
import com.example.grocery.adapter.ImgageSliderAdapter
import com.example.grocery.fragment.SlideImageFragment
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.Category
import com.example.grocery.module.CategoryResponse
import com.example.grocery.module.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_category.view_pager
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class CategoryActivity : AppCompatActivity(), CategoryGridAdapter.onAdapterListener {

    var categories:ArrayList<Category> = ArrayList()

    val URL="https://rjtmobile.com/grocery/images/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        init()
    }
    private fun init(){
        var toolbar=my_toolbar
        toolbar.title="Category"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //set back button

        var categoryResponse:CategoryResponse?=null

        var jsonObject: JSONObject?=null
        var requestQueue = Volley.newRequestQueue(this)
        var req = JsonObjectRequest(
            Request.Method.GET,
            Endpoints.getCategoryURL(),
            jsonObject,
            Response.Listener {
                var gson= Gson()
                categoryResponse=gson.fromJson(it.toString(),CategoryResponse::class.java)
                categories=ArrayList(categoryResponse?.data)
                var fragAdaptor = ImgageSliderAdapter(supportFragmentManager)
                for(c in categories){
                    fragAdaptor.mFragmentList.add(SlideImageFragment.newInstance(c.catImage,URL+c.catImage) )
                }
                view_pager.adapter =fragAdaptor
                var catoAdapter=CategoryGridAdapter(this, categories)
                catoAdapter?.setData(categories)

                recycler_view.adapter = catoAdapter
                recycler_view.layoutManager = GridLayoutManager(this,2)
                catoAdapter.setOnAdapterListener(this)

                //progress_bar.visibility= View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(req)
    }

    override fun onItemClick(category: Category) {
        intent=Intent(this, SubCategoryActivity::class.java)
        intent.putExtra(Category.KEY,category)
        startActivity(intent)
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