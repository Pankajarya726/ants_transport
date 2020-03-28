package com.ants.driverpartner.everywhere.activity.webView

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityWebViewBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils

class WebViewActivity : BaseMainActivity(),
    com.ants.driverpartner.everywhere.activity.webView.WebView {

    private var title = ""
    private var pageId = 0;

    private var presenter: WebViewPresenter? = null

    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        presenter = WebViewPresenter(this, this)
//        url = intent.getStringExtra(Constant.WEB_URL)
        title = intent.getStringExtra(Constant.WEB_VIEW_TITLE)
        pageId = intent.getIntExtra(Constant.WEB_VIEW_PAGE_ID, 0)

        binding.tvTitle.text = title

        presenter!!.loadPage(pageId)

        binding.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }


    override  fun onPageLoad(data : GetWebViewResponse.Data) {
        val settings = binding.webView.getSettings()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE


        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                binding.progressBar.setVisibility(View.VISIBLE)
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.setVisibility(View.GONE)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                onFail("Error : $description")
            }
        })
        binding.webView.loadUrl(data.url)

    }

    fun onFail(error_message: String) {
        DialogUtils.showSuccessDialog(this, error_message)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun validateError(message: String) {

       DialogUtils.showSuccessDialog(this, message)
    }
}
