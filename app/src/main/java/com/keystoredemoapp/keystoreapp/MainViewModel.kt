package com.keystoredemoapp.keystoreapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.keystoredemoapp.keystoreapp.util.HOME_ENCRYPT_DECRYPT_TEXT_MESSAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var keystoreManager = KeystoreManager()
    var uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Init(
        HOME_ENCRYPT_DECRYPT_TEXT_MESSAGE
    ))

    fun encrypt(contentToEncrypt: String){
       viewModelScope.launch{

          uiState.emit(UIState.Encrypt(keystoreManager.encrypt(contentToEncrypt)))
       }
    }

    fun decrypt(){
        viewModelScope.launch{
            uiState.emit(UIState.Decrypt(keystoreManager.decrypt()))
        }
    }
}

sealed class UIState{
    data class Init(var defaultContent: String): UIState()
    data class Encrypt(var encryptedContent: String): UIState()
    data class Decrypt(var decryptedContent: String): UIState()
}