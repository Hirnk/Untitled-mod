package Hirnk.src.content

import arc.graphics.Color
import mindustry.type.Liquid

object UntiltedFluids {
    lateinit var steam: Liquid

    fun load() {
        steam = Liquid("steam", Color.valueOf("ffffff")).apply {
            gas = true
        }
    }
}