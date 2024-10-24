package com.smartherd.aniaux.AuthViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel: ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(email: String, password: String, fullName: String, role: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                val userRef = database.reference.child("users").child(userId)
                val user = User(fullName, role, email)
                userRef.setValue(user).addOnCompleteListener { dbTask ->
                    if (dbTask.isSuccessful) {
                        onResult(true) // Registration successful
                    } else {
                        onResult(false) // Failed to store user info
                    }
                }
            } else {
                onResult(false) // Registration failed
            }
        }
    }
}

data class User(
    val fullName: String,
    val role: String,
    val email: String
)






sealed class AuthState{
    object Authenticated: AuthState()
    object Anauthenticated: AuthState()
    object Loading: AuthState()
    data class Error (val message: String) : AuthState()
}