package Hirnk.src.gen

import Hirnk.src.util.graphic.HSVLayerProcessor
import Hirnk.src.util.steam.Res
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

    val rawOre = ObjectMap<Item, Item>()
    val orePulver = ObjectMap<Item, Item>()

    fun load() {
        list.forEach {
            rawOre.put(it, RawOre(it))
            orePulver.put(it, OrePulver(it))
        }
    }

    object OreIconGenerator {
        val rand = Rand()
        val oreTemplates = 1
        val orePulverTemplates = 1
        val alpha = 0.4f
        val saturation = 15
        val bakery = StackIconBakery(32, 32).apply {
            postProcessors.add(AntiAliasingLayerProcessor)
        }
        val oreBase = ArrayList<Pixmap>()
        val pulverBase = ArrayList<Pixmap>()

        fun ore(i: Int) = "/sprites/misc/ore-template$i.png"
        fun pulver(i: Int) = "/sprites/misc/ore-pulver-template$i.png"
        fun loadPixmap(internalName: String) = Res.load(name = internalName).use { it.readAsPixmap() }

        fun load() {
            for (i in 0 until oreTemplates) oreBase += loadPixmap(ore(i))
            for (i in 0 until orePulverTemplates) pulverBase += loadPixmap(pulver(i))
        }

        fun generateOre(ore: Item): TextureRegion {
            rand.setSeed(ore.id.toLong())
            val l = rand.random(oreTemplates - 1)
            val layer = Layer(oreBase[l].toLayerBuffer()) {
                +TintBlendLayerProcessor(ore.color.cpy().a(alpha))
                +HSVLayerProcessor(s = saturation)
            }
            val pLayer = bakery.bake(layer)
            return TextureRegion(Texture(pLayer.createPixmap()))
        }
        fun generatePulver(pulver: Item): TextureRegion {
            rand.setSeed(pulver.id.toLong())
            val l = rand.random(orePulverTemplates - 1)
            val layer = Layer(pulverBase[l].toLayerBuffer()) {
                +TintBlendLayerProcessor(pulver.color.cpy().a(alpha))
                +HSVLayerProcessor(s = saturation)
            }
            val pLayer = bakery.bake(layer)
            return TextureRegion(Texture(pLayer.createPixmap()))
        }
    }

    class RawOre(source: Item) : Item("gen-ore-${source.name}") {
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
            val icon = OreIconGenerator.generateOre(this)
            fullIcon = icon
            uiIcon = icon
        }
    }

    class OrePulver(source: Item) : Item("gen-pulver-${source.name}") {
        init {
            localizedName = "${source.localizedName} ${Core.bundle.get("untitled-pulver")}"
            color = source.color
            flammability = source.flammability
            explosiveness = source.explosiveness
            hardness = source.hardness
            charge = source.charge
            radioactivity = source.radioactivity * 0.8f
            healthScaling = source.healthScaling * 0.8f
            cost = source.cost
        }

        override fun loadIcon() {
            val icon = OreIconGenerator.generatePulver(this)
            fullIcon = icon
            uiIcon = icon
        }
    }
}