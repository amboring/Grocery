package com.example.grocery.module

import java.io.Serializable

data class AddressResponse(
    val count: Int,
    val data: List<Address>,
    val error: Boolean
)

data class Address(
    val __v: Int?,
    val _id: String?,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String?
):Serializable{
    companion object{
        const val KEY="ADDRESS"
        const val USERID="_id"
    }
}