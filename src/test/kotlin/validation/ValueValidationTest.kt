package validation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValueValidationTest {

    @Test
    fun shouldReturnTrueIfValueIsPresent() {
        val valueValidation = ValueValidation()
        val allowedValues = listOf<String>("IND", "USA", "AUS")
        val value = "USA"

        val actual = valueValidation.valueCheck(allowedValues, value)

        assertTrue(actual)
    }

    @Test
    fun shouldReturnFalseIfValueIsNotPresent() {
        val valueValidation = ValueValidation()
        val allowedValues = listOf<String>("IND", "USA", "AUS")
        val value = "CHINA"

        val actual = valueValidation.valueCheck(allowedValues, value)

        assertFalse(actual)
    }
}