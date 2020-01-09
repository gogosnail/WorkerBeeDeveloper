package me.gogosnail.workerbee.base.extends

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/** Created by max on 2018/12/25.<br/>
 */

fun <T> observableIOSwitch(): ObservableTransformer<T, T> {
    return ObservableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
}
