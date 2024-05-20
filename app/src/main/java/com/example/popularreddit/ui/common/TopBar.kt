package com.example.popularreddit.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.popularreddit.R
import com.example.popularreddit.ui.common.themes.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditTopBar() {
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
