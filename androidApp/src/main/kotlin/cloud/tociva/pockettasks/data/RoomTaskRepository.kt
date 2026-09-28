package cloud.tociva.pockettasks.data

import android.content.Context
import androidx.room.Room
import cloud.tociva.pockettasks.model.Task
import cloud.tociva.pockettasks.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomTaskRepository(context: Context) : TaskRepository {
    private val dao = Room.databaseBuilder(
        context.applicationContext, TaskDatabase::class.java, "pocket_tasks.db"
    ).build().taskDao()

    override val tasks: Flow<List<Task>> = dao.observeAll().map { entities ->
        entities.map { entity ->
            Task(entity.id, entity.title, entity.description, entity.completed,
                TaskPriority.valueOf(entity.priority), entity.dueDate, entity.createdAt)
        }
    }

    override suspend fun add(task: Task) = withContext(Dispatchers.IO) { dao.insert(task.toEntity()) }
    override suspend fun update(task: Task) = withContext(Dispatchers.IO) { dao.update(task.toEntity()) }
    override suspend fun delete(taskId: String) = withContext(Dispatchers.IO) { dao.delete(taskId) }
}

private fun Task.toEntity() = TaskEntity(id, title, description, completed, priority.name, dueDate, createdAt)
