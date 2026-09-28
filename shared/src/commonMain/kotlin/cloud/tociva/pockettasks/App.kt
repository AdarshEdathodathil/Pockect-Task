package cloud.tociva.pockettasks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cloud.tociva.pockettasks.data.InMemoryTaskRepository
import cloud.tociva.pockettasks.data.TaskRepository
import cloud.tociva.pockettasks.ui.screens.HomeScreen
import cloud.tociva.pockettasks.ui.theme.PocketTasksTheme
import cloud.tociva.pockettasks.viewmodel.TaskViewModel

@Composable
fun App(repository: TaskRepository = remember { InMemoryTaskRepository() }) {

    PocketTasksTheme {

        val viewModel = remember(repository) { TaskViewModel(repository) }

        val uiState by viewModel.uiState.collectAsState()

        HomeScreen(
            uiState = uiState,
            onToggleTask = viewModel::toggleTask,
            onAddTask = viewModel::addTask,
            onUpdateTask = viewModel::updateTask,
            onDeleteTask = viewModel::deleteTask,
            onRestoreTask = viewModel::restoreTask,
            onSearchChanged = viewModel::updateSearch,
            onFilterChanged = viewModel::updateFilter
        )

    }
}
