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
}
