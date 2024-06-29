package Hirnk.src.content

import Hirnk.src.world.block.transportation.GridPacketType

object UntitledPackets {
    lateinit var basic: GridPacketType

    fun load() {
        basic = GridPacketType("grid-packet")
    }
}