package me.gogosnail.workerbee.base

/** Created by max on 2018/12/26.<br/>
 */
const val FUNCTION_TAG = "function_tag"

class CrossSpKey {
    companion object {
        const val SP_SYSTEM_ACCOUNT_PASSWORD = "system_account_password"
        const val SP_AUTO_INSTALL_ENABLE = "auto_install_enable"
        const val SP_FOREGROUND_MONITOR_ENABLE = "foreground_monitor_enable"
    }
}

class CommonSpKey {
    companion object {
        const val SP_RULER_USE_DIP_UNIT = "use_dip_as_ruler_unit"
        const val SP_RULER_CONTROL_LOCATION_PORTRAIT_X = "ruler_control_location_portrait_x"
        const val SP_RULER_CONTROL_LOCATION_PORTRAIT_Y = "ruler_control_location_portrait_y"
        const val SP_RULER_CONTROL_LOCATION_LAND_X = "ruler_control_location_land_x"
        const val SP_RULER_CONTROL_LOCATION_LAND_Y = "ruler_control_location_land_y"

        const val SP_FWM_CONTROL_LOCATION_PORTRAIT_X = "fwm_control_location_portrait_x"
        const val SP_FWM_CONTROL_LOCATION_PORTRAIT_Y = "fwm_control_location_portrait_y"
        const val SP_FWM_CONTROL_LOCATION_LAND_X = "fwm_control_location_land_x"
        const val SP_FWM_CONTROL_LOCATION_LAND_Y = "fwm_control_location_land_y"

        const val SP_OBSERVER_APP_PACKAGE = "observer_app_package"//全局观察应用
        const val SP_OBSERVER_APP_NAME = "observer_app_name"//全局观察应用
        const val SP_APP_LIST_FILTER = "app_filter"//应用列表过滤方式
    }
}