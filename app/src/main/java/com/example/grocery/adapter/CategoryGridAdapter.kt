package com.example.grocery.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.manager.Config

import com.example.grocery.module.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_category_grid.view.*
import kotlinx.android.synthetic.main.fragment_product_row.view.*

class CategoryGridAdapter (var mContext: Context, var mList: ArrayList<Category>) : RecyclerView.Adapter<CategoryGridAdapter.MyViewHolder>(){

    var listener :onAdapterListener? = null
    val URL="https://rjtmobile.com/grocery/images/"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view= LayoutInflater.from(mContext).inflate(R.layout.fragment_category_grid,parent,false)
        var myViewHolder= MyViewHolder(view)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var category=mList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    fun setData(list: ArrayList<Category>){            //to reload the view after it first loaded before getting imgs
        mList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category){
            //Picasso.get().load(URL+category.catImage).into(itemView.imageview_img)
            Picasso.get().load(URL+category.catImage)
                .placeholder(R.drawable.no_img)
                .resize(400, 300)
                .centerInside()
                .into(itemView.imageview_img)


            itemView.text_view_title.text=category.catName

            itemView.setOnClickListener{
                listener?.onItemClick(category)
            }
        }
    }


    fun setOnAdapterListener(onAdapterListener: onAdapterListener){
        listener = onAdapterListener
    }

    interface onAdapterListener{
        fun onItemClick(category:Category)
    }

}