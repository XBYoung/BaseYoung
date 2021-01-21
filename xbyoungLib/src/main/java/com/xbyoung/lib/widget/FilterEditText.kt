package com.xbyoung.lib.widget

import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import com.xbyoung.lib.R
import java.util.regex.Pattern


open class FilterEditText : androidx.appcompat.widget.AppCompatEditText {
    private var isAllowEmoji = true
    private var isAllowSpace = true
    private var isAllowSpecial = true
    private var filterMaxLength = -1
    private var filterArray = arrayListOf<InputFilter>()


    var doAfter: ((s: String) -> Unit)? = null

    private var mContext: Context? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context?.obtainStyledAttributes(attrs, R.styleable.FilterEditText)?.apply {
            isAllowEmoji = getBoolean(R.styleable.FilterEditText_allowEmoji, true)
            isAllowSpace = getBoolean(R.styleable.FilterEditText_allowSpace, true)
            isAllowSpecial = getBoolean(R.styleable.FilterEditText_allowSpecial, true)
            filterMaxLength = getInteger(R.styleable.FilterEditText_filterMaxLength, -1)
            if (!isAllowEmoji) {
                filterArray.add(emojiFilter)
            }
            if (!isAllowSpace) {
                filterArray.add(spaceFilter)
            }
            if (!isAllowSpecial) {
                filterArray.add(specialFilter)
            }
            if (filterMaxLength >= 0) {
                filterArray.add(InputFilter.LengthFilter(filterMaxLength))
            }
            recycle()
        }
        initEditText(context)
        Log.d(
            "filter",
            "isAllowEmoji = $isAllowEmoji  isAllowSpace = $isAllowSpace  isAllowSpecial = $isAllowSpecial"
        )
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    private val specialFilter = InputFilter { source, _, _, _, _, _ ->
        if (isSpecialText(source.toString())) {
            Toast.makeText(mContext, "不支持特殊字符", Toast.LENGTH_SHORT).show()
            ""
        } else {
            null
        }
    }
    private val spaceFilter = InputFilter { source, start, end, _, _, _ ->
        if (source.isBlank() && end > start) {
            Toast.makeText(mContext, "不支持空格", Toast.LENGTH_SHORT).show()
            ""
        } else {
            null
        }
    }
    val emojiFilter = InputFilter { source, _, _, _, _, _ ->
        if (containsEmoji(source.toString())) {
            ""
        } else {
            null
        }
    }


    // 初始化edittext 控件
    private fun initEditText(context: Context?) {
        this.mContext = context
        val forbidFilters = arrayOfNulls<InputFilter>(filterArray.count())
        for (i in 0 until filterArray.count()) {
            forbidFilters[i] = filterArray[i]
        }
        filters = forbidFilters
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                doAfter?.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    private fun containsEmoji(source: String): Boolean {

        val len = source.length
        for (i in 0 until len) {
            val codePoint = source[i]
            if (!isEmojiCharacter(codePoint)) {
                //如果不能匹配,则该字符是Emoji表情
                Toast.makeText(mContext, "不支持输入表情符号", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }


    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return !(codePoint.toInt() != 0x0 && codePoint.toInt() != 0x9 && codePoint.toInt() != 0xA && codePoint.toInt() != 0xD && codePoint.toInt() !in 0x20..0xD7FF && codePoint.toInt() !in 0xE000..0xFFFD && codePoint.toInt() !in 0x10000..0x10FFFF)
    }


    private fun isSpecialText(codePoint: String): Boolean {
        val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(speChat);
        val matcher = pattern.matcher(codePoint)
        return matcher.find()
    }

}