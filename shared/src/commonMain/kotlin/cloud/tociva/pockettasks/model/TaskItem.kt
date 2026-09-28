package cloud.tociva.pockettasks.model

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val dueDate: String? = null,
    val createdAt: Long
)

enum class TaskPriority {
    LOW,
    NORMAL,
    HIGH
}