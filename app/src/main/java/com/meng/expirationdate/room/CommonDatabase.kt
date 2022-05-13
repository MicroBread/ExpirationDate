package com.meng.expirationdate.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.meng.expirationdate.BuildConfig
import com.meng.expirationdate.base.BaseApplication

@Database(entities = [ItemInfo::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class CommonDatabase : RoomDatabase() {
    companion object {
        val instance: CommonDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(BaseApplication.instance(), CommonDatabase::class.java, "EXPIRATION_DATE.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    abstract val itemsDAO: ItemsDAO?
}