package validation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LengthValidationTest {

    @Test
    fun shouldBeAbleToValidateFixedLength() {
        val lengthValidation = LengthValidation()

        val actual = lengthValidation.lengthCheck("gftgd", 5)

        assertTrue(actual)
    }
}