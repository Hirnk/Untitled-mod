package Hirnk.src.world.block.distribution.fluid

import Hirnk.src.util.sheet
import arc.func.Prov
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import mindustry.gen.Building
import mindustry.world.Block

//todo fix silly code
class GasPipe(name: String) : Block(name) {
    var tileOffset = 3

    lateinit var regions: Array<TextureRegion>

    override fun load() {
        super.load()
        regions = "$name-tile".sheet(8, 3)
    }

    init {
        update = true
        solid = true
        hasLiquids = true

        buildType = Prov { GasPipeBuild() }
    }

    inner class GasPipeBuild : Building(), GasBuild {
        var index = 0

        fun updateIndex() {
            var newIndex = 0

            for (i in 0 until 4) {
                val b = nearby((4 - i) % 4)
                if (b is GasBuild) newIndex += 1 shl i

            }

            if (newIndex != index) {
                index = newIndex

                for (i in 0 until 4) {
                    val b = nearby((4 - i) % 4)
                    if (b is GasPipeBuild && b.isStraight()) b.updateIndex()
                }
            }

            updateIndexStraight()
        }

        fun updateIndexStraight() {
            val o = if (index == 5) 0 else if (index == 10) 1 else return

            var sIndex = 0

            for (i in 0 until 2) {
                val b = nearby((4 - (i * 2 + o)) % 4)
                if (b is GasPipeBuild && !b.isStraight()) sIndex += 1 shl i
            }

            if (sIndex != 0) when (index) {
                5 -> index = 16 + sIndex
                10 -> index = 20 + sIndex
            }
        }

        fun isStraight() = index == 5 || index == 10 || index >= 16

        override fun onProximityUpdate() {
            super.onProximityUpdate()
            updateIndex()
        }

        override fun draw() {
            Draw.rect(regions[index], x, y)
        }
    }
}