package Hirnk.src.gen

import arc.Core
import arc.graphics.Pixmap
import arc.math.Rand
import arc.struct.ObjectMap
import arc.struct.Seq
import mindustry.content.Items
import mindustry.type.Item

object OreItemGen {
    val rand = Rand()
    val templates = 1
    val alpha = 0.5f

    val list = Seq(arrayOf(
        Items.copper,
        Items.lead,
        Items.titanium,
        Items.thorium
    ))

    val map = ObjectMap<Item, Item>()

    fun load() {
        generate()
        generateTexture()
    }

    fun generate() {
        list.forEach {
            map.put(it, Item("ore-${it.name}").apply {
                hardness = it.hardness
                cost = it.cost
                explosiveness = it.explosiveness
                radioactivity = it.radioactivity
                healthScaling = it.healthScaling
            })
        }
    }

    fun generateTexture() {
        val bases = Array<Pixmap>(templates) {
            return@Array Core.atlas.getPixmap("ore-template$it").pixmap
        }
        list.forEach {
        }
    }
}