package me.gogosnail.workerbee.base.ui

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/** Created by max on 2018/12/27.<br/>
 */
abstract class CommonAdapter<T>: RecyclerView.Adapter<CommonViewHolder>(){
    var data:MutableList<T> = mutableListOf()

    fun putData(list: List<T>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun viewHolderLayoutId():Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewHolderLayoutId(), null)
        return CommonViewHolder(itemView)
    }

    override fun getItemCount() = data.size
}

/** Created by max on 2018/12/26.<br/>
 */
class CommonViewHolder(view: View):RecyclerView.ViewHolder(view) {
}