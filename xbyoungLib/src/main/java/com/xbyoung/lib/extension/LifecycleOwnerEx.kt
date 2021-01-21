package com.xbyoung.lib.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import java.util.ArrayList


 const val TAG_EXTRA = "tag_xbyLib"

inline fun AppCompatActivity.createVp(vp: ViewPager, fragments: ArrayList<out Fragment>) {

    vp.adapter = object :
        FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(p0: Int): Fragment {
            return fragments[p0]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}

inline fun LifecycleOwner.show(message: Any) {
    showToast(message.toString(), Toast.LENGTH_SHORT)
}


inline fun LifecycleOwner.longShow(message: Any) {
    showToast(message.toString(), Toast.LENGTH_LONG)
}


inline fun LifecycleOwner.shortShow(message: Any?) {
    showToast(message.toString(), Toast.LENGTH_SHORT)
}

inline fun LifecycleOwner.showToast(message: String, time: Int) {
    nowContext()?.let {
        Toast.makeText(it, message, time).show()
    }
}


inline fun LifecycleOwner.nowContext(): Context? {
    return when (this) {
        is AppCompatActivity -> {
            this
        }
        is Fragment -> {
            context
        }
        else -> {
            throw IllegalArgumentException("不支持该类型 ${this::class.java.simpleName}")
            null
        }
    }
}

inline fun LifecycleOwner.nowActivity(): Activity? {
    return when (this) {
        is AppCompatActivity -> {
            this
        }
        is Fragment -> {
            activity
        }
        else -> {
            throw IllegalArgumentException("不支持该类型 ${this::class.java.simpleName}")
            null
        }
    }
}


inline fun LifecycleOwner.startActivity(cls: Class<*>) {
    nowActivity()?.let {
        it.startActivity(Intent(it, cls))
    }
}

inline fun LifecycleOwner.startActivityWithFinish(cls: Class<*>) {
    nowActivity()?.let {
        startActivity(cls)
        it.finish()
    }
}

inline fun LifecycleOwner.startActivityWithGift(cls: Class<*>, gift: String) {
    nowContext()?.let {
        it.startActivity(Intent(it, cls).apply {
            putExtra(TAG_EXTRA, gift)
        })
    }
}

inline fun LifecycleOwner.startActivityWithGift(cls: Class<*>, gift: Int) {
    nowContext()?.let {
        it.startActivity(Intent(it, cls).apply {
            putExtra(TAG_EXTRA, gift)
        })
    }
}

inline fun LifecycleOwner.startActivityWithGift(cls: Class<*>, gift: Parcelable) {
    nowContext()?.let {
        it.startActivity(Intent(it, cls).apply {
            putExtra(TAG_EXTRA, gift)
        })
    }
}


inline fun <A : Parcelable, T : ArrayList<A>> LifecycleOwner.startActivityWithGift(
    cls: Class<*>,
    gift: T
) {
    nowContext()?.let {
        it.startActivity(Intent(it, cls).apply {
            putParcelableArrayListExtra(TAG_EXTRA, gift)
        })
    }
}


inline fun Activity.getStringGift(): String? {
    return intent.getStringExtra(TAG_EXTRA)
}

inline fun <T : Parcelable> Activity.getParcelableGift(): T? {
    return intent.getParcelableExtra(TAG_EXTRA) as? T
}


inline fun LifecycleOwner.getResColor(@ColorRes res: Int): Int {
    nowContext()?.let {
        return it.resources.getColor(res)
    }
    return 0
}





