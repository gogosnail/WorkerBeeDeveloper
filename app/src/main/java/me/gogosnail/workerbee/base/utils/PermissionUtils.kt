package me.gogosnail.workerbee.base.utils

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.util.ArrayList

/** Created by max on 2019/1/15.<br/>
 */
object PermissionUtils {

    fun getManifestPermissions(activity: Activity): Array<String> {
        var packageInfo: PackageInfo? = null
        val list = ArrayList<String>(1)
        try {
            packageInfo = activity.packageManager.getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        if (packageInfo != null) {
            val permissions = packageInfo.requestedPermissions
            if (permissions != null) {
                for (perm in permissions) {
                    list.add(perm)
                }
            }
        }
        return list.toTypedArray()
    }
}