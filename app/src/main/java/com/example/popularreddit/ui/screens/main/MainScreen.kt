package com.example.popularreddit.ui.screens.main

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.models.models.Const.REDDIT_THUMBNAIL_DEFAULT
import com.example.models.models.posts.entities.Post
import com.example.popularreddit.ui.common.mediaview.FullScreenPhoto
import com.example.popularreddit.R
import com.example.popularreddit.managers.DownloadManager
import com.example.models.models.appsettings.model.AppSettings
import com.example.popularreddit.ui.common.themes.DarkGrey
import com.example.popularreddit.ui.common.themes.MainBlue
import com.example.models.models.appsettings.model.AppSettingsPrefs
import com.example.popularreddit.ui.common.InformationBanner
import com.example.popularreddit.ui.common.RedditTopBar
import com.example.popularreddit.ui.common.mediaview.Player
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel, settingsStore: AppSettingsPrefs) {

    val state by mainViewModel.state.collectAsState()
    val posts = state.posts.collectAsLazyPagingItems()

    val orientation = LocalConfiguration.current.orientation
    val infoBannerClosed = rememberSaveable { mutableStateOf(true) }

    var currentFullImageUrl by rememberSaveable { mutableStateOf("") }
    var fullScreenImageVisibility by rememberSaveable { mutableStateOf(false) }
    var needGetTopPosts by rememberSaveable { mutableStateOf(true) }

    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        if (needGetTopPosts) {
            mainViewModel.applyAction(MainAction.GetTopPosts)
            needGetTopPosts = false
        }
    }

    LaunchedEffect(true) {
        settingsStore.getSettings().collect { settings ->
            infoBannerClosed.value = settings.infoBannerClosed
        }
    }

    LaunchedEffect(key1 = infoBannerClosed.value) {
        settingsStore.getSettings().collect { settings ->
            if (infoBannerClosed.value != settings.infoBannerClosed)
                settingsStore.putSettings(AppSettings(infoBannerClosed = infoBannerClosed.value))
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxSize()) {

                RedditTopBar()

                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        RedditContentVertical(
                            posts = posts,
                            listState = listState,
                            infoBannerClosed = infoBannerClosed,
                            onCardImageClick = { url ->
                                url?.let {
                                    currentFullImageUrl = url
                                    fullScreenImageVisibility = true
                                }
                            })
                    }

                    else -> RedditContentHorizontal(posts = posts,
                        listState = listState,
                        infoBannerClosed,
                        onCardImageClick = { url ->
                            url?.let {
                                currentFullImageUrl = url
                                fullScreenImageVisibility = true
                            }
                        })
                }

            }
            AnimatedVisibility(
                visible = fullScreenImageVisibility, enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                FullScreenPhoto(
                    imageUrl = currentFullImageUrl,
                    onDismiss = { fullScreenImageVisibility = false })
            }
        }

        MainEventsProcessor(viewModel = mainViewModel)
    }
}

@Composable
private fun MainEventsProcessor(
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    when (val event = viewModel.event.collectAsState(initial = null).value) {
        is MainEvent.ShowError -> {
            Toast.makeText(context, stringResource(id = event.message), Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }
}

@Composable
private fun RedditContentVertical(
    posts: LazyPagingItems<Post>,
    listState: LazyListState,
    infoBannerClosed: MutableState<Boolean>,
    onCardImageClick: (imageUrl: String?) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            AnimatedVisibility(
                visible = !infoBannerClosed.value,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> 2 * fullWidth }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> 2 * fullWidth }) + fadeOut()
            ) {
                InformationBanner()
                { infoBannerClosed.value = true }
            }
        }
        items(posts.itemCount) {
            posts[it]?.let { post ->
                if (post.widthThumbnail != null && post.heightThumbnail != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    RedditCardVertical(post = post) { imageId ->
                        onCardImageClick(imageId)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        item {
            when (posts.loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingItem()
                }

                is LoadState.NotLoading, is LoadState.Error -> {
                    ErrorItem(stringResource(id = R.string.error_message_connection)) { posts.retry() }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun RedditContentHorizontal(
    posts: LazyPagingItems<Post>,
    listState: LazyListState,
    infoBannerClosed: MutableState<Boolean>,
    onCardImageClick: (imageId: String?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 50.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                AnimatedVisibility(
                    visible = !infoBannerClosed.value,
                    enter = slideInHorizontally(initialOffsetX = { fullWidth -> 2 * fullWidth }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { fullWidth -> 2 * fullWidth }) + fadeOut()
                ) {
                    InformationBanner()
                    { infoBannerClosed.value = true }
                }
            }

            items(posts.itemCount) {
                posts[it]?.let { post ->
                    if (post.widthThumbnail != null && post.heightThumbnail != null) {
                        Spacer(modifier = Modifier.width(20.dp))
                        RedditCardHorizontal(post = post) { imageId ->
                            onCardImageClick(imageId)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }

            item {
                when (posts.loadState.refresh) {
                    is LoadState.Loading -> {
                        LoadingItem()
                    }

                    is LoadState.NotLoading, is LoadState.Error -> {
                        ErrorItem(stringResource(id = R.string.error_message_connection)) { posts.retry() }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun RedditCardVertical(
    post: Post,
    onCardImageClick: (imageUrl: String?) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (post.isVideo) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFE5EBEE)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.authorName,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
                Text(
                    text = "${post.timeOfCreation} hours ago",
                    color = DarkGray,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
            }

            post.videoUrl?.let { Player(it) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Image(
                        modifier = Modifier
                            .width(25.dp)
                            .wrapContentHeight(),
                        painter = painterResource(id = R.drawable.icon_comment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = post.numComments.toString(),
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.spartan_medium))
                    )

                }
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .wrapContentHeight().clickable {
                                post.videoUrl?.let {
                                    DownloadManager.downloadMedia(
                                        context,
                                        it,
                                        "reddit"
                                    )
                                }
                        },
                    painter = painterResource(id = R.drawable.icon_not_saved),
                    contentDescription = null
                )
            }
        }
    } else {
        if (post.widthThumbnail != null && post.heightThumbnail != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE5EBEE)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (post.thumbnail == REDDIT_THUMBNAIL_DEFAULT) {
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .background(MainBlue)
                        .padding(horizontal = 5.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlDest))
                            context.startActivity(intent)
                        }) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 13.dp),
                            text = "Open link",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.spartan_medium))
                        )
                    }
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(post.thumbnail),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 10.dp)
                            .width(post.widthThumbnail!!.dp)
                            .height(post.heightThumbnail!!.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onCardImageClick(post.urlDest ?: post.thumbnail) }

                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 5.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = post.authorName,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.spartan_medium))
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "${post.timeOfCreation} hours ago",
                            color = DarkGrey,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.spartan_medium))
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Image(
                                modifier = Modifier
                                    .width(25.dp)
                                    .wrapContentHeight(),
                                painter = painterResource(id = R.drawable.icon_comment),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = post.numComments.toString(),
                                color = DarkGray,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.spartan_medium))
                            )

                        }
                        Image(
                            modifier = Modifier
                                .width(20.dp)
                                .wrapContentHeight()
                                .clickable {
                                    coroutineScope.launch {
                                        post.videoUrl?.let {
                                            DownloadManager.downloadMedia(
                                                context,
                                                it,
                                                "reddit"
                                            )
                                        }
                                        if (post.thumbnail != REDDIT_THUMBNAIL_DEFAULT && post.thumbnail != null) {
                                            (post.thumbnail)?.let {
                                                DownloadManager.downloadMedia(context, it, "reddit")
                                            }
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.image_is_not_available),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                },
                            painter = painterResource(id = R.drawable.icon_not_saved),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun RedditCardHorizontal(
    post: Post, onCardImageClick: (imageUrl: String?) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .wrapContentWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE5EBEE))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (post.isVideo) {
            post.videoUrl?.let { Player(it) }
        } else {
            if (post.thumbnail == REDDIT_THUMBNAIL_DEFAULT) {
                Box(modifier = Modifier
                    .wrapContentSize()
                    .background(MainBlue)
                    .padding(horizontal = 5.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlDest))
                        context.startActivity(intent)
                    }) {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 13.dp),
                        text = "Open link",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.spartan_medium))
                    )
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(post.thumbnail),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 10.dp)
                        .width(post.widthThumbnail!!.dp)
                        .height(post.heightThumbnail!!.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onCardImageClick(post.urlDest ?: post.thumbnail) }

                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = post.authorName,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${post.timeOfCreation} hours ago",
                    color = DarkGray,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
            }
            Column(Modifier.padding(horizontal = 5.dp, vertical = 4.dp)) {
                Row {
                    Image(
                        modifier = Modifier
                            .width(25.dp)
                            .wrapContentHeight(),
                        painter = painterResource(id = R.drawable.icon_comment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = post.numComments.toString(),
                        color = DarkGray,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.spartan_medium))
                    )

                }
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .wrapContentHeight()
                        .clickable {
                            scope.launch {
                                if (post.isVideo){
                                    post.videoUrl?.let {
                                        DownloadManager.downloadMedia(
                                            context,
                                            it,
                                            "reddit"
                                        )
                                    }
                                }else{
                                    if (post.thumbnail != REDDIT_THUMBNAIL_DEFAULT && post.thumbnail != null) {
                                        (post.thumbnail)?.let {
                                            DownloadManager.downloadMedia(context, it, "reddit")
                                        }
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                context.getString(R.string.image_is_not_available),
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                        },
                    painter = painterResource(id = R.drawable.icon_not_saved),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun LoadingItem() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Restart),
        label = ""
    )
    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .rotate(angle),
            painter = painterResource(id = R.drawable.reddit_bar_logo),
            contentDescription = null
        )
    }
}

@Composable
fun ErrorItem(message: String, onClickRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = message,
            color = Color.Black,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily(Font(R.font.spartan))
        )
        ElevatedButton(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
            onClick = { onClickRetry() },
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = stringResource(R.string.retry),
                color = Color.White,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily(Font(R.font.spartan))
            )
        }
    }
}


