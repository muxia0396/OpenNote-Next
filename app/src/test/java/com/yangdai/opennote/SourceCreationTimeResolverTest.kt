package com.yangdai.opennote

import com.yangdai.opennote.presentation.viewmodel.SourceCreationTimeResolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class SourceCreationTimeResolverTest {

    @Test
    fun `media store date added is not treated as file creation time`() {
        assertFalse("date_added" in SourceCreationTimeResolver.trustedProviderColumns)
    }

    @Test
    fun `provider timestamps are normalized to milliseconds`() {
        assertEquals(
            1_723_456_789_000L,
            SourceCreationTimeResolver.normalizeProviderTimestamp(1_723_456_789L)
        )
        assertEquals(
            1_723_456_789_123L,
            SourceCreationTimeResolver.normalizeProviderTimestamp(1_723_456_789_123L)
        )
        assertNull(SourceCreationTimeResolver.normalizeProviderTimestamp(0L))
    }
}
