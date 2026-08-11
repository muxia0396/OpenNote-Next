package com.yangdai.opennote

import com.yangdai.opennote.domain.usecase.NoteOrder
import com.yangdai.opennote.domain.usecase.OrderType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NoteOrderPreferenceTest {

    @Test
    fun `all note orders survive preference round trip`() {
        val orders = listOf(
            NoteOrder.Title(OrderType.Ascending),
            NoteOrder.Title(OrderType.Descending),
            NoteOrder.Created(OrderType.Ascending),
            NoteOrder.Created(OrderType.Descending),
            NoteOrder.Modified(OrderType.Ascending),
            NoteOrder.Modified(OrderType.Descending)
        )

        orders.forEach { original ->
            val restored = NoteOrder.fromPreferenceValue(original.toPreferenceValue())
            assertEquals(original::class, restored::class)
            assertEquals(original.orderType, restored.orderType)
        }
    }

    @Test
    fun `invalid preference falls back to modified descending`() {
        listOf(null, "", "created", "unknown:ascending", "title:sideways").forEach { value ->
            val restored = NoteOrder.fromPreferenceValue(value)
            assertTrue(restored is NoteOrder.Modified)
            assertEquals(OrderType.Descending, restored.orderType)
        }
    }
}
