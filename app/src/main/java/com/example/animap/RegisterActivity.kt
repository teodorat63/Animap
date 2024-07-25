package com.example.animap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            var photoUri by remember { mutableStateOf<Uri?>(null) }
            val photoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                photoUri = uri
            }

            RegistrationScreen(
                onRegisterClick = { email, password, firstName, lastName, phone, photo ->
                    registerUser(email, password, firstName, lastName, phone, photo)
                },
                onPhotoSelectClick = {
                    photoPickerLauncher.launch("image/*")
                },
                photoUri = photoUri
            )
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String,
        photoUri: Uri?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val storageRef = FirebaseStorage.getInstance().reference.child("user_photos/${user?.uid}.jpg")
                    photoUri?.let { uri ->
                        storageRef.putFile(uri).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { url ->
                                val userData = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "phone" to phone,
                                    "photoUrl" to url.toString()
                                )
                                firestore.collection("users").document(user!!.uid).set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(baseContext, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        // Navigate to login screen or main activity
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(baseContext, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

@Composable
fun RegistrationScreen(
    onRegisterClick: (String, String, String, String, String, Uri?) -> Unit,
    onPhotoSelectClick: () -> Unit,
    photoUri: Uri?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onPhotoSelectClick) {
            Text("Select Photo")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRegisterClick(email, password, firstName, lastName, phone, photoUri) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
