package com.example.grocery.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activity.ProductDetailActivity
import com.example.grocery.manager.Config
import com.example.grocery.manager.DBHelper
import com.example.grocery.module.CartItem
import com.example.grocery.module.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_buttons.view.*
import kotlinx.android.synthetic.main.fragment_product_row.view.*
import kotlinx.android.synthetic.main.fragment_product_row.view.cart_buttons
import kotlinx.android.synthetic.main.fragment_product_row.view.text_view_name


class ProductListAdapter (var mContext: Context, var mList: ArrayList<Product>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(){

    var listener :onAdapterListener? = null
    var cartItems: ArrayList<CartItem> = ArrayList<CartItem>()
    var db=DBHelper(mContext)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {   //called only once to return myViewHolder
        var view = LayoutInflater.from(mContext).inflate(R.layout.fragment_product_row,parent,false)


        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post=mList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    fun setData(list: ArrayList<Product>){
        mList = list
        notifyDataSetChanged()
    }
    fun setOnAdapterListener(onAdapterListener: onAdapterListener){
        listener = onAdapterListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(p: Product){

            var db=DBHelper(mContext)
            cartItems=db.readCartItem()

            Picasso.get().load(Config.Companion.IMGE_BASE_URL+p.image)
                .resize(300, 200)
                .centerInside()
                .placeholder(R.drawable.no_img)
                .into(itemView.image_view_product)

            itemView.text_view_name.text=p.productName
            itemView.text_view_unit.text=p.unit
            itemView.text_view_new_price.text=p.price.toString()

            itemView.button_add_to_chart.setOnClickListener {
                var db = DBHelper(mContext)
                db.addCartItem(CartItem(p._id,p.image,p.mrp, p.price,p.productName, p.quantity, p.unit))
                updateCartBts(p._id)
                Toast.makeText(mContext, "${p.productName} added to your cart.", Toast.LENGTH_SHORT).show()
            }
            var inCart=db.getQuantity(p._id)
            if(inCart!=0){ updateCartBts(p._id) }

            if(p.mrp!=null && p.price<p.mrp){ itemView.text_view_discount.text=((p.price-p.mrp)*100/(p.mrp)).toInt().toString()+"%" }
            else{ itemView.text_view_discount.background=null }

            itemView.button_less.setOnClickListener {
                p.quantity=db.getQuantity(p._id)
                listener?.onItemLess(p)

                if (db.getQuantity(p._id)==0){
                    itemView.cart_buttons.visibility=View.GONE
                    itemView.button_add_to_chart.visibility=View.VISIBLE
                }
                else{ updateCartBts(p._id)}
            }
            itemView.button_more.setOnClickListener {
                p.quantity=db.getQuantity(p._id)
                listener?.onItemMore(p)

                updateCartBts(p._id)
            }

            itemView.setOnClickListener {
                var intent = Intent(itemView.context, ProductDetailActivity::class.java)
                intent.putExtra(Product.KEY,p)
                mContext.startActivity(intent)
            }
            itemView.setOnClickListener{ listener?.onItemClick(p) }
        }


        fun updateCartBts(id:String){
            itemView.button_add_to_chart.visibility=View.GONE
            itemView.cart_buttons.visibility=View.VISIBLE
            itemView.edit_text_quantity.setText(db.getQuantity(id).toString())
        }
    }


    interface onAdapterListener{
        fun onItemClick(product:Product)
        fun onItemLess(p: Product)
        fun onItemMore(p: Product)
    }

}