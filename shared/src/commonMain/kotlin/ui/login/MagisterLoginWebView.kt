package ui.login

import androidx.compose.runtime.Composable

@Composable
expect fun MagisterLoginWebView(getLoginUrl: () -> String, onUrlLoad: (url: String) -> Boolean)