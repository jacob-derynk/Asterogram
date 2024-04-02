package dev.jacobderynk.asterogram.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jacobderynk.asterogram.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (uiState.isLoading) {
        Box {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    if (uiState.showUsernameChangedToast) {
        Toast.makeText(LocalContext.current, stringResource(R.string.username_saved), Toast.LENGTH_SHORT).show()
        viewModel.resetUsernameSaved()
    }

    if (showBottomSheet) {
        ChangeUsernameModal(
            onDismiss = { showBottomSheet = false },
            onUsernameChange = viewModel::saveUsername
        )
    }

    ProfileScreenContent(
        posts = uiState.list,
        profileName = uiState.username,
        postsNumber = uiState.list.size.toString(),
        onEditUsernameClick = {
            showBottomSheet = true
        }
    )
}

@Composable
fun ChangeUsernameModal(
    onUsernameChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedUsername by remember { mutableStateOf("") }

    AlertDialog(
        modifier = Modifier.padding(16.dp),
        title = {
            Text(stringResource(R.string.choose_new_username))
        },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedUsername,
                    onValueChange = { selectedUsername = it },
                    label = { Text(stringResource(R.string.username)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                )
            }
        },
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            TextButton(
                onClick = {
                    onUsernameChange.invoke(selectedUsername)
                    onDismiss.invoke()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss.invoke() }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}