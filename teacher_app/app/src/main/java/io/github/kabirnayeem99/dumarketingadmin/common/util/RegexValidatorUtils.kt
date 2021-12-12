package io.github.kabirnayeem99.dumarketingadmin.common.util

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexValidatorUtils {
    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    /**
     * This method validates email
     *
     * @param emailStr  email in String value
     *
     * @return [Boolean] value whether this email is valid or not
     */
    fun validateEmail(emailStr: String): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }
}