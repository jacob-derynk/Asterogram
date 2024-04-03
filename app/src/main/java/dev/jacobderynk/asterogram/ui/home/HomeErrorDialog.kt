package dev.jacobderynk.asterogram.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jacobderynk.asterogram.R
import dev.jacobderynk.asterogram.data.model.CommunicationError
import dev.jacobderynk.asterogram.data.model.ErrorResponse
import java.io.IOException
import java.lang.Exception

@Composable
fun HomeErrorDialog(
    error: HomeViewModel.HomeErrorState,
    onTryAgain: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = stringResource(R.string.generic_error),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            HomeErrorDialogTitle(error)
        },
        text = {
            HomeErrorDialogContent(error)
        },
        onDismissRequest = {
            onDismiss.invoke()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTryAgain.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text(stringResource(R.string.try_again))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Composable
private fun HomeErrorDialogTitle(error: HomeViewModel.HomeErrorState) {
    when (error) {
        is HomeViewModel.HomeErrorState.ConnectionError -> {
            Text(text = stringResource(R.string.network_error))
        }

        is HomeViewModel.HomeErrorState.ApiCommunicationError -> {
            Text(text = stringResource(R.string.fetching_error))
        }

        is HomeViewModel.HomeErrorState.GenericError -> {
            Text(text = stringResource(R.string.generic_error))
        }
    }
}

@Composable
private fun HomeErrorDialogContent(error: HomeViewModel.HomeErrorState) {
    when (error) {
        is HomeViewModel.HomeErrorState.ConnectionError -> {
            Column {
                Text(text = stringResource(R.string.network_error_content))
                if (error.throwable != null && error.throwable.localizedMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(text = error.throwable.localizedMessage!!, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        is HomeViewModel.HomeErrorState.ApiCommunicationError -> {
            Column {
                Text(text = stringResource(R.string.generic_error_content))
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${error.communicationError.responseCode} - ${error.communicationError.errorResponse.message}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        is HomeViewModel.HomeErrorState.GenericError -> {
            Column {
                Text(text = stringResource(R.string.generic_error_content))
                if (error.throwable != null && error.throwable.localizedMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(text = error.throwable.localizedMessage!!, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeErrorDialogGenericErrPreview() {
    HomeErrorDialog(HomeViewModel.HomeErrorState.GenericError(Exception("This is a generic nothing-saying error")), {}) {}
}

@Preview(showBackground = true)
@Composable
private fun HomeErrorDialogConnectionErrPreview() {
    HomeErrorDialog(HomeViewModel.HomeErrorState.ConnectionError(IOException("This is a very bad connectivity error")), {}) {}
}

@Preview(showBackground = true)
@Composable
private fun HomeErrorDialogApiErrPreview() {
    HomeErrorDialog(
        error = HomeViewModel.HomeErrorState.ApiCommunicationError(
            CommunicationError(
                500,
                ErrorResponse(
                    code = "query.compiler.malformed",
                    error = true,
                    message = "Could not parse SoQL query \"select * where string_column > 42\"",
                    data = ErrorResponse.CommunicationErrorData(query = "select * where string_column > 42"),
                )
            )
        ),
        {}
    ) {}
}

