package cloud.tociva.pockettasks.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    Flow<List<TaskEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskEntity task);

    @Update
    void update(TaskEntity task);

    @Query("DELETE FROM tasks WHERE id = :taskId")
    void delete(String taskId);
}
