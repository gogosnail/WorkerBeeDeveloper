package me.gogosnail.workerbee.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.widget.CommonTitleBar
import java.util.*

/** Created by max on 2018/12/25.<br/>
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var titlebar:CommonTitleBar
    lateinit var titleTv:TextView
    lateinit var backTv:TextView
    lateinit var moreTv:TextView

    private var disposableList: MutableList<Disposable> = ArrayList()

    var disposeConsumer: Consumer<Disposable> = Consumer { d -> disposableList.add(d) }

    open var supportCommonTitleBar = true

    @LayoutRes
    abstract fun layoutId():Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initCommonTitleBar()
        initView()
        overridePendingTransition(R.anim.trans_slide_in_right, R.anim.trans_slide_out_left)
    }

    open fun initView() {

    }

    private fun initCommonTitleBar() {
        if (!supportCommonTitleBar) return
        titlebar = findViewById(R.id.title_bar)
        titleTv = titlebar.titleTv
        backTv = titlebar.backTv
        moreTv = titlebar.moreTv
        backTv.setOnClickListener {
            onBackClick()
        }
        moreTv.setOnClickListener {
            onMoreClick()
        }
    }

    open fun onBackClick() {
        onBackPressed()
    }

    open fun onMoreClick() {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposableList.forEach { it.dispose() }
        disposableList.clear()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.trans_slide_in_left, R.anim.trans_slide_out_right)
    }
}