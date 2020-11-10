package com.sedsoftware.primitive

import com.sedsoftware.common.primitive.isLeap
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PrimitivesExtTest {

    @Test
    fun `leap year test`() {
        assertTrue { 2020.isLeap }
        assertFalse { 2021.isLeap }
    }
}
