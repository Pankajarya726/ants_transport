package com.ants.driverpartner.everywhere.activity.webView

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface WebView:BaseMainView {
    fun onPageLoad(data: GetWebViewResponse.Data)
}