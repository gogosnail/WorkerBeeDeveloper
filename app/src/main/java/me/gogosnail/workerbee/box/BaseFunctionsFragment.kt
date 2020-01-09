package me.gogosnail.workerbee.box

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_base_functions.view.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseFragment
import me.gogosnail.workerbee.box.setting.SettingFunctionsModel
import me.gogosnail.workerbee.box.test.TestFunctionsModel
import me.gogosnail.workerbee.data.FUNCTION_BOX_SETTING
import me.gogosnail.workerbee.data.FUNCTION_BOX_TEST
import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/26.<br/>
 * 功能列表基类
 */
open class BaseFunctionsFragment: BaseFragment() {
    protected val functions = mutableListOf<FunctionsItem>()
    override fun layoutId(): Int = R.layout.fragment_base_functions
    private val adapter = FunctionsAdapter()
    var fragmentTag = "default"
    companion object {
        fun creator(tag:String, builder: BaseFunctionsFragment.() ->  Unit): BaseFunctionsFragment {
            val fragment = BaseFunctionsFragment()
            fragment.fragmentTag = tag
            builder.invoke(fragment)
            return fragment
        }
    }

    /**
     * 添加功能列表
     */
    open fun initFunctions() {
        val list = when(fragmentTag) {
            FUNCTION_BOX_SETTING -> SettingFunctionsModel().loadFunctions()
//            FUNCTION_BOX_APP -> AppFunctionsModel().loadFunctions()
            FUNCTION_BOX_TEST -> TestFunctionsModel().loadFunctions()
//            FUNCTION_BOX_NETWORK -> NetworkFunctionsModel().loadFunctions()
            else -> emptyList<FunctionsItem>()
        }
        functions.clear()
        functions.addAll(list)
    }

    private fun initData() {
        initFunctions()
        adapter.putData(functions)
    }

    override fun initView(rootView: View) {
        val layoutManager = LinearLayoutManager(this.context)
        val rv = rootView.rv_content
        rv.adapter = adapter
        rv.layoutManager = layoutManager
        initData()
    }

}