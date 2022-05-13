package com.meng.expirationdate.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class BaseApplication : Application()  {
    private var activityList: ArrayList<AppCompatActivity> = ArrayList()

    companion object {
        private var instance: BaseApplication? = null
        fun instance() = instance ?: throw Throwable("instance 还未初始化完成")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun addActivity(activity: AppCompatActivity) {
        activityList.add(activity)
    }

    fun removeActivity(a: AppCompatActivity) {
        activityList.remove(a)
    }

    fun getActivityList(): ArrayList<AppCompatActivity>? {
        return if (activityList.size > 0) {
            activityList
        } else null
    }

    fun getTopActivity(): AppCompatActivity? {
        return if (0 != activityList.size) {
            activityList[activityList.size - 1]
        } else null
    }
}