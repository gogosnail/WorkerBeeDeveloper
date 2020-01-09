package me.gogosnail.workerbee

import android.app.Application
import android.content.Context

/** Created by max on 2018/12/25.<br/>
 */
class BaseApplication :Application(){
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        INSTANCE = this
    }
    companion object {
        lateinit var INSTANCE: BaseApplication protected set
    }
}