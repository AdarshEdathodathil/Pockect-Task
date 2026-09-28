package cloud.tociva.pockettasks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cloud.tociva.pockettasks.model.Task
import cloud.tociva.pockettasks.model.TaskPriority

@Composable
fun TaskItem(
    task: Task,
    onToggle: (String) -> Unit,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onEdit(task) },
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .heightIn(min = 116.dp)
                    .width(5.dp)
                    .background(task.priority.accent())
            )
            Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)) {
                Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle(task.id) }
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                if (task.description.isNotBlank()) {
                    Spacer(Modifier.size(4.dp))
                    Text(
                        task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
                Spacer(Modifier.size(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AssistChip(onClick = {}, label = { Text(task.priority.label()) })
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { onDelete(task) }, modifier = Modifier.size(40.dp)) {
                        Text("⋮", style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
                }
            }
        }
    }
}

private fun TaskPriority.label(): String = when (this) {
    TaskPriority.HIGH -> "High priority"
    TaskPriority.NORMAL -> "Normal"
    TaskPriority.LOW -> "Low priority"
}

private fun TaskPriority.accent(): Color = when (this) {
    TaskPriority.HIGH -> Color(0xFFE56B6F)
    TaskPriority.NORMAL -> Color(0xFF4F46E5)
    TaskPriority.LOW -> Color(0xFF0F9D78)
}
