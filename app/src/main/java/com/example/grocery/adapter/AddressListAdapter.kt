package com.example.grocery.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.manager.Endpoints
import com.example.grocery.module.Address
import com.example.grocery.module.AddressResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.fragment_address_row.view.*


//INTENT EXPECTING : userId:String ${Address.USERID}, ADDRESS :Address? ${Address.KEY}

class AddressListAdapter (var mContext: Context, var mList: List<Address>) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    var listener: onAdapterListener? = null
    var address: Address?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {   //called only once to return myViewHolder
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_address_row, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = mList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(uid:String) {
        var addressResponse: AddressResponse?=null
        var requestQueue = Volley.newRequestQueue(mContext)
        var request= StringRequest(
            Request.Method.GET,
            Endpoints.getAddressURL(uid),
            Response.Listener{

                var gson= Gson()
                addressResponse = gson.fromJson(it.toString(), AddressResponse::class.java)
                var addressList=addressResponse?.data
                if (addressList!=null){ mList=addressList }
                notifyDataSetChanged()
            },
            Response.ErrorListener {
                Toast.makeText(mContext.applicationContext,it.message, Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)

    }



    fun setOnAdapterListener(onAdapterListener: onAdapterListener) {
        listener = onAdapterListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Address) {
            itemView.text_view_type.text=p.type
            itemView.text_view_house.text=p.houseNo
            itemView.text_view_street.text=p.streetName.uppercase()
            itemView.text_view_city.text=p.city.uppercase()
            itemView.text_view_zip.text=p.pincode.toString()

            itemView.button_delete.setOnClickListener {listener?.onItemDelete(p) }
            itemView.button_edit.setOnClickListener {listener?.onItemEdit(p) }
            itemView.setOnClickListener{listener?.onItemClicked(p)}


        }
    }

    interface onAdapterListener {
        fun onItemDelete(address: Address)
        fun onItemEdit(address: Address)
        fun onItemClicked(address:Address)
    }
}