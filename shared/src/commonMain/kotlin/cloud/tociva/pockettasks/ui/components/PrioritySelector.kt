
package cloud.tociva.pockettasks.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cloud.tociva.pockettasks.model.TaskPriority
import androidx.compose.ui.unit.dp

@Composable
fun PrioritySelector(
    priority: TaskPriority,
    onPriorityChanged: (TaskPriority) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        FilterChip(
            modifier = Modifier.weight(1f),
            selected = priority == TaskPriority.LOW,
            onClick = { onPriorityChanged(TaskPriority.LOW) },
            label = {
                Text("🟢 Low", maxLines = 1)
            }
        )

        FilterChip(
            modifier = Modifier.weight(1f),
            selected = priority == TaskPriority.NORMAL,
            onClick = { onPriorityChanged(TaskPriority.NORMAL) },
            label = {
                Text("🟡 Med", maxLines = 1)
            }
        )

        FilterChip(
            modifier = Modifier.weight(1f),
            selected = priority == TaskPriority.HIGH,
            onClick = { onPriorityChanged(TaskPriority.HIGH) },
            label = {
                Text("🔴 High", maxLines = 1)
            }
        )

    }

}