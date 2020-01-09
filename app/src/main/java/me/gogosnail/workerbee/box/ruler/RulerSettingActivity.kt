package me.gogosnail.workerbee.box.ruler

import android.view.Gravity
import kotlinx.android.synthetic.main.activity_ruler_setting.*
import kotlinx.android.synthetic.main.item_common_single_text_setting.view.*
import kotlinx.android.synthetic.main.item_common_text_with_checkbox_setting.view.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CommonSpKey
import me.gogosnail.workerbee.base.environment.CommonPreferenceManager
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.SystemSettingsUtils

/** Created by max on 2019/12/16.<br/>
 * 尺子设置页面
 */
class RulerSettingActivity: BaseActivity() {
    override fun layoutId() = R.layout.activity_ruler_setting

    override fun initView() {
        ll_use_dp.tv_check_box_setting.setText(R.string.ruler_unit_use_dp)
        ll_use_dp.cb_check_box_setting.isChecked = CommonPreferenceManager.getBoolean(CommonSpKey.SP_RULER_USE_DIP_UNIT, true)
        ll_use_dp.setOnClickListener {
            ll_use_dp.cb_check_box_setting.isChecked = !ll_use_dp.cb_check_box_setting.isChecked
            CommonPreferenceManager.putBoolean(CommonSpKey.SP_RULER_USE_DIP_UNIT, ll_use_dp.cb_check_box_setting.isChecked)
        }
        ll_switch.tv_name.setCompoundDrawablesRelativeWithIntrinsicBounds(0 , 0, 0, 0)
        ll_switch.tv_name.setText(if (RulerWindowManager.isWindowShown()) R.string.close else R.string.open)
        ll_switch.tv_name.gravity = Gravity.CENTER
        ll_switch.setOnClickListener {
            RulerWindowManager.showWindow(!RulerWindowManager.isWindowShown())
            ll_switch.tv_name.setText(if (RulerWindowManager.isWindowShown()) R.string.close else R.string.open)
        }
        ll_floating_permission.let {
            it.setOnClickListener {
                SystemSettingsUtils.toOverlayPermissionManage(this, packageName)
            }
            it.tv_name.setText(R.string.authority_floating)
        }
    }
}