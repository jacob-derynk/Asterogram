package dev.jacobderynk.asterogram.ui.profile

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import dev.jacobderynk.asterogram.R
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.ui.home.PostCard
import dev.jacobderynk.asterogram.ui.theme.AsterogramTheme
import dev.jacobderynk.asterogram.ui.theme.Typography
import dev.jacobderynk.asterogram.utils.NumbersFormatter
import java.nio.ByteBuffer

sealed class ProfileScreenTabs(@DrawableRes val icon: Int) {
    data object GridView : ProfileScreenTabs(R.drawable.icon_grid)
    data object RowView : ProfileScreenTabs(R.drawable.icon_rows)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    profileName: String,
    postsNumber: String,
    posts: List<AsteroidEntity>,
    onEditUsernameClick: () -> Unit,
) {
    var state by remember { mutableIntStateOf(0) }
    val tabs = listOf(ProfileScreenTabs.GridView, ProfileScreenTabs.RowView)

    @Composable
    fun TabsHeader() {
        PrimaryTabRow(
            selectedTabIndex = state,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = "Tab ${index + 1}"
                        )
                    }
                )
            }
        }
    }

    // Content

    Column {
        when (state) {
            // Grid view
            0 -> {
                Column {
                    ProfileHeader(profileName, postsNumber, onEditUsernameClick)
                    TabsHeader()
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 3),
                    contentPadding = PaddingValues(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                    items(
                        items = posts,
                        key = { it.id }
                    ) { photo ->
                        GridPost(svgString = photo.svg ?: "")
                    }
                }
            }
            // Row view
            1 -> {
                LazyColumn {
                    item {
                        ProfileHeader(profileName, postsNumber, onEditUsernameClick)
                        TabsHeader()
                    }

                    items(
                        items = posts,
                        key = { it.id }
                    ) {
                        val context = LocalContext.current
                        PostCard(
                            bookmarked = it.isSaved,
                            onBookmarkClick = {
                                // TODO add bookmark removing
                                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
                            },
                            svgString = it.svg ?: "",
                            name = it.name,
                            year = it.year,
                            `class` = it.`class`,
                            mass = NumbersFormatter.formatMass(it.mass),
                            fall = it.fall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GridPost(svgString: String) {

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
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ProfileHeader(
    profileName: String,
    postsNumber: String,
    onEditUsernameClick: () -> Unit,
) {

    Column(Modifier.padding(8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Icon(
                modifier = Modifier.size(80.dp),
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "$profileName profile picture"
            )

            Spacer(Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = postsNumber, style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(text = stringResource(R.string.posts))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "128K", style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(text = stringResource(R.string.followers))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "256", style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(text = stringResource(R.string.following))
            }
        }

        Column(Modifier.padding(start = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = profileName,  style = Typography.titleLarge, fontWeight = FontWeight.SemiBold)
                IconButton(onClick = { onEditUsernameClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit username")
                }
            }
            Text(text = "Some super cool profile description")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenContentPreview() {
    AsterogramTheme {
        Column {
            ProfileScreenContent(profileName = "Franta Pepa Jednicka", postsNumber = "1916", posts = listOf()) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    ProfileHeader(
        profileName = "Lorem Ipsum Name",
        postsNumber = "14",
        onEditUsernameClick = {},
    )
}