package com.example

import com.example.ui.util.AppStrings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testBangladeshMobileValidation() {
        val validBkash = "01712345678"
        val validNagad = "01987654321"
        val invalidShort = "01712345"
        val invalidPrefix = "02712345678"

        val regex = Regex("^01[3-9]\\d{8}$")
        assertTrue(validBkash.matches(regex))
        assertTrue(validNagad.matches(regex))
        assertFalse(invalidShort.matches(regex))
        assertFalse(invalidPrefix.matches(regex))
    }

    @Test
    fun testMinimumWithdrawalAmount() {
        val minLimit = 50.0
        val requestedSmall = 30.0
        val requestedValid = 150.0

        assertTrue(requestedValid >= minLimit)
        assertFalse(requestedSmall >= minLimit)
    }

    @Test
    fun testBilingualStrings() {
        val titleBn = AppStrings.t("app_title", isBn = true)
        val titleEn = AppStrings.t("app_title", isBn = false)

        assertEquals("Alamin Global Earn", titleBn)
        assertEquals("Alamin Global Earn", titleEn)

        val withdrawBn = AppStrings.t("withdraw_now_btn", isBn = true)
        val withdrawEn = AppStrings.t("withdraw_now_btn", isBn = false)

        assertTrue(withdrawBn.contains("উত্তোলন"))
        assertTrue(withdrawEn.contains("Withdrawal"))
    }
}
