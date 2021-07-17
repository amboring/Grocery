package com.example.grocery.manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.grocery.module.CartItem

class DBHelper (var context : Context)
    :SQLiteOpenHelper(context, DB_NAME,null,DATABASE_VERSION){
    companion object{
        const val DB_NAME="mydb"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME="cartItem"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        var createTable="create table $TABLE_NAME ( ${CartItem.ID} integer, ${CartItem.NAME} char(50), " +
                "${CartItem.IMAGE} integer,${CartItem.UNIT} char(20), ${CartItem.QUANTITY} integer" +
                ",${CartItem.PRICE} integer, ${CartItem.MRP} integer )"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var db = writableDatabase
        var dropTable="drop table $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)            //recreate the table
    }

    fun clearCart(){
        var db=writableDatabase
        var clear="delete from $TABLE_NAME"
        db?.execSQL(clear)

    }

    fun addCartItem(p:CartItem){
        var db = writableDatabase

        var whereClause="${CartItem.ID} = ?"
        var whereArgs= arrayOf(p.id)
        var colums= arrayOf(CartItem.QUANTITY)
        var cursor=db.query(TABLE_NAME,colums,whereClause,whereArgs,null,null,null)
        if (cursor.getCount() !=0 && cursor.moveToFirst()){             //necessary : cursor.moveToFirst()
            var quantity=cursor.getInt(cursor.getColumnIndex(CartItem.QUANTITY))
            p.quantity=quantity+1
            updateCartItem(p)
        }
        else{
            var contentValue=ContentValues()
            contentValue.put(CartItem.ID,p.id)
            contentValue.put(CartItem.NAME,p.productName)
            contentValue.put(CartItem.IMAGE,p.image)
            contentValue.put(CartItem.UNIT,p.unit)
            contentValue.put(CartItem.PRICE,p.price)
            contentValue.put(CartItem.MRP,p.mrp)
            contentValue.put(CartItem.QUANTITY,1)
            db.insert(TABLE_NAME,null,contentValue)
        }

    }
    fun updateCartItem(p:CartItem){
        var db = writableDatabase
        var contentValue=ContentValues()
        contentValue.put(CartItem.ID,p.id)
        contentValue.put(CartItem.NAME,p.productName)
        contentValue.put(CartItem.IMAGE,p.image)
        contentValue.put(CartItem.UNIT,p.unit)
        contentValue.put(CartItem.QUANTITY,p.quantity)
        contentValue.put(CartItem.PRICE,p.price)
        contentValue.put(CartItem.MRP,p.mrp)

        var whereClause="${CartItem.ID} = ?"
        var whereArg= arrayOf(p.id)
        db.update(TABLE_NAME,contentValue,whereClause,whereArg)
    }

    fun deleteCartItem(id:String){
        Log.i("*****db","deleting ${id}")

        var db = writableDatabase
        var whereClause="${CartItem.ID} = ?"
        var whereArgs= arrayOf(id)
        var result=db.delete(TABLE_NAME,whereClause,whereArgs)
    }

    fun readCartItem():ArrayList<CartItem>{
        var db = readableDatabase
        var items=ArrayList<CartItem>()
        var colums= arrayOf(CartItem.ID, CartItem.NAME, CartItem.IMAGE, CartItem.UNIT, CartItem.QUANTITY, CartItem.PRICE, CartItem.MRP)
        var cursor=db.query(TABLE_NAME,colums,null,null,null,null,null)
        if(cursor.getCount() !=0 && cursor.moveToFirst()){
            do{
                var id=cursor.getString(cursor.getColumnIndex(CartItem.ID))             //NEED TO BE FIXED: PASSIG IN INDEX
                var name=cursor.getString(cursor.getColumnIndex(CartItem.NAME))
                var img=cursor.getString(cursor.getColumnIndex(CartItem.IMAGE))
                var unit=cursor.getString(cursor.getColumnIndex(CartItem.UNIT))
                var quantity=cursor.getInt(cursor.getColumnIndex(CartItem.QUANTITY))
                var price=cursor.getFloat(cursor.getColumnIndex(CartItem.PRICE))
                var mrp=cursor.getFloat(cursor.getColumnIndex(CartItem.MRP))
                items.add(CartItem(id,img,mrp,price,name,quantity,unit))
            }while(cursor.moveToNext())
        }
        return items
    }
    fun getCartQuantity(id:String):ArrayList<Int>{
        var db = readableDatabase
        var items=ArrayList<Int>()
        var colums= arrayOf(CartItem.QUANTITY)
        var whereClause="${CartItem.ID} = ?"
        var whereArg= arrayOf(id)
        var cursor=db.query(TABLE_NAME,colums,whereClause,whereArg,null,null,null)
        if(cursor.getCount() !=0 && cursor.moveToFirst()){
            do{
                var quantity=cursor.getInt(cursor.getColumnIndex(CartItem.QUANTITY))
                items.add(quantity)
            }while(cursor.moveToNext())
        }
        return items
    }

    fun getCartTotal():Float{
        var total= 0F
        var items=readCartItem()
        if (items.size==0){return total}
        for (i in items){total+= (i.quantity*i.mrp)}
        return total
    }
    fun getCartPay():Float{
        var total= 0F
        var items=readCartItem()
        if (items.size==0){return total}
        for (i in items){total+= (i.quantity*i.price)}
        return total
    }
    fun getQuantity(id:String):Int{
        var q=getCartQuantity(id)
        if (q.size!=0){ return q[0] }
        else{return 0}
    }

    fun dropTable(){
        var db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }
}