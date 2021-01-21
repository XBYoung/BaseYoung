package com.xbyoung.lib.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.xbyoung.lib.R


/**
 * Created by luke on 2017/1/18.
 */
class BaseTitle : RelativeLayout {
    private var backEnable: Boolean = false
    private var settingEnable: Boolean = false
    private var textSettingEnable: Boolean = false
    private var title: String = ""
    private var textSetting: String = ""
    private var settingRes: Int = 0
    private var backRes: Int = 0
    private var contentColor: Int = 0
    private var bgRes: Int = 0
    private var titleSize: Int = 0
    private lateinit var tvTitle: TextView
    private lateinit var tvSetting: TextView
    private lateinit var viewBg: View
    private var mContext: Context
    private lateinit var backBtn: ImageView
    private lateinit var setting: ImageView

    var doBack = {
        (context as Activity).onBackPressed()
    }

    lateinit var doSettings: () -> Unit

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        LayoutInflater.from(context).inflate(R.layout.title, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseTitle, 0, 0)
        try {
            title = ta.getString(R.styleable.BaseTitle_title) ?: ""
            textSetting = ta.getString(R.styleable.BaseTitle_settingText) ?: ""
            backEnable = ta.getBoolean(R.styleable.BaseTitle_backEnable, false)
            settingEnable = ta.getBoolean(R.styleable.BaseTitle_settingEnable, false)
            textSettingEnable = ta.getBoolean(R.styleable.BaseTitle_settingEnable, false)
            settingRes = ta.getResourceId(R.styleable.BaseTitle_settingRes, 0)
            backRes = ta.getResourceId(R.styleable.BaseTitle_backRes, R.mipmap.back)
            bgRes =
                ta.getResourceId(R.styleable.BaseTitle_titleBackground, R.color.common_white)
            titleSize =
                ta.getDimensionPixelSize(R.styleable.BaseTitle_titleTextSize, sp2px(context, 18))
            contentColor =
                ta.getResourceId(R.styleable.BaseTitle_titleContentColor, android.R.color.black)
            setUpView()
        } finally {
            ta.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs)

    @SuppressLint("WrongConstant")
    private fun setUpView() {
        setBackgroundResource(bgRes)
        isClickable = true
        tvTitle = this.findViewById(R.id.title_tv_content)
        with(tvTitle) {
            text = title
            textSize = px2sp(mContext, titleSize.toFloat()).toFloat()
            setTextColor(resources.getColor(contentColor))
        }

        tvSetting = this.findViewById(R.id.title_tv_setting)
        with(tvSetting) {
            text = textSetting
            visibility = if (textSettingEnable) View.VISIBLE else View.GONE
            setTextColor(context.resources.getColor(contentColor))
        }


        backBtn = findViewById(R.id.title_iv_back)
        with(backBtn) {
            setImageResource(backRes)
            visibility = if (backEnable) View.VISIBLE else View.INVISIBLE
            setOnClickListener {
                doBack.invoke()
            }
        }




        if (settingRes != 0) {
            val moreImgView = findViewById<ImageView>(R.id.title_iv_setting)
            moreImgView.visibility = View.INVISIBLE
        }

        setting = findViewById(R.id.title_iv_setting)

        with(setting){
           visibility = if (settingEnable) View.VISIBLE else View.GONE

           setImageResource(settingRes)

           setOnClickListener {
                if (this@BaseTitle::doSettings.isInitialized) {
                    doSettings.invoke()
                }
            }
        }


        tvSetting.setOnClickListener {
            if (this::doSettings.isInitialized) {
                doSettings.invoke()
            }
        }

    }


    private fun sp2px(context: Context, spValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }


    @SuppressLint("WrongConstant")
            /**
             * 返回键是否可见
             * @param visible
             */
    fun setBackVisible(visible: Boolean) {
        backBtn.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @SuppressLint("WrongConstant")
    /**
     * setting字样是否可见
     * @param visible
     */
    open fun setSettingVisible(visible: Boolean) {
        setting?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        tvSetting?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @SuppressLint("WrongConstant")
    fun settingTextVisible() {
        tvSetting?.visibility = View.VISIBLE
        setting?.visibility = View.GONE
    }

    @SuppressLint("WrongConstant")
    fun settingResVisible() {
        tvSetting?.visibility = View.GONE
        setting?.visibility = View.VISIBLE

    }

    /**
     * title内容
     * @param title
     */
    fun setTitleText(title: String) {
        tvTitle.text = title
    }

    fun setSettingText(setting: String) {
        tvSetting?.text = setting
    }

    companion object {
        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }
    }

    fun touchBack(isCanTouchBack: Boolean) {
        isTouchBack = isCanTouchBack
    }

    private var isTouchBack = true
    private val BACK_DURATION = 500
    private val BACK_DISTANCE = 96f
    private var orgX = 0f
    private var orgY = 0f
    private var touchMillis = 0L
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                orgX = event.x
                orgY = event.y
                touchMillis = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                if (System.currentTimeMillis() - touchMillis > BACK_DURATION) {
                    touchMillis = System.currentTimeMillis()
                    orgX = event.x
                    orgY = event.y
                }
            }
            MotionEvent.ACTION_UP -> {
                val upX = event.x
                val upY = event.y
                if (System.currentTimeMillis() - touchMillis <= BACK_DURATION) {
                    if (upX.abs() - orgX.abs() > 0 && (upX.abs() - orgX.abs()).abs() > px2sp(
                            context,
                            BACK_DISTANCE
                        )
                    ) {
                        if (isTouchBack) {
                            (context as Activity).finish()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun Float.abs(): Float {
        return kotlin.math.abs(this)
    }


}
