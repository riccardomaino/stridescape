import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R

@Composable
fun ProfileDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Confirm Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.dialog_ok_btn))
            }
        },
        dismissButton = {

        },
    )
}