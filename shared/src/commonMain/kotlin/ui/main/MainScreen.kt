package ui.main

import Data
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import api.person.info.AVTRanks
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.RootComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceScreen
import ui.main.children.settings.DefaultSettingsComponent
import ui.main.children.settings.SettingsScreen
import ui.main.icons.CalendarTodayIcon

@Composable
fun MainScreen(component: MainComponent) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    val items = listOf(
        MainComponent.Config.Presence,
        MainComponent.Config.Settings
    )

    Scaffold(
        snackbarHost = { SnackbarHost(component.snackbarHost) },
        bottomBar = {
            NavigationBar(contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    val icon = when (screen) {
                        is MainComponent.Config.Presence -> CalendarTodayIcon
                        is MainComponent.Config.Settings -> Icons.Default.Settings
                        else -> throw RuntimeException("Icon not defined") // yes, this else branch is needed
                    }

                    NavigationBarItem(
                        icon = { Icon(icon, screen.text) },
                        label = { Text(screen.text) },
                        selected = overlay.configuration == screen,
                        onClick = {
                            component.navigateTo(screen)
                        }
                    )
                }

                if (Data.userRank >= AVTRanks.Hoofd.order) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Settings") },
                        selected = overlay.configuration == MainComponent.Config.Settings,
                        onClick = {
                            component.parent.navigateTo(RootComponent.Config.Admin)
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