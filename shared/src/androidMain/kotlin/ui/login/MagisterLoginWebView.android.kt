package ui.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun MagisterLoginWebView(getLoginUrl: () -> String, onUrlLoad: (url: String) -> Boolean) {
    AndroidView(factory = {
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                webResourceRequest: WebResourceRequest
            ): Boolean {
                if (!onUrlLoad(webResourceRequest.url.toString())) {
                    view.loadUrl(getLoginUrl())
                }

                return true
            }
        }

        val webView = WebView(it).apply {
            settings.javaScriptEnabled = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.webViewClient = webViewClient
            loadUrl(getLoginUrl())
        }

        webView
    }, update = {
        it.loadUrl(getLoginUrl())
    })
}

fun getActivity(context: Context?): Activity? {
    return if (context is ContextWrapper) {
        if (context is Activity) {
            context
        } else {
            getActivity(context.baseContext)
        }
    } else {
        null
    }
}