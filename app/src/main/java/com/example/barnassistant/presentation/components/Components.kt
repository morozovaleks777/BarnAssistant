package com.example.barnassistant.presentation.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.barnassistant.R
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

import com.google.android.gms.ads.AdView


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Text(text = "Barn Assistant",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.h3,
        color = Color(0xFF8D5ACC).copy(alpha = 0.7f),

        fontFamily = FontFamily.Cursive)
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)


}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType ,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value  = it},
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction)


}



@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)},
        keyboardActions = onAction)

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        Icons.Default.Close

    }

}


@Composable
fun BarnAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onSearchClicked:() -> Unit = {},
    onBackArrowClicked:() -> Unit = {}
) {

    TopAppBar(title = {




        Row(verticalAlignment = Alignment.CenterVertically){
            if (showProfile) {


                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "search Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f)
                        .clickable {
                            onSearchClicked.invoke()
                        }
                )
               
            }
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "arrow back",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { onBackArrowClicked.invoke() })
            }
            Spacer(modifier = Modifier.width(40.dp) )
            Text(text = title,
                color = Color(0xFF8D5ACC).copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp),
                fontFamily = FontFamily.Cursive
            )


        }


    },




        actions = {

            IconButton(onClick = {
                FirebaseAuth.getInstance()
                    .signOut().run {
                        navController.navigate(AppScreens.LoginScreen.name)
                    }
            }) {
                if (showProfile) Row() {
                    Icon(imageVector = Icons.Filled.Logout ,
                        contentDescription = "Logout" ,
                        // tint = Color.Green.copy(alpha = 0.4f)
                    )
                } else Box {}



            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp)

}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = { onTap()},
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF),
    ) {
//        Icon(imageVector = Icons.Default.Add,
//            contentDescription = "Add a Book",
//            tint = Color.White)
        Text("add new list")

    }

}

@ExperimentalMaterialApi
@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    barnItem:BarnItemDB= BarnItemDB(name = "", itemId = 0, count = 0f, price = 0f, enabled = true, listName = ""),
    listName: String="",
    name: String ="",
    count:String ="",
    price:String ="",
    sum:String="",
    stringSum:String="sum :",
    onNoteClicked: (BarnItemDB) -> Unit
) {




    Surface(
        modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),

//        color =when(barnItem.enabled) {
//            true -> Color(0xFFBB86FC)
//            false -> {Color(0xFF9797BB)
//            }
       // }

        elevation = 6.dp
    ) {
        Log.d("test", "NoteRow:barnItem.enabled ${barnItem.enabled} ")
        Column(modifier
            .clickable {
                onNoteClicked(barnItem)

            }
            .background(
                color = when (barnItem.enabled) {
                    true -> Color(0xFFBB86FC)
                    false -> {
                        Color(0xFF9797BB)
                    }
                }
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start) {
            Text(
                text = listName,
                style = MaterialTheme.typography.subtitle2
            )
            Row(horizontalArrangement = Arrangement.Center,
           ) {
                Column() {
                    Text(text = "name :", style = MaterialTheme.typography.subtitle1)
                    Text(text = name, style = MaterialTheme.typography.subtitle1)
                }
                Spacer(modifier = modifier.padding(25.dp))

                Column() {
                    Text(text = "count :", style = MaterialTheme.typography.subtitle1)
                    Text(text = count, style = MaterialTheme.typography.subtitle1)
                }
                Spacer(modifier = modifier.padding(25.dp))

                Column() {
                    Text(text = "price :", style = MaterialTheme.typography.subtitle1)
                    Text(text = price, style = MaterialTheme.typography.subtitle1)

                }
                Spacer(modifier = modifier.padding(25.dp))

                Column() {
                    Text(text = stringSum, style = MaterialTheme.typography.subtitle1)
                    Text(text = sum, style = MaterialTheme.typography.subtitle1)
                }


            }






        }
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    book: String,
    time: String,
    onLongPressed: (String) -> Unit = {},
    onPressDetails: (String) -> Unit = {},
    onDoubleClick: () -> Unit ={}

    ) {
    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    val isRotated = remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (isRotated.value) 360F else 0F,
        animationSpec = tween(
            durationMillis = 2500, // duration
            easing = FastOutSlowInEasing
        )
    )

    var expanded by remember { mutableStateOf(false) }
    AnimatedContent(

        targetState = expanded,
        transitionSpec = {
            fadeIn(animationSpec = tween(150, 150)) with
                    fadeOut(animationSpec = tween(150)) using
                    SizeTransform { initialSize, targetSize ->
                        if (targetState) {
                            keyframes {
                                // Expand horizontally first.
                                IntSize(targetSize.width, initialSize.height) at 150
                                durationMillis = 300
                            }
                        } else {
                            keyframes {
                                // Shrink vertically first.
                                IntSize(initialSize.width, targetSize.height) at 150
                                durationMillis = 300
                            }
                        }
                    }
        }
    )
    { targetExpanded ->
        if (targetExpanded) {
            Icon(
                imageVector = Icons.Rounded.Done,
                contentDescription = "Fav Icon",
                modifier = Modifier
                    .padding(bottom = 1.dp)
                    .clickable {
                        expanded = false
                    }


            )
        } else {


            val myColor=Color(0xFFBB86FC)

            Card(shape = RoundedCornerShape(size = 29.dp,),
                backgroundColor = myColor.copy(alpha = 0.3f),

                elevation = -1.dp,
                modifier = modifier

                    .padding(16.dp)
                    .height(242.dp)
                    .rotate(angle)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                isRotated.value = true
                                onLongPressed.invoke(book)
                            },
                            onDoubleTap = {
                                expanded = true
                                // onPressDetails.invoke(book)
                                onDoubleClick.invoke()
                            },
                            onTap = { onPressDetails.invoke(book) })
                    }) {

                Column(
                    modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {

//                Image(painter = rememberImagePainter(data = book.photoUrl.toString()),
//                    contentDescription = "book image",
//                    modifier = Modifier
//                        .height(140.dp)
//                        .width(100.dp)
//                        .padding(4.dp))
                        Spacer(modifier = Modifier.width(30.dp))

//                Column(
//                    modifier = Modifier.padding(top = 25.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//
//                ) {
//
//
//                   // BookRating(score = book.rating!!)
//                }

                    }
                    Text(

                        text = book, modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(15.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Blue,
                                        Color.Green
                                    )
                                )
                            )
                        ,

                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )


                    Text(text = time, modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.caption)

//            Icon(
//                imageVector = Icons.Rounded.Done,
//                contentDescription = "Fav Icon",
//                modifier = Modifier
//                    .padding(bottom = 1.dp)
//                    .align(Alignment.End)
//
//
//            )

                    val isStartedReading = remember {
                        mutableStateOf(false)
                    }
val viewModel:BarnItemViewModel= hiltViewModel()
                    val list=viewModel.favList.collectAsState().value.filter { barnItemDB -> barnItemDB.listName==book }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                       val bool=list.any { barnItemDB -> barnItemDB.enabled }
                         isStartedReading.value =when(bool){
                             true ->false
                             false ->true
                         }


                        RoundedButton(label = if (isStartedReading.value)  "Done" else "Not Yet done",
                            radius = 70){
                            isStartedReading.value=!isStartedReading.value
                        }

                    }
                }


            }}
    }
}


@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}) {
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF),


        ) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = Color.Black,
                fontSize = 15.sp),)

        }

    }


}

@Composable
fun TitleSection(modifier: Modifier = Modifier,
                 label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 40.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left,
            color = Color(0xFF8BC34A),
            fontFamily = FontFamily.Cursive,)
        }

    }

}

@ExperimentalMaterialApi
@Composable
fun LazyColumnBarnItemDB(
    listBarnItemDB: List<BarnItemDB>,
    viewModel: BarnItemViewModel,
    name: MutableState<String>,
    count: MutableState<String>,
    price: MutableState<String>,
    itemId: MutableState<Int>,
    listName: MutableState<String>
) {
    LazyColumn {
        items(items = listBarnItemDB) { it ->

            val barn = it
            var unread by remember { mutableStateOf(false) }
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd) {
                        unread = !unread
                    }
                    if (it == DismissValue.DismissedToStart) {
                        viewModel.removeItem(barn)


                    }

                    false

                }
            )
            SwipeToDismiss(dismissState, it, viewModel)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SwipeToDismiss(
    dismissState: DismissState,
    it: BarnItemDB,
    viewModel: BarnItemViewModel
) {
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
        },
        background = {
            val direction =
                dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.Unspecified
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> {
                    Icons.Default.Delete
                }


            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {

            NoteRow(
                barnItem = BarnItemDB(
                    name = it.name,
                    count = it.count,
                    price = it.price,
                    itemId = it.itemId,
                    listName = it.listName,
                    enabled = it.enabled
                ),
                name = it.name,
                count = it.count.toString(),
                price = it.price.toString(),
                sum = viewModel.getItemSum(it),
                stringSum = when (it.count <= 1) {
                    true -> ""
                    else -> "sum :"
                }
            ) {
//                        name.value = it.name
//                        count.value = it.count.toString()
//                        price.value = it.price.toString()
//                        itemId.value = it.itemId
//                        listName.value = it.listName


                viewModel.changeEnableState(it)

            }
        })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ButtonWithLongPress(
    onClick: () -> Unit,
    onDoubleClick:()->Unit = {},
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = { },
        modifier = modifier
            .combinedClickable(
                interactionSource,
                rememberRipple(),
                true,
                null,
                Role.Button,
                null,
                onClick = { onClick() },
                onLongClick = { onLongClick() },
                onDoubleClick = {onDoubleClick()}),
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding)
                        .combinedClickable(interactionSource,
                            null,
                            true,
                            null,
                            Role.Button,
                            null,
                            onClick = { onClick() },
                            onLongClick = { onLongClick() },
                            onDoubleClick = { onDoubleClick() }),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }}












@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavDrawerItem.Home,
        NavDrawerItem.Help,
       NavDrawerItem.Counter,

    )
    Column(
        modifier = Modifier
           // .background(colorResource(id = R.color.purple_200))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB9DCE9),
                        Color(0xFFCD1FEB)

                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        // Header
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = R.drawable.ic_launcher_background.toString(),
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp)
        )
        // Space between
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        // List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })

            /* Add code later */

        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            fontFamily = FontFamily.Cursive,
            text = "Developed by Oleksandr Morozov",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}



sealed class NavDrawerItem(var route: String,val icon:ImageVector, var title: String) {
    object Home : NavDrawerItem(AppScreens.HomeScreen.name,Icons.Filled.Home, "Home")
    object Help : NavDrawerItem(AppScreens.HelpScreen.name, Icons.Filled.Help,"Help")
    object Counter : NavDrawerItem(AppScreens.Counter.name,Icons.Filled.Calculate,  "Counter")

}
@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val background = if (selected) R.color.purple_700 else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            //.background(colorResource(id = background))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        colorResource(id = R.color.purple_200)
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(start = 10.dp)
    ) {
//        Image(
//            painter = painterResource(id =item.icon),
//            contentDescription = item.title,
//            colorFilter = ColorFilter.tint(Color.White),
//            contentScale = ContentScale.Fit,
//            modifier = Modifier
//                .height(35.dp)
//                .width(35.dp)
//        )
        Icon(imageVector = item.icon,
            contentDescription = "search Icon",
            modifier = Modifier
                .scale(2f)
                .padding(start = 20.dp)

            , tint = Color.White)
        Spacer(modifier = Modifier.fillMaxWidth(0.6f))
        Text(

            text = item.title,
            fontSize = 35.sp,
            color = Color.White,
            fontFamily = FontFamily.Cursive,
       fontWeight = FontWeight.Bold
        )
    }
}
@Preview
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    filterName: String="filter",
    onClick: () -> Unit={}
) {

            Card(shape = RoundedCornerShape(size = 29.dp,),
                backgroundColor = Color.Gray.copy(alpha = 0.3f),
border = BorderStroke(width = 1.dp,color= Color.Black.copy(alpha = 0.6f)),
                elevation = -1.dp,
                modifier = modifier

                    .padding(10.dp)
                    .height(34.dp)
                    .widthIn(40.dp)
                   ) {
                Row(//horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {

                    Text(text = filterName,
                        modifier = Modifier
                            .padding(6.dp)
                            .height(18.dp)
                        ,

                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(imageVector = Icons.Default.Close, contentDescription = "closed",
                            tint = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { onClick.invoke() })
                    }

                }
            }


@Preview
@Composable
fun AdvertView(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
               setAdSize(AdSize.BANNER)
                    adUnitId = context.getString(R.string.ad_id_banner_start)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
@Preview
@Composable
fun AdvertView2(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize( AdSize.BANNER)
                    adUnitId = context.getString(R.string.ad_id_banner2)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}