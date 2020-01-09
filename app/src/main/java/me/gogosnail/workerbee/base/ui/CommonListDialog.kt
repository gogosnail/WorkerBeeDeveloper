package me.gogosnail.workerbee.base.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_common_list.*
import kotlinx.android.synthetic.main.viewholder_dialog_list_item.view.*
import me.gogosnail.workerbee.R

/** Created by max on 2018/12/28.<br/>
 * 列表弹窗
 */
class CommonListDialog(activity: Activity, val item: MutableList<String>, val onClick: ((position: Int) -> Unit)? = null) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common_list)
        initData()
    }

    private fun initData() {
        val rv = rv_content
        rv.layoutManager = LinearLayoutManager(ownerActivity)
        rv.adapter = adapter
        adapter.putData(item)
    }


    val adapter = object : CommonAdapter<String>() {
        override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
            val name = data[position]
            holder.itemView.tv_name.text = name
            holder.itemView.setOnClickListener {
                onClick?.invoke(position)
                dismiss()
            }
        }

        override fun viewHolderLayoutId() = R.layout.viewholder_dialog_list_item

    }
}