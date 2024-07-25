package com.example.task_2.ui.screens

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_2.data.AppDatabase
import com.example.task_2.data.Task
import com.example.task_2.data.TaskRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): ViewModel() {

    val tasksList: LiveData<List<Task>>
    private val repository: TaskRepository


    init {
        val  tasksDb = AppDatabase.getInstance(application)
        val taskDao = tasksDb.getStaticDao()
        repository = TaskRepository(taskDao)
        tasksList = repository.allTasks
    }

    fun insertTask(name: String, content: String, category: String){
        viewModelScope.launch {
            val newTask = Task(name = name, content = content, category = category)
            repository.insertNewTask(newTask)
        }
    }
    fun updateTask(id: Int?, name: String, content: String, category: String){
        viewModelScope.launch {
            val newTask = Task(id = id, name = name, content = content, category = category)
            repository.updateTask(newTask)
        }
    }

    fun deleteTask(id: Int){
        viewModelScope.launch {
            repository.removeTask(id)
        }
    }
}