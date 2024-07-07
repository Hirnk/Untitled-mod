package Hirnk.src.world.block.environment

import arc.Core
import arc.graphics.Blending
import arc.graphics.g2d.Draw
import mindustry.gen.Building
import mindustry.graphics.Pal
import mindustry.world.Block
import mindustry.world.Tile
import plumy.core.assets.TR

class PulseCrystal(name: String) : Block(name) {
    var glowColor = Pal.lancerLaser

    init {
        solid = true
        clipSize = 90f
    }

    lateinit var glowRegions: Array<TR>

    override fun load() {
        super.load()
        glowRegions = Array(variants) {
            Core.atlas.find("$name-glow$it")
        }
    }

    inner class PulseCrystalBuild : Building() {
        var progress = 0f

        override fun update() {
            progress += delta()
            if (progress >= 1f) {
                progress %= 1f
            }
        }
    }
}