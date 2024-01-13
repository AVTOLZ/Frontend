package ui.admin.children.members

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.admin.children.Table

@Composable
fun MembersScreen(component: MembersComponent) {
    val members = component.members.subscribeAsState().value

    Table(
        modifier = Modifier.fillMaxSize(),
        columnCount = 9,
        rowCount = members.size + 1,
    ) { row, col ->
        if (row == 0) {
            Header(col)
            return@Table
        }

        val member = members[row - 1]

        when (col) {
            0 /* ID */-> TableStringTextItem(member.id.toString())
            1 /* Username */-> TableStringTextItem(member.username.toString())
            2 /* Password */-> TableStringTextItem("******")
            3 /* Email */-> TableStringTextItem(member.email.toString())
            4 /* First name */-> TableStringTextItem(member.firstName.toString())
            5 /* Last name */-> TableStringTextItem(member.lastName.toString())
            6 /* Student ID */-> TableStringTextItem(member.studentId.toString())
            7 /* Rank */-> TableStringTextItem(member.rank.toString())
            8 /* State */-> TableStringTextItem(member.state.toString())
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
fun TableItem(contents: @Composable BoxScope.() -> Unit) {
    Box(Modifier.padding(end = 10.dp, bottom = 5.dp)) {
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