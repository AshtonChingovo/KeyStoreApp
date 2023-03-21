package com.keystoredemoapp.keystoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.keystoredemoapp.keystoreapp.ui.theme.KeystoreAppTheme
import com.keystoredemoapp.keystoreapp.ui.theme.*
import com.keystoredemoapp.keystoreapp.util.*

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(color = Color.Transparent)
            systemUiController.systemBarsDarkContentEnabled = false

            WindowCompat.setDecorFitsSystemWindows(window, false)

            var uiState = viewModel.uiState.collectAsStateWithLifecycle()

            KeystoreAppTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                            .background(color = AppMainPurple)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Title(Modifier.padding(16.dp))
                        Spacer(modifier = Modifier.height(32.dp))
                        Image(uiState.value)
                        Spacer(modifier = Modifier.height(32.dp))
                        DecryptionUI(viewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                        EncryptedContent(uiState.value)
                    }
                }
            }
        }

    }

    @Composable
    fun Title(modifier: Modifier){
        Column(modifier = modifier) {
            Text(HOME_TOP_LABEL, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(HOME_MESSAGE,
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun Image(uiState: UIState){

        var imageResourceId: Int = when(uiState){
            is UIState.Init -> R.drawable.keystore_app
            is UIState.Encrypt -> R.drawable.encrypt
            is UIState.Decrypt -> R.drawable.decrypt
        }

        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
        )
    }

    @Composable
    fun DecryptionUI(viewModel: MainViewModel){

        var contentToEncrypt = remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(HOME_TEXT_FIELD_LABEL,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White,)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = contentToEncrypt.value,
                onValueChange = {
                    contentToEncrypt.value = it
                },
                placeholder = {
                    Text(text = HOME_TEXT_FIELD_HINT)
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.padding(start = 16.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ){
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if(contentToEncrypt.value.isNotEmpty())
                            viewModel.encrypt(contentToEncrypt.value)
                    }
                ) {
                    Text(
                        HOME_ENCRYPT_BUTTON_LABEL,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
                Divider(
                    Modifier
                        .fillMaxHeight()
                        .padding(vertical = 12.dp)
                        .alpha(0.4f)
                        .width(1.dp),
                    color = Color.White
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.decrypt()
                    }
                ) {
                    Text(
                        HOME_DECRYPT_BUTTON_LABEL,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun EncryptedContent(uiState: UIState){

        var content = when(uiState){
            is UIState.Init -> uiState.defaultContent
            is UIState.Encrypt -> uiState.encryptedContent
            is UIState.Decrypt -> uiState.decryptedContent
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                .padding(horizontal = 16.dp)
        ) {
            Text(
                HOME_ENCRYPT_DECRYPT_LABEL,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

