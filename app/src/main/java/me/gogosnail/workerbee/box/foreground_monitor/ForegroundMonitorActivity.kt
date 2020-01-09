package me.gogosnail.workerbee.box.foreground_monitor

import android.os.Bundle
import kotlinx.android.synthetic.main.item_common_single_text_setting.view.*
import kotlinx.android.synthetic.main.item_common_text_with_checkbox_setting.view.*
import kotlinx.android.synthetic.main.tool_foreground_monitor_activity.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_FOREGROUND_MONITOR_ENABLE
import me.gogosnail.workerbee.base.environment.CrossPreferenceManager
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.SystemSettingsUtils

/** Created by max on 2019/3/24.<br/>
 * 前台Window监听
 */
class ForegroundMonitorActivity : BaseActivity() {
    private val crossSharedPreference = CrossPreferenceManager.getDefaultSharedPreference(this)
    override fun layoutId() = R.layout.tool_foreground_monitor_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ll_floating_permission.let {
            it.setOnClickListener {
                SystemSettingsUtils.toOverlayPermissionManage(this, packageName)
            }
            it.tv_name.setText(R.string.authority_floating)
        }
        ll_service.let {
            it.setOnClickListener {
                SystemSettingsUtils.toAccessibilitySetting(this)
            }
            it.tv_name.setText(R.string.install_access_manage_service)
        }
        ll_switch.tv_check_box_setting.setText(R.string.foreground_monitor_function_switch)
        ll_switch.cb_check_box_setting.isChecked = crossSharedPreference.getBoolean(SP_FOREGROUND_MONITOR_ENABLE, true)
        ll_switch.setOnClickListener {
            ll_switch.cb_check_box_setting.isChecked = !ll_switch.cb_check_box_setting.isChecked
            crossSharedPreference.putBoolean(SP_FOREGROUND_MONITOR_ENABLE, ll_switch.cb_check_box_setting.isChecked)
        }
    }
}