package cloud.tociva.pockettasks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cloud.tociva.pockettasks.model.Task
import cloud.tociva.pockettasks.model.TaskPriority
import cloud.tociva.pockettasks.ui.components.EmptyState
import cloud.tociva.pockettasks.ui.components.SearchBar
import cloud.tociva.pockettasks.ui.components.TaskDialog
import cloud.tociva.pockettasks.ui.components.TaskItem
import cloud.tociva.pockettasks.viewmodel.TaskFilter
import cloud.tociva.pockettasks.viewmodel.TaskListUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: TaskListUiState,
    onToggleTask: (String) -> Unit,
    onAddTask: (String, String, TaskPriority) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit,
    onRestoreTask: (Task) -> Unit,
    onSearchChanged: (String) -> Unit,
    onFilterChanged: (TaskFilter) -> Unit
) {
    var showTaskDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val total = uiState.tasks.size
    val completed = uiState.tasks.count { it.completed }
    val pending = total - completed
    val highPriority = uiState.tasks.count { !it.completed && it.priority == TaskPriority.HIGH }
    val progress = if (total == 0) 0f else completed.toFloat() / total

    val filteredTasks = uiState.tasks.filter { task ->
        val matchesFilter = when (uiState.selectedFilter) {
            TaskFilter.ALL -> true
            TaskFilter.PENDING -> !task.completed
            TaskFilter.COMPLETED -> task.completed
        }
        val query = uiState.searchText.trim()
        matchesFilter && (query.isBlank() || task.title.contains(query, true) || task.description.contains(query, true))
    }
    val activeTasks = filteredTasks.filterNot { it.completed }
    val completedTasks = filteredTasks.filter { it.completed }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editingTask = null; showTaskDialog = true },
                text = { Text("New task", fontWeight = FontWeight.SemiBold) },
                icon = { Text("+", style = MaterialTheme.typography.titleLarge) },
                shape = RoundedCornerShape(18.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HeroHeader(
                    total = total,
                    pending = pending,
                    progress = progress
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricTile(
                        value = pending.toString(),
                        label = "To do",
                        accent = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricTile(
                        value = completed.toString(),
                        label = "Completed",
                        accent = Color(0xFF0F9D78),
                        modifier = Modifier.weight(1f)
                    )
                    MetricTile(
                        value = highPriority.toString(),
                        label = "Urgent",
                        accent = Color(0xFFE56B6F),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text("Your tasks", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    if (uiState.searchText.isBlank()) "Keep your momentum going" else "Results for \"${uiState.searchText}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                SearchBar(text = uiState.searchText, onTextChanged = onSearchChanged)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = uiState.selectedFilter == filter,
                            onClick = { onFilterChanged(filter) },
                            label = {
                                Text("${filter.label()} ${filter.count(total, pending, completed)}")
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            if (filteredTasks.isEmpty()) {
                item { EmptyState() }
            } else {
                if (activeTasks.isNotEmpty()) {
                    item { SectionLabel("In progress", activeTasks.size) }
                    itemsIndexed(activeTasks, key = { _, task -> task.id }) { index, task ->
                        TaskItem(
                            task = task,
                            onToggle = onToggleTask,
                            onDelete = { taskToDelete = it; showDeleteDialog = true },
                            onEdit = { editingTask = it; showTaskDialog = true }
                        )
                    }
                }
                if (completedTasks.isNotEmpty()) {
                    item { SectionLabel("Completed", completedTasks.size) }
                    items(completedTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggle = onToggleTask,
                            onDelete = { taskToDelete = it; showDeleteDialog = true },
                            onEdit = { editingTask = it; showTaskDialog = true }
                        )
                    }
                }
            }
        }

        if (showTaskDialog) {
            TaskDialog(
                task = editingTask,
                onDismiss = { showTaskDialog = false; editingTask = null },
                onSave = { task ->
                    if (editingTask == null) onAddTask(task.title, task.description, task.priority)
                    else onUpdateTask(task)
                    showTaskDialog = false
                    editingTask = null
                }
            )
        }

        if (showDeleteDialog && taskToDelete != null) {
            val deletingTask = taskToDelete!!
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false; taskToDelete = null },
                title = { Text("Delete task?") },
                text = { Text("${deletingTask.title} will be removed from your list.") },
                confirmButton = {
                    TextButton(onClick = {
                        onDeleteTask(deletingTask.id)
                        showDeleteDialog = false
                        taskToDelete = null
                        scope.launch {
                            val result = snackbarHostState.showSnackbar("Task deleted", "Undo")
                            if (result == SnackbarResult.ActionPerformed) onRestoreTask(deletingTask)
                        }
                    }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false; taskToDelete = null }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
private fun HeroHeader(total: Int, pending: Int, progress: Float) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(
            Brush.linearGradient(listOf(colors.primary, Color(0xFF7C3AED)))
        ).padding(24.dp)
    ) {
        Column {
            Text("POCKET TASKS", style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.75f), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Focus on what matters.", style = MaterialTheme.typography.headlineMedium,
                color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                if (total == 0) "Your next win is waiting." else "$pending tasks waiting for your attention",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(10.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.25f)
            )
        }
    }
}

@Composable
private fun MetricTile(value: String, label: String, accent: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = accent, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SectionLabel(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.size(8.dp))
        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
            Text(count.toString(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun TaskFilter.label() = when (this) {
    TaskFilter.ALL -> "All"
    TaskFilter.PENDING -> "To do"
    TaskFilter.COMPLETED -> "Done"
}

private fun TaskFilter.count(total: Int, pending: Int, completed: Int) = when (this) {
    TaskFilter.ALL -> total
    TaskFilter.PENDING -> pending
    TaskFilter.COMPLETED -> completed
}
