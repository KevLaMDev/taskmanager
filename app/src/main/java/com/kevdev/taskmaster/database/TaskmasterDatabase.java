package com.kevdev.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kevdev.taskmaster.Model.Task;
import com.kevdev.taskmaster.dao.TaskDAO;

@Database(entities = {Task.class}, version = 2)
public abstract class TaskmasterDatabase extends RoomDatabase {

    public abstract TaskDAO taskDAO();

}
