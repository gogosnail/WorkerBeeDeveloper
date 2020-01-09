package me.gogosnail.workerbee.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/** Created by max on 2018/12/26.<br/>
 */
abstract class BaseFragment:Fragment() {

    @LayoutRes
    abstract fun layoutId():Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(layoutId(), container, false)
        initView(view)
        return view
    }

    protected open fun initView(rootView: View) {

    }
}