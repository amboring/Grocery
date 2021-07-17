package com.example.grocery.module

import java.io.Serializable

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val mobile: String,
    val password: String
):Serializable{
    companion object{
        const val ID="_id"
        const val TOKEN="token"
        const val USER="user"
        const val FIRSTNAME="firstName"
        const val EMAIL="email"
        const val MOBILE="mobile"
        const val PASSWORD="password"
        const val CREATEDAT="createdAt"
        const val VERSION="__v"
        const val IS_LOGGED_IN="ISlOGGEDIN"
        const val FILE_NAME="userInfo"
    }
}