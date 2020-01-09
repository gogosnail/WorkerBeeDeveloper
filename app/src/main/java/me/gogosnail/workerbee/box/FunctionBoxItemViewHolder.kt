package me.gogosnail.workerbee.box

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.viewholer_function_box.view.*

/** Created by max on 2018/12/26.<br/>
 * 工具箱item
 */
class FunctionBoxItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val contentLl:LinearLayout = itemView.ll_item_content
    val nameTv:TextView = itemView.tv_name
    val iconTv:ImageView = itemView.iv_icon
}