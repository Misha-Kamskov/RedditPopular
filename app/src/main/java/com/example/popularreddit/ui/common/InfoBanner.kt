package com.example.popularreddit.ui.common

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.popularreddit.R


@Composable
fun InformationBanner(modifier: Modifier = Modifier, onCloseButtonClick: () -> Unit) {

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
