package cloud.tociva.pockettasks.viewmodel

import cloud.tociva.pockettasks.model.Task

data class TaskListUiState(

    val tasks: List<Task> = emptyList(),

    val searchText: String = "",

    val selectedFilter: TaskFilter = TaskFilter.ALL

)