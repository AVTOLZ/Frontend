package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.RootComponent
import ui.admin.AdminComponent
import ui.admin.children.events.EventsComponent
import ui.admin.children.events.EventsScreen
import ui.admin.children.members.MembersComponent
import ui.admin.children.members.MembersScreen

@Composable
fun AdminScreen(component: AdminComponent) {
    val dialog = component.dialog.subscribeAsState()
    val overlay = dialog.value.child ?: return

    val items = listOf(
        AdminComponent.Config.Members,
        AdminComponent.Config.Events
    )

    Scaffold(
        bottomBar = {
            NavigationBar(contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                items.forEach { screen ->
                    val icon = when (screen) {
                        is AdminComponent.Config.Members -> { Icons.Default.Person }
                        is AdminComponent.Config.Events -> { Icons.Default.Add }
                        else -> { Icons.Default.Settings }
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

                NavigationBarItem(
                    icon = { Icon(Icons.Default.ArrowBack, "") },
                    label = { Text("Back") },
                    selected = false,
                    onClick = {
                        component.parent.clearStack(RootComponent.Config.Main)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val dialogComponent = overlay.instance) {
                is MembersComponent -> MembersScreen(dialogComponent)
                is EventsComponent -> EventsScreen(dialogComponent)
                else -> {}
            }
        }
    }
}