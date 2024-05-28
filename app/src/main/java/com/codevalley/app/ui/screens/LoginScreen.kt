package com.codevalley.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoginScreen() {
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val changePasswordText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("Forgot your password?")
        }
        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.example.com",
            start = 0,
            end = 10
        )
    }
    val registerText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("Register")
        }
        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.example.com",
            start = 0,
            end = 10
        )
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
                text = "Hello Again!",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sign in to your account",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(48.dp))
            TextField(value = emailAddress,
                onValueChange = { newText -> emailAddress = newText },
                label = { Text("Enter your email address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = password,
                onValueChange = { newText -> password = newText },
                label = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                ClickableText(
                    text = changePasswordText,
                    onClick = { offset ->
                        changePasswordText.getStringAnnotations(
                            tag = "URL",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let { annotation ->
                                // Handle the click
                                println("Clicked URL: ${annotation.item}")
                            }
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                Text(text = "Log in")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "- - - - - - - - - - - - - - Or with - - - - - - - - - - - - - -",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { /* TODO: Handle button click */ }) {
                    Text(text = "Google")
                }
                Button(onClick = { /* TODO: Handle button click */ }) {
                    Text(text = "Microsoft")
                }
                Button(onClick = { /* TODO: Handle button click */ }) {
                    Text(text = "Apple")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? Let's ",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                ClickableText(
                    text = registerText,
                    onClick = { offset ->
                        registerText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                // Handle the click
                                println("Clicked URL: ${annotation.item}")
                            }
                    }
                )
            }
        }
    }
}
