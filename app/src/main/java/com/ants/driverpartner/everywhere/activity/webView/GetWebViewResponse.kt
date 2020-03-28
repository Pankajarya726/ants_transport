package com.ants.driverpartner.everywhere.activity.webView


import com.google.gson.annotations.SerializedName

data class GetWebViewResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("url")
        var url: String = ""
    )
}