package me.gogosnail.workerbee.box

import kotlinx.android.synthetic.main.viewholder_function.view.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.CommonAdapter
import me.gogosnail.workerbee.base.ui.CommonViewHolder
import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/27.<br/>
 * 功能列表
 */
class FunctionsAdapter : CommonAdapter<FunctionsItem>() {

    override fun viewHolderLayoutId(): Int = R.layout.viewholder_function

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val functionsItem = data[position]
        val nameTv = holder.itemView.tv_name
        val descTv = holder.itemView.tv_desc
        nameTv.text = functionsItem.title
        descTv.text = functionsItem.desc
        holder.itemView.setOnClickListener {
            if (functionsItem.onClick?.invoke() != true) {
                holder.itemView.context.startActivity(functionsItem.intentInfo.buildIntent())
            }
        }
    }
}