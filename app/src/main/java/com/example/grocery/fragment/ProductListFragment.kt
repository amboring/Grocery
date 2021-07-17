package com.example.grocery.fragment

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.activity.ProductDetailActivity
import com.example.grocery.adapter.ProductListAdapter
import com.example.grocery.manager.DBHelper
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.cart_buttons.view.*
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import kotlinx.android.synthetic.main.fragment_product_row.*
import kotlinx.android.synthetic.main.fragment_product_row.view.*
import kotlinx.android.synthetic.main.fragment_product_row.view.cart_buttons

private const val SUBID = "param1"

class ProductListFragment : Fragment(), ProductListAdapter.onAdapterListener{

    private var subId: Int =-1
    var products:ArrayList<Product> = ArrayList()
    var productListAdapter:ProductListAdapter? = null
    var mContext:Context?=null
    var db:DBHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(SUBID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_product_list, container, false)
        init(view)
         mContext = container?.getContext()
        if(mContext!=null){db=DBHelper(mContext!!)}
        return view
    }

    private fun init (view:View){
        productListAdapter= ProductListAdapter(requireContext(), products)
        loadProducts()
        view.recycler_view.adapter=productListAdapter
        view.recycler_view.layoutManager= LinearLayoutManager(activity)
        productListAdapter!!.setOnAdapterListener(this)
    }



    fun loadProducts(){
        var productResponse: ProductsResponse?=null
        var requestQueue = Volley.newRequestQueue(activity)
        var request= StringRequest(
            Request.Method.GET,
            Endpoints.getSubCategoryProductURL(subId),
            Response.Listener{
                var gson= Gson()
                productResponse = gson.fromJson(it.toString(), ProductsResponse::class.java)
                products=ArrayList(productResponse?.data)

                requireView().recycler_view.adapter = productListAdapter
                requireView().recycler_view.layoutManager = LinearLayoutManager(requireContext())

                productListAdapter?.setData(products)
                productListAdapter?.setOnAdapterListener(this)
            },
            Response.ErrorListener {
                Toast.makeText(activity?.applicationContext,it.message, Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
    }

    override fun onItemClick(product: Product) {
        var intent = Intent(context, ProductDetailActivity::class.java)
        intent.putExtra(Product.KEY,product)
        startActivity(intent)
    }

    override fun onItemMore(p: Product) {
        db?.addCartItem(CartItem(p._id,p.image,p.mrp,p.price,p.productName,p.quantity+1,p.unit))
    }

    override fun onItemLess(p: Product) {
        if(p.quantity==1){db?.deleteCartItem(p._id)}
        else{ db?.updateCartItem(CartItem(p._id,p.image,p.mrp,p.price,p.productName,p.quantity-1,p.unit)) }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putInt(SUBID, param1)
                }
            }
    }
}