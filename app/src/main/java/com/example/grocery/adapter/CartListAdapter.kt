package com.example.grocery.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.manager.*
import com.example.grocery.module.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_buttons.view.*
import kotlinx.android.synthetic.main.fragment_cart_item_row.view.*
import kotlinx.android.synthetic.main.fragment_cart_item_row.view.cart_buttons
import kotlinx.android.synthetic.main.fragment_cart_item_row.view.text_view_name
import kotlinx.android.synthetic.main.fragment_product_row.view.*


class CartListAdapter (var mContext: Context, var mList: ArrayList<CartItem>) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    var listener: onAdapterListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {   //called only once to return myViewHolder
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_cart_item_row, parent, false)
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
        val db=DBHelper(mContext)
        mList = db.readCartItem()
        notifyDataSetChanged()

    }

    fun setOnAdapterListener(onAdapterListener: onAdapterListener) {
        listener = onAdapterListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: CartItem) {

            Picasso.get().load(Config.Companion.IMGE_BASE_URL + p.image)
                .resize(200, 200)
                .centerInside()
                .placeholder(R.drawable.no_img)
                .into(itemView.image_view_cart_item)

            itemView.text_view_name.text = p.productName
            itemView.text_view_price.setText(p.price.toString())
            itemView.text_view_mrp.text=p.mrp.toString()
            itemView.text_view_mrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            itemView.cart_buttons.edit_text_quantity.setText(p.quantity.toString())

            itemView.button_delete.setOnClickListener {listener?.onItemDelete(p) }
            itemView.cart_buttons.button_less.setOnClickListener {listener?.onItemLess(p) }
            itemView.cart_buttons.button_more.setOnClickListener { listener?.onItemMore(p) }


            itemView.setOnClickListener{listener?.onItemClicked(p)}
        }
    }
//    class MyTextWatcher (var view: View,var cartItem: CartItem?,var listener: onAdapterListener?): TextWatcher {
//        override fun beforeTextChanged(charSequence:CharSequence, i:Int, i1:Int, i2:Int) {}
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        override fun afterTextChanged(s: Editable?) {
//            val quantity=view.edit_text_quantity.text.toString().toInt()
//            cartItem?.quantity=quantity
//            listener?.onItemUpdate(cartItem)
//        }
//    }         need to add lisner to lost focus


    interface onAdapterListener {
        fun onItemDelete(cartItem: CartItem)
        fun onItemLess(cartItem: CartItem)
        fun onItemMore(cartItem: CartItem)
        fun onItemClicked(cartItem: CartItem)
        fun onItemUpdate(cartItem: CartItem?)
    }
}

