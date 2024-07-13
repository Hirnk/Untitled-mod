package Hirnk.src.world.block.environment

import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import mindustry.gen.Building
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.Block
import mindustry.world.Tile
import plumy.core.assets.TR

class PulseCrystal(name: String) : Block(name) {
    var glowColor = Pal.lancerLaser
    var rotationRand = 20f
    var shadowOffset = -3f
    var shadowAlpha: Float = 0.6f

    init {
        solid = true
        clipSize = 90f
        update = true
        customShadow = true
        destructible = false
    }

    lateinit var glowRegions: Array<TR>

    override fun load() {
        super.load()
        glowRegions = Array(variants) {
            Core.atlas.find("$name-glow$it")
        }
    }

    override fun drawShadow(tile: Tile) {
    }

    inner class PulseCrystalBuild : Building() {
        var progress = 0f

        override fun update() {
            progress += delta()
            if (progress >= 1f) {
                progress %= 1f
            }
        }

        override fun draw() {
            val rot = Mathf.randomSeedRange(tile.pos().toLong(), rotationRand)
            val seed = Mathf.randomSeed(tile.pos().toLong(), 0, variantShadowRegions.size - 1)

            Draw.z(Layer.power - 1f)
            Draw.color(0f, 0f, 0f, shadowAlpha)
            Draw.rect(variantShadowRegions[seed], x, y, rot)
            Draw.color()
            Draw.z(Layer.power + 1f)
            Draw.rect(variantRegions[seed], x, y, rot)
        }
    }
}