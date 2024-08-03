package com.example.task_2.data

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskRepository(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val noteCategoryDao: NoteCategoryDao
) {
    val allTasks: LiveData<List<Task>> = taskDao.getTasks()
    val allCategories: LiveData<List<Category>> = categoryDao.getCategories()



    fun removeCategory(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDao.deleteCategory(id)
        }
    }

    fun insertNewTask(task: Task, categories: List<Category>) {
        CoroutineScope(Dispatchers.IO).launch {
            val noteId = taskDao.insertNewTask(task)
            categories.forEach { category ->
                noteCategoryDao.insertNoteCategoryCrossRef(NoteCategoryCrossRef(noteId.toInt(), category.idCat))
            }
        }
    }
    fun insertNewCategory(category: Category) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDao.insertCategory(category)
        }
    }


    fun removeTask(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.deleteTask(id)
        }
    }

    fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.updateTask(task.noteId, task.name, task.content)
        }
    }

    fun assignCategoryToTask(noteId: Int, categoryId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            noteCategoryDao.insertNoteCategoryCrossRef(NoteCategoryCrossRef(noteId, categoryId))
        }
    }

    fun getNoteWithCategories(noteId: Int): LiveData<List<Category>> {
        return noteCategoryDao.getNoteWithCategories(noteId)
    }

    fun getCategoryWithNotes(categoryId: Int): LiveData<List<Task>> {
        Log.d("Rep", noteCategoryDao.getCategoryWithNotes(categoryId).value.toString())

        return noteCategoryDao.getCategoryWithNotes(categoryId)
    }

    fun updateCategoriesForNote(noteId: Int, newCategories: List<Category>) {
        CoroutineScope(Dispatchers.IO).launch {
            noteCategoryDao.deleteAllCategoriesForNote(noteId)
            val newCrossRefs = newCategories.map { category ->
                NoteCategoryCrossRef(noteId, category.idCat)
            }
            noteCategoryDao.insertNoteCategoryCrossRefs(newCrossRefs)
        }
    }
}
