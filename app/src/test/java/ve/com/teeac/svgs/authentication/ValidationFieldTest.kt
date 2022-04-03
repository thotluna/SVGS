
package ve.com.teeac.svgs.authentication

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidationFieldTest {

    @Test
    fun notBlank() {
        var error = ValidationField("").notBlank().result()
        assertEquals(1, error.size)
        assertEquals(ValidationField.BLANK, error.first())
        error = ValidationField("Algo").notBlank().result()
        assertEquals(0, error.size)
    }

    @Test
    fun emailValid() {
        val list = listOf(
            "aasd@gmail.com",
            "asdfsd@xcs.net",
            "asdfsd@xcs.com.ve",
            "asdfsd@xcs.co",
            "asdfsd@xcs.sd",
        )
        list.forEach {
            val error = ValidationField(it).emailValid().result()
            assertEquals(0, error.size)
        }
        val listError = listOf(
            "asdasd",
            "@.com",
            "@",
            "asda.@"
        )
        listError.forEach {
            val error = ValidationField(it).emailValid().result()
            assertEquals(1, error.size)
            assertEquals(ValidationField.NOT_EMAIL, error.first())
        }
    }

    @Test
    fun passwordValid() {
        val passwords = HashMap<String, String>()
        passwords["1aA#2345"] = ""
        passwords["11111111"] = ValidationField.PASSWORD_INVALID
        passwords["asdfghjk"] = ValidationField.PASSWORD_INVALID
        passwords["ASDFGHJK"] = ValidationField.PASSWORD_INVALID
        passwords["ADA0 ASD"] = ValidationField.PASSWORD_INVALID
        passwords["1Aa#1234"] = ""

        passwords.forEach { (k, v) ->
            println(k)
            val error = ValidationField(k).passwordValid().result()
            if (v.isNotBlank()) {
                assertEquals(1, error.size)
                assertEquals(v, error.first())
            } else {
                assertEquals(0, error.size)
            }
        }
    }
}
