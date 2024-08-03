package com.example.task_2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewTask(task: Task): Long

    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task WHERE noteId = :id")
    fun deleteTask(id: Int)

    @Query("UPDATE task SET name = :name, content = :content WHERE noteId = :id")
    fun updateTask(id: Int, name: String, content: String)
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>

    @Query("DELETE FROM category WHERE idCat = :id")
    fun deleteCategory(id: Int)
}

@Dao
interface NoteCategoryDao {

    @Transaction
    @Query("""
        SELECT *
        FROM category
        INNER JOIN note_category_cross_ref ON category.idCat = note_category_cross_ref.idCat
        WHERE note_category_cross_ref.noteId = :noteId
    """)
    fun getNoteWithCategories(noteId: Int): LiveData<List<Category>>

    @Transaction
    @Query("DELETE FROM note_category_cross_ref WHERE noteId = :noteId")
    fun deleteAllCategoriesForNote(noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteCategoryCrossRefs(noteCategoryCrossRefs: List<NoteCategoryCrossRef>)

    @Transaction
    @Query("""
    SELECT *
    FROM task
    INNER JOIN note_category_cross_ref ON task.noteId = note_category_cross_ref.noteId
    WHERE note_category_cross_ref.idCat = :categoryId
""")
    fun getCategoryWithNotes(categoryId: Int): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteCategoryCrossRef(noteCategoryCrossRef: NoteCategoryCrossRef)
}