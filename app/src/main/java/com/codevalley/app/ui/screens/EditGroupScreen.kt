package com.codevalley.app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.model.GroupDTO
import com.codevalley.app.ui.components.getFileFromUri
import com.codevalley.app.ui.viewmodel.EditGroupViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun EditGroupScreen(
    navController: NavController,
    groupId: Int,
    editGroupViewModel: EditGroupViewModel = hiltViewModel()
) {
    val groupDetails by editGroupViewModel.groupDetails.collectAsState()
    val loading by editGroupViewModel.loading.collectAsState()
    val error by editGroupViewModel.error.collectAsState()
    val context = LocalContext.current

    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var filePart by remember { mutableStateOf<MultipartBody.Part?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val filePath = getFileFromUri(context, it)
            val file = File(filePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        }
    }


    LaunchedEffect(groupId) {
        editGroupViewModel.loadGroupDetails(groupId)
    }

    LaunchedEffect(groupDetails) {
        groupDetails?.let {
            groupName = it.name
            groupDescription = it.description ?: ""
            isPublic = it.isPublic
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Group") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    groupDetails?.let { group ->
                        TextField(
                            value = groupName,
                            onValueChange = { groupName = it },
                            label = { Text("Group Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = groupDescription,
                            onValueChange = { groupDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isPublic,
                                onCheckedChange = { isPublic = it }
                            )
                            Text("Public")
                        }
                        Button(onClick = {
                            launcher.launch("image/*")
                        }) {
                            Text("Select File")
                        }
                        Button(onClick = {
                            val groupDTO = GroupDTO(
                                name = groupName,
                                description = groupDescription,
                                isPublic = isPublic
                            )
                            editGroupViewModel.updateGroup(groupId, groupDTO, filePart)
                            Toast.makeText(context, "Group updated successfully", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }) {
                            Text("Save Changes")
                        }
                    }
                }

                error?.let {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                        )
                    }
                }
            }
        }
    )
}
