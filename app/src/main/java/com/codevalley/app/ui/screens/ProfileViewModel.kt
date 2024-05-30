package com.codevalley.app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
@HiltViewModel
public class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var profile by mutableStateOf<UserResponseDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadProfile(id: Int, token: String) {
        viewModelScope.launch {
            try {
                profile = userRepository.getProfile(id, token)
            } catch (e: Exception) {
                errorMessage = "Erreur lors du chargement du profil. Veuillez réessayer."
            }
        }
    }

}
