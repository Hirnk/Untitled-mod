package Hirnk.src.gen

import arc.Core
import arc.graphics.Pixmap
import arc.graphics.Texture
import arc.graphics.g2d.TextureRegion
import arc.math.Rand
import arc.struct.ObjectMap
import arc.struct.Seq
import mindustry.content.Items
import mindustry.type.Item
import plumy.texture.*

object OreItemGen {

    val list = Seq(arrayOf(
        Items.copper,
        Items.lead,
        Items.titanium,
        Items.thorium
    ))

    val map = ObjectMap<Item, Item>()

    fun load() {
        list.forEach {
            map.put(it, RawOre(it))
        }
    }

    object OreIconGenerator {
        val rand = Rand()
        val templates = 1
        val alpha = 0.5f
        val bakery = StackIconBakery(32, 32).apply {
            postProcessors.add(AntiAliasingLayerProcessor)
        }

        val bases = Array<Pixmap>(templates) {
            return@Array Core.atlas.getPixmap("ore-template").pixmap
        }

        fun generate(ore: Item): TextureRegion {
            rand.setSeed(ore.id.toLong())
            val l = rand.random(templates - 1)
            val layer = Layer(bases[l].toLayerBuffer()) {
                +TintBlendLayerProcessor(ore.color.cpy().a(alpha))
            }
            val pLayer = bakery.bake(layer)
            return TextureRegion(Texture(pLayer.createPixmap()))
        }
    }

    class RawOre(source: Item) : Item("oregen-${source.name}") {
        init {
            localizedName = "${source.localizedName} ${Core.bundle.get("untitled-ore")}"
            color = source.color
            flammability = source.flammability
            explosiveness = source.explosiveness
            hardness = source.hardness
            charge = source.charge
            radioactivity = source.radioactivity * 0.3f
            healthScaling = source.healthScaling * 0.5f
            cost = source.cost * 0.8f
        }

        override fun loadIcon() {
            val icon = OreIconGenerator.generate(this)
            fullIcon = icon
            uiIcon = icon
        }
    }
}