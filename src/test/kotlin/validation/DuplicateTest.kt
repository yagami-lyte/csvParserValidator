package validation

import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DuplicateTest {

    @Test
    fun shouldReturnEmptyMapForNoDuplicates() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 4}]"
        val jsonArray = JSONArray(jsonString)

        val actual = duplicateValidation.checkDuplicates(jsonArray)

        assertTrue(actual.isEmpty)
    }

    @Test
    fun shouldReturnDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3}]"
        val jsonArray = JSONArray(jsonString)
        val expected = JSONArray("[{2 : Row Duplicated From 1}]")

        val actual = duplicateValidation.checkDuplicates(jsonArray)

        assertEquals(expected.toList(), actual.toList())
    }

    @Test
    fun shouldReturnAllDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3} ]"
        val jsonArray = JSONArray(jsonString)
        val expected = JSONArray("[{2 : Row Duplicated From 1}, {3 : Row Duplicated From 1}]")

        val actual = duplicateValidation.checkDuplicates(jsonArray)
        2
        assertEquals(expected.toList(), actual.toList())
    }

    @Test
    fun shouldReturnDifferentDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3},{a : 2, b : 2, c : 2}, {a : 2, b : 2, c : 2}]"
        val jsonArray = JSONArray(jsonString)
        val expected = JSONArray("[{2 : Row Duplicated From 1}, {4 : Row Duplicated From 3}]")

        val actual = duplicateValidation.checkDuplicates(jsonArray)

        assertEquals(expected.toList(), actual.toList())
    }
}