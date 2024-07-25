package com.example.task_2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StaticDao {
    @Insert(entity = Task::class)
    fun insertNewTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteTask(id: Int)

    @Query("UPDATE task SET name = :name, category = :category, content = :content WHERE id = :id")
    fun updateTask(id: Int, name: String, category: String, content: String)
}