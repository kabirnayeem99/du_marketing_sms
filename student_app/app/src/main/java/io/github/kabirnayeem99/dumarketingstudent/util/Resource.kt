package io.github.kabirnayeem99.dumarketingstudent.util

/***
 * A sealed class, in C#, is a class that cannot be inherited by any
 * class but can be instantiated. The design intent of a sealed class
 * is to indicate that the class is specialized and there is no need
 * to extend it to provide any additional functionality through inheritance
 * to override its behavior.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}