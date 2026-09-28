package cloud.tociva.pockettasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.tociva.pockettasks.data.TaskRepository
import cloud.tociva.pockettasks.model.Task
import cloud.tociva.pockettasks.model.TaskPriority
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.tasks.collect { tasks ->
                _uiState.value = _uiState.value.copy(tasks = tasks)
            }
        }
    }

    fun addTask(title: String, description: String, priority: TaskPriority) {
        viewModelScope.launch {
            repository.add(Task(
                id = Random.nextLong().toString(),
                title = title,
                description = description,
                priority = priority,
                createdAt = System.currentTimeMillis()
            ))
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch { repository.update(updatedTask) }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch { repository.delete(taskId) }
    }

    fun restoreTask(task: Task) {
        viewModelScope.launch { repository.add(task) }
    }

    fun toggleTask(taskId: String) {
        val task = _uiState.value.tasks.firstOrNull { it.id == taskId } ?: return
        updateTask(task.copy(completed = !task.completed))
    }

    fun updateSearch(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
    }

    fun updateFilter(filter: TaskFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }
}
