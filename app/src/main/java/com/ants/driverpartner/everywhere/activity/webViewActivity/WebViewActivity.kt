package com.ants.driverpartner.everywhere.activity.webViewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private var url = "http://dev.tekzee.in/Ants/get_staticPage/20/2"
    private var title = ""

    lateinit var binding:ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        url = intent.getStringExtra(Constant.WEB_URL)
        title = intent.getStringExtra(Constant.WEB_VIEW_TITLE)

        binding.tvTitle.text = title

        onPageLoad(url)

        binding.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }


    fun onPageLoad(url: String) {
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
        binding.webView.loadUrl(url)

    }

    fun onFail(error_message: String) {
        // snackBarTop(R.id.drawerLayout, error_message)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
