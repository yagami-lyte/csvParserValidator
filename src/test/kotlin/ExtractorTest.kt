import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExtractorTest {


    @Test
    fun shouldBeAbleToExtractThePathForAGetRequest() {

        val extractor = Extractor()
        val request = "GET / HTTP/1.0" + "\n\n"
        val expectedPath = "/"

        val actualPath = extractor.extractPath(request)
        println(actualPath)

        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun shouldBeAbleToExtractThePathForAPostRequest() {
        val extractor = Extractor()
        val request = """POST /add-meta-data HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 266""".trimMargin() + "\r\n\r\n"
        val expectedPath = "/add-meta-data"

        val actualPath = extractor.extractPath(request)
        println(actualPath)
        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun shouldBeAbleToExtractTheContentLength() {
        val extractor = Extractor()
        val request = """POST /add-meta-data HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 266""".trimMargin() + "\r\n\r\n"
        val expectedContentLength = 266

        val actualContentLength = extractor.extractContentLength(request)

        assertEquals(expectedContentLength, actualContentLength)

    }

    @Test
    fun shouldBeAbleToExtractTheFileContentForMainJs() {
        val extractor = Extractor()
        val path = "/main.js"
        val content = extractor.extractFileContent(path)

        val actualContent = content.contains(
            "function validateConfigName(){\n" +
                    "    var file_name = document.getElementById(\"fileName\").value\n" +
                    "    var getCheckBox = document.getElementById(\"configCheckBox\").checked\n" +
                    "    if((file_name == \"\" ||  checkIfConfigNameAlreadyExit(file_name)) && getCheckBox){\n" +
                    "        document.getElementById(\"config_name_validation\").style.display = 'block';\n" +
                    "        return 0\n" +
                    "    }\n" +
                    "    return 1\n" +
                    "}"
        )

        assertTrue(actualContent)
    }

    @Test
    fun shouldBeAbleToExtractTheFileContentForMainCss() {
        val extractor = Extractor()
        val path = "/main.css"
        val content = extractor.extractFileContent(path)
        println(content)

        val actualContent = content.contains(
            "body {\n" +
                    "    background: linear-gradient(-45deg, #FFFAFA, #F5F5F5, #FDF5E6, #F0FFF0, #FFFAFA);\n" +
                    "    background-size: 400% 400%;\n" +
                    "    animation: gradient 15s ease infinite;\n" +
                    "    min-height: 100vh;\n" +
                    "\n" +
                    "}"
        )

        assertTrue(actualContent)
    }

    @Test
    fun shouldBeAbleToExtractTheFileContentForIndexHTML() {
        val extractor = Extractor()
        val path = "/index.html"
        val content = extractor.extractFileContent(path)
        println(content)

        val actualContent = content.contains(
            "<div class=\"header\">\n" +
                    "    <div class=\"logo\">\n" +
                    "        <h1>CSV Parser And Validator</h1>\n" +
                    "    </div>\n" +
                    "</div>"
        )

        assertTrue(actualContent)
    }
}