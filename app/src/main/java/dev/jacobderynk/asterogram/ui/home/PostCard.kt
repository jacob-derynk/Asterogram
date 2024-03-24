package dev.jacobderynk.asterogram.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import dev.jacobderynk.asterogram.utils.generateSvgAsteroid
import java.nio.ByteBuffer

@Composable
fun PostCard(
    name: String,
    year: String,
) {

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Localized description"
            )

            Spacer(Modifier.size(4.dp))

            Text(text = name, style = Typography.titleMedium, fontWeight = FontWeight.SemiBold)

        }

        Box(Modifier.fillMaxWidth()) {
            val svgString: String by rememberSaveable {
                mutableStateOf(generateSvgAsteroid())
            }

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .placeholder(R.drawable.ic_launcher_background)
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

        BottomSection(name, year)
    }

}

@Composable
private fun BottomSection(
    name: String,
    year: String,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Localized description"
            )

            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.icon_comment),
                contentDescription = "Localized description"
            )

            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.icon_send),
                contentDescription = "Localized description"
            )


        }

        Column(modifier = Modifier.fillMaxWidth().padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "924 Likes", fontWeight = FontWeight.SemiBold)
            Text(text = name, fontWeight = FontWeight.SemiBold)
            Text(text = year, style = Typography.labelMedium, color = Color.Gray)
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    PostCard(
        name = "Lorem Ipsum Name",
        year = "1996"
    )
}