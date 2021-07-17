package com.example.grocery.module

import java.io.Serializable

data class OrderResponse(
    val count: Int,
    val `data`: List<Order>,
    val error: Boolean
)

data class Order(
    val __v: Int?,
    val _id: String?,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: List<CartItem>,
    val shippingAddress: ShippingAddress,
    val user: UserInfo,
    val userId: String
):Serializable{
    companion object{
       const val PRODUCTS="products"
        const val USERID="userId"
        const val SHIPPINGADDRESS="shippingAddress"
        const val USERINFO="user"
        const val ORDERSTATUS="orderStatus"
        const val ORDERSUMMARY="orderSummary"
        const val PAYMENT="payment"
        const val PROCESSING="Processing"
        const val KEY="ORder"
    }
}
data class OrderSummary(
    val _id: String?,
    val deliveryCharges: Float,
    val discount: Float,
    val orderAmount: Float,
    val ourPrice: Float,
    val totalAmount: Float
):Serializable

data class Payment(
    val _id: String?,
    val paymentMode: String,
    val paymentStatus: String
):Serializable{
    companion object{
        const val PENDING="Pending"
        const val CASH="Cash"
        const val OL="Online"
    }
}

data class ShippingAddress(
    val _id: String?,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
):Serializable

data class UserInfo(
    val _id: String,
    val email: String,
    val mobile: String,
    val name: String
):Serializable