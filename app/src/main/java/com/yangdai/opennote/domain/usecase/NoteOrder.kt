package com.yangdai.opennote.domain.usecase

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Created(orderType: OrderType) : NoteOrder(orderType)
    class Modified(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Created -> Created(orderType)
            is Modified -> Modified(orderType)
        }
    }

    fun toPreferenceValue(): String {
        val field = when (this) {
            is Title -> FIELD_TITLE
            is Created -> FIELD_CREATED
            is Modified -> FIELD_MODIFIED
        }
        val direction = when (orderType) {
            OrderType.Ascending -> DIRECTION_ASCENDING
            OrderType.Descending -> DIRECTION_DESCENDING
        }
        return "$field:$direction"
    }

    companion object {
        private const val FIELD_TITLE = "title"
        private const val FIELD_CREATED = "created"
        private const val FIELD_MODIFIED = "modified"
        private const val DIRECTION_ASCENDING = "ascending"
        private const val DIRECTION_DESCENDING = "descending"

        fun fromPreferenceValue(value: String?): NoteOrder {
            val parts = value?.split(':', limit = 2).orEmpty()
            val orderType = when (parts.getOrNull(1)) {
                DIRECTION_ASCENDING -> OrderType.Ascending
                DIRECTION_DESCENDING -> OrderType.Descending
                else -> return Modified(OrderType.Descending)
            }
            return when (parts.getOrNull(0)) {
                FIELD_TITLE -> Title(orderType)
                FIELD_CREATED -> Created(orderType)
                FIELD_MODIFIED -> Modified(orderType)
                else -> Modified(OrderType.Descending)
            }
        }
    }
}
