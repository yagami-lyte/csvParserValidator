package validation

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypeValidationTest {

    @Test
    fun shouldBeAbleToCheckIfValueIsNumeric() {
        val typeValidation = TypeValidation()
        val value = "1234"

        val actual = typeValidation.isNumeric(value)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsAlphabetic() {
        val typeValidation = TypeValidation()
        val value = "shikha"

        val actual = typeValidation.isAlphabetic(value)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsAlphaNumeric() {
        val typeValidation = TypeValidation()
        val value = "shikha123"

        val actual = typeValidation.isAlphaNumeric(value)

        assertTrue(actual)
    }


}