package me.gogosnail.workerbee.box.app

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_app_scan.*
import kotlinx.android.synthetic.main.viewholder_app_info.view.*
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CommonSpKey
import me.gogosnail.workerbee.base.environment.CommonSettingsManger
import me.gogosnail.workerbee.base.extends.observableIOSwitch
import me.gogosnail.workerbee.base.ui.*
import me.gogosnail.workerbee.base.ui.BundleKeys.CHOOSE_APP_SCAN_ACTION
import me.gogosnail.workerbee.base.utils.ClipboardUtils
import me.gogosnail.workerbee.base.utils.SystemSettingsUtils
import me.gogosnail.workerbee.data.AppInfoItem

/** Created by max on 2018/12/27.<br/>
 * 应用列表扫描
 */
class AppListScanActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_app_scan
    private val longPressItem = mutableListOf<String>()
    private var currentFilter = FILTER_ONLY_THIRD

    companion object {
        const val ACTION_SET_OBSERVER = 1

        const val FILTER_ALL = 0//全部
        const val FILTER_ONLY_SYSTEM = 1//系统
        const val FILTER_ONLY_THIRD = 2//第三方
    }

    private var action = 0

    private val adapter = object : CommonAdapter<AppInfoItem>() {
        override fun viewHolderLayoutId() = R.layout.viewholder_app_info

        override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
            val appInfoItem = data[position]
            val nameTv = holder.itemView.tv_name
            val packageTv = holder.itemView.tv_package
            val iconTv = holder.itemView.iv_logo
            nameTv.text = appInfoItem.name + " (${appInfoItem.versionName}, ${appInfoItem.versionCode})"
            packageTv.text = appInfoItem.packageName
            iconTv.setImageDrawable(appInfoItem.icon)
            holder.itemView.setOnLongClickListener {
                val dialog = CommonListDialog(this@AppListScanActivity, longPressItem) { position ->
                    when (position) {
                        0 -> {
                            //观察
                            CommonSettingsManger.setObserverApp(appInfoItem.name, appInfoItem.packageName)
                            BaseToast.showToast("已设置\"${appInfoItem.name}\"为全局观察应用")
                            if (action == 1) {
                                finish()
                            }
                        }
                        1 -> {
                            //复制信息
                            val copyText = "${appInfoItem.name}\n${appInfoItem.packageName}\n${appInfoItem.versionName}\n${appInfoItem.versionCode}\n"
                            ClipboardUtils.setClip(applicationContext, copyText)
                        }
                        2 -> {
                            SystemSettingsUtils.toApplicationDetail(this@AppListScanActivity, appInfoItem.packageName)
                        }
                    }
                }
                dialog.show()
                true
            }
        }
    }


    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        scanAppList()
        moreTv.visibility = View.VISIBLE
        updateMoreOptions()
    }

    override fun initView() {
        val rv = rv_content
        rv.layoutManager = LinearLayoutManager(applicationContext)
        rv.adapter = adapter
        moreTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,0)
        moreTv.setTextColor(resources.getColor(R.color.white))
    }

    private fun updateMoreOptions() {
        val text = when(currentFilter) {
            FILTER_ONLY_THIRD -> "第三方应用"
            FILTER_ONLY_SYSTEM -> "系统应用"
            else -> "全部应用"
        }
        moreTv.text = text
    }

    override fun onMoreClick() {
        showFilterDialog()
    }

    @SuppressLint("CheckResult")
    private fun initData() {
        val options = BaseApplication.INSTANCE.resources.getStringArray(R.array.app_info_long_press)
        longPressItem.addAll(options)
        currentFilter = CommonSettingsManger.sp.getInt(CommonSpKey.SP_APP_LIST_FILTER, FILTER_ONLY_THIRD)
        action = intent?.getIntExtra(CHOOSE_APP_SCAN_ACTION, 0) ?: 0
    }

    @SuppressLint("CheckResult")
    private fun scanAppList() {
        loadingDialog.show()
        Observable.create<MutableList<AppInfoItem>> {
            val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            val appInfos = mutableListOf<AppInfoItem>()
            packages?.let { packages ->
                loop@ for (i in 0 until packages.size) {
                    val packageInfo = packages[i]
                    when (currentFilter) {
                        FILTER_ONLY_SYSTEM -> {
                            if (packageInfo.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) == 0) continue@loop
                        }
                        FILTER_ONLY_THIRD -> {
                            if (packageInfo.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) != 0) continue@loop
                        }
                    }
                    if (!packageInfo?.packageName.isNullOrEmpty()) {
                        val appInfo = AppInfoItem(packageInfo?.applicationInfo?.loadLabel(packageManager)?.toString()
                                ?: "null",
                                packageInfo.packageName,
                                packageInfo?.versionName,
                                packageInfo?.versionCode ?: 0,
                                packageInfo.applicationInfo.loadIcon(packageManager))
                        appInfos.add(appInfo)
                    }
                }
            }
            it.onNext(appInfos)
        }.compose(observableIOSwitch())
                .doOnSubscribe(disposeConsumer)
                .subscribe {
                    loadingDialog.dismiss()
                    adapter.putData(it)
                }
    }

    private fun updateFilter(filter: Int) {
        if (filter != currentFilter) {
            currentFilter = filter
            CommonSettingsManger.editor.putInt(CommonSpKey.SP_APP_LIST_FILTER, filter).apply()
            scanAppList()
            updateMoreOptions()
        }
    }

    private fun showFilterDialog() {
        val options = mutableListOf<Option>()
        options.add(Option("全部应用", currentFilter == FILTER_ALL) {
            updateFilter(FILTER_ALL)
        })
        options.add(Option("第三方应用", currentFilter == FILTER_ONLY_THIRD) {
            updateFilter(FILTER_ONLY_THIRD)
        })
        options.add(Option("系统应用", currentFilter == FILTER_ONLY_SYSTEM) {
            updateFilter(FILTER_ONLY_SYSTEM)
        })
        val dialog = SingleSelectListDialog(this, options)
        dialog.show()
    }
}