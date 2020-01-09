package me.gogosnail.workerbee.base.extends

import me.gogosnail.workerbee.BaseApplication

/** Created by max on 2018/12/28.<br/>
 */
inline val Int.dipInt: Int
    get() = this.dip.toInt()

inline val Float.dipInt: Int
    get() = this.dip.toInt()

inline val Int.dip: Float
    get() = (BaseApplication.INSTANCE.resources.displayMetrics.density * this) + 0.5f

inline val Float.dip: Float
    get() = (BaseApplication.INSTANCE.resources.displayMetrics.density * this) + 0.5f
