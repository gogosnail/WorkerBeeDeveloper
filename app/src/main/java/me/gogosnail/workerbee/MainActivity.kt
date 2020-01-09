package me.gogosnail.workerbee

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import me.gogosnail.workerbee.base.extends.dip
import me.gogosnail.workerbee.base.extends.dipInt
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.Logger
import me.gogosnail.workerbee.box.AboutActivity
import me.gogosnail.workerbee.box.FunctionBoxItemViewHolder
import me.gogosnail.workerbee.box.FunctionBoxListModel
import me.gogosnail.workerbee.data.FunctionBoxBean
import java.util.*

class MainActivity : BaseActivity() {
    private var adapter: FunctionBoxAdapter? = null
    private var layoutManger: GridLayoutManager? = null
    private var functionBoxList: MutableList<FunctionBoxBean> = FunctionBoxListModel().createList()
    override fun layoutId(): Int = R.layout.activity_main
    private var itemWidth = 0
    companion object {
        const val ROW_COUNT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        itemWidth = resources.displayMetrics.widthPixels / ROW_COUNT - 8.dipInt
        adapter = FunctionBoxAdapter()
        layoutManger = GridLayoutManager(applicationContext, ROW_COUNT)
        rv_content.layoutManager = layoutManger
        rv_content.adapter = adapter
        adapter?.notifyDataSetChanged()
        Log.d("MainActivity", "initData functionBoxList=${functionBoxList.size}")
    }

    override fun onMoreClick() {
        val aboutIntent = Intent(this, AboutActivity::class.java)
        startActivity(aboutIntent)
        Settings.System.NAME
        Logger.d("MainActivity", "onMoreClick brand=${android.os.Build.BRAND} model=${android.os.Build.MODEL} " +
                "display=${android.os.Build.DISPLAY} product=${android.os.Build.PRODUCT} manufacturer=${android.os.Build.MANUFACTURER}" )
    }

    inner class FunctionBoxAdapter : RecyclerView.Adapter<FunctionBoxItemViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, position: Int): FunctionBoxItemViewHolder {
            val view = LayoutInflater.from(applicationContext).inflate(R.layout.viewholer_function_box, null)
            return FunctionBoxItemViewHolder(view)
        }

        override fun getItemCount() = functionBoxList.size

        override fun onBindViewHolder(vh: FunctionBoxItemViewHolder, position: Int) {
            Logger.d("MainActivity", "onBindViewHolder")
            val functionBox = functionBoxList[position]
            vh.iconTv.setImageDrawable(functionBox.icon)
            val random = Random()
            val color = Color.argb(25, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            val bg = ColorDrawable(color)
            vh.contentLl.setBackgroundDrawable(bg)
            vh.nameTv.text = functionBox.name
            vh.nameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, measureTextSize(functionBox.name, itemWidth))
            vh.itemView.setOnClickListener {
                startActivity(functionBox.intentInfo.buildIntent())
            }
        }

        /**
         * 计算文字大小
         */
        private fun measureTextSize(text: String, maxWidth: Int): Float {
            val minSize = 10.dip
            val defaultSize = 16.dip
            val stepSize = 2
            var measureTextSize = defaultSize
            val paint = Paint()
            paint.textSize = measureTextSize
            var measureWidth = paint.measureText(text)
            while (measureWidth > maxWidth && measureTextSize > minSize) {
                measureTextSize -= stepSize
                paint.textSize = measureTextSize
                measureWidth = paint.measureText(text)
            }
            return measureTextSize
        }

    }
}
