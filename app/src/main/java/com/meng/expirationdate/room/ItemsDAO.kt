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

    @Query("SELECT * FROM ItemInfo ORDER BY itemId")
    fun getAllItems(): List<ItemInfo>

    @Query("SELECT * FROM ItemInfo WHERE itemId = :itemID")
    fun getItemById(itemID: Long): ItemInfo?

    @Query("SELECT * FROM ItemInfo WHERE itemName = :itemName")
    fun getItemByName(itemName: String): ItemInfo?

    @Query("SELECT * FROM ItemInfo WHERE itemName LIKE '%' || :name || '%' or itemType = :itemType")
    fun searchItem(name: String, itemType: Int = -1): List<ItemInfo>
}