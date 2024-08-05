package Hirnk.src.content

import arc.graphics.Color
import mindustry.type.Item

object UntitledItems {
    lateinit var duitium: Item
    lateinit var fluxoglass: Item
    lateinit var recycledScrap: Item

    fun load() {
        duitium = Item("duitium", Color.valueOf("a8dcdc")).apply {
            cost = 0.4f
        }
        fluxoglass = Item("fluxoglass", Color.valueOf("d1daff")).apply {
            cost = 1.2f
        }
        recycledScrap = Item("recycled-scrap", Color.valueOf("9a9fb4")).apply {
            cost = 0.2f
        }
    }
}