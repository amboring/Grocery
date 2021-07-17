package com.example.grocery.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.manager.Config
import com.example.grocery.module.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cart_item_row.view.*

class OrderListAdapter  (var mContext: Context, var mList: ArrayList<CartItem>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    var currentItem: CartItem?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {   //called only once to return myViewHolder
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_order_history_item_row, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = mList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: CartItem) {

            Log.i("******cartitem",p.toString())


            Picasso.get().load(Config.Companion.IMGE_BASE_URL + p.image)
                .resize(200, 200)
                .centerInside()
                .placeholder(R.drawable.no_img)
                .into(itemView.image_view_cart_item)

            itemView.text_view_name.text = p.productName

            itemView.text_view_price.setText(p.price.toString())
            itemView.text_view_mrp.text=p.mrp.toString()
            itemView.text_view_mrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)


        }
    }

}

