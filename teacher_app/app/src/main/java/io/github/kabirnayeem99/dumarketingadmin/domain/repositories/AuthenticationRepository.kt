package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.flow.Flow


interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Resource<String>
    suspend fun register(email: String, password: String): Resource<String>
    fun logOut()
    fun getAuthenticationStatus(): Flow<Boolean>
}