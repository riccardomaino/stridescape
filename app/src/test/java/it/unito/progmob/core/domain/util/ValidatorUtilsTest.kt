package it.unito.progmob.core.domain.util

import org.junit.Assert.*
import org.junit.Test

class ValidatorUtilsTest{
        @Test
        fun `is integer, should return true`() {
            assertTrue(ValidatorUtils.isInteger("123"))
        }

        @Test
        fun `is integer, should return false`(){
            assertFalse(ValidatorUtils.isInteger("123.4"))
        }
}