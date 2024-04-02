package dev.jacobderynk.asterogram.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import dev.jacobderynk.asterogram.R
import dev.jacobderynk.asterogram.ui.theme.Typography
import dev.jacobderynk.asterogram.utils.DateTimeManager
import dev.jacobderynk.asterogram.utils.LikesFormatter
import java.nio.ByteBuffer
import kotlin.random.Random

@Composable
fun PostCard(
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    svgString: String,
    name: String,
    year: String,
) {
    val userColor = remember {
        Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
    }

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(40.dp),
                tint = userColor,
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Localized description"
            )

            Spacer(Modifier.size(4.dp))

            Text(text = name, style = Typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .placeholder(R.drawable.onboarding_save)
                    .fallback(R.drawable.ic_launcher_background)
                    .data(ByteBuffer.wrap(svgString.toByteArray()))
                    .size(Size.ORIGINAL)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }

        BottomSection(bookmarked, onBookmarkClick, name, year)
    }

}

@Composable
private fun BottomSection(
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    name: String,
    year: String,
){
    val context = LocalContext.current
    var liked by rememberSaveable { mutableStateOf(false) }
    val postLikes by rememberSaveable {
        mutableStateOf(
            LikesFormatter.formatLikeCount(LikesFormatter.generateRandomLikes())
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){

        Row {
            IconButton(
                onClick = { liked = !liked },
                modifier = Modifier.then(Modifier.size(40.dp))
            ) {
                if (liked) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.Favorite,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Localized description"
                    )
                }
            }

            IconButton(
                onClick = {
                    Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.then(Modifier.size(40.dp))
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.icon_comment),
                    contentDescription = "Localized description"
                )
            }

            IconButton(
                onClick = {
                    Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.then(Modifier.size(40.dp))
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.icon_send),
                    contentDescription = "Localized description"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    onBookmarkClick.invoke()
                },
                modifier = Modifier.then(Modifier.size(40.dp))
            ) {
                if (bookmarked) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.icon_bookmark_filled),
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.icon_bookmark),
                        contentDescription = "Localized description"
                    )
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = postLikes, fontWeight = FontWeight.SemiBold)
            Text(text = name, fontWeight = FontWeight.SemiBold)
            Text(text = DateTimeManager.formatResponseDatetime(year), style = Typography.labelMedium, color = Color.Gray)
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    PostCard(
        bookmarked = true,
        onBookmarkClick = {},
        svgString = "",
        name = "Lorem Ipsum Name",
        year = "1999-01-01T00:00:00.000"
    )
}