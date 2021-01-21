package com.xbyoung.lib.manager

import android.app.Activity

object ActivityManager {
    private val activities by lazy { mutableListOf<Activity>() }

    @Synchronized
    fun add(activity: Activity) {
        if (activities.contains(activity)) {
            activities.remove(activity)
        }
        activities.add(activity)
    }

    @Synchronized
    fun remove(activity: Activity) {
        if (activities.contains(activity)) {
            if (!activity.isDestroyed || !activity.isFinishing) {
                activity.finish()
            }
            activities.remove(activity)
        }
    }

    @Synchronized
    fun removeAll() {
        activities.forEach {
            it?.finish()
        }
        activities.clear()
    }
}