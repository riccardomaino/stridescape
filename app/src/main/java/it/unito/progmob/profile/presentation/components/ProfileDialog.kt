import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.ui.theme.extralargeRadius

@Composable
fun ProfileDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
               Icon(
                   Icons.Filled.CheckCircle,
                   contentDescription = "",
                   tint = MaterialTheme.colorScheme.primary
               )
//            Image(
//                painter = painterResource(id = R.drawable.dialog_confirm),
//                contentDescription = " "
//            )
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
            Button(
                onClick = onConfirmation,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(extralargeRadius)
            ) {
                Text(
                    text = stringResource(R.string.dialog_ok_btn),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    )
}