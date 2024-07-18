package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.codevalley.app.model.UserResponseDTO

class SharedViewModel : ViewModel() {
    var currentUser: UserResponseDTO? = null
}
