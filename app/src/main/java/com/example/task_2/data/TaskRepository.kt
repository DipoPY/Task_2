package com.example.task_2.data

import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: StaticDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val allTasks: LiveData<List<Task>> = taskDao.getTasks()

    fun insertNewTask(task: Task){
        coroutineScope.launch(Dispatchers.IO){
            taskDao.insertNewTask(task)
        }
    }

    fun removeTask(id: Int){
        coroutineScope.launch(Dispatchers.IO){
            taskDao.deleteTask(id)
        }
    }

    fun updateTask(task: Task){
        coroutineScope.launch(Dispatchers.IO ){
            task.id?.let { taskDao.updateTask(it, task.name,task.category, task.content,) }
        }
    }


}