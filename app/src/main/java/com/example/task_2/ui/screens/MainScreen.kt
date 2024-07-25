package com.example.task_2.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.icu.util.Output
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import com.example.task_2.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class UserViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}

class MainActivity : ComponentActivity() {

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
                NavScreen(viewModel)
            }
        }
    }
}

@Composable
fun NavScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController = navController, viewModel = viewModel) }
        composable(
            "addNote?name={name}&category={category}&content={content}&id={id}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("category") { type = NavType.StringType; defaultValue = "Выбор категории" },
                navArgument("content") { type = NavType.StringType; defaultValue = "" },
                navArgument("id") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: "Выбор категории"
            val content = backStackEntry.arguments?.getString("content") ?: ""
            val id = backStackEntry.arguments?.getInt("id")
            AddNote(navController = navController, viewModel = viewModel, name = name, category = category, content = content, id = id)
        }
    }
}

fun NavController.navigateToAddNote(
    name: String = "",
    category: String = "Выбор категории",
    content: String = "",
    id: Int? = null
) {
    navigate("addNote?name=${Uri.encode(name)}&category=${Uri.encode(category)}&content=${Uri.encode(content)}&id=${id?.toString()}")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(), navController: NavController) {
    val tasks by viewModel.tasksList.observeAsState(listOf())
    var stateSelect by remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Список заметок", fontSize = 40.sp) },
                actions = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate("addNote")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        IconButton(onClick = {
                            stateSelect = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Category",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            
            itemsIndexed(tasks.filter {  it.category == category.value}) { _, task ->
                        DraggableTask(task = task, navController = navController, viewModel)


            }
        }
    }
    if(stateSelect) SelectCategory(onClose = {stateSelect = false}, onSelect = {select -> category.value = select
    Log.d("myy", category.value)})
}

@SuppressLint("CoroutineCreationDuringComposition")
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(xOffset.roundToInt(), 0) }
            .padding(5.dp)
            .border(6.dp, Color.Black, RoundedCornerShape(10.dp))
            .clickable {
                navController.navigateToAddNote(
                    task.name, task.category, task.content, task.id
                )
            }
            .background(Color.LightGray)
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
        Text(
            text = task.name,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 26.sp
        )
        Text(
            text = task.content,
            modifier = Modifier.padding(start = 8.dp, bottom = 5.dp),
            maxLines = 1
        )
    }
    if (flagSelect) {
        SelectForDelete(onDismiss = { flagSelect = false }, onConfirm = {
            task.id?.let { vm.deleteTask(it) }
            flagSelect = false


        }

        )
        xOffset = 0f

    }
}

@Composable
fun SelectCategory(onClose: () -> Unit, onSelect: (select: String) -> Unit){
    val language = listOf("Все", "Спорт", "Хобби", "Работа", "Учеба")

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(language[0]) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .border(3.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Column(modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(start = 5.dp)) {
                Text(
                    "Выберите категорию",
                    fontSize = 34.sp
                )
                Column(Modifier.selectableGroup()) {
                    language.forEach{
                    Row {
                        RadioButton(
                            selected = it == selectedOption,
                            onClick = { onOptionSelected(it) }
                        )
                        Text(it, fontSize = 28.sp, modifier = Modifier.padding(4.dp))
                    }
                }
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
                                if(selectedOption == "Все") onSelect("")
                                else onSelect(selectedOption)
                                onClose()}
                    ) {
                        Text(text = "Ок", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp, fontWeight = FontWeight.W500)
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 75.dp)
                            .background(Color.Red)
                            .clickable { onClose()}
                    ) {
                        Text(text = "Назад", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp, fontWeight = FontWeight.W500)
                    }
                }

            }
        }
    }
}


@Composable
fun SelectForDelete(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 400.dp, height = 130.dp)
                .background(Color.LightGray)
                .border(3.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Удалить заметку?",
                    fontSize = 34.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 75.dp)
                            .background(Color.Green)
                            .clickable { onConfirm() }
                    ) {
                        Text(text = "Да", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp, fontWeight = FontWeight.W500)
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 75.dp)
                            .background(Color.Red)
                            .clickable { onDismiss() }
                    ) {
                        Text(text = "Нет", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp, fontWeight = FontWeight.W500)
                    }
                }
            }
        }
    }
}

@Composable
fun calculateThreshold(): Float {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    return with(density) {
        0.8f * configuration.screenWidthDp.dp.toPx()
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    navController: NavController,
    viewModel: MainViewModel,
    name: String = "",
    category: String = "Выбор категории",
    content: String = "",
    id: Int? = -1
) {
    val isNewNote = id == -1
    val nameNote = remember { mutableStateOf(name) }
    val contentNote = remember { mutableStateOf(content) }
    var expanded by remember { mutableStateOf(false) }
    val categoryNote = remember { mutableStateOf(category) }
    val categories = listOf("Работа", "Учеба", "Хобби", "Спорт")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("main")
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent), actions = {
                IconButton(onClick = {
                    if (isNewNote) {
                        viewModel.insertTask(
                            name = nameNote.value,
                            content = contentNote.value,
                            category = if (categoryNote.value !in categories) "" else categoryNote.value
                        )
                    } else {
                        viewModel.updateTask(
                            id = id,
                            name = nameNote.value,
                            content = contentNote.value,
                            category = if (categoryNote.value !in categories) "" else categoryNote.value
                        )
                    }
                    navController.navigate("main")
                }) {
                    Icon(
                        Icons.Default.Check, "Принять", modifier = Modifier.size(40.dp)
                    )
                }
            })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = nameNote.value,
                onValueChange = { new ->
                    nameNote.value = new
                },
                placeholder = {
                    Text(text = "Заголовок...", fontSize = 30.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, start = 5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 34.sp)
            )
            TextField(
                value = contentNote.value,
                onValueChange = { new ->
                    contentNote.value = new
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.99f)
                    .padding(start = 5.dp)
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .clickable { expanded = true }
            ) {
                Text(
                    text = categoryNote.value, fontSize = 30.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterStart)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .padding(start = 5.dp, end = 0.dp)
                ) {
                    categories.forEach {
                        Text(
                            it, fontSize = 20.sp,
                            modifier = Modifier.clickable {
                                categoryNote.value = it
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
