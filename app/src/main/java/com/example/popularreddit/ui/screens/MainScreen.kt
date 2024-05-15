package com.example.popularreddit.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.popularreddit.R
import com.example.popularreddit.ui.DarkGrey
import com.example.popularreddit.ui.MainBlue

@Composable
fun MainScreen() {

    val orientation = LocalConfiguration.current.orientation

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxSize()) {

                RedditTopBar()

                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> RedditContentVertical()
                    else -> RedditContentHorizontal()
                }
            }

            AnimatedVisibility(
                visible = false, enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                //Full Screen Image
                var fullScreenImageVisibility by rememberSaveable { mutableStateOf(false) }
            }
        }
    }
}

@Composable
private fun RedditContentVertical() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        item {
            InformationBanner {}
        }
        items(20) {
            RedditCardVertical()
        }
    }
}

@Composable
private fun RedditContentHorizontal() {
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        items(20) {
            RedditCardHorizontal()
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
                    painter = painterResource(id = R.drawable.reddit_logo),
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
private fun RedditCardVertical() {
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
                text = "Alex",
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.spartan_medium))
            )
            Text(
                text = "10 hours ago",
                color = DarkGrey,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.spartan_medium))
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MainBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Image from Reddit", color = Color.White)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 5.dp, end = 4.dp),
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
                    text = "1918",
                    color = DarkGrey,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )

            }
            Image(
                modifier = Modifier
                    .width(20.dp)
                    .wrapContentHeight(),
                painter = painterResource(id = R.drawable.icon_not_saved),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun RedditCardHorizontal() {
    Row(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(MainBlue),
            contentAlignment = Alignment.Center
        ) { Text(text = "Image from Reddit", color = Color.White) }

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
                    text = "Alex",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "10 hours ago",
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
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(MainBlue)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "1918",
                    color = DarkGrey,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spartan_medium))
                )

            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp)
                    .size(20.dp)
                    .background(MainBlue)
            )
        }
    }
}

@Composable
private fun InformationBanner(onCloseButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
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
                    modifier = Modifier.wrapContentWidth().padding(end = 14.dp),
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