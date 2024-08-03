package com.example.task_2.ui.screens

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_2.data.AppDatabase
import com.example.task_2.data.Category
import com.example.task_2.data.CategoryWithNotes
import com.example.task_2.data.NoteWithCategories
import com.example.task_2.data.Task
import com.example.task_2.data.TaskRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var tasksList: LiveData<List<Task>>
    val categoriesList: LiveData<List<Category>>
    private val repository: TaskRepository

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    private val _categoryForMainScreen = MutableLiveData<List<Int>>(listOf())
    val categoryForMainScreen: LiveData<List<Int>> = _categoryForMainScreen




    init {
        val tasksDb = AppDatabase.getInstance(application)
        val taskDao = tasksDb.getTaskDao()
        val categoryDao = tasksDb.getCategoryDao()
        val noteCategoryDao = tasksDb.getNoteCategoryDao()
        repository = TaskRepository(taskDao, categoryDao, noteCategoryDao)
        tasksList = repository.allTasks
        categoriesList = repository.allCategories
    }

    fun addCategoryForMainScreen(categorysId: List<Int>){
        _categoryForMainScreen.value = categorysId

    }

    fun switchTheme() {
        _isDarkTheme.value = _isDarkTheme.value != true
    }

    fun insertTask(name: String, content: String, category: List<Category>) {
        viewModelScope.launch {
            val newTask = Task(noteId = 0, name = name, content = content)
            repository.insertNewTask(newTask, category)
            category.forEach {
                addCategoryToNote(newTask.noteId, it.idCat)
            }
        }
    }

    fun insertCategory(category: String, color: Color) {
        viewModelScope.launch {
            val newCategory = Category(idCat = 0, category = category, color = color.toArgb())
            repository.insertNewCategory(newCategory)
        }
    }

    fun updateTask(id: Int, name: String, content: String) {
        viewModelScope.launch {
            val updatedTask = Task(noteId = id, name = name, content = content)
            repository.updateTask(updatedTask)
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.removeTask(id)
        }
    }
    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            repository.removeCategory(id)
        }
    }

    fun updateCategoriesForNote(noteId: Int, newCategories: List<Category>) {
        viewModelScope.launch {
            repository.updateCategoriesForNote(noteId, newCategories)
        }
    }

    fun assignCategoryToTask(noteId: Int, categoryId: Int) {
        viewModelScope.launch {
            repository.assignCategoryToTask(noteId, categoryId)
        }
    }

    fun getNoteWithCategories(noteId: Int): LiveData<List<Category>> {
        return repository.getNoteWithCategories(noteId)
    }

    fun getCategoryWithNotes(categoryId: Int): LiveData<List<Task>> {
        Log.d("ViewModel", repository.getCategoryWithNotes(categoryId).value.toString())
        return repository.getCategoryWithNotes(categoryId)
    }

    private fun addCategoryToNote(noteId: Int, categoryId: Int) {
        viewModelScope.launch {
            repository.assignCategoryToTask(noteId, categoryId)
        }
    }
}