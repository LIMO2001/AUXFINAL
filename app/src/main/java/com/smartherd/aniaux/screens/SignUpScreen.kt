package com.smartherd.aniaux.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.smartherd.aniaux.AuthViewModel.AuthViewModel
import com.smartherd.aniaux.R
import com.smartherd.aniaux.components.CustomCheckbox
import com.smartherd.aniaux.components.DynamicSelectTextField
import com.smartherd.aniaux.components.HeadingTextComponents
import com.smartherd.aniaux.components.MyTextFiedComponent
import com.smartherd.aniaux.components.PasswordTextField

import com.smartherd.aniaux.ui.theme.ANIAUXTheme
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignUpScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var isAgreeChecked by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Select Role") }
    var registrationError by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.login_image),
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth()
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            HeadingTextComponents(value = "Sign UP")
            Spacer(modifier = Modifier.height(10.dp))

            // Email Input
            MyTextFiedComponent(labelValue = "Email Address", icon = Icons.Default.Email) {
                email = it
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Full Name Input
            MyTextFiedComponent(labelValue = "Full Name", icon = Icons.Default.AccountCircle) {
                fullName = it
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Password Input
            MyPasswordTextField(text = password, onTextChange = { password = it })
            Spacer(modifier = Modifier.height(10.dp))

            // Confirm Password Input
            MyPasswordTextField(text = confirmPassword, onTextChange = { confirmPassword = it })
            Spacer(modifier = Modifier.height(20.dp))

            // Role Selection
            DynamicSelectTextField(
                selectedValue = selectedRole,
                options = listOf("Buyer", "Seller"),
                label = "Role",
                onValueChangedEvent = { newRole -> selectedRole = newRole },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))

            // Terms and Conditions Checkbox
            CustomCheckbox(
                isChecked = isAgreeChecked,
                onCheckedChange = { isAgreeChecked = it },
                label = "I agree to the terms and conditions"
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Error Message Display (if any)
            if (registrationError.isNotEmpty()) {
                Text(text = registrationError, color = Color.Red)
            }
            @Composable
            fun MyPasswordTextField(
                text: String,
                onTextChange: (String) -> Unit
            ) {
                var password by remember { mutableStateOf(text) }

                TextField(
                    value = password,
                    onValueChange = { newPassword ->
                        password = newPassword
                        onTextChange(newPassword)  // Pass new password back to the caller
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) }
                )
            }

            // Submit Button
            FilledTonalButton(
                onClick = {
                    if (password != confirmPassword) {
                        registrationError = "Passwords do not match"
                    } else if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                        authViewModel.registerUser(
                            email, password, fullName, selectedRole
                        ) { success ->
                            if (success) {
                                navController.navigate("BottomAppBar")
                            } else {
                                registrationError = "Registration failed. Please try again."
                            }
                        }
                    } else {
                        registrationError = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(50.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                enabled = isAgreeChecked
            ) {
                Text(text = "Sign Up", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Redirect to Login
            Text(
                text = "Already have an account? Login",
                color = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate("Login") })
                    .padding(top = 8.dp, bottom = 30.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
