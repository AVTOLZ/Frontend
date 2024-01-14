package ui.admin.children.members

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import api.admin.person.UserState
import api.person.info.AVTRanks
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.admin.children.Table

@Composable
fun MembersScreen(component: MembersComponent) {
    var expandedRank = remember { mutableStateOf(false) }
    var expandedState = remember { mutableStateOf(false) }
    val selectedRankFilters = remember { mutableStateListOf<AVTRanks>() }
    val selectedStateFilters = remember { mutableStateListOf<UserState>() }
    val members = component.members.subscribeAsState().value
    val ranks = AVTRanks.entries
    val states = UserState.entries

    val filteredMembers = members.filter {
        (selectedRankFilters.isEmpty() || selectedRankFilters.contains(it.rank)) &&
                (selectedStateFilters.isEmpty() || selectedStateFilters.contains(it.state))
    }

    Box(Modifier.fillMaxSize()) {
        Table(
            modifier = Modifier.fillMaxSize(),
            rowModifier = { i ->
                if (i == 0) Modifier.background(Color.Gray)
                else Modifier
            },
            columnCount = 9,
            rowCount = filteredMembers.size + 1,
        ) { row, col ->
            if (row == 0) {
                Header(col)
                return@Table
            }

            val member = filteredMembers[row - 1]

            when (col) {
                0 /* ID */ -> TableStringTextItem(member.id.toString())
                1 /* Username */ -> TableStringTextItem(member.username.toString())
                2 /* Password */ -> TableStringTextItem("******")
                3 /* Email */ -> TableStringTextItem(member.email.toString())
                4 /* First name */ -> TableStringTextItem(member.firstName.toString())
                5 /* Last name */ -> TableStringTextItem(member.lastName.toString())
                6 /* Student ID */ -> TableStringTextItem(member.studentId.toString())
                7 /* Rank */ -> TableStringTextItem(member.rank.toString())
                8 /* State */ -> TableStringTextItem(member.state.toString())
            }
        }
        Column(Modifier.align(Alignment.TopEnd)) {
            Box {
                Text("Select Rank", Modifier.clickable { expandedRank.value = true })
                DropdownMenu(
                    expanded = expandedRank.value,
                    onDismissRequest = { expandedRank.value = false }
                ) {
                    ranks.forEach { rank ->
                        DropdownMenuItem(onClick = {
                            if (!selectedRankFilters.contains(rank)) selectedRankFilters.add(rank)
                            else selectedRankFilters.remove(rank)
                        }, text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = selectedRankFilters.contains(rank), onCheckedChange = null)
                                Text(rank.name, Modifier.padding(10.dp))
                            }
                        })
                    }
                }
            }

            Box {
                Text("Select State", Modifier.clickable { expandedState.value = true })
                DropdownMenu(
                    expanded = expandedState.value,
                    onDismissRequest = { expandedState.value = false }
                ) {
                    states.forEach { state ->
                        DropdownMenuItem(onClick = {
                            if (!selectedStateFilters.contains(state)) selectedStateFilters.add(state)
                            else selectedStateFilters.remove(state)
                        }, text =  {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = selectedStateFilters.contains(state), onCheckedChange = null)
                                Text(state.name, Modifier.padding(10.dp))
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun Header(col: Int) {
    when (col) {
        0 /* ID */-> HeaderItem("ID")
        1 /* Username */-> HeaderItem("Username")
        2 /* Password */-> HeaderItem("Password")
        3 /* Email */-> HeaderItem("Email")
        4 /* First name */-> HeaderItem("First name")
        5 /* Last name */-> HeaderItem("Last name")
        6 /* Student ID */-> HeaderItem("Student ID")
        7 /* Rank */-> HeaderItem("Rank")
        8 /* State */-> HeaderItem("State")
    }
}

@Composable
fun TableItem(modifier: Modifier = Modifier, contents: @Composable BoxScope.() -> Unit) {
    Box(Modifier.padding(end = 10.dp, bottom = 5.dp).then(modifier)) {
        contents()
    }
}

@Composable
fun HeaderItem(text: String) {
    TableItem {
        Text(text)
    }
}

@Composable
fun TableStringTextItem(text: String) {
    TableItem {
        Text(text)
    }
}