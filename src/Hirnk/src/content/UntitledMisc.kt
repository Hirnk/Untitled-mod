package Hirnk.src.content

import Hirnk.src.entities.special.GridPacketType
import Hirnk.src.entities.special.ItemType

object UntitledMisc {
    lateinit var basicPacket: GridPacketType
    lateinit var basicItem: ItemType

    fun load() {
        basicItem = ItemType()
        basicPacket = GridPacketType("grid-packet")
    }
}