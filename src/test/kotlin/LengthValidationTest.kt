import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LengthValidationTest {

    @Test
    fun shouldBeAbleToValidateMinLength() {
        val lengthValidation = LengthValidation()

        val actual = lengthValidation.minLength("gftgd", 4)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToValidateMaxLength() {
        val lengthValidation = LengthValidation()

        val actual = lengthValidation.maxLength("gftgd", 7)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToValidateFixedLength() {
        val lengthValidation = LengthValidation()

        val actual = lengthValidation.fixedLenght("gftgd", 5)

        assertTrue(actual)
    }
}