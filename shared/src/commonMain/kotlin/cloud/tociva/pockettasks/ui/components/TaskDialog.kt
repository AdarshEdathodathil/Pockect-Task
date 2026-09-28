package cloud.tociva.pockettasks.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cloud.tociva.pockettasks.model.Task
import cloud.tociva.pockettasks.model.TaskPriority
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(task: Task? = null, onDismiss: () -> Unit, onSave: (Task) -> Unit) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var priority by remember { mutableStateOf(task?.priority ?: TaskPriority.NORMAL) }
    var showTitleError by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, modifier = Modifier.navigationBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            Text(
                text = if (task == null) "Create a task" else "Edit task",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; showTitleError = false },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Task title") },
                placeholder = { Text("What needs to be done?") },
                singleLine = true,
                isError = showTitleError,
                supportingText = if (showTitleError) ({ Text("Add a title before saving") }) else null
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Notes (optional)") },
                placeholder = { Text("Add helpful details") },
                minLines = 3,
                maxLines = 5
            )
            Spacer(Modifier.height(20.dp))
            Text("Priority", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            PrioritySelector(priority = priority, onPriorityChanged = { priority = it })
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (title.isBlank()) {
                        showTitleError = true
                    } else {
                        onSave(
                            task?.copy(title = title, description = description, priority = priority)
                                ?: Task(
                                    id = Random.nextLong().toString(),
                                    title = title,
                                    description = description,
                                    priority = priority,
                                    createdAt = System.currentTimeMillis()
                                )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (task == null) "Create task" else "Save changes")
            }
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    }
}
