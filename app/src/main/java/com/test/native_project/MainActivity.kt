@file:kotlin.OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.test.native_project

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.os.Bundle
import android.preference.PreferenceActivity.Header
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.internal.service.Common
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.ui.tooling.preview.Preview as comPreView
import com.test.native_project.comm.Common as comm

data class CardItem(val id: Int, val name : String,  val phoneNum : String, val telNum:String,  val email : String, val company: String, val comDept: String, val comRank:String, val comAddr: String, val comAddrDetail:String, val comPost: String, val bizNum: String, val bizLogo: Int)
data class BoardItem(val boardId: Int, val title: String, val content: String, val caregory: String, val regId: String, val regDate: String, val view: Int, val like: Int, val replyCnt: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm : HomeViewModel by viewModels()
        vm.addAllItems(
            listOf(
                CardItem(
                    0,
                    "김홍자",
                    "010-3867-4453",
                    "02-418-7864",
                    "hongja1134@gmail.com",
                    "(주)하나식품",
                    "영업2팀",
                    "주임",
                    "서울특별시 강서구 공항대로 200",
                    "하나빌리지 302호",
                    "07631",
                    "0423842168",
                    R.drawable.img_hana_logo
                ),
                CardItem(
                    1,
                    "박철수",
                    "010-4120-0345",
                    "070-4843-1624",
                    "park.c.s@naver.com",
                    "(주)롯데정보통신",
                    "개발3팀",
                    "파트장",
                    "서울특별시 금천구 가산디지털2로 179",
                    "롯데Gos타워 701호",
                    "07981",
                    "1435451987",
                    R.drawable.img_lotte_logo
                ),
                CardItem(
                    2,
                    "김동재",
                    "010-1111-2222",
                    "02-348-4547",
                    "dongjae.kim@hansol.com",
                    "(주)한솔로지스유",
                    "카고링크 사업팀",
                    "책임",
                    "서울특별시 강서구 마곡중앙4로 22",
                    "파인스퀘어 B동 7층",
                    "07631",
                    "1537462492",
                    R.drawable.img_hansol_logo
                ),
                CardItem(
                    3,
                    "김동재",
                    "010-1111-2222",
                    "02-348-4547",
                    "dongjae.kim@hansol.com",
                    "(주)한솔로지스유",
                    "카고링크 사업팀",
                    "책임",
                    "서울특별시 강서구 마곡중앙4로 22",
                    "파인스퀘어 B동 7층",
                    "07631",
                    "1537462492",
                    R.drawable.img_hansol_logo
                ),
                CardItem(
                    4,
                    "김동재",
                    "010-1111-2222",
                    "02-348-4547",
                    "dongjae.kim@hansol.com",
                    "(주)한솔로지스유",
                    "카고링크 사업팀",
                    "책임",
                    "서울특별시 강서구 마곡중앙4로 22",
                    "파인스퀘어 B동 7층",
                    "07631",
                    "1537462492",
                    R.drawable.img_hansol_logo
                ),
                CardItem(
                    5,
                    "김동재",
                    "010-1111-2222",
                    "02-348-4547",
                    "dongjae.kim@hansol.com",
                    "(주)한솔로지스유",
                    "카고링크 사업팀",
                    "책임",
                    "서울특별시 강서구 마곡중앙4로 22",
                    "파인스퀘어 B동 7층",
                    "07631",
                    "1537462492",
                    R.drawable.img_hansol_logo
                ),
                CardItem(
                    6,
                    "김동재",
                    "010-1111-2222",
                    "02-348-4547",
                    "dongjae.kim@hansol.com",
                    "(주)한솔로지스유",
                    "카고링크 사업팀",
                    "책임",
                    "서울특별시 강서구 마곡중앙4로 22",
                    "파인스퀘어 B동 7층",
                    "07631",
                    "1537462492",
                    R.drawable.img_hansol_logo
                )
            ))
        // setContent = Composable UI 시작 지점.
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Scaffold (
                    modifier = Modifier
                        .padding(WindowInsets.statusBars.asPaddingValues())
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .fillMaxSize(),
                    topBar = { appBar() },
                    bottomBar = {bottomNavigationBar(navController)},
                    floatingActionButton = { floatingActionButton() },
                    floatingActionButtonPosition = FabPosition.Center,
                    isFloatingActionButtonDocked = true
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(vm) }
                        composable("community") { CommunityScreen() }
                        composable("profile") { ProfileScreen() }
                        composable("settings") { SettingsScreen() }
                    }
                }
            }
        }
    }
}

class HomeViewModel: ViewModel() {

    private var cardList = mutableStateListOf<CardItem>()

    fun addAllItems(items: List<CardItem>) {
        cardList.addAll(items)
    }

    fun addItem(item: CardItem) {
        cardList.add(item)
    }

    fun removeItem(item: CardItem) {
        cardList.remove(item)
    }

    fun getItems() : List<CardItem> {
        return cardList
    }
}

    @comPreView(showBackground = true)
    @Composable
    fun appBar() {
        val typography = MaterialTheme.typography
        TopAppBar(
            backgroundColor = Color.Black,
            title = {
                /*Text(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    text = "김동재 책임님",
                    style = typography.h6,
                    color = Color.Black,
                    fontSize = 28.sp
                )*/
            },
            navigationIcon = {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Wallet",
                    style = typography.h6,
                    color = Color.White,
                    fontSize = 18.sp
                )
            },
            actions = {
                Icon(modifier = Modifier.padding(end = 10.dp), imageVector = Icons.Default.Menu, tint = Color.White, contentDescription = "Search")
            }
        )
    }

    @comPreView(showBackground = true)
    @Composable
    fun SearchableTopBar(
        modifier: Modifier = Modifier,          // 수정자
        searchMode: Boolean,                    // 검색 모드 ON/OFF 상태 확인
        searchText: String,                     // 검색창 텍스트
        onSearchTextChanged: (String) -> Unit,  // 검색창 테스트가 바뀔때 호출
    ){
                TopAppBar(
                    modifier = modifier,
                    backgroundColor = Color.Black
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.weight(1f),
                        visible = true,
                        enter = scaleIn() + expandHorizontally(),
                        exit = scaleOut() + shrinkHorizontally()
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .background(
                                    Color.White,
                                    RoundedCornerShape(3.dp)
                                )
                                .padding(top = 5.dp, bottom = 5.dp)
                                .height(36.dp),
                            value = searchText,
                            onValueChange = onSearchTextChanged,
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colors.primary),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = MaterialTheme.typography.body2.fontSize
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(Modifier.weight(1f)) {
                                        if (searchText.isEmpty()) Text(
                                            text = "성명 또는 회사명을 입력해주세요.",
                                            style = LocalTextStyle.current.copy(
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                                                fontSize = MaterialTheme.typography.body2.fontSize
                                            )
                                        )
                                        innerTextField()
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Search Icon",
                                            tint = LocalContentColor.current.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        )
                    }
        }
    }

    @comPreView(showBackground = true)
    @Composable
    fun HomeScreen(vm: HomeViewModel) {
        var searchMode by remember { mutableStateOf(false) }
        var searchText by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer( modifier = Modifier
                .height(1.dp)
                .background(Color.DarkGray)
                .fillMaxSize(),
            )
            SearchableTopBar(
                modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                searchMode = searchMode,
                searchText = searchText,
                onSearchTextChanged = {searchText = it},
            )
            nameCardList(vm = vm, searchText = searchText)
        }
    }

    @comPreView(showBackground = true)
    @Composable
    fun CommunityScreen() {
        var boardList: List<BoardItem> = listOf(
            BoardItem(
                boardId = 1,
                title = "테스트 No1",
                content = "테스트 입니다. 테스트 입니다테스트 입니다테스트 입니다테스트 입니다테스트 입니다테스트 입니다테스트 입니다",
                caregory = "명함공유",
                regId = "kimDongjae",
                regDate = "2025-08-19 16:12:38",
                view = 38,
                like = 11,
                replyCnt = 5
            )
        )
        LazyColumn {
            items(boardList) { board ->
                boardCard(board)
            }
        }
    }

    @Composable
    fun boardCard(boardItem: BoardItem) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ){
            Column {
                Text(
                    text = boardItem.title
                )
                Text(
                    text = boardItem.content
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text =  comm.DiffToNowDate(boardItem.regDate),
                        color = Color.Gray,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Outlined.RemoveRedEye,
                            contentDescription = "조회수",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 3.dp, end = 10.dp),
                            text = boardItem.view.toString(),
                            color = Color.Gray,
                            style = TextStyle(
                                fontSize = 11.sp,
                            )
                        )
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "좋아요",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray,
                        )
                        Text(
                            modifier = Modifier.padding(start = 3.dp, end = 10.dp),
                            text = boardItem.like.toString(),
                            color = Color.Gray,
                            style = TextStyle(
                                fontSize = 11.sp,
                            )
                        )
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = "댓글갯수",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = boardItem.replyCnt.toString(),
                            color = Color.Gray,
                            style = TextStyle(
                                fontSize = 11.sp,
                            )
                        )
                    }
                }
            }
        }
    }

    @comPreView(showBackground = true)
    @Composable
    fun ProfileScreen() {
        Text("ProfileScreen 입니다.")
    }

    @comPreView(showBackground = true)
    @Composable
    fun SettingsScreen() {
        Text("SettingsScreen 입니다.")
    }

    @comPreView(showBackground = true)
    @kotlin.OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun nameCardList(vm: HomeViewModel, searchText: String) {
        val listItems = vm.getItems()
        Log.d("qwer123456","으잉뭐야 => " + listItems.size)

        val filterList by remember(searchText, listItems) {
            mutableStateOf(
                if (searchText.isNotEmpty()) {
                    listItems.filter {
                        it.company.contains(searchText, true) ||
                                it.name.contains(searchText, true)
                    }
                } else {
                    listItems
                }
            )
        }

        LazyColumn {
            stickyHeader {
                Header()
            }
            items(filterList) { userNameCard ->
                Card(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
                ){
                    Row {
                        Column(
                            modifier = Modifier
                                .padding(start = 15.dp, top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                        ) {
                            Image(
                                painter = painterResource(id = userNameCard.bizLogo),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(125.dp)
                                    .height(60.dp)
                            )
                            Text(
                                text = userNameCard.company,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = userNameCard.comPost,
                                style = TextStyle(fontSize = 10.sp)
                            )
                            Text(
                                text = userNameCard.comAddr,
                                style = TextStyle(fontSize = 10.sp)
                            )
                            Text(
                                text = userNameCard.comAddrDetail,
                                style = TextStyle(fontSize = 10.sp)
                            )
                        }
                        Column (
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth()
                                .weight(1f),
                            ){
                            Box(modifier = Modifier.height(45.dp))
                            Column {
                                Text(
                                    text = userNameCard.name,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${userNameCard.comRank} / ${userNameCard.comDept}",
                                    style = TextStyle(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Row(
                                    modifier = Modifier.padding(top = 5.dp)
                                ){
                                    Text(
                                        text = "T",
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            color = Color.Blue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = userNameCard.telNum,
                                        style = TextStyle(fontSize = 10.sp)
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(top = 3.dp),
                                ){
                                    Text(
                                        text = "M",
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            color = Color.Blue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = userNameCard.phoneNum,
                                        style = TextStyle(fontSize = 10.sp)
                                    )
                                }
                                Row(modifier = Modifier.padding(top = 3.dp)){
                                    Text(
                                        text = "E",
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            color = Color.Blue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = userNameCard.email,
                                        style = TextStyle(fontSize = 10.sp)
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @comPreView(showBackground = true)
    @Composable
    fun bottomNavigationBar(navController: NavController) {
        val items = listOf(
            BottomNavItem("홈", "home", Icons.Filled.Home, Icons.Outlined.Home),
            BottomNavItem("커뮤니티", "community", Icons.Filled.SpaceDashboard, Icons.Outlined.SpaceDashboard),
            BottomNavItem("", "", null, null), // 가운데 공백
            BottomNavItem("내 프로필", "profile", Icons.Filled.Person, Icons.Outlined.Person),
            BottomNavItem("설정", "settings", Icons.Filled.Settings, Icons.Outlined.Settings)
        )
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        NavigationBar(
           contentColor = Color.White,
           windowInsets = WindowInsets(0, 0, 0, 0), // 🔹 인셋 제거
           modifier = Modifier.height(66.dp),
           tonalElevation = 0.dp
       ){
           items.forEach{ item ->
               val isSelected = currentDestination?.route == item.route
               NavigationBarItem(
                   selected = isSelected,
                   onClick = {
                       if(item.route.isNotEmpty()){
                           navController.navigate(item.route){
                               popUpTo(navController.graph.startDestinationId) { saveState = true}
                               launchSingleTop = true
                               restoreState = true
                           }
                       }
                   },
                   icon = {
                       val icon = if (isSelected) item.selectedIcon else item.unselectedIcon
                       icon?.let {
                           Icon(
                               imageVector = it,
                               contentDescription = item.title,
                               modifier = Modifier
                                   .size(24.dp) // width+height를 size로 통합
                                   .padding(bottom = 4.dp),
                               tint = if (isSelected) Color.Black else Color.LightGray
                           )
                       }
                   },
                   label = {
                       if (isSelected)
                       Text(
                           item.title,
                           style = TextStyle(
                               fontSize = 11.sp,
                               color = Color.Black,
                               fontWeight = FontWeight.Bold
                            )
                          )
                       },
                   colors = NavigationBarItemDefaults.colors(
                       indicatorColor = Color.LightGray
                   ),
               )
           }
       }
    }

    @comPreView(showBackground = true)
    @Composable
    fun floatingActionButton() {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    //.graphicsLayer { translationY = 35.dp.toPx() }
                    .size(56.dp)
                    .shadow(2.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .border(1.5.dp, Color.Black, CircleShape)
                    .clickable {  }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "추가",
                    tint = Color.Black
                )
            }
    }


    @OptIn(ExperimentalGetImage::class)
    @Composable
    fun faceDetectionScreen() {
        val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val previewView = remember { PreviewView(context) }
        var previewWidth by remember { mutableStateOf(0f) }
        var previewHeight by remember { mutableStateOf(0f) }
        val faceBounds = remember { mutableStateListOf<Rect>() }

        var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }

        fun createCameraSelector(): CameraSelector {
            return CameraSelector.Builder().requireLensFacing(lensFacing).build()
        }

        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val cameraProvider = remember(cameraProviderFuture) {
            cameraProviderFuture.get()
        }

        // 📸 카메라 설정 함수
        fun bindCamera() {

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val faceDetector = FaceDetection.getClient(
                FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST) // 얼굴 인식 시 속도 또는 정확도 우선
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE) // 눈,귀.코,뺨,입등 얼굴의 랜드마크 식별 여부
                    //.setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL) // 얼굴 특징의 윤곽선 인식 여부. 뚜렷한 얼굴만
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE) // 웃고있음, 눈을뜸 카테고리 분류
                    //.enableTracking() // 얼굴에 ID를 할당할지 여부. 서로 다른 이미지에서 얼굴 추적하는데 사용. 인식속도 높이려면 사용X
                    .build()
            )

            val analyzer = ImageAnalysis.Builder()
                //.setTargetResolution(android.util.Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        val mediaImg = imageProxy.image
                        if (mediaImg != null) {
                            val input = InputImage.fromMediaImage(
                                mediaImg,
                                imageProxy.imageInfo.rotationDegrees
                            )
                            faceDetector.process(input)
                                .addOnSuccessListener { faces ->
                                    faceBounds.clear()
                                    faces.forEach { face ->
                                        faceBounds.add(face.boundingBox)
                                    }
                                }.addOnCompleteListener { imageProxy.close() }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.Builder().requireLensFacing(lensFacing).build(),
                    preview,
                    analyzer
                )
            } catch (exc: Exception) {
                Log.e("Camera", "Binding failed", exc)
            }
        }

        // ✅ 카메라 권한 요청
        LaunchedEffect(Unit) {
            if (!cameraPermission.status.isGranted) {
                cameraPermission.launchPermissionRequest()
            } else {
                bindCamera()
            }
        }

        // ✅ lensFacing 변경될 때마다 카메라 재바인딩
        LaunchedEffect(lensFacing) {
            if (cameraPermission.status.isGranted) {
                bindCamera()
            }
        }

        if (cameraPermission.status.isGranted) {
            Box(modifier = Modifier.navigationBarsPadding()) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            previewWidth = coordinates.size.width.toFloat()
                            previewHeight = coordinates.size.height.toFloat()
                        }
                )

                // 얼굴 인식 결과 사각형 그리기
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaleX = previewWidth / 480f  // 분석 해상도의 가로 (640x480 기준이지만 실제는 portrait 기준)
                    val scaleY = previewHeight / 640f
                    faceBounds.forEach { rect ->
                        val left = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                            480 - rect.right // 좌우 반전
                        } else {
                            rect.left
                        }
                        drawRect(
                            color = Color.Green,
                            topLeft = Offset(rect.left * scaleX, rect.top * scaleY),
                            size = Size(rect.width() * scaleX, rect.height() * scaleY),
                            style = Stroke(width = 4f)
                        )
                    }
                }

                // 📌 카메라 전환 버튼
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT)
                            CameraSelector.LENS_FACING_BACK
                        else
                            CameraSelector.LENS_FACING_FRONT
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Loop,
                        contentDescription = "카메라 전환",
                        tint = Color.White
                    )
                }
            }
        } else {
            // 권한 없을 때 메시지
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("카메라 권한이 필요합니다.")
            }
        }
    }

    @Composable
    private fun MainScreen() {
        val context = LocalContext.current

        var imageBitmap by remember { mutableStateOf(ImageBitmap(32, 32)) }

        val faces = remember { mutableStateListOf<Face>() }
        LaunchedEffect(imageBitmap) {
            faces.clear()

            val image = InputImage.fromBitmap(imageBitmap.asAndroidBitmap(), 0)
            faces.addAll(ImageProcessor.processInputImage(image))
        }

        //  권한요청
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("test5", "권한이 동의되었습니다.")
            } else {
                Log.d("test5", "권한이 거부되었습니다.")
            }
        }

        // 이미지 가져오기
        val pickGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) {
            if (it == null) throw NullPointerException()
            val source = ImageDecoder.createSource(context.contentResolver, it)
            imageBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.setTargetSize(
                    1080,
                    1080
                )
            }.asImageBitmap()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    if (checkSinglePermission(context, Manifest.permission.READ_MEDIA_IMAGES)) {
                        pickGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
            ) {
                Text(text = "갤러리에서 불러오기")
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawImage(image = imageBitmap)
                faces.forEach { face ->
                    val rect = face.boundingBox
                    drawRect(
                        color = Color.Green,
                        style = Stroke(
                            width = 2.dp.toPx()
                        ),
                        topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                        size = Size(rect.width().toFloat(), rect.height().toFloat())
                    )
                }
            }
            // 얼굴에 사각형 그리기
        }

    }

    object ImageProcessor {
        private val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        private val detector = FaceDetection.getClient(highAccuracyOpts)

        suspend fun processInputImage(image: InputImage): List<Face> {
            return suspendCoroutine { continuation ->
                detector.process(image).addOnSuccessListener { faces ->
                    continuation.resume(faces)
                }.addOnFailureListener {
                    throw it
                }
            }
        }
    }

    fun checkSinglePermission(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Test", "권한이 이미 존재합니다.")
            return true
        }
        return false
    }

    fun checkMultiplePermission(context: Context, permission: List<String>): Boolean {
        permission.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?
)
