package me.gogosnail.workerbee.box.network.browser

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.webkit.*
import kotlinx.android.synthetic.main.activity_browser.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.Logger

/** Created by max on 2019/5/22.<br/>
 * 自定义WebView
 */
class BrowserActivity: BaseActivity() {

    companion object {
        const val TAG = "Browser"
    }
    lateinit var webView:WebView
    override fun layoutId(): Int {
        return R.layout.activity_browser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportCommonTitleBar = false
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        super.initView()
        webView = webview
        tv_go.setOnClickListener {
            et_address.text?.toString()?.let {
                var url = it
                if (!it.startsWith("http")) {
                    url = "http://$it"
                }
                webView.loadUrl(url)
            }
        }
        webView.webViewClient = WebClient()
        webView.settings.apply {
            javaScriptCanOpenWindowsAutomatically = true
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            defaultZoom = WebSettings.ZoomDensity.CLOSE
            useWideViewPort = true
            loadWithOverviewMode = true
            setAppCacheEnabled(true)
            domStorageEnabled = true
        }

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebClient()

        title_bar_back.setOnClickListener {
            finish()
        }
    }

    class WebChromeClient: android.webkit.WebChromeClient() {
    }

    class WebClient: WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            Logger.d(TAG, " shouldOverrideUrlLoading1 ${request?.url}")
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Logger.d(TAG, " shouldOverrideUrlLoading2 $url")
            return super.shouldOverrideUrlLoading(view, url)
        }
    }
}