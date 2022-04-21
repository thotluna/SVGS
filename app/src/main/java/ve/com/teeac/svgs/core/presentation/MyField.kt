package ve.com.teeac.svgs.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun MyField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    validation: SnapshotStateList<String>,
    validate: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        isError = validate && validation.isNotEmpty(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 1f)
        ),
        shape = RoundedCornerShape(36.dp)
    )

    if (validate && validation.isNotEmpty()) {
        validation.forEach {
            Text(text = it, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
fun FieldText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    testTag: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    validate: Boolean = false,
    validation: List<String> = listOf(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .testTag("${testTag}_Field"),
            label = { Text(text = label) },
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = validate,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 1f)
            ),
            shape = RoundedCornerShape(36.dp)
        )

        if (validate && validation.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .testTag("${testTag}_Errors"),
            ) {
                validation.forEach {
                    Text(text = it, style = MaterialTheme.typography.caption)
                }
            }
        }
    }
}
