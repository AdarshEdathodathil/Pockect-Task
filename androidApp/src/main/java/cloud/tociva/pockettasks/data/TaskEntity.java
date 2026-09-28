package cloud.tociva.pockettasks.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tasks")
public class TaskEntity {
    @NonNull @PrimaryKey public final String id;
    public final String title;
    public final String description;
    public final boolean completed;
    public final String priority;
    public final String dueDate;
    public final long createdAt;

    public TaskEntity(String id, String title, String description, boolean completed,
                      String priority, String dueDate, long createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }
}
