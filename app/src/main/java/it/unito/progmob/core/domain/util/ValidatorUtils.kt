package it.unito.progmob.core.domain.util

object ValidatorUtils {
    /**
     * Checks if the given string is a valid integer.
     *
     * @param value The string to check.
     * @return True if the string is a valid integer, false otherwise.
     */
    fun isInteger(value: String): Boolean {
        return value.isEmpty() || Regex("^\\d+\$").matches(value)
    }
}