package hirnk.src.world.block

import arc.math.geom.Geometry
import hirnk.src.content.UntiltedBlocks.copperTankBig
import mindustry.Vars
import mindustry.world.blocks.liquid.LiquidRouter

//hardcoded
class LiquidCell(name: String) : LiquidRouter(name) {
    var build = copperTankBig

    inner class LiquidCellBuild : LiquidRouterBuild() {
        override fun placed() {
            super.placed()
            for (i in 0..3) {
                if (check(i)) Vars.world.tile(
                    Geometry.d4x[i]+size/2,
                    Geometry.d4y[i]+size/2
                ).setBlock(build)
            }
        }

        fun check(rot: Int): Boolean {
            for (i in rot..rot+2) {
                if (nearby(Geometry.d4x[i] * size, Geometry.d4y[i] * size).block != this@LiquidCell) return false
            }
            return true
        }
    }
}