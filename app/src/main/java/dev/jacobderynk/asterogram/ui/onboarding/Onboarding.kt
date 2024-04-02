package dev.jacobderynk.asterogram.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import dev.jacobderynk.asterogram.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    finishOnboarding: () -> Unit,
    saveUsernameAction: (username: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var selectedUsername by remember { mutableStateOf("") }
    val onboardingPagesList = listOf(
        OnboardingPage.Welcome,
        OnboardingPage.CreateUsername { saveUsernameAction.invoke(selectedUsername) },
        OnboardingPage.LikeFeature,
        OnboardingPage.SaveFeature,
    )
    val pagerState = rememberPagerState(pageCount = {
        onboardingPagesList.size
    })

    fun finishOnboardingPage() {
        finishOnboarding.invoke()
        saveUsernameAction.invoke(selectedUsername)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = ::finishOnboardingPage,
        ) {
            Text(stringResource(R.string.skip_onboarding))
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {

                Text(
                    text = stringResource(onboardingPagesList[it].title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(24.dp))

                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(onboardingPagesList[it].imageRes),
                    contentDescription = "Localized description"
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    lineHeight = 1.6.em,
                    text = stringResource(onboardingPagesList[it].description),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )

                if (onboardingPagesList[pagerState.currentPage] is OnboardingPage.CreateUsername) {
                    OutlinedTextField(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth(),
                        value = selectedUsername,
                        onValueChange = { selectedUsername = it },
                        label = { Text(stringResource(R.string.username)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = {
                    if (pagerState.currentPage + 1 > 1) scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = pagerState.currentPage + 1 > 1
            ) {
                Text(stringResource(R.string.previous))
            }

            Spacer(modifier = Modifier.weight(1f))

            PageIndicators(size = onboardingPagesList.size, index = pagerState.currentPage)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (pagerState.currentPage + 1 < onboardingPagesList.size) scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    } else {
                        finishOnboardingPage()
                    }
                },
            ) {

                if (pagerState.currentPage + 1 < onboardingPagesList.size) { // has next
                    Text(stringResource(R.string.next))
                } else {
                    Text(stringResource(R.string.finish_onboarding))
                }
            }
        }

    }
}

@Composable
fun PageIndicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = ""
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primaryContainer
            )
    ) {}
}

sealed class OnboardingPage(
    @DrawableRes val imageRes: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
) {
    data object Welcome : OnboardingPage(
        imageRes = R.drawable.onboarding_welcome,
        title = R.string.welcome_title,
        description = R.string.welcome_description,
    )

    data class CreateUsername(
        val saveUsernameAction: (username: String) -> Unit,
    ) : OnboardingPage(
        imageRes = R.drawable.onboarding_username,
        title = R.string.username_title,
        description = R.string.username_description,
    )

    data object LikeFeature : OnboardingPage(
        imageRes = R.drawable.onboarding_like,
        title = R.string.like_title,
        description = R.string.like_description,
    )

    data object SaveFeature : OnboardingPage(
        imageRes = R.drawable.onboarding_save,
        title = R.string.save_title,
        description = R.string.save_description,
    )

}

@Preview(showBackground = true)
@Composable
private fun OnboardingPagePreview() {
    OnboardingScreen({},{})
}