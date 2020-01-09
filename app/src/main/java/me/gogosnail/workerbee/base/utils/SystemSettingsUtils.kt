package me.gogosnail.workerbee.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


/** Created by max on 2018/12/28.<br/>
 */
object SystemSettingsUtils {

    /**
     * 跳转到系统设置
     */
    fun toSystemSettings(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * 跳转到应用详情
     */
    fun toApplicationDetail(context: Context, packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = Uri.fromParts("package", packageName, null)
        context.startActivity(intent)
    }

    /**
     * 卸载应用
     */
    fun toUninstallApp(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = Uri.fromParts("package", packageName, null)
        context.startActivity(intent)
    }

    /**
     * 浮窗权限管理
     */
    fun toOverlayPermissionManage(context: Context, packageName: String) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = Uri.fromParts("package", packageName, null)
        context.startActivity(intent)
    }

    /**
     * 跳转到辅助功能页面
     */
    fun toAccessibilitySetting(activity: Activity) {
        val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
        activity.startActivity(intent)
    }
}