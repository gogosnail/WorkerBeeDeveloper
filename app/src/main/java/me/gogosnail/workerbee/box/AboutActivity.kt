package me.gogosnail.workerbee.box

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.environment.CommonSettingsManger
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.ui.BundleKeys
import me.gogosnail.workerbee.box.app.AppListScanActivity

/** Created by max on 2018/12/28.<br/>
 */
class AboutActivity:BaseActivity() {
    override fun layoutId() = R.layout.activity_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAction()
    }

    private fun initAction() {
        ll_app_observer.setOnClickListener {
            val intent = Intent(this@AboutActivity, AppListScanActivity::class.java)
            intent.putExtra(BundleKeys.CHOOSE_APP_SCAN_ACTION, AppListScanActivity.ACTION_SET_OBSERVER)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    private fun updateData() {
        val name = CommonSettingsManger.getObserverAppName()
        val packageName = CommonSettingsManger.getObserverAppPackage()
        val info = "应用：$name\n包名：$packageName"
        tv_app_observer_info.text = info
    }
}