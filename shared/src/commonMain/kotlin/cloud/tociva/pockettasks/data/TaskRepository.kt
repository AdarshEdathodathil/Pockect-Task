package cloud.tociva.pockettasks.data

import cloud.tociva.pockettasks.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TaskRepository {
    val tasks: Flow<List<Task>>
    suspend fun add(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
}

class InMemoryTaskRepository : TaskRepository {
    private val state = MutableStateFlow(emptyList<Task>())
    override val tasks: Flow<List<Task>> = state
    override suspend fun add(task: Task) { state.value += task }
    override suspend fun update(task: Task) {
        state.value = state.value.map { if (it.id == task.id) task else it }
    }
    override suspend fun delete(taskId: String) {
        state.value = state.value.filterNot { it.id == taskId }
    }
}
