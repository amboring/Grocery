package com.example.grocery.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activity.OrderDetailActivity
import com.example.grocery.module.Order
import kotlinx.android.synthetic.main.fragment_order_history_row.view.*

class OrderHistoryRowAdapter(var mContext: Context, var mList: ArrayList<Order>) : RecyclerView.Adapter<OrderHistoryRowAdapter.ViewHolder>() {

    var listener: onAdapterListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {   //called only once to return myViewHolder
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_order_history_row, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = mList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData() {
//        val db= DBHelper(mContext)
//        mList = db.readCartItem()
        notifyDataSetChanged()

    }

    fun setOnAdapterListener(onAdapterListener: onAdapterListener) {
        listener = onAdapterListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order) {
            itemView.text_view_id.text= order._id
            itemView.text_view_id.setPaintFlags(itemView.text_view_cancel.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            itemView.text_view_id.setOnClickListener {
                var intent=Intent(mContext,OrderDetailActivity::class.java)
                intent.putExtra(Order.KEY,order)
                mContext.startActivity(intent)
            }

            itemView.text_view_date.text= "Placed on: ${ order.date.substring(0,order.date.indexOf("T")) }"
            if(order.shippingAddress!=null){
                itemView.text_view_house.text= "Will be ship to: ${ order.shippingAddress.houseNo }"
                itemView.text_view_street.text=order.shippingAddress.streetName.uppercase()
                itemView.text_view_city.text=order.shippingAddress.city.uppercase()
                itemView.text_view_zip.text=order.shippingAddress.pincode.toString()
            }
            itemView.text_view_status.text=order.orderStatus
            if (order.orderStatus==Order.PROCESSING){
                itemView.text_view_cancel.visibility=View.VISIBLE
                itemView.text_view_cancel.setPaintFlags(itemView.text_view_cancel.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

                itemView.text_view_cancel.setOnClickListener {
                    listener?.onItemDelete(order)
                }
            }
            if(order.orderSummary!=null){
                itemView.text_view_total.setText("$ ${ order.orderSummary.totalAmount }")
            }
            itemView.setOnClickListener{listener?.onItemClicked(order)}
        }
    }

    interface onAdapterListener {
        fun onItemDelete(cartItem: Order)
        fun onItemClicked(cartItem: Order)
    }
}

