package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceScreen
import ui.main.children.settings.DefaultSettingsComponent
import ui.main.children.settings.SettingsScreen

@Composable
fun MainScreen(component: MainComponent) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    val items = listOf(
        MainComponent.Config.Presence,
        MainComponent.Config.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(screen.text) },
                        selected = overlay.configuration == screen,
                        onClick = {
                            component.navigateTo(screen)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val dialogComponent = overlay.instance) {
                is DefaultPresenceComponent -> PresenceScreen(dialogComponent)
                is DefaultSettingsComponent -> SettingsScreen(dialogComponent)
                else -> {}
            }
        }
    }
}