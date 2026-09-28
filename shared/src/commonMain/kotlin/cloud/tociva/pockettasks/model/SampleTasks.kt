package cloud.tociva.pockettasks.model

val sampleTasks = listOf(
    Task(
        id = "1",
        title = "Learn Kotlin Multiplatform",
        description = "Study Compose Multiplatform and project structure",
        priority = TaskPriority.HIGH,
        createdAt = 1L
    ),
    Task(
        id = "2",
        title = "Build Pocket Tasks",
        description = "Build a pocket task mobile application in KMP",
        completed = true,
        priority = TaskPriority.NORMAL,
        createdAt = 2L
    ),
    Task(
        id = "3",
        title = "Learn Room Database",
        description = "Learn room database",
        priority = TaskPriority.LOW,
        createdAt = 3L
    )
)