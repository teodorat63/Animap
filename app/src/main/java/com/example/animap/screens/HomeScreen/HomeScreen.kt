package com.example.animap.screens.HomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun RegistrationScreen() {
    //val auth = FirebaseAuth.getInstance()
    //val context = LocalContext.current

    // Registration form state
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    //val photoUri = remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") }
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        // Photo picker
        Button(onClick = { /* Open photo picker */ }) {
            Text("Select Photo")
        }

//        Button(onClick = {
//            auth.createUserWithEmailAndPassword(email.value, password.value)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val user = auth.currentUser
//                        val storageRef = FirebaseStorage.getInstance().reference.child("user_photos/${user?.uid}.jpg")
//                        photoUri.value?.let { uri ->
//                            storageRef.putFile(uri).addOnSuccessListener {
//                                storageRef.downloadUrl.addOnSuccessListener { url ->
//                                    val userData = hashMapOf(
//                                        "fullName" to fullName.value,
//                                        "phone" to phone.value,
//                                        "photoUrl" to url.toString()
//                                    )
//                                    FirebaseFirestore.getInstance().collection("users").document(user!!.uid).set(userData)
//                                }
//                            }
//                        }
//                    } else {
//                        Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }) {
//            Text("Register")
//        }


    }
}
