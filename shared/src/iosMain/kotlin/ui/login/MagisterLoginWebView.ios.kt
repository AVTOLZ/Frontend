@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MagisterLoginWebView(getLoginUrl: () -> String, onUrlLoad: (url: String) -> Boolean) {
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration()
            val preferences = WKPreferences()
            preferences.javaScriptEnabled = true
            preferences.javaScriptCanOpenWindowsAutomatically = true

            config.setURLSchemeHandler(object : NSObject(), WKURLSchemeHandlerProtocol {
                override fun webView(
                    webView: WKWebView,
                    startURLSchemeTask: WKURLSchemeTaskProtocol
                ) {
                    if (!onUrlLoad(startURLSchemeTask.request.URL?.absoluteString!!)) {
                        webView.loadRequest(NSURLRequest(uRL = NSURL(string = getLoginUrl())))
                    }
                }

                override fun webView(
                    webView: WKWebView,
                    stopURLSchemeTask: WKURLSchemeTaskProtocol
                ) {
                }
            }, forURLScheme = "m6loapp")

            config.preferences = preferences

            val webView = object : WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config) {}

            webView.loadRequest(NSURLRequest(uRL = NSURL(string = getLoginUrl())))

            webView
        },
        update = { webView: WKWebView ->
        },
        modifier = Modifier.fillMaxSize(),
        interactive = true
    )
}