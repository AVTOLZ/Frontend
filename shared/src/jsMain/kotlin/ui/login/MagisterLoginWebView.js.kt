package ui.login

import androidx.compose.runtime.Composable

@Composable
actual fun MagisterLoginWebView(getLoginUrl: () -> String, onUrlLoad: (url: String) -> Boolean) {}