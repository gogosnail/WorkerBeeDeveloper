package me.gogosnail.workerbee.base.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_common_list.*
import kotlinx.android.synthetic.main.viewholder_dialog_list_item.view.tv_name
import kotlinx.android.synthetic.main.viewholder_single_select_dialog_list_item.view.*
import me.gogosnail.workerbee.R

class Option(val text: String, var onSelect: Boolean = false, var onClick: (() -> Unit)? = null)

/** Created by max on 2018/12/28.<br/>
 * 单选列表弹窗
 */
class SingleSelectListDialog(activity: Activity,
                             val options: List<Option>) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common_list)
        initData()
    }

    private fun initData() {
        val rv = rv_content
        rv.layoutManager = LinearLayoutManager(ownerActivity)
        rv.adapter = adapter
        adapter.putData(options)
    }

    private fun updateSelected(position: Int) {
        if (position in adapter.data.indices) {
            options.forEachIndexed { index, option ->
                val isChecked = position == index
                if (isChecked != option.onSelect) {
                    option.onSelect = isChecked
                    adapter.notifyItemChanged(index)
                }
            }
        }
    }

    val adapter = object : CommonAdapter<Option>() {
        override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
            val option = data[position]
            holder.itemView.tv_name.text = option.text
            holder.itemView.rb_select.isChecked = option.onSelect
            holder.itemView.rb_select.isClickable = false
            holder.itemView.setOnClickListener {
                updateSelected(position)
                option.onClick?.invoke()
                dismiss()
            }
        }

        override fun viewHolderLayoutId() = R.layout.viewholder_single_select_dialog_list_item

    }
}