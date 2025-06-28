package com.example.cooksy.data.repository

import android.content.Context
import com.example.cooksy.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import androidx.core.content.edit

class AuthRepository {

        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val firestore = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser?
            get() = auth.currentUser

        fun isUserLoggedIn(): Boolean = currentUser != null

        fun isEmailVerified(): Boolean = currentUser?.isEmailVerified == true

        fun logout() {
            auth.signOut()
        }

        fun setKeepSession(value: Boolean) {
            val prefs = Firebase.auth.app.applicationContext
                .getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
            prefs.edit { putBoolean("keep_session", value) }
        }

        fun isKeepSession(): Boolean {
            val prefs = Firebase.auth.app.applicationContext
                .getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
            return prefs.getBoolean("keep_session", false)
        }


        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun login(email: String, password: String): Result<Unit> =
            suspendCancellableCoroutine { cont ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (isEmailVerified()) {
                                cont.resume(Result.success(Unit), null)
                            } else {
                                logout()
                                cont.resume(Result.failure(Exception("Verifica tu correo electrónico")), null)
                            }
                        } else {
                            cont.resume(Result.failure(task.exception ?: Exception("Error desconocido")), null)
                        }
                    }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun register(fullName: String, email: String, password: String, avatar: String): Result<Unit> =
            suspendCancellableCoroutine { cont ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = currentUser
                            user?.sendEmailVerification()

                            val userData = User(
                                uid = user?.uid ?: "",
                                fullName = fullName,
                                email = email,
                                avatar = avatar
                            )

                            firestore.collection("users")
                                .document(user?.uid ?: "")
                                .set(userData)
                                .addOnSuccessListener {
                                    cont.resume(Result.success(Unit), null)
                                }
                                .addOnFailureListener {
                                    cont.resume(Result.failure(it), null)
                                }
                        } else {
                            cont.resume(Result.failure(task.exception ?: Exception("No se pudo registrar")), null)
                        }
                    }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun sendResetPassword(email: String): Result<Unit> =
            suspendCancellableCoroutine { cont ->
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cont.resume(Result.success(Unit), null)
                        } else {
                            cont.resume(Result.failure(task.exception ?: Exception("No se pudo enviar el email")), null)
                        }
                    }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun getUserProfile(): Result<User> = suspendCancellableCoroutine { cont ->
            val uid = currentUser?.uid ?: return@suspendCancellableCoroutine cont.resume(
                Result.failure(Exception("Usuario no logueado")), null
            )

            firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        cont.resume(Result.success(user), null)
                    } else {
                        cont.resume(Result.failure(Exception("No se encontró el perfil")), null)
                    }
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it), null)
                }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun updateUserProfile(fullName: String?, avatar: String?): Result<Unit> =
            suspendCancellableCoroutine { cont ->
                val uid = currentUser?.uid ?: return@suspendCancellableCoroutine cont.resume(
                    Result.failure(Exception("Usuario no logueado")), null
                )

                val updates = mutableMapOf<String, Any>()
                fullName?.let { updates["fullName"] = it }
                avatar?.let { updates["avatar"] = it }

                firestore.collection("users").document(uid)
                    .update(updates)
                    .addOnSuccessListener {
                        cont.resume(Result.success(Unit), null)
                    }
                    .addOnFailureListener {
                        cont.resume(Result.failure(it), null)
                    }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
            suspendCancellableCoroutine { cont ->
                val user = currentUser
                val email = user?.email ?: return@suspendCancellableCoroutine cont.resume(
                    Result.failure(Exception("Usuario no válido")), null
                )

                val credential = EmailAuthProvider.getCredential(email, currentPassword)

                user.reauthenticate(credential)
                    .addOnSuccessListener {
                        user.updatePassword(newPassword)
                            .addOnSuccessListener {
                                cont.resume(Result.success(Unit), null)
                            }
                            .addOnFailureListener {
                                cont.resume(Result.failure(it), null)
                            }
                    }
                    .addOnFailureListener {
                        cont.resume(Result.failure(it), null)
                    }
            }


    }
