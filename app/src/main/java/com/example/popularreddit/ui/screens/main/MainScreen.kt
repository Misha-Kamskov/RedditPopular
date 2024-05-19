package com.example.popularreddit.ui.screens.main

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.popularreddit.ui.common.FullScreenPhoto
import com.example.popularreddit.R
import com.example.popularreddit.models.Utils
import com.example.popularreddit.models.appsettings.model.AppSettings
import com.example.popularreddit.ui.common.DarkGrey
import com.example.popularreddit.ui.common.MainBlue
import com.example.popularreddit.models.appsettings.model.AppSettingsPrefs
import com.example.popularreddit.models.posts.entities.Post
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel, settingsStore: AppSettingsPrefs) {

    val scope = rememberCoroutineScope()

    val state by mainViewModel.state.collectAsState()

    val orientation = LocalConfiguration.current.orientation
    val infoBannerClosed = rememberSaveable { mutableStateOf(true) }
    var currentFullImageUrl by rememberSaveable { mutableStateOf("") }
    var fullScreenImageVisibility by rememberSaveable { mutableStateOf(false) }
    var needGetTopPosts by rememberSaveable { mutableStateOf(true) }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }


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

                if ((!state.loading || !state.retryButtonVisible) && state.posts.isNotEmpty()) {
                    when (orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> RedditContentVertical(
                            data = state,
                            listState = listState,
                            infoBannerClosed = infoBannerClosed,
                            onCardImageClick = { url ->
                                url?.let {
                                    currentFullImageUrl = url
                                    fullScreenImageVisibility = true
                                }
                            })

                        else -> RedditContentHorizontal(data = state,
                            listState = listState,
                            infoBannerClosed,
                            onCardImageClick = { url ->
                                url?.let {
                                    currentFullImageUrl = url
                                    fullScreenImageVisibility = true
                                }
                            })
                    }
                } else {
                    LoadingAnaRefreshScreen(
                        loading = state.loading,
                        retryButtonVisible = state.retryButtonVisible
                    ) {
                        scope.launch {
                            mainViewModel.applyAction(MainAction.GetTopPosts)
                        }
                    }
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
    data: MainState,
    listState: LazyListState,
    infoBannerClosed: MutableState<Boolean>,
    onCardImageClick: (imageUrl: String?) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
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

        items(20) {
            RedditCardVertical(post = data.posts[it]) { contentUrl ->
                onCardImageClick(contentUrl)
            }
        }
    }
}

@Composable
private fun RedditContentHorizontal(
    data: MainState,
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
            horizontalArrangement = Arrangement.spacedBy(50.dp)
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

            items(20) {
                RedditCardHorizontal(
                    post = data.posts[it]
                ) { imageId ->
                    onCardImageClick(imageId)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RedditTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(vertical = 3.dp),
                    painter = painterResource(id = R.drawable.reddit_bar_logo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.reddit_popular),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
            }
        },
        modifier = Modifier.shadow(8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MainBlue,
            titleContentColor = Color.White,
        )
    )
}

@Composable
private fun RedditCardVertical(
    post: Post,
    onCardImageClick: (imageUrl: String?) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
                color = DarkGrey,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.spartan_medium))
            )
        }
        val isImageExists =
            post.heightThumbnail != null && post.widthThumbnail != null && post.urlDest != null

        if (post.isVideo) {
            post.videoUrl?.let {

            }
        } else {
            val ratio = post.heightThumbnail?.let { post.widthThumbnail?.div(it) }
            if (isImageExists && ratio != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            onCardImageClick(post.urlDest ?: post.thumbnail)
                        },
                    contentScale = ContentScale.FillBounds,
                    painter = rememberAsyncImagePainter(post.urlDest ?: post.thumbnail),
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_image_or_video),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.spartan_medium))
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp, vertical = 4.dp),
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
                    color = DarkGrey,
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
                            if (isImageExists) {
                                (post.urlDest ?: post.thumbnail)?.let {
                                    Utils.downloadImage(context, it, "reddit")
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


@Composable
private fun RedditCardHorizontal(
    post: Post, onCardImageClick: (imageUrl: String?) -> Unit
) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isImageExists =
            post.heightThumbnail != null && post.widthThumbnail != null && post.urlDest != null
        val ratio = post.heightThumbnail?.let { post.widthThumbnail?.div(it) }

        if (isImageExists && ratio != null) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(ratio)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onCardImageClick(post.urlDest ?: post.thumbnail)
                    },
                contentScale = ContentScale.FillBounds,
                painter = rememberAsyncImagePainter(post.urlDest ?: post.thumbnail),
                contentDescription = null
            )
        } else {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_image_or_video),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .wrapContentWidth()
                .shadow(
                    8.dp, RoundedCornerShape(
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.padding(
                    top = 5.dp,
                    start = 10.dp,
                    end = 10.dp
                )
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

            Row(
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp
                )
            ) {
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
                    color = DarkGrey,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )

            }

            Image(
                modifier = Modifier
                    .width(20.dp)
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp)
                    .clickable {
                        if (isImageExists) {
                            (post.urlDest ?: post.thumbnail)?.let {
                                Utils.downloadImage(context, it, "reddit")
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
                    },
                painter = painterResource(id = R.drawable.icon_not_saved),
                contentDescription = null
            )
        }
    }
}


@Composable
private fun InformationBanner(modifier: Modifier = Modifier, onCloseButtonClick: () -> Unit) {

    val orientation = LocalConfiguration.current.orientation

    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(top = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .shadow(14.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF4500)),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.End)
                            .width(35.dp)
                            .wrapContentHeight()
                            .padding(top = 8.dp, end = 15.dp)
                            .clickable { onCloseButtonClick() },
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = null
                    )
                    Row(
                        modifier = Modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Image(
                            modifier = Modifier
                                .width(70.dp)
                                .wrapContentHeight()
                                .padding(horizontal = 14.dp),
                            painter = painterResource(id = R.drawable.icon_star),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 14.dp),
                            text = stringResource(R.string.info_banner_text),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily(Font(R.font.spartan))
                        )

                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f)
                    .padding(start = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .shadow(14.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF4500)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        modifier = Modifier
                            .width(70.dp)
                            .wrapContentHeight()
                            .padding(horizontal = 14.dp),
                        painter = painterResource(id = R.drawable.icon_star),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 14.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.info_banner_text),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily(Font(R.font.spartan))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Image(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(35.dp)
                        .wrapContentHeight()
                        .padding(top = 8.dp, end = 15.dp)
                        .clickable { onCloseButtonClick() },
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun LoadingAnaRefreshScreen(
    loading: Boolean,
    retryButtonVisible: Boolean,
    onRetryButtonClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Restart),
        label = ""
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (loading) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .rotate(angle),
                painter = painterResource(id = R.drawable.reddit_bar_logo),
                contentDescription = null
            )
        } else if (retryButtonVisible) {
            ElevatedButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp)),
                onClick = { onRetryButtonClick() },
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
}


