package com.smartherd.aniaux.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.smartherd.aniaux.AuthViewModel.AuthViewModel
//


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.TextField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation


import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.smartherd.aniaux.R
import com.smartherd.aniaux.components.CustomCheckbox
import com.smartherd.aniaux.components.DynamicSelectTextField
import com.smartherd.aniaux.components.HeadingTextComponents
import com.smartherd.aniaux.components.MyTextFiedComponent
import com.smartherd.aniaux.components.PasswordTextField

import com.smartherd.aniaux.ui.theme.ANIAUXTheme


@Composable
fun Login(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAgreeChecked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(R.drawable.login_image),
                contentDescription = "Login Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth()
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            HeadingTextComponents(value = "Sign In")
            Spacer(modifier = Modifier.height(10.dp))

            // Email input
            MyTextFiedComponent(
                labelValue = "Email Address",
                icon = Icons.Default.Email,
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Forgot Password
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier.clickable {
                        navController.navigate("forgotPassword")
                    },
                    color = Color.Blue
                )
            }

            // Password input
            MyPasswordTextField(
                text = password,
               onTextChange = {password=it}
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Submit Button
            FilledTonalButton(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        loginUser(
                            email, password, navController, authViewModel
                        ) { error -> errorMessage = error }
                    } else {
                        errorMessage = "Please fill out all fields."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Submit")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Error message display
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }

            // Redirect to Sign Up
            Text(
                text = "Have No account? SignUp",
                color = Color.Blue,
                modifier = Modifier
                    .clickable {
                        navController.navigate("signUp")
                    }
                    .padding(top = 8.dp)
            )
        }

    }


}
@Composable
fun MyTextFiedComponent(
    labelValue: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange(newText)  // Pass new value back to the caller
        },
        label = { Text(labelValue) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) }
    )
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

fun loginUser(
    email: String,
    password: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // If login is successful, check the user role from Firebase Database
                val userId = auth.currentUser?.uid
                val databaseRef =
                    FirebaseDatabase.getInstance().getReference("users").child(userId!!)

                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val role = snapshot.child("role").getValue(String::class.java)

                        when (role) {
                            "Seller" -> {
                                // Navigate to Admin's dashboard
                                navController.navigate("BottomAppBar")
                            }

                            "Buyer" -> {
                                // Navigate to User's home screen
                                navController.navigate("BottomAppBar")
                            }

                            else -> {
                                onError("Role not defined.")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
        }
}

