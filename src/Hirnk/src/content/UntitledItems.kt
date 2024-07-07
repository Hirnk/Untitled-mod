package Hirnk.src.content

import arc.graphics.Color
import mindustry.type.Item

object UntitledItems {
    lateinit var duitium: Item
    lateinit var fluxoglass: Item

    fun load() {
        duitium = Item("duitium", Color.valueOf("a8dcdc")).apply {
            cost = 0.4f
        }
        fluxoglass = Item("fluxoglass", Color.valueOf("d1daff")).apply {
            cost = 1.2f
        }
    }
}