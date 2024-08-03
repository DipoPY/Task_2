package com.example.task_2.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.app.backup.SharedPreferencesBackupHelper
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.hardware.camera2.params.BlackLevelPattern
import android.icu.util.Output
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import com.example.task_2.R
import com.example.task_2.data.Category
import com.example.task_2.data.Task
import com.example.task_2.ui.theme.Green40
import com.example.task_2.ui.theme.Pink80
import com.example.task_2.ui.theme.Purple80
import com.example.task_2.ui.theme.PurpleGrey80
import com.example.task_2.ui.theme.Task_2Theme
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.PaletteContentScale
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.function.Predicate.not
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class UserViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val owner = LocalViewModelStoreOwner.current

            owner?.let {
                val viewModel: MainViewModel = viewModel(
                    it,
                    "UserViewModel",
                    UserViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                val theme by viewModel.isDarkTheme.observeAsState()
                theme?.let { it1 ->
                    Task_2Theme(darkTheme = it1) {
                        NavScreen(viewModel)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController = navController, viewModel = viewModel) }
        composable(
            "addNote?name={name}&category={category}&content={content}&id={id}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("content") { type = NavType.StringType; defaultValue = "" },
                navArgument("id") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                AddNote(
                    navController = navController,
                    viewModel = viewModel,
                    name = name,
                    content = content,
                    id = id
                )
            }
        }
        composable("setting") { SettingAll(navController, viewModel) }
        composable("categorys") { TagsScreen(viewModel = viewModel, navController = navController) }
    }
}


fun NavController.navigateToAddNote(
    name: String = "",
    content: String = "",
    id: Int? = null
) {
    navigate(
        "addNote?name=${Uri.encode(name)}&content=${
            Uri.encode(
                content
            )
        }&id=${id?.toString()}"
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(), navController: NavController) {

    var stateSelect by remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateListOf<Int>()
    }
    var tasks = mutableListOf<Task>()
    if (viewModel.categoryForMainScreen.observeAsState(listOf()).value.isEmpty()) {
        tasks.clear()
        tasks.addAll(
        viewModel.tasksList.observeAsState(listOf()).value.toMutableList())
        tasks.distinct()
    }
    else {
        tasks.clear()
        viewModel.categoryForMainScreen.observeAsState(listOf()).value.forEach {
            viewModel.getCategoryWithNotes(it)
                .observeAsState().value?.let { it1 -> tasks.addAll(it1) }
        }

    }
    val stateField = remember {
        mutableStateOf("")
    }
    tasks = tasks.toSet().toMutableList()
    Log.d("ui", tasks.toString())


    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = stateField.value,
                        onValueChange = { newText ->
                            stateField.value = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp)),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(text = "Поиск", fontSize = 22.sp)
                        },
                        singleLine = true,
                    )

                },
                actions = {
                    Row {


                        IconButton(onClick = {
                            stateSelect = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Category",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate("setting")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Category",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.Transparent) {
                IconButton(
                    onClick = {
                        navController.navigate("addNote")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSecondary

                    )
                }
            }
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            itemsIndexed(tasks.filter { stateField.value in it.content || stateField.value in it.name }) { _, task ->
                DraggableTask(task = task, navController = navController, viewModel)


            }

        }
    }
    if (stateSelect) SelectCategory(onClose = { stateSelect = false }, onSelect = { select ->
        viewModel.addCategoryForMainScreen(select)
    },
        viewModel
    )
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun DraggableTask(
    task: Task,
    navController: NavController,
    vm: MainViewModel
) {
    var xOffset by remember { mutableStateOf(0f) }
    val offsetXAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val threshold = remember {
        0.8f * with(density) { configuration.screenWidthDp.dp.toPx() }
    }
    var flagSelect by remember { mutableStateOf(false) }
    val colorCategories = remember { mutableStateListOf<Color>() }


    val noteCategories by vm.getNoteWithCategories(task.noteId).observeAsState(emptyList())

    LaunchedEffect(noteCategories) {
        colorCategories.clear()
        colorCategories.addAll(noteCategories.map { Color(it.color) })
    }

    Log.d("muu", colorCategories.toList().toString())

    val brush = if (colorCategories.count() > 1) {
        Brush.linearGradient(colors = colorCategories)
    } else if (colorCategories.count() == 1) {
        Brush.linearGradient(colors = colorCategories.plus(colorCategories))
    } else Brush.linearGradient(colors = listOf(Color.Black, Color.Black))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(xOffset.roundToInt(), 0) }
            .padding(5.dp)
            .border(
                width = 6.dp,
                brush = brush,
                RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                navController.navigateToAddNote(
                    task.name, task.content, task.noteId
                )
            }
            .background(MaterialTheme.colorScheme.secondary)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    xOffset += delta
                },
                onDragStopped = {
                    coroutineScope.launch {
                        if (abs(xOffset) > threshold) {
                            flagSelect = true
                        } else {
                            offsetXAnim.animateTo(0f, animationSpec = tween(300))
                            xOffset = offsetXAnim.value
                        }
                    }
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            if (task.name.isNotEmpty())
            Text(
                text = task.name,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.W800,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1
            )

        }
        if (task.content.isNotEmpty())
        Text(
            text = task.content,
            modifier = Modifier.padding(start = 15.dp, bottom = 8.dp, end = 15.dp, top = if(task.name.isEmpty()) 15.dp else 0.dp),
            maxLines = 3,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 20.sp
        )
    }
    if (flagSelect) {
        SelectForDelete(onDismiss = { flagSelect = false }, onConfirm = {
            task.noteId.let { vm.deleteTask(it) }
            flagSelect = false


        },
            forTags = false

        )
        xOffset = 0f

    }
}

@Composable
fun SelectCategory(onClose: () -> Unit, onSelect: (select: List<Int>) -> Unit, vm: MainViewModel) {
    val catgorys = vm.categoriesList.observeAsState().value?.toList()
    val selectedCategories = remember {
        mutableStateListOf<Int>()
    }
    val selectAll = remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier

                .border(3.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 5.dp)
            ) {
                Text(
                    "Выберите категорию",
                    fontSize = 34.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Column(Modifier.selectableGroup()) {
                    catgorys?.forEach {
                        Row {

                            Checkbox(
                                checked = selectedCategories.contains(it.idCat),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (!selectedCategories.contains(it.idCat)) {
                                            selectedCategories.add(it.idCat)
                                        }
                                    } else {
                                        selectedCategories.remove(it.idCat)
                                    }
                                })

                            Text(
                                it.category, fontSize = 28.sp, modifier = Modifier.padding(4.dp),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                    Row {

                        Checkbox(checked = selectAll.value, onCheckedChange = { isChecked ->
                            if (isChecked) {
                                if (catgorys != null) {
                                    selectedCategories.addAll(catgorys.map { it.idCat })
                                    selectAll.value = true
                                }

                            } else {
                                if (catgorys != null) {
                                    selectedCategories.removeAll(catgorys.map { it.idCat })
                                    selectAll.value = false
                                }
                            }
                        })

                        Text(
                            "Все", fontSize = 28.sp, modifier = Modifier.padding(4.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 75.dp)
                                .background(Color.Green)
                                .clickable {

                                    onSelect(
                                        selectedCategories
                                            .map { it }
                                            .toSet()
                                            .toList()
                                    )
                                    onClose()
                                }
                        ) {
                            Text(
                                text = "Ок",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 75.dp)
                                .background(Color.Red)
                                .clickable { onClose() }
                        ) {
                            Text(
                                text = "Назад",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun SelectForDelete(onDismiss: () -> Unit, onConfirm: () -> Unit, forTags: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 400.dp, height = 130.dp)
                .border(3.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(forTags) "Удалить категорию?"
                    else "Удалить заметку?",
                    fontSize = 34.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 75.dp)
                            .clickable { onConfirm() }
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Green)
                    ) {
                        Text(
                            text = "Да",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.W500,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 75.dp)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Red)
                            .clickable { onDismiss() }
                    ) {
                        Text(
                            text = "Нет",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.W500,

                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    navController: NavController,
    viewModel: MainViewModel,
    name: String = "",
    content: String = "",
    id: Int
) {
    val isNewNote = id == -1
    val nameNote = remember { mutableStateOf(name) }
    val contentNote = remember { mutableStateOf(content) }
    var expanded by remember { mutableStateOf(false) }
    val categories by viewModel.categoriesList.observeAsState(emptyList())
    val selectedCategories = remember { mutableStateListOf<Category>() }


    val noteCategories by viewModel.getNoteWithCategories(id).observeAsState(emptyList())

    LaunchedEffect(noteCategories) {
        selectedCategories.clear()
        selectedCategories.addAll(noteCategories)
    }
    Log.d("Ui", id.toString())






    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("main") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                actions = {
                    IconButton(onClick = {
                        if (isNewNote) {
                            if (selectedCategories.isNotEmpty()) {
                                viewModel.insertTask(
                                    name = nameNote.value,
                                    content = contentNote.value,
                                    category = selectedCategories
                                )
                            }
                        } else {
                            viewModel.updateTask(
                                id = id,
                                name = nameNote.value,
                                content = contentNote.value,
                            )
                            viewModel.updateCategoriesForNote(id, selectedCategories)
                        }
                        navController.navigate("main")
                    }) {
                        Icon(
                            Icons.Default.Check, "Принять", modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = nameNote.value,
                onValueChange = { new -> nameNote.value = new },
                placeholder = {
                    Text(
                        text = "Заголовок...",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, start = 5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 34.sp)
            )
            TextField(
                value = contentNote.value,
                onValueChange = { new -> contentNote.value = new },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary
                ),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.99f)
                    .padding(start = 5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { expanded = true }
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = "Выберите категории", fontSize = 30.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterStart),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .padding(start = 5.dp, end = 0.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    categories.forEach { category ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(0.dp)
                                .background(MaterialTheme.colorScheme.secondary),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = selectedCategories.contains(category),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (!selectedCategories.contains(category)) {
                                            selectedCategories.add(category)
                                        }
                                    } else {
                                        selectedCategories.remove(category)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                category.category,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)) {


                                Box(
                                    modifier = Modifier

                                        .size(30.dp)
                                        .align(Alignment.End)
                                        .border(
                                            3.dp,
                                            MaterialTheme.colorScheme.onSecondary,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(category.color))
                                )
                            }
                        }


                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Divider(
                        color = MaterialTheme.colorScheme.onSecondary,
                        thickness = 3.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "Настроить категории",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .clickable {
                                navController.navigate("categorys")
                            }
                    )
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingAll(navController: NavController, viewModel: MainViewModel) {
    val stateButton by viewModel.isDarkTheme.observeAsState()
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Настройки",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("main") }) {
                        Icon(
                            imageVector = Icons.Default.Home, contentDescription = "Назад",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(start = 30.dp, end = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Тема",
                    fontWeight = FontWeight.W500,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Button(
                    onClick = {
                        viewModel.switchTheme()
                    },
                    modifier = Modifier.size(width = 120.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (stateButton!!) Color.LightGray
                        else Color.DarkGray,
                        contentColor = if (stateButton!!) Color.DarkGray
                        else Color.LightGray
                    )
                ) {
                    Text(
                        text =
                        if (stateButton!!) "Темная"
                        else "Светлая",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Text(
                text = "Настройка тегов ->", fontSize = 30.sp, fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate("categorys")
                    },
                color = MaterialTheme.colorScheme.onSecondary
            )

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TagsScreen(viewModel: MainViewModel, navController: NavController) {

    
    val categorys by viewModel.categoriesList.observeAsState(listOf())
    val stateSelecteCategory = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Категории",
                        fontSize = 30.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        stateSelecteCategory.value = !stateSelecteCategory.value
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 30.dp)
        ) {
            itemsIndexed(categorys) { _, it ->
                CategoryBox(it = it, viewModel)
            }

        }


    }
    if (stateSelecteCategory.value) AddCategory(
        viewModel = viewModel,
        onClose = { stateSelecteCategory.value = false })
}

@Composable
fun CategoryBox(it: Category, vm: MainViewModel){
    var xOffset by remember { mutableStateOf(0f) }
    val offsetXAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val threshold = remember {
        0.8f * with(density) { configuration.screenWidthDp.dp.toPx() }
    }
    var flagSelect by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(xOffset.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    xOffset += delta
                },
                onDragStopped = {
                    coroutineScope.launch {
                        if (abs(xOffset) > threshold) {
                            flagSelect = true
                        } else {
                            offsetXAnim.animateTo(0f, animationSpec = tween(300))
                            xOffset = offsetXAnim.value
                        }
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "#${it.category}",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(0.4f),
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.4f))
        Box(
            modifier = Modifier
                .padding(end = 10.dp)


                .border(
                    3.dp,
                    MaterialTheme.colorScheme.onSecondary,
                    RoundedCornerShape(5.dp)
                )
                .clip(RoundedCornerShape(5.dp))
                .size(30.dp)
                .background(Color(it.color))
        )
    }
    if (flagSelect) {
        SelectForDelete(onDismiss = { flagSelect = false }, onConfirm = {
            vm.deleteCategory(it.idCat)
            flagSelect = false


        },
            forTags = true

        )
        xOffset = 0f

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddCategory(viewModel: MainViewModel, onClose: () -> Unit) {
    val stateNameCategory = remember {
        mutableStateOf("")
    }
    val color = remember {
        mutableStateOf(Color.Black)
    }
    val controller = rememberColorPickerController()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier

                .border(5.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .size(400.dp, 410.dp)
                .background(MaterialTheme.colorScheme.secondary),
        ) {
            Column(modifier = Modifier) {
                TextField(
                    value = stateNameCategory.value,
                    onValueChange = { new -> stateNameCategory.value = new },
                    singleLine = true,
                    placeholder = {
                        Text(text = "Введите категорию")
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.secondary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Выберите цвет категории:",
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(5.dp, Color.Black, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    ImageColorPicker(modifier = Modifier.fillMaxSize(),
                        controller = controller,
                        paletteImageBitmap = ImageBitmap.imageResource(id = R.drawable.rgb),
                        paletteContentScale = PaletteContentScale.CROP,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            color.value = colorEnvelope.color
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AlphaTile(
                    modifier = Modifier
                        .size(height = 40.dp, width = 80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.CenterHorizontally),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Red)
                            .clickable {
                                onClose()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Отмена", fontWeight = FontWeight.W800, fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(100.dp, 40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Green40)
                            .clickable {
                                viewModel.insertCategory(
                                    stateNameCategory.value,
                                    color = color.value
                                )
                                onClose()

                            },
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "Принять", fontWeight = FontWeight.W800, fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }


            }
        }
    }
}
