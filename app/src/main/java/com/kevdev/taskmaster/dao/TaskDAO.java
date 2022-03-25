package com.kevdev.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kevdev.taskmaster.Model.Task;

import java.util.List;

@Dao
public interface TaskDAO
{
    @Insert
    public void insertATask(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

}
