package com.meng.expirationdate.room

import androidx.room.*

@Dao
interface ItemsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun  insertItem(vararg item: ItemInfo)

    @Update
    fun  updateItem(vararg item: ItemInfo)

    @Delete
    fun deleteItem(vararg item: ItemInfo)

    @Query("SELECT * FROM ItemInfo WHERE itemId = :itemID")
    fun getUser(itemID: Long): ItemInfo?
}