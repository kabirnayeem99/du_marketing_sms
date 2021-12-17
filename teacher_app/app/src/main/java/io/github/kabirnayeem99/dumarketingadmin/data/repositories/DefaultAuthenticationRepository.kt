package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import com.google.firebase.auth.FirebaseAuth
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
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

    override fun getAuthenticationStatus() = channelFlow {

        try {
            val isLoggedIn = auth.currentUser != null
            if (isLoggedIn) send(true)
            else send(false)
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            send(false)
        }

    }

    override fun logOut() {
        auth.signOut()
    }


}