package validation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PrependingZeroesValidationTest {

    @Test
    fun shouldReqturnTrueForPrependingZeroesValues(){
        val prependingZeroesValidation = PrependingZeroesValidation()
        val value = "0123"

        val expected = prependingZeroesValidation.checkPrePendingZeros(value)

        assertTrue(expected)
    }

    @Test
    fun shouldFalseTrueIfValueDoesNotHavePrependingZeroes(){
        val prependingZeroesValidation = PrependingZeroesValidation()
        val value = "123"

        val expected = prependingZeroesValidation.checkPrePendingZeros(value)

        assertFalse(expected)
    }



}