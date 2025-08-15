package com.example.cooksy.data.repository

import android.content.Context
import androidx.compose.ui.geometry.isEmpty
import com.example.cooksy.data.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import androidx.core.content.edit // For SharedPreferences edit lambda
import com.example.cooksy.data.model.supermarket.SupermarketItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.SetOptions
import kotlin.coroutines.resume // For resuming coroutines

private const val PREFS_NAME = "profile_prefs"
private const val KEY_AVATAR = "avatar_preference"
private const val USERS_COLLECTION = "users" // Consistent collection name
private const val SUPERMARKET_ITEMS_COLLECTION = "supermarket_items" // Collection for supermarket items

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val applicationContext: Context = Firebase.auth.app.applicationContext


    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun isUserLoggedIn(): Boolean = currentUser != null

    fun isEmailVerified(): Boolean = currentUser?.isEmailVerified == true

    fun logout() {
        auth.signOut()
        // Clear local avatar preference on logout if desired
        // clearAvatarPreference()
    }

    fun setKeepSession(value: Boolean) {
        val prefs = applicationContext
            .getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        prefs.edit { putBoolean("keep_session", value) }
    }

    fun isKeepSession(): Boolean {
        val prefs = applicationContext
            .getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("keep_session", false)
    }

    // --- Avatar Preferences (SharedPreferences) ---
    fun saveAvatarPreference(avatarName: String) {
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_AVATAR, avatarName) }
    }

    fun getAvatarPreference(): String? {
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AVATAR, null) // Return null if no preference set
    }

    // --- End Avatar Preferences ---

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun login(email: String, password: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (isEmailVerified()) {
                            cont.resume(Result.success(Unit))
                        } else {
                            logout()
                            cont.resume(Result.failure(Exception("Verifica tu correo electrónico")))
                        }
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("Error desconocido")))
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

                        firestore.collection(USERS_COLLECTION)
                            .document(user?.uid ?: "")
                            .set(userData)
                            .addOnSuccessListener {
                                cont.resume(Result.success(Unit))
                            }
                            .addOnFailureListener {
                                cont.resume(Result.failure(it))
                            }
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("No se pudo registrar")))
                    }
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun sendResetPassword(email: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(Result.success(Unit))
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("No se pudo enviar el email")))
                    }
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserProfile(): Result<User> = suspendCancellableCoroutine { cont ->
        val uid = currentUser?.uid ?: return@suspendCancellableCoroutine cont.resume(
            Result.failure(Exception("Usuario no logueado"))
        )

        firestore.collection(USERS_COLLECTION).document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    cont.resume(Result.success(user))
                } else {
                    cont.resume(Result.failure(Exception("No se encontró el perfil")))
                }
            }
            .addOnFailureListener {
                cont.resume(Result.failure(it))
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun updateUserFullName(fullName: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            val uid = currentUser?.uid ?: return@suspendCancellableCoroutine cont.resume(
                Result.failure(Exception("Usuario no logueado"))
            )

            val updates = mutableMapOf<String, Any>()
            updates["fullName"] = fullName

            firestore.collection(USERS_COLLECTION).document(uid)
                .update(updates)
                .addOnSuccessListener {
                    cont.resume(Result.success(Unit))
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            val user = currentUser
            val email = user?.email ?: return@suspendCancellableCoroutine cont.resume(
                Result.failure(Exception("Usuario no válido"))
            )

            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            cont.resume(Result.success(Unit))
                        }
                        .addOnFailureListener {
                            cont.resume(Result.failure(it))
                        }
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

    // --- Supermarket List Functions ---

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun addSupermarketItem(item: SupermarketItem): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            if (item.userId.isEmpty()) {
                cont.resume(Result.failure(IllegalArgumentException("UserID no puede estar vacío en SupermarketItem")))
                return@suspendCancellableCoroutine
            }
            firestore.collection(SUPERMARKET_ITEMS_COLLECTION)
                .document(item.id) // Use SupermarketItem's own ID
                .set(item)
                .addOnSuccessListener {
                    cont.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getSupermarketList(userId: String): Result<List<SupermarketItem>> =
        suspendCancellableCoroutine { cont ->
            if (userId.isEmpty()) {
                cont.resume(Result.failure(IllegalArgumentException("UserID no puede estar vacío para obtener la lista")))
                return@suspendCancellableCoroutine
            }
            firestore.collection(SUPERMARKET_ITEMS_COLLECTION)
                .whereEqualTo("userId", userId) // Filter by userId
                .orderBy("dateAdded") // Optional: order by dateAdded or name
                .get()
                .addOnSuccessListener { snapshot ->
                    try {
                        val items = snapshot.toObjects(SupermarketItem::class.java)
                        cont.resume(Result.success(items))
                    } catch (e: Exception) {
                        cont.resume(Result.failure(e))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun updateSupermarketItem(item: SupermarketItem): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            if (item.userId.isEmpty() || item.id.isEmpty()) {
                cont.resume(Result.failure(IllegalArgumentException("UserID e ItemID no pueden estar vacíos en SupermarketItem para actualizar")))
                return@suspendCancellableCoroutine
            }
            firestore.collection(SUPERMARKET_ITEMS_COLLECTION)
                .document(item.id)
                .set(item, SetOptions.merge()) // Use merge to update fields or create if not exists (though it should exist for update)
                .addOnSuccessListener {
                    cont.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun deleteSupermarketItem(itemId: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            if (itemId.isEmpty()) {
                cont.resume(Result.failure(IllegalArgumentException("ItemID no puede estar vacío para eliminar")))
                return@suspendCancellableCoroutine
            }
            firestore.collection(SUPERMARKET_ITEMS_COLLECTION)
                .document(itemId)
                .delete()
                .addOnSuccessListener {
                    cont.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }
}

