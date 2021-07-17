package com.example.grocery.manager

import kotlin.math.PI

class Endpoints {
    companion object{
        const val URL_REGISTER="auth/register"
        const val URL_LOGIN="auth/login"
        const val URL_CATEGORY="category"
        const val URL_SUB_CATEGORY="subcategory/"
        const val URL_SUB_CATEGORY_PRODUCT="products/sub/"
        const val URL_PRODUCT="products/"
        const val URL_ADDRESS="address"
        const val URL_ORDERS="orders"
        fun getRegisterURL():String{ return "${Config.BASE_URL+ URL_REGISTER}" }
        fun getLoginURL():String{return "${Config.BASE_URL+ URL_LOGIN}" }
        fun getCategoryURL():String{return "${Config.BASE_URL+ URL_CATEGORY}" }
        fun getSubCategoryURL(catId:Int):String{return "${Config.BASE_URL+ URL_SUB_CATEGORY+catId}" }
        fun getSubCategoryProductURL(subId:Int):String{return "${Config.BASE_URL+ URL_SUB_CATEGORY_PRODUCT+subId}" }
        fun getProductURL(pId:String):String{return "${Config.BASE_URL+ URL_PRODUCT+ pId}" }
        fun getAddressURL(uId:String):String{return "${Config.BASE_URL+ URL_ADDRESS +"/"+ uId}" }
        fun setAddressURL():String{return "${Config.BASE_URL+ URL_ADDRESS}" }
        fun setOrderURL():String{return "${Config.BASE_URL+ URL_ORDERS}" }
        fun getOrderByUserURL(uId:String):String{return "${Config.BASE_URL+ URL_ORDERS}/${uId}" }
    }

}