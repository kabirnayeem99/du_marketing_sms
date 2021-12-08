package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import com.google.firebase.auth.FirebaseAuth
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultAuthenticationRepository @Inject constructor(
    val auth: FirebaseAuth
) : AuthenticationRepository {


    override suspend fun login(email: String, password: String): Resource<String> {
        return try {
            val logInTask = auth.signInWithEmailAndPassword(email, password).await()
            if (logInTask.user == null) Resource.Error("Could not log you in.")
            else Resource.Success(logInTask.user?.uid.toString())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not log you in.")
        }

    }

    override suspend fun register(email: String, password: String): Resource<String> {
        return try {
            val registerTask = auth.createUserWithEmailAndPassword(email, password).await()
            if (registerTask.user == null) Resource.Error("Could not log you in.")
            else Resource.Success(registerTask.user?.uid.toString())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not register your account.")
        }
    }

    override fun getAuthenticationStatus() = flow<Boolean> {
        emit(auth.currentUser != null)
    }


}