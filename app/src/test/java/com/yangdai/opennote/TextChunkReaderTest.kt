package com.yangdai.opennote

import com.yangdai.opennote.presentation.util.readTextInChunks
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.StringReader

class TextChunkReaderTest {

    @Test
    fun largeTextIsReassembledWithoutDataLoss() {
        val source = buildString(180_000) {
            repeat(30_000) { index ->
                append(index % 10)
                append("行\n")
            }
        }
        var chunks = 0

        val chunkSize = 16 * 1024
        val result = StringReader(source).readTextInChunks(chunkSize) {
            chunks++
        }

        assertEquals(source, result)
        assertEquals((source.length + chunkSize - 1) / chunkSize, chunks)
    }

    @Test
    fun oversizedTextIsRejectedBeforeFullAllocation() {
        try {
            StringReader("a".repeat(20_000)).readTextInChunks(
                chunkSize = 1_024,
                maxChars = 8_000
            )
            fail("Expected oversized text to be rejected")
        } catch (_: IllegalStateException) {
            // Expected.
        }
    }
}
