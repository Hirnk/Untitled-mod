package Hirnk.src.world.block.environment

import Hirnk.src.content.UntitledFx
import Hirnk.src.content.UntitledItems
import Hirnk.src.entities.special.ItemType
import arc.Core
import arc.graphics.Blending
import arc.graphics.g2d.Draw
import arc.math.Mathf
import mindustry.gen.Building
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.Block
import mindustry.world.Tile
import plumy.core.assets.TR

class PulseCrystal(name: String) : Block(name) {
    var staticColor = Pal.lancerLaser
    var staticAlpha = 0.4f
    var rotationRand = 20f
    var shadowOffset = -2.2f
    var shadowAlpha: Float = 0.4f
    var staticSize = 2.5f
    var outputRate = 160f
    var pulseFx = UntitledFx.crystalPulse
    var item = UntitledItems.duitium
    var amount = 3

    var itemType = ItemType()

    init {
        solid = true
        clipSize = 90f
        update = true
        customShadow = true
        destructible = false
    }

    lateinit var staticRegions: Array<TR>

    override fun load() {
        super.load()
        staticRegions = Array(variants) {
            Core.atlas.find("$name-static${it + 1}")
        }
    }

    override fun drawShadow(tile: Tile) {
    }

    inner class PulseCrystalBuild : Building() {
        var progress = 0f

        override fun update() {
            progress += delta() / outputRate
            if (progress >= 1f) {
                progress %= 1f
                pulseFx.at(x, y, 0f, staticColor, staticRegions[
                        Mathf.randomSeed(tile.pos().toLong(), 0, variantShadowRegions.size - 1)
                    ]
                )
                for (i in 0 until amount) {
                    itemType.create(x, y, item, 1, Mathf.random(360f))
                }
            }
        }

        override fun draw() {
            val seed = Mathf.randomSeed(tile.pos().toLong(), 0, variantShadowRegions.size - 1)
            val rot = Mathf.randomSeedRange(tile.pos().toLong(), rotationRand)

            Draw.z(Layer.power - 1f)
            Draw.color(0f, 0f, 0f, shadowAlpha)
            Draw.rect(variantShadowRegions[seed], x + shadowOffset, y + shadowOffset, rot)
            Draw.color()
            Draw.z(Layer.power + 1f)
            Draw.rect(variantRegions[seed], x, y, rot)
            Draw.blend(Blending.additive)
            Draw.color(staticColor, progress * staticAlpha)
            Draw.rect(staticRegions[seed], x + Mathf.range(staticSize) * progress, y + Mathf.range(staticSize) * progress, rot)
            Draw.color()
            Draw.blend()
        }
    }
}