package com.codevalley.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.R
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = hiltViewModel()) {

    val loginText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("Log in")
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Create your account",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(48.dp))
            TextField(
                value = registerViewModel.username,
                onValueChange = { newText -> registerViewModel.username = newText },
                label = { Text("Enter your username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = registerViewModel.email,
                onValueChange = { newText -> registerViewModel.email = newText },
                label = { Text("Enter your email address") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = registerViewModel.password,
                onValueChange = { newText -> registerViewModel.password = newText },
                label = { Text("Enter your password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = registerViewModel.samePassword,
                onValueChange = { newText -> registerViewModel.samePassword = newText },
                label = { Text("Confirm your password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = registerViewModel.errorMessage,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { registerViewModel.register(navController) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                Text(text = "Register")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    text = " Or with ",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { registerViewModel.errorMessage = "Google authentication is not available" },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Register in with Google",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    onClick = { registerViewModel.errorMessage = "Microsoft authentication is not available"  },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.microsoft_icon),
                        contentDescription = "Register in with Microsoft",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    onClick = { registerViewModel.errorMessage = "Apple authentication is not available"  },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apple_icon),
                        contentDescription = "Register with Apple",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? Let's ",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                ClickableText(
                    text = loginText,
                    onClick = { _ -> navController.navigate(ScreenName.Login.toString()) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    CodeValleyTheme {
        RegisterScreen(rememberNavController())
    }
}
