package me.gogosnail.workerbee.box.app

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_observer_app_options.*
import kotlinx.android.synthetic.main.viewholder_single_text.view.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.environment.CommonSettingsManger
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.ui.BundleKeys
import me.gogosnail.workerbee.base.ui.CommonAdapter
import me.gogosnail.workerbee.base.ui.CommonViewHolder
import me.gogosnail.workerbee.base.utils.SystemSettingsUtils

/** Created by max on 2018/12/28.<br/>
 */
class ObserverAppOptionsActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_observer_app_options
    private val options = mutableListOf<String>()
    private var currentAppName: String? = null
    private var currentAppPackageName: String? = null

    companion object {
        const val OPTION_DELETE = "卸载"
        const val OPTION_MANAGE_DETAIL = "应用管理"
        const val OPTION_OVERLAY_PERMISSION_MANAGE = "开启悬浮窗权限"
    }

    private val adapter = object : CommonAdapter<String>() {
        override fun viewHolderLayoutId() = R.layout.viewholder_single_text

        override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
            val option = data[position]
            val optionTv = holder.itemView.tv_name
            optionTv.text = option
            holder.itemView.setOnClickListener {
                when (option) {
                    OPTION_DELETE -> {
                        currentAppPackageName?.let { SystemSettingsUtils.toUninstallApp(this@ObserverAppOptionsActivity, it) }
                    }
                    OPTION_MANAGE_DETAIL -> {
                        currentAppPackageName?.let { SystemSettingsUtils.toApplicationDetail(this@ObserverAppOptionsActivity, it) }
                    }
                    OPTION_OVERLAY_PERMISSION_MANAGE -> {
                        currentAppPackageName?.let { SystemSettingsUtils.toOverlayPermissionManage(this@ObserverAppOptionsActivity, it) }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initAction()
    }

    private fun initAction() {
        ll_app_observer.setOnClickListener {
            val intent = Intent(this@ObserverAppOptionsActivity, AppListScanActivity::class.java)
            intent.putExtra(BundleKeys.CHOOSE_APP_SCAN_ACTION, AppListScanActivity.ACTION_SET_OBSERVER)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    private fun updateData() {
        currentAppName = CommonSettingsManger.getObserverAppName()
        currentAppPackageName = CommonSettingsManger.getObserverAppPackage()
        val info = "应用：$currentAppName\n包名：$currentAppPackageName"
        tv_app_observer_info.text = info
    }

    private fun initData() {
        options.add(OPTION_DELETE)
        options.add(OPTION_MANAGE_DETAIL)
        options.add(OPTION_OVERLAY_PERMISSION_MANAGE)
        val rv = rv_content
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        adapter.putData(options)
    }
}