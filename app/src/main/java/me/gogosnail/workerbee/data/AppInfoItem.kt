package me.gogosnail.workerbee.data

import android.graphics.drawable.Drawable

/** Created by max on 2018/12/27.<br/>
 */
class AppInfoItem(val name: String,
                  val packageName: String,
                  val versionName: String ?= "0.0",
                  val versionCode: Int ?= 0,
                  val icon: Drawable ?= null)