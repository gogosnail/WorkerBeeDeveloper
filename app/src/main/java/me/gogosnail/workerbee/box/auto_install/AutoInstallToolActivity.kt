package me.gogosnail.workerbee.box.auto_install

import android.os.Bundle
import kotlinx.android.synthetic.main.item_common_single_text_setting.view.*
import kotlinx.android.synthetic.main.item_common_text_with_checkbox_setting.view.*
import kotlinx.android.synthetic.main.tool_app_install_accessibility.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_AUTO_INSTALL_ENABLE
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_SYSTEM_ACCOUNT_PASSWORD
import me.gogosnail.workerbee.base.environment.CrossPreferenceManager
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.SystemSettingsUtils

/** Created by max on 2019/3/22.<br/>
 * 自动安装应用
 */
class AutoInstallToolActivity: BaseActivity() {
    private val crossSharedPreference = CrossPreferenceManager.getDefaultSharedPreference(this)

    companion object {
        const val DEFAULT_ACCOUNT_PASSWORD = "a1234567"
    }

    override fun layoutId() = R.layout.tool_app_install_accessibility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ll_service.let {
            it.tv_name.setText(R.string.install_access_manage_service)
            it.setOnClickListener {
                SystemSettingsUtils.toAccessibilitySetting(this)
            }
        }
        btn_save_password.setOnClickListener {
            val text = et_password.text?.toString()?:""
            crossSharedPreference.putString(SP_SYSTEM_ACCOUNT_PASSWORD, text)
        }
        val text = crossSharedPreference.getString(SP_SYSTEM_ACCOUNT_PASSWORD, DEFAULT_ACCOUNT_PASSWORD)
        et_password.setText(text)
        ll_switch.tv_check_box_setting.setText(R.string.install_function_switch)
        ll_switch.cb_check_box_setting.isChecked = crossSharedPreference.getBoolean(SP_AUTO_INSTALL_ENABLE, true)
        ll_switch.setOnClickListener {
            ll_switch.cb_check_box_setting.isChecked = !ll_switch.cb_check_box_setting.isChecked
            crossSharedPreference.putBoolean(SP_AUTO_INSTALL_ENABLE, ll_switch.cb_check_box_setting.isChecked)
        }
    }
}