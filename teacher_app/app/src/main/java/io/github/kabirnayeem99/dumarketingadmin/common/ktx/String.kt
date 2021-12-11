package io.github.kabirnayeem99.dumarketingadmin.common.ktx

import android.util.Patterns.*
import io.github.kabirnayeem99.dumarketingadmin.BuildConfig
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Checks whether the string is a phone number or not
 */
fun String.isPhoneNumber(): Boolean {
    return PHONE
        .matcher(this)
        .matches()
}

/**
 * Checks whether the string is an email address or not
 */
fun String.isEmailAddress(): Boolean {
    return EMAIL_ADDRESS
        .matcher(this)
        .matches()
}

/**
 * Checks if the password is secure or non-secure
 *
 * @return if it is secure or not
 */
fun String.isPasswordSecure(): Boolean {

    if (BuildConfig.DEBUG) return true

    val pattern: Pattern

    val passwordPattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

    pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(this)

    return matcher.matches()
}


/**
 * Checks whether the name is correct or not,
 * the name is not correct if it contains something like '@'
 *
 * @return if the name is valid or not
 */
fun String.isName(): Boolean {

    val pattern: Pattern

    val namePattern = "^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)"

    pattern = Pattern.compile(namePattern)
    val matcher: Matcher = pattern.matcher(this)

    return matcher.matches()
}