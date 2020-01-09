package me.gogosnail.workerbee.base.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_common_titlebar.view.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.extends.dip

/**
 * Created by max on 2018/12/25.<br></br>
 */
class CommonTitleBar : RelativeLayout {
    lateinit var titleTv: TextView
    lateinit var backTv: TextView
    lateinit var moreTv: TextView

    private var backVisible = true
    private var moreVisible = true
    private var titleSize = 16.dip
    private var leftTextSize = 16.dip
    private var rightTextSize = 16.dip
    private var titleColor = resources.getColor(R.color.icons)
    private var bgColor =resources.getColor(R.color.primary)
    private var backIconRes = R.drawable.ic_title_back
    private var moreIconRes = R.drawable.ic_title_more
    private var titleStr = 0

    constructor(context: Context) : super(context) {
        fillLayout()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttr(attrs)
    }

    @SuppressLint("ResourceType")
    private fun initAttr(attrs: AttributeSet?) {
        attrs?:return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar)
        if (ta != null) {
            bgColor = resources.getColor(ta.getResourceId(R.styleable.CommonTitleBar_bg_color, R.color.primary))
            backIconRes = ta.getResourceId(R.styleable.CommonTitleBar_back_ic, R.drawable.ic_title_back)
            backVisible = ta.getBoolean(R.styleable.CommonTitleBar_back_show, true)
            moreVisible = ta.getBoolean(R.styleable.CommonTitleBar_more_show, true)
            moreIconRes = ta.getResourceId(R.styleable.CommonTitleBar_more_ic, R.drawable.ic_title_more)
            titleStr = ta.getResourceId(R.styleable.CommonTitleBar_title_str, 0)
            titleColor = resources.getColor(ta.getResourceId(R.styleable.CommonTitleBar_title_color, R.color.icons))
            ta.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        fillLayout()
    }

    private fun fillLayout() {
        val layout:RelativeLayout = LayoutInflater.from(context).inflate(R.layout.layout_common_titlebar, null) as RelativeLayout
        titleTv = layout.title_bar_title
        backTv = layout.title_bar_back
        moreTv = layout.title_bar_more
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(0)
            layout.removeView(child)
            addView(child, child.layoutParams)
        }
        initView()
    }

    private fun initView() {
        backTv.visibility = if (backVisible) View.VISIBLE else View.INVISIBLE
        moreTv.visibility = if (moreVisible) View.VISIBLE else View.INVISIBLE
        backTv.setCompoundDrawablesWithIntrinsicBounds(backIconRes, 0, 0, 0)
        moreTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, moreIconRes, 0)
        titleTv.setTextColor(titleColor)
        if (titleStr > 0) titleTv.setText(titleStr)
        backTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
        moreTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        setBackgroundColor(bgColor)
    }
}
