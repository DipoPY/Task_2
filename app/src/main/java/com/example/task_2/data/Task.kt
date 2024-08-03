package com.example.task_2.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val noteId: Int,
    val name: String,
    val content: String,
)

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val idCat: Int,
    val category: String,
    val color: Int = Color.Black.toArgb()
)


@Entity(tableName = "note_category_cross_ref",
    primaryKeys = ["noteId", "idCat"])
data class NoteCategoryCrossRef(
    val noteId: Int,
    val idCat: Int
)

data class NoteWithCategories(
    @Embedded val note: Task,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "idCat",
        associateBy = Junction(NoteCategoryCrossRef::class)
    )
    val categories: List<Category>
)
data class CategoryWithNotes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "idCat",
        entityColumn = "noteId",
        associateBy = Junction(NoteCategoryCrossRef::class)
    )
    val notes: List<Task>
)

