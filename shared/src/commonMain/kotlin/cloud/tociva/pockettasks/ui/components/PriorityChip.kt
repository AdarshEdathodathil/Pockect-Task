package cloud.tociva.pockettasks.ui.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cloud.tociva.pockettasks.model.TaskPriority

@Composable
fun PriorityChip(
    priority: TaskPriority
) {

    val (label, color) = when (priority) {

        TaskPriority.HIGH ->
            "🔴 HIGH" to MaterialTheme.colorScheme.errorContainer

        TaskPriority.NORMAL ->
            "🟡 NORMAL" to MaterialTheme.colorScheme.secondaryContainer

        TaskPriority.LOW ->
            "🟢 LOW" to MaterialTheme.colorScheme.tertiaryContainer

    }

    AssistChip(
        onClick = { },
        label = {
            Text(label)
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color
        )
    )

}