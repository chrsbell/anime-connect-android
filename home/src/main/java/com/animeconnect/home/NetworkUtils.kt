package com.animeconnect.home

import android.webkit.CookieManager
import android.webkit.WebView
import java.net.URLDecoder

class NetworkUtils(webView: WebView) {
    companion object {
        private val API_HOST_NAME =
            if (BuildConfig.DEBUG) {
                when (IS_EMULATOR) {
                    // https://developer.android.com/studio/run/emulator-networking#networkaddresses
                    true -> "10.0.2.2"
                    false -> "192.168.1.9"
                }
            } else ""

        private const val PORT = 3000
        val LOGIN_URL = "http://$API_HOST_NAME:$PORT/login"
        val CALLBACK_URL = "http://$API_HOST_NAME:$PORT/callback"
    }

    init {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, false)
    }

    private fun decodeURIComponent(param: String) = URLDecoder.decode(
        param.replace("+", "%2B"), "UTF-8"
    ).replace("%2B", "+")

    fun getAndDecodeCookie(): String? {
        return CookieManager.getInstance().getCookie(API_HOST_NAME)?.apply {
            decodeURIComponent(this)
        }
    }
}