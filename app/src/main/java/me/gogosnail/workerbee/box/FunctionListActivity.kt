package me.gogosnail.workerbee.box

import android.os.Bundle
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.ui.BaseFragment
import me.gogosnail.workerbee.base.FUNCTION_TAG
import me.gogosnail.workerbee.data.FUNCTION_BOX_SETTING

/** Created by max on 2018/12/26.<br/>
 */
class FunctionListActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_function_list
    lateinit var fragment: BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun initView() {
        super.initView()
    }

    private fun initData() {
        initCategory()
    }

    private fun initCategory() {
        val tag = intent?.getStringExtra(FUNCTION_TAG) ?: FUNCTION_BOX_SETTING
        val nameId = resources.getIdentifier("function_box_$tag", "string", BaseApplication.INSTANCE.packageName)
        titleTv.setText(nameId)
        supportFragmentManager.beginTransaction()
        val ft = supportFragmentManager.beginTransaction()
        val fragment = BaseFunctionsFragment.creator(tag) {}
        ft.add(R.id.fl_content, fragment, tag)
        ft.commit()
    }
}