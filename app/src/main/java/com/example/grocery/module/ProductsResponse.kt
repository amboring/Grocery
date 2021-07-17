package com.example.grocery.module

import java.io.Serializable

data class ProductsResponse(
    val count: Int,
    val `data`: List<Product>,
    val error: Boolean
)

data class Product(
    val __v: Int?,
    val _id: String,
    val catId: Int,
    val created: String,
    val description: String,
    val image: String,
    val mrp: Float,
    val position: Int,
    val price: Float,
    val productName: String,
    var quantity: Int,
    val status: Boolean,
    val subId: Int,
    val unit: String
):Serializable{
    companion object{
        val KEY="procuct info"
        val NAME="Name"
    }
}
data class CartItem(
    val id: String,
    val image: String,
    val mrp: Float,
    val price: Float,
    val productName: String,
    var quantity: Int,
    val unit: String
):Serializable{
    companion object{
        val KEY="item info"
        val ID="id"
        val NAME="name"
        val QUANTITY="quantity"
        val IMAGE="image"
        val UNIT="unit"
        val MRP="mrp"
        val PRICE="price"
    }
}